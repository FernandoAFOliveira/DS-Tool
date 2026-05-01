package com.fernando.ds.engine;
import java.util.List;

import com.fernando.ds.model.DSRequirements;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.model.Preference;
import com.fernando.ds.model.RemovalOrder;
import com.fernando.ds.library.DataStructureLibrary;


public class ScoringEngine {

    private final List<DataStructure> library;

    public ScoringEngine() {
        this.library = DataStructureLibrary.getAll();
    }

    
    public double calculate(DataStructure ds, DSRequirements req) {
        double score = 0.0;
        
        // Key-value mapping
        if (req.getKeyValuePreference() == Preference.YES && !ds.isKeys()) {
            return -1.0;
        }

        if (req.getKeyValuePreference() == Preference.NO && ds.isKeys()) {
            return -1.0;
        }

        // Duplicates
        if (req.getDuplicatePreference() == Preference.YES && !ds.isDuplicates()) {
            return -1.0;
        }

        if (req.getDuplicatePreference() == Preference.NO && ds.isDuplicates()) {
            return -1.0;
        }
        // Indexed access
        if (req.getIndexedPreference() == Preference.YES && !ds.isIndexed()) {
            return -1.0;
        }

        if (req.getIndexedPreference() == Preference.NO && ds.isIndexed()) {
            return -1.0;
        }

        // Sorted
        if (req.getSortedPreference() == Preference.YES && ds.getSorted() == 0) {
            return -1.0;
        }

        if (req.getSortedPreference() == Preference.NO && ds.getSorted() > 0) {
            return -1.0;
        }

        if (req.getRemovalOrderPreference() != RemovalOrder.ANY
            && ds.getRemovalOrder() != req.getRemovalOrderPreference()) {
            return -1.0;
        }


        score += ds.getLookup() * req.getLookupWeight();
        score += ds.getAddDelete() * req.getAddDeleteWeight();
        score += ds.getMemory() * req.getMemoryWeight();

        return score;
    }
}