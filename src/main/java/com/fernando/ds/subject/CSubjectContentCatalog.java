package com.fernando.ds.subject;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.subject.SubjectStructureContent.CodeExample;
import com.fernando.ds.subject.SubjectStructureContent.Section;

/** C implementation guidance used by the C subject provider. */
final class CSubjectContentCatalog {

    private static final Map<StructureId, SubjectStructureContent> CONTENT =
        createContent();

    private CSubjectContentCatalog() {
    }

    static SubjectStructureContent get(StructureId id) {
        return CONTENT.get(id);
    }

    private static Map<StructureId, SubjectStructureContent> createContent() {
        Map<StructureId, SubjectStructureContent> content =
            new EnumMap<>(StructureId.class);

        content.put(StructureId.DYNAMIC_ARRAY, details(
            section(
                "C implementation strategy",
                "Store a pointer, logical length, and capacity in a struct. "
                    + "Grow the allocation with realloc when capacity is "
                    + "exhausted. This is an application-owned strategy, not "
                    + "a C standard collection."
            ),
            bullets(
                "Typical representation",
                "Element pointer, size, and capacity fields",
                "Initialization, reserve, append, and destroy helper functions"
            ),
            bullets(
                "C ownership caveats",
                "Specify whether the array owns pointed-to elements.",
                "Use a temporary pointer for realloc so allocation failure "
                    + "does not lose the original block."
            ),
            example(
                "Dynamic-array shape",
                """
                typedef struct {
                    int *items;
                    size_t size;
                    size_t capacity;
                } IntVector;
                """
            )
        ));
        content.put(StructureId.STACK, details(
            section(
                "C implementation strategy",
                "Use a growable array with a top index, or linked nodes when "
                    + "stable individual allocations are preferred. C has no "
                    + "standard stack collection."
            ),
            bullets(
                "Typical operations",
                "stack_init, stack_push, stack_pop, stack_peek, stack_destroy"
            ),
            bullets(
                "C ownership caveats",
                "Define how underflow is reported.",
                "Release the backing allocation and any owned elements."
            ),
            example(
                "Array-backed stack shape",
                """
                typedef struct {
                    int *items;
                    size_t size;
                    size_t capacity;
                } IntStack;
                """
            )
        ));
        content.put(StructureId.QUEUE, details(
            section(
                "C implementation strategy",
                "A circular buffer tracks front, length, and capacity so "
                    + "storage can wrap without shifting every element. This "
                    + "is a custom strategy rather than a standard C queue."
            ),
            bullets(
                "Typical operations",
                "queue_init, queue_enqueue, queue_dequeue, queue_front",
                "Use modular indexing to advance front and insertion slots."
            ),
            bullets(
                "C ownership caveats",
                "Define full-buffer growth and empty-queue behavior.",
                "Document whether dequeued pointer values transfer ownership."
            ),
            example(
                "Circular-buffer queue shape",
                """
                typedef struct {
                    int *items;
                    size_t front;
                    size_t size;
                    size_t capacity;
                } IntQueue;
                """
            )
        ));
        content.put(StructureId.PRIORITY_QUEUE, details(
            section(
                "C implementation strategy",
                "Store entries in an array-backed binary heap and supply a "
                    + "comparison function when priorities are not plain "
                    + "numbers. C has no standard priority-queue collection."
            ),
            bullets(
                "Typical operations",
                "heap_init, heap_push, heap_peek, heap_pop, heap_destroy",
                "Sift up after insertion and sift down after removal."
            ),
            bullets(
                "C ownership caveats",
                "Keep comparator and element lifetime rules with the heap.",
                "Handle allocation failure without changing the existing heap."
            ),
            example(
                "Binary-heap shape",
                """
                typedef struct {
                    int *items;
                    size_t size;
                    size_t capacity;
                } IntMinHeap;
                """
            )
        ));
        content.put(StructureId.DEQUE, details(
            section(
                "C implementation strategy",
                "Use a circular buffer with front and length metadata to "
                    + "support both ends. It is an application-defined "
                    + "strategy; the C standard library supplies no deque."
            ),
            bullets(
                "Typical operations",
                "deque_push_front, deque_push_back",
                "deque_pop_front, deque_pop_back, deque_destroy"
            ),
            bullets(
                "C ownership caveats",
                "Define growth before front or back insertion.",
                "Keep wraparound calculations in tested helper functions."
            ),
            example(
                "Circular-buffer deque shape",
                """
                typedef struct {
                    int *items;
                    size_t front;
                    size_t size;
                    size_t capacity;
                } IntDeque;
                """
            )
        ));
        content.put(StructureId.HASH_SET, details(
            section(
                "C implementation strategy",
                "Build a hash table that stores keys only, using separate "
                    + "chaining or open addressing. The hash and equality "
                    + "functions are application choices, not a standard C "
                    + "collection API."
            ),
            bullets(
                "Typical operations",
                "set_init, set_insert, set_contains, set_remove, set_destroy",
                "Track empty and deleted slots explicitly with open addressing."
            ),
            bullets(
                "C ownership caveats",
                "Document whether inserted keys are copied or borrowed.",
                "Rehashing must preserve entries if allocation fails."
            ),
            example(
                "Chained hash-set entry",
                """
                typedef struct SetNode {
                    char *key;
                    struct SetNode *next;
                } SetNode;
                """
            )
        ));
        content.put(StructureId.ORDERED_SET, details(
            section(
                "C implementation strategy",
                "Use a balanced search tree whose nodes store one value and "
                    + "balancing metadata. The tree and comparator are custom "
                    + "application components, not a standard C collection."
            ),
            bullets(
                "Typical operations",
                "set_insert, set_find, set_remove, set_lower_bound",
                "Rotations and balance updates belong in private helpers."
            ),
            bullets(
                "C ownership caveats",
                "Specify value ownership and comparator lifetime.",
                "Free every node and any owned value during destruction."
            ),
            example(
                "Balanced-set node shape",
                """
                typedef struct SetNode {
                    int value;
                    int height;
                    struct SetNode *left;
                    struct SetNode *right;
                } SetNode;
                """
            )
        ));
        content.put(StructureId.HASH_MAP, details(
            section(
                "C implementation strategy",
                "Build a hash table of key-value entries using chaining or "
                    + "open addressing. C does not provide a standard hash-map "
                    + "collection equivalent to Java's HashMap."
            ),
            bullets(
                "Typical operations",
                "map_init, map_put, map_get, map_remove, map_destroy",
                "Provide hash and equality functions appropriate to the key."
            ),
            bullets(
                "C ownership caveats",
                "State whether keys and values are copied, borrowed, or owned.",
                "Destruction callbacks can make ownership rules explicit."
            ),
            example(
                "Chained map entry",
                """
                typedef struct MapEntry {
                    char *key;
                    void *value;
                    struct MapEntry *next;
                } MapEntry;
                """
            )
        ));
        content.put(StructureId.ORDERED_MAP, details(
            section(
                "C implementation strategy",
                "Use a balanced search tree whose nodes store keys and values. "
                    + "Ordering comes from an application-supplied comparator; "
                    + "there is no standard C ordered-map collection."
            ),
            bullets(
                "Typical operations",
                "map_put, map_get, map_remove, map_lower_bound",
                "Tree rotations and balance maintenance stay internal."
            ),
            bullets(
                "C ownership caveats",
                "Define ownership independently for keys and values.",
                "All nodes and owned payloads must be released on destruction."
            ),
            example(
                "Balanced-map node shape",
                """
                typedef struct MapNode {
                    char *key;
                    void *value;
                    int height;
                    struct MapNode *left;
                    struct MapNode *right;
                } MapNode;
                """
            )
        ));

        return Map.copyOf(content);
    }

    private static SubjectStructureContent details(
        Section implementation,
        Section operations,
        Section caveats,
        CodeExample example
    ) {
        return new SubjectStructureContent(
            List.of(implementation, operations, caveats),
            List.of(example)
        );
    }

    private static Section section(String title, String paragraph) {
        return new Section(title, List.of(paragraph), List.of());
    }

    private static Section bullets(String title, String... values) {
        return new Section(title, List.of(), List.of(values));
    }

    private static CodeExample example(String title, String source) {
        return new CodeExample(title, source);
    }
}
