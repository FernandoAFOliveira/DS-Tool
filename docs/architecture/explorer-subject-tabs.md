# ADR: Explorer subject tabs and code examples

- Status: Accepted for Milestone 8 implementation
- Date: 2026-07-25

## Context

Milestones 4–7 separated language-neutral knowledge from Java and C
representations. Explorer consequently retained strong conceptual material but
offered little implementation-oriented guidance. Milestone 8 restores that
guidance without moving language-specific information into the Knowledge Core
or making Explorer depend on one active subject.

## Decision

The lower Explorer detail area uses an Overview-first tab set. **Overview** is
always first. Each enabled `SubjectProvider` contributes one subsequent tab in
the stable order supplied by `SubjectProviderRegistry`. Disabled providers do
not appear, and Explorer contains no Java- or C-specific tab construction.

Overview is composed only from `DataStructureKnowledge` and related
language-neutral knowledge. It has no active-subject field. The active subject
continues to supply the left Explorer representation list.

`SubjectProvider` may supply immutable `SubjectStructureContent` for a
`StructureId`. The deliberately small content model contains ordered titled
sections with plain prose or bullets and zero or more titled plain-text code
examples. It contains no GUI types, HTML, or resource paths. Providers must not
repeat conceptual definitions, general complexity, strengths, weaknesses, use
cases, or relationships.

Java and C supply content for all nine current concepts. C describes common
implementation strategies and ownership choices rather than a standardized
collection framework.

The abstract diagram remains outside the tab set and continues resolving from
`StructureId`.

## State and failure behavior

The selected tab is local Swing presentation state. It is preserved across
structure selection, theme refresh, experience switching, and active-subject
switching. If a previously selected provider tab disappears, selection falls
back to Overview. No tab interaction changes `ApplicationState.activeSubject`.

Explorer composes and renders the complete detail page before committing a new
selected `StructureId`. Runtime presentation failures continue through
`UiActionGuard`, retain the prior committed selection, show only the standard
generic message, and leave the application usable.

## Consequences

- Knowledge Core structure and content remain unchanged.
- Advisor and its existing Java explanation resources remain unchanged.
- Explorer can add a future enabled subject tab without UI changes.
- Provider content remains suitable for later physical extraction because it
  is platform-neutral.
- Java material remains duplicated between legacy Advisor resources and the
  new Explorer provider content until a separately approved consolidation
  milestone.

## Validation

Automated tests cover enabled-provider discovery, exact and synthetic tab
ordering, disabled-provider exclusion, provider coverage, neutral Overview
composition, escaping, local tab preservation and fallback, shared diagram
ownership, and selection failure rollback. Visible themes, scaling, keyboard
operation, code readability, diagrams, and controlled dialog recovery remain
manual desktop acceptance work.
