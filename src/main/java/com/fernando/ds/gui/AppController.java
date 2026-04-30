package com.fernando.ds.gui;

// Java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Projects
import com.fernando.ds.library.QuestionInfo;
import com.fernando.ds.library.DataStructureLibrary;
import com.fernando.ds.model.DSRequirements;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.model.Preference;
import com.fernando.ds.model.RemovalOrder;
import com.fernando.ds.util.DiagramTemplateLoader;
import com.fernando.ds.util.MermaidResult;
import com.fernando.ds.engine.ScoringEngine;

public class AppController {

    private final DSRequirements requirements = new DSRequirements();
    private final ScoringEngine scoringEngine = new ScoringEngine();
    private final DSListPanel dsListPanel;
     private final DiagramPanel diagramPanel;
    private final ExplanationPanel explanationPanel;
    private Theme currentTheme = Theme.LIGHT;
    private final QuestionPanel questionPanel;
    private DataStructure currentDataStructure;
    private QuestionInfo currentQuestion;

    public AppController(
        QuestionPanel questionPanel,
        DSListPanel dsListPanel,
        DiagramPanel diagramPanel,
        ExplanationPanel explanationPanel
    ) {
        this.questionPanel = questionPanel;
        this.dsListPanel = dsListPanel;
        this.diagramPanel = diagramPanel;
        this.explanationPanel = explanationPanel;

        dsListPanel.setSelectionListener(this::showDataStructure);
        questionPanel.setQuestionSelectionListener(this::handleQuestionChange);
        questionPanel.setPreferenceSelectionListener(this::updatePreference);
        questionPanel.setWeightSelectionListener(this::updateWeight);
        questionPanel.setRemovalOrderSelectionListener(this::updateRemovalOrder);
    }

    private void showDataStructure(DataStructure ds) {
        currentDataStructure = ds;
        currentQuestion = null;
        MermaidResult result = DiagramTemplateLoader.getProcessedMermaid(
            ds.getName(),
            currentTheme
        );

        diagramPanel.showDiagram(result.mmdSource, result.backgroundColor);
        explanationPanel.showDataStructure(ds);
    }

    private void updateRemovalOrder(QuestionInfo q, RemovalOrder value) {
        requirements.setRemovalOrderPreference(value);
        refreshDataStructureList();
    }

    public void applyTheme(Theme theme) {

        currentTheme = theme;
        ThemeManager.applyThemeToComponent(explanationPanel, theme);
        explanationPanel.applyTheme(theme);
            if (currentDataStructure != null) {
        showDataStructure(currentDataStructure);
        } else {
            showWelcome();
        }

        refreshDataStructureList();
        showWelcome();
    }

    private void refreshDataStructureList() {
        List<DataStructure> all = DataStructureLibrary.getAll();
        List<DataStructure> valid = new ArrayList<>();

        for (DataStructure ds : all) {
            double score = scoringEngine.calculate(ds, requirements);

            if (score >= 0) {
                ds.setLastCalculatedScore(score);
                valid.add(ds);
            }
        }

        Collections.sort(valid);
        dsListPanel.updateList(valid);
    }

    private void handleQuestionChange(QuestionInfo q) {
        explanationPanel.showQuestion(q);

        refreshDataStructureList();
    }

    private void updatePreference(QuestionInfo q, Preference value) {
        if (q.getId() == QuestionInfo.QuestionId.KEY_VALUE) {
            requirements.setKeyValuePreference(value);

            if (value == Preference.YES) {
                requirements.setDuplicatePreference(Preference.ANY);
                questionPanel.setDuplicateQuestionEnabled(false);
                explanationPanel.showMessage(
                    "Key-value mapping selected",
                    "Keys must be unique in Java maps. Different keys may still point to the same value, so the duplicate question has been set to Any."
                );
            } else {
                questionPanel.setDuplicateQuestionEnabled(true);
            }

        } else if (q.getId() == QuestionInfo.QuestionId.DUPLICATES) {
            requirements.setDuplicatePreference(value);

        } else if (q.getId() == QuestionInfo.QuestionId.SORTED) {
            requirements.setSortedPreference(value);
        }
        else if (q.getId() == QuestionInfo.QuestionId.INDEXED) {
            requirements.setIndexedPreference(value);
        }

        refreshDataStructureList();
    }

    private void updateWeight(QuestionInfo q, int value) {
        if (q.getId() == QuestionInfo.QuestionId.LOOKUP) {
            requirements.setLookupWeight(value);
        } else if (q.getId() == QuestionInfo.QuestionId.ADD_DELETE) {
            requirements.setAddDeleteWeight(value);
        } else if (q.getId() == QuestionInfo.QuestionId.MEMORY) {
            requirements.setMemoryWeight(value);
        }

        refreshDataStructureList();
    }

    private void showWelcome() {
        MermaidResult result = DiagramTemplateLoader.getProcessedMermaid(
            "welcome",
            currentTheme
        );

        diagramPanel.showDiagram(result.mmdSource, result.backgroundColor);
        explanationPanel.showWelcome();
    }
    
    public void reset() {
        requirements.reset();
        questionPanel.resetSelections();
        explanationPanel.showWelcome();
        refreshDataStructureList();
        showWelcome();
    }


    }