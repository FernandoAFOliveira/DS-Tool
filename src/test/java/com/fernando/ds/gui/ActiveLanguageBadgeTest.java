package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JMenu;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.subject.CSubjectProvider;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProviderRegistry;

class ActiveLanguageBadgeTest {

    @Test
    void showsReadableLanguageNameAndAccessibleContext() {
        ActiveLanguageBadge badge = new ActiveLanguageBadge();

        badge.showSubject(SubjectId.JAVA, "Java");

        assertEquals("Java", badge.displayedLanguageName());
        assertEquals(
            "Active programming language: Java",
            badge.getToolTipText()
        );
        assertEquals(
            "Active programming language: Java",
            badge.getAccessibleContext().getAccessibleName()
        );
        assertNotNull(badge.displayedLanguageIcon());
        assertTrue(badge.displayedIconSize() >= 18);
        assertTrue(badge.displayedIconSize() <= 22);
    }

    @Test
    void rendersJavaAndCIconsDistinctly() {
        ActiveLanguageBadge badge = new ActiveLanguageBadge();
        badge.showSubject(SubjectId.JAVA, "Java");
        long javaHash = iconHash(badge.displayedLanguageIcon());

        badge.showSubject(SubjectId.C, "C");
        long cHash = iconHash(badge.displayedLanguageIcon());

        assertEquals("C", badge.displayedLanguageName());
        assertNotEquals(javaHash, cHash);
    }

    @Test
    void usesGenericFallbackForProvidersWithoutDedicatedIcons() {
        ActiveLanguageBadge badge = new ActiveLanguageBadge();
        badge.showSubject(SubjectId.PYTHON, "Python");
        long pythonHash = iconHash(badge.displayedLanguageIcon());

        badge.showSubject(SubjectId.CPP, "C++");
        long cppHash = iconHash(badge.displayedLanguageIcon());
        assertEquals("C++", badge.displayedLanguageName());

        badge.showSubject(SubjectId.C, "C");
        long cHash = iconHash(badge.displayedLanguageIcon());

        assertEquals(pythonHash, cppHash);
        assertNotEquals(cHash, cppHash);
    }

    @Test
    void isNotTreatedAsMenuAndThemeRefreshPreservesBadgeContents() {
        ActiveLanguageBadge badge = new ActiveLanguageBadge();
        badge.showSubject(SubjectId.JAVA, "Java");
        Object icon = badge.displayedLanguageIcon();

        ThemeManager.applyThemeToComponent(badge, Theme.DARK_BLUE);

        assertFalse(JMenu.class.isAssignableFrom(badge.getClass()));
        assertEquals("Java", badge.displayedLanguageName());
        assertNotNull(badge.displayedLanguageIcon());
        assertEquals(icon, badge.displayedLanguageIcon());
    }

    @Test
    void failedSubjectSwitchPreservesPreviousBadgeState() {
        ApplicationState state = new ApplicationState();
        SubjectProviderRegistry registry = new SubjectProviderRegistry(List.of(
            new JavaSubjectProvider(),
            new CSubjectProvider()
        ));
        SubjectSelectionController controller =
            new SubjectSelectionController(state, registry);
        ActiveLanguageBadge badge = new ActiveLanguageBadge();
        badge.showSubject(
            state.getActiveSubject(),
            registry.get(state.getActiveSubject()).displayName()
        );
        Object previousIcon = badge.displayedLanguageIcon();

        assertThrows(
            IllegalStateException.class,
            () -> controller.select(SubjectId.C, provider -> {
                throw new IllegalStateException("render failed");
            })
        );

        assertEquals("Java", badge.displayedLanguageName());
        assertEquals(previousIcon, badge.displayedLanguageIcon());
    }

    private static long iconHash(Icon icon) {
        BufferedImage image = new BufferedImage(
            icon.getIconWidth(),
            icon.getIconHeight(),
            BufferedImage.TYPE_INT_ARGB
        );
        java.awt.Graphics2D g = image.createGraphics();
        try {
            JLabel label = new JLabel();
            label.setForeground(Color.BLACK);
            icon.paintIcon(label, g, 0, 0);
        } finally {
            g.dispose();
        }
        long hash = 1;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                hash = 31 * hash + image.getRGB(x, y);
            }
        }
        return hash;
    }
}
