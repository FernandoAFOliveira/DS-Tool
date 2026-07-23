package com.fernando.ds.gui;

import java.awt.Component;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * Executes UI actions inside a feature-level failure boundary.
 *
 * <p>A failed feature must report the problem, log the full exception, and
 * leave the main application available for continued use.</p>
 */
public final class UiActionGuard {

    private static final Logger LOGGER = Logger.getLogger(UiActionGuard.class.getName());

    private UiActionGuard() {
    }

    public static void run(Component parent, String featureName, Runnable action) {
        Objects.requireNonNull(featureName, "featureName");
        Objects.requireNonNull(action, "action");

        try {
            action.run();
        } catch (RuntimeException exception) {
            LOGGER.log(Level.SEVERE, "Unable to display feature: " + featureName, exception);
            JOptionPane.showMessageDialog(
                parent,
                "There was an error trying to display this feature.",
                featureName,
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void showNotEnabled(Component parent, String featureName) {
        JOptionPane.showMessageDialog(
            parent,
            "This feature is not enabled yet.",
            featureName,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
