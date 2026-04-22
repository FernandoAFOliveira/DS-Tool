package com.fernando.ds.model;

public class DSHashMap extends DataStructure {
    public DSHashMap() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("HashMap", false, true, true, false, false, 10, 10, 5, 0);
		setAlternative("ArrayDeque");
    }

    @Override
    public double calculateMatchScore(UserRequirements req) {
        // Your logic will now consistently access these in a set order.
		return 0.0;
    }
}
