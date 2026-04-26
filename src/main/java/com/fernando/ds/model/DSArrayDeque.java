package com.fernando.ds.model;

public class DSArrayDeque extends DataStructure {

    private static final String EXPLANATION =
        "ArrayDeque is a resizable double-ended queue. It is useful when you need efficient insertion and removal from both the front and back.";

    private static final String EXAMPLE_USE =
        "Use ArrayDeque for queues, stacks, undo systems, or processing items from both ends.";

    private static final String API_OVERVIEW =
        "addFirst(E e), addLast(E e), pollFirst(), pollLast(), peekFirst(), peekLast(), size()";

        private static final String CODE_EXAMPLE =
        """
        ArrayDeque<String> deque = new ArrayDeque<>();

        deque.addFirst("Alice");
        deque.addLast("Bob");

        System.out.println(deque.pollFirst()); // Alice
        System.out.println(deque.pollLast());  // Bob
        """;

    public DSArrayDeque() {
        super(
            "ArrayDeque",              // name

            false,                     // keyValue
            true,                      // allowsDuplicates
            false,                     // indexed
            RemovalOrder.DOUBLE_ENDED, // removalOrder

            3,                         // lookup
            9,                         // addDelete
            7,                         // memory
            0,                         // sorted

            false,                     // legacy

            EXPLANATION,
            EXAMPLE_USE,
            API_OVERVIEW,
            CODE_EXAMPLE
        );
    }
}