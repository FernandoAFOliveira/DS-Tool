package com.fernando.ds.model;

public class DSQueue extends DataStructure {
    public DSQueue() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("Queue", false, true, false, false, false, 2, 8, 5, 0);
		setAlternative("ArrayDeque");
    }
}
