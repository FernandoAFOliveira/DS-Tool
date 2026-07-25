# ADR: Subject provider boundary

- Status: Accepted for Milestone 3 implementation
- Date: 2026-07-24

## Context

The desktop Advisor obtains Java structures directly from a static library, while
the Subject menu changes application state independently of the data source.
Milestone 3 requires one documented provider boundary, a functional Java
provider, safe C, C++, and Python placeholders, and preservation of unrelated
application state. Milestone 4, not this milestone, owns the extraction of
language-neutral structure identifiers and the Knowledge Core.

## Decision

A platform-neutral `SubjectProvider` contract supplies a stable `SubjectId`,
display name, availability, and fresh subject-specific structure
representations. The contract contains no Swing or JavaFX behavior.

The desktop composition root registers:

- `JavaSubjectProvider`, which delegates to the existing Java structure library;
- one generic `UnavailableSubjectProvider` instance for each of C, C++, and
  Python.

`SubjectProviderRegistry` validates unique provider identifiers and resolves
providers for the controller. `AppController` coordinates selection directly:
it updates `ApplicationState` only when the requested provider is enabled.
The Swing menu converts an unavailable selection to the existing
`UiActionGuard.showNotEnabled` behavior. A separate subject-selection service
is unnecessary for this milestone.

Advisor filtering and structure lookup now obtain candidates from the active
provider rather than importing `DataStructureLibrary` directly.

## State preservation

Selecting Java changes only the active subject. Selecting an unavailable subject
does not change the active subject. Neither path resets recommendation answers,
ranking weights, selected structure, appearance, navigation, locale, or
accessibility preferences.

Java remains the default and only enabled subject, so existing Java content,
diagrams, explanations, scoring, and menu wording remain unchanged.

## Milestone boundary

The provider returns the existing `DataStructure` model. That model still
combines recommendation attributes with Java representation names, and
`ApplicationState` still uses a Java name as its transitional selection key.
Introducing abstract structure identifiers, mappings, code examples, or a
Knowledge Core is deferred to Milestone 4.

The repository remains a single Maven module. Physical subject modules are
outside the current desktop proof-of-concept roadmap.

## Consequences

### Positive

- The controller no longer selects recommendation candidates from a Java static
  library.
- Provider and selection behavior can be tested without a visible desktop.
- Planned subjects are explicit providers with uniform unavailable behavior.
- Subject changes preserve state they do not own.

### Trade-offs

- Java-specific educational prose and resources remain in their existing
  locations until the Knowledge Core separates concepts from representations.
- The provider contract is intentionally transitional and may evolve when
  abstract structure identifiers are introduced.
- Only Java can become active in this milestone.

## Validation

Automated tests cover provider registration, the exact Java catalog, fresh
provider results, placeholder availability, invalid registry configuration,
successful Java selection, and preservation after an unavailable selection.
The desktop checklist covers visible dialogs, continued usability, Java catalog
regression, and preservation of Advisor state.
