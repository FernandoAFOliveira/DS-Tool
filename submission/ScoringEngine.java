/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: ScoringEngine.java
 *
 * ScoringEngine contains the recommendation logic.
 * It filters out data structures that violate required features and then
 * calculates a score using the user's performance weights.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoringEngine {

     private final List<DataStructure> library;

    public ScoringEngine() {
        this.library = DataStructureLibrary.getAll();
    }

    public void evaluateAndPrint(DSRequirements req, InputCollector input) {
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
            System.out.println("\nBest Choice(s):");
            System.out.println(bestCandidates.get(0).getName());
            System.out.println();
            System.out.println(bestCandidates.get(0));
            return;
        }

        // Multiple → show names only
        System.out.println("\nBest Choice(s):");

        for (int i = 0; i < bestCandidates.size(); i++) {
            System.out.println((i + 1) + ". " + bestCandidates.get(i).getName());
        }

        int choice = input.askChoiceFromList(bestCandidates.size());

        DataStructure selected = bestCandidates.get(choice - 1);

        System.out.println(selected);
    }
    
    public double calculate(DataStructure ds, DSRequirements req) {
        double score = 0.0;
        
        // Key-value mapping
        // A score of -1 means the data structure fails a required feature
        // and should not appear in the recommendation list.
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