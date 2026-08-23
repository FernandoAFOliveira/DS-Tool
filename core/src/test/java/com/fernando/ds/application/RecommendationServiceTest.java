package com.fernando.ds.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.library.QuestionInfo.QuestionId;
import com.fernando.ds.model.DSRequirements;
import com.fernando.ds.model.Preference;
import com.fernando.ds.model.RemovalOrder;
import com.fernando.ds.subject.JavaSubjectProvider;

class RecommendationServiceTest {

    private final RecommendationService service = new RecommendationService();

    @Test
    void preservesTheExistingDefaultJavaRecommendationOrder() {
        List<Recommendation> recommendations = service.recommend(
            new DSRequirements()
        );

        assertEquals(
            List.of(
                StructureId.HASH_MAP,
                StructureId.HASH_SET,
                StructureId.DYNAMIC_ARRAY,
                StructureId.DEQUE,
                StructureId.ORDERED_MAP,
                StructureId.ORDERED_SET,
                StructureId.QUEUE,
                StructureId.STACK,
                StructureId.PRIORITY_QUEUE
            ),
            ids(recommendations)
        );

        JavaSubjectProvider javaProvider = new JavaSubjectProvider();
        assertEquals(
            List.of(
                "HashMap",
                "HashSet",
                "ArrayList",
                "ArrayDeque",
                "TreeMap",
                "TreeSet",
                "Queue",
                "Stack",
                "PriorityQueue"
            ),
            recommendations.stream()
                .map(Recommendation::structure)
                .map(structure ->
                    javaProvider.getRepresentation(structure.id()).orElseThrow()
                )
                .map(representation -> representation.getName())
                .toList()
        );
    }

    @Test
    void filtersAbstractStructuresWithoutConsultingJavaNames() {
        DSRequirements requirements = new DSRequirements();
        requirements.setKeyValuePreference(Preference.YES);
        requirements.setSortedPreference(Preference.YES);

        List<Recommendation> recommendations = service.recommend(requirements);

        assertEquals(List.of(StructureId.ORDERED_MAP), ids(recommendations));
        assertFalse(
            recommendations.getFirst().structure().displayName()
                .contains("TreeMap")
        );
    }

    @Test
    void preservesIndexedDuplicateAndRemovalOrderFilters() {
        DSRequirements requirements = new DSRequirements();
        requirements.setIndexedPreference(Preference.YES);
        assertEquals(
            List.of(StructureId.DYNAMIC_ARRAY),
            ids(service.recommend(requirements))
        );

        requirements.reset();
        requirements.setDuplicatePreference(Preference.NO);
        assertEquals(
            List.of(StructureId.HASH_SET, StructureId.ORDERED_SET),
            ids(service.recommend(requirements))
        );

        requirements.reset();
        requirements.setRemovalOrderPreference(RemovalOrder.PRIORITY);
        assertEquals(
            List.of(StructureId.PRIORITY_QUEUE),
            ids(service.recommend(requirements))
        );
    }

    @Test
    void stateAnswersCanDriveRecommendationWithoutRepresentationState() {
        ApplicationState state = new ApplicationState();
        state.setPreference(QuestionId.KEY_VALUE, Preference.YES);

        assertEquals(
            List.of(StructureId.HASH_MAP, StructureId.ORDERED_MAP),
            ids(service.recommend(state.getRecommendationAnswers()))
        );
    }

    private static List<StructureId> ids(
        List<Recommendation> recommendations
    ) {
        return recommendations.stream()
            .map(Recommendation::structure)
            .map(structure -> structure.id())
            .toList();
    }
}
