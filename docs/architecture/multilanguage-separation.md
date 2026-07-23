# Multilanguage Application Separation

## Purpose

DS-Tool currently presents Java data structures through a Java desktop application. As the project expands into an Android application supporting C/C++, Java, and Python, the user interface must not be duplicated for every programming language.

The application should use one shared data-structure model, one recommendation engine, and one visualization system. Language-specific content should be provided through replaceable language modules.

## Architectural rule

> Data-structure knowledge and visualization describe the concept. Language modules describe how that concept is expressed in C/C++, Java, or Python.

The Android interface consumes both layers but does not contain language-specific decision logic.

## Proposed separation

```text
DS-Tool
├── core
│   ├── model
│   │   ├── DataStructure
│   │   ├── Operation
│   │   ├── Constraint
│   │   └── Complexity
│   ├── recommendation
│   │   ├── FilterEngine
│   │   ├── RankingEngine
│   │   └── RecommendationResult
│   └── visualization
│       ├── VisualizationModel
│       ├── VisualizationStep
│       └── VisualizationController
│
├── languages
│   ├── common
│   │   ├── LanguageId
│   │   ├── LanguageModule
│   │   ├── StructureBinding
│   │   └── CodeExample
│   ├── cpp
│   │   └── CppLanguageModule
│   ├── java
│   │   └── JavaLanguageModule
│   └── python
│       └── PythonLanguageModule
│
├── application
│   ├── SelectLanguageUseCase
│   ├── CompareStructuresUseCase
│   ├── RecommendStructureUseCase
│   └── ViewStructureUseCase
│
├── desktop
│   └── JavaFX user interface
│
└── android
    └── Android user interface
```

These names describe architectural responsibilities. They do not require the current repository to be reorganized immediately.

## 1. Shared core model

The core model contains concepts that remain true in every language:

- Whether a structure stores key-value pairs
- Whether duplicates are supported
- Whether ordering is maintained
- Whether indexed access is available
- Typical lookup, insertion, and removal complexity
- Memory characteristics
- Abstract operations such as insert, remove, search, peek, enqueue, and dequeue

The core must not refer directly to Java classes such as `ArrayList`, `HashMap`, or `PriorityQueue`. It should use language-neutral identifiers such as:

```text
DYNAMIC_ARRAY
LINKED_LIST
STACK
QUEUE
DEQUE
HASH_SET
ORDERED_SET
HASH_MAP
ORDERED_MAP
HEAP
```

This allows the recommendation engine to choose a concept before the selected language maps that concept to an implementation.

## 2. Shared recommendation engine

Filtering and ranking belong to the shared application logic.

For example, a request for:

- key-value storage
- unique keys
- no sorted ordering requirement
- fast average lookup

should produce the abstract recommendation `HASH_MAP`.

Only after that decision should the selected language module translate it:

| Language | Suggested implementation |
|---|---|
| C++ | `std::unordered_map` |
| Java | `HashMap` |
| Python | `dict` |

The recommendation algorithm therefore runs once and is tested once.

## 3. Shared visualization model

Visualizations should represent operations on abstract structures rather than classes from a specific language.

For example, inserting a value into a stack is represented as a shared sequence of visualization steps:

```text
Highlight input value
Move value toward top position
Add value to stack model
Update size
```

Both the JavaFX desktop interface and Android interface can render those steps using their own UI frameworks.

This separation avoids embedding JavaFX nodes, Android views, or Jetpack Compose components in the core visualization logic.

## 4. Language modules

Each supported language implements the same language-module contract.

Conceptual interface:

```java
public interface LanguageModule {
    LanguageId id();
    String displayName();
    StructureBinding bindingFor(DataStructureId structureId);
    CodeExample exampleFor(DataStructureId structureId, Operation operation);
}
```

A `StructureBinding` supplies language-specific information such as:

