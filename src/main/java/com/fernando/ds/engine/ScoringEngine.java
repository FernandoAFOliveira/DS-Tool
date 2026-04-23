package com.fernando.ds.engine;

import com.fernando.ds.model.*;
import com.fernando.ds.requirements.*;

public class ScoringEngine {

    public double calculate(DataStructure ds, UserRequirements req) {
        // 1. HARD FILTERS (Dealbreakers)
        if (req.isBeUnique() && !ds.isDuplicates()) return -1.0;
        if (req.isHaveKeys() && !ds.isKeys()) return -1.0;

        // 2. WEIGHTED SCORING
        double score = 0.0;
        score += (ds.getLookup() * req.getLookupWeight());
        score += (ds.getAddDelete() * req.getInsertWeight());
        score += (ds.getMemory() * req.getMemoryWeight());

        // 3. TIE-BREAKERS
        if (!ds.isLegacy()) {
            score += 5.0;
        }

        if (req.getSortWeight() > 0 && ds.getSorted() > 0) {
            score += (ds.getSorted() * req.getSortWeight());
        }

        return score;
    }
}