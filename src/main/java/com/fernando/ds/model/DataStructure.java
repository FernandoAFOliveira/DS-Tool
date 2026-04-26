package com.fernando.ds.model;

public abstract class DataStructure implements Comparable<DataStructure>{
	
	private final String name;

	// Main behavior/category attributes
	private final boolean keyValue;
	private final boolean allowsDuplicates;
	private final boolean indexed;
	private final RemovalOrder removalOrder;

	// Scoring attributes
	private final int lookup;
	private final int addDelete;
	private final int memory;
	private final int sorted;

	// Tie-breaking / display
	private final boolean legacy;
	private double lastCalculatedScore;

	protected final String explanation;
	protected final String exampleUse;
	protected final String apiOverview;
	protected final String codeExample;
	
	
	public DataStructure(
		String name,

		boolean keyValue,
		boolean allowsDuplicates,
		boolean indexed,
		RemovalOrder removalOrder,

		int lookup,
		int addDelete,
		int memory,
		int sorted,

		boolean legacy,

		String explanation,
		String exampleUse,
		String apiOverview,
		String codeExample
	) {
		this.name = name;
		this.keyValue = keyValue;
		this.allowsDuplicates = allowsDuplicates;
		this.indexed = indexed;
		this.removalOrder = removalOrder;

		this.lookup = lookup;
		this.addDelete = addDelete;
		this.memory = memory;
		this.sorted = sorted;

		this.legacy = legacy;

		this.explanation = explanation;
		this.exampleUse = exampleUse;
		this.apiOverview = apiOverview;
		this.codeExample = codeExample;
	}

	public String getDisplayName() {
		return name;
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
		return allowsDuplicates;
	}

	public boolean isKeys() {
		return keyValue;
	}

	public boolean isIndexed() {
		return indexed;
	}

	public RemovalOrder getRemovalOrder() {
		return removalOrder;
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
			"Code:\n" + codeExample + "\n\n" ;
	}
}