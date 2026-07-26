# ADR: Persistent and experience-specific subject context

- Status: Accepted for the post-Milestone-8 context polish
- Date: 2026-07-25

## Context

Java and C are functional subjects, but the active subject was visible only in
the Subject menu. Explorer representation names could also be clipped by a
fixed, narrow left area. The application needs clearer context without
duplicating active-subject state in each experience or weakening the existing
render-before-commit subject switch.

## Decision

`ApplicationState.activeSubject` remains the only active-subject state, and
`SubjectProviderRegistry` remains the source of provider display names. The
shared `MainFrame` menu bar renders a slightly taller workspace header with
menus on the left and a compact, right-aligned active-language badge that
shows a larger language icon and the active provider display name. Explorer
owns a presentation label:

- Explorer identifies the subject controlling its representation list.

The badge and labels implement display boundaries and do not retain a
`SubjectId`, provider, or second subject model. Flash Cards no longer repeats
global language context in its own header.

During a subject switch, each active experience renders all other
subject-sensitive content before changing its context label. Only after that
render succeeds does `SubjectSelectionController` commit the active subject.
The shared menu-bar indicator is updated after the selection transaction
returns. A rendering exception therefore leaves committed state and visible
context unchanged.

Explorer places the representation list and existing diagram/details content
in a user-adjustable horizontal split. The preferred list width is measured
from the current rendered cells and section title, including cell padding,
scrollbar allowance, borders, and insets, then clamped to 220–340 pixels. A
subject refresh updates the preferred width and moves an automatically placed
divider. Once the user moves the divider, later refreshes preserve that
position while retaining the new preferred size for layout.

The Overview tab explicitly identifies itself as a language-neutral concept
overview. Subject tabs and the abstract diagram retain their Milestone 8
behavior.

## Accessibility and themes

All context is expressed as text rather than color. Context labels participate
in the existing recursive theme application and use the current theme
foreground and background. No horizontal scrollbar is introduced as the
primary representation-name solution.

## Consequences

- Future enabled providers automatically use their provider display names.
- No application-state field, persistence, framework, or event bus is added.
- Explorer tab selection remains local presentation state.
- Flash Card session and progress remain independent and survive subject
  switching.
- Visible layout, themes, scaling, keyboard focus, and controlled failure
  recovery still require desktop acceptance.

## Validation

Headless tests cover measured width bounds, longer synthetic names, automatic
width refresh, manual divider preservation, dynamic context labels, theme
refresh, successful switches, and failed-switch rollback. Existing controller
tests cover concept and session preservation.
