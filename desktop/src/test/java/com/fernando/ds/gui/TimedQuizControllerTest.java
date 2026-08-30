package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.application.quiz.QuizQuestion;
import com.fernando.ds.application.quiz.QuizQuestionSource;
import com.fernando.ds.application.quiz.QuizSession;
import com.fernando.ds.application.quiz.QuizSession.AnswerReview;
import com.fernando.ds.application.quiz.QuizSummary;
import com.fernando.ds.application.quiz.TimeSource;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.library.QuestionInfo.QuestionId;
import com.fernando.ds.model.Preference;
import com.fernando.ds.subject.CSubjectProvider;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProviderRegistry;

class TimedQuizControllerTest {

    @Test
    void initialScreenStartsAndRendersFirstQuestion() {
        ApplicationState state = new ApplicationState();
        RecordingView view = new RecordingView();
        TimedQuizController controller = controller(
            state,
            new MutableTime(),
            view
        );

        controller.activate();
        assertEquals("Java", view.subject);
        assertEquals(10, view.questionCount);
        assertEquals(ViewState.NOT_STARTED, view.state);

        controller.startQuiz();
        assertEquals(ViewState.QUESTION, view.state);
        assertEquals("Java", view.subject);
        assertEquals(1, view.position);
        assertEquals(10, view.questionCount);
        assertEquals(
            QuizSession.Lifecycle.QUESTION_ACTIVE,
            controller.session().lifecycle()
        );
    }

    @Test
    void answerFeedbackRequiresNextAndCompletesWithCorrectTotals() {
        RecordingView view = new RecordingView();
        TimedQuizController controller = controller(
            new ApplicationState(),
            new MutableTime(),
            view
        );
        controller.startQuiz();

        for (int index = 0; index < 10; index++) {
            int correct = controller.session()
                .currentQuestion()
                .correctOptionIndex();
            controller.submitAnswer(correct);
            assertEquals(ViewState.REVIEW, view.state);
            assertTrue(view.review.correct());
            assertEquals(index + 1, view.position);

            controller.showNext();
            assertEquals(
                index == 9 ? ViewState.COMPLETED : ViewState.QUESTION,
                view.state
            );
        }

        assertEquals(10, view.summary.correctCount());
        assertEquals(0, view.summary.incorrectCount());
        assertEquals(100, view.summary.percentage());
    }

    @Test
    void activeSessionIgnoresLaterGlobalSubjectChange() {
        ApplicationState state = new ApplicationState();
        RecordingView view = new RecordingView();
        TimedQuizController controller = controller(
            state,
            new MutableTime(),
            view
        );
        controller.startQuiz();

        state.setActiveSubject(SubjectId.C);
        controller.activate(new CSubjectProvider());

        assertEquals("Java", view.subject);
        assertEquals(
            SubjectId.JAVA,
            controller.session().subjectId().orElseThrow()
        );
        assertEquals(SubjectId.C, state.getActiveSubject());
    }

    @Test
    void resetChangesOnlyQuizAndPreservesFlashAdvisorAndExplorerState() {
        ApplicationState state = populatedState();
        state.navigateFlashCard(StructureId.QUEUE);
        state.revealCurrentFlashCard();
        RecordingView view = new RecordingView();
        TimedQuizController controller = controller(
            state,
            new MutableTime(),
            view
        );
        controller.startQuiz();
        controller.submitAnswer(0);

        controller.reset();

        assertEquals(ViewState.NOT_STARTED, view.state);
        assertEquals(
            QuizSession.Lifecycle.NOT_STARTED,
            controller.session().lifecycle()
        );
        assertEquals(
            StructureId.HASH_MAP,
            state.getSelectedStructureId().orElseThrow()
        );
        assertEquals(QuestionId.MEMORY, state.getNavigation().questionId());
        assertEquals(
            Preference.YES,
            state.getRecommendationAnswers().getSortedPreference()
        );
        assertEquals(
            StructureId.QUEUE,
            state.getFlashCardSession().currentStructureId()
        );
        assertTrue(state.getFlashCardSession().answerRevealed());
        assertEquals(1, state.getLearningProgress().reviewedCount());
    }

