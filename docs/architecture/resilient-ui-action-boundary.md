# ADR: Resilient desktop UI action boundary

- Status: Accepted for Milestone 1
- Date: 2026-07-24

## Context

DS-Tool's desktop commands run on Swing's event-dispatch thread. A feature `RuntimeException` that escapes an action listener can interrupt the current interaction, expose inconsistent behavior, and make recovery uncertain. The compliance rules require developer logging, generic user messaging, and continued application use. Direct use of `JOptionPane` inside the original guard also made failure behavior difficult to test on a headless build worker.

## Decision

All visible desktop command items enter through `UiActionGuard` or use its standard not-enabled operation.

The boundary is separated into three responsibilities:

- `UiActionGuard` executes a feature action, catches feature-level `RuntimeException`s, logs the full exception with feature context, and requests a generic notification.
- `UiMessagePresenter` is a package-private, platform-neutral notification contract used by the guard's non-graphical behavior.
- `SwingUiMessagePresenter` adapts that contract to owned `JOptionPane` dialogs.

The guard also contains and logs a runtime failure raised while presenting a notification. This avoids replacing a contained feature failure with a dialog-subsystem failure on the event-dispatch thread.

Programming-contract violations such as null required arguments remain fail-fast. Serious JVM `Error`s are not caught because they do not represent recoverable feature-level runtime failures.

## User-message policy

Only these messages cross the failure boundary:

- unavailable: `This feature is not enabled yet.`
- feature failure: `There was an error trying to display this feature.`

The exception object remains attached to the developer log record and is never supplied to the message presenter.

## Consequences

### Positive

- Guard behavior is unit-testable without constructing a frame or opening a dialog.
- Feature failures do not escape their command listener through the guard.
- Developer logs retain exception type, message, and stack trace.
- Swing presentation remains replaceable and localized.
- A failed presenter is logged and contained.

### Trade-offs

- The package gains a small presentation interface and Swing adapter.
- Asynchronous failures must be handled at the completion boundary of their background task; wrapping only the task submission is insufficient.
- The current logger uses `java.util.logging` defaults. Persistent log-file configuration is outside Milestone 1 and should be decided before production support depends on local diagnostic files.

## Alternatives considered

### Call `JOptionPane` directly in every listener

Rejected because behavior and wording would be duplicated, exceptions could escape inconsistently, and headless tests could not observe notification requests cleanly.

### Catch exceptions in each feature

Rejected because it relies on every feature author to reproduce the same logging, privacy, wording, and recovery rules.

### Catch all `Throwable`

Rejected because JVM errors are not ordinary feature failures and may indicate a process state that is unsafe to continue.

## Validation

Headless unit tests cover successful execution, exception containment and logging, generic error text, continued execution, presenter failure containment, and exact unavailable-feature text. Visible dialog ownership, menu wiring, theme rendering, and post-dialog interaction remain part of manual desktop acceptance.
