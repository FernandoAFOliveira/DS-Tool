package com.fernando.ds.application;

import java.util.Objects;

import com.fernando.ds.knowledge.DataStructureKnowledge;

/**
 * A scored recommendation for an abstract data-structure concept.
 *
 * @param structure recommended language-neutral knowledge
 * @param score established Advisor ranking score
 */
public record Recommendation(
    DataStructureKnowledge structure,
    double score
) {

    public Recommendation {
        Objects.requireNonNull(structure, "structure");
    }
}
