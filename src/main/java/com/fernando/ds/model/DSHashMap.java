package com.fernando.ds.model;

public class DSHashMap extends DataStructure {

    private static final String EXPLANATION =
        "HashMap stores key-value pairs and provides very fast average lookup, insertion, and deletion by key.";

    private static final String EXAMPLE_USE =
        "Use HashMap for counting words, storing usernames with scores, or mapping IDs to objects.";

    private static final String API_OVERVIEW =
        "put(K key, V value), get(Object key), containsKey(Object key), remove(Object key), keySet(), size()";

    private static final String CODE_EXAMPLE =
        """
        HashMap<String, Integer> wordCount = new HashMap<>();

        wordCount.put("hello", 1);
        wordCount.put("world", 2);

        System.out.println(wordCount.get("hello")); // 1
        System.out.println(wordCount.containsKey("world")); // true
        """;

    public DSHashMap() {
        super(
            "HashMap",                 // name

            true,                      // keyValue
            true,                      // allowsDuplicates for values
            false,                     // indexed
            RemovalOrder.ANY,          // removalOrder

            10,                        // lookup
            10,                        // addDelete
            5,                         // memory
            0,                         // sorted

            false,                     // legacy

            EXPLANATION,
            EXAMPLE_USE,
            API_OVERVIEW,
            CODE_EXAMPLE
        );
    }
}