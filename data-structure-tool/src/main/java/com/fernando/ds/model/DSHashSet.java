package com.fernando.ds.model;

public class DSHashSet extends DataStructure {
    public DSArrayList() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("HashSet", false, false, false, false, false, 9, 9, 6, 0);
		setAlternative("ArrayDeque");
    }

    @Override
    public double calculateMatchScore(UserRequirements req) {
        // Your logic will now consistently access these in a set order.
		return 0.0;
    }
}
