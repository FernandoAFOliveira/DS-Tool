# ADR: Subject-bound Timed Quiz

- Status: Accepted for Milestone 9 implementation
- Date: 2026-07-26

## Context

Milestone 9 completes the second Learn activity without expanding the desktop
proof of concept into a quiz platform. The activity must reuse Knowledge Core
and Subject Provider content, preserve unrelated state, remain deterministic
in headless tests, and keep one subject for an entire session even when the
global subject changes.

## Decision

Timed Quiz provides one fixed ten-question mode:

- five concept-from-definition questions derived from Knowledge Core;
- five representation-from-concept questions derived from the subject
  captured at session start;
- four distinct choices with exactly one correct answer;
- one point per correct answer and no partial or negative scoring; and
- one session-level elapsed timer with no countdown or automatic failure.

The two groups cover all nine release concepts; Deque appears once in each
pattern to reach ten questions without inventing a new concept or duplicating
arbitrary educational prose.

`QuizQuestionSource` is concrete and uses a small injected random generator to
shuffle the fixed questions and each question's four options. Production
sessions vary; tests inject a fixed seed. Randomization does not alter content,
coverage, scoring, or subject binding. There is no question repository,
category system, difficulty abstraction, or general quiz framework.

## State ownership

`QuizSession` owns its captured `SubjectId`, subject display name, fixed
questions, current position, submitted answers, score, lifecycle, and timing.
It is owned by `TimedQuizController`, not `ApplicationState`.

The explicit lifecycle is:

1. `NOT_STARTED`;
2. `QUESTION_ACTIVE`;
3. `ANSWER_REVIEWED`; and
4. `COMPLETED`.

Every transition returns an immutable proposed session. The controller renders
that proposal before replacing its committed session. A presentation
`RuntimeException` therefore leaves the prior quiz state valid for a retry.

Quiz state is independent of Flash Cards session/progress and does not read or
write Advisor answers, Advisor navigation, Explorer selection, or the shared
selected `StructureId`. **File > Reset selections** resets only the quiz while
Timed Quiz is the visible activity; established reset behavior remains in
other experiences.

## Subject binding

The pre-session screen previews the current global subject. Starting captures
that enabled provider's identity and builds all subject-specific questions
immediately.

A later global subject switch follows the existing render-before-commit
boundary but an active or completed quiz continues rendering its captured
subject and questions. Other experiences use the newly committed global
subject normally. Starting a new completed quiz captures whichever global
subject is active at that time.

No quiz-subject field is added to `ApplicationState`.

## Timing

`TimeSource` supplies monotonic nanoseconds. Production uses
`System.nanoTime`; tests use a mutable fake and never sleep.

Timing starts when the session starts and freezes when the final answer is
submitted. The final feedback still requires an explicit **View Results**
action. Explicit quiz reset freezes and abandons the old session. The Swing
refresh timer updates only the visible elapsed label and is stopped when the
quiz view is hidden, reset, or completed.

## Desktop presentation and failure behavior

`TimedQuizController` depends on the display-independent `TimedQuizView`.
`TimedQuizPanel` uses standard Swing radio buttons, a button group, scrollable
content, and Start, Submit, Next/View Results, and Start New Quiz controls.
Selecting a radio button never submits it.

All menu, answer, navigation, reset, and timer callbacks enter through
`UiActionGuard`. Runtime failures use the existing generic user message and
full developer logging. The desktop composition root keeps a local Learn
activity marker solely to route the visible Flash Cards or Timed Quiz card;
the shared application experience remains `LEARN`.

## Consequences and boundary

- Java and C quizzes use their provider representation names.
- Language-neutral questions are identical for Java and C.
- Flash Cards progress and session behavior are unchanged.
- Quiz progress is in-memory and ends with the process.
- There is no pause/resume control, question history screen, per-question
  countdown, persistence, analytics, adaptive behavior, categories,
  achievements, animation, sound, or additional quiz mode.

Manual desktop validation remains required for focus behavior, keyboard-only
completion, themes, supported scaling, resizing, visible timing, subject
switching, and guarded dialog recovery.
