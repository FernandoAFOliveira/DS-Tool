package com.fernando.ds.diagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.fernando.ds.gui.Theme;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.util.DiagramTemplateLoader;
import com.fernando.ds.util.MermaidResult;

class DiagramCatalogTest {

    private static final List<String> SUBJECT_SPECIFIC_TERMS = List.of(
        "ArrayList",
        "ArrayDeque",
        "HashMap",
        "HashSet",
        "TreeMap",
        "TreeSet",
        "PriorityQueue",
        "LinkedList",
        "malloc",
        "calloc",
        "realloc",
        "sizeof",
        "typedef",
        "#include"
    );

    @Test
    void everyStructureHasExactlyOneUniquePrimaryMapping() {
        Set<String> primaryResources = new HashSet<>();

        for (StructureId structureId : StructureId.values()) {
            List<DiagramDescriptor> diagrams =
                DiagramCatalog.get(structureId);
            assertFalse(diagrams.isEmpty());
            assertEquals(DiagramId.STRUCTURE, diagrams.getFirst().id());
            assertEquals(
                1,
                diagrams.stream()
                    .filter(item -> item.id() == DiagramId.STRUCTURE)
                    .count()
            );
            assertTrue(primaryResources.add(
                diagrams.getFirst().resourceName()
            ));
        }

        assertEquals(
            EnumSet.allOf(StructureId.class).size(),
            primaryResources.size()
        );
    }

    @Test
    void optionalMappingsAreStableOrderedAndIntentionallyBounded() {
        assertEquals(
            List.of(DiagramId.STRUCTURE, DiagramId.RESIZE_COPY),
            ids(StructureId.DYNAMIC_ARRAY)
        );
        assertEquals(
            List.of(DiagramId.STRUCTURE),
            ids(StructureId.STACK)
        );
        assertEquals(
            List.of(DiagramId.STRUCTURE, DiagramId.CIRCULAR_BUFFER),
            ids(StructureId.QUEUE)
        );
        assertEquals(
            List.of(DiagramId.STRUCTURE, DiagramId.ARRAY_LAYOUT),
            ids(StructureId.PRIORITY_QUEUE)
        );
        assertEquals(
            List.of(DiagramId.STRUCTURE),
            ids(StructureId.DEQUE)
        );
        assertEquals(
            List.of(DiagramId.STRUCTURE),
            ids(StructureId.HASH_SET)
        );
        assertEquals(
            List.of(DiagramId.STRUCTURE, DiagramId.BALANCING),
            ids(StructureId.ORDERED_SET)
        );
        assertEquals(
            List.of(
                DiagramId.STRUCTURE,
                DiagramId.COLLISION_HANDLING
            ),
            ids(StructureId.HASH_MAP)
        );
        assertEquals(
            List.of(DiagramId.STRUCTURE, DiagramId.BALANCING),
            ids(StructureId.ORDERED_MAP)
        );

        Set<String> conceptResources = new HashSet<>();
        for (StructureId structureId : StructureId.values()) {
            DiagramCatalog.get(structureId).stream()
                .map(DiagramDescriptor::resourceName)
                .forEach(conceptResources::add);
        }
        assertEquals(14, conceptResources.size());
    }

    @Test
    void everyMappedDiagramLoadsInEveryThemeAndRemainsNeutral() {
        for (StructureId structureId : StructureId.values()) {
            for (DiagramDescriptor descriptor :
                DiagramCatalog.get(structureId)) {
                for (Theme theme : Theme.values()) {
                    MermaidResult result =
                        DiagramTemplateLoader.getProcessedMermaid(
                            structureId,
                            descriptor.id(),
                            theme
                        );
                    assertFalse(result.mmdSource.isBlank());
                    assertFalse(result.mmdSource.contains("{{"));
                    assertFalse(result.backgroundColor.isBlank());
                    for (String forbidden : SUBJECT_SPECIFIC_TERMS) {
                        assertFalse(
                            result.mmdSource.contains(forbidden),
                            () -> descriptor.resourceName()
                                + " contains " + forbidden
                        );
                    }
                }
            }
        }
    }

    @Test
    void everyConceptDiagramHasWidePresentationMetadata() {
        Set<String> checkedResources = new HashSet<>();

        for (StructureId structureId : StructureId.values()) {
            for (DiagramDescriptor descriptor :
                DiagramCatalog.get(structureId)) {
                if (!checkedResources.add(descriptor.resourceName())) {
                    continue;
                }
                MermaidResult result =
                    DiagramTemplateLoader.getProcessedMermaid(
                        structureId,
                        descriptor.id(),
                        Theme.LIGHT
                    );

                assertTrue(
                    result.mmdSource.matches(
                        "(?sm).*^%% @title \\S.*$.*"
                    ),
                    descriptor.resourceName()
                );
                assertTrue(
                    result.mmdSource.matches(
                        "(?sm).*^%% @subtitle \\S.*$.*"
                    ),
                    descriptor.resourceName()
                );
                assertFalse(
                    result.mmdSource.contains("Title["),
                    descriptor.resourceName()
                );
            }
        }

        assertEquals(14, checkedResources.size());
    }

    @Test
    void descriptorRequiresSafePlainLabelsAndNeutralResourceNames() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new DiagramDescriptor(
                DiagramId.STRUCTURE,
                "<html>unsafe",
                "safe-name"
            )
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new DiagramDescriptor(
                DiagramId.STRUCTURE,
                "Structure",
                "JavaArrayList"
            )
        );
    }

    private static List<DiagramId> ids(StructureId structureId) {
        return DiagramCatalog.get(structureId).stream()
            .map(DiagramDescriptor::id)
            .toList();
    }
}
