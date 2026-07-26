# DS-Tool 2.0 Development Roadmap

This roadmap defines the planned sequence of runnable development milestones for DS-Tool 2.0.

The project must remain compilable and usable at the end of every milestone. Each milestone is reviewed through architecture, implementation, testing, compliance, and owner acceptance before it is considered complete.

## Guiding Principle

Development proceeds through small vertical slices rather than large speculative refactors.

Every milestone must:

- preserve currently working behavior
- produce a runnable application
- include appropriate automated tests
- include manual acceptance criteria for visible behavior
- comply with `docs/COMPLIANCE.md`
- avoid release, deployment, tag, merge, or publication actions unless explicitly performed by the project owner

---

## Milestone 1 — Resilient Desktop Shell

### Goal

Establish a desktop application shell in which unfinished or failed features degrade gracefully instead of terminating the application.

### Current foundation

- Subject menu
- Experience menu
- Java shown as the active subject
- Advisor shown as the active experience
- Placeholder entries for C, C++, Python, Explorer, Flash Cards, and Timed Quiz
- `UiActionGuard`
- Standard not-enabled message
- Compliance rules in `docs/COMPLIANCE.md`

### Remaining work

- Create root `AGENTS.md`
- Define the Compliance Agent and other core engineering roles
- Add JUnit 5 support
- Add automated tests for `UiActionGuard`
- Separate testable action-guard behavior from Swing dialog presentation where necessary
- Guard all applicable menu actions
- Verify that the application remains usable after a feature failure
- Add a manual desktop acceptance checklist
- Complete architecture, quality, and compliance reviews
- Produce one verified development commit

### Visible result

The existing Advisor remains functional. Every unfinished menu item responds with a clear message, and a failed feature cannot terminate the application.

---

## Milestone 2 — Application State Foundation

### Goal

Introduce a central application-state model that replaces state scattered across UI panels and controllers.

### State responsibilities

- active subject
- active experience
- recommendation answers
- selected abstract data structure
- theme and appearance preferences
- navigation state
- accessibility preferences
- locale and display-language preferences

### Acceptance requirements

- UI components read from and update the shared state
- switching screens does not unnecessarily reset answers or selections
- theme and other user preferences remain stable
- existing Advisor behavior remains functional

### Visible result

The user can move among available screens without losing unrelated state.

---

## Milestone 3 — Subject Provider Boundary

### Goal

Move language-specific information behind a documented subject-provider interface.

### Initial providers

- Java — functional
- C — placeholder
- C++ — placeholder
- Python — placeholder

### Acceptance requirements

- recommendation concepts remain independent of programming-language class names
- selecting Java displays the existing Java structures
- selecting an unfinished subject uses the standard not-enabled behavior
- subject changes do not reset unrelated application state

### Visible result

Java remains fully usable while other subject selections fail safely and consistently.

---

## Milestone 4 — Knowledge Core

### Goal

Separate abstract data-structure knowledge from Java implementation details.

### Core concepts

- indexed access
- key-value mapping
- ordering
- duplicate handling
- insertion and removal cost
- lookup cost
- memory considerations
- iteration characteristics
- common use cases
- conceptual relationships among structures

### Provider responsibilities

The Java provider supplies representations such as:

- `ArrayList`
- `LinkedList`
- `HashMap`
- `TreeMap`
- `HashSet`
- `TreeSet`
- `Queue`
- `Stack`
- `PriorityQueue`

### Visible result

The Advisor recommends an abstract structure first, then displays the Java representation supplied by the active subject provider.

---

## Milestone 5 — Explorer Experience

### Goal

Create the first additional application experience using the shared state and knowledge core.

### Explorer content

- conceptual description
- strengths and weaknesses
- supported operations
- complexity information
- diagrams
- active-subject representation
- related structures
- common use cases

### Acceptance requirements

- switching between Advisor and Explorer preserves active subject and selected structure
- Explorer consumes shared knowledge rather than duplicating it
- feature-level failures remain isolated

### Visible result

The user can select a structure and explore it without losing Advisor state.

---

## Milestone 6 — Learn Foundation

### Goal

