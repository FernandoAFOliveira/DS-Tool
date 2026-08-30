package com.fernando.ds.application.quiz;

import java.util.Objects;

/**
 * One user-visible answer choice.
 *
 * @param text concise answer text
 * @param correct whether this is the question's single correct answer
 */
public record QuizAnswerOption(String text, boolean correct) {

    public QuizAnswerOption {
        Objects.requireNonNull(text, "text");
        if (text.isBlank()) {
            throw new IllegalArgumentException("text must not be blank");
        }
    }
}
