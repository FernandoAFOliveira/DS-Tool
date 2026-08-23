package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.diagram.DiagramCatalog;
import com.fernando.ds.diagram.DiagramDescriptor;
import com.fernando.ds.diagram.DiagramId;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.subject.SubjectId;

class DiagramSelectionModelTest {

    @Test
    void defaultsToPrimaryAndRestoresLocalSelectionPerConcept() {
        DiagramSelectionModel model = new DiagramSelectionModel();
        List<DiagramDescriptor> dynamicArray =
            DiagramCatalog.get(StructureId.DYNAMIC_ARRAY);

        assertEquals(
            DiagramId.STRUCTURE,
            model.selected(
                StructureId.DYNAMIC_ARRAY,
                dynamicArray
            ).id()
        );

        model.select(
            StructureId.DYNAMIC_ARRAY,
            DiagramId.RESIZE_COPY,
            dynamicArray
        );
        assertEquals(
            DiagramId.STRUCTURE,
            model.selected(
                StructureId.STACK,
                DiagramCatalog.get(StructureId.STACK)
            ).id()
        );
        assertEquals(
            DiagramId.RESIZE_COPY,
            model.selected(
                StructureId.DYNAMIC_ARRAY,
                dynamicArray
            ).id()
        );
    }

    @Test
    void subjectChangesDoNotAffectLocalDiagramOrApplicationState() {
        ApplicationState state = new ApplicationState();
        state.selectStructure(StructureId.QUEUE);
        DiagramSelectionModel model = new DiagramSelectionModel();
        List<DiagramDescriptor> queue =
            DiagramCatalog.get(StructureId.QUEUE);
        model.select(
            StructureId.QUEUE,
            DiagramId.CIRCULAR_BUFFER,
            queue
        );

        state.setActiveSubject(SubjectId.C);

        assertEquals(
            DiagramId.CIRCULAR_BUFFER,
            model.selected(StructureId.QUEUE, queue).id()
        );
        assertEquals(
            StructureId.QUEUE,
            state.getSelectedStructureId().orElseThrow()
        );
        assertEquals(SubjectId.C, state.getActiveSubject());
    }

    @Test
    void rejectsUnavailableSelectionWithoutChangingPriorChoice() {
        DiagramSelectionModel model = new DiagramSelectionModel();
        List<DiagramDescriptor> stack =
            DiagramCatalog.get(StructureId.STACK);

        assertThrows(
            IllegalArgumentException.class,
            () -> model.select(
                StructureId.STACK,
                DiagramId.RESIZE_COPY,
                stack
            )
        );
        assertEquals(
            DiagramId.STRUCTURE,
            model.selected(StructureId.STACK, stack).id()
        );
    }

    @Test
    void diagramSelectionDoesNotEnterApplicationState() {
        assertFalse(java.util.Arrays.stream(
            ApplicationState.class.getDeclaredFields()
        ).anyMatch(field ->
            field.getType() == DiagramId.class
                || field.getName().toLowerCase().contains("diagram")
        ));
    }
}
