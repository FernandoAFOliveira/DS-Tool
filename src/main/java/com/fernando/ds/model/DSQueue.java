package com.fernando.ds.model;

public class DSQueue extends DataStructure {

    public DSQueue() {
        super(
            "Queue",                   // name

            false,                     // keyValue
            true,                      // allowsDuplicates
            false,                     // indexed
            RemovalOrder.FIFO,         // removalOrder

            2,                         // lookup
            8,                         // addDelete
            5,                         // memory
            0,                         // sorted

            false                     // legacy

        );
    }
}