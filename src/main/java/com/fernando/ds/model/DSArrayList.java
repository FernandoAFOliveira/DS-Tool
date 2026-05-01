package com.fernando.ds.model;

public class DSArrayList extends DataStructure {


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

            false                   // legacy
        );
    }
}