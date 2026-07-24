package com.fernando.ds.gui;

import com.fernando.ds.library.QuestionInfo;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.util.ContentLoader;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class ExplanationPanel extends JPanel {

    private final JEditorPane textPane;
    private Theme currentTheme = Theme.LIGHT;

    public ExplanationPanel() {
        setLayout(new BorderLayout());

        textPane = new JEditorPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);

        add(new JScrollPane(textPane), BorderLayout.CENTER);

        showWelcome();
    }

    public void showWelcome() {
        displayContent("/content/welcome.html");
    }

    public void showQuestionsHelp() {
        displayContent("/content/questions.html");
    }

    public void showQuestion(QuestionInfo question) {
        displayContent(
            "/content/" + question.getId().name().toLowerCase() + ".html"
        );
    }

    public void showDataStructure(DataStructure ds) {
        displayContent("/content/" + ds.getName().toLowerCase() + ".html");
    }

    public void showMessage(String titleText, String message) {
        String html =
            "<h1>" + titleText + "</h1>"
            + "<p>" + message + "</p>";

        textPane.setText(ContentLoader.applyThemeToHtml(html, currentTheme));
        textPane.setCaretPosition(0);
    }

    public void applyTheme(Theme theme) {
        currentTheme = theme;
    }

    private void displayContent(String path) {
        textPane.setContentType("text/html");
        textPane.setText(ContentLoader.loadThemedHtml(path, currentTheme));
        textPane.setCaretPosition(0);
    }
}