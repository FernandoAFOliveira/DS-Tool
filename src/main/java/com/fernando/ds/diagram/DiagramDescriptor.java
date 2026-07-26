package com.fernando.ds.diagram;

import java.util.Objects;

/**
 * Describes one language-neutral static diagram resource.
 *
 * @param id stable view identity within a concept
 * @param label short user-facing selector label
 * @param resourceName language-neutral resource basename
 */
public record DiagramDescriptor(
    DiagramId id,
    String label,
    String resourceName
) {

    public DiagramDescriptor {
        Objects.requireNonNull(id, "id");
        label = requireText(label, "label");
        if (label.matches(".*[<>&].*")) {
            throw new IllegalArgumentException(
                "Diagram selector label must be plain text"
            );
        }
        resourceName = requireText(resourceName, "resourceName");
        if (!resourceName.matches("[a-z0-9]+(?:-[a-z0-9]+)*")) {
            throw new IllegalArgumentException(
                "Diagram resource name must be lowercase kebab-case"
            );
        }
    }

    @Override
    public String toString() {
        return label;
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
