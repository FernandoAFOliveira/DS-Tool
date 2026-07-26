# ADR: Learn foundation

- Status: Accepted for Milestone 6 implementation
- Date: 2026-07-25

## Context

Milestone 6 adds the Learn experience on top of shared application state and
the Knowledge Core. Its first vertical slice must provide a working flash-card
activity, progress and session foundations, and safe interruption without
implementing the later Timed Quiz or persistence.

The existing `QuestionLibrary` collects Advisor constraints and includes
Advisor-specific explanatory text. It is not the shared source for learning
activities.

## Decision

`LearningQuestionSource` derives one immutable, language-neutral question from
each `DataStructureKnowledge` record in stable Knowledge Core order. The
question prompt and answer teach the abstract concept. It is a concrete source,
not an interface plus implementation, because Milestone 6 has only one source
and no approved need for interchangeable backends.

There is no separate `FlashCardService`. `FlashCardController` consumes the
question source, coordinates the in-memory session and progress, and optionally
resolves the active `SubjectProvider` representation as supplementary content.
The subject name appears only after the concept answer and is not required for
the card to exist.

`LearningProgress` records the distinct concepts whose answers have been
successfully revealed. Review is not a score and does not claim correctness or
mastery. `FlashCardSession` records the current concept and answer visibility.
`ApplicationState` owns both so they survive experience switches.

## Selection independence

Flash-card navigation does not read or update
`ApplicationState.selectedStructureId`. That identifier is the shared
Advisor/Explorer selection and can control restored Advisor structure content.
Synchronizing sequential study navigation with it could silently replace a
user's Advisor/Explorer choice, including with a structure excluded by current
Advisor answers.

Learn therefore has its own current-card identifier. Switching among
experiences preserves both selections independently.

## Desktop composition and failure behavior

`FlashCardController` depends on a display-independent `FlashCardView`.
`FlashCardPanel` supplies the Swing presentation and is created lazily by the
desktop composition root.

Opening Flash Cards and using Previous, Reveal answer, and Next all enter
through `UiActionGuard`. The controller renders before changing current-card,
answer, or progress state. A runtime presentation failure therefore produces
the standard generic error without committing false progress or navigation.

Previous and Next use stable Knowledge Core order and stop at the ends.

## Timed Quiz successor milestone

Milestone 9 implements the subject invariant established here: a Timed Quiz
captures the active subject when it begins, displays that subject throughout
the session, and remains bound when the global subject changes. Its independent
session, scoring, timing, and failure behavior are recorded in
[`timed-quiz.md`](timed-quiz.md).

## Reset and persistence

**File > Reset selections** retains its established Advisor-owned semantics and
does not reset Learn progress or the Flash Cards session. Learn state is
in-memory only and is not restored after an application restart.

## Consequences

- Advisor, Explorer, and Learn consume the same Knowledge Core.
- Flash Cards remains concept-first while allowing small subject-specific
  context.
- No second knowledge catalog or duplicate question content is introduced.
- The current desktop remains a single Maven module.
- Scoring is not applicable to this passive reveal activity.

## Milestone boundary

At Milestone 6, Timed Quiz, timers, correctness scoring, persistence,
additional functional subjects, randomized decks, spaced repetition, and
Explorer tabs were outside that milestone. Module separation and Android
remain successor-application concerns.

## Validation

Automated tests cover Knowledge Core derivation, immutable question ordering,
progress, session resume, selection independence, supplementary Java mapping,
render-before-commit failures, and headless Swing rendering. Visible themes,
keyboard behavior, interruption, failure recovery, scaling, and regression
behavior remain in the desktop manual acceptance checklist.
