package com.fernando.ds.model;

public class DSQueue extends DataStructure {
    public DSArrayList() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("Queue", false, true, false, false, false, 2, 8, 5, 0);
		setAlternative("ArrayDeque");
    }

    @Override
    public double calculateMatchScore(UserRequirements req) {
        // Your logic will now consistently access these in a set order.
		return 0.0;
    }
}
