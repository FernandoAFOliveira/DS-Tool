package com.fernando.ds.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fernando.ds.knowledge.StructureId;

class LearningProgressTest {

    @Test
    void recordsDistinctReviewedConceptsIdempotently() {
        LearningProgress progress = new LearningProgress();

        progress.markReviewed(StructureId.QUEUE);
        progress.markReviewed(StructureId.QUEUE);

        assertEquals(1, progress.reviewedCount());
        assertTrue(progress.isReviewed(StructureId.QUEUE));
        assertFalse(progress.isReviewed(StructureId.STACK));
    }

    @Test
    void copiesProgressAndExposesAnImmutableSnapshot() {
        LearningProgress original = new LearningProgress();
        original.markReviewed(StructureId.DEQUE);
        LearningProgress copy = new LearningProgress(original);

        original.markReviewed(StructureId.STACK);

        assertEquals(1, copy.reviewedCount());
        assertThrows(
            UnsupportedOperationException.class,
            () -> copy.reviewedStructures().clear()
        );
    }
}
