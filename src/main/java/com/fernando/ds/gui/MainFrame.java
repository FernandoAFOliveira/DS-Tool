package com.fernando.ds.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
        setJMenuBar(createMenuBar());

        DetailPanel detailPanel = new DetailPanel();
        QuestionPanel questionPanel = new QuestionPanel();
        DSListPanel dsListPanel = new DSListPanel();

        LeftPanel leftPanel = new LeftPanel(questionPanel, dsListPanel);

        new AppController(questionPanel, dsListPanel, detailPanel);

        add(leftPanel, BorderLayout.WEST);
        add(detailPanel, BorderLayout.CENTER);
    }
        private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem resetItem = new JMenuItem("Reset selections");
        JMenuItem exitItem = new JMenuItem("Exit");

        exitItem.addActionListener(e -> dispose());

        fileMenu.add(resetItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");
        JMenu themeMenu = new JMenu("Theme");

        themeMenu.add(new JMenuItem("Light"));
        themeMenu.add(new JMenuItem("Soft Blue"));
        themeMenu.add(new JMenuItem("Dark"));

        viewMenu.add(themeMenu);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);

        return menuBar;
    }
    
}