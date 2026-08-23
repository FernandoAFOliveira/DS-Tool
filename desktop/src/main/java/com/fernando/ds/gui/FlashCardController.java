package com.fernando.ds.gui;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.application.FlashCardContent;
import com.fernando.ds.application.FlashCardContent.SubjectRepresentation;
import com.fernando.ds.application.FlashCardSession;
import com.fernando.ds.application.LearningProgress;
import com.fernando.ds.application.LearningQuestion;
import com.fernando.ds.application.LearningQuestionSource;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.subject.SubjectProvider;
import com.fernando.ds.subject.SubjectProviderRegistry;

/**
 * Coordinates concept-first Flash Cards without changing Advisor or Explorer
 * selection state.
 */
final class FlashCardController {

    private final ApplicationState state;
    private final SubjectProviderRegistry subjectProviders;
    private final List<LearningQuestion> questions;
    private final FlashCardView view;

    FlashCardController(
        ApplicationState state,
        SubjectProviderRegistry subjectProviders,
        LearningQuestionSource questionSource,
        FlashCardView view
    ) {
        this.state = Objects.requireNonNull(state, "state");
        this.subjectProviders = Objects.requireNonNull(
            subjectProviders,
            "subjectProviders"
        );
        this.questions = List.copyOf(
            Objects.requireNonNull(questionSource, "questionSource").getAll()
        );
        if (questions.isEmpty()) {
            throw new IllegalArgumentException(
                "Flash Cards requires at least one question"
            );
        }
        this.view = Objects.requireNonNull(view, "view");
    }

    void activate() {
        activate(activeSubject());
    }

    /**
     * Renders the current card using a requested provider before subject
     * commit.
     */
    void activate(SubjectProvider provider) {
        Objects.requireNonNull(provider, "provider");
        FlashCardSession session = state.getFlashCardSession();
        int index = indexOf(session.currentStructureId());
        render(
            provider,
            index,
            session.answerRevealed(),
            state.getLearningProgress().reviewedCount()
        );
        view.showSubjectContext(provider.displayName());
    }

    void revealAnswer() {
        FlashCardSession session = state.getFlashCardSession();
        int index = indexOf(session.currentStructureId());
        LearningProgress progress = state.getLearningProgress();
        int reviewed = progress.reviewedCount()
            + (progress.isReviewed(session.currentStructureId()) ? 0 : 1);

        // Render first so a presentation failure cannot commit false progress.
        render(activeSubject(), index, true, reviewed);
        state.revealCurrentFlashCard();
    }

    void showPrevious() {
        navigateBy(-1);
    }

    void showNext() {
        navigateBy(1);
    }

    void applyTheme(Theme theme) {
        view.applyTheme(Objects.requireNonNull(theme, "theme"));
        activate();
    }

    private void navigateBy(int offset) {
        int current = indexOf(
            state.getFlashCardSession().currentStructureId()
        );
        int requested = current + offset;
        if (requested < 0 || requested >= questions.size()) {
            return;
        }

        LearningQuestion next = questions.get(requested);
        // Render first so a presentation failure cannot move the session.
        render(
            activeSubject(),
            requested,
            false,
            state.getLearningProgress().reviewedCount()
        );
        state.navigateFlashCard(next.structureId());
    }

    private void render(
        SubjectProvider provider,
        int index,
        boolean revealed,
        int reviewed
    ) {
        LearningQuestion question = questions.get(index);
        view.showCard(
            new FlashCardContent(
                question,
                resolveSubjectRepresentation(
                    provider,
                    question.structureId()
                )
            ),
            index + 1,
            questions.size(),
            reviewed,
            revealed,
            index > 0,
            index < questions.size() - 1
        );
    }

    private Optional<SubjectRepresentation> resolveSubjectRepresentation(
        SubjectProvider provider,
        StructureId structureId
    ) {
        return provider.getRepresentation(structureId)
            .map(representation -> new SubjectRepresentation(
                provider.displayName(),
                representation.getDisplayName()
            ));
    }

    private SubjectProvider activeSubject() {
        return subjectProviders.get(state.getActiveSubject());
    }

    private int indexOf(StructureId structureId) {
        for (int index = 0; index < questions.size(); index++) {
            if (questions.get(index).structureId() == structureId) {
                return index;
            }
        }
        throw new IllegalStateException(
            "Flash Cards question is unavailable: " + structureId
        );
    }
}
