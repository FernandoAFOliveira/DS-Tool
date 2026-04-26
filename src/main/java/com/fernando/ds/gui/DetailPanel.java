package com.fernando.ds.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

import com.fernando.ds.library.QuestionInfo;
import com.fernando.ds.model.DataStructure;

public class DetailPanel extends JPanel {

    private JTextPane textPane;
    private Style bold;
    private Style code;
    private Style normal;
    private Style title;
    private Style sectionHeader;
    private QuestionInfo currentQuestion;
    private DataStructure currentDataStructure;

    public DetailPanel() {
        setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

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

        bold = textPane.addStyle("Bold", null);
            StyleConstants.setBold(bold, true);
            StyleConstants.setAlignment(bold, StyleConstants.ALIGN_LEFT);
            StyleConstants.setLeftIndent(bold, 20);
            StyleConstants.setSpaceAbove(bold, 10);
            StyleConstants.setSpaceBelow(bold, 4);

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
        currentQuestion = null;

        StyledDocument doc = textPane.getStyledDocument();

        try {
            appendCenteredTitle(doc, ds.getName());

            appendSection(doc, "Description:", ds.getExplanation(), normal);

            appendSection(doc, "Example:", ds.getExampleUse(), normal);

            appendSection(doc, "API Overview:", ds.getApiOverview(), normal);

            appendSection(doc, "Code:", ds.getCodeExample(), code);
            //doc.insertString(doc.getLength(), "Alternative:\n", bold);
            //doc.insertString(doc.getLength(), ds.getAlternative(), normal);

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