package com.fernando.ds.model;

public class DSTreeSet extends DataStructure {
    public DSTreeSet() {
		// Name, legacy | duplicates, keys, navigable, doubleEnded | lookup, addDelete, memory, sorted
        super("TreeSet", false, false, false, true, false, 7, 7, 5, 10);
		setAlternative("ArrayDeque");
    }

    @Override
    public double calculateMatchScore(UserRequirements req) {
        // Your logic will now consistently access these in a set order.
		return 0.0;
    }
}
