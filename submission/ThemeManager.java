/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: ThemeManager.java
 *
 * ThemeManager applies a selected theme to Swing components.
 * Keeping this logic in one class avoids repeating color-setting code
 * throughout the GUI.
 */

import javax.swing.*;
import java.awt.*;

public class ThemeManager {

    private ThemeManager() {
        // Prevent object creation.
    }

    public static void applyTheme(Component component, Theme theme) {
        applyThemeToComponent(component, theme);
        component.repaint();
    }

    public static void applyThemeToComponent(Component component, Theme theme) {
        if (component instanceof JPanel) {
            component.setBackground(theme.getPanelBackground());
            component.setForeground(theme.getForeground());
        } else {
            component.setBackground(theme.getBackground());
            component.setForeground(theme.getForeground());
        }

        if (component instanceof JTextPane textPane) {
            textPane.setBackground(theme.getBackground());
            textPane.setForeground(theme.getForeground());
        }

        if (component instanceof JList<?> list) {
            list.setBackground(theme.getBackground());
            list.setForeground(theme.getForeground());
            list.setSelectionBackground(theme.getSelectionColor());
            list.setSelectionForeground(theme.getForeground());
        }

        if (component instanceof JScrollPane scrollPane) {
            scrollPane.getViewport().setBackground(theme.getBackground());
        }

        if (component instanceof JMenuBar || component instanceof JMenu || component instanceof JMenuItem) {
            component.setBackground(theme.getPanelBackground());
            component.setForeground(theme.getForeground());
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeToComponent(child, theme);
            }
        }
    }
}