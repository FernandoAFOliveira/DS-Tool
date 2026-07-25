package com.fernando.ds.gui;

import com.fernando.ds.library.QuestionInfo;
import com.fernando.ds.knowledge.DataStructureKnowledge;
import com.fernando.ds.knowledge.KnowledgeCatalog;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProvider;
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

    /**
     * Renders established Java content or a concept-first view for another
     * subject.
     *
     * @param provider active or requested subject provider
     * @param representation provider representation to display
     */
    public void showDataStructure(
        SubjectProvider provider,
        DataStructure representation
    ) {
        if (provider.id() == SubjectId.JAVA) {
            showDataStructure(representation);
            return;
        }

        DataStructureKnowledge knowledge = KnowledgeCatalog.get(
            representation.getStructureId()
        );
        String html = """
            <h1>%s</h1>
            <h2>Concept</h2>
            <p>%s</p>
            <h2>%s representation</h2>
            <p>%s</p>
            <p>This name describes a common implementation strategy rather
            than a standardized library type.</p>
            """.formatted(
                escape(knowledge.displayName()),
                escape(knowledge.description()),
                escape(provider.displayName()),
                escape(representation.getDisplayName())
            );

        textPane.setText(ContentLoader.applyThemeToHtml(html, currentTheme));
        textPane.setCaretPosition(0);
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

    String displayedHtml() {
        return textPane.getText();
    }

    private static String escape(String value) {
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
}
