package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ExplorerContent;
import com.fernando.ds.application.ExplorerService;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.subject.JavaSubjectProvider;

class ExplorerDetailsPanelTest {

    @Test
    void rendersEveryRequiredExplorerContentSection() throws Exception {
        ExplorerContent content = new ExplorerService().getContent(
            new JavaSubjectProvider(),
            StructureId.HASH_MAP
        ).orElseThrow();
        AtomicReference<String> html = new AtomicReference<>();
        AtomicReference<Object> headingAlignment = new AtomicReference<>();
        AtomicReference<Object> sectionSpacing = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            ExplorerDetailsPanel panel = new ExplorerDetailsPanel();
            panel.applyTheme(Theme.DARK);
            panel.showContent(content);
            html.set(panel.displayedHtml());

            HTMLDocument document = (HTMLDocument) descendantsTextPane(panel)
                .getDocument();
            Element heading = findElement(
                document.getDefaultRootElement(),
                HTML.Tag.H1
            );
            headingAlignment.set(heading.getAttributes().getAttribute(
                CSS.Attribute.TEXT_ALIGN
            ));
            Element section = findElement(
                document.getDefaultRootElement(),
                HTML.Tag.H2
            );
            sectionSpacing.set(section.getAttributes().getAttribute(
                CSS.Attribute.MARGIN_TOP
            ));
        });

        String displayed = html.get();
        assertTrue(displayed.contains("Concept"));
        assertTrue(displayed.contains("Java representation"));
        assertTrue(displayed.contains("HashMap"));
        assertTrue(displayed.contains("Strengths"));
        assertTrue(displayed.contains("Weaknesses"));
        assertTrue(displayed.contains("Supported operations"));
        assertTrue(displayed.contains("Complexity"));
        assertTrue(displayed.contains("Memory and iteration"));
        assertTrue(displayed.contains("Common use cases"));
        assertTrue(displayed.contains("Related structures"));
        assertEquals("left", headingAlignment.get().toString());
        assertEquals("14px", sectionSpacing.get().toString());
        assertFalse(displayed.contains("HASH_MAP"));
        assertFalse(displayed.contains("/content/"));
    }

    private static JEditorPane descendantsTextPane(
        ExplorerDetailsPanel panel
    ) {
        return (JEditorPane) ((JScrollPane) panel.getComponent(0))
            .getViewport()
            .getView();
    }

    private static Element findElement(Element root, HTML.Tag tag) {
        if (tag.equals(root.getAttributes().getAttribute(
            StyleConstants.NameAttribute
        ))) {
            return root;
        }
        for (int index = 0; index < root.getElementCount(); index++) {
            Element match = findElement(root.getElement(index), tag);
            if (match != null) {
                return match;
            }
        }
        return null;
    }
}
