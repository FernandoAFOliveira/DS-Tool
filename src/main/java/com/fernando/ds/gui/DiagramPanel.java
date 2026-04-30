package com.fernando.ds.gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebView;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class DiagramPanel extends JPanel {

    private final JFXPanel jfxPanel;
    private WebView webView;
    private boolean diagramPageReady = false;
    private String pendingMmdSource;
    private String pendingBgColor;

    public DiagramPanel() {
        setLayout(new BorderLayout());

        jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);

        initializeWebView();
    }

    private void initializeWebView() {
        Platform.runLater(() -> {
            webView = new WebView();

            webView.getEngine().setOnError(event ->
                System.err.println("JS Error: " + event.getMessage())
            );

        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                diagramPageReady = true;

                if (pendingMmdSource != null && pendingBgColor != null) {
                    showDiagram(pendingMmdSource, pendingBgColor);
                    pendingMmdSource = null;
                    pendingBgColor = null;
                }
            }
        });

            webView.getEngine().load(getClass().getResource("/diagram.html").toExternalForm());
            jfxPanel.setScene(new javafx.scene.Scene(webView));
        });
    }

    public void showDiagram(String mmdSource, String bgColor) {
        Platform.runLater(() -> {
            if (!diagramPageReady || webView == null || webView.getEngine() == null) {
                pendingMmdSource = mmdSource;
                pendingBgColor = bgColor;
                return;
            }

            String sanitized = mmdSource.replace("\\", "\\\\")
                                        .replace("`", "\\`")
                                        .replace("\n", "\\n");

            webView.getEngine().executeScript(
                "renderDiagram(`" + sanitized + "`, '" + bgColor + "')"
            );
        });
    }
}