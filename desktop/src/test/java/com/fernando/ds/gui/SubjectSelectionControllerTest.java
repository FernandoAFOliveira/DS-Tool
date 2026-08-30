package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.application.ApplicationState.AccessibilityPreferences;
import com.fernando.ds.application.ApplicationState.Appearance;
import com.fernando.ds.application.ApplicationState.Experience;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.library.QuestionInfo.QuestionId;
import com.fernando.ds.model.Preference;
import com.fernando.ds.subject.CSubjectProvider;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProviderRegistry;
import com.fernando.ds.subject.UnavailableSubjectProvider;

class SubjectSelectionControllerTest {

    @Test
    void rendersCBeforeCommitAndPreservesUnrelatedState() {
        ApplicationState state = populatedState();
        SubjectSelectionController controller = controller(state);
        AtomicBoolean rendered = new AtomicBoolean();

        boolean selected = controller.select(SubjectId.C, provider -> {
            assertEquals(SubjectId.C, provider.id());
            assertEquals(SubjectId.JAVA, state.getActiveSubject());
            assertStateOtherThanSubject(state);
            rendered.set(true);
        });

        assertTrue(selected);
        assertTrue(rendered.get());
        assertEquals(SubjectId.C, state.getActiveSubject());
        assertStateOtherThanSubject(state);
    }

    @Test
    void renderingFailureDoesNotCommitAndLaterSelectionStillWorks() {
        ApplicationState state = populatedState();
        SubjectSelectionController controller = controller(state);

        assertThrows(
            IllegalStateException.class,
            () -> controller.select(SubjectId.C, provider -> {
                throw new IllegalStateException("render failed");
            })
        );
        assertEquals(SubjectId.JAVA, state.getActiveSubject());
        assertStateOtherThanSubject(state);

        assertTrue(controller.select(SubjectId.C, provider -> {
        }));
        assertEquals(SubjectId.C, state.getActiveSubject());
        assertStateOtherThanSubject(state);
    }

    @Test
    void unavailableSubjectsDoNotRenderOrChangeState() {
        for (SubjectId id : List.of(SubjectId.CPP, SubjectId.PYTHON)) {
            ApplicationState state = populatedState();
            AtomicBoolean rendered = new AtomicBoolean();

            boolean selected = controller(state).select(
                id,
                provider -> rendered.set(true)
            );

            assertFalse(selected);
            assertFalse(rendered.get());
            assertEquals(SubjectId.JAVA, state.getActiveSubject());
            assertStateOtherThanSubject(state);
        }
    }

    private static ApplicationState populatedState() {
        ApplicationState state = new ApplicationState(Locale.US);
        state.setPreference(QuestionId.INDEXED, Preference.YES);
        state.setWeight(QuestionId.LOOKUP, 2);
        state.selectStructure(StructureId.DYNAMIC_ARRAY);
        state.navigateToQuestion(QuestionId.MEMORY);
        state.setAppearance(Appearance.DARK_BLUE);
        state.setLocale(Locale.FRANCE);
        state.setAccessibilityPreferences(
            new AccessibilityPreferences(true, true)
        );
        state.setActiveExperience(Experience.EXPLORER);
        state.navigateFlashCard(StructureId.STACK);
        state.revealCurrentFlashCard();
        return state;
    }

    private static void assertStateOtherThanSubject(ApplicationState state) {
        assertEquals(
            Preference.YES,
            state.getRecommendationAnswers().getIndexedPreference()
        );
        assertEquals(2, state.getRecommendationAnswers().getLookupWeight());
        assertEquals(
            StructureId.DYNAMIC_ARRAY,
            state.getSelectedStructureId().orElseThrow()
        );
        assertEquals(Appearance.DARK_BLUE, state.getAppearance());
        assertEquals(QuestionId.MEMORY, state.getNavigation().questionId());
        assertEquals(Locale.FRANCE, state.getLocale());
        assertEquals(
            new AccessibilityPreferences(true, true),
            state.getAccessibilityPreferences()
        );
        assertEquals(Experience.EXPLORER, state.getActiveExperience());
        assertEquals(
            StructureId.STACK,
            state.getFlashCardSession().currentStructureId()
        );
        assertTrue(state.getFlashCardSession().answerRevealed());
        assertEquals(1, state.getLearningProgress().reviewedCount());
    }

    private static SubjectSelectionController controller(
        ApplicationState state
    ) {
        return new SubjectSelectionController(state, registry());
    }

    private static SubjectProviderRegistry registry() {
        return new SubjectProviderRegistry(List.of(
            new JavaSubjectProvider(),
            new CSubjectProvider(),
            new UnavailableSubjectProvider(SubjectId.CPP, "C++"),
            new UnavailableSubjectProvider(SubjectId.PYTHON, "Python")
        ));
    }
}
