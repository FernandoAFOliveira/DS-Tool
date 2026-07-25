package com.fernando.ds.gui;

import java.awt.BorderLayout;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.fernando.ds.application.ExplorerContent;
import com.fernando.ds.application.ExplorerSubject;
import com.fernando.ds.subject.SubjectId;

/** Owns the Overview-first, dynamically generated Explorer detail tabs. */
final class ExplorerTabbedDetailsPanel extends JPanel {

    private static final String OVERVIEW = "Overview";

    private final JTabbedPane tabs = new JTabbedPane();
    private final ExplorerDetailsPanel overview = new ExplorerDetailsPanel();
    private final Map<SubjectId, ExplorerSubjectDetailsPanel> subjectPanels =
        new EnumMap<>(SubjectId.class);
    private Theme currentTheme = Theme.LIGHT;

    ExplorerTabbedDetailsPanel() {
        setLayout(new BorderLayout());
        tabs.addTab(OVERVIEW, overview);
        add(tabs, BorderLayout.CENTER);
    }

    void showSubjects(List<ExplorerSubject> subjects) {
        SubjectId selectedSubject = selectedSubjectId();
        subjectPanels.clear();
        tabs.removeAll();
        tabs.addTab(OVERVIEW, overview);

        for (ExplorerSubject subject : subjects) {
            ExplorerSubjectDetailsPanel panel =
                new ExplorerSubjectDetailsPanel(subject.displayName());
            ThemeManager.applyThemeToComponent(panel, currentTheme);
            panel.applyTheme(currentTheme);
            subjectPanels.put(subject.id(), panel);
            tabs.addTab(subject.displayName(), panel);
        }

        ExplorerSubjectDetailsPanel selectedPanel =
            subjectPanels.get(selectedSubject);
        tabs.setSelectedComponent(
            selectedPanel == null ? overview : selectedPanel
        );
    }

    void showWelcome() {
        overview.showWelcome();
        subjectPanels.values().forEach(
            ExplorerSubjectDetailsPanel::showWelcome
        );
    }

    void showContent(ExplorerContent content) {
        overview.showContent(content);
        content.subjectContents().forEach(subject -> {
            ExplorerSubjectDetailsPanel panel =
                subjectPanels.get(subject.subjectId());
            if (panel != null) {
                panel.showContent(subject);
            }
        });
    }

    void applyTheme(Theme theme) {
        currentTheme = theme;
        overview.applyTheme(theme);
        subjectPanels.values().forEach(panel -> panel.applyTheme(theme));
    }

    List<String> tabTitles() {
        return java.util.stream.IntStream.range(0, tabs.getTabCount())
            .mapToObj(tabs::getTitleAt)
            .toList();
    }

    void selectSubject(SubjectId subjectId) {
        ExplorerSubjectDetailsPanel panel = subjectPanels.get(subjectId);
        tabs.setSelectedComponent(panel == null ? overview : panel);
    }

    String selectedTitle() {
        return tabs.getTitleAt(tabs.getSelectedIndex());
    }

    String overviewHtml() {
        return overview.displayedHtml();
    }

    String subjectHtml(SubjectId subjectId) {
        ExplorerSubjectDetailsPanel panel = subjectPanels.get(subjectId);
        return panel == null ? "" : panel.displayedHtml();
    }

    private SubjectId selectedSubjectId() {
        java.awt.Component selected = tabs.getSelectedComponent();
        return subjectPanels.entrySet().stream()
            .filter(entry -> entry.getValue() == selected)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);
    }
}
