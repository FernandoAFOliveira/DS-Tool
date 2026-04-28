/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: DSRequirements.java
 *
 * DSRequirements stores the user's current preferences.
 * The scoring engine reads this object to decide which data structures
 * match the user's selected requirements.
*/ 

public class DSRequirements {

    private Preference keyValuePreference;
    private Preference duplicatePreference;
    private Preference sortedPreference;
    private Preference indexedPreference;

    private int lookupWeight;
    private int addDeleteWeight;
    private int memoryWeight;
    private RemovalOrder removalOrderPreference;

    public DSRequirements() {
        reset();
    }

    public Preference getKeyValuePreference() {
        return keyValuePreference;
    }

    public void setKeyValuePreference(Preference keyValuePreference) {
        this.keyValuePreference = keyValuePreference;
    }

    public Preference getDuplicatePreference() {
        return duplicatePreference;
    }

    public void setDuplicatePreference(Preference duplicatePreference) {
        this.duplicatePreference = duplicatePreference;
    }

    public Preference getSortedPreference() {
        return sortedPreference;
    }

    public void setSortedPreference(Preference sortedPreference) {
        this.sortedPreference = sortedPreference;
    }

    public int getLookupWeight() {
        return lookupWeight;
    }

    public void setLookupWeight(int lookupWeight) {
        this.lookupWeight = lookupWeight;
    }

    public int getAddDeleteWeight() {
        return addDeleteWeight;
    }

    public void setAddDeleteWeight(int addDeleteWeight) {
        this.addDeleteWeight = addDeleteWeight;
    }

    public int getMemoryWeight() {
        return memoryWeight;
    }

    public void setMemoryWeight(int memoryWeight) {
        this.memoryWeight = memoryWeight;
    }

    public void setIndexedPreference(Preference indexedPreference) {
        this.indexedPreference = indexedPreference;
    }

    public Preference getIndexedPreference() {
        return this.indexedPreference;
    }

    public RemovalOrder getRemovalOrderPreference() {
        return removalOrderPreference;
    }

    public void setRemovalOrderPreference(RemovalOrder removalOrderPreference) {
        this.removalOrderPreference = removalOrderPreference;
    }

    public void reset() {
        keyValuePreference = Preference.ANY;
        duplicatePreference = Preference.ANY;
        sortedPreference = Preference.ANY;
        removalOrderPreference = RemovalOrder.ANY;
        indexedPreference = Preference.ANY;

        lookupWeight = 5;
        addDeleteWeight = 5;
        memoryWeight = 5;
    }
}