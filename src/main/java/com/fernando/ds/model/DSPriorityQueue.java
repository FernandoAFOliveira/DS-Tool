package com.fernando.ds.model;

public class DSPriorityQueue extends DataStructure {

    private static final String EXPLANATION =
        """
        PriorityQueue stores elements so that the highest-priority or lowest-priority item can be removed first.
        PriorityQueue does not keep all elements fully sorted. It only guarantees that peek() and poll() access the next priority item.
        """;

    private static final String EXAMPLE_USE =
        "Use PriorityQueue for scheduling tasks, processing jobs by priority, or algorithms that repeatedly need the next smallest item.";

    private static final String API_OVERVIEW =
        "add(E e), offer(E e), poll(), peek(), remove(Object o), size()";

    private static final String CODE_EXAMPLE =  
        """
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        pq.add(5);
        pq.add(2);
        pq.add(8);

        System.out.println(pq.poll()); // 2 (the smallest element)
        System.out.println(pq.peek()); // 5 (the next smallest element)
        """;

    public DSPriorityQueue() {
        super(
            "PriorityQueue",           // name

            false,                      // keyValue
            true,                       // allowsDuplicates
            false,                      // indexed
            RemovalOrder.PRIORITY,      // removalOrder

            2,                          // lookup
            6,                          // addDelete
            6,                          // memory
            0,                          // sorted / priority-based ordering

            false,                      // legacy

            EXPLANATION,
            EXAMPLE_USE,
            API_OVERVIEW,
            CODE_EXAMPLE
        );
    }
}   