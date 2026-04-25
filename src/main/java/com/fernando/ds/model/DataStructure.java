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
	protected final String explanation;
	protected final String exampleUse;
	protected final String apiOverview;
	protected final String codeExample;
	protected final String alternative;
	
	
	public DataStructure(
		String name, 
		boolean legacy, 
		boolean duplicates, 
		boolean keys,
		boolean navigable, 
		boolean doubleEnded, 
		int lookup, 
		int addDelete, 
		int memory,  
		int sorted,
	    String explanation,
    	String exampleUse,
    	String apiOverview,		
		String codeExample,
		String alternative
		) {
		
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
		this.explanation = explanation;
		this.exampleUse   = exampleUse;
		this.apiOverview = apiOverview;
		this.codeExample = codeExample;
		this.alternative = alternative;		
	}

	public String getDisplayName() {
		return name;
	}

	public String getAlternative() { 
		return alternative; 
	}
	public String getExplanation() {
		return explanation;
	}

	public String getExampleUse() {
		return exampleUse;
	}

	public String getApiOverview() {
		return apiOverview;
	}

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
	public String getCodeExample() {
		return codeExample;
	}
	
		@Override
	public int compareTo(DataStructure other) {
		int scoreCompare = Double.compare(
			other.lastCalculatedScore,
			this.lastCalculatedScore
		);

		if (scoreCompare != 0) {
			return scoreCompare;
		}

		return this.name.compareTo(other.name);
	}

	@Override
	public String toString() {
		return "Best choice: " + name + "\n\n" +
			"Why:\n" + explanation + "\n\n" +
			"Example:\n" + exampleUse + "\n\n" +
			"API Overview:\n" + apiOverview + "\n\n" +
			"Code:\n" + codeExample + "\n\n" +
			"Alternative:\n" + alternative;
	}
}