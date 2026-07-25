package com.fernando.ds.knowledge;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fernando.ds.model.RemovalOrder;

/**
 * Built-in language-neutral data-structure knowledge used by the Advisor.
 */
public final class KnowledgeCatalog {

    private static final List<DataStructureKnowledge> ALL = List.of(
        knowledge(
            StructureId.DYNAMIC_ARRAY,
            "Dynamic array",
            false,
            true,
            true,
            Ordering.INDEX,
            RemovalOrder.ANY,
            9,
            5,
            8,
            "O(1) indexed access; O(n) value search",
            "Amortized O(1) append; O(n) insertion or removal in the middle",
            "Compact contiguous references with possible unused capacity",
            "Iterates in index order with good locality",
            List.of("Indexed collections", "Frequently traversed sequences"),
            List.of(StructureId.DEQUE)
        ),
        knowledge(
            StructureId.STACK,
            "Stack",
            false,
            true,
            false,
            Ordering.LIFO,
            RemovalOrder.LIFO,
            2,
            8,
            5,
            "O(1) access to the top; O(n) search",
            "O(1) push and pop at the top",
            "Implementation-dependent storage overhead",
            "Iteration order is implementation-dependent and is not removal order",
            List.of("Undo history", "Backtracking", "Expression evaluation"),
            List.of(StructureId.DEQUE)
        ),
        knowledge(
            StructureId.QUEUE,
            "Queue",
            false,
            true,
            false,
            Ordering.FIFO,
            RemovalOrder.FIFO,
            2,
            8,
            5,
            "O(1) access to the front; O(n) search",
            "Typically O(1) enqueue and dequeue",
            "Implementation-dependent storage overhead",
            "Iterates from the front toward the back",
            List.of("Task processing", "Breadth-first search", "Waiting lines"),
            List.of(StructureId.DEQUE, StructureId.PRIORITY_QUEUE)
        ),
        knowledge(
            StructureId.PRIORITY_QUEUE,
            "Priority queue",
            false,
            true,
            false,
            Ordering.PRIORITY,
            RemovalOrder.PRIORITY,
            2,
            6,
            6,
            "O(1) access to the next priority item; O(n) arbitrary search",
            "Typically O(log n) insertion and priority removal",
            "Usually backed by a compact heap",
            "Iteration does not guarantee priority order",
            List.of("Scheduling", "Graph algorithms", "Event processing"),
            List.of(StructureId.QUEUE)
        ),
        knowledge(
            StructureId.DEQUE,
            "Deque",
            false,
            true,
            false,
            Ordering.DOUBLE_ENDED,
            RemovalOrder.DOUBLE_ENDED,
            3,
            9,
            7,
            "O(1) access at either end; O(n) arbitrary search",
            "Typically O(1) insertion and removal at either end",
            "Array-backed implementations may reserve unused capacity",
            "Iterates consistently from one end to the other",
            List.of("Sliding windows", "Work queues", "Stack or queue behavior"),
            List.of(
                StructureId.DYNAMIC_ARRAY,
                StructureId.STACK,
                StructureId.QUEUE
            )
        ),
        knowledge(
            StructureId.HASH_SET,
            "Hash set",
            false,
            false,
            false,
            Ordering.NONE,
            RemovalOrder.ANY,
            9,
            9,
            6,
            "Average O(1) membership lookup; worst-case behavior varies",
            "Average O(1) insertion and removal",
            "Hash table capacity and bookkeeping require extra memory",
            "Iteration order is not inherently stable or sorted",
            List.of("Membership checks", "Deduplication", "Visited-item tracking"),
            List.of(StructureId.ORDERED_SET, StructureId.HASH_MAP)
        ),
        knowledge(
            StructureId.ORDERED_SET,
            "Ordered set",
            false,
            false,
            false,
            Ordering.SORTED,
            RemovalOrder.ANY,
            7,
            7,
            5,
            "Typically O(log n) membership and neighbor lookup",
            "Typically O(log n) insertion and removal",
            "Tree nodes require links and balancing metadata",
            "Iterates in sorted value order",
            List.of("Sorted unique values", "Range queries", "Neighbor lookup"),
            List.of(StructureId.HASH_SET, StructureId.ORDERED_MAP)
        ),
        knowledge(
            StructureId.HASH_MAP,
            "Hash map",
            true,
            true,
            false,
            Ordering.NONE,
            RemovalOrder.ANY,
            10,
            10,
            5,
            "Average O(1) lookup by key; worst-case behavior varies",
            "Average O(1) insertion and removal by key",
            "Hash table capacity and entries require extra memory",
            "Iteration order is not inherently stable or sorted",
            List.of("Indexes by identifier", "Counting", "Caches"),
            List.of(StructureId.ORDERED_MAP, StructureId.HASH_SET)
        ),
        knowledge(
            StructureId.ORDERED_MAP,
            "Ordered map",
            true,
            true,
            false,
            Ordering.SORTED,
            RemovalOrder.ANY,
            7,
            7,
            5,
            "Typically O(log n) lookup and neighbor lookup by key",
            "Typically O(log n) insertion and removal by key",
            "Tree entries require links and balancing metadata",
            "Iterates in sorted key order",
            List.of("Sorted indexes", "Range queries", "Nearest-key lookup"),
            List.of(StructureId.HASH_MAP, StructureId.ORDERED_SET)
        )
    );

    private static final Map<StructureId, DataStructureKnowledge> BY_ID =
        createIndex();

    private KnowledgeCatalog() {
        // Utility class
    }

    /** @return the complete immutable catalog in stable recommendation order */
    public static List<DataStructureKnowledge> getAll() {
        return ALL;
    }

    /**
     * Resolves knowledge for an abstract structure.
     *
     * @param id identifier to resolve
     * @return matching knowledge
     */
    public static DataStructureKnowledge get(StructureId id) {
        Objects.requireNonNull(id, "id");
        DataStructureKnowledge knowledge = BY_ID.get(id);
        if (knowledge == null) {
            throw new IllegalArgumentException("Unknown structure: " + id);
        }
        return knowledge;
    }

    private static DataStructureKnowledge knowledge(
        StructureId id,
        String displayName,
        boolean keyValueMapping,
        boolean allowsDuplicates,
        boolean indexedAccess,
        Ordering ordering,
        RemovalOrder removalOrder,
        int lookupRating,
        int insertionRemovalRating,
        int memoryRating,
        String lookupCost,
        String insertionRemovalCost,
        String memoryConsiderations,
        String iterationCharacteristics,
        List<String> commonUseCases,
        List<StructureId> relatedStructures
    ) {
        return new DataStructureKnowledge(
            id,
            displayName,
            keyValueMapping,
            allowsDuplicates,
            indexedAccess,
            ordering,
            removalOrder,
            lookupRating,
            insertionRemovalRating,
            memoryRating,
            lookupCost,
            insertionRemovalCost,
            memoryConsiderations,
            iterationCharacteristics,
            commonUseCases,
            relatedStructures
        );
    }

    private static Map<StructureId, DataStructureKnowledge> createIndex() {
        Map<StructureId, DataStructureKnowledge> index =
            new EnumMap<>(StructureId.class);
        for (DataStructureKnowledge knowledge : ALL) {
            if (index.put(knowledge.id(), knowledge) != null) {
                throw new IllegalStateException(
                    "Duplicate knowledge identifier: " + knowledge.id()
                );
            }
        }
        return Map.copyOf(index);
    }
}
