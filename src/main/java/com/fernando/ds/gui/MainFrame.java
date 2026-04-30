package com.fernando.ds.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Data Structure Advisor");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 900);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setIconImage(
            Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/icon.png")
            )
        );

        QuestionPanel questionPanel = new QuestionPanel();
        DSListPanel dsListPanel = new DSListPanel();
        DiagramPanel diagramPanel = new DiagramPanel();
        ExplanationPanel explanationPanel = new ExplanationPanel();

        MainPanel mainPanel = new MainPanel(
            questionPanel,
            dsListPanel,
            diagramPanel,
            explanationPanel
        );

        AppController controller = new AppController(
                questionPanel,
                dsListPanel,
                diagramPanel,
                explanationPanel
            );

        setJMenuBar(createMenuBar(controller, explanationPanel));

        explanationPanel.showWelcome();
        controller.reset();

        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JMenuBar createMenuBar(AppController controller, ExplanationPanel explanationPanel){
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

        lightThemeItem.addActionListener(e -> applyTheme(Theme.LIGHT, controller));

        softBlueThemeItem.addActionListener(e -> applyTheme(Theme.SOFT_BLUE, controller));

        darkThemeItem.addActionListener(e -> applyTheme(Theme.DARK, controller));

        darkBlueThemeItem.addActionListener(e -> applyTheme(Theme.DARK_BLUE, controller));

        return menuBar;
    }

    private void applyTheme(Theme theme, AppController controller) {
        ThemeManager.applyTheme(this, theme);
        controller.applyTheme(theme);
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