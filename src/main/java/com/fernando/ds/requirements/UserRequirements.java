package com.fernando.ds.requirements;

public class UserRequirements {

    // Categorization attributes (Matches InputCollector)
    private boolean haveKeys;
    private boolean allowDuplicates;
    private int sortWeight;
    
    // Scoring Attributes (Weights 1-5)
    private int lookupWeight;
    private int insertWeight;
    private int memoryWeight;
    
    public UserRequirements() {}

    public UserRequirements(boolean haveKeys, boolean allowDuplicates, int sortWeight, int lookupWeight, int insertWeight, int memoryWeight) {
        this.haveKeys = haveKeys;
        this.allowDuplicates = allowDuplicates;
        this.sortWeight = sortWeight;
        this.lookupWeight = lookupWeight;
        this.insertWeight = insertWeight;
        this.memoryWeight = memoryWeight;
    }   

    // Getters and Setters matching your InputCollector logic
    public void setHaveKeys(boolean haveKeys) { this.haveKeys = haveKeys; }
    public boolean isHaveKeys() { return haveKeys; }

    public void setAllowDuplicates(boolean allowDuplicates) { this.allowDuplicates = allowDuplicates; }
    public boolean isAllowDuplicates() { return allowDuplicates; }

    public void setSortWeight(int sortWeight) { this.sortWeight = sortWeight; }
    public int getSortWeight() { return sortWeight; }

    public void setLookupWeight(int lookupWeight) { this.lookupWeight = lookupWeight; }
    public int getLookupWeight() { return lookupWeight; }

    public void setInsertWeight(int insertWeight) { this.insertWeight = insertWeight; }
    public int getInsertWeight() { return insertWeight; }

    public void setMemoryWeight(int memoryWeight) { this.memoryWeight = memoryWeight; }
    public int getMemoryWeight() { return memoryWeight; }
}