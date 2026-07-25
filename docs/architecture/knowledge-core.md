# ADR: Knowledge Core

- Status: Accepted for Milestone 4 implementation
- Date: 2026-07-24

## Context

After Milestone 3, the Advisor obtained Java `DataStructure` objects through a
subject provider, but those objects still contained the capabilities and
ratings used to make recommendations. `ApplicationState` also identified the
selection by a Java class name. The provider boundary therefore controlled the
catalog source without yet separating universal data-structure knowledge from
Java representation details.

Milestone 4 requires the Advisor to recommend an abstract structure first and
then display the active subject's representation. It must preserve the accepted
nine-item Java catalog and all Milestones 1–3 behavior.

## Decision

The platform-neutral Knowledge Core consists of:

- `StructureId`, the stable identity of an abstract data-structure concept;
- immutable `DataStructureKnowledge` records; and
- `KnowledgeCatalog`, the built-in catalog used by the Advisor.

Each knowledge record owns indexed access, key-value behavior, duplicate
handling, ordering, removal behavior, lookup and insertion/removal cost,
memory considerations, iteration characteristics, common use cases,
relationships, and the numeric ratings needed to preserve existing Advisor
ranking.

`RecommendationService` filters and ranks only these abstract records.
`ScoringEngine` has no subject-provider or Java-representation dependency.
The controller asks the active provider for a representation only after the
abstract recommendation list has been produced.

`ApplicationState` stores the selected `StructureId`. Java class names and
resource keys therefore cannot become cross-subject selection identity.

## Java representation layer

The existing `DataStructure`, `DataStructureLibrary`, and concrete `DS...`
classes remain the Java representation layer. Recommendation attributes were
removed as representation-owned data; each representation now supplies:

- its abstract `StructureId`;
- its Java display name; and
- Java-specific presentation metadata already used by the desktop, such as
  the legacy status of `Stack`.

The former public capability getters and mutable comparison-score methods
remain as deprecated compatibility adapters. Capability getters delegate to
the Knowledge Core, so they do not create a second source of truth. The Advisor
does not use these adapters.

`SubjectProvider.getRepresentation(StructureId)` resolves a representation.
Its default implementation searches the provider's established representation
list, which preserves the Milestone 3 provider API and fresh-instance behavior.

The Java catalog remains exactly:

- `ArrayList`
- `Stack`
- `Queue`
- `PriorityQueue`
- `ArrayDeque`
- `HashSet`
- `TreeSet`
- `HashMap`
- `TreeMap`

`LinkedList` is not introduced in Milestone 4. Expanding or rebalancing the
recommendation catalog requires a separate approved change.

## Ranking compatibility

The Knowledge Core retains the existing 0–10 relative ratings and the current
hard filters. They are recommendation heuristics, not formal complexity
measurements. Explicit complexity descriptions are stored separately.

Recommendations sort by descending score and then by the abstract display
name. The chosen names preserve the existing Java ordering for all current
ties, so the visible default list does not change.

## Failure and state behavior

An enabled provider may omit a representation for a concept it does not
support. The desktop omits that result instead of exposing an internal mapping
failure to the user. Contract tests require the Java provider to cover the
entire current Knowledge Core.

Unavailable subjects remain disabled and continue using the standard
not-enabled action. Subject selection, theme changes, navigation, and list
refreshes preserve the selected abstract identifier and all unrelated state.

## Consequences

### Positive

- Recommendation logic is independent of Java class names.
- The selected concept can survive a future change of active subject.
- Java presentation behavior and resources remain intact.
- Knowledge is immutable and can be consumed by future experiences.
- Ranking and filtering are testable without Swing, JavaFX, or a provider.

### Trade-offs

- The repository remains a single Maven module.
- Java explanation pages still combine conceptual prose with Java API and code
  examples; they are treated as Java representation content.
- The numeric ratings remain subjective compatibility inputs.
- Only Java has complete representation coverage.

## Milestone boundary

Explorer, Learn activities, a second enabled subject, structured multi-language
code examples, physical module separation, shared visualization extraction,
and Android remain assigned to later roadmap milestones.

## Validation

Automated tests cover catalog completeness and immutability, language-neutral
identifiers, established scores and filters, default order, Java mapping,
state preservation, and silent UI selection restoration. The desktop checklist
covers visible Java names, diagrams, explanations, state preservation,
unavailable subjects, and post-failure usability.
