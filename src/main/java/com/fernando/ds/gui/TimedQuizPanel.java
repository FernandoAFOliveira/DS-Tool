package com.fernando.ds.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntConsumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.fernando.ds.application.quiz.QuizAnswerOption;
import com.fernando.ds.application.quiz.QuizQuestion;
import com.fernando.ds.application.quiz.QuizSession.AnswerReview;
import com.fernando.ds.application.quiz.QuizSummary;

/** Swing presentation for the single subject-bound Timed Quiz mode. */
final class TimedQuizPanel extends JPanel implements TimedQuizView {

    private final JLabel subject =
        new JLabel("", SwingConstants.CENTER);
    private final JLabel progress =
        new JLabel("", SwingConstants.LEFT);
    private final JLabel elapsed =
        new JLabel("Elapsed: 0:00", SwingConstants.RIGHT);
    private final JTextArea question = textArea(20, Font.BOLD);
    private final JPanel answerChoices = new JPanel();
    private final JTextArea feedback = textArea(16, Font.PLAIN);
    private final JButton start = new JButton("Start Quiz");
    private final JButton submit = new JButton("Submit");
    private final JButton next = new JButton("Next");
    private final JButton startNew = new JButton("Start New Quiz");
    private final JPanel controls =
        new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
    private final List<JRadioButton> optionButtons = new ArrayList<>();
    private final ButtonGroup answerGroup = new ButtonGroup();
    private final Timer elapsedTimer;
    private IntConsumer submitListener = ignored -> {
    };
    private Runnable startListener = () -> {
    };
    private Runnable nextListener = () -> {
    };
    private Runnable startNewListener = () -> {
    };
    private Runnable timerTickListener = () -> {
    };
    private Theme currentTheme = Theme.LIGHT;

