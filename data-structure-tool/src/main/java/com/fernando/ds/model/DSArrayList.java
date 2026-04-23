package com.fernando.ds.model;

public class DSArrayList extends DataStructure {
    public DSArrayList() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("ArrayList", false, true, false, false, false, 8, 9, 7, 0);
		setAlternative("ArrayDeque");
    }
}