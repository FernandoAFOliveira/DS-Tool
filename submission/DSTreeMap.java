public class DSTreeMap extends DataStructure {

    private static final String EXPLANATION =
        "TreeMap stores key-value pairs in sorted key order and supports navigation methods such as finding nearby keys.";

    private static final String EXAMPLE_USE =
        "Use TreeMap when you need key-value mapping and also need the keys kept sorted.";

    private static final String API_OVERVIEW =
        "put(K key, V value), get(Object key), containsKey(Object key), firstKey(), lastKey(), lowerKey(K key), higherKey(K key)";

    private static final String CODE_EXAMPLE =
        """
        TreeMap<String, Integer> sortedMap = new TreeMap<>();

        sortedMap.put("banana", 2);
        sortedMap.put("apple", 1);
        sortedMap.put("cherry", 3);

        System.out.println(sortedMap.firstKey()); // apple
        System.out.println(sortedMap.lastKey()); // cherry
        System.out.println(sortedMap.lowerKey("banana")); // apple
        System.out.println(sortedMap.higherKey("banana")); // cherry
        """;

    public DSTreeMap() {
        super(
            "TreeMap",                 // name

            true,                      // keyValue
            true,                      // allowsDuplicates for values
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