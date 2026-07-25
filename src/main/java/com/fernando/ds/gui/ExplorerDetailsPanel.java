package com.fernando.ds.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.fernando.ds.application.ExplorerContent;
import com.fernando.ds.knowledge.DataStructureKnowledge;
import com.fernando.ds.knowledge.Ordering;
import com.fernando.ds.model.RemovalOrder;
import com.fernando.ds.util.ContentLoader;

/** Renders language-neutral Knowledge Core content as themed HTML. */
final class ExplorerDetailsPanel extends JPanel {

    private static final String CONTENT_STYLE =
        "text-align: left; margin: 0;";
    private static final String TITLE_STYLE =
        "text-align: left; margin: 0 0 14px 0;";
    private static final String SECTION_STYLE =
        "text-align: left; margin: 14px 0 6px 0;";

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
            <h1 style="%s">Explore data structures</h1>
            <p>Select a structure to browse its concepts, trade-offs,
            operations, complexity, and relationships.</p>
            """.formatted(TITLE_STYLE));
    }

    void showContent(ExplorerContent content) {
        DataStructureKnowledge knowledge = content.knowledge();
        String body = """
            <h1 style="%s">%s</h1>
            <h2 style="%s">Definition</h2>
            <p>%s</p>
            <h2 style="%s">Characteristics</h2>
            %s
            <h2 style="%s">Strengths</h2>
            %s
            <h2 style="%s">Weaknesses</h2>
            %s
            <h2 style="%s">Supported operations</h2>
            %s
            <h2 style="%s">Complexity</h2>
            <table>
            <tr><th align="left">Operation</th>
            <th align="left">Typical cost</th></tr>
            <tr><td>Lookup</td><td>%s</td></tr>
            <tr><td>Insertion and removal</td><td>%s</td></tr>
            </table>
            <h2 style="%s">Memory and iteration</h2>
            <p><strong>Memory:</strong> %s</p>
            <p><strong>Iteration:</strong> %s</p>
            <h2 style="%s">Common use cases</h2>
            %s
            <h2 style="%s">Related structures</h2>
            %s
            """.formatted(
                TITLE_STYLE,
                escape(knowledge.displayName()),
                SECTION_STYLE,
                escape(knowledge.description()),
                SECTION_STYLE,
                characteristics(knowledge),
                SECTION_STYLE,
                list(knowledge.strengths()),
                SECTION_STYLE,
                list(knowledge.weaknesses()),
                SECTION_STYLE,
                list(knowledge.supportedOperations()),
                SECTION_STYLE,
                escape(knowledge.lookupCost()),
                escape(knowledge.insertionRemovalCost()),
                SECTION_STYLE,
                escape(knowledge.memoryConsiderations()),
                escape(knowledge.iterationCharacteristics()),
                SECTION_STYLE,
                list(knowledge.commonUseCases()),
                SECTION_STYLE,
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
        textPane.setText(ContentLoader.applyThemeToHtml(
            "<div style=\"" + CONTENT_STYLE + "\">" + body + "</div>",
            currentTheme
        ));
        textPane.setCaretPosition(0);
    }

    private static String list(List<String> values) {
        return "<ul>" + values.stream()
            .map(value -> "<li>" + escape(value) + "</li>")
            .reduce("", String::concat)
            + "</ul>";
    }

    private static String characteristics(DataStructureKnowledge knowledge) {
        return "<ul>"
            + item("Key-value mapping", yesNo(knowledge.keyValueMapping()))
            + item("Duplicate values", yesNo(knowledge.allowsDuplicates()))
            + item("Indexed access", yesNo(knowledge.indexedAccess()))
            + item("Ordering", ordering(knowledge.ordering()))
            + item(
                "Removal behavior",
                removalOrder(knowledge.removalOrder())
            )
            + "</ul>";
    }

    private static String item(String label, String value) {
        return "<li><strong>" + escape(label) + ":</strong> "
            + escape(value) + "</li>";
    }

    private static String yesNo(boolean value) {
        return value ? "Yes" : "No";
    }

    private static String ordering(Ordering ordering) {
        return switch (ordering) {
            case NONE -> "No inherent order";
            case INDEX -> "Index order";
            case SORTED -> "Sorted order";
            case LIFO -> "Last-in, first-out";
            case FIFO -> "First-in, first-out";
            case PRIORITY -> "Priority order";
            case DOUBLE_ENDED -> "Double-ended";
        };
    }

    private static String removalOrder(RemovalOrder removalOrder) {
        return switch (removalOrder) {
            case ANY -> "No fixed removal order";
            case FIFO -> "First-in, first-out";
            case LIFO -> "Last-in, first-out";
            case DOUBLE_ENDED -> "Either end";
            case PRIORITY -> "Priority order";
        };
    }

    private static String relatedList(
        List<DataStructureKnowledge> related
    ) {
        return "<ul>" + related.stream()
            .map(item -> "<li>" + escape(item.displayName()) + "</li>")
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
