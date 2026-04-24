package com.fernando.ds.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fernando.ds.model.DataStructure;
import com.fernando.ds.requirements.UserRequirements;

public class ScoringEngine {

     private final List<DataStructure> library;

    public ScoringEngine() {
        this.library = DataStructureLibrary.getAll();
    }

    public void evaluateAndPrint(UserRequirements req, InputCollector input) {
        for (DataStructure ds : library) {
            double score = calculate(ds, req);
            ds.setLastCalculatedScore(score);
        }

        Collections.sort(library);

        double bestScore = library.get(0).getLastCalculatedScore();

        if (bestScore < 0) {
            System.out.println("\nNo matching data structure found.");
            return;
        }

        List<DataStructure> bestCandidates = new ArrayList<>();

        for (DataStructure ds : library) {
            if (ds.getLastCalculatedScore() == bestScore) {
                bestCandidates.add(ds);
            }
        }

        // Single best → print full details
        if (bestCandidates.size() == 1) {
            System.out.println(bestCandidates.get(0));
            return;
        }

        // Multiple → show names only
        System.out.println("\n=== Multiple Best Candidates ===");
        System.out.println("Select one to see details:\n");

        for (int i = 0; i < bestCandidates.size(); i++) {
            System.out.println((i + 1) + ". " + bestCandidates.get(i).getName());
        }

        int choice = input.askChoiceFromList(bestCandidates.size());

        DataStructure selected = bestCandidates.get(choice - 1);

        System.out.println(selected);
    }
    
     public double calculate(DataStructure ds, UserRequirements req) {
        // 1. HARD FILTERS (Dealbreakers)
        if (!req.isAllowDuplicates() && ds.isDuplicates()) return -1.0;
        if (req.isHaveKeys() && !ds.isKeys()) return -1.0;
        if (!req.isHaveKeys() && ds.isKeys()) return -1.0;

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