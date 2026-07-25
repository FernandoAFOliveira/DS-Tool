package com.fernando.ds.gui;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.fernando.ds.application.ExplorerContent.SubjectContent;
import com.fernando.ds.util.ContentLoader;

/** Renders one enabled subject provider's Explorer detail content. */
final class ExplorerSubjectDetailsPanel extends JPanel {

    private final String subjectDisplayName;
    private final JEditorPane textPane = new JEditorPane();
    private Theme currentTheme = Theme.LIGHT;

    ExplorerSubjectDetailsPanel(String subjectDisplayName) {
        this.subjectDisplayName = subjectDisplayName;
        setLayout(new BorderLayout());
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        add(new JScrollPane(textPane), BorderLayout.CENTER);
        showWelcome();
    }

    void showWelcome() {
        setBody("""
            <h1>%s</h1>
            <p>Select a structure to browse subject-specific implementation
            guidance.</p>
            """.formatted(
                SubjectContentHtmlRenderer.escape(subjectDisplayName)
            ));
    }

    void showContent(SubjectContent content) {
        setBody(SubjectContentHtmlRenderer.render(content));
    }

    void applyTheme(Theme theme) {
        currentTheme = theme;
    }

    String displayedHtml() {
        return textPane.getText();
    }

    private void setBody(String body) {
        textPane.setText(ContentLoader.applyThemeToHtml(body, currentTheme));
        textPane.setCaretPosition(0);
    }
}