    @Test
    void renderFailuresRollbackAndLaterActionsRecover() {
        RecordingView view = new RecordingView();
        TimedQuizController controller = controller(
            new ApplicationState(),
            new MutableTime(),
            view
        );
        view.failRendering = true;

        assertThrows(IllegalStateException.class, controller::startQuiz);
        assertEquals(
            QuizSession.Lifecycle.NOT_STARTED,
            controller.session().lifecycle()
        );

        view.failRendering = false;
        controller.startQuiz();
        int correct = controller.session()
            .currentQuestion()
            .correctOptionIndex();
        view.failRendering = true;

        assertThrows(
            IllegalStateException.class,
            () -> controller.submitAnswer(correct)
        );
        assertEquals(
            QuizSession.Lifecycle.QUESTION_ACTIVE,
            controller.session().lifecycle()
        );
        assertEquals(0, controller.session().correctCount());

        view.failRendering = false;
        controller.submitAnswer(correct);
        assertEquals(
            QuizSession.Lifecycle.ANSWER_REVIEWED,
            controller.session().lifecycle()
        );
        assertEquals(1, controller.session().correctCount());
    }

    @Test
    void timerRefreshAndThemePreserveBoundSession() {
        MutableTime time = new MutableTime();
        RecordingView view = new RecordingView();
        TimedQuizController controller = controller(
            new ApplicationState(),
            time,
            view
        );
        controller.startQuiz();
        time.advanceSeconds(65);

        controller.refreshElapsedTime();
        controller.applyTheme(Theme.DARK);

        assertEquals(Duration.ofSeconds(65), view.elapsed);
        assertEquals(Theme.DARK, view.theme);
        assertEquals("Java", view.subject);
        assertEquals(1, view.position);
        controller.deactivate();
        assertTrue(view.timerStopped);
    }

    private static TimedQuizController controller(
        ApplicationState state,
        MutableTime time,
        RecordingView view
    ) {
        return new TimedQuizController(
            state,
            new SubjectProviderRegistry(List.of(
                new JavaSubjectProvider(),
                new CSubjectProvider()
            )),
            new QuizQuestionSource(new Random(42)),
            time,
            view
        );
    }

    private static ApplicationState populatedState() {
        ApplicationState state = new ApplicationState();
        state.setPreference(QuestionId.SORTED, Preference.YES);
        state.selectStructure(StructureId.HASH_MAP);
        state.navigateToQuestion(QuestionId.MEMORY);
        return state;
    }

    private enum ViewState {
        NOT_STARTED,
        QUESTION,
        REVIEW,
        COMPLETED
    }

    private static final class MutableTime implements TimeSource {

        private long nanos;

        @Override
        public long nanoTime() {
            return nanos;
        }

        void advanceSeconds(long seconds) {
            nanos += Duration.ofSeconds(seconds).toNanos();
        }
    }

    private static final class RecordingView implements TimedQuizView {

        private ViewState state;
        private String subject;
        private int position;
        private int questionCount;
        private Duration elapsed;
        private AnswerReview review;
        private QuizSummary summary;
        private Theme theme;
        private boolean timerStopped;
        private boolean failRendering;

        @Override
        public void showNotStarted(
            String subjectDisplayName,
            int count
        ) {
            failIfRequested();
            state = ViewState.NOT_STARTED;
            subject = subjectDisplayName;
            questionCount = count;
        }

        @Override
        public void showQuestion(
            String subjectDisplayName,
            int currentPosition,
            int count,
            Duration elapsedTime,
            QuizQuestion question
        ) {
            failIfRequested();
            state = ViewState.QUESTION;
            subject = subjectDisplayName;
            position = currentPosition;
            questionCount = count;
            elapsed = elapsedTime;
        }

        @Override
        public void showAnswerReview(
            String subjectDisplayName,
            int currentPosition,
            int count,
            Duration elapsedTime,
            AnswerReview answerReview,
            boolean finalQuestion
        ) {
            failIfRequested();
            state = ViewState.REVIEW;
            subject = subjectDisplayName;
            position = currentPosition;
            questionCount = count;
            elapsed = elapsedTime;
            review = answerReview;
        }

        @Override
        public void showCompleted(
            String subjectDisplayName,
            QuizSummary result
        ) {
            failIfRequested();
            state = ViewState.COMPLETED;
            subject = subjectDisplayName;
            summary = result;
        }

        @Override
        public void updateElapsedTime(Duration elapsedTime) {
            failIfRequested();
            elapsed = elapsedTime;
        }

        @Override
        public void stopTimerUpdates() {
            timerStopped = true;
        }

        @Override
        public void applyTheme(Theme requestedTheme) {
            theme = requestedTheme;
        }

        private void failIfRequested() {
            if (failRendering) {
                throw new IllegalStateException("render failed");
            }
        }
    }
}
