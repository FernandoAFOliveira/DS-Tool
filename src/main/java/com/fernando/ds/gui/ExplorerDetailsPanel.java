package com.fernando.ds.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.fernando.ds.application.ExplorerContent;
import com.fernando.ds.application.ExplorerContent.RelatedStructure;
import com.fernando.ds.knowledge.DataStructureKnowledge;
import com.fernando.ds.util.ContentLoader;

/** Renders educational Knowledge Core content as themed HTML. */
final class ExplorerDetailsPanel extends JPanel {

    private final JEditorPane textPane = new JEditorPane();
    private Theme currentTheme = Theme.LIGHT;

    ExplorerDetailsPanel() {
        setLayout(new BorderLayout());
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        add(new JScrollPane(textPane), BorderLayout.CENTER);
        showWelcome();
    }

    void showWelcome() {
        setBody("""
            <h1>Explore data structures</h1>
            <p>Select a structure to browse its concepts, trade-offs,
            operations, complexity, and active-subject representation.</p>
            """);
    }

    void showContent(ExplorerContent content) {
        DataStructureKnowledge knowledge = content.knowledge();
        String body = """
            <h1>%s</h1>
            <h2>Concept</h2>
            <p>%s</p>
            <h2>%s representation</h2>
            <p>%s</p>
            <h2>Strengths</h2>
            %s
            <h2>Weaknesses</h2>
            %s
            <h2>Supported operations</h2>
            %s
            <h2>Complexity</h2>
            <ul><li><strong>Lookup:</strong> %s</li>
            <li><strong>Insertion and removal:</strong> %s</li></ul>
            <h2>Memory and iteration</h2>
            <p><strong>Memory:</strong> %s</p>
            <p><strong>Iteration:</strong> %s</p>
            <h2>Common use cases</h2>
            %s
            <h2>Related structures</h2>
            %s
            """.formatted(
                escape(knowledge.displayName()),
                escape(knowledge.description()),
                escape(content.subjectDisplayName()),
                escape(content.representation().getDisplayName()),
                list(knowledge.strengths()),
                list(knowledge.weaknesses()),
                list(knowledge.supportedOperations()),
                escape(knowledge.lookupCost()),
                escape(knowledge.insertionRemovalCost()),
                escape(knowledge.memoryConsiderations()),
                escape(knowledge.iterationCharacteristics()),
                list(knowledge.commonUseCases()),
                relatedList(content.relatedStructures())
            );
        setBody(body);
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

    private static String list(List<String> values) {
        return "<ul>" + values.stream()
            .map(value -> "<li>" + escape(value) + "</li>")
            .reduce("", String::concat)
            + "</ul>";
    }

    private static String relatedList(List<RelatedStructure> related) {
        return "<ul>" + related.stream()
            .map(item -> {
                String representation = item.representation()
                    .map(value -> " (" + escape(value.getDisplayName()) + ")")
                    .orElse("");
                return "<li>" + escape(item.knowledge().displayName())
                    + representation + "</li>";
            })
            .reduce("", String::concat)
            + "</ul>";
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
