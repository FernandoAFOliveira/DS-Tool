package com.fernando.ds.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fernando.ds.knowledge.KnowledgeCatalog;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.model.DSRequirements;
import com.fernando.ds.model.Preference;

class ScoringEngineTest {

    private final ScoringEngine scoringEngine = new ScoringEngine();

    @Test
    void calculatesTheEstablishedScoreFromAbstractKnowledge() {
        assertEquals(
            125.0,
            scoringEngine.calculate(
                KnowledgeCatalog.get(StructureId.HASH_MAP),
                new DSRequirements()
            )
        );
    }

    @Test
    void rejectsAnAbstractConceptThatViolatesARequiredCapability() {
        DSRequirements requirements = new DSRequirements();
        requirements.setIndexedPreference(Preference.YES);

        assertEquals(
            -1.0,
            scoringEngine.calculate(
                KnowledgeCatalog.get(StructureId.HASH_MAP),
                requirements
            )
        );
    }
}
