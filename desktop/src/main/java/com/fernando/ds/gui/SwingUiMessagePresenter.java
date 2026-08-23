package com.fernando.ds.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Displays action-boundary messages with Swing dialogs.
 */
final class SwingUiMessagePresenter implements UiMessagePresenter {

    private final Component parent;

    SwingUiMessagePresenter(Component parent) {
        this.parent = parent;
    }

    @Override
    public void showInformation(String title, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }
}
