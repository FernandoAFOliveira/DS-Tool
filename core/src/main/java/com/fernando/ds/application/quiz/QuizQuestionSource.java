package com.fernando.ds.application.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

import com.fernando.ds.knowledge.DataStructureKnowledge;
import com.fernando.ds.knowledge.KnowledgeCatalog;
import com.fernando.ds.subject.SubjectProvider;

/**
 * Builds one varied ten-question quiz from the Knowledge Core and a captured
 * subject provider.
 */
public final class QuizQuestionSource {

    /** Fixed release-sized session length. */
    public static final int QUESTION_COUNT = 10;
    private static final int OPTION_COUNT = 4;
    private final RandomGenerator random;

    /** Creates a production source with independently seeded randomness. */
    public QuizQuestionSource() {
        this(RandomGenerator.getDefault());
    }

    /**
     * Creates a source with injectable randomness for deterministic tests.
     *
     * @param random source used only for question and option ordering
     */
    public QuizQuestionSource(RandomGenerator random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    /**
     * Creates five language-neutral definition questions and five
     * captured-subject representation questions.
     *
     * @param provider subject captured for this session
     * @return immutable fixed-length question list
     */
    public List<QuizQuestion> create(SubjectProvider provider) {
        Objects.requireNonNull(provider, "provider");
        if (!provider.isEnabled()) {
            throw new IllegalArgumentException(
                "Quiz subject must be enabled"
            );
        }

        List<DataStructureKnowledge> knowledge = KnowledgeCatalog.getAll();
        if (knowledge.size() < 9) {
            throw new IllegalStateException(
                "Timed Quiz requires the nine release concepts"
            );
        }
        List<String> concepts = knowledge.stream()
            .map(DataStructureKnowledge::displayName)
            .toList();
        List<String> representations = knowledge.stream()
            .map(item -> provider.getRepresentation(item.id())
                .orElseThrow(() -> new IllegalStateException(
                    "Quiz subject is missing a concept representation"
                ))
                .getDisplayName())
            .toList();

        List<QuizQuestion> questions = new ArrayList<>(QUESTION_COUNT);
        for (int index = 0; index < 5; index++) {
            DataStructureKnowledge item = knowledge.get(index);
            questions.add(new QuizQuestion(
                item.id(),
                QuizQuestion.Type.CONCEPT_FROM_DEFINITION,
                "Which concept matches this definition?\n"
                    + item.description(),
                options(concepts, index),
                item.displayName() + ": " + item.description()
            ));
        }
        for (int offset = 0; offset < 5; offset++) {
            int index = offset + 4;
            DataStructureKnowledge item = knowledge.get(index);
            String representation = representations.get(index);
            questions.add(new QuizQuestion(
                item.id(),
                QuizQuestion.Type.REPRESENTATION_FROM_CONCEPT,
                "Which " + provider.displayName()
                    + " representation matches the "
                    + item.displayName() + " concept?",
                options(representations, index),
                "For this quiz, " + provider.displayName() + " represents "
                    + item.displayName() + " as " + representation + "."
            ));
        }
        shuffle(questions);
        return List.copyOf(questions);
    }

    private List<QuizAnswerOption> options(
        List<String> candidates,
        int correctIndex
    ) {
        String correct = candidates.get(correctIndex);
        List<String> selected = new ArrayList<>(OPTION_COUNT);
        selected.add(correct);
        for (int step = 1;
            selected.size() < OPTION_COUNT;
            step++) {
            String candidate = candidates.get(
                (correctIndex + step) % candidates.size()
            );
            if (!selected.contains(candidate)) {
                selected.add(candidate);
            }
        }

        List<QuizAnswerOption> result = new ArrayList<>(OPTION_COUNT);
        result.add(new QuizAnswerOption(correct, true));
        for (int index = 1; index < OPTION_COUNT; index++) {
            result.add(new QuizAnswerOption(selected.get(index), false));
        }
        shuffle(result);
        return List.copyOf(result);
    }

    private <T> void shuffle(List<T> values) {
        for (int index = values.size() - 1; index > 0; index--) {
            int swapIndex = random.nextInt(index + 1);
            T value = values.get(index);
            values.set(index, values.get(swapIndex));
            values.set(swapIndex, value);
        }
    }
}
