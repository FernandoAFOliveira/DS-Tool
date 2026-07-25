package com.fernando.ds.subject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fernando.ds.model.DataStructure;

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
