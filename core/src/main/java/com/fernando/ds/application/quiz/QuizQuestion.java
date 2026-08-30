package com.fernando.ds.application.quiz;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import com.fernando.ds.knowledge.StructureId;

/**
 * Immutable multiple-choice quiz question derived from existing knowledge.
 *
 * @param structureId concept assessed by the question
 * @param type educational question pattern
 * @param prompt concise question text
 * @param options distinct choices with exactly one correct answer
 * @param explanation concise answer explanation
 */
public record QuizQuestion(
    StructureId structureId,
    Type type,
    String prompt,
    List<QuizAnswerOption> options,
    String explanation
) {

    /** The two intentionally bounded question patterns in this milestone. */
    public enum Type {
        CONCEPT_FROM_DEFINITION(false),
        REPRESENTATION_FROM_CONCEPT(true);

        private final boolean subjectSpecific;

        Type(boolean subjectSpecific) {
            this.subjectSpecific = subjectSpecific;
        }

        /** @return whether the question uses captured-subject terminology */
        public boolean isSubjectSpecific() {
            return subjectSpecific;
        }
    }

    public QuizQuestion {
        Objects.requireNonNull(structureId, "structureId");
        Objects.requireNonNull(type, "type");
        prompt = requireText(prompt, "prompt");
        explanation = requireText(explanation, "explanation");
        options = List.copyOf(Objects.requireNonNull(options, "options"));
        if (options.size() < 2) {
            throw new IllegalArgumentException(
                "A quiz question requires at least two options"
            );
        }
        if (options.stream().filter(QuizAnswerOption::correct).count() != 1) {
            throw new IllegalArgumentException(
                "A quiz question requires exactly one correct option"
            );
        }
        if (new HashSet<>(
            options.stream().map(QuizAnswerOption::text).toList()
        ).size() != options.size()) {
            throw new IllegalArgumentException(
                "Quiz answer options must be distinct"
            );
        }
    }

    /** @return the zero-based index of the single correct option */
    public int correctOptionIndex() {
        for (int index = 0; index < options.size(); index++) {
            if (options.get(index).correct()) {
                return index;
            }
        }
        throw new IllegalStateException("Question has no correct option");
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
