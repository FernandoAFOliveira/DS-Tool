package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.awt.Container;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.application.LearningQuestionSource;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectProviderRegistry;

class FlashCardPanelTest {

    @Test
    void rendersPromptThenConceptAnswerAndProgressHeadlessly()
        throws Exception {
        AtomicReference<FlashCardPanel> panelReference =
            new AtomicReference<>();
        AtomicReference<FlashCardController> controllerReference =
            new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            FlashCardPanel panel = new FlashCardPanel();
            FlashCardController controller = new FlashCardController(
                new ApplicationState(),
                new SubjectProviderRegistry(
                    List.of(new JavaSubjectProvider())
                ),
                new LearningQuestionSource(),
                panel
            );
            panel.applyTheme(Theme.DARK);
            controller.activate();
            panelReference.set(panel);
            controllerReference.set(controller);
        });

        FlashCardPanel panel = panelReference.get();
        assertTrue(panel.displayedHtml().contains("What is a Dynamic array?"));
        assertTrue(panel.displayedHtml().contains("Think about the concept"));
        assertFalse(panel.displayedHtml().contains("Fast positional access"));
        assertFalse(panel.displayedHtml().contains("ArrayList"));
        assertTrue(panel.progressText().contains("Card 1 of 9"));
        assertTrue(panel.progressText().contains("0 reviewed"));
        assertFalse(containsLabelText(panel, "Subject context:"));
        assertTrue(containsLabelText(panel, "Flash Cards"));
        assertFalse(panel.isPreviousEnabled());
        assertTrue(panel.isRevealEnabled());
        assertTrue(panel.isNextEnabled());

        SwingUtilities.invokeAndWait(
            controllerReference.get()::revealAnswer
        );

        assertTrue(panel.displayedHtml().contains("Concept"));
        assertTrue(panel.displayedHtml().contains("contiguous array"));
        assertTrue(panel.displayedHtml().contains("Java representation"));
        assertTrue(panel.displayedHtml().contains("ArrayList"));
        assertTrue(panel.progressText().contains("1 reviewed"));
        assertFalse(panel.isRevealEnabled());
        assertFalse(containsLabelText(panel, "Subject context:"));
    }

    private static boolean containsLabelText(Container root, String textPrefix) {
        for (Component component : root.getComponents()) {
            if (component instanceof JLabel label
                && label.getText() != null
                && label.getText().startsWith(textPrefix)) {
                return true;
            }
            if (component instanceof Container container
                && containsLabelText(container, textPrefix)) {
                return true;
            }
        }
        return false;
    }
}
