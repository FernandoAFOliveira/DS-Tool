package com.fernando.ds.gui;

import java.util.List;

import com.fernando.ds.application.ExplorerContent;
import com.fernando.ds.application.ExplorerSubject;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.model.DataStructure;

/** Presentation boundary used by the display-independent Explorer controller. */
interface ExplorerView extends SubjectContextView {

    void showSubjects(List<ExplorerSubject> subjects);

    void showStructures(
        List<DataStructure> structures,
        StructureId selectedStructureId
    );

    void showWelcome();

    void showContent(ExplorerContent content);

    void applyTheme(Theme theme);
}
