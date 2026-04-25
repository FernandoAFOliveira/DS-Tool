package com.fernando.ds.gui;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fernando.ds.library.QuestionInfo;
import com.fernando.ds.library.DataStructureLibrary;
import com.fernando.ds.model.DSRequirements;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.model.Preference;
import com.fernando.ds.engine.ScoringEngine;

public class AppController {

    private final DSRequirements requirements = new DSRequirements();
    private final ScoringEngine scoringEngine = new ScoringEngine();
    private final DSListPanel dsListPanel;
    private final DetailPanel detailPanel;

    public AppController(
        QuestionPanel questionPanel,
        DSListPanel dsListPanel,
        DetailPanel detailPanel
    ) {
        this.dsListPanel = dsListPanel;
        this.detailPanel = detailPanel;
        dsListPanel.setSelectionListener(detailPanel::showDataStructure);
        questionPanel.setQuestionSelectionListener(this::handleQuestionChange);
        questionPanel.setPreferenceSelectionListener(this::updatePreference);
        questionPanel.setWeightSelectionListener(this::updateWeight);
    }

    private void refreshDataStructureList() {
        List<DataStructure> all = DataStructureLibrary.getAll();
        List<DataStructure> valid = new ArrayList<>();

        System.out.println("---- Scores ----");

        for (DataStructure ds : all) {
            double score = scoringEngine.calculate(ds, requirements);
            System.out.println(ds.getName() + " = " + score);

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

        // You will later update preferences here
        // For now just refresh:
        refreshDataStructureList();
    }

    private void updatePreference(QuestionInfo q, Preference value) {
        if (q.getId() == QuestionInfo.QuestionId.KEY_VALUE) {
            requirements.setKeyValuePreference(value);
        } else if (q.getId() == QuestionInfo.QuestionId.DUPLICATES) {
            requirements.setDuplicatePreference(value);
        } else if (q.getId() == QuestionInfo.QuestionId.SORTED) {
            requirements.setSortedPreference(value);
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
        System.out.println(q.getShortText() + " = " + value);  //Debug Printing. Delete later

        refreshDataStructureList();
    }
}