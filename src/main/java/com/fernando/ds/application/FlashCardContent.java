package com.fernando.ds.application;

import java.util.Objects;
import java.util.Optional;

/**
 * Concept-first flash-card content with optional subject-specific context.
 *
 * @param question shared Knowledge Core question
 * @param subjectRepresentation supplementary active-subject representation
 */
public record FlashCardContent(
    LearningQuestion question,
    Optional<SubjectRepresentation> subjectRepresentation
) {

    public FlashCardContent {
        Objects.requireNonNull(question, "question");
        subjectRepresentation = Objects.requireNonNull(
            subjectRepresentation,
            "subjectRepresentation"
        );
    }

    /**
     * Supplementary subject-specific name displayed after the concept answer.
     *
     * @param subjectDisplayName active subject name
     * @param representationDisplayName subject representation name
     */
    public record SubjectRepresentation(
        String subjectDisplayName,
        String representationDisplayName
    ) {

        public SubjectRepresentation {
            subjectDisplayName = requireText(
                subjectDisplayName,
                "subjectDisplayName"
            );
            representationDisplayName = requireText(
                representationDisplayName,
                "representationDisplayName"
            );
        }

        private static String requireText(String value, String name) {
            Objects.requireNonNull(value, name);
            if (value.isBlank()) {
                throw new IllegalArgumentException(name + " must not be blank");
            }
            return value;
        }
    }
}
