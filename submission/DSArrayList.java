/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: DSArrayList.java
 *
 * DSArrayList describes the ArrayList option used by the advisor.
 * It provides the structure's behavior ratings, explanation, common methods,
 * and example code shown in the GUI.
 */

public class DSArrayList extends DataStructure {

    private static final String EXPLANATION =
        "Resizable array implementation of the List interface. Provides fast random access but slower insertions/deletions in the middle.";

    private static final String EXAMPLE_USE =
        "Use ArrayList when storing items where you often access elements by index, such as a list of users, products, or scores.";

    private static final String API_OVERVIEW =
        "Key methods: add(), get(), set(), remove(), size(), contains()";
    
    private static final String CODE_EXAMPLE =
        """
        ArrayList<String> list = new ArrayList<>();

        list.add("Alice");
        list.add("Bob");

        System.out.println(list.get(0)); // Alice

        list.remove("Bob");
        System.out.println(list.size()); // 1
        """;

    public DSArrayList() {
        super(
            "ArrayList",               // name

            false,                     // keyValue
            true,                      // allowsDuplicates
            true,                      // indexed
            RemovalOrder.ANY,          // removalOrder

            9,                         // lookup
            5,                         // addDelete
            8,                         // memory
            0,                         // sorted

            false,                     // legacy

            EXPLANATION,
            EXAMPLE_USE,
            API_OVERVIEW,
            CODE_EXAMPLE
        );
    }
}