package com.fernando.ds.model;

public class DSHashMap extends DataStructure {



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

            false                   // legacy
        );
    }
}