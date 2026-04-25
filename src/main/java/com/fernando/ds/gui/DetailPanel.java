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

    public DetailPanel() {
        setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

        normal = textPane.addStyle("Normal", null);

        bold = textPane.addStyle("Bold", null);
        StyleConstants.setBold(bold, true);

        code = textPane.addStyle("Code", null);
        StyleConstants.setFontFamily(code, "monospaced");
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
            doc.insertString(doc.getLength(), "Best choice: ", bold);
            doc.insertString(doc.getLength(), ds.getName() + "\n\n", normal);

            doc.insertString(doc.getLength(), "Why:\n", bold);
            doc.insertString(doc.getLength(), ds.getExplanation() + "\n\n", normal);

            doc.insertString(doc.getLength(), "Example:\n", bold);
            doc.insertString(doc.getLength(), ds.getExampleUse() + "\n\n", normal);

            doc.insertString(doc.getLength(), "API Overview:\n", bold);
            doc.insertString(doc.getLength(), ds.getApiOverview() + "\n\n", normal);

            doc.insertString(doc.getLength(), "Code:\n", bold);
            doc.insertString(doc.getLength(), ds.getCodeExample() + "\n\n", code);

            doc.insertString(doc.getLength(), "Alternative:\n", bold);
            doc.insertString(doc.getLength(), ds.getAlternative(), normal);

        } catch (BadLocationException e) {
            textPane.setText("Error displaying data structure information.");
        }
    }
}