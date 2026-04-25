package com.fernando.ds.model;

public class DSRequirements {

    private Preference keyValuePreference;
    private Preference duplicatePreference;
    private Preference sortedPreference;

    private int lookupWeight;
    private int addDeleteWeight;
    private int memoryWeight;

    public DSRequirements() {
        keyValuePreference = Preference.ANY;
        duplicatePreference = Preference.ANY;
        sortedPreference = Preference.ANY;

        lookupWeight = 0;
        addDeleteWeight = 0;
        memoryWeight = 0;
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
}