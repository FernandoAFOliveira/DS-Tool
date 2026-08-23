package com.fernando.ds.gui;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

import com.fernando.ds.diagram.DiagramDescriptor;
import com.fernando.ds.diagram.DiagramId;
import com.fernando.ds.knowledge.StructureId;

/** Local, non-persistent diagram selection for one presentation surface. */
final class DiagramSelectionModel {

    private final EnumMap<StructureId, DiagramId> selections =
        new EnumMap<>(StructureId.class);

    DiagramDescriptor selected(
        StructureId structureId,
        List<DiagramDescriptor> available
    ) {
        validate(structureId, available);
        DiagramId requested = selections.get(structureId);
        return available.stream()
            .filter(diagram -> diagram.id() == requested)
            .findFirst()
            .orElse(available.getFirst());
    }

    void select(
        StructureId structureId,
        DiagramId diagramId,
        List<DiagramDescriptor> available
    ) {
        validate(structureId, available);
        Objects.requireNonNull(diagramId, "diagramId");
        if (available.stream().noneMatch(
            diagram -> diagram.id() == diagramId
        )) {
            throw new IllegalArgumentException(
                "Diagram is not available for: " + structureId
            );
        }
        selections.put(structureId, diagramId);
    }

    private static void validate(
        StructureId structureId,
        List<DiagramDescriptor> available
    ) {
        Objects.requireNonNull(structureId, "structureId");
        Objects.requireNonNull(available, "available");
        if (available.isEmpty()) {
            throw new IllegalArgumentException(
                "At least one diagram is required"
            );
        }
    }
}
