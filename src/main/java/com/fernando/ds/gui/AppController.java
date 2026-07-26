package com.fernando.ds.gui;

import java.util.List;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.application.RecommendationService;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.library.QuestionInfo;
import com.fernando.ds.library.QuestionLibrary;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.model.Preference;
import com.fernando.ds.model.RemovalOrder;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProvider;
import com.fernando.ds.subject.SubjectProviderRegistry;
import com.fernando.ds.util.DiagramTemplateLoader;
import com.fernando.ds.util.MermaidResult;

public class AppController {

    private final ApplicationState state;
    private final RecommendationService recommendationService =
        new RecommendationService();
    private final SubjectProviderRegistry subjectProviders;
    private final DSListPanel dsListPanel;
    private final DiagramPanel diagramPanel;
    private final ExplanationPanel explanationPanel;
    private final QuestionPanel questionPanel;
    private final SubjectContextView subjectContextView;

    public AppController(
        ApplicationState state,
        SubjectProviderRegistry subjectProviders,
        QuestionPanel questionPanel,
        DSListPanel dsListPanel,
        DiagramPanel diagramPanel,
        ExplanationPanel explanationPanel,
        SubjectContextView subjectContextView
    ) {
        this.state = state;
        this.questionPanel = questionPanel;
        this.subjectProviders = subjectProviders;
        this.dsListPanel = dsListPanel;
        this.diagramPanel = diagramPanel;
        this.explanationPanel = explanationPanel;
        this.subjectContextView = subjectContextView;

        dsListPanel.setSelectionListener(this::showDataStructure);
        questionPanel.setQuestionSelectionListener(this::handleQuestionChange);
        questionPanel.setPreferenceSelectionListener(this::updatePreference);
        questionPanel.setWeightSelectionListener(this::updateWeight);
        questionPanel.setRemovalOrderSelectionListener(this::updateRemovalOrder);
    }

    public void initialize() {
        questionPanel.applyAnswers(state.getRecommendationAnswers());
        refreshDataStructureList();
        renderNavigation();
        subjectContextView.showSubjectContext(
            activeSubject().displayName()
        );
    }

    /** Restores Advisor controls without discarding an Explorer selection. */
    public void activate() {
        activate(activeSubject());
    }

    /**
     * Restores Advisor controls using a requested subject provider.
     *
     * <p>This overload supports rendering a subject before it is committed to
     * shared application state.</p>
     */
    public void activate(SubjectProvider provider) {
        questionPanel.applyAnswers(state.getRecommendationAnswers());
        refreshDataStructureList(provider, false);
        renderNavigation(provider);
        subjectContextView.showSubjectContext(provider.displayName());
    }

    private void showDataStructure(DataStructure dataStructure) {
        state.selectStructure(dataStructure.getStructureId());
        state.navigateToDataStructure();
        renderDataStructure(activeSubject(), dataStructure);
    }

    private void renderDataStructure(
        SubjectProvider provider,
        DataStructure dataStructure
    ) {
        MermaidResult result = DiagramTemplateLoader.getProcessedMermaid(
            dataStructure.getStructureId(),
            currentTheme()
        );

        diagramPanel.showDiagram(result.mmdSource, result.backgroundColor);
        explanationPanel.showDataStructure(provider, dataStructure);
    }

    private void updateRemovalOrder(QuestionInfo question, RemovalOrder value) {
        state.setRemovalOrder(value);
        refreshDataStructureList();
    }

    public void applyTheme(Theme theme) {
        state.setAppearance(theme.getAppearance());
        explanationPanel.applyTheme(theme);
        renderNavigation();
        refreshDataStructureList();
    }

    private Theme currentTheme() {
        return Theme.fromAppearance(state.getAppearance());
    }

    private void refreshDataStructureList() {
        refreshDataStructureList(activeSubject(), true);
    }

