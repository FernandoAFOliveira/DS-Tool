package com.fernando.ds.model;

public abstract class DataStructure implements Comparable<DataStructure>{
	
	private final String name;
	private final boolean legacy;  // Tie-breaking attribute	
	//Categorization attributes
	private final boolean duplicates;
	private final boolean keys; // e.g., Map vs List/Set
	private final boolean navigable;
	private final boolean doubleEnded;	
	// Scoring Attributes (Scale: 0 to 10)
	private final int lookup;
	private final int addDelete;
	private final int memory;
	private final int sorted;

	// Temporary field for sorting
    private double lastCalculatedScore;

	private String alternative;
	
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

	public String getAlternative() { return alternative; }
	public void setAlternative(String alternative) { this.alternative = alternative; }

	// Getter AND Setter for the temporary score
    public double getLastCalculatedScore() { return lastCalculatedScore; }
    public void setLastCalculatedScore(double score) { this.lastCalculatedScore = score; }
		
	//Getters 
	public String getName() {
		return name;
	}

	public boolean isLegacy() {
		return legacy;
	}

	public boolean isDuplicates() {
		return duplicates;
	}

	public boolean isKeys() {
		return keys;
	}

	public boolean isNavigable() {
		return navigable;
	}

	public boolean isDoubleEnded() {
		return doubleEnded;
	}

	public int getLookup() {
		return lookup;
	}

	public int getAddDelete() {
		return addDelete;
	}

	public int getMemory() {
		return memory;
	}

	public int getSorted() {
		return sorted;
	}	
}