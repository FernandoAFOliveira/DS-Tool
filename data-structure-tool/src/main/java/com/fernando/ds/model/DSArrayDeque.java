package com.fernando.ds.model;

public class DSArrayDeque extends DataStructure {
    public DSArrayList() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("ArrayDeque", false, true, false, false, true, 3, 9, 7, 0);
		setAlternative("ArrayDeque");
    }

    @Override
    public double calculateMatchScore(UserRequirements req) {
        // Your logic will now consistently access these in a set order.
		return 0.0;
    }
}
