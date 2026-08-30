package com.fernando.ds.application.quiz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.subject.CSubjectProvider;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectId;

class QuizSessionTest {

    @Test
    void applicationStateDoesNotOwnQuizSessionOrProgress() {
        assertFalse(
            List.of(ApplicationState.class.getDeclaredFields()).stream()
                .anyMatch(field ->
                    field.getName().toLowerCase().contains("quiz")
                        || field.getType().equals(QuizSession.class)
                )
        );
    }

    @Test
    void capturesSubjectAndFixedQuestionCountIndependentlyOfGlobalState() {
        MutableTime time = new MutableTime();
        ApplicationState state = new ApplicationState();
        QuizSession session = start(time);

        state.setActiveSubject(SubjectId.C);

        assertEquals(
            SubjectId.JAVA,
            session.subjectId().orElseThrow()
        );
        assertEquals("Java", session.subjectDisplayName().orElseThrow());
        assertEquals(10, session.questionCount());
        assertEquals(1, session.currentPosition());
        assertEquals(SubjectId.C, state.getActiveSubject());
    }

    @Test
    void oneAnswerIsAcceptedAndNextIsRequiredForProgress() {
        QuizSession session = start(new MutableTime());
        int correct = session.currentQuestion().correctOptionIndex();

        QuizSession reviewed = session.submitAnswer(correct);

        assertEquals(
            QuizSession.Lifecycle.ANSWER_REVIEWED,
            reviewed.lifecycle()
        );
        assertEquals(1, reviewed.currentPosition());
        assertEquals(1, reviewed.correctCount());
        assertThrows(
            IllegalStateException.class,
            () -> reviewed.submitAnswer(correct)
        );

        QuizSession next = reviewed.next();
        assertEquals(
            QuizSession.Lifecycle.QUESTION_ACTIVE,
            next.lifecycle()
        );
        assertEquals(2, next.currentPosition());
    }

    @Test
    void scoreChangesOnlyForCorrectAnswersAndSummaryIsTransparent() {
        MutableTime time = new MutableTime();
        QuizSession session = start(time);

        for (int index = 0; index < session.questionCount(); index++) {
            int correct = session.currentQuestion().correctOptionIndex();
            int selected = index < 6
                ? correct
                : (correct + 1)
                    % session.currentQuestion().options().size();
            session = session.submitAnswer(selected);
            if (index == session.questionCount() - 1) {
                time.advanceSeconds(1);
                assertEquals(
                    QuizSession.Lifecycle.ANSWER_REVIEWED,
                    session.lifecycle()
                );
            }
            session = session.next();
        }

        QuizSummary summary = session.summary();
        assertEquals(6, summary.correctCount());
        assertEquals(4, summary.incorrectCount());
        assertEquals(60, summary.percentage());
        assertEquals(10, summary.questionCount());
    }

    @Test
    void timerStartsStopsOnFinalAnswerAndUsesNoRealSleeping() {
        MutableTime time = new MutableTime();
        QuizSession session = start(time);
        time.advanceSeconds(12);
        assertEquals(Duration.ofSeconds(12), session.elapsed());

        for (int index = 0; index < session.questionCount(); index++) {
            session = session.submitAnswer(
                session.currentQuestion().correctOptionIndex()
            );
            if (index < session.questionCount() - 1) {
                session = session.next();
            }
        }
        Duration stopped = session.elapsed();
        time.advanceSeconds(30);

        assertEquals(stopped, session.elapsed());
        assertEquals(Duration.ofSeconds(12), stopped);
        assertEquals(
            QuizSession.Lifecycle.ANSWER_REVIEWED,
            session.lifecycle()
        );
        assertEquals(
            QuizSession.Lifecycle.COMPLETED,
            session.next().lifecycle()
        );
    }

    @Test
    void abandonStopsTimerAndAllowsCleanNewSession() {
        MutableTime time = new MutableTime();
        QuizSession active = start(time);
        time.advanceSeconds(8);

        QuizSession abandoned = active.abandon();
        time.advanceSeconds(20);

        assertEquals(
            QuizSession.Lifecycle.NOT_STARTED,
            abandoned.lifecycle()
        );
        assertEquals(Duration.ofSeconds(8), abandoned.elapsed());
        QuizSession restarted = abandoned.start(
            SubjectId.C,
            "C",
            new QuizQuestionSource(new Random(42))
                .create(new CSubjectProvider())
        );
        assertEquals(SubjectId.C, restarted.subjectId().orElseThrow());
        assertEquals(0, restarted.correctCount());
        assertTrue(restarted.answers().isEmpty());
        assertFalse(restarted.elapsed().isNegative());
    }

    private static QuizSession start(MutableTime time) {
        return QuizSession.notStarted(time).start(
            SubjectId.JAVA,
            "Java",
            new QuizQuestionSource(new Random(42))
                .create(new JavaSubjectProvider())
        );
    }

    static final class MutableTime implements TimeSource {

        private long nanos;

        @Override
        public long nanoTime() {
            return nanos;
        }

        void advanceSeconds(long seconds) {
            nanos += Duration.ofSeconds(seconds).toNanos();
        }
    }
}
