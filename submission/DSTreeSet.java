/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: DSTreeSet.java
 *
 * DSTreeSet describes the TreeSet option used by the advisor.
 * It provides the structure's behavior ratings, explanation, common methods,
 * and example code shown in the GUI.
 */

public class DSTreeSet extends DataStructure {

    private static final String EXPLANATION =
        "TreeSet stores unique elements in sorted order and supports navigation methods for finding nearby values.";

    private static final String EXAMPLE_USE =
        "Use TreeSet when you need unique values, sorted order, or operations like finding the next higher or lower value.";

    private static final String API_OVERVIEW =
        "add(E e), contains(Object o), remove(Object o), first(), last(), lower(E e), higher(E e), floor(E e), ceiling(E e)";

    private static final String CODE_EXAMPLE =
        """
        TreeSet<String> sortedSet = new TreeSet<>();

        sortedSet.add("banana");
        sortedSet.add("apple");
        sortedSet.add("cherry");

        System.out.println(sortedSet.first()); // apple
        System.out.println(sortedSet.last()); // cherry
        System.out.println(sortedSet.lower("banana")); // apple
        System.out.println(sortedSet.higher("banana")); // cherry
        """;    

    public DSTreeSet() {
        super(
            "TreeSet",                 // name

            false,                     // keyValue
            false,                     // allowsDuplicates
            false,                     // indexed
            RemovalOrder.ANY,          // removalOrder

            7,                         // lookup
            7,                         // addDelete
            5,                         // memory
            10,                        // sorted

            false,                     // legacy

            EXPLANATION,
            EXAMPLE_USE,
            API_OVERVIEW,
            CODE_EXAMPLE
        );
    }
}