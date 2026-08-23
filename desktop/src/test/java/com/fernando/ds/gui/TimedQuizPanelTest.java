package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.AbstractButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.application.quiz.QuizQuestionSource;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectProviderRegistry;

class TimedQuizPanelTest {

    @Test
    void supportsCompleteMouseAndKeyboardAccessibleControlFlow()
        throws Exception {
        AtomicReference<TimedQuizPanel> panelRef = new AtomicReference<>();
        AtomicReference<TimedQuizController> controllerRef =
            new AtomicReference<>();
        AtomicReference<JRootPane> rootRef = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            TimedQuizPanel panel = new TimedQuizPanel();
            TimedQuizController controller = new TimedQuizController(
                new ApplicationState(),
                new SubjectProviderRegistry(
                    List.of(new JavaSubjectProvider())
                ),
                new QuizQuestionSource(new Random(42)),
                () -> 0,
                panel
            );
            panel.setStartListener(controller::startQuiz);
            panel.setSubmitListener(controller::submitAnswer);
            panel.setNextListener(controller::showNext);
            panel.setStartNewListener(controller::startNewQuiz);
            panel.setTimerTickListener(controller::refreshElapsedTime);
            JRootPane root = new JRootPane();
            root.setContentPane(panel);
            panel.applyTheme(Theme.DARK);
            controller.activate();
            panelRef.set(panel);
            controllerRef.set(controller);
            rootRef.set(root);
        });
        flushEdt();

        TimedQuizPanel panel = panelRef.get();
        TimedQuizController controller = controllerRef.get();
        assertTrue(panel.subjectText().contains(
            "Quiz subject when started: Java"
        ));
        assertTrue(panel.progressText().contains("10 questions"));
        assertTrue(panel.questionText().contains(
            "captured when you start"
        ));

        SwingUtilities.invokeAndWait(panel::clickStart);
        flushEdt();

        assertEquals(4, panel.answerCount());
        assertTrue(panel.subjectText().contains("Quiz subject: Java"));
        assertTrue(panel.progressText().contains("Question 1 of 10"));
        assertFalse(panel.isSubmitEnabled());
        JScrollPane scrolling = (JScrollPane) java.util.Arrays.stream(
            panel.getComponents()
        )
            .filter(JScrollPane.class::isInstance)
            .findFirst()
            .orElseThrow();
        assertEquals(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER,
            scrolling.getHorizontalScrollBarPolicy()
        );
        assertTrue(
            ((Scrollable) scrolling.getViewport().getView())
                .getScrollableTracksViewportWidth()
        );
        for (AbstractButton answer : panel.answerButtons()) {
            assertTrue(answer.isFocusable());
            assertFalse(
                answer.getAccessibleContext()
                    .getAccessibleName()
                    .isBlank()
            );
            assertNotNull(answer.getInputMap().get(
                KeyStroke.getKeyStroke("SPACE")
            ));
        }

        int correct = controller.session()
            .currentQuestion()
            .correctOptionIndex();
        SwingUtilities.invokeAndWait(() -> panel.selectAnswer(correct));
        assertTrue(panel.isSubmitEnabled());
        assertEquals(
            com.fernando.ds.application.quiz.QuizSession.Lifecycle
                .QUESTION_ACTIVE,
            controller.session().lifecycle()
        );

        SwingUtilities.invokeAndWait(panel::clickSubmit);
        flushEdt();
        assertTrue(panel.feedbackText().startsWith("Correct."));
        assertTrue(panel.feedbackText().contains("Correct answer:"));
        assertTrue(panel.isNextVisible());
        assertEquals(
            com.fernando.ds.application.quiz.QuizSession.Lifecycle
                .ANSWER_REVIEWED,
            controller.session().lifecycle()
        );
        assertNotNull(rootRef.get().getDefaultButton());

        SwingUtilities.invokeAndWait(panel::clickNext);
        assertTrue(panel.progressText().contains("Question 2 of 10"));
        panel.stopTimerUpdates();
    }

    @Test
    void formatsElapsedTimeAndCompletedSummaryReadably()
        throws Exception {
        AtomicReference<TimedQuizPanel> panelRef = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            TimedQuizPanel panel = new TimedQuizPanel();
            panel.showCompleted(
                "C",
                new com.fernando.ds.application.quiz.QuizSummary(
                    7,
                    3,
                    70,
                    Duration.ofSeconds(125)
                )
            );
            panelRef.set(panel);
        });

        TimedQuizPanel panel = panelRef.get();
        assertEquals("Elapsed: 2:05", panel.elapsedText());
        assertTrue(panel.questionText().contains("7 of 10 (70%)"));
        assertTrue(panel.feedbackText().contains("Incorrect: 3"));
        assertTrue(panel.isStartNewVisible());
        assertEquals(
            "2:05",
            TimedQuizPanel.formatDuration(Duration.ofSeconds(125))
        );
    }

    private static void flushEdt() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
        });
    }
}
