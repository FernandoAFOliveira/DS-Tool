package com.fernando.ds.knowledge;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fernando.ds.model.RemovalOrder;

/**
 * Built-in language-neutral data-structure knowledge shared by experiences.
 */
public final class KnowledgeCatalog {

    private static final List<DataStructureKnowledge> ALL = List.of(
        knowledge(
            StructureId.DYNAMIC_ARRAY,
            "Dynamic array",
            "A sequence stored in a resizable contiguous array, combining "
                + "fast positional access with automatic capacity growth.",
            List.of(
                "Fast indexed access",
                "Efficient iteration and append operations",
                "Compact storage compared with node-based structures"
            ),
            List.of(
                "Middle insertions and removals require shifting elements",
                "Capacity growth may allocate and copy storage"
            ),
            List.of(
                "Get or replace by index",
                "Append, insert, and remove values",
                "Search and iterate in index order"
            ),
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
            "A last-in, first-out collection where insertion and removal "
                + "happen at the same accessible end.",
            List.of(
                "Constant-time access to the most recent item",
                "Simple ordering for nested or reversible work"
            ),
            List.of(
                "Only the top item is directly accessible",
                "Searching requires linear traversal"
            ),
            List.of("Push", "Pop", "Peek", "Check whether empty"),
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
            "A first-in, first-out collection that accepts items at the back "
                + "and processes them from the front.",
            List.of(
                "Predictable arrival-order processing",
                "Efficient insertion and removal at opposite ends"
            ),
            List.of(
                "No efficient arbitrary indexed access",
                "Searching generally requires linear traversal"
            ),
            List.of("Enqueue", "Dequeue", "Peek at the front", "Iterate"),
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
            "A collection that exposes the next item according to priority "
                + "rather than insertion order.",
            List.of(
                "Efficient access to the next priority item",
                "Well suited to scheduling and greedy algorithms"
            ),
            List.of(
                "Does not provide fully sorted iteration",
                "Arbitrary lookup and removal can be expensive"
            ),
            List.of(
                "Insert with priority",
                "Peek at the next item",
                "Remove the next priority item"
            ),
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
            "A double-ended queue supporting efficient insertion, removal, "
                + "and inspection at both ends.",
            List.of(
                "Efficient operations at both ends",
                "Can provide stack or queue behavior"
            ),
            List.of(
                "No efficient arbitrary indexed access",
                "Searching generally requires linear traversal"
            ),
            List.of(
                "Add or remove at the front",
                "Add or remove at the back",
                "Peek at either end"
            ),
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
            "A collection of unique values organized by hashing for fast "
                + "membership operations.",
            List.of(
                "Fast average membership checks",
                "Automatically enforces uniqueness"
            ),
            List.of(
                "No inherent sorted or stable iteration order",
                "Hash table capacity adds memory overhead"
            ),
            List.of("Add", "Remove", "Contains", "Iterate unique values"),
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
            "A collection of unique values maintained in sorted order, "
                + "typically by a balanced search tree.",
            List.of(
                "Maintains unique values in sorted order",
                "Supports range and nearest-neighbor queries"
            ),
            List.of(
                "Basic operations are slower than average hash-set operations",
                "Tree nodes require additional memory"
            ),
            List.of(
                "Add", "Remove", "Contains", "Find neighbors", "Range iteration"
            ),
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
            "A key-value mapping organized by hashing for fast average access "
                + "through unique keys.",
            List.of(
                "Fast average lookup and update by key",
                "Natural representation for indexes and associations"
            ),
            List.of(
                "No inherent sorted or stable key iteration order",
                "Hash table entries add memory overhead"
            ),
            List.of(
                "Put or replace by key",
                "Get or remove by key",
                "Test for a key",
                "Iterate entries"
            ),
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
            "A key-value mapping that maintains keys in sorted order, "
                + "typically using a balanced search tree.",
            List.of(
                "Maintains sorted keys",
                "Supports range and nearest-key queries"
            ),
            List.of(
                "Basic operations are slower than average hash-map operations",
                "Tree entries require additional memory"
            ),
            List.of(
                "Put, get, or remove by key",
                "Find neighboring keys",
                "Iterate entries in key order"
            ),
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

    /** @return the complete immutable catalog in stable display order */
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
        String description,
        List<String> strengths,
        List<String> weaknesses,
        List<String> supportedOperations,
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
            description,
            strengths,
            weaknesses,
            supportedOperations,
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
