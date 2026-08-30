package com.fernando.ds.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fernando.ds.diagram.DiagramCatalog;
import com.fernando.ds.diagram.DiagramDescriptor;
import com.fernando.ds.diagram.DiagramId;
import com.fernando.ds.gui.Theme;
import com.fernando.ds.knowledge.StructureId;

class DiagramTemplateLoaderTest {

    @Test
    void resolvesEveryAbstractStructureToItsPrimaryDiagram() {
        for (StructureId structureId : StructureId.values()) {
            MermaidResult result =
                DiagramTemplateLoader.getProcessedMermaid(
                    structureId,
                    Theme.LIGHT
                );

            assertNotNull(result.mmdSource);
            assertFalse(result.mmdSource.isBlank());
            assertNotNull(result.backgroundColor);
            assertEquals(
                DiagramId.STRUCTURE,
                DiagramCatalog.primary(structureId).id()
            );
        }
    }

    @Test
    void missingOptionalDiagramFallsBackToPrimary() {
        DiagramDescriptor primary = new DiagramDescriptor(
            DiagramId.STRUCTURE,
            "Structure",
            "queue-structure"
        );
        DiagramDescriptor missing = new DiagramDescriptor(
            DiagramId.CIRCULAR_BUFFER,
            "Optional",
            "missing-optional-diagram"
        );

        MermaidResult expected =
            DiagramTemplateLoader.getProcessedMermaid(
                List.of(primary),
                DiagramId.STRUCTURE,
                Theme.LIGHT
            );
        MermaidResult actual =
            DiagramTemplateLoader.getProcessedMermaid(
                List.of(primary, missing),
                DiagramId.CIRCULAR_BUFFER,
                Theme.LIGHT
            );

        assertEquals(expected.mmdSource, actual.mmdSource);
        assertEquals(expected.backgroundColor, actual.backgroundColor);
    }

    @Test
    void missingPrimaryDiagramFailsWithoutBlankContent() {
        DiagramDescriptor missing = new DiagramDescriptor(
            DiagramId.STRUCTURE,
            "Structure",
            "missing-primary-diagram"
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> DiagramTemplateLoader.getProcessedMermaid(
                List.of(missing),
                DiagramId.STRUCTURE,
                Theme.LIGHT
            )
        );
    }
}
