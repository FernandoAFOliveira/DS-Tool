package com.fernando.ds.gui;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public class TextStyles {

    private final Style sectionHeader;
    private final Style normal;
    private final Style code;
    private final Style title;

    public TextStyles(JTextPane textPane) {
        sectionHeader = textPane.addStyle("SectionHeader", null);
        StyleConstants.setBold(sectionHeader, true);
        StyleConstants.setFontSize(sectionHeader, 14);
        StyleConstants.setForeground(sectionHeader, Color.BLACK);
        StyleConstants.setLeftIndent(sectionHeader, 20);
        StyleConstants.setSpaceAbove(sectionHeader, 12);
        StyleConstants.setSpaceBelow(sectionHeader, 4);

        normal = textPane.addStyle("Normal", null);
        StyleConstants.setFontSize(normal, 14);
        StyleConstants.setForeground(normal, Color.BLACK);
        StyleConstants.setAlignment(normal, StyleConstants.ALIGN_LEFT);
        StyleConstants.setLeftIndent(normal, 35);
        StyleConstants.setRightIndent(normal, 20);

        code = textPane.addStyle("Code", null);
        StyleConstants.setFontFamily(code, "Monospaced");
        StyleConstants.setFontSize(code, 14);
        StyleConstants.setForeground(code, Color.BLACK);
        StyleConstants.setAlignment(code, StyleConstants.ALIGN_LEFT);
        StyleConstants.setLeftIndent(code, 35);
        StyleConstants.setRightIndent(code, 20);

        title = textPane.addStyle("Title", null);
        StyleConstants.setBold(title, true);
        StyleConstants.setFontSize(title, 20);
        StyleConstants.setAlignment(title, StyleConstants.ALIGN_CENTER);
        StyleConstants.setLeftIndent(title, 0);
        StyleConstants.setSpaceAbove(title, 10);
        StyleConstants.setSpaceBelow(title, 4);
    }

    public void applyTheme(Theme theme) {
        StyleConstants.setForeground(normal, theme.getForeground());
        StyleConstants.setForeground(sectionHeader, theme.getForeground());
        StyleConstants.setForeground(title, theme.getForeground());
        StyleConstants.setForeground(code, theme.getForeground());
    }

    public Style getSectionHeader() {
        return sectionHeader;
    }

    public Style getNormal() {
        return normal;
    }

    public Style getCode() {
        return code;
    }

    public Style getTitle() {
        return title;
    }
}