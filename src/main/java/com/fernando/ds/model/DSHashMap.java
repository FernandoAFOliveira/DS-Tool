package com.fernando.ds.model;

public class DSHashMap extends DataStructure {
    public DSHashMap() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("HashMap", false, true, true, false, false, 10, 10, 5, 0);
		setAlternative("ArrayDeque");
    }
}
