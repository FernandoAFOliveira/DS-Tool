package com.fernando.ds.model;

public class DSStack extends DataStructure {
    public DSStack() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("Stack", true, true, false, false, false, 2, 8, 5, 0);
		setAlternative("ArrayDeque");
    }
}
