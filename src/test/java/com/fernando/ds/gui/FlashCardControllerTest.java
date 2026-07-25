package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.application.ApplicationState.NavigationKind;
import com.fernando.ds.application.FlashCardContent;
import com.fernando.ds.application.LearningQuestionSource;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.library.QuestionInfo.QuestionId;
import com.fernando.ds.model.Preference;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectProviderRegistry;

class FlashCardControllerTest {

    @Test
    void startsWithAHiddenConceptFirstCardAndSupplementaryJavaName() {
        ApplicationState state = new ApplicationState(Locale.US);
        RecordingView view = new RecordingView();

        controller(state, view).activate();

        assertEquals(
            StructureId.DYNAMIC_ARRAY,
            view.content.question().structureId()
        );
        assertEquals("What is a Dynamic array?", view.content.question().prompt());
        assertFalse(view.answerRevealed);
        assertEquals(1, view.position);
        assertEquals(9, view.total);
        assertEquals(0, view.reviewed);
        assertEquals(
            "ArrayList",
            view.content.subjectRepresentation()
                .orElseThrow()
                .representationDisplayName()
        );
    }

    @Test
    void revealsProgressAndResumesWithoutChangingSharedSelection() {
        ApplicationState state = populatedState();
        RecordingView firstView = new RecordingView();
        FlashCardController controller = controller(state, firstView);

        controller.showNext();
        controller.revealAnswer();

        assertEquals(StructureId.STACK, state.getFlashCardSession()
            .currentStructureId());
        assertTrue(state.getFlashCardSession().answerRevealed());
        assertTrue(state.getLearningProgress().isReviewed(StructureId.STACK));
        assertEquals(1, state.getLearningProgress().reviewedCount());
        assertEquals(
            StructureId.HASH_MAP,
            state.getSelectedStructureId().orElseThrow()
        );
        assertEquals(NavigationKind.QUESTION, state.getNavigation().kind());
        assertEquals(
            Preference.YES,
            state.getRecommendationAnswers().getSortedPreference()
        );

        RecordingView resumedView = new RecordingView();
        controller(state, resumedView).activate();
        assertEquals(StructureId.STACK, resumedView.content.question()
            .structureId());
        assertTrue(resumedView.answerRevealed);
        assertEquals(1, resumedView.reviewed);
    }

    @Test
    void renderFailureDoesNotCommitNavigationOrProgressAndLaterUseWorks() {
        ApplicationState state = new ApplicationState(Locale.US);
        RecordingView view = new RecordingView();
        FlashCardController controller = controller(state, view);
        view.failRendering = true;

        assertThrows(IllegalStateException.class, controller::revealAnswer);
        assertFalse(state.getFlashCardSession().answerRevealed());
        assertEquals(0, state.getLearningProgress().reviewedCount());

        assertThrows(IllegalStateException.class, controller::showNext);
        assertEquals(
            StructureId.DYNAMIC_ARRAY,
            state.getFlashCardSession().currentStructureId()
        );

        view.failRendering = false;
        controller.showNext();
        controller.revealAnswer();
        assertEquals(
            StructureId.STACK,
            state.getFlashCardSession().currentStructureId()
        );
        assertEquals(1, state.getLearningProgress().reviewedCount());
    }

    private static FlashCardController controller(
        ApplicationState state,
        RecordingView view
    ) {
        return new FlashCardController(
            state,
            new SubjectProviderRegistry(List.of(new JavaSubjectProvider())),
            new LearningQuestionSource(),
            view
        );
    }

    private static ApplicationState populatedState() {
        ApplicationState state = new ApplicationState(Locale.US);
        state.setPreference(QuestionId.SORTED, Preference.YES);
        state.selectStructure(StructureId.HASH_MAP);
        state.navigateToQuestion(QuestionId.MEMORY);
        return state;
    }

    private static final class RecordingView implements FlashCardView {

        private FlashCardContent content;
        private int position;
        private int total;
        private int reviewed;
        private boolean answerRevealed;
        private boolean failRendering;

        @Override
        public void showCard(
            FlashCardContent value,
            int currentPosition,
            int questionCount,
            int reviewedCount,
            boolean revealed,
            boolean hasPrevious,
            boolean hasNext
        ) {
            if (failRendering) {
                throw new IllegalStateException("render failed");
            }
            content = value;
            position = currentPosition;
            total = questionCount;
            reviewed = reviewedCount;
            answerRevealed = revealed;
        }

        @Override
        public void applyTheme(Theme theme) {
        }
    }
}
