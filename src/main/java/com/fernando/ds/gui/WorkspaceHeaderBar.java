package com.fernando.ds.gui;

import java.awt.Dimension;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

final class WorkspaceHeaderBar extends JMenuBar {

    private static final int BASE_MINIMUM_HEIGHT = 34;
    private final ActiveLanguageBadge activeLanguageBadge;

    WorkspaceHeaderBar(ActiveLanguageBadge activeLanguageBadge) {
        this.activeLanguageBadge = Objects.requireNonNull(
            activeLanguageBadge,
            "activeLanguageBadge"
        );
        setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 8));
    }

    void addPrimaryMenu(JMenu menu) {
        add(Objects.requireNonNull(menu, "menu"));
    }

    void anchorActiveLanguageBadge() {
        add(Box.createHorizontalGlue());
        add(activeLanguageBadge);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferred = super.getPreferredSize();
        int textHeight = getFontMetrics(getFont()).getHeight();
        int targetHeight = Math.max(
            BASE_MINIMUM_HEIGHT,
            Math.max(
                textHeight + 14,
                activeLanguageBadge.getPreferredSize().height + 8
            )
        );
        return new Dimension(preferred.width, Math.max(preferred.height, targetHeight));
    }
}
