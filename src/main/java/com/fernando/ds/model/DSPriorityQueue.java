package com.fernando.ds.model;

public class DSPriorityQueue extends DataStructure {

    public DSPriorityQueue() {
        super(
            "PriorityQueue",           // name

            false,                      // keyValue
            true,                       // allowsDuplicates
            false,                      // indexed
            RemovalOrder.PRIORITY,      // removalOrder

            2,                          // lookup
            6,                          // addDelete
            6,                          // memory
            0,                          // sorted / priority-based ordering

            false                      // legacy
        );
    }
}   