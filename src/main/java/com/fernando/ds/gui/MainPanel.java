package com.fernando.ds.gui;

import java.awt.*;
import javax.swing.*;

public class MainPanel extends JPanel {

    public MainPanel(
        QuestionPanel questionPanel,
        DSListPanel dsListPanel,
        DiagramPanel diagramPanel,
        ExplanationPanel explanationPanel
    ) {
        setLayout(new BorderLayout());

    JPanel leftPanel = new JPanel(new BorderLayout());

    leftPanel.add(questionPanel, BorderLayout.NORTH);
    leftPanel.add(dsListPanel, BorderLayout.CENTER);

    add(leftPanel, BorderLayout.WEST);

        JLabel resizeHint = new JLabel("↕ Drag to resize diagram", SwingConstants.CENTER);
        resizeHint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        resizeHint.setForeground(Color.GRAY);

        JPanel diagramContainer = new JPanel(new BorderLayout());
        diagramContainer.add(diagramPanel, BorderLayout.CENTER);
        diagramContainer.add(resizeHint, BorderLayout.SOUTH);

        JSplitPane rightPanel = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            diagramContainer,
            explanationPanel
        );

        rightPanel.setResizeWeight(0.55);
        rightPanel.setOneTouchExpandable(true);
        rightPanel.setContinuousLayout(true);

        SwingUtilities.invokeLater(() -> rightPanel.setDividerLocation(0.55));

        add(rightPanel, BorderLayout.CENTER);
    }
}