package com.fernando.ds.application;

import java.util.Objects;

import com.fernando.ds.knowledge.StructureId;

/**
 * One language-neutral learning prompt derived from the Knowledge Core.
 *
 * @param structureId abstract concept taught by the question
 * @param prompt text shown before the answer is revealed
 * @param answer concept-first answer text
 */
public record LearningQuestion(
    StructureId structureId,
    String prompt,
    String answer
) {

    public LearningQuestion {
        Objects.requireNonNull(structureId, "structureId");
        prompt = requireText(prompt, "prompt");
        answer = requireText(answer, "answer");
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
