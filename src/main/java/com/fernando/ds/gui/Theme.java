package com.fernando.ds.gui;

import java.awt.Color;

public enum Theme {
    LIGHT(
        Color.WHITE,
        Color.BLACK,
        new Color(238, 238, 238),
        new Color(184, 207, 229)
    ),

    SOFT_BLUE(
        new Color(235, 245, 255),
        Color.BLACK,
        new Color(220, 235, 250),
        new Color(150, 190, 230)
    ),

    DARK(
        new Color(40, 44, 52),
        new Color(230, 230, 230),
        new Color(55, 60, 70),
        new Color(90, 120, 180)
    ),

    DARK_BLUE(
        new Color(18, 32, 47),      // main background
        new Color(235, 245, 255),   // text
        new Color(25, 45, 65),      // panel/card background
        new Color(70, 130, 180)     // selection color
    );

    private final Color background;
    private final Color foreground;
    private final Color panelBackground;
    private final Color selectionColor;

    Theme(Color background, Color foreground, Color panelBackground, Color selectionColor) {
        this.background = background;
        this.foreground = foreground;
        this.panelBackground = panelBackground;
        this.selectionColor = selectionColor;
    }

    public Color getBackground() {
        return background;
    }
    
    public String getBackgroundHex() {
        return String.format("#%02x%02x%02x",
                background.getRed(),
                background.getGreen(),
                background.getBlue());
    }

    public String toHex(Color color) {
        return String.format("#%02x%02x%02x",
                color.getRed(),
                color.getGreen(),
                color.getBlue());
    }

    public Color getForeground() {
        return foreground;
    }

    public Color getPanelBackground() {
        return panelBackground;
    }

    public Color getSelectionColor() {
        return selectionColor;
    }
}