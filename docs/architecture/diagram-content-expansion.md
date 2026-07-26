# ADR: Diagram Content Expansion

- Status: Accepted for late-stage desktop implementation
- Date: 2026-07-25

## Context and inventory

The desktop proof of concept had one Mermaid template for each current
`StructureId`, plus welcome and an unused linked-list template. Mapping already
started from `StructureId`, but resource names and labels retained Java
collection terminology, each template duplicated its theme setup and style
definitions, and several concepts lacked one especially valuable second view.

| Concept | Previous template | Previous educational role | Gap and decision |
|---|---|---|---|
| Dynamic array | `arraylist` | Indexed cells and common operations | Java name/API; replace primary and add resize/copy |
| Stack | `stack` | LIFO storage and top operations | Retain one stronger neutral primary |
| Queue | `queue` | FIFO front/rear flow | Add circular-buffer view |
| Priority queue | `priorityqueue` | Heap tree and next item | Java-derived labels; add array layout |
| Deque | `arraydeque` | Operations at both ends | Java class/API; replace with neutral primary |
| Hash set | `hashset` | Buckets and duplicate rejection | Java class/API; replace with neutral primary |
| Ordered set | `treeset` | Search-tree order | Java API; add shared balancing view |
| Hash map | `hashmap` | Hash-to-entry lookup | Java API; add collision view |
| Ordered map | `treemap` | Ordered key-value tree | Java API; add shared balancing view |

The unreferenced `linkedlist` resource had no Knowledge Core concept and was
removed. Welcome remains a standalone neutral application-flow diagram.

## Decision

`DiagramCatalog` owns an immutable `StructureId` mapping. Every concept has
exactly one primary `STRUCTURE` descriptor first and may have one optional
descriptor after it. `DiagramId` identifies the small accepted view set;
resource lookup never uses provider IDs, display names, or representation
names.

The final physical diagram set contains:

- nine unique primary concept resources;
- dynamic-array resize/copy;
- circular-buffer queue;
- priority-queue array layout;
- hash-map collision handling;
- one balancing resource shared by ordered set and ordered map; and
- welcome.

That is 15 complete diagram resources. Shared Mermaid initialization and style
fragments are authoring infrastructure, not diagrams.

`DiagramPanel` displays a compact focusable selector only when the catalog
contains more than one view. Primary is the default. Each DiagramPanel owns a
`DiagramSelectionModel`, so Advisor and Explorer selections are local,
non-persistent presentation state. The model remembers a view per
`StructureId`; subject and theme refreshes therefore retain an available
selection without changing `ApplicationState`.

## Shared visual language

All diagrams use the shared fragments and these semantics:

- solid links represent structural relationships, adjacency, or stored links;
- dotted links represent an operation, temporary movement, or annotation;
- `data` is an ordinary stored value or structural element;
- `focus` marks the active or defining element;
- `action` names an abstract operation;
- `note` supplies non-operational explanation or unused capacity;
- `warning` marks a constraint, rejected case, or unbalanced condition;
- `container` outlines a conceptual storage region; and
- template `@title` and `@subtitle` metadata provides a consistent
  concept-first heading outside the Mermaid SVG.

Theme JSON files supply semantic colors. Color is supplemental: every focus,
operation, state, and warning also has a text label. Templates use consistent
18-pixel base typography, 17-pixel primary node text, 16-pixel annotation
text, 52-pixel node spacing, and 54-pixel rank spacing. The shared HTML
heading uses a 28-pixel title and an 18-pixel subtitle.

## Responsive presentation

Concept views remove the legacy in-viewport application banner; the window
title and shell retain application identity. Welcome may retain a compact
42-pixel brand header.

After Mermaid produces an SVG, the shared page compares its view box with the
actual padded diagram viewport and applies a contain scale. Width and height
use the same scale factor, the SVG uses `xMidYMid meet`, and flex alignment
centers the result. A resize observer repeats the fit when the window or a
splitter changes the available area. Every new render repeats it as well, so
diagram selection, theme refresh, and experience changes all follow the same
rule in Advisor and Explorer.

Keeping headings outside the SVG prevents title width from changing the
relationship graph's aspect ratio or collapsing into a narrow side column.
No zoom state, persistence, or `ApplicationState` field is introduced.

## Language-neutrality rule

Shared diagrams use abstract terms such as key, value, bucket, capacity,
front, rear, parent, child, and root. They must not contain Java collection
class names, Java API-specific labels, C allocation or structure identifiers,
provider display names, imports, or includes. Java and C implementation
teaching remains in provider-owned Explorer content and Advisor explanations.

## Failure behavior

Template and theme loading occurs synchronously before a diagram is queued for
JavaFX rendering. A missing optional resource falls back to the concept's
primary diagram. A missing primary resource raises a runtime failure to the
existing guarded UI boundary and leaves the prior visual available.

Advisor now renders a selected structure before committing its selection and
navigation, matching Explorer's established render-before-commit behavior. A
primary load failure therefore cannot commit a new selected `StructureId` or
active subject. A later valid selection can retry normally.

JavaScript/WebView rendering remains asynchronous. The existing engine error
listener reports JavaScript errors for developers; converting asynchronous
Mermaid errors into a transactional UI result would require a larger rendering
protocol and is outside this bounded milestone.

## Consequences

- Knowledge Core, subject providers, recommendations, Learn, and
  `ApplicationState` remain unchanged.
- Advisor and Explorer continue using the same concept-owned catalog.
- The selector uses standard Swing keyboard and focus behavior.
- Shared authoring setup is centralized without a diagram DSL, plugin system,
  generalized rendering engine, or new background-task framework.
- Static diagrams remain illustrative rather than executable semantic models.

## Successor-project boundary

Animated operations, data-driven semantic diagrams, draggable nodes, touch
interaction, editing, classroom presentation tools, and Flutter rendering are
deferred to the successor project.
