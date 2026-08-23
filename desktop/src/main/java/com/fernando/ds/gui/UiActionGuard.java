package com.fernando.ds.gui;

import java.awt.Component;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Executes UI actions inside a feature-level failure boundary.
 *
 * <p>A failed feature must report the problem, log the full exception, and
 * leave the main application available for continued use.</p>
 */
public final class UiActionGuard {

    static final String FEATURE_ERROR_MESSAGE =
        "There was an error trying to display this feature.";
    static final String NOT_ENABLED_MESSAGE =
        "This feature is not enabled yet.";

    private static final Logger LOGGER = Logger.getLogger(UiActionGuard.class.getName());

    private UiActionGuard() {
    }

    /**
     * Runs a Swing feature action without allowing its runtime failure to
     * escape onto the event-dispatch thread.
     *
     * @param parent window used to own any user notification
     * @param featureName user-facing feature name and log context
     * @param action feature work to execute
     */
    public static void run(Component parent, String featureName, Runnable action) {
        run(featureName, action, new SwingUiMessagePresenter(parent));
    }

    static void run(
        String featureName,
        Runnable action,
        UiMessagePresenter messagePresenter
    ) {
        Objects.requireNonNull(featureName, "featureName");
        Objects.requireNonNull(action, "action");
        Objects.requireNonNull(messagePresenter, "messagePresenter");

        try {
            action.run();
        } catch (RuntimeException exception) {
            LOGGER.log(Level.SEVERE, "Unable to display feature: " + featureName, exception);
            showSafely(
                featureName,
                () -> messagePresenter.showError(featureName, FEATURE_ERROR_MESSAGE)
            );
        }
    }

    /**
     * Reports an intentionally unavailable Swing feature using the standard
     * project message.
     *
     * @param parent window used to own the notification
     * @param featureName user-facing feature name
     */
    public static void showNotEnabled(Component parent, String featureName) {
        showNotEnabled(featureName, new SwingUiMessagePresenter(parent));
    }

    static void showNotEnabled(
        String featureName,
        UiMessagePresenter messagePresenter
    ) {
        Objects.requireNonNull(featureName, "featureName");
        Objects.requireNonNull(messagePresenter, "messagePresenter");

        showSafely(
            featureName,
            () -> messagePresenter.showInformation(featureName, NOT_ENABLED_MESSAGE)
        );
    }

    private static void showSafely(String featureName, Runnable notification) {
        try {
            notification.run();
        } catch (RuntimeException exception) {
            LOGGER.log(
                Level.SEVERE,
                "Unable to show UI notification for feature: " + featureName,
                exception
            );
        }
    }
}
