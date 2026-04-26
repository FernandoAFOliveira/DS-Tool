package com.fernando.ds.model;

public class DSStack extends DataStructure {

    private static final String EXPLANATION =
        "Stack represents last-in, first-out behavior, where the most recently added item is removed first.";

    private static final String EXAMPLE_USE =
        "Use Stack behavior for undo operations, expression evaluation, recursion simulation, or backtracking.";

    private static final String API_OVERVIEW =
        "push(E e), pop(), peek(), empty(), search(Object o)";

    private static final String CODE_EXAMPLE =
        """
        Stack<String> stack = new Stack<>();

        stack.push("first");
        stack.push("second");

        System.out.println(stack.pop()); // second
        System.out.println(stack.peek()); // first
        """;    

    public DSStack() {
        super(
            "Stack",                   // name

            false,                     // keyValue
            true,                      // allowsDuplicates
            false,                     // indexed
            false,                     // navigable
            RemovalOrder.LIFO,         // removalOrder

            2,                         // lookup
            8,                         // addDelete
            5,                         // memory
            0,                         // sorted

            true,                      // legacy

            EXPLANATION,
            EXAMPLE_USE,
            API_OVERVIEW,
            CODE_EXAMPLE,
            "ArrayDeque"
        );
    }
}