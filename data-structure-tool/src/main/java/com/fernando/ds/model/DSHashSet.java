package com.fernando.ds.model;

public class DSHashSet extends DataStructure {
    public DSHashSet() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("HashSet", false, false, false, false, false, 9, 9, 6, 0);
		setAlternative("ArrayDeque");
    }
}
