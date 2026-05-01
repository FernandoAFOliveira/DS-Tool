package com.fernando.ds.model;

public class DSArrayDeque extends DataStructure {

    public DSArrayDeque() {
        super(
            "ArrayDeque",              // name

            false,                     // keyValue
            true,                      // allowsDuplicates
            false,                     // indexed
            RemovalOrder.DOUBLE_ENDED, // removalOrder

            3,                         // lookup
            9,                         // addDelete
            7,                         // memory
            0,                         // sorted

            false                     // legacy
        );
    }
}