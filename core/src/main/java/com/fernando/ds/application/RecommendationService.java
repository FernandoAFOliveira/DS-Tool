package com.fernando.ds.application;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.fernando.ds.engine.ScoringEngine;
import com.fernando.ds.knowledge.KnowledgeCatalog;
import com.fernando.ds.model.DSRequirements;

/**
 * Filters and ranks abstract data-structure knowledge without consulting a
 * programming-language subject provider.
 */
public final class RecommendationService {

    private static final Comparator<Recommendation> RECOMMENDATION_ORDER =
        Comparator.comparingDouble(Recommendation::score)
            .reversed()
            .thenComparing(recommendation ->
                recommendation.structure().displayName()
            );

    private final ScoringEngine scoringEngine;

    /** Creates a service using the standard scoring engine. */
    public RecommendationService() {
        this(new ScoringEngine());
    }

    RecommendationService(ScoringEngine scoringEngine) {
        this.scoringEngine = Objects.requireNonNull(
            scoringEngine,
            "scoringEngine"
        );
    }

    /**
     * Produces abstract recommendations before a subject representation is
     * selected.
     *
     * @param requirements Advisor answers and ranking weights
     * @return eligible abstract structures in ranked order
     */
    public List<Recommendation> recommend(DSRequirements requirements) {
        Objects.requireNonNull(requirements, "requirements");

        return KnowledgeCatalog.getAll().stream()
            .map(structure -> new Recommendation(
                structure,
                scoringEngine.calculate(structure, requirements)
            ))
            .filter(recommendation -> recommendation.score() >= 0)
            .sorted(RECOMMENDATION_ORDER)
            .toList();
    }
}
