/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: LeftPanel.java
 *
 * LeftPanel groups the question controls and data structure list on the
 * left side of the application window.
 */

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class LeftPanel extends JPanel {

    public LeftPanel(QuestionPanel questionPanel, DSListPanel dsListPanel) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 0));

        add(questionPanel, BorderLayout.NORTH);
        add(dsListPanel, BorderLayout.CENTER);
    }
}