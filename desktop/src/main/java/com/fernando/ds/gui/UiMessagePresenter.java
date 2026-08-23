package com.fernando.ds.gui;

/**
 * Presents user-safe messages produced by the UI action boundary.
 *
 * <p>The contract is independent of Swing so action-boundary behavior can be
 * tested without creating a visible desktop.</p>
 */
interface UiMessagePresenter {

    void showInformation(String title, String message);

    void showError(String title, String message);
}
