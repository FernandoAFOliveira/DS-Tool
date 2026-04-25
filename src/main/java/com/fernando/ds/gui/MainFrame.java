package com.fernando.ds.gui;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Data Structure Advisor");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DetailPanel detailPanel = new DetailPanel();
        QuestionPanel questionPanel = new QuestionPanel();
        DSListPanel dsListPanel = new DSListPanel();

        LeftPanel leftPanel = new LeftPanel(questionPanel, dsListPanel);

        new AppController(questionPanel, dsListPanel, detailPanel);

        add(leftPanel, BorderLayout.WEST);
        add(detailPanel, BorderLayout.CENTER);
    }
}