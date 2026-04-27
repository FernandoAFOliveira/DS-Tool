public class DSHashSet extends DataStructure {

    private static final String EXPLANATION =
        "HashSet stores unique elements and provides fast average add, remove, and contains operations.";

    private static final String EXAMPLE_USE =
        "Use HashSet when you need to remove duplicates or quickly check whether an item has already appeared.";

    private static final String API_OVERVIEW =
        "add(E e), contains(Object o), remove(Object o), clear(), size()";

    private static final String CODE_EXAMPLE =
        """
        HashSet<String> uniqueItems = new HashSet<>();

        uniqueItems.add("apple");
        uniqueItems.add("banana");
        uniqueItems.add("apple"); // This won't be added again

        System.out.println(uniqueItems.contains("apple")); // true
        System.out.println(uniqueItems.size()); // 2
        """;

    public DSHashSet() {
        super(
            "HashSet",                 // name

            false,                     // keyValue
            false,                     // allowsDuplicates
            false,                     // indexed
            RemovalOrder.ANY,          // removalOrder

            9,                         // lookup
            9,                         // addDelete
            6,                         // memory
            0,                         // sorted

            false,                     // legacy

            EXPLANATION,
            EXAMPLE_USE,
            API_OVERVIEW,
            CODE_EXAMPLE
        );
    }
}