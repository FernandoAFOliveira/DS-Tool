package com.fernando.ds.gui;

import com.fernando.ds.library.QuestionInfo;
import com.fernando.ds.model.DataStructure;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class ExplanationPanel extends JPanel {

    private final JTextPane textPane;
    private final TextStyles textStyles;

    public ExplanationPanel() {
        setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));

        textStyles = new TextStyles(textPane);

        add(new JScrollPane(textPane), BorderLayout.CENTER);
    }

    public void showQuestion(QuestionInfo question) {
        textPane.setText("");

        StyledDocument doc = textPane.getStyledDocument();

        try {
            appendCenteredTitle(doc, question.getShortText());
            appendSection(doc, "What this means:", question.getLongText(), textStyles.getNormal());
        } catch (BadLocationException e) {
            textPane.setText("Error displaying question information.");
        }

        textPane.setCaretPosition(0);
    }

    public void showDataStructure(DataStructure ds) {
        textPane.setText("");

        StyledDocument doc = textPane.getStyledDocument();

        try {
            appendCenteredTitle(doc, ds.getName());

            appendSection(doc, "Description:", ds.getExplanation(), textStyles.getNormal());
            appendSection(doc, "Example:", ds.getExampleUse(), textStyles.getNormal());
            appendSection(doc, "API Overview:", ds.getApiOverview(), textStyles.getNormal());
            appendSection(doc, "Code:", ds.getCodeExample(), textStyles.getCode());

        } catch (BadLocationException e) {
            textPane.setText("Error displaying data structure information.");
        }

        textPane.setCaretPosition(0);
    }

    public void showWelcome() {
        textPane.setText("");

        StyledDocument doc = textPane.getStyledDocument();

        try {
            appendCenteredTitle(doc, "Welcome to Data Structure Advisor");

            appendSection(
                doc,
                "Description:",
                "This app helps compare common Java data structures based on the needs of a programming task.",
                textStyles.getNormal()
            );

            appendSection(
                doc,
                "How to use it:",
                "Use the questions on the left to describe what your program needs. "
                    + "The data structure list will update as you choose options. "
                    + "Click a data structure name to see its description, common methods, and example code.",
                textStyles.getNormal()
            );

            appendSection(
                doc,
                "Tip:",
                "Choose Any when a feature does not matter for your problem. "
                    + "Use the sliders to sort choices by lookup speed, add/delete speed, or memory efficiency.",
                textStyles.getNormal()
            );

        } catch (BadLocationException e) {
            textPane.setText("Error displaying welcome information.");
        }

        textPane.setCaretPosition(0);
    }

    public void showMessage(String titleText, String message) {
        textPane.setText("");

        StyledDocument doc = textPane.getStyledDocument();

        try {
            appendCenteredTitle(doc, titleText);
            appendSection(doc, "Note:", message, textStyles.getNormal());
        } catch (BadLocationException e) {
            textPane.setText("Error displaying message.");
        }

        textPane.setCaretPosition(0);
    }

    public void applyTheme(Theme theme) {
        textPane.setBackground(theme.getBackground());
        textPane.setForeground(theme.getForeground());
        textStyles.applyTheme(theme);
        repaint();
    }

    private void append(StyledDocument doc, String text, Style style) throws BadLocationException {
        int start = doc.getLength();
        doc.insertString(start, text, style);
        doc.setParagraphAttributes(start, text.length(), style, false);
    }

    private void appendCenteredTitle(StyledDocument doc, String text) throws BadLocationException {
        int start = doc.getLength();
        String fullText = text + "\n\n";
        doc.insertString(start, fullText, textStyles.getTitle());
        doc.setParagraphAttributes(start, fullText.length(), textStyles.getTitle(), false);
    }

    private void appendSection(StyledDocument doc, String heading, String body, Style bodyStyle)
            throws BadLocationException {
        append(doc, heading + "\n", textStyles.getSectionHeader());
        append(doc, body + "\n\n", bodyStyle);
    }
}