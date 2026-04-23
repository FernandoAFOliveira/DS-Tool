package com.fernando.ds.model;

public class DSPriorityQueue extends DataStructure {
    public DSPriorityQueue() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("PriorityQueue", false, true, false, false, false, 2, 6, 6, 8);
		setAlternative("ArrayDeque");
    }
}
