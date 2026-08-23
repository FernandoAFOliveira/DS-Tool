package com.fernando.ds.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fernando.ds.diagram.DiagramCatalog;
import com.fernando.ds.diagram.DiagramDescriptor;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.util.DiagramTemplateLoader;
import com.fernando.ds.util.MermaidResult;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebView;

/**
 * Renders static Mermaid diagrams and owns local concept-diagram selection.
 */
public class DiagramPanel extends JPanel {

    private final JFXPanel jfxPanel;
    private final JPanel selectorBar = new JPanel(
        new FlowLayout(FlowLayout.CENTER, 8, 2)
    );
    private final JComboBox<DiagramDescriptor> selector =
        new JComboBox<>();
    private final DiagramSelectionModel selectionModel =
        new DiagramSelectionModel();
    private StructureId currentStructureId;
    private Theme currentTheme = Theme.LIGHT;
    private boolean updatingSelector;
    private WebView webView;
    private boolean diagramPageReady;
    private String pendingMmdSource;
    private String pendingBgColor;
    private boolean pendingBranding;

    public DiagramPanel() {
        setLayout(new BorderLayout());

        JLabel selectorLabel = new JLabel("Diagram:");
        selectorLabel.setLabelFor(selector);
        selector.setFocusable(true);
        selector.addActionListener(event -> {
            if (!updatingSelector && currentStructureId != null) {
                UiActionGuard.run(
                    this,
                    "Diagram",
                    this::selectAndRender
                );
            }
        });
        selectorBar.add(selectorLabel);
        selectorBar.add(selector);
        selectorBar.setVisible(false);
        add(selectorBar, BorderLayout.NORTH);

        jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);
        initializeWebView();
    }

    /**
     * Displays the locally selected view for a concept. The primary view is
     * selected by default.
     */
    public void showStructure(StructureId structureId, Theme theme) {
        List<DiagramDescriptor> available = DiagramCatalog.get(structureId);
        DiagramDescriptor selected = selectionModel.selected(
            structureId,
            available
        );
        MermaidResult result = DiagramTemplateLoader.getProcessedMermaid(
            structureId,
            selected.id(),
            theme
        );

        currentStructureId = structureId;
        currentTheme = theme;
        updateSelector(available, selected);
        queueDiagram(result.mmdSource, result.backgroundColor, false);
    }

    /**
     * Displays a standalone diagram without concept-specific selector state.
     */
    public void showDiagram(String mmdSource, String bgColor) {
        selectorBar.setVisible(false);
        currentStructureId = null;
        queueDiagram(mmdSource, bgColor, true);
    }

    private void selectAndRender() {
        DiagramDescriptor selected =
            (DiagramDescriptor) selector.getSelectedItem();
        if (selected == null) {
            return;
        }
        List<DiagramDescriptor> available =
            DiagramCatalog.get(currentStructureId);
        selectionModel.select(
            currentStructureId,
            selected.id(),
            available
        );
        MermaidResult result = DiagramTemplateLoader.getProcessedMermaid(
            currentStructureId,
            selected.id(),
            currentTheme
        );
        queueDiagram(result.mmdSource, result.backgroundColor, false);
    }

    private void updateSelector(
        List<DiagramDescriptor> available,
        DiagramDescriptor selected
    ) {
        updatingSelector = true;
        try {
            selector.removeAllItems();
            available.forEach(selector::addItem);
            selector.setSelectedItem(selected);
            selectorBar.setVisible(
                selectorVisibleFor(available.size())
            );
        } finally {
            updatingSelector = false;
        }
    }

    static boolean selectorVisibleFor(int diagramCount) {
        return diagramCount > 1;
    }

    private void initializeWebView() {
        Platform.runLater(() -> {
            webView = new WebView();
            webView.getEngine().setOnError(event ->
                System.err.println("JS Error: " + event.getMessage())
            );
            webView.getEngine().getLoadWorker().stateProperty().addListener(
                (observable, oldState, newState) -> {
                    if (newState
                        == javafx.concurrent.Worker.State.SUCCEEDED) {
                        diagramPageReady = true;
                        if (pendingMmdSource != null
                            && pendingBgColor != null) {
                            queueDiagram(
                                pendingMmdSource,
                                pendingBgColor,
                                pendingBranding
                            );
                            pendingMmdSource = null;
                            pendingBgColor = null;
                        }
                    }
                }
            );
            webView.getEngine().load(
                getClass().getResource("/diagram.html").toExternalForm()
            );
            jfxPanel.setScene(new javafx.scene.Scene(webView));
        });
    }

    private void queueDiagram(
        String mmdSource,
        String bgColor,
        boolean showBranding
    ) {
        Platform.runLater(() -> {
            if (!diagramPageReady
                || webView == null
                || webView.getEngine() == null) {
                pendingMmdSource = mmdSource;
                pendingBgColor = bgColor;
                pendingBranding = showBranding;
                return;
            }

            String sanitized = mmdSource
                .replace("\\", "\\\\")
                .replace("`", "\\`")
                .replace("\n", "\\n");
            webView.getEngine().executeScript(
                "renderDiagram(`" + sanitized + "`, '" + bgColor + "', "
                    + showBranding + ")"
            );
        });
    }
}
