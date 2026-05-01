package com.fernando.ds.model;

public class DSTreeMap extends DataStructure {

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

            false                     // legacy

        );
    }
}