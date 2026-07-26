package com.fernando.ds.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MainPanel extends JPanel implements SubjectContextView {

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

        JLabel resizeHint = new JLabel(
            "\u2195 Drag to resize diagram",
            SwingConstants.CENTER
        );
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
        SwingUtilities.invokeLater(() ->
            rightPanel.setDividerLocation(0.55)
        );
        add(rightPanel, BorderLayout.CENTER);
    }

    @Override
    public void showSubjectContext(String subjectDisplayName) {
        // Advisor no longer displays a dedicated subject-context label.
    }
}
