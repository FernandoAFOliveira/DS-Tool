/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: DSArrayDeque.java
 *
 * DSArrayDeque describes the ArrayDeque option used by the advisor.
 * It provides the structure's behavior ratings, explanation, common methods,
 * and example code shown in the GUI.
 */

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