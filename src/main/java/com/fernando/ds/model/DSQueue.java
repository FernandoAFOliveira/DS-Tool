package com.fernando.ds.model;

public class DSQueue extends DataStructure {

    private static final String EXPLANATION =
        "Queue represents first-in, first-out behavior, where the first item added is the first item removed.";

    private static final String EXAMPLE_USE =
        "Use Queue for waiting lines, task processing, breadth-first search, or anything processed in arrival order.";

    private static final String API_OVERVIEW =
        "add(E e), offer(E e), poll(), peek(), remove(), size()";

    private static final String CODE_EXAMPLE =
        """
        Queue<String> queue = new LinkedList<>();

        queue.add("Alice");
        queue.add("Bob");

        System.out.println(queue.poll()); // Alice
        System.out.println(queue.peek()); // Bob
        """;

    public DSQueue() {
        super(
            "Queue",                   // name

            false,                     // keyValue
            true,                      // allowsDuplicates
            false,                     // indexed
            false,                     // navigable
            RemovalOrder.FIFO,         // removalOrder

            2,                         // lookup
            8,                         // addDelete
            5,                         // memory
            0,                         // sorted

            false,                     // legacy

            EXPLANATION,
            EXAMPLE_USE,
            API_OVERVIEW,
            CODE_EXAMPLE,
            "ArrayDeque"
        );
    }
}