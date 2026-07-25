package com.fernando.ds.knowledge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fernando.ds.model.RemovalOrder;

class KnowledgeCatalogTest {

    @Test
    void containsTheExistingNineAbstractStructuresInStableOrder() {
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
            KnowledgeCatalog.getAll().stream()
                .map(DataStructureKnowledge::id)
                .toList()
        );
    }

    @Test
    void suppliesEveryRoadmapKnowledgeConceptForEveryStructure() {
        EnumSet<StructureId> knownIds = EnumSet.allOf(StructureId.class);

        for (DataStructureKnowledge knowledge : KnowledgeCatalog.getAll()) {
            assertFalse(knowledge.displayName().isBlank());
            assertFalse(knowledge.lookupCost().isBlank());
            assertFalse(knowledge.insertionRemovalCost().isBlank());
            assertFalse(knowledge.memoryConsiderations().isBlank());
            assertFalse(knowledge.iterationCharacteristics().isBlank());
            assertFalse(knowledge.commonUseCases().isEmpty());
            assertTrue(knownIds.containsAll(knowledge.relatedStructures()));
            assertFalse(knowledge.relatedStructures().contains(knowledge.id()));
            assertEquals(knowledge, KnowledgeCatalog.get(knowledge.id()));
        }
    }

    @Test
    void distinguishesCoreCapabilitiesWithoutJavaClassNames() {
        DataStructureKnowledge dynamicArray = KnowledgeCatalog.get(
            StructureId.DYNAMIC_ARRAY
        );
        DataStructureKnowledge hashMap = KnowledgeCatalog.get(
            StructureId.HASH_MAP
        );
        DataStructureKnowledge orderedSet = KnowledgeCatalog.get(
            StructureId.ORDERED_SET
        );

        assertTrue(dynamicArray.indexedAccess());
        assertFalse(dynamicArray.keyValueMapping());
        assertTrue(hashMap.keyValueMapping());
        assertTrue(hashMap.allowsDuplicates());
        assertTrue(orderedSet.isSorted());
        assertFalse(orderedSet.allowsDuplicates());

        for (DataStructureKnowledge knowledge : KnowledgeCatalog.getAll()) {
            assertFalse(knowledge.displayName().contains("ArrayList"));
            assertFalse(knowledge.displayName().contains("HashMap"));
            assertFalse(knowledge.displayName().contains("Tree"));
        }
    }

    @Test
    void catalogAndNestedListsAreImmutable() {
        assertThrows(
            UnsupportedOperationException.class,
            () -> KnowledgeCatalog.getAll().clear()
        );
        assertThrows(
            UnsupportedOperationException.class,
            () -> KnowledgeCatalog.get(StructureId.QUEUE)
                .commonUseCases()
                .clear()
        );
    }

    @Test
    void rejectsIncompleteOrInvalidKnowledge() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new DataStructureKnowledge(
                StructureId.QUEUE,
                " ",
                false,
                true,
                false,
                Ordering.FIFO,
                RemovalOrder.FIFO,
                2,
                8,
                5,
                "O(1)",
                "O(1)",
                "Implementation dependent",
                "Front to back",
                List.of("Task processing"),
                List.of(StructureId.DEQUE)
            )
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new DataStructureKnowledge(
                StructureId.QUEUE,
                "Queue",
                false,
                true,
                false,
                Ordering.FIFO,
                RemovalOrder.FIFO,
                11,
                8,
                5,
                "O(1)",
                "O(1)",
                "Implementation dependent",
                "Front to back",
                List.of("Task processing"),
                List.of(StructureId.DEQUE)
            )
        );
    }
}
