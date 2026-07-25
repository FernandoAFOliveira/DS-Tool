package com.fernando.ds.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState.AccessibilityPreferences;
import com.fernando.ds.application.ApplicationState.Appearance;
import com.fernando.ds.application.ApplicationState.Experience;
import com.fernando.ds.application.ApplicationState.NavigationKind;
import com.fernando.ds.library.QuestionInfo.QuestionId;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.model.DSRequirements;
import com.fernando.ds.model.Preference;
import com.fernando.ds.model.RemovalOrder;
import com.fernando.ds.subject.SubjectId;

class ApplicationStateTest {

    @Test
    void startsWithAdvisorDefaults() {
        ApplicationState state = new ApplicationState(Locale.CANADA);
        DSRequirements answers = state.getRecommendationAnswers();

        assertEquals(SubjectId.JAVA, state.getActiveSubject());
        assertEquals(Experience.ADVISOR, state.getActiveExperience());
        assertEquals(Appearance.LIGHT, state.getAppearance());
        assertEquals(Locale.CANADA, state.getLocale());
        assertEquals(NavigationKind.WELCOME, state.getNavigation().kind());
        assertFalse(state.getSelectedStructureId().isPresent());
        assertEquals(Preference.ANY, answers.getKeyValuePreference());
        assertEquals(5, answers.getLookupWeight());
    }

    @Test
    void ownsRecommendationAnswersAndProtectsThemFromExternalMutation() {
        ApplicationState state = new ApplicationState(Locale.US);
        state.setPreference(QuestionId.SORTED, Preference.YES);
        state.setWeight(QuestionId.LOOKUP, 2);
        state.setRemovalOrder(RemovalOrder.FIFO);

        DSRequirements snapshot = state.getRecommendationAnswers();
        assertEquals(Preference.YES, snapshot.getSortedPreference());
        assertEquals(2, snapshot.getLookupWeight());
        assertEquals(RemovalOrder.FIFO, snapshot.getRemovalOrderPreference());

        snapshot.setLookupWeight(4);
        assertEquals(2, state.getRecommendationAnswers().getLookupWeight());
    }

    @Test
    void keyValueSelectionMaintainsDuplicateAnswerInvariant() {
        ApplicationState state = new ApplicationState(Locale.US);
        state.setPreference(QuestionId.DUPLICATES, Preference.YES);
        state.setPreference(QuestionId.KEY_VALUE, Preference.YES);
        state.setPreference(QuestionId.DUPLICATES, Preference.NO);

        assertEquals(
            Preference.ANY,
            state.getRecommendationAnswers().getDuplicatePreference()
        );
    }

    @Test
    void resetClearsOnlyAdvisorSessionState() {
        ApplicationState state = new ApplicationState(Locale.US);
        AccessibilityPreferences accessibility =
            new AccessibilityPreferences(true, true);
        state.setAppearance(Appearance.DARK_BLUE);
        state.setLocale(Locale.FRANCE);
        state.setAccessibilityPreferences(accessibility);
        state.setPreference(QuestionId.INDEXED, Preference.YES);
        state.setWeight(QuestionId.MEMORY, 1);
        state.selectStructure(StructureId.DYNAMIC_ARRAY);

        state.resetAdvisorSession();

        DSRequirements answers = state.getRecommendationAnswers();
        assertEquals(Preference.ANY, answers.getIndexedPreference());
        assertEquals(5, answers.getMemoryWeight());
        assertFalse(state.getSelectedStructureId().isPresent());
        assertEquals(NavigationKind.WELCOME, state.getNavigation().kind());
        assertEquals(Appearance.DARK_BLUE, state.getAppearance());
        assertEquals(Locale.FRANCE, state.getLocale());
        assertEquals(accessibility, state.getAccessibilityPreferences());
    }

    @Test
    void navigationChangesDoNotResetUnrelatedState() {
        ApplicationState state = new ApplicationState(Locale.US);
        state.setPreference(QuestionId.INDEXED, Preference.YES);
        state.selectStructure(StructureId.DYNAMIC_ARRAY);

        state.navigateToQuestion(QuestionId.MEMORY);

        assertEquals(NavigationKind.QUESTION, state.getNavigation().kind());
        assertEquals(QuestionId.MEMORY, state.getNavigation().questionId());
        assertEquals(
            Preference.YES,
            state.getRecommendationAnswers().getIndexedPreference()
        );
        assertEquals(
            StructureId.DYNAMIC_ARRAY,
            state.getSelectedStructureId().orElseThrow()
        );
    }

    @Test
    void explorerSelectionDoesNotReplaceAdvisorNavigationOrAnswers() {
        ApplicationState state = new ApplicationState(Locale.US);
        state.setPreference(QuestionId.SORTED, Preference.YES);
        state.navigateToQuestion(QuestionId.MEMORY);
        state.setActiveExperience(Experience.EXPLORER);

        state.selectStructure(StructureId.HASH_MAP);

        assertEquals(Experience.EXPLORER, state.getActiveExperience());
        assertEquals(NavigationKind.QUESTION, state.getNavigation().kind());
        assertEquals(QuestionId.MEMORY, state.getNavigation().questionId());
        assertEquals(
            Preference.YES,
            state.getRecommendationAnswers().getSortedPreference()
        );
        assertEquals(
            StructureId.HASH_MAP,
            state.getSelectedStructureId().orElseThrow()
        );
    }

    @Test
    void rejectsInvalidUpdates() {
        ApplicationState state = new ApplicationState(Locale.US);

        assertThrows(
            IllegalArgumentException.class,
            () -> state.setWeight(QuestionId.LOOKUP, 6)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> state.setWeight(QuestionId.SORTED, 3)
        );
        assertThrows(NullPointerException.class, () -> state.setLocale(null));
    }
}
