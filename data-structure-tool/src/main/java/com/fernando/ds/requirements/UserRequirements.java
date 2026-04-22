package com.fernando.ds.requirements;


public class UserRequirements {

	private String name;
	private boolean isLegacy;  // Tie-breaking attribute
	
	// Scoring Attributes (Scale: 0 to 10)
	private int lookupSpeed;
	private int insertionSpeed;
	private int memoryEfficiency;
	
	//Categorization attributes
	private boolean allowDuplicates;
	private boolean isKeyed; // e.g., Map vs List/Set
	private boolean isSorted;
	
	
	public UserRequirements(){
	
	}
}
