# DS-Tool Agent Instructions

## Project objective

DS-Tool 2.0 is an extensible data-structure learning and recommendation application. The current milestone establishes a resilient Java desktop shell that keeps working when an individual feature is unavailable or fails. Changes must preserve the existing Java advisor while moving toward the separation described in `docs/architecture/multilanguage-separation.md`.

All work must comply with `docs/COMPLIANCE.md`.

## Agent authority

Agents may, within the scope requested by the project owner:

- inspect tracked and untracked project files and Git state;
- edit source code, tests, resources, and documentation;
- run non-destructive build, test, lint, and diagnostic commands;
- create generated build output under `target/`;
- recommend architecture, quality, accessibility, and compliance improvements.

Agents must stop and request direction before materially expanding scope, changing externally managed state, discarding user work, or taking an action that requires release authority. Existing unrelated working-tree changes belong to the user and must be preserved.

## Prohibited release actions

Unless the project owner explicitly authorizes the exact action, agents must not:

- change, create, delete, or rewrite branches;
- commit or amend commits;
- push or force-push;
- merge or rebase;
- create or move tags;
- create a release or publish an artifact;
- run installer/release scripts or package a release;
- upload artifacts or modify remote repository state.

`mvn clean test` is validation, not release packaging, and is permitted. Do not substitute `package`, `install`, `deploy`, `jpackage`, or a script under `tools/` when only test validation was requested.

## Required development workflow

1. Read this file, `docs/COMPLIANCE.md`, relevant architecture documentation, and every file directly involved in the change.
2. Inspect the current branch and working tree without changing either. Identify and preserve pre-existing changes.
3. Trace callers and user-visible behavior before editing. For menu work, inventory every visible command item and classify it as working, intentionally unavailable, or guarded failure.
4. Make the smallest cohesive change that satisfies the request. Keep core/domain logic independent of Swing and JavaFX where practical.
5. Add or update tests for normal and failure paths. UI boundary logic must be testable without a visible desktop.
6. Update user, developer, architecture, and acceptance documentation when behavior or structure changes.
7. Run focused checks while developing, then run `mvn clean test` before handoff.
8. Inspect `git diff`, `git diff --check`, and `git status` before reporting. Do not stage files unless explicitly requested.
9. Report architecture, quality, and compliance findings separately, including unresolved concerns and required manual tests.

## UI reliability requirements

- A feature-level `RuntimeException` must not escape its UI action boundary.
- Log the full exception for developers with a feature name that identifies the failing action.
- Never place stack traces, exception messages, secrets, resource paths, or other internal details in a user dialog.
- Failed features display exactly: `There was an error trying to display this feature.`
- Intentionally unavailable features display exactly: `This feature is not enabled yet.`
- Every visible command item must perform a working action or produce one of those standard messages.
- After dismissing a failure or unavailable-feature dialog, the main window must remain usable and another command must work.
- Swing dialog presentation must remain separate from non-graphical action-boundary behavior.
- Long-running work must run off the Swing event-dispatch thread, with UI updates returned safely to that thread.
- Feature changes must preserve unrelated user state as required by `docs/COMPLIANCE.md`.

## Validation and commit rules

The minimum completion gate for Java changes is:

```text
mvn clean test
git diff --check
git status --short --branch
```

Tests must be deterministic, must not require a visible display unless explicitly categorized as UI integration tests, and must not depend on execution order. A test for a failure boundary must prove both containment and subsequent usability.

Manual desktop acceptance remains required for user-visible Swing or JavaFX changes; use `docs/manual-desktop-acceptance.md` and report unchecked items to the project owner.

Do not create a commit merely because validation passes. A commit requires an explicit owner request after the diff and validation results are available. Before an authorized commit:

- verify the intended file list and exclude generated `target/` content;
- ensure tests pass and the diff has no whitespace errors;
- use a focused message describing one cohesive change;
- never amend, squash, or rewrite history unless explicitly authorized.
