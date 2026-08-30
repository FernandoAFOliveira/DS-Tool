# ADR: Explorer experience

- Status: Accepted for Milestone 5 implementation
- Date: 2026-07-24

## Context

Milestone 5 adds the first experience alongside Advisor. It must consume the
Milestone 4 Knowledge Core, preserve shared application state when experiences
change, and remain inside the resilient UI action boundary.

Explorer is an educational catalog browser. It is not another view of the
Advisor's current recommendations, so Advisor answers and hard filters must not
reduce the structures available to browse.

## Decision

`ExplorerService` combines immutable `DataStructureKnowledge` with the active
`SubjectProvider` representation. It enumerates the complete Knowledge Core in
its stable catalog order and resolves:

- conceptual description;
- strengths and weaknesses;
- supported operations;
- lookup and insertion/removal complexity;
- memory and iteration characteristics;
- common use cases;
- related abstract structures; and
- active-subject names and diagram identities.

Explorer adds no second knowledge catalog. The core records now contain the
educational description, strengths, weaknesses, and operations required by the
roadmap; existing core capabilities, ratings, costs, use cases, and
relationships remain the source used across experiences.

`ExplorerController` depends on an `ExplorerView` boundary and contains no Swing
or JavaFX types. `ExplorerPanel` composes the existing structure list and
diagram infrastructure with a new details renderer. The desktop creates this
composition lazily on the first guarded Explorer action.

## Shared state and navigation

`ApplicationState.selectedStructureId` is shared by Advisor and Explorer.
Selecting a structure in Explorer updates that identifier only after content
has rendered successfully.

Selection identity and Advisor navigation are separate state transitions:

- Advisor selection sets the shared identifier and navigates Advisor to the
  structure page.
- Explorer selection sets the shared identifier without changing the saved
  Advisor page.
- Entering Explorer restores and highlights the shared selection; with no
  selection it shows Explorer welcome content.
- Returning to Advisor restores its answers, weights, subject, appearance,
  preferences, and navigation. The Advisor list highlights the shared
  selection only when it is eligible under the current answers.
- Subsequent Advisor answer changes retain their established behavior,
  including clearing a selection that becomes ineligible.

Explorer never invokes `RecommendationService`, reads recommendation answers,
or changes Advisor filtering and ranking.

## Failure boundary

Experience switching, lazy Explorer creation, structure selection, theme
changes, reset, and the existing menu actions enter through `UiActionGuard`.
A feature-level runtime failure is logged and produces only the standard
generic error. Explorer renders before committing a new selection, preventing
a failed presentation from leaving partially updated shared state.

## Consequences

- The Java catalog remains exactly the accepted nine structures, with no
  `LinkedList`.
- Existing Java representation classes, `DataStructureLibrary`, providers,
  explanations, and diagrams remain in use.
- Explorer and Advisor can show different lists without duplicating knowledge
  or recommendation rules.
- The single desktop state instance survives experience card changes.
- Explorer content is testable without constructing a visible desktop.

## Milestone boundary

Learn activities, additional enabled subjects, persistence across launches,
authoring or editing knowledge, new recommendation filters, new structures,
and shared visualization extraction are not part of Milestone 5. Physical
module separation and Android belong to a future successor application.

## Future tab direction

A future Explorer refinement will separate language-neutral content into an
**Overview** tab and subject-specific content into a subject tab such as
**Java**. Additional subject tabs will appear only when their providers become
functional. This direction does not change the current Explorer layout or the
Knowledge Core and subject-provider boundaries.

Milestone 8 implements this direction. The follow-up decision is recorded in
`explorer-subject-tabs.md`.

## Validation

Automated tests cover full-catalog enumeration, core/provider content
composition, welcome and selection restoration, navigation and answer
preservation, render-before-commit failure behavior, and required detail
sections. Visible switching, diagrams, themes, accessibility, and recovery
remain in the desktop manual acceptance checklist.