    TimedQuizPanel() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 42, 24, 42));

        JLabel title = new JLabel("Timed Quiz", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        subject.setFont(subject.getFont().deriveFont(Font.BOLD));

        JPanel heading = new JPanel(new BorderLayout(8, 8));
        heading.add(title, BorderLayout.NORTH);
        heading.add(subject, BorderLayout.CENTER);
        JPanel status = new JPanel(new BorderLayout());
        status.add(progress, BorderLayout.WEST);
        status.add(elapsed, BorderLayout.EAST);
        heading.add(status, BorderLayout.SOUTH);
        add(heading, BorderLayout.NORTH);

        answerChoices.setLayout(
            new BoxLayout(answerChoices, BoxLayout.Y_AXIS)
        );
        JPanel content = new WrappingContentPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        question.setAlignmentX(Component.LEFT_ALIGNMENT);
        answerChoices.setAlignmentX(Component.LEFT_ALIGNMENT);
        feedback.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(question);
        content.add(Box.createVerticalStrut(14));
        content.add(answerChoices);
        content.add(Box.createVerticalStrut(14));
        content.add(feedback);

        JScrollPane scrolling = new JScrollPane(content);
        scrolling.setBorder(BorderFactory.createEmptyBorder());
        scrolling.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrolling.getVerticalScrollBar().setUnitIncrement(16);
        add(scrolling, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        start.addActionListener(event -> startListener.run());
        submit.addActionListener(event -> {
            int selected = selectedAnswerIndex();
            if (selected >= 0) {
                submitListener.accept(selected);
            }
        });
        next.addActionListener(event -> nextListener.run());
        startNew.addActionListener(event -> startNewListener.run());

        elapsedTimer = new Timer(
            250,
            event -> timerTickListener.run()
        );
        elapsedTimer.setCoalesce(true);

        getAccessibleContext().setAccessibleDescription(
            "Ten-question subject-bound data-structure quiz"
        );
        showOnly(start);
    }

    void setStartListener(Runnable listener) {
        startListener = Objects.requireNonNull(listener, "listener");
    }

    void setSubmitListener(IntConsumer listener) {
        submitListener = Objects.requireNonNull(listener, "listener");
    }

    void setNextListener(Runnable listener) {
        nextListener = Objects.requireNonNull(listener, "listener");
    }

    void setStartNewListener(Runnable listener) {
        startNewListener = Objects.requireNonNull(listener, "listener");
    }

    void setTimerTickListener(Runnable listener) {
        timerTickListener = Objects.requireNonNull(listener, "listener");
    }

    @Override
    public void showNotStarted(
        String subjectDisplayName,
        int questionCount
    ) {
        stopTimerUpdates();
        subject.setText("Quiz subject when started: " + subjectDisplayName);
        progress.setText(questionCount + " questions");
        elapsed.setText("Elapsed: 0:00");
        question.setText(
            "Test your understanding of data-structure concepts and their "
                + subjectDisplayName + " representations. The subject is "
                + "captured when you start and remains fixed for the session."
        );
        clearAnswers();
        feedback.setText(
            "One point per correct answer. Review feedback, then choose Next."
        );
        showOnly(start);
        focus(start);
    }

    @Override
    public void showQuestion(
        String subjectDisplayName,
        int position,
        int questionCount,
        Duration elapsedTime,
        QuizQuestion quizQuestion
    ) {
        subject.setText("Quiz subject: " + subjectDisplayName);
        progress.setText(
            "Question " + position + " of " + questionCount
        );
        updateElapsedTime(elapsedTime);
        question.setText(quizQuestion.prompt());
        populateAnswers(quizQuestion.options());
        feedback.setText(
            "Choose one answer. Selecting an option does not submit it."
        );
        submit.setEnabled(false);
        showOnly(submit);
        startTimerUpdates();
        focusFirstAnswer();
    }

    @Override
    public void showAnswerReview(
        String subjectDisplayName,
        int position,
        int questionCount,
        Duration elapsedTime,
        AnswerReview review,
        boolean finalQuestion
    ) {
        subject.setText("Quiz subject: " + subjectDisplayName);
        progress.setText(
            "Question " + position + " of " + questionCount
        );
        updateElapsedTime(elapsedTime);
        question.setText(review.question().prompt());
        populateAnswers(review.question().options());
        optionButtons.get(review.selectedOptionIndex()).setSelected(true);
        optionButtons.forEach(button -> button.setEnabled(false));
        QuizAnswerOption correct = review.question().options().get(
            review.question().correctOptionIndex()
        );
        feedback.setText(
            (review.correct() ? "Correct." : "Incorrect.")
                + "\nCorrect answer: " + correct.text()
                + "\n" + review.question().explanation()
                + "\nChoose Next to continue."
        );
        next.setText(finalQuestion ? "View Results" : "Next");
        showOnly(next);
        if (finalQuestion) {
            stopTimerUpdates();
        } else {
            startTimerUpdates();
        }
        focus(next);
    }

    @Override
    public void showCompleted(
        String subjectDisplayName,
        QuizSummary summary
    ) {
        stopTimerUpdates();
        subject.setText("Quiz subject: " + subjectDisplayName);
        progress.setText("Completed");
        updateElapsedTime(summary.elapsed());
        question.setText(
            "Final score: " + summary.correctCount()
                + " of " + summary.questionCount()
                + " (" + summary.percentage() + "%)"
        );
        clearAnswers();
        feedback.setText(
            "Correct: " + summary.correctCount()
                + "\nIncorrect: " + summary.incorrectCount()
                + "\nElapsed time: " + formatDuration(summary.elapsed())
        );
        showOnly(startNew);
        focus(startNew);
    }

    @Override
    public void updateElapsedTime(Duration elapsedTime) {
        elapsed.setText("Elapsed: " + formatDuration(elapsedTime));
    }

    @Override
    public void stopTimerUpdates() {
        elapsedTimer.stop();
    }

    @Override
    public void applyTheme(Theme theme) {
        currentTheme = Objects.requireNonNull(theme, "theme");
        ThemeManager.applyThemeToComponent(this, currentTheme);
        repaint();
    }

    int selectedAnswerIndex() {
        for (int index = 0; index < optionButtons.size(); index++) {
            if (optionButtons.get(index).isSelected()) {
                return index;
            }
        }
        return -1;
    }

    String subjectText() {
        return subject.getText();
    }

    String progressText() {
        return progress.getText();
    }

    String elapsedText() {
        return elapsed.getText();
    }

    String questionText() {
        return question.getText();
    }

    String feedbackText() {
        return feedback.getText();
    }

    int answerCount() {
        return optionButtons.size();
    }

    boolean isSubmitEnabled() {
        return submit.isEnabled();
    }

    boolean isNextVisible() {
        return next.isVisible();
    }

    boolean isStartNewVisible() {
        return startNew.isVisible();
    }

    void selectAnswer(int index) {
        optionButtons.get(index).doClick();
    }

    void clickStart() {
        start.doClick();
    }

    void clickSubmit() {
        submit.doClick();
    }

    void clickNext() {
        next.doClick();
    }

    void clickStartNew() {
        startNew.doClick();
    }

    List<JRadioButton> answerButtons() {
        return List.copyOf(optionButtons);
    }

    private void populateAnswers(List<QuizAnswerOption> options) {
        clearAnswers();
        for (int index = 0; index < options.size(); index++) {
            JRadioButton button = new JRadioButton(
                options.get(index).text()
            );
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height)
            );
            button.getAccessibleContext().setAccessibleName(
                "Answer " + (index + 1) + ": " + options.get(index).text()
            );
            button.addActionListener(event -> submit.setEnabled(true));
            answerGroup.add(button);
            optionButtons.add(button);
            answerChoices.add(button);
            answerChoices.add(Box.createVerticalStrut(8));
        }
        answerChoices.revalidate();
        answerChoices.repaint();
    }

    private void clearAnswers() {
        answerGroup.clearSelection();
        for (JRadioButton button : optionButtons) {
            answerGroup.remove(button);
        }
        optionButtons.clear();
        answerChoices.removeAll();
        answerChoices.revalidate();
        answerChoices.repaint();
    }

    private void showOnly(JButton requested) {
        controls.removeAll();
        controls.add(requested);
        controls.revalidate();
        controls.repaint();
        start.setVisible(requested == start);
        submit.setVisible(requested == submit);
        next.setVisible(requested == next);
        startNew.setVisible(requested == startNew);
        setDefaultButton(requested);
    }

    private void startTimerUpdates() {
        if (!elapsedTimer.isRunning()) {
            elapsedTimer.start();
        }
    }

    private void focusFirstAnswer() {
        if (!optionButtons.isEmpty()) {
            focus(optionButtons.getFirst());
        }
    }

    private void setDefaultButton(JButton button) {
        SwingUtilities.invokeLater(() -> {
            if (getRootPane() != null) {
                getRootPane().setDefaultButton(button);
            }
        });
    }

    private static void focus(Component component) {
        SwingUtilities.invokeLater(component::requestFocusInWindow);
    }

    private static JTextArea textArea(int fontSize, int style) {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setFocusable(false);
        area.setFont(new Font("SansSerif", style, fontSize));
        area.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        return area;
    }

    static String formatDuration(Duration duration) {
        long seconds = Math.max(0, duration.toSeconds());
        return "%d:%02d".formatted(seconds / 60, seconds % 60);
    }

    private static final class WrappingContentPanel
        extends JPanel implements Scrollable {

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(
            Rectangle visibleRect,
            int orientation,
            int direction
        ) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(
            Rectangle visibleRect,
            int orientation,
            int direction
        ) {
            return Math.max(16, visibleRect.height - 16);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}
