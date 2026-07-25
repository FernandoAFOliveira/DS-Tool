package com.fernando.ds.gui;

import java.awt.BorderLayout;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.fernando.ds.application.ExplorerContent;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.util.DiagramTemplateLoader;
import com.fernando.ds.util.MermaidResult;

/** Swing composition for the Explorer experience. */
final class ExplorerPanel extends JPanel implements ExplorerView {

    private final DSListPanel structures = new DSListPanel();
    private final DiagramPanel diagram = new DiagramPanel();
    private final ExplorerDetailsPanel details = new ExplorerDetailsPanel();
    private Theme currentTheme = Theme.LIGHT;

    ExplorerPanel() {
        setLayout(new BorderLayout());
        add(structures, BorderLayout.WEST);

        JSplitPane content = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            diagram,
            details
        );
        content.setResizeWeight(0.48);
        content.setOneTouchExpandable(true);
        content.setContinuousLayout(true);
        SwingUtilities.invokeLater(() -> content.setDividerLocation(0.48));
        add(content, BorderLayout.CENTER);
    }

    void setSelectionListener(Consumer<DataStructure> listener) {
        structures.setSelectionListener(listener);
    }

    @Override
    public void showStructures(
        List<DataStructure> available,
        StructureId selectedStructureId
    ) {
        structures.updateList(available, selectedStructureId);
    }

    @Override
    public void showWelcome() {
        MermaidResult result = DiagramTemplateLoader.getProcessedMermaid(
            "welcome",
            currentTheme
        );
        diagram.showDiagram(result.mmdSource, result.backgroundColor);
        details.showWelcome();
    }

    @Override
    public void showContent(ExplorerContent content) {
        MermaidResult result = DiagramTemplateLoader.getProcessedMermaid(
            content.representation().getName(),
            currentTheme
        );
        diagram.showDiagram(result.mmdSource, result.backgroundColor);
        details.showContent(content);
    }

    @Override
    public void applyTheme(Theme theme) {
        currentTheme = theme;
        details.applyTheme(theme);
    }
}
