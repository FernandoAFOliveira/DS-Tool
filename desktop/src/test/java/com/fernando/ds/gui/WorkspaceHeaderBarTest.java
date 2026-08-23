package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.JMenu;

import org.junit.jupiter.api.Test;

class WorkspaceHeaderBarTest {

    @Test
    void keepsMenuOrderAndUsesTallerHeaderHeight() {
        ActiveLanguageBadge badge = new ActiveLanguageBadge();
        badge.showSubject(com.fernando.ds.subject.SubjectId.JAVA, "Java");
        WorkspaceHeaderBar header = new WorkspaceHeaderBar(badge);
        header.addPrimaryMenu(new JMenu("File"));
        header.addPrimaryMenu(new JMenu("Subject"));
        header.addPrimaryMenu(new JMenu("Experience"));
        header.addPrimaryMenu(new JMenu("View"));
        header.addPrimaryMenu(new JMenu("Help"));
        header.anchorActiveLanguageBadge();

        assertEquals("File", header.getMenu(0).getText());
        assertEquals("Subject", header.getMenu(1).getText());
        assertEquals("Experience", header.getMenu(2).getText());
        assertEquals("View", header.getMenu(3).getText());
        assertEquals("Help", header.getMenu(4).getText());
        assertTrue(header.getPreferredSize().height >= 34);
        assertTrue(header.getPreferredSize().height <= 48);
    }

    @Test
    void anchorsBadgeAtFarRightEdge() {
        ActiveLanguageBadge badge = new ActiveLanguageBadge();
        badge.showSubject(com.fernando.ds.subject.SubjectId.C, "C");
        WorkspaceHeaderBar header = new WorkspaceHeaderBar(badge);
        header.addPrimaryMenu(new JMenu("File"));
        header.addPrimaryMenu(new JMenu("Subject"));
        header.addPrimaryMenu(new JMenu("Experience"));
        header.addPrimaryMenu(new JMenu("View"));
        header.addPrimaryMenu(new JMenu("Help"));
        header.anchorActiveLanguageBadge();

        header.setSize(1100, header.getPreferredSize().height);
        header.doLayout();

        JMenu helpMenu = header.getMenu(4);
        int badgeRight = badge.getX() + badge.getWidth();
        assertTrue(badge.getX() > helpMenu.getX() + helpMenu.getWidth());
        assertTrue(header.getWidth() - badgeRight <= 24);
    }
}
