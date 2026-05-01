package com.fernando.ds.model;

public class DSTreeSet extends DataStructure {
  

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

            false                     // legacy

        );
    }
}