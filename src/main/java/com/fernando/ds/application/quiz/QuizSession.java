package com.fernando.ds.application.quiz;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fernando.ds.subject.SubjectId;

/**
 * Immutable, presentation-neutral Timed Quiz session.
 *
 * <p>Every transition returns a new session so a controller can render the
 * proposed state before committing it.</p>
 */
public final class QuizSession {

    /** Explicit lifecycle for the single supported quiz mode. */
    public enum Lifecycle {
        NOT_STARTED,
        QUESTION_ACTIVE,
        ANSWER_REVIEWED,
        COMPLETED
    }

    /**
     * One committed answer used for feedback and score accounting.
     *
     * @param question question that was answered
     * @param selectedOptionIndex selected zero-based option index
     * @param correct whether the selected option was correct
     */
    public record AnswerReview(
        QuizQuestion question,
        int selectedOptionIndex,
        boolean correct
    ) {
        public AnswerReview {
            Objects.requireNonNull(question, "question");
            if (selectedOptionIndex < 0
                || selectedOptionIndex >= question.options().size()) {
                throw new IllegalArgumentException(
                    "selected option is outside the question"
                );
            }
        }
    }

    private final TimeSource timeSource;
    private final Lifecycle lifecycle;
    private final SubjectId subjectId;
    private final String subjectDisplayName;
    private final List<QuizQuestion> questions;
    private final List<AnswerReview> answers;
    private final int currentIndex;
    private final long startedAt;
    private final Long stoppedAt;

    private QuizSession(
        TimeSource timeSource,
        Lifecycle lifecycle,
        SubjectId subjectId,
        String subjectDisplayName,
        List<QuizQuestion> questions,
        List<AnswerReview> answers,
        int currentIndex,
        long startedAt,
        Long stoppedAt
    ) {
        this.timeSource = Objects.requireNonNull(timeSource, "timeSource");
        this.lifecycle = Objects.requireNonNull(lifecycle, "lifecycle");
        this.subjectId = subjectId;
        this.subjectDisplayName = subjectDisplayName;
        this.questions = List.copyOf(questions);
        this.answers = List.copyOf(answers);
        this.currentIndex = currentIndex;
        this.startedAt = startedAt;
        this.stoppedAt = stoppedAt;
    }

    /** Creates an empty session that has not captured a subject. */
    public static QuizSession notStarted(TimeSource timeSource) {
        return new QuizSession(
            timeSource,
            Lifecycle.NOT_STARTED,
            null,
            null,
            List.of(),
            List.of(),
            0,
            0,
            0L
        );
    }

    /**
     * Starts or restarts a fixed question set and captures its subject.
     */
    public QuizSession start(
        SubjectId requestedSubjectId,
        String requestedSubjectDisplayName,
        List<QuizQuestion> requestedQuestions
    ) {
        if (lifecycle != Lifecycle.NOT_STARTED
            && lifecycle != Lifecycle.COMPLETED) {
            throw new IllegalStateException("Quiz is already active");
        }
        Objects.requireNonNull(requestedSubjectId, "requestedSubjectId");
        String displayName = requireText(
            requestedSubjectDisplayName,
            "requestedSubjectDisplayName"
        );
        List<QuizQuestion> questionCopy = List.copyOf(
            Objects.requireNonNull(requestedQuestions, "requestedQuestions")
        );
        if (questionCopy.isEmpty()) {
            throw new IllegalArgumentException(
                "Quiz requires at least one question"
            );
        }
        long now = timeSource.nanoTime();
        return new QuizSession(
            timeSource,
            Lifecycle.QUESTION_ACTIVE,
            requestedSubjectId,
            displayName,
            questionCopy,
            List.of(),
            0,
            now,
            null
        );
    }

    /**
     * Records exactly one answer for the active question.
     */
    public QuizSession submitAnswer(int selectedOptionIndex) {
        requireLifecycle(Lifecycle.QUESTION_ACTIVE);
        QuizQuestion question = currentQuestion();
        if (selectedOptionIndex < 0
            || selectedOptionIndex >= question.options().size()) {
            throw new IllegalArgumentException(
                "selected option is outside the question"
            );
        }

        boolean correct = question.options()
            .get(selectedOptionIndex)
            .correct();
        List<AnswerReview> updated = new ArrayList<>(answers);
        updated.add(new AnswerReview(
            question,
            selectedOptionIndex,
            correct
        ));
        Long stop = currentIndex == questions.size() - 1
            ? timeSource.nanoTime()
            : null;
        return copy(
            Lifecycle.ANSWER_REVIEWED,
            updated,
            currentIndex,
            stop
        );
    }

