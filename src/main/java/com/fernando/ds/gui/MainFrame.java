package com.fernando.ds.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

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

        AppController controller = new AppController(questionPanel, dsListPanel, detailPanel);

        setJMenuBar(createMenuBar(controller, detailPanel));

        detailPanel.showWelcome();

        add(leftPanel, BorderLayout.WEST);
        add(detailPanel, BorderLayout.CENTER);
    }
    private JMenuBar createMenuBar(AppController controller, DetailPanel detailPanel){
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem resetItem = new JMenuItem("Reset selections");
        JMenuItem exitItem = new JMenuItem("Exit");

        resetItem.addActionListener(e -> controller.reset());
        exitItem.addActionListener(e -> dispose());

        fileMenu.add(resetItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");
        JMenu themeMenu = new JMenu("Theme");

        JMenuItem lightThemeItem = new JMenuItem("Light");
        JMenuItem softBlueThemeItem = new JMenuItem("Soft Blue");
        JMenuItem darkThemeItem = new JMenuItem("Dark");
        JMenuItem darkBlueThemeItem = new JMenuItem("Dark Blue");

        themeMenu.add(lightThemeItem);
        themeMenu.add(softBlueThemeItem);
        themeMenu.add(darkThemeItem);
        themeMenu.add(darkBlueThemeItem);

        viewMenu.add(themeMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");

        aboutItem.addActionListener(e -> showAboutDialog());

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        lightThemeItem.addActionListener(e -> {
            ThemeManager.applyTheme(this, Theme.LIGHT);
            detailPanel.applyTheme(Theme.LIGHT);
        });

        softBlueThemeItem.addActionListener(e -> {
            ThemeManager.applyTheme(this, Theme.SOFT_BLUE);
            detailPanel.applyTheme(Theme.SOFT_BLUE);
        });

        darkThemeItem.addActionListener(e -> {
            ThemeManager.applyTheme(this, Theme.DARK);
            detailPanel.applyTheme(Theme.DARK);
        });

        darkBlueThemeItem.addActionListener(e -> {
            ThemeManager.applyTheme(this, Theme.DARK_BLUE);
            detailPanel.applyTheme(Theme.DARK_BLUE);
        });

        return menuBar;
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(
            this,
            "Data Structure Advisor\n\n"
            + "Created by Fernando Oliveira\n"
            + "COP 3330 Object-Oriented Programming\n\n"
            + "This app helps students compare common Java data structures.\n"
            + "Use the questions and sliders to filter and sort the structures,\n"
            + "then select a data structure to view details and example code.",
            "About Data Structure Advisor",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
}