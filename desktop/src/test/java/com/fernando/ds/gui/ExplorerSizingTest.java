package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.model.DataStructure;

class ExplorerSizingTest {

    @Test
    void longestRenderedNameInfluencesWidthWithinMinimumAndMaximum()
        throws Exception {
        AtomicReference<Integer> minimum = new AtomicReference<>();
        AtomicReference<Integer> measured = new AtomicReference<>();
        AtomicReference<Integer> maximum = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            DSListPanel panel = new DSListPanel(4, 10, 10);
            panel.updateList(List.of(named("x")), null);
            minimum.set(panel.preferredContentWidth());

            panel.updateList(List.of(named(
                "Synthetic provider representation with a longer name"
            )), null);
            measured.set(panel.preferredContentWidth());

            panel.updateList(List.of(named("x".repeat(200))), null);
            maximum.set(panel.preferredContentWidth());
        });

        assertEquals(DSListPanel.MIN_CONTENT_WIDTH, minimum.get());
        assertTrue(measured.get() > minimum.get());
        assertEquals(DSListPanel.MAX_CONTENT_WIDTH, maximum.get());
    }

    @Test
    void refreshedContentWidthMovesOnlyAnUntouchedDivider()
        throws Exception {
        AtomicReference<ContentAwareSplitPane> splitReference =
            new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            ContentAwareSplitPane split = new ContentAwareSplitPane(
                new JPanel(),
                new JPanel()
            );
            split.refreshPreferredLeftWidth(230);
            assertEquals(230, split.getDividerLocation());

            split.refreshPreferredLeftWidth(290);
            assertEquals(290, split.getDividerLocation());

            split.setDividerLocation(255);
            split.refreshPreferredLeftWidth(320);
            splitReference.set(split);
        });

        assertEquals(255, splitReference.get().getDividerLocation());
        assertEquals(
            320,
            splitReference.get().getLeftComponent()
                .getPreferredSize().width
        );
    }

    private static DataStructure named(String displayName) {
        return new DataStructure(
            StructureId.DYNAMIC_ARRAY,
            displayName,
            false
        ) {
        };
    }
}
