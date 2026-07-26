package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ExplorerContent;
import com.fernando.ds.application.ExplorerService;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.subject.CSubjectProvider;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProvider;
import com.fernando.ds.subject.SubjectProviderRegistry;

class ExplorerDetailsPanelTest {

    @Test
    void rendersEveryAvailableLanguageNeutralOverviewSection()
        throws Exception {
        ExplorerContent content = content(StructureId.HASH_MAP);
        AtomicReference<String> html = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            ExplorerDetailsPanel panel = new ExplorerDetailsPanel();
            panel.applyTheme(Theme.DARK);
            panel.showContent(content);
            html.set(panel.displayedHtml());
        });

        String displayed = html.get();
        assertTrue(displayed.contains("Definition"));
        assertTrue(displayed.contains(
            "Language-neutral concept overview"
        ));
        assertTrue(displayed.contains("Characteristics"));
        assertTrue(displayed.contains("Key-value mapping"));
        assertTrue(displayed.contains("Strengths"));
        assertTrue(displayed.contains("Weaknesses"));
        assertTrue(displayed.contains("Supported operations"));
        assertTrue(displayed.contains("Complexity"));
        assertTrue(displayed.contains("Memory and iteration"));
        assertTrue(displayed.contains("Common use cases"));
        assertTrue(displayed.contains("Related structures"));
        assertTrue(displayed.contains("Ordered map"));
        assertFalse(displayed.contains("Java"));
        assertFalse(displayed.contains("HashMap"));
        assertFalse(displayed.contains("HASH_MAP"));
        assertFalse(displayed.contains("/content/"));
    }

    @Test
    void rendersExactDynamicTabOrderAndPreservesSelection() throws Exception {
        AtomicReference<List<String>> titles = new AtomicReference<>();
        AtomicReference<String> selectedAfterRefresh = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            ExplorerTabbedDetailsPanel panel =
                new ExplorerTabbedDetailsPanel();
            panel.showSubjects(subjects());
            assertEquals("Overview", panel.selectedTitle());

            panel.showContent(content(StructureId.HASH_MAP));
            panel.selectSubject(SubjectId.C);
            panel.showContent(content(StructureId.QUEUE));
            panel.applyTheme(Theme.DARK_BLUE);
            panel.showSubjects(subjects());
            panel.showContent(content(StructureId.QUEUE));

            titles.set(panel.tabTitles());
            selectedAfterRefresh.set(panel.selectedTitle());
        });

        assertEquals(List.of("Overview", "Java", "C"), titles.get());
        assertEquals("C", selectedAfterRefresh.get());
    }

    @Test
    void syntheticEnabledSubjectAddsATabAndDisappearanceFallsBack()
        throws Exception {
        AtomicReference<List<String>> titles = new AtomicReference<>();
        AtomicReference<String> fallback = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            ExplorerTabbedDetailsPanel panel =
                new ExplorerTabbedDetailsPanel();
            SubjectProviderRegistry registry = new SubjectProviderRegistry(
                List.of(
                    new JavaSubjectProvider(),
                    new CSubjectProvider(),
                    syntheticProvider()
                )
            );
            panel.showSubjects(new ExplorerService().getSubjects(
                registry.getEnabled()
            ));
            titles.set(panel.tabTitles());
            panel.selectSubject(SubjectId.PYTHON);
            panel.showSubjects(subjects());
            fallback.set(panel.selectedTitle());
        });

        assertEquals(
            List.of("Overview", "Java", "C", "Synthetic"),
            titles.get()
        );
        assertEquals("Overview", fallback.get());
    }

    @Test
    void sharedDiagramLivesOutsideAllDetailTabs() {
        long explorerDiagrams = java.util.Arrays.stream(
            ExplorerPanel.class.getDeclaredFields()
        ).filter(field -> field.getType() == DiagramPanel.class).count();
        long tabbedDiagrams = java.util.Arrays.stream(
            ExplorerTabbedDetailsPanel.class.getDeclaredFields()
        ).filter(field -> field.getType() == DiagramPanel.class).count();

        assertEquals(1, explorerDiagrams);
        assertEquals(0, tabbedDiagrams);
    }

    @Test
    void welcomeRendersAllCurrentTabs() throws Exception {
        AtomicReference<String> overview = new AtomicReference<>();
        AtomicReference<String> java = new AtomicReference<>();
        AtomicReference<String> c = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            ExplorerTabbedDetailsPanel panel =
                new ExplorerTabbedDetailsPanel();
            panel.showSubjects(subjects());
            panel.showWelcome();
            overview.set(panel.overviewHtml());
            java.set(panel.subjectHtml(SubjectId.JAVA));
            c.set(panel.subjectHtml(SubjectId.C));
        });

        assertTrue(overview.get().contains("Select a structure"));
        assertTrue(overview.get().contains(
            "Language-neutral concept overview"
        ));
        assertTrue(java.get().contains("Select a structure"));
        assertTrue(c.get().contains("Select a structure"));
    }

    @Test
    void rendersUsefulJavaAndCProviderGuidanceAndCode() throws Exception {
        AtomicReference<String> java = new AtomicReference<>();
        AtomicReference<String> c = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            ExplorerTabbedDetailsPanel panel =
                new ExplorerTabbedDetailsPanel();
            panel.showSubjects(subjects());
            panel.showContent(content(StructureId.DYNAMIC_ARRAY));
            java.set(panel.subjectHtml(SubjectId.JAVA));
            c.set(panel.subjectHtml(SubjectId.C));
        });

        assertTrue(java.get().contains("ArrayList"));
        assertTrue(java.get().contains("Common API"));
        assertTrue(java.get().contains("values.add"));
        assertTrue(c.get().contains("C implementation strategy"));
        assertTrue(c.get().contains("realloc"));
        assertTrue(c.get().contains("typedef struct"));
        assertFalse(c.get().contains("ArrayList"));
    }

    @Test
    void safelyEscapesProviderTextAndCode() {
        com.fernando.ds.subject.SubjectStructureContent unsafe =
            new com.fernando.ds.subject.SubjectStructureContent(
                List.of(new com.fernando.ds.subject
                    .SubjectStructureContent.Section(
                        "<Section>",
                        List.of("Use <unsafe> & \"quoted\" text."),
                        List.of("'bullet'")
                    )),
                List.of(new com.fernando.ds.subject
                    .SubjectStructureContent.CodeExample(
                        "<Example>",
                        "if (left < right && value > 0) { \"ok\"; }"
                    ))
            );
        ExplorerContent.SubjectContent subject =
            new ExplorerContent.SubjectContent(
                SubjectId.JAVA,
                "<Java>",
                java.util.Optional.empty(),
                java.util.Optional.of(unsafe)
            );

        String html = SubjectContentHtmlRenderer.render(subject);

        assertTrue(html.contains("&lt;Java&gt;"));
        assertTrue(html.contains("&lt;Section&gt;"));
        assertTrue(html.contains("&lt;unsafe&gt; &amp; &quot;quoted&quot;"));
        assertTrue(html.contains("left &lt; right &amp;&amp; value &gt; 0"));
        assertFalse(html.contains("<unsafe>"));
    }

    private static ExplorerContent content(StructureId id) {
        return new ExplorerService().getContent(
            List.of(new JavaSubjectProvider(), new CSubjectProvider()),
            id
        );
    }

    private static List<com.fernando.ds.application.ExplorerSubject>
        subjects() {
        return new ExplorerService().getSubjects(
            List.of(new JavaSubjectProvider(), new CSubjectProvider())
        );
    }

    private static SubjectProvider syntheticProvider() {
        return new SubjectProvider() {
            @Override
            public SubjectId id() {
                return SubjectId.PYTHON;
            }

            @Override
            public String displayName() {
                return "Synthetic";
            }

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public List<com.fernando.ds.model.DataStructure>
                getDataStructures() {
                return List.of();
            }
        };
    }
}