    /**
     * Advances only after feedback; the final reviewed answer transitions to
     * the completed summary.
     */
    public QuizSession next() {
        requireLifecycle(Lifecycle.ANSWER_REVIEWED);
        if (currentIndex == questions.size() - 1) {
            return copy(
                Lifecycle.COMPLETED,
                answers,
                currentIndex,
                stoppedAt == null ? timeSource.nanoTime() : stoppedAt
            );
        }
        return copy(
            Lifecycle.QUESTION_ACTIVE,
            answers,
            currentIndex + 1,
            null
        );
    }

    /**
     * Explicitly abandons the current session and freezes its elapsed time.
     */
    public QuizSession abandon() {
        long stop = switch (lifecycle) {
            case QUESTION_ACTIVE, ANSWER_REVIEWED -> timeSource.nanoTime();
            case COMPLETED -> stoppedAt;
            case NOT_STARTED -> 0L;
        };
        return new QuizSession(
            timeSource,
            Lifecycle.NOT_STARTED,
            null,
            null,
            List.of(),
            List.of(),
            0,
            startedAt,
            stop
        );
    }

    /** @return current lifecycle */
    public Lifecycle lifecycle() {
        return lifecycle;
    }

    /** @return captured subject, empty before start */
    public Optional<SubjectId> subjectId() {
        return Optional.ofNullable(subjectId);
    }

    /** @return captured subject display name, empty before start */
    public Optional<String> subjectDisplayName() {
        return Optional.ofNullable(subjectDisplayName);
    }

    /** @return fixed session question count */
    public int questionCount() {
        return questions.size();
    }

    /** @return one-based current position */
    public int currentPosition() {
        requireStarted();
        return currentIndex + 1;
    }

    /** @return current question */
    public QuizQuestion currentQuestion() {
        requireStarted();
        return questions.get(currentIndex);
    }

    /** @return latest answer feedback */
    public AnswerReview currentReview() {
        requireLifecycle(Lifecycle.ANSWER_REVIEWED);
        return answers.getLast();
    }

    /** @return number of correct submitted answers */
    public int correctCount() {
        return (int) answers.stream()
            .filter(AnswerReview::correct)
            .count();
    }

    /** @return immutable answer history */
    public List<AnswerReview> answers() {
        return answers;
    }

    /** @return deterministic elapsed session time */
    public Duration elapsed() {
        if (lifecycle == Lifecycle.NOT_STARTED
            && startedAt == 0
            && stoppedAt != null
            && stoppedAt == 0) {
            return Duration.ZERO;
        }
        long end = stoppedAt == null
            ? timeSource.nanoTime()
            : stoppedAt;
        return Duration.ofNanos(Math.max(0, end - startedAt));
    }

    /** @return completed score summary */
    public QuizSummary summary() {
        requireLifecycle(Lifecycle.COMPLETED);
        int correct = correctCount();
        int incorrect = answers.size() - correct;
        int percentage = Math.round(
            (correct * 100.0f) / questions.size()
        );
        return new QuizSummary(
            correct,
            incorrect,
            percentage,
            elapsed()
        );
    }

    private QuizSession copy(
        Lifecycle nextLifecycle,
        List<AnswerReview> nextAnswers,
        int nextIndex,
        Long nextStoppedAt
    ) {
        return new QuizSession(
            timeSource,
            nextLifecycle,
            subjectId,
            subjectDisplayName,
            questions,
            nextAnswers,
            nextIndex,
            startedAt,
            nextStoppedAt
        );
    }

    private void requireStarted() {
        if (lifecycle == Lifecycle.NOT_STARTED) {
            throw new IllegalStateException("Quiz has not started");
        }
    }

    private void requireLifecycle(Lifecycle expected) {
        if (lifecycle != expected) {
            throw new IllegalStateException(
                "Quiz state must be " + expected
            );
        }
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
