# ADR: Second subject

- Status: Accepted for Milestone 7 implementation
- Date: 2026-07-25

## Context

Milestone 7 must prove that the accepted subject-provider boundary works for a
second functional subject without changing the language-neutral Knowledge Core
or recommendation logic. C is the roadmap's selected subject.

C does not provide a standardized collections framework comparable to Java's.
Its initial representation names must therefore describe educational
implementation strategies rather than imply library classes or APIs.

## Decision

`CSubjectProvider` implements the existing `SubjectProvider` contract and maps
every current `StructureId` to a small C-oriented name such as dynamic array,
binary heap, circular-buffer queue, hash table, or balanced-tree map.

The provider contains no capabilities, complexity claims, recommendation
rules, conceptual descriptions, or UI behavior. `KnowledgeCatalog` remains the
only conceptual source, and `RecommendationService` remains independent of
every subject provider.

The Java and C providers both cover all nine current Knowledge Core concepts.
C++ and Python remain unavailable providers.

## Presentation

Advisor retains its established Java explanation resources. For C, Advisor
renders a compact concept-first view composed from the Knowledge Core and the C
representation name. The view identifies the name as an implementation
strategy rather than a standardized library type.

Explorer continues composing Knowledge Core content with the active provider.
Flash Cards remain concept-first and show the provider representation only as
supplementary revealed content.

Shared diagrams are language-neutral conceptual assets. Presentation resolves
their primary and optional views from `StructureId`, so subject display names
and provider representation names are never resource identifiers.

## Subject switching

Subject selection prepares the currently visible experience with the requested
provider before changing `ApplicationState.activeSubject`. If presentation
raises a `RuntimeException`, the exception reaches `UiActionGuard` and the
previous subject and all unrelated state remain unchanged.

After successful rendering, only the active subject changes. The selected
abstract structure, recommendation answers and weights, theme, locale,
accessibility preferences, Advisor navigation, active experience, Flash Cards
session, and Learn progress are preserved. Hidden or lazily created
experiences resolve the committed provider when next activated.

## Consequences

- The provider contract is proven by Java and C implementations.
- The same abstract recommendation and selection survive subject changes.
- C presentation remains deliberately small and does not claim a standard
  collections library.
- No conceptual knowledge is duplicated in the C provider.
- The current single-module desktop architecture remains in place.

## Milestone boundary

Timed Quiz, persistence, C compilation, detailed C code examples, C++ and
Python providers, new Knowledge Core content, Explorer tabs, and unrelated
refactoring are outside Milestone 7. Physical module separation and Android
belong to a future successor application.

## Validation

Headless tests cover provider completeness, fresh representations,
Knowledge Core composition, requested-provider rendering, render-before-commit
ordering, failure rollback, state preservation, shared diagram resolution, and
concept-first C presentation. Visible menus, repeated switching, themes,
experience restoration, dialogs, scaling, and failure recovery remain in the
desktop manual acceptance checklist.
