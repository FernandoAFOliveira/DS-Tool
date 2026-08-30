# ADR: Application state foundation

- Status: Accepted for Milestone 2 implementation
- Date: 2026-07-24

## Context

Advisor state was divided among `AppController` and several Swing panels. Answers, theme, selected recommendation, and displayed content therefore had different owners. Rebuilding a recommendation list also discarded its Swing selection even when the same recommendation remained valid. This does not provide the state-preservation boundary required before adding subjects and experiences.

Milestone 2 must establish that boundary without introducing the subject-provider and knowledge-core contracts planned for later milestones.

## Decision

`ApplicationState` is the in-memory owner of:

- the active subject and experience;
- recommendation answers and ranking weights;
- the selected data-structure name;
- appearance;
- current Advisor navigation;
- accessibility preferences; and
- locale.

The desktop composition root creates one state instance. Swing events enter through `AppController`, which updates the state and then renders panels from it. Panels retain only control and rendering details. Programmatic rendering does not emit user-change events.

The current state uses the Java data-structure name as a stable transitional selection key. It does not introduce a language-neutral structure identifier or move Java knowledge into the application layer. That separation remains Milestone 4 work.

`ApplicationState` returns a copy of `DSRequirements`, preventing callers from mutating recommendation answers outside its validated update methods.

## Reset semantics

**File > Reset selections** clears only Advisor session state:

- recommendation answers and weights return to defaults;
- selected recommendation is cleared; and
- navigation returns to welcome.

It preserves active subject, active experience, appearance, locale, and accessibility preferences.

If changed answers make the selected recommendation ineligible, the selection is cleared and the next navigation event displays welcome or question-help content instead of retaining stale structure content. If it remains eligible, list refresh restores its selection without generating a false user-selection event.

## Threading

The state model has no Swing or JavaFX dependency and performs no thread dispatch. The current desktop controller accesses it on Swing's event-dispatch thread. JavaFX diagram rendering continues to cross to the JavaFX application thread inside `DiagramPanel`.

## Consequences

- Existing Advisor behavior and menus remain available.
- State ownership is explicit and testable without a visible desktop.
- Theme and navigation rendering no longer depend on private panel state.
- Locale and accessibility settings have an application-owned location even though Milestone 2 adds no new controls for them.
- Persistence across process restarts is not included.
- Subject providers, abstract data-structure identifiers, and knowledge-core extraction are not included.

## Validation

Automated tests cover defaults, validated updates, defensive answer snapshots, reset preservation, navigation preservation, silent control rendering, and list-selection restoration. Visible state behavior, diagram rendering, theme readability, and menu recovery remain part of manual desktop acceptance.
