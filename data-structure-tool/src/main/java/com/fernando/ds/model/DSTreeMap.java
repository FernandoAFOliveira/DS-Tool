package com.fernando.ds.model;

public class DSTreeMap extends DataStructure {
    public DSTreeMap() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("TreeMap", false, true, true, true, false, 7, 7, 5, 10);
		setAlternative("ArrayDeque");
    }
}
