package com.fernando.ds.subject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fernando.ds.model.DataStructure;
import com.fernando.ds.knowledge.StructureId;

class SubjectProviderRegistryTest {

    @Test
    void registersTheFourRoadmapSubjectsInStableOrder() {
        SubjectProviderRegistry registry = registry();

        assertEquals(
            List.of(
                SubjectId.JAVA,
                SubjectId.C,
                SubjectId.CPP,
                SubjectId.PYTHON
            ),
            registry.getAll().stream().map(SubjectProvider::id).toList()
        );
        assertEquals(
            List.of("Java", "C", "C++", "Python"),
            registry.getAll().stream()
                .map(SubjectProvider::displayName)
                .toList()
        );
    }

    @Test
    void javaProviderSuppliesTheExistingStructuresAsFreshInstances() {
        SubjectProvider java = registry().get(SubjectId.JAVA);

        List<DataStructure> first = java.getDataStructures();
        List<DataStructure> second = java.getDataStructures();

        assertTrue(java.isEnabled());
        assertEquals(
            List.of(
                "ArrayList",
                "Stack",
                "Queue",
                "PriorityQueue",
                "ArrayDeque",
                "HashSet",
                "TreeSet",
                "HashMap",
                "TreeMap"
            ),
            first.stream().map(DataStructure::getName).toList()
        );
        assertNotSame(first.getFirst(), second.getFirst());
        assertEquals(
            List.of(
                StructureId.DYNAMIC_ARRAY,
                StructureId.STACK,
                StructureId.QUEUE,
                StructureId.PRIORITY_QUEUE,
                StructureId.DEQUE,
                StructureId.HASH_SET,
                StructureId.ORDERED_SET,
                StructureId.HASH_MAP,
                StructureId.ORDERED_MAP
            ),
            first.stream().map(DataStructure::getStructureId).toList()
        );
        assertEquals(
            "ArrayList",
            java.getRepresentation(StructureId.DYNAMIC_ARRAY)
                .orElseThrow()
                .getName()
        );
        assertThrows(
            NullPointerException.class,
            () -> java.getRepresentation(null)
        );
    }

    @Test
    void unfinishedSubjectsAreUnavailableAndHaveNoStructures() {
        SubjectProviderRegistry registry = registry();

        for (SubjectId id : List.of(
            SubjectId.C,
            SubjectId.CPP,
            SubjectId.PYTHON
        )) {
            SubjectProvider provider = registry.get(id);
            assertFalse(provider.isEnabled());
            assertTrue(provider.getDataStructures().isEmpty());
        }
    }

    @Test
    @SuppressWarnings("deprecation")
    void javaRepresentationsRetainKnowledgeBackedCompatibilityAccessors() {
        DataStructure arrayList = registry()
            .get(SubjectId.JAVA)
            .getRepresentation(StructureId.DYNAMIC_ARRAY)
            .orElseThrow();

        assertTrue(arrayList.isDuplicates());
        assertTrue(arrayList.isIndexed());
        assertFalse(arrayList.isKeys());
        assertEquals(9, arrayList.getLookup());
        assertEquals(5, arrayList.getAddDelete());
        assertEquals(8, arrayList.getMemory());
        assertEquals(0, arrayList.getSorted());
    }

    @Test
    void rejectsDuplicateAndMissingProviders() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new SubjectProviderRegistry(
                List.of(new JavaSubjectProvider(), new JavaSubjectProvider())
            )
        );

        SubjectProviderRegistry registry = new SubjectProviderRegistry(
            List.of(new JavaSubjectProvider())
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> registry.get(SubjectId.C)
        );
    }

    private static SubjectProviderRegistry registry() {
        return new SubjectProviderRegistry(List.of(
            new JavaSubjectProvider(),
            new UnavailableSubjectProvider(SubjectId.C, "C"),
            new UnavailableSubjectProvider(SubjectId.CPP, "C++"),
            new UnavailableSubjectProvider(SubjectId.PYTHON, "Python")
        ));
    }
}
