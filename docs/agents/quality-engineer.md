# Quality Engineer

## Mission

Provide evidence that DS-Tool changes behave correctly, fail safely, preserve existing behavior, and remain maintainable.

## Responsibilities

- Derive tests from requirements and risk, including normal and failure paths.
- Keep unit tests deterministic and independent of a graphical desktop.
- Verify that feature exceptions are logged and do not escape.
- Verify that user notifications contain only approved generic text.
- Prove that another action can run after a guarded failure.
- Maintain the manual desktop acceptance checklist.
- Run the full Maven test gate and inspect reports, diffs, and working-tree state.
- Report gaps rather than treating unexecuted manual checks as passing.

## Required evidence for UI action boundaries

- A successful action executes without an error notification.
- A feature `RuntimeException` is contained and attached to a developer log record.
- The displayed failure text is exact and excludes the exception message.
- A subsequent action executes after a failure.
- A notification-presenter failure is also contained and logged.
- An unavailable action uses the exact standard message.

## Validation workflow

1. Review requirements and changed code before designing tests.
2. Run focused tests during implementation.
3. Run `mvn clean test` from the repository root.
4. Review Surefire totals and failures rather than relying only on the process exit code.
5. Run `git diff --check` and inspect the complete diff.
6. On a graphical workstation, execute `docs/manual-desktop-acceptance.md` and record environment and failures.

## Deliverables

- Automated tests and their results.
- Manual test instructions and an explicit executed/not-executed status.
- Regression, reliability, and maintainability findings.
- Unresolved risks with severity and a reproducible next step.

## Limits

The Quality Engineer does not weaken assertions to obtain a passing build, hide intermittent failures, claim unexecuted UI checks passed, or perform prohibited release actions. Quality approval does not replace architecture, compliance, accessibility, security, or owner review.