    private void refreshDataStructureList(
        SubjectProvider provider,
        boolean clearIneligibleSelection
    ) {
        List<DataStructure> valid = recommendationService.recommend(
            state.getRecommendationAnswers()
        ).stream()
            .map(recommendation -> provider.getRepresentation(
                recommendation.structure().id()
            ))
            .flatMap(java.util.Optional::stream)
            .toList();

        StructureId requestedSelection = state.getSelectedStructureId()
            .orElse(null);
        boolean selectedIsAvailable = requestedSelection == null
            || valid.stream().anyMatch(dataStructure ->
                dataStructure.getStructureId() == requestedSelection
            );
        StructureId selectedId = requestedSelection;

        if (!selectedIsAvailable && clearIneligibleSelection) {
            state.clearSelectedStructure();
            if (state.getNavigation().kind()
                == ApplicationState.NavigationKind.DATA_STRUCTURE) {
                state.navigateToWelcome();
                renderNavigation(provider);
            }
            selectedId = null;
        } else if (!selectedIsAvailable) {
            selectedId = null;
        }

        dsListPanel.updateList(valid, selectedId);
    }

    private void handleQuestionChange(QuestionInfo question) {
        state.navigateToQuestion(question.getId());
        explanationPanel.showQuestion(question);
        refreshDataStructureList();
    }

    private void updatePreference(QuestionInfo question, Preference value) {
        state.setPreference(question.getId(), value);
        questionPanel.applyAnswers(state.getRecommendationAnswers());

        if (question.getId() == QuestionInfo.QuestionId.KEY_VALUE
            && value == Preference.YES) {
            String title = "Key-value mapping selected";
            String message = activeSubject().id()
                == SubjectId.JAVA
                    ? "Keys must be unique in Java maps. Different keys may "
                        + "still point to the same value, so the duplicate "
                        + "question has been set to Any."
                    : "Keys must be unique in key-value mappings. Different "
                        + "keys may still point to the same value, so the "
                        + "duplicate question has been set to Any.";
            state.navigateToMessage(title, message);
            explanationPanel.showMessage(title, message);
        }

        refreshDataStructureList();
    }

    private void updateWeight(QuestionInfo question, int value) {
        state.setWeight(question.getId(), value);
        refreshDataStructureList();
    }

    private void renderNavigation() {
        renderNavigation(activeSubject());
    }

    private void renderNavigation(SubjectProvider provider) {
        ApplicationState.Navigation navigation = state.getNavigation();

        switch (navigation.kind()) {
            case WELCOME -> showWelcome();
            case QUESTION -> explanationPanel.showQuestion(
                findQuestion(navigation.questionId())
            );
            case DATA_STRUCTURE -> state.getSelectedStructureId()
                .flatMap(provider::getRepresentation)
                .ifPresentOrElse(
                    representation -> renderDataStructure(
                        provider,
                        representation
                    ),
                    this::showWelcome
                );
            case MESSAGE -> explanationPanel.showMessage(
                navigation.title(),
                navigation.message()
            );
        }
    }

    private QuestionInfo findQuestion(QuestionInfo.QuestionId questionId) {
        for (QuestionInfo question : QuestionLibrary.getAll()) {
            if (question.getId() == questionId) {
                return question;
            }
        }
        throw new IllegalStateException("Unknown question: " + questionId);
    }

    private SubjectProvider activeSubject() {
        return subjectProviders.get(state.getActiveSubject());
    }

    private void showWelcome() {
        MermaidResult result = DiagramTemplateLoader.getProcessedMermaid(
            "welcome",
            currentTheme()
        );

        diagramPanel.showDiagram(result.mmdSource, result.backgroundColor);
        explanationPanel.showWelcome();
    }

    public void reset() {
        state.resetAdvisorSession();
        questionPanel.applyAnswers(state.getRecommendationAnswers());
        questionPanel.clearSelectedQuestion();
        refreshDataStructureList();
        renderNavigation();
    }
}
