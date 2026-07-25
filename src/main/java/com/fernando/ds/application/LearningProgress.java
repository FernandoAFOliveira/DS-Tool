package com.fernando.ds.application;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import com.fernando.ds.knowledge.StructureId;

/**
 * In-memory progress shared by Learn activities.
 *
 * <p>Milestone 6 records a concept as reviewed after its flash-card answer has
 * been displayed successfully. Review does not imply correctness or mastery.</p>
 */
public final class LearningProgress {

    private final EnumSet<StructureId> reviewedStructures;

    public LearningProgress() {
        reviewedStructures = EnumSet.noneOf(StructureId.class);
    }

    public LearningProgress(LearningProgress source) {
        Objects.requireNonNull(source, "source");
        reviewedStructures = source.reviewedStructures.clone();
    }

    /** Records a concept as reviewed. Repeated calls are idempotent. */
    public void markReviewed(StructureId structureId) {
        reviewedStructures.add(
            Objects.requireNonNull(structureId, "structureId")
        );
    }

    /** @return whether the concept has been reviewed */
    public boolean isReviewed(StructureId structureId) {
        return reviewedStructures.contains(
            Objects.requireNonNull(structureId, "structureId")
        );
    }

    /** @return the number of distinct reviewed concepts */
    public int reviewedCount() {
        return reviewedStructures.size();
    }

    /** @return an immutable snapshot of reviewed concept identifiers */
    public Set<StructureId> reviewedStructures() {
        return Set.copyOf(reviewedStructures);
    }
}
