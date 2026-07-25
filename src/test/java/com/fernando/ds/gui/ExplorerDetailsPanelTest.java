package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingUtilities;

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

        SwingUtilities.invokeAndWait(() -> {
            ExplorerDetailsPanel panel = new ExplorerDetailsPanel();
            panel.applyTheme(Theme.DARK);
            panel.showContent(content);
            html.set(panel.displayedHtml());
        });

        String displayed = html.get();
        assertTrue(displayed.contains("Concept"));
        assertTrue(displayed.contains("Java representation"));
        assertTrue(displayed.contains("HashMap"));
        assertTrue(displayed.contains("Strengths"));
        assertTrue(displayed.contains("Weaknesses"));
        assertTrue(displayed.contains("Supported operations"));
        assertTrue(displayed.contains("Complexity"));
        assertTrue(displayed.contains("Common use cases"));
        assertTrue(displayed.contains("Related structures"));
        assertFalse(displayed.contains("HASH_MAP"));
        assertFalse(displayed.contains("/content/"));
    }
}
