package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.application.ApplicationState.AccessibilityPreferences;
import com.fernando.ds.application.ApplicationState.Appearance;
import com.fernando.ds.library.QuestionInfo.QuestionId;
import com.fernando.ds.model.Preference;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProviderRegistry;
import com.fernando.ds.subject.UnavailableSubjectProvider;

class AppControllerSubjectTest {

    @Test
    void unavailableSubjectsDoNotChangeActiveSubjectOrUnrelatedState() {
        for (SubjectId id : List.of(
            SubjectId.C,
            SubjectId.CPP,
            SubjectId.PYTHON
        )) {
            ApplicationState state = populatedState();

            boolean selected = AppController.selectSubject(
                state,
                registry(),
                id
            );

            assertFalse(selected);
            assertEquals(SubjectId.JAVA, state.getActiveSubject());
            assertEquals(
                Preference.YES,
                state.getRecommendationAnswers().getIndexedPreference()
            );
            assertEquals(2, state.getRecommendationAnswers().getLookupWeight());
            assertEquals(
                "ArrayList",
                state.getSelectedDataStructureName().orElseThrow()
            );
            assertEquals(Appearance.DARK_BLUE, state.getAppearance());
            assertEquals(QuestionId.MEMORY, state.getNavigation().questionId());
            assertEquals(Locale.FRANCE, state.getLocale());
            assertEquals(
                new AccessibilityPreferences(true, true),
                state.getAccessibilityPreferences()
            );
        }
    }

    @Test
    void enabledJavaSubjectCanBeSelectedWithoutResettingState() {
        ApplicationState state = populatedState();

        boolean selected = AppController.selectSubject(
            state,
            registry(),
            SubjectId.JAVA
        );

        assertTrue(selected);
        assertEquals(SubjectId.JAVA, state.getActiveSubject());
        assertEquals(
            "ArrayList",
            state.getSelectedDataStructureName().orElseThrow()
        );
        assertEquals(
            QuestionId.MEMORY,
            state.getNavigation().questionId()
        );
    }

    private static ApplicationState populatedState() {
        ApplicationState state = new ApplicationState(Locale.US);
        state.setPreference(QuestionId.INDEXED, Preference.YES);
        state.setWeight(QuestionId.LOOKUP, 2);
        state.selectDataStructure("ArrayList");
        state.navigateToQuestion(QuestionId.MEMORY);
        state.setAppearance(Appearance.DARK_BLUE);
        state.setLocale(Locale.FRANCE);
        state.setAccessibilityPreferences(
            new AccessibilityPreferences(true, true)
        );
        return state;
    }

    private static SubjectProviderRegistry registry() {
        return new SubjectProviderRegistry(List.of(
            new JavaSubjectProvider(),
            new UnavailableSubjectProvider(SubjectId.C, "C"),
            new UnavailableSubjectProvider(SubjectId.CPP, "C++"),
            new UnavailableSubjectProvider(SubjectId.PYTHON, "Python")
        ));
    }
}