Introduce the Learn experience with shared progress and question infrastructure.

### Initial activities

- Flash Cards
- Timed Quiz

### Foundation responsibilities

- shared question source
- progress model
- session state
- scoring where applicable
- safe interruption and resume behavior

### Scope rule

Begin with one small functional activity rather than attempting the complete learning system at once.

### Visible result

A working flash-card activity presents content derived from the same knowledge core used by Advisor and Explorer.

---

## Milestone 7 — Second Subject

### Goal

Implement one additional subject fully to prove that the subject-provider boundary is genuinely independent of Java.

### Planned candidate

C is the likely first additional subject, subject to architecture review and the existing planning documentation.

### Acceptance requirements

- Java and C implement the same subject-provider contract
- switching Java and C preserves the selected abstract concept
- language-specific representations change without resetting unrelated state
- shared experiences work with both subjects where supported

### Visible result

The user can switch between Java and C while retaining the same abstract data-structure selection and application state.

---

## Milestone 8 — Explorer Subject Tabs and Code Examples

### Goal

Restore implementation-oriented educational value to Explorer without moving
language-specific material into the Knowledge Core.

### Explorer details

- the shared abstract diagram remains outside the detail tabs
- the first detail tab is always **Overview**
- one subsequent tab appears for each enabled Subject Provider in stable
  registry order
- Overview renders only language-neutral Knowledge Core content
- Subject Providers supply platform-neutral implementation guidance and code
  examples
- the active subject continues controlling the left Explorer representation
  list without controlling the detail tabs

### Acceptance requirements

- current tabs are exactly **Overview**, **Java**, and **C**
- disabled C++ and Python providers do not create tabs
- future enabled providers create tabs without Explorer UI hardcoding
- Java and C provide subject-specific content for all current concepts
- tab selection remains local presentation state and survives ordinary
  Explorer refreshes
- structure selection retains render-before-commit failure behavior
- Advisor, Learn, the Knowledge Core structure, and existing reset behavior
  remain unchanged

### Visible result

The user can keep the abstract concept and diagram in view while moving among
language-neutral Overview, Java implementation guidance, and C implementation
guidance.

### Current project boundary

DS-Tool remains a single-module desktop proof of concept. Additional operating
systems, mobile devices, Android work, and physical platform separation belong
to a future successor application and are not milestones in this roadmap.

### Post-Milestone-8 subject-context rule

The shared desktop shell visibly identifies the active subject. Advisor,
Explorer, and Flash Cards explain how that subject affects their content, and
Explorer uses content-aware sizing for its adjustable representation list.

Timed Quiz remains intentionally unavailable. Its future session model must
capture the active subject when a quiz begins, display it throughout the
session, and never silently change it mid-session when the global subject
changes. A later design may prevent global switching during a quiz or allow
the global switch while retaining the quiz's captured subject.

---

## Standard Development Cycle

Every milestone follows this sequence:

1. Lead Architect defines and reviews the milestone boundaries.
2. Implementation agents build the smallest useful vertical slice.
3. Quality Engineer runs compilation, tests, and regression checks.
4. The application is launched in an appropriate graphical environment.
5. The owner performs behavior-based UI acceptance.
6. The Lead Architect reviews architectural boundaries.
7. The Compliance Agent reviews `docs/COMPLIANCE.md` requirements.
8. Required corrections are completed and revalidated.
9. The owner authorizes the development commit.

## Agent Authority

Agents may:

- inspect the repository
- modify code and documentation on approved development branches
- run builds and tests
- launch development builds
- produce review findings
- reject noncompliant work
- create development commits only when explicitly authorized

Agents may not:

- merge into protected or release branches
- create tags
- create GitHub releases
- publish packages
- deploy software
- force-push
- rewrite published history
- remove compliance protections
- declare a release

Only the project owner may authorize or create a release.

## Roadmap Maintenance

This file is the canonical milestone roadmap for DS-Tool 2.0.

When milestone scope changes:

- update this document in the same development branch
- record major architectural changes in an ADR
- do not silently remove incomplete acceptance requirements
- mark milestones complete only after owner acceptance and verified commit status
