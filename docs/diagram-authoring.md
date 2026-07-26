# Static diagram authoring guide

DS-Tool diagrams are language-neutral Mermaid resources owned by abstract
`StructureId` concepts. They illustrate a small number of high-value ideas;
they are not provider documentation or an operation-animation system.

## Files and naming

Complete diagrams live in:

```text
src/main/resources/diagrams/templates/
```

Use lowercase kebab-case:

- `<concept>-structure` for the required primary view;
- `<concept>-operation` for one selected operation;
- `<concept>-memory` for storage layout; or
- `<concept>-relationship` for a conceptual relationship.

Use a more precise suffix when it teaches better, such as
`dynamic-array-resize`, `queue-circular-buffer`, or `hash-map-collision`.
Names must not contain Java or C representation names.

Do not copy Mermaid initialization or class definitions into a template.
`shared-init.mmd.fragment` supplies layout and typography;
`shared-styles.mmd.fragment` supplies semantic classes. A complete template
starts with concise presentation metadata followed by its Mermaid body:

```text
%% @title Hash map
%% @subtitle Keys select buckets that store key-value entries
flowchart LR
    ...
```

The shared page renders this title and subtitle above the SVG so a heading
cannot distort Mermaid's graph aspect ratio. Keep each metadata value on one
line and do not add a title node to the graph itself.

## Visual conventions

- Use `:::data` for stored values and ordinary structural nodes.
- Use `:::focus` for the active or defining element.
- Use `:::action` for abstract operations.
- Use `:::note` for explanation or unused capacity.
- Use `:::warning` only for a rejected case, constraint, or problematic state.
- Use `class <subgraph-id> container` for storage regions.
- Use solid links for structure and dotted links for operation flow,
  temporary movement, or annotations.
- Give every concept template one concise title and subtitle in its metadata,
  and keep graph labels readable without relying on color.
- Prefer 3–7 meaningful data nodes. Avoid excessive whitespace, crossed
  arrows, implementation trivia, and prose-heavy nodes.
- Prefer a balanced horizontal arrangement when a narrow vertical graph would
  waste the normal desktop viewport. The shared page preserves aspect ratio,
  centers the SVG, and refits it after viewport changes.

## Language neutrality

Use terms such as dynamic array, key, value, bucket, capacity, front, rear,
root, parent, and child. Do not include collection class names, method
signatures tied to one language, allocation functions, struct identifiers,
imports/includes, resource paths, or provider names.

## Mapping a primary diagram

Add the descriptor to `DiagramCatalog` under its `StructureId`. The first
descriptor must use `DiagramId.STRUCTURE`; every `StructureId` must have
exactly one unique primary resource.

Do not build a resource name from a provider or display name. Do not add a
diagram field to `ApplicationState`.

## Adding an optional diagram

Add an enum-like `DiagramId` only when the view expresses a stable concept
used by the catalog. Append at most one optional descriptor to a concept's
list. Primary must remain first. Reuse a neutral optional resource across
concepts only when its semantics truly match, as with search-tree balancing.

`DiagramPanel` automatically shows its selector for lists longer than one.
Missing optional resources fall back to primary. Do not add a separate
provider diagram selector or persistence.

## Preview and validation

Run focused mapping tests:

```text
mvn -Dtest=DiagramCatalogTest,DiagramTemplateLoaderTest test
```

Run all regression tests before handoff:

```text
mvn clean test
```

For standalone SVG previews, install Mermaid CLI (`mmdc`) and run from the
repository root:

```text
.\tools\generate-diagrams.ps1
```

The script combines each body with the shared fragments and generates
theme-specific previews. Visually inspect every theme at narrow and maximized
window sizes. Generated previews are review aids; do not package or publish
them as part of this workflow.

Never add animation, draggable nodes, editor behavior, touch interaction,
classroom tooling, a diagram DSL, or a generalized rendering engine to this
desktop milestone.
