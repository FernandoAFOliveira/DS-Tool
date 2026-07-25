package com.fernando.ds.subject;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.subject.SubjectStructureContent.CodeExample;
import com.fernando.ds.subject.SubjectStructureContent.Section;

/** Java implementation guidance used by the Java subject provider. */
final class JavaSubjectContentCatalog {

    private static final Map<StructureId, SubjectStructureContent> CONTENT =
        createContent();

    private JavaSubjectContentCatalog() {
    }

    static SubjectStructureContent get(StructureId id) {
        return CONTENT.get(id);
    }

    private static Map<StructureId, SubjectStructureContent> createContent() {
        Map<StructureId, SubjectStructureContent> content =
            new EnumMap<>(StructureId.class);

        content.put(StructureId.DYNAMIC_ARRAY, details(
            section(
                "Java implementation",
                "ArrayList<E> is Java's general-purpose resizable-array "
                    + "implementation of the List<E> interface."
            ),
            bullets(
                "Common API",
                "add, get, set, remove, contains, size",
                "Construct with new ArrayList<>() and program to List<E> "
                    + "when implementation-specific behavior is unnecessary."
            ),
            bullets(
                "Java caveats",
                "Removing while iterating requires Iterator.remove or an "
                    + "appropriate bulk operation.",
                "ArrayList stores references and cannot use primitive type "
                    + "parameters directly."
            ),
            example(
                "Create and update an ArrayList",
                """
                List<Integer> values = new ArrayList<>();
                values.add(10);
                values.add(20);
                int first = values.get(0);
                values.set(1, 25);
                """
            )
        ));
        content.put(StructureId.STACK, details(
            section(
                "Java implementation",
                "Use ArrayDeque<E> for new stack implementations. The older "
                    + "Stack<E> class is retained mainly for compatibility."
            ),
            bullets(
                "Common API",
                "push, pop, peek, isEmpty"
            ),
            bullets(
                "Java caveats",
                "ArrayDeque does not permit null elements.",
                "Stack extends Vector and carries legacy synchronization and "
                    + "API behavior."
            ),
            example(
                "Use an ArrayDeque as a stack",
                """
                Deque<String> stack = new ArrayDeque<>();
                stack.push("first");
                stack.push("second");
                String next = stack.pop();
                """
            )
        ));
        content.put(StructureId.QUEUE, details(
            section(
                "Java implementation",
                "Queue<E> is an interface. ArrayDeque<E> is a common "
                    + "general-purpose implementation for in-memory queues."
            ),
            bullets(
                "Common API",
                "offer, poll, peek, isEmpty",
                "offer and poll express non-throwing queue operations more "
                    + "clearly than add and remove."
            ),
            bullets(
                "Java caveats",
                "ArrayDeque does not permit null elements.",
                "Choose a concurrent queue when multiple threads share it."
            ),
            example(
                "Create and consume a queue",
                """
                Queue<String> queue = new ArrayDeque<>();
                queue.offer("first");
                queue.offer("second");
                String next = queue.poll();
                """
            )
        ));
        content.put(StructureId.PRIORITY_QUEUE, details(
            section(
                "Java implementation",
                "PriorityQueue<E> uses natural ordering or a supplied "
                    + "Comparator to determine the next element."
            ),
            bullets(
                "Common API",
                "offer, peek, poll, remove, size",
                "Use Comparator.reverseOrder() when larger values should be "
                    + "removed first."
            ),
            bullets(
                "Java caveats",
                "Iteration does not visit elements in priority order.",
                "Elements must be mutually comparable under the configured "
                    + "ordering."
            ),
            example(
                "Process values by priority",
                """
                Queue<Integer> priorities = new PriorityQueue<>();
                priorities.offer(5);
                priorities.offer(2);
                priorities.offer(8);
                int next = priorities.poll();
                """
            )
        ));
        content.put(StructureId.DEQUE, details(
            section(
                "Java implementation",
                "ArrayDeque<E> implements Deque<E> with a resizable array and "
                    + "supports both queue and stack-style APIs."
            ),
            bullets(
                "Common API",
                "addFirst, addLast, pollFirst, pollLast",
                "peekFirst, peekLast, push, pop"
            ),
            bullets(
                "Java caveats",
                "ArrayDeque does not permit null elements.",
                "It is not thread-safe without external coordination."
            ),
            example(
                "Work at both ends",
                """
                Deque<Integer> values = new ArrayDeque<>();
                values.addLast(10);
                values.addFirst(5);
                int front = values.removeFirst();
                """
            )
        ));
        content.put(StructureId.HASH_SET, details(
            section(
                "Java implementation",
                "HashSet<E> is backed by hashing and uses equals and hashCode "
                    + "to identify matching elements."
            ),
            bullets(
                "Common API",
                "add, contains, remove, clear, size"
            ),
            bullets(
                "Java caveats",
                "Mutable fields used by equals or hashCode must not change "
                    + "while an element is stored.",
                "Iteration order is unspecified."
            ),
            example(
                "Track unique values",
                """
                Set<String> seen = new HashSet<>();
                seen.add("alpha");
                boolean firstVisit = seen.add("beta");
                boolean known = seen.contains("alpha");
                """
            )
        ));
        content.put(StructureId.ORDERED_SET, details(
            section(
                "Java implementation",
                "TreeSet<E> implements NavigableSet<E> with natural ordering "
                    + "or a supplied Comparator."
            ),
            bullets(
                "Common API",
                "add, contains, remove, first, last",
                "lower, floor, higher, ceiling"
            ),
            bullets(
                "Java caveats",
                "The comparator must be consistent enough for set identity.",
                "Incompatible elements can cause a ClassCastException."
            ),
            example(
                "Find neighboring values",
                """
                NavigableSet<Integer> values = new TreeSet<>();
                values.add(10);
                values.add(30);
                Integer lower = values.floor(20);
                Integer higher = values.ceiling(20);
                """
            )
        ));
        content.put(StructureId.HASH_MAP, details(
            section(
                "Java implementation",
                "HashMap<K, V> uses key hash codes and equality to locate "
                    + "entries."
            ),
            bullets(
                "Common API",
                "put, get, getOrDefault, containsKey, remove",
                "entrySet, keySet, values, computeIfAbsent"
            ),
            bullets(
                "Java caveats",
                "Mutable key state used by equals or hashCode can make an "
                    + "entry effectively unreachable.",
                "HashMap is not thread-safe."
            ),
            example(
                "Count occurrences",
                """
                Map<String, Integer> counts = new HashMap<>();
                counts.merge("apple", 1, Integer::sum);
                counts.merge("apple", 1, Integer::sum);
                int total = counts.getOrDefault("apple", 0);
                """
            )
        ));
        content.put(StructureId.ORDERED_MAP, details(
            section(
                "Java implementation",
                "TreeMap<K, V> implements NavigableMap<K, V> using natural "
                    + "key ordering or a supplied Comparator."
            ),
            bullets(
                "Common API",
                "put, get, remove, firstKey, lastKey",
                "lowerKey, floorKey, higherKey, ceilingKey"
            ),
            bullets(
                "Java caveats",
                "Keys must be comparable under one consistent ordering.",
                "Natural-order TreeMap generally does not accept null keys."
            ),
            example(
                "Navigate sorted keys",
                """
                NavigableMap<Integer, String> values = new TreeMap<>();
                values.put(10, "ten");
                values.put(30, "thirty");
                Integer previous = values.floorKey(20);
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
