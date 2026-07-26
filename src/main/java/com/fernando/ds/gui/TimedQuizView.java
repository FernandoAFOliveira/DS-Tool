package com.fernando.ds.gui;

import java.time.Duration;

import com.fernando.ds.application.quiz.QuizQuestion;
import com.fernando.ds.application.quiz.QuizSession.AnswerReview;
import com.fernando.ds.application.quiz.QuizSummary;

/** Display boundary for the subject-bound Timed Quiz activity. */
interface TimedQuizView {

    /** Shows the pre-session purpose, subject preview, and fixed length. */
    void showNotStarted(String subjectDisplayName, int questionCount);

    /** Shows one unanswered question. */
    void showQuestion(
        String subjectDisplayName,
        int position,
        int questionCount,
        Duration elapsed,
        QuizQuestion question
    );

    /** Shows immediate feedback while requiring an explicit Next action. */
    void showAnswerReview(
        String subjectDisplayName,
        int position,
        int questionCount,
        Duration elapsed,
        AnswerReview review,
        boolean finalQuestion
    );

    /** Shows transparent final totals. */
    void showCompleted(
        String subjectDisplayName,
        QuizSummary summary
    );

    /** Updates only the elapsed-time label. */
    void updateElapsedTime(Duration elapsed);

    /** Stops presentation timer updates while the view is not displayed. */
    void stopTimerUpdates();

    /** Applies the active application theme. */
    void applyTheme(Theme theme);
}
