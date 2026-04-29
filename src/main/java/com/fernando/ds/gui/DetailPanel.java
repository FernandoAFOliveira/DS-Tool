package com.fernando.ds.gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebView;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.Style;

import com.fernando.ds.library.QuestionInfo;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.util.DiagramTemplateLoader;
import com.fernando.ds.util.MermaidResult;

public class DetailPanel extends JPanel {
    private JTextPane textPane;
    private JFXPanel jfxPanel;
    private WebView webView;
    private QuestionInfo currentQuestion;
    private DataStructure currentDataStructure;
    private Theme currentTheme = Theme.LIGHT;
    private javax.swing.text.Style code, normal, title, sectionHeader;

    public DetailPanel() {
        setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setEditable(false);

        sectionHeader = textPane.addStyle("SectionHeader", null);
            StyleConstants.setBold(sectionHeader, true);
            StyleConstants.setFontSize(sectionHeader, 14);
            StyleConstants.setForeground(sectionHeader, Color.BLACK);
            StyleConstants.setLeftIndent(sectionHeader, 20);
            StyleConstants.setSpaceAbove(sectionHeader, 12);
            StyleConstants.setSpaceBelow(sectionHeader, 4);

        normal = textPane.addStyle("Normal", null);
            StyleConstants.setFontSize(normal, 14);
            StyleConstants.setForeground(normal, Color.BLACK);
            StyleConstants.setAlignment(normal, StyleConstants.ALIGN_LEFT);
            StyleConstants.setLeftIndent(normal, 35);
            StyleConstants.setRightIndent(normal, 20);

        code = textPane.addStyle("Code", null);
            StyleConstants.setFontFamily(code, "Monospaced");
            StyleConstants.setFontSize(code, 14);
            StyleConstants.setForeground(code, Color.BLACK);
            StyleConstants.setAlignment(code, StyleConstants.ALIGN_LEFT);
            StyleConstants.setLeftIndent(code, 35);
            StyleConstants.setRightIndent(code, 20);

        title = textPane.addStyle("Title", null);
            StyleConstants.setBold(title, true);
            StyleConstants.setFontSize(title, 20);
            StyleConstants.setAlignment(title, StyleConstants.ALIGN_CENTER);
            StyleConstants.setLeftIndent(title, 0);
            StyleConstants.setSpaceAbove(title, 10);
            StyleConstants.setSpaceBelow(title, 4);
    

    jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.NORTH);

        Platform.runLater(() -> {
            webView = new WebView();
            // Set the error/status listeners ONCE here
            webView.getEngine().setOnError(event -> System.err.println("JS Error: " + event.getMessage()));
            
            // Load the HTML once
            webView.getEngine().load(getClass().getResource("/diagram.html").toExternalForm());
            jfxPanel.setScene(new javafx.scene.Scene(webView));
        });

            add(new JScrollPane(textPane), BorderLayout.CENTER);
    }


    public void showQuestion(QuestionInfo question) {
        currentQuestion = question;
        currentDataStructure = null;
        textPane.setText("");

        StyledDocument doc = textPane.getStyledDocument();

        try {
            appendCenteredTitle(doc, question.getShortText());

            appendSection(doc, "What this means:", question.getLongText(), normal);

        } catch (BadLocationException e) {
            textPane.setText("Error displaying question information.");
        }
    }

    public void showDataStructure(DataStructure ds) {
        textPane.setText("");
        currentDataStructure = ds;
        MermaidResult result = DiagramTemplateLoader.getProcessedMermaid(ds.getName(), currentTheme);
            showDiagram(result.mmdSource, result.backgroundColor);
        currentQuestion = null;

        StyledDocument doc = textPane.getStyledDocument();

        try {
            appendCenteredTitle(doc, ds.getName());

            appendSection(doc, "Description:", ds.getExplanation(), normal);

            appendSection(doc, "Example:", ds.getExampleUse(), normal);

            appendSection(doc, "API Overview:", ds.getApiOverview(), normal);

            appendSection(doc, "Code:", ds.getCodeExample(), code);

        } catch (BadLocationException e) {
            textPane.setText("Error displaying data structure information.");
        }
    }

    public void showWelcome() {
        textPane.setText("");
        currentQuestion = null;
        currentDataStructure = null;

        StyledDocument doc = textPane.getStyledDocument();

        try {
            appendCenteredTitle(doc, "Data Structure Advisor");

            appendSection(doc, "Description:", "This app helps compare common Java data structures based on the needs of a programming task.", normal);

            appendSection(doc, "How to use it:", "Use the questions on the left to describe what your program needs. "
                + "The data structure list will update as you choose options. "
                + "Click a data structure name to see its description, common methods, and example code.", normal);

            appendSection(doc, "Tip:", "Choose Any when a feature does not matter for your problem. "
                + "Use the sliders to sort choices by lookup speed, add/delete speed, or memory efficiency.", normal);

        } catch (BadLocationException e) {
            textPane.setText("Error displaying welcome information.");
        }
    }

    public void showMessage(String titleText, String message) {
        currentQuestion = null;
        currentDataStructure = null;
        textPane.setText("");

        StyledDocument doc = textPane.getStyledDocument();

        try {
            appendCenteredTitle(doc, titleText);
            appendSection(doc, "Note:",  message, normal);
        } catch (BadLocationException e) {
            textPane.setText("Error displaying message.");
        }
    }

    public void showDiagram(String mmdSource, String bgColor) {
        Platform.runLater(() -> {
            if (webView != null && webView.getEngine() != null) {
                String sanitized = mmdSource.replace("\\", "\\\\")
                                            .replace("`", "\\`")
                                            .replace("\n", "\\n");
                
                webView.getEngine().executeScript("renderDiagram(`" + sanitized + "`, '" + bgColor + "')");
            }
        });
    }
    private void append(StyledDocument doc, String text, Style style) throws BadLocationException {
        int start = doc.getLength();
        doc.insertString(start, text, style);
        doc.setParagraphAttributes(start, text.length(), style, false);
    }

    private void appendCenteredTitle(StyledDocument doc, String text) throws BadLocationException {
        int start = doc.getLength();
        String fullText = text + "\n\n";
        doc.insertString(start, fullText, title);
        doc.setParagraphAttributes(start, fullText.length(), title, false);
    }
    private void appendSection(StyledDocument doc, String heading, String body, Style bodyStyle)
            throws BadLocationException {
        append(doc, heading + "\n", sectionHeader);
        append(doc, body + "\n\n", bodyStyle);
    }

    public void applyTheme(Theme theme) {
        currentTheme = theme;

        textPane.setBackground(theme.getBackground());
        textPane.setForeground(theme.getForeground());

        StyleConstants.setForeground(normal, theme.getForeground());
        StyleConstants.setForeground(sectionHeader, theme.getForeground());
        StyleConstants.setForeground(title, theme.getForeground());
        StyleConstants.setForeground(code, theme.getForeground());

        if (currentDataStructure != null) {
            showDataStructure(currentDataStructure);
        } else if (currentQuestion != null) {
            showQuestion(currentQuestion);
        } else {
            showWelcome();
        }
        

        repaint();
    }
}