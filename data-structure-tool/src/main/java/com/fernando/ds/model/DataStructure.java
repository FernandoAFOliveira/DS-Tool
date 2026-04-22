package com.fernando.ds.model;

import com.fernando.ds.model.*;

public abstract class DataStructure {
	
	private String name;
	private boolean legacy;  // Tie-breaking attribute	
	//Categorization attributes
	private boolean duplicates;
	private boolean keys; // e.g., Map vs List/Set
	private boolean navigable;
	private boolean doubleEnded;	
	// Scoring Attributes (Scale: 0 to 10)
	private int lookup;
	private int addDelete;
	private int memory;
	private int sorted;
	
	public DataStructure(String name, boolean legacy, boolean duplicates, boolean keys ,boolean navigable, boolean doubleEnded , int lookup, int addDelete, int memory,  int sorted) {
		
		this.name = name;
		this.legacy = legacy;
		this.duplicates = duplicates;
		this.keys = keys;
		this.navigable = navigable;
		this.doubleEnded = doubleEnded;
		this.lookup = lookup;
		this.addDelete = addDelete;
		this.memory = memory;		
		this.sorted = sorted;		
	}
	
	@Override
    public int compareTo(DataStructure other) {
        // Sort by the calculated score (highest score first)
        // You'll need to store the score temporarily after calculation
        return Double.compare(other.lastCalculatedScore, this.lastCalculatedScore);
    }
	
	// Abstract method: Each subclass defines how to score itself based on user Input.
	public abstract double calculateMatchScore(UserRequirements req);
		// 1. HARD FILTERS (Dealbreakers)
		if (req.beUnique() && !this.duplicates) return -1.0;
		if (req.haveKeys() && !this.keys) return -1.0;
		
		// 2. WEIGHTED SCORING (Preference)
		double score = 0.0;
		
		// Multiply performance by weight (e.g., if user cares about lookup, lookupWeight will be high)
		score += (this.lookup * req.getLookupWeight());
		score += (this.addDelete * req.getInsertWeight());
		score += (this.memory * req.getMemoryWeight());
		
		// 3. TIE-BREAKERS AND BONUSES
		// Bonus for modern structures if they are not legacy
		if (!this.legacy) {
			score += 5.0;
		}
		
		// Adjust based on sorting requirement
		if (req.sortWeight() > 0 && this.sorted > 0) {
			score += (this.sorted * req.sortWeight());
		}

		return score;

	}	
	//Getters Setters
	
}