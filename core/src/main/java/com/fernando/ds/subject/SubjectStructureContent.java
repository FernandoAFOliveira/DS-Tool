package com.fernando.ds.subject;

import java.util.List;
import java.util.Objects;

/**
 * Immutable, platform-neutral implementation guidance supplied by a subject
 * provider for one abstract data-structure concept.
 *
 * @param sections ordered subject-specific educational sections
 * @param codeExamples zero or more subject-specific code examples
 */
public record SubjectStructureContent(
    List<Section> sections,
    List<CodeExample> codeExamples
) {

    public SubjectStructureContent {
        sections = List.copyOf(Objects.requireNonNull(sections, "sections"));
        codeExamples = List.copyOf(
            Objects.requireNonNull(codeExamples, "codeExamples")
        );
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("sections must not be empty");
        }
    }

    /**
     * One titled block of prose or bullet content.
     *
     * @param title user-visible section title
     * @param paragraphs ordered prose paragraphs
     * @param bulletItems ordered bullet items
     */
    public record Section(
        String title,
        List<String> paragraphs,
        List<String> bulletItems
    ) {

        public Section {
            title = requireText(title, "title");
            paragraphs = copyText(paragraphs, "paragraphs");
            bulletItems = copyText(bulletItems, "bulletItems");
            if (paragraphs.isEmpty() && bulletItems.isEmpty()) {
                throw new IllegalArgumentException(
                    "A section must contain prose or bullet content"
                );
            }
        }
    }

    /**
     * One titled code example. Source is plain text and contains no markup.
     *
     * @param title user-visible example title
     * @param source plain source code
     */
    public record CodeExample(String title, String source) {

        public CodeExample {
            title = requireText(title, "title");
            source = requireText(source, "source");
        }
    }

    private static List<String> copyText(
        List<String> values,
        String name
    ) {
        List<String> copy = List.copyOf(
            Objects.requireNonNull(values, name)
        );
        if (copy.stream().anyMatch(value ->
            value == null || value.isBlank()
        )) {
            throw new IllegalArgumentException(
                name + " must contain non-blank text"
            );
        }
        return copy;
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
