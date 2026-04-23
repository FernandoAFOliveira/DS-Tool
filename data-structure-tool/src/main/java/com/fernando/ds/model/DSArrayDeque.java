package com.fernando.ds.model;


public class DSArrayDeque extends DataStructure {
    public DSArrayDeque() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("ArrayDeque", false, true, false, false, true, 3, 9, 7, 0);
		setAlternative("ArrayDeque");
    }
}
