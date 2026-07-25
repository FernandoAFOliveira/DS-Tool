package com.fernando.ds.engine;

import com.fernando.ds.model.DSRequirements;
import com.fernando.ds.knowledge.DataStructureKnowledge;
import com.fernando.ds.model.Preference;
import com.fernando.ds.model.RemovalOrder;

/**
 * Applies Advisor requirements to language-neutral data-structure knowledge.
 */
public class ScoringEngine {

    /**
     * Calculates the established Advisor score.
     *
     * @return a non-negative score, or {@code -1} when a hard requirement is
     *         not satisfied
     */
    public double calculate(
        DataStructureKnowledge structure,
        DSRequirements requirements
    ) {
        double score = 0.0;

        // Key-value mapping
        if (requirements.getKeyValuePreference() == Preference.YES
            && !structure.keyValueMapping()) {
            return -1.0;
        }

        if (requirements.getKeyValuePreference() == Preference.NO
            && structure.keyValueMapping()) {
            return -1.0;
        }

        // Duplicates
        if (requirements.getDuplicatePreference() == Preference.YES
            && !structure.allowsDuplicates()) {
            return -1.0;
        }

        if (requirements.getDuplicatePreference() == Preference.NO
            && structure.allowsDuplicates()) {
            return -1.0;
        }
        // Indexed access
        if (requirements.getIndexedPreference() == Preference.YES
            && !structure.indexedAccess()) {
            return -1.0;
        }

        if (requirements.getIndexedPreference() == Preference.NO
            && structure.indexedAccess()) {
            return -1.0;
        }

        // Sorted
        if (requirements.getSortedPreference() == Preference.YES
            && !structure.isSorted()) {
            return -1.0;
        }

        if (requirements.getSortedPreference() == Preference.NO
            && structure.isSorted()) {
            return -1.0;
        }

        if (requirements.getRemovalOrderPreference() != RemovalOrder.ANY
            && structure.removalOrder()
                != requirements.getRemovalOrderPreference()) {
            return -1.0;
        }

        score += structure.lookupRating() * requirements.getLookupWeight();
        score += structure.insertionRemovalRating()
            * requirements.getAddDeleteWeight();
        score += structure.memoryRating() * requirements.getMemoryWeight();

        return score;
    }
}
