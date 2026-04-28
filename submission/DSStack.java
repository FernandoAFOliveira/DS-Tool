/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: DSStack.java
 *
 * DSStack describes the Stack option used by the advisor.
 * It provides the structure's behavior ratings, explanation, common methods,
 * and example code shown in the GUI.
 */

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
            RemovalOrder.LIFO,         // removalOrder

            2,                         // lookup
            8,                         // addDelete
            5,                         // memory
            0,                         // sorted

            true,                      // legacy

            EXPLANATION,
            EXAMPLE_USE,
            API_OVERVIEW,
            CODE_EXAMPLE
        );
    }
}