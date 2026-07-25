package com.fernando.ds.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.fernando.ds.gui.Theme;
import com.fernando.ds.knowledge.StructureId;

class DiagramTemplateLoaderTest {

    @Test
    void resolvesEveryAbstractStructureToAnExistingSharedDiagram() {
        for (StructureId structureId : StructureId.values()) {
            MermaidResult result =
                DiagramTemplateLoader.getProcessedMermaid(
                    structureId,
                    Theme.LIGHT
                );

            assertNotNull(result.mmdSource);
            assertFalse(result.mmdSource.isBlank());
            assertNotNull(result.backgroundColor);
        }
    }
}
