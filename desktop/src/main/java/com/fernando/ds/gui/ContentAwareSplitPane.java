package com.fernando.ds.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JSplitPane;

/**
 * Applies refreshed content width until the user moves the divider, then
 * preserves that explicit divider position.
 */
final class ContentAwareSplitPane extends JSplitPane {

    private int appliedLeftWidth = -1;

    ContentAwareSplitPane(Component left, Component right) {
        super(JSplitPane.HORIZONTAL_SPLIT, left, right);
        setContinuousLayout(true);
        setOneTouchExpandable(true);
        setResizeWeight(0.0);
    }

    void refreshPreferredLeftWidth(int preferredWidth) {
        Component left = getLeftComponent();
        Dimension preferredSize = left.getPreferredSize();
        left.setPreferredSize(new Dimension(
            preferredWidth,
            preferredSize.height
        ));

        int currentLocation = getDividerLocation();
        if (appliedLeftWidth < 0 || currentLocation == appliedLeftWidth) {
            setDividerLocation(preferredWidth);
            appliedLeftWidth = preferredWidth;
        }
    }
}
