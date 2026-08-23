package com.fernando.ds.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.fernando.ds.application.FlashCardContent;
import com.fernando.ds.util.ContentLoader;

/** Swing presentation for the concept-first Flash Cards activity. */
final class FlashCardPanel extends JPanel implements FlashCardView {

    private final JEditorPane card = new JEditorPane();
    private final JLabel progress = new JLabel("", SwingConstants.CENTER);
    private final JButton previous = new JButton("Previous");
    private final JButton reveal = new JButton("Reveal answer");
    private final JButton next = new JButton("Next");
    private Theme currentTheme = Theme.LIGHT;

    FlashCardPanel() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(18, 48, 28, 48));

        JLabel title = new JLabel("Flash Cards", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        card.setContentType("text/html");
        card.setEditable(false);
        add(new JScrollPane(card), BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.add(progress, BorderLayout.NORTH);
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        controls.add(previous);
        controls.add(reveal);
        controls.add(next);
        footer.add(controls, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    void setPreviousListener(Runnable listener) {
        previous.addActionListener(event -> listener.run());
    }

    void setRevealListener(Runnable listener) {
        reveal.addActionListener(event -> listener.run());
    }

    void setNextListener(Runnable listener) {
        next.addActionListener(event -> listener.run());
    }

    @Override
    public void showCard(
        FlashCardContent content,
        int position,
        int total,
        int reviewed,
        boolean answerRevealed,
        boolean hasPrevious,
        boolean hasNext
    ) {
        String answer = answerRevealed
            ? """
                <hr>
                <h2>Concept</h2>
                <p>%s</p>
                %s
                """.formatted(
                    escape(content.question().answer()),
                    supplementaryRepresentation(content)
                )
            : """
                <p><em>Think about the concept, then reveal the answer.</em></p>
                """;

        String body = """
            <div style="text-align: left; margin: 12px;">
            <h1 style="text-align: left;">%s</h1>
            %s
            </div>
            """.formatted(escape(content.question().prompt()), answer);
        card.setText(ContentLoader.applyThemeToHtml(body, currentTheme));
        card.setCaretPosition(0);

        progress.setText(
            "Card " + position + " of " + total
                + " \u2022 " + reviewed + " reviewed"
        );
        previous.setEnabled(hasPrevious);
        next.setEnabled(hasNext);
        reveal.setEnabled(!answerRevealed);
    }

    @Override
    public void applyTheme(Theme theme) {
        currentTheme = theme;
    }

    @Override
    public void showSubjectContext(String subjectDisplayName) {
        // Global language context is shown in the workspace header badge.
    }

    String displayedHtml() {
        return card.getText();
    }

    String progressText() {
        return progress.getText();
    }

    boolean isPreviousEnabled() {
        return previous.isEnabled();
    }

    boolean isRevealEnabled() {
        return reveal.isEnabled();
    }

    boolean isNextEnabled() {
        return next.isEnabled();
    }

    private static String supplementaryRepresentation(
        FlashCardContent content
    ) {
        return content.subjectRepresentation()
            .map(representation -> """
                <p><strong>%s representation:</strong> %s</p>
                """.formatted(
                    escape(representation.subjectDisplayName()),
                    escape(representation.representationDisplayName())
                ))
            .orElse("");
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
