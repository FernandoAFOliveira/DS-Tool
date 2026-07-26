package com.fernando.ds.gui;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fernando.ds.subject.SubjectId;

final class ActiveLanguageBadge extends JPanel {

    private static final int ICON_SIZE = 20;
    private static final String ACCESSIBLE_PREFIX =
        "Active programming language: ";

    private final JLabel iconLabel = new JLabel();
    private final JLabel languageNameLabel = new JLabel();

    ActiveLanguageBadge() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
        setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 12));
        setFocusable(false);
        setOpaque(false);
        Font currentFont = languageNameLabel.getFont();
        languageNameLabel.setFont(
            currentFont.deriveFont(
                Font.BOLD,
                Math.max(13f, currentFont.getSize2D() + 1f)
            )
        );
        add(iconLabel);
        add(languageNameLabel);
    }

    void showSubject(SubjectId subjectId, String subjectDisplayName) {
        SubjectId id = Objects.requireNonNull(subjectId, "subjectId");
        String displayName = Objects.requireNonNull(
            subjectDisplayName,
            "subjectDisplayName"
        );
        iconLabel.setIcon(new SubjectGlyphIcon(glyphFor(id), ICON_SIZE));
        iconLabel.setText("");
        languageNameLabel.setText(displayName);
        setToolTipText(ACCESSIBLE_PREFIX + displayName);
        getAccessibleContext().setAccessibleName(ACCESSIBLE_PREFIX + displayName);
    }

    String displayedLanguageName() {
        return languageNameLabel.getText();
    }

    Icon displayedLanguageIcon() {
        return iconLabel.getIcon();
    }

    int displayedIconSize() {
        Icon icon = displayedLanguageIcon();
        return icon == null ? 0 : icon.getIconWidth();
    }

    private static GlyphKind glyphFor(SubjectId subjectId) {
        return switch (subjectId) {
            case JAVA -> GlyphKind.JAVA_CUP;
            case C -> GlyphKind.C_BADGE;
            case CPP, PYTHON -> GlyphKind.GENERIC_CODE;
        };
    }

    private enum GlyphKind {
        JAVA_CUP,
        C_BADGE,
        GENERIC_CODE
    }

    private static final class SubjectGlyphIcon implements Icon {

        private final GlyphKind glyphKind;
        private final int size;

        SubjectGlyphIcon(GlyphKind glyphKind, int size) {
            this.glyphKind = glyphKind;
            this.size = size;
        }

        @Override
        public void paintIcon(Component c, Graphics graphics, int x, int y) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            try {
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setColor(c.getForeground());
                switch (glyphKind) {
                    case JAVA_CUP -> paintJavaIcon(g2, x, y, size);
                    case C_BADGE -> paintCIcon(g2, x, y, size);
                    case GENERIC_CODE -> paintGenericCodeIcon(g2, x, y, size);
                }
            } finally {
                g2.dispose();
            }
        }

        private static void paintJavaIcon(Graphics2D g2, int x, int y, int size) {
            int steamStroke = Math.max(1, size / 12);
            g2.setStroke(new BasicStroke(steamStroke));
            g2.drawArc(x + size / 4, y + 1, size / 7, size / 3, 20, 150);
            g2.drawArc(x + size / 2, y + 1, size / 7, size / 3, 20, 150);

            int saucerY = y + size - size / 4;
            g2.drawLine(x + size / 6, saucerY, x + size - size / 4, saucerY);

            int cupX = x + size / 6;
            int cupY = y + size / 2;
            int cupWidth = size - size / 2;
            int cupHeight = size / 4;
            g2.drawRoundRect(cupX, cupY, cupWidth, cupHeight, 4, 4);
            g2.drawOval(
                cupX + cupWidth - 1,
                cupY + size / 12,
                size / 4,
                size / 5
            );
        }

        private static void paintCIcon(
            Graphics2D g2,
            int x,
            int y,
            int size
        ) {
            g2.setStroke(new BasicStroke(Math.max(1f, size / 10f)));
            g2.drawOval(x + 2, y + 2, size - 5, size - 5);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, size * 0.63f));
            g2.drawString("C", x + size / 4, y + size - size / 4);
        }

        private static void paintGenericCodeIcon(
            Graphics2D g2,
            int x,
            int y,
            int size
        ) {
            g2.setStroke(new BasicStroke(Math.max(1f, size / 12f)));
            g2.drawRoundRect(x + 2, y + 2, size - 4, size - 4, 4, 4);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, size * 0.35f));
            g2.drawString("</>", x + size / 6, y + size - size / 3);
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
}