- Type or class name
- Required import, include, or package
- Construction syntax
- Common methods or functions
- Important language-specific behavior
- Code examples
- Links or references to official documentation

The module does not control screens, navigation, filtering, ranking, or visualization rendering.

## 5. Language selection

The selected language is application state, not a separate version of the app.

```text
User selects language
        ↓
Application stores LanguageId
        ↓
Core recommends an abstract structure
        ↓
Selected LanguageModule supplies names and code
        ↓
The same screen renders the result
```

Changing the language should refresh language-specific labels and examples while preserving the user's current filters, selected structure, theme, and navigation position.

Initial language identifiers:

```text
CPP
JAVA
PYTHON
```

The user-visible label for `CPP` may be `C/C++`. Internally, C and C++ may later become separate modules if the application begins presenting their substantially different standard-library capabilities.

## 6. Application use cases

Application-level use cases coordinate the shared core and the active language module.

Examples:

### SelectLanguageUseCase

- Validates the requested language
- Stores the selection
- Loads the corresponding module
- Notifies the interface that language-specific content changed

### RecommendStructureUseCase

- Accepts user constraints
- Calls the shared recommendation engine
- Obtains the selected language's binding for every result
- Returns display-ready recommendation data

### ViewStructureUseCase

- Loads the shared conceptual explanation
- Loads the shared visualization definition
- Loads language-specific API details and examples

The user interface should call these use cases instead of reading language files directly.

## 7. Desktop and Android interfaces

The current JavaFX desktop interface and the future Android interface should be presentation layers over the same application behavior.

### Shared

- Data-structure definitions
- Constraints and capabilities
- Recommendation and ranking logic
- Complexity information
- Visualization state and operation sequences
- Language-module contracts
- C/C++, Java, and Python mappings

### Platform-specific

- JavaFX controls, stages, scenes, and CSS
- Android activities, navigation, Jetpack Compose components, and resources
- Window and device adaptation
- Platform storage implementation
- Installation and packaging

The Android project should therefore not be created by copying the complete JavaFX application and replacing each screen independently.

## 8. Suggested content format

Most language mappings and examples can eventually be stored as structured data rather than hard-coded screen content.

Example:

```json
{
  "structureId": "DYNAMIC_ARRAY",
  "language": "PYTHON",
  "displayName": "list",
  "construction": "values = []",
  "operations": {
    "insertEnd": "values.append(value)",
    "removeEnd": "values.pop()",
    "indexedAccess": "values[index]"
  }
}
```

Code may still be used when a module requires validation, formatting, or behavior that structured content cannot express cleanly.

## 9. Testing boundaries

The separation creates clear test groups:

### Core tests

- Filtering produces correct candidates
- Ranking produces correct ordering
- Complexities and capabilities are consistent
- Visual operation steps are correct

### Language-module contract tests

- Every supported abstract structure has an expected mapping
- Every displayed operation has an example
- Required imports or includes are present
- No module returns content for the wrong language

### Presentation tests

- Language selection updates displayed code
- Changing languages preserves filters and selected structure
- JavaFX and Android render equivalent recommendations
- Unsupported mappings are explained instead of crashing

## 10. Migration path

The existing application can be migrated incrementally.

1. Introduce language-neutral structure identifiers.
2. Move Java-specific names and examples behind a `JavaLanguageModule`.
3. Keep the current JavaFX screens using the new application interfaces.
4. Extract recommendation and ranking logic from JavaFX controllers.
5. Extract visualization state from JavaFX nodes.
6. Add C/C++ and Python modules.
7. Build the Android presentation layer over the extracted application layer.
8. Add persistent language selection and Android packaging.

During the first migration steps, Java remains the default language so existing behavior can be preserved while the boundaries are introduced.

## Result

This architecture produces one DS-Tool application with multiple language perspectives, rather than three applications that duplicate screens, recommendation logic, visualizations, fixes, and new features.

Adding another language later should primarily require a new `LanguageModule` and its content, not another user interface.