package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.subject.CSubjectProvider;
import com.fernando.ds.subject.JavaSubjectProvider;

class ExplanationPanelTest {

    @Test
    void rendersCAsAConceptFirstImplementationStrategy() throws Exception {
        CSubjectProvider provider = new CSubjectProvider();
        AtomicReference<String> html = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            ExplanationPanel panel = new ExplanationPanel();
            panel.showDataStructure(
                provider,
                provider.getRepresentation(StructureId.HASH_MAP).orElseThrow()
            );
            html.set(panel.displayedHtml());
        });

        assertTrue(html.get().contains("Hash map"));
        assertTrue(html.get().contains("Concept"));
        assertTrue(html.get().contains("C representation"));
        assertTrue(html.get().contains("Hash table"));
        assertTrue(html.get().contains("implementation strategy"));
        assertTrue(html.get().contains("standardized library type"));
        assertFalse(html.get().contains("HashMap"));
        assertFalse(html.get().contains("HASH_MAP"));
        assertFalse(html.get().contains("/content/"));
    }

    @Test
    void preservesTheExistingJavaExplanationResource() throws Exception {
        JavaSubjectProvider provider = new JavaSubjectProvider();
        AtomicReference<String> html = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            ExplanationPanel panel = new ExplanationPanel();
            panel.showDataStructure(
                provider,
                provider.getRepresentation(StructureId.DYNAMIC_ARRAY)
                    .orElseThrow()
            );
            html.set(panel.displayedHtml());
        });

        assertTrue(html.get().contains("ArrayList"));
        assertTrue(html.get().contains("API Overview"));
        assertTrue(html.get().contains("numbers.add"));
    }
}
