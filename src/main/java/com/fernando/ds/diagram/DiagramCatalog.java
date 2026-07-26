package com.fernando.ds.diagram;

import static com.fernando.ds.diagram.DiagramId.ARRAY_LAYOUT;
import static com.fernando.ds.diagram.DiagramId.BALANCING;
import static com.fernando.ds.diagram.DiagramId.CIRCULAR_BUFFER;
import static com.fernando.ds.diagram.DiagramId.COLLISION_HANDLING;
import static com.fernando.ds.diagram.DiagramId.RESIZE_COPY;
import static com.fernando.ds.diagram.DiagramId.STRUCTURE;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fernando.ds.knowledge.StructureId;

/** StructureId-owned mapping for primary and optional static diagrams. */
public final class DiagramCatalog {

    private static final Map<StructureId, List<DiagramDescriptor>> DIAGRAMS =
        createCatalog();

    private DiagramCatalog() {
    }

    /**
     * Returns stable diagram order with exactly one primary structure view
     * first.
     */
    public static List<DiagramDescriptor> get(StructureId structureId) {
        List<DiagramDescriptor> diagrams = DIAGRAMS.get(
            Objects.requireNonNull(structureId, "structureId")
        );
        if (diagrams == null) {
            throw new IllegalArgumentException(
                "No diagrams registered for: " + structureId
            );
        }
        return diagrams;
    }

    /** Returns the required primary structure view. */
    public static DiagramDescriptor primary(StructureId structureId) {
        return get(structureId).getFirst();
    }

    private static Map<StructureId, List<DiagramDescriptor>>
        createCatalog() {
        EnumMap<StructureId, List<DiagramDescriptor>> diagrams =
            new EnumMap<>(StructureId.class);
        diagrams.put(StructureId.DYNAMIC_ARRAY, List.of(
            diagram(STRUCTURE, "Structure", "dynamic-array-structure"),
            diagram(RESIZE_COPY, "Resize and copy", "dynamic-array-resize")
        ));
        diagrams.put(StructureId.STACK, List.of(
            diagram(STRUCTURE, "Structure", "stack-structure")
        ));
        diagrams.put(StructureId.QUEUE, List.of(
            diagram(STRUCTURE, "Structure", "queue-structure"),
            diagram(
                CIRCULAR_BUFFER,
                "Circular buffer",
                "queue-circular-buffer"
            )
        ));
        diagrams.put(StructureId.PRIORITY_QUEUE, List.of(
            diagram(STRUCTURE, "Structure", "priority-queue-structure"),
            diagram(ARRAY_LAYOUT, "Array layout", "priority-queue-array")
        ));
        diagrams.put(StructureId.DEQUE, List.of(
            diagram(STRUCTURE, "Structure", "deque-structure")
        ));
        diagrams.put(StructureId.HASH_SET, List.of(
            diagram(STRUCTURE, "Structure", "hash-set-structure")
        ));
        diagrams.put(StructureId.ORDERED_SET, List.of(
            diagram(STRUCTURE, "Structure", "ordered-set-structure"),
            diagram(BALANCING, "Balancing", "ordered-tree-balancing")
        ));
        diagrams.put(StructureId.HASH_MAP, List.of(
            diagram(STRUCTURE, "Structure", "hash-map-structure"),
            diagram(
                COLLISION_HANDLING,
                "Collision handling",
                "hash-map-collision"
            )
        ));
        diagrams.put(StructureId.ORDERED_MAP, List.of(
            diagram(STRUCTURE, "Structure", "ordered-map-structure"),
            diagram(BALANCING, "Balancing", "ordered-tree-balancing")
        ));
        return Map.copyOf(diagrams);
    }

    private static DiagramDescriptor diagram(
        DiagramId id,
        String label,
        String resourceName
    ) {
        return new DiagramDescriptor(id, label, resourceName);
    }
}
