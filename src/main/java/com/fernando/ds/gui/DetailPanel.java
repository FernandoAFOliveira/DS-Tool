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
        textPane.setText("");

        StyledDocument doc = textPane.getStyledDocument();

        try {
            doc.insertString(doc.getLength(), question.getShortText() + "\n\n", bold);
            doc.insertString(doc.getLength(), question.getLongText(), normal);
        } catch (BadLocationException e) {
            textPane.setText("Error displaying question information.");
        }
    }

    public void showDataStructure(DataStructure ds) {
        textPane.setText("");

        StyledDocument doc = textPane.getStyledDocument();

        try {
            appendCenteredTitle(doc, ds.getName());

            append(doc, "Description:\n", sectionHeader);
            append(doc, ds.getExplanation() + "\n\n", normal);

            append(doc, "Example:\n", sectionHeader);
            append(doc, ds.getExampleUse() + "\n\n", normal);

            append(doc, "API Overview:\n", sectionHeader);
            append(doc, ds.getApiOverview() + "\n\n", normal);

            append(doc, "Code:\n", sectionHeader);
            append(doc, ds.getCodeExample() + "\n\n", code);
            //doc.insertString(doc.getLength(), "Alternative:\n", bold);
            //doc.insertString(doc.getLength(), ds.getAlternative(), normal);

        } catch (BadLocationException e) {
            textPane.setText("Error displaying data structure information.");
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
}