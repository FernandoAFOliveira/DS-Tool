package com.fernando.ds.application;

import java.util.List;

import com.fernando.ds.knowledge.KnowledgeCatalog;

/**
 * Supplies the shared learning questions derived from the Knowledge Core.
 *
 * <p>The source is independent of presentation, subject representations, and
 * activity-specific session behavior.</p>
 */
public final class LearningQuestionSource {

    /**
     * Returns one concept-first question for every current Knowledge Core
     * structure in stable catalog order.
     *
     * @return immutable learning questions
     */
    public List<LearningQuestion> getAll() {
        return KnowledgeCatalog.getAll().stream()
            .map(knowledge -> new LearningQuestion(
                knowledge.id(),
                "What is a " + knowledge.displayName() + "?",
                knowledge.description()
            ))
            .toList();
    }
}
