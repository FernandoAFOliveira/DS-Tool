package com.fernando.ds.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.fernando.ds.util.ContentLoader;

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

        setJMenuBar(createMenuBar(controller));
        UiActionGuard.run(this, "Advisor", controller::reset);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar(AppController controller) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu(controller));
        menuBar.add(createSubjectMenu());
        menuBar.add(createExperienceMenu());
        menuBar.add(createViewMenu(controller));
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    private JMenu createFileMenu(AppController controller) {
        JMenu fileMenu = new JMenu("File");
        JMenuItem resetItem = new JMenuItem("Reset selections");
        JMenuItem exitItem = new JMenuItem("Exit");

        resetItem.addActionListener(event ->
            UiActionGuard.run(this, "Reset selections", controller::reset)
        );
        exitItem.addActionListener(event -> dispose());

        fileMenu.add(resetItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        return fileMenu;
    }

    private JMenu createSubjectMenu() {
        JMenu subjectMenu = new JMenu("Subject");
        JMenuItem javaItem = new JMenuItem("Java (active)");
        JMenuItem cItem = new JMenuItem("C");
        JMenuItem cppItem = new JMenuItem("C++");
        JMenuItem pythonItem = new JMenuItem("Python");

        javaItem.addActionListener(event -> JOptionPane.showMessageDialog(
            this,
            "Java is the active subject.",
            "Java",
            JOptionPane.INFORMATION_MESSAGE
        ));
        addNotEnabledAction(cItem, "C");
        addNotEnabledAction(cppItem, "C++");
        addNotEnabledAction(pythonItem, "Python");

        subjectMenu.add(javaItem);
        subjectMenu.addSeparator();
        subjectMenu.add(cItem);
        subjectMenu.add(cppItem);
        subjectMenu.add(pythonItem);
        return subjectMenu;
    }

    private JMenu createExperienceMenu() {
        JMenu experienceMenu = new JMenu("Experience");
        JMenuItem advisorItem = new JMenuItem("Advisor (active)");
        JMenuItem explorerItem = new JMenuItem("Explorer");
        JMenu learnMenu = new JMenu("Learn");
        JMenuItem flashCardsItem = new JMenuItem("Flash Cards");
        JMenuItem timedQuizItem = new JMenuItem("Timed Quiz");

        advisorItem.addActionListener(event -> JOptionPane.showMessageDialog(
            this,
            "Advisor is the active experience.",
            "Advisor",
            JOptionPane.INFORMATION_MESSAGE
        ));
        addNotEnabledAction(explorerItem, "Explorer");
        addNotEnabledAction(flashCardsItem, "Flash Cards");
        addNotEnabledAction(timedQuizItem, "Timed Quiz");

        learnMenu.add(flashCardsItem);
        learnMenu.add(timedQuizItem);
        experienceMenu.add(advisorItem);
        experienceMenu.add(explorerItem);
        experienceMenu.add(learnMenu);
        return experienceMenu;
    }

    private JMenu createViewMenu(AppController controller) {
        JMenu viewMenu = new JMenu("View");
        JMenu themeMenu = new JMenu("Theme");

        addThemeItem(themeMenu, "Light", Theme.LIGHT, controller);
        addThemeItem(themeMenu, "Soft Blue", Theme.SOFT_BLUE, controller);
        addThemeItem(themeMenu, "Dark", Theme.DARK, controller);
        addThemeItem(themeMenu, "Dark Blue", Theme.DARK_BLUE, controller);

        viewMenu.add(themeMenu);
        return viewMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");

        aboutItem.addActionListener(event ->
            UiActionGuard.run(this, "About", this::showAboutDialog)
        );
        helpMenu.add(aboutItem);
        return helpMenu;
    }

    private void addThemeItem(
        JMenu menu,
        String label,
        Theme theme,
        AppController controller
    ) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(event -> UiActionGuard.run(
            this,
            label + " theme",
            () -> applyTheme(theme, controller)
        ));
        menu.add(item);
    }

    private void addNotEnabledAction(JMenuItem item, String featureName) {
        item.addActionListener(event ->
            UiActionGuard.showNotEnabled(this, featureName)
        );
    }

    private void applyTheme(Theme theme, AppController controller) {
        ThemeManager.applyTheme(this, theme);
        controller.applyTheme(theme);
    }

    private void showAboutDialog() {
        String html = ContentLoader.loadTextResource("/content/about.html");

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setText(html);
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        editorPane.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(520, 360));
        scrollPane.setBorder(null);

        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "About Data Structure Advisor",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
