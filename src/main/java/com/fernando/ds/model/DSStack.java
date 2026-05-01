package com.fernando.ds.model;

public class DSStack extends DataStructure {

  

    public DSStack() {
        super(
            "Stack",                   // name

            false,                     // keyValue
            true,                      // allowsDuplicates
            false,                     // indexed
            RemovalOrder.LIFO,         // removalOrder

            2,                         // lookup
            8,                         // addDelete
            5,                         // memory
            0,                         // sorted

            true                      // legacy

        );
    }
}