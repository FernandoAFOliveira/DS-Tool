# DS-Tool Compliance Rules

These rules apply to every human- or AI-authored change. A pull request is not ready for review until it satisfies this document.

## 1. Module Design

Every module must:

1. Have one clear responsibility.
2. Expose a documented public interface.
3. Be independently developable wherever practical.

Additional requirements:

- Dependencies point toward the shared core.
- Platform modules do not contain recommendation or subject logic.
- Subject providers do not contain platform or UI logic.
- Experience modules consume shared knowledge; they do not redefine it.
- Circular dependencies are prohibited.

## 2. UI Reliability

A missing, unfinished, or failed feature must degrade gracefully inside the application.

- A feature-level failure must not terminate the application.
- Every visible menu item must perform an action.
- An unfinished feature displays: `This feature is not enabled yet.`
- A feature that fails to open displays: `There was an error trying to display this feature.`
- Full exceptions are logged for developers.
- Stack traces and internal exception details are not shown to normal users.
- After a failure, the user must be able to continue using another feature.
- Long-running work must not block the UI thread.

## 3. State Preservation

Changing a subject provider, experience, or screen must not unnecessarily reset user state.

Subject switching must preserve:

- selected abstract data structure
- recommendation answers
- ranking priorities and weights
- theme
- display language and locale
- accessibility preferences
- navigation position
- other user personalization not owned by the subject provider

## 4. Extensibility

New capabilities should compose existing knowledge rather than modify unrelated modules.

- New subjects implement the subject-provider contract.
- New platforms implement platform adapters.
- New experiences consume existing application and knowledge APIs.
- Placeholder features must use the standard not-enabled behavior.

## 5. Documentation

A change must include the documentation needed to understand and use it.

- Public interfaces require documentation.
- Significant architectural decisions require an ADR.
- Developer setup changes require an update to `DEVELOPMENT.md` or `requirements.txt`.
- User-visible behavior changes require user documentation or release notes.

## 6. Testing and Validation

Every feature should validate:

- its normal path
- its failure path
- state preservation where applicable
- regression behavior for existing features

A deliberately failing feature must be testable without terminating the application.

## 7. Compliance Review

The Compliance Agent reviews each pull request independently of the implementation author.

The agent must reject or request changes when:

- a rule in this document is violated
- a public interface is undocumented
- a menu item performs no action
- an exception can escape a feature boundary and terminate the UI
- unrelated responsibilities are mixed in one module
- state is reset without a documented reason
- tests or documentation required by this document are missing

Approval means only that the change satisfies these compliance rules. It does not replace architecture, correctness, security, accessibility, or owner review.
