package com.fernando.ds.application.quiz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.fernando.ds.subject.CSubjectProvider;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.subject.SubjectProvider;

class QuizQuestionSourceTest {

    private static final List<String> INTERNAL_MARKERS = List.of(
        "StructureId",
        "DYNAMIC_ARRAY",
        "HASH_MAP",
        "/content/",
        "\\content\\",
        ".mmd",
        ".java"
    );
    private static final List<String> SUBJECT_TERMS = List.of(
        "ArrayList",
        "ArrayDeque",
        "PriorityQueue",
        "HashSet",
        "TreeSet",
        "HashMap",
        "TreeMap",
        "Binary heap",
        "Circular-buffer queue",
        "Hash table",
        "Balanced-tree map"
    );

    @Test
    void javaAndCProduceFixedValidMixedQuizzes() {
        for (SubjectProvider provider : List.of(
            new JavaSubjectProvider(),
            new CSubjectProvider()
        )) {
            List<QuizQuestion> questions = fixedSource(42).create(provider);

            assertEquals(QuizQuestionSource.QUESTION_COUNT, questions.size());
            assertEquals(
                5,
                questions.stream()
                    .filter(question ->
                        question.type()
                            == QuizQuestion.Type.CONCEPT_FROM_DEFINITION
                    )
                    .count()
            );
            assertEquals(
                5,
                questions.stream()
                    .filter(question ->
                        question.type()
                            == QuizQuestion.Type.REPRESENTATION_FROM_CONCEPT
                    )
                    .count()
            );

            for (QuizQuestion question : questions) {
                assertFalse(question.prompt().isBlank());
                assertFalse(question.explanation().isBlank());
                assertEquals(4, question.options().size());
                assertEquals(
                    1,
                    question.options().stream()
                        .filter(QuizAnswerOption::correct)
                        .count()
                );
                assertEquals(
                    question.options().size(),
                    new HashSet<>(
                        question.options().stream()
                            .map(QuizAnswerOption::text)
                            .toList()
                    ).size()
                );
                assertTrue(question.options().stream()
                    .noneMatch(option -> option.text().isBlank()));
                String visible = question.prompt() + " "
                    + question.explanation() + " "
                    + question.options();
                INTERNAL_MARKERS.forEach(marker ->
                    assertFalse(visible.contains(marker), visible)
                );
            }
            Set<StructureId> covered = new HashSet<>(
                questions.stream()
                    .map(QuizQuestion::structureId)
                    .toList()
            );
            assertEquals(Set.of(StructureId.values()), covered);
            assertEquals(
                2,
                questions.stream()
                    .filter(question ->
                        question.structureId() == StructureId.DEQUE
                    )
                    .count()
            );
        }
    }

    @Test
    void neutralQuestionsStayNeutralAndSpecificQuestionsUseProvider() {
        List<QuizQuestion> javaQuestions =
            fixedSource(73).create(new JavaSubjectProvider());
        List<QuizQuestion> cQuestions =
            fixedSource(73).create(new CSubjectProvider());

        for (QuizQuestion javaQuestion : javaQuestions) {
            QuizQuestion cQuestion = matching(cQuestions, javaQuestion);
            if (javaQuestion.type().isSubjectSpecific()) {
                assertTrue(javaQuestion.prompt().contains("Java"));
                assertTrue(cQuestion.prompt().contains("C representation"));
                assertNotEquals(
                    javaQuestion.options(),
                    cQuestion.options()
                );
                continue;
            }
            assertEquals(javaQuestion.prompt(), cQuestion.prompt());
            assertEquals(
                javaQuestion.explanation(),
                cQuestion.explanation()
            );
            assertEquals(javaQuestion.options(), cQuestion.options());
            String neutral = javaQuestion.prompt()
                + javaQuestion.explanation()
                + javaQuestion.options();
            SUBJECT_TERMS.forEach(term ->
                assertFalse(neutral.contains(term), neutral)
            );
        }
    }

    @Test
    void fixedSeedIsRepeatableAndDifferentSeedsVaryOrdering() {
        SubjectProvider provider = new JavaSubjectProvider();

        List<QuizQuestion> first = fixedSource(91).create(provider);
        List<QuizQuestion> repeated = fixedSource(91).create(provider);
        List<QuizQuestion> varied = fixedSource(92).create(provider);

        assertEquals(first, repeated);
        assertNotEquals(first, varied);
    }

    private static QuizQuestion matching(
        List<QuizQuestion> questions,
        QuizQuestion requested
    ) {
        return questions.stream()
            .filter(question ->
                question.structureId() == requested.structureId()
                    && question.type() == requested.type()
            )
            .findFirst()
            .orElseThrow();
    }

    private static QuizQuestionSource fixedSource(long seed) {
        return new QuizQuestionSource(new Random(seed));
    }
}
