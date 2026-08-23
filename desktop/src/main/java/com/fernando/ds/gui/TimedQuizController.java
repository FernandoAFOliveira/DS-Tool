package com.fernando.ds.gui;

import java.time.Duration;
import java.util.Objects;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.application.quiz.QuizQuestionSource;
import com.fernando.ds.application.quiz.QuizSession;
import com.fernando.ds.application.quiz.TimeSource;
import com.fernando.ds.subject.SubjectProvider;
import com.fernando.ds.subject.SubjectProviderRegistry;

/**
 * Coordinates one subject-bound Timed Quiz without modifying shared
 * Advisor, Explorer, or Flash Cards state.
 */
final class TimedQuizController {

    private final ApplicationState state;
    private final SubjectProviderRegistry subjectProviders;
    private final QuizQuestionSource questionSource;
    private final TimedQuizView view;
    private QuizSession session;

    TimedQuizController(
        ApplicationState state,
        SubjectProviderRegistry subjectProviders,
        QuizQuestionSource questionSource,
        TimeSource timeSource,
        TimedQuizView view
    ) {
        this.state = Objects.requireNonNull(state, "state");
        this.subjectProviders = Objects.requireNonNull(
            subjectProviders,
            "subjectProviders"
        );
        this.questionSource = Objects.requireNonNull(
            questionSource,
            "questionSource"
        );
        session = QuizSession.notStarted(
            Objects.requireNonNull(timeSource, "timeSource")
        );
        this.view = Objects.requireNonNull(view, "view");
    }

    void activate() {
        activate(activeSubject());
    }

    /**
     * Renders a requested global subject before its commit. An active quiz
     * ignores that request and continues rendering its captured subject.
     */
    void activate(SubjectProvider requestedProvider) {
        Objects.requireNonNull(requestedProvider, "requestedProvider");
        if (session.lifecycle() == QuizSession.Lifecycle.NOT_STARTED) {
            view.showNotStarted(
                requestedProvider.displayName(),
                QuizQuestionSource.QUESTION_COUNT
            );
            return;
        }
        render(session);
    }

    void startQuiz() {
        startWith(activeSubject());
    }

    void startNewQuiz() {
        startWith(activeSubject());
    }

    void submitAnswer(int selectedOptionIndex) {
        QuizSession proposed = session.submitAnswer(selectedOptionIndex);
        render(proposed);
        session = proposed;
    }

    void showNext() {
        QuizSession proposed = session.next();
        render(proposed);
        session = proposed;
    }

    /**
     * Abandons only quiz state and returns to the current-subject intro.
     */
    void reset() {
        SubjectProvider provider = activeSubject();
        QuizSession proposed = session.abandon();
        view.showNotStarted(
            provider.displayName(),
            QuizQuestionSource.QUESTION_COUNT
        );
        session = proposed;
    }

    void refreshElapsedTime() {
        if (session.lifecycle() == QuizSession.Lifecycle.QUESTION_ACTIVE
            || session.lifecycle()
                == QuizSession.Lifecycle.ANSWER_REVIEWED) {
            view.updateElapsedTime(session.elapsed());
        }
    }

    void deactivate() {
        view.stopTimerUpdates();
    }

    void applyTheme(Theme theme) {
        view.applyTheme(Objects.requireNonNull(theme, "theme"));
        activate();
    }

    QuizSession session() {
        return session;
    }

    private void startWith(SubjectProvider provider) {
        QuizSession proposed = session.start(
            provider.id(),
            provider.displayName(),
            questionSource.create(provider)
        );
        render(proposed);
        session = proposed;
    }

    private void render(QuizSession requested) {
        String subject = requested.subjectDisplayName().orElseThrow();
        Duration elapsed = requested.elapsed();
        switch (requested.lifecycle()) {
            case QUESTION_ACTIVE -> view.showQuestion(
                subject,
                requested.currentPosition(),
                requested.questionCount(),
                elapsed,
                requested.currentQuestion()
            );
            case ANSWER_REVIEWED -> view.showAnswerReview(
                subject,
                requested.currentPosition(),
                requested.questionCount(),
                elapsed,
                requested.currentReview(),
                requested.currentPosition() == requested.questionCount()
            );
            case COMPLETED -> view.showCompleted(
                subject,
                requested.summary()
            );
            case NOT_STARTED -> throw new IllegalStateException(
                "Not-started quiz requires a subject preview"
            );
        }
    }

    private SubjectProvider activeSubject() {
        return subjectProviders.get(state.getActiveSubject());
    }
}
