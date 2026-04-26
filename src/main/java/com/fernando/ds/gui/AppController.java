package com.fernando.ds.gui;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fernando.ds.library.QuestionInfo;
import com.fernando.ds.library.DataStructureLibrary;
import com.fernando.ds.model.DSRequirements;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.model.Preference;
import com.fernando.ds.model.RemovalOrder;
import com.fernando.ds.engine.ScoringEngine;

public class AppController {

    private final DSRequirements requirements = new DSRequirements();
    private final ScoringEngine scoringEngine = new ScoringEngine();
    private final DSListPanel dsListPanel;
    private final DetailPanel detailPanel;
    private final QuestionPanel questionPanel;

    public AppController(
        QuestionPanel questionPanel,
        DSListPanel dsListPanel,
        DetailPanel detailPanel
    ) {
        this.questionPanel = questionPanel;
        this.dsListPanel = dsListPanel;
        this.detailPanel = detailPanel;        
        dsListPanel.setSelectionListener(detailPanel::showDataStructure);
        questionPanel.setQuestionSelectionListener(this::handleQuestionChange);
        questionPanel.setPreferenceSelectionListener(this::updatePreference);
        questionPanel.setWeightSelectionListener(this::updateWeight);
        questionPanel.setRemovalOrderSelectionListener(this::updateRemovalOrder);
    }

    private void updateRemovalOrder(QuestionInfo q, RemovalOrder value) {
        requirements.setRemovalOrderPreference(value);
        refreshDataStructureList();
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
        detailPanel.showQuestion(q);

        refreshDataStructureList();
    }

    private void updatePreference(QuestionInfo q, Preference value) {
        if (q.getId() == QuestionInfo.QuestionId.KEY_VALUE) {
            requirements.setKeyValuePreference(value);

            if (value == Preference.YES) {
                requirements.setDuplicatePreference(Preference.ANY);
                questionPanel.setDuplicateQuestionEnabled(false);
                detailPanel.showMessage(
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

    public void reset() {
        requirements.reset();
        questionPanel.resetSelections();
        detailPanel.showWelcome();
        refreshDataStructureList();
    }

    public void applyTheme(Theme theme) {
        ThemeManager.applyThemeToComponent(detailPanel, theme);
        detailPanel.applyTheme(theme);
    }
    }