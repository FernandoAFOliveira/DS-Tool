package com.fernando.ds.application.quiz;

import java.time.Duration;
import java.util.Objects;

/**
 * Transparent completed-quiz totals.
 *
 * @param correctCount one point per correct answer
 * @param incorrectCount answered questions that were incorrect
 * @param percentage whole-number score percentage
 * @param elapsed session-level elapsed time
 */
public record QuizSummary(
    int correctCount,
    int incorrectCount,
    int percentage,
    Duration elapsed
) {

    public QuizSummary {
        if (correctCount < 0 || incorrectCount < 0) {
            throw new IllegalArgumentException(
                "Quiz counts must not be negative"
            );
        }
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException(
                "percentage must be between 0 and 100"
            );
        }
        Objects.requireNonNull(elapsed, "elapsed");
    }

    /** @return total answered questions */
    public int questionCount() {
        return correctCount + incorrectCount;
    }
}
