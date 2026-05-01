package com.fernando.ds.model;

public class DSHashSet extends DataStructure {

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

            false                     // legacy

        );
    }
}