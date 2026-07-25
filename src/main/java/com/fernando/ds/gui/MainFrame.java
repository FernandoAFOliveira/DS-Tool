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

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProvider;
import com.fernando.ds.subject.SubjectProviderRegistry;
import com.fernando.ds.subject.UnavailableSubjectProvider;
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

        ApplicationState state = new ApplicationState();
        SubjectProviderRegistry subjectProviders =
            new SubjectProviderRegistry(java.util.List.of(
                new JavaSubjectProvider(),
                new UnavailableSubjectProvider(SubjectId.C, "C"),
                new UnavailableSubjectProvider(SubjectId.CPP, "C++"),
                new UnavailableSubjectProvider(SubjectId.PYTHON, "Python")
            ));

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
            state,
            subjectProviders,
            questionPanel,
            dsListPanel,
            diagramPanel,
            explanationPanel
        );

        setJMenuBar(createMenuBar(
            controller,
            state,
            subjectProviders
        ));
        UiActionGuard.run(this, "Advisor", controller::initialize);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar(
        AppController controller,
        ApplicationState state,
        SubjectProviderRegistry subjectProviders
    ) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu(controller));
        menuBar.add(createSubjectMenu(controller, state, subjectProviders));
        menuBar.add(createExperienceMenu(state));
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
        exitItem.addActionListener(event ->
            UiActionGuard.run(this, "Exit", this::dispose)
        );

        fileMenu.add(resetItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        return fileMenu;
    }

    private JMenu createSubjectMenu(
        AppController controller,
        ApplicationState state,
        SubjectProviderRegistry subjectProviders
    ) {
        JMenu subjectMenu = new JMenu("Subject");

        for (SubjectProvider provider : subjectProviders.getAll()) {
            if (provider.id() == SubjectId.C) {
                subjectMenu.addSeparator();
            }

            String activeLabel = provider.id() == state.getActiveSubject()
                ? " (active)"
                : "";
            JMenuItem item = new JMenuItem(
                provider.displayName() + activeLabel
            );
            addSubjectAction(
                item,
                provider.displayName(),
                provider.id(),
                controller
            );
            subjectMenu.add(item);
        }

        return subjectMenu;
    }

    private JMenu createExperienceMenu(ApplicationState state) {
        JMenu experienceMenu = new JMenu("Experience");
        JMenuItem advisorItem = new JMenuItem("Advisor (active)");
        JMenuItem explorerItem = new JMenuItem("Explorer");
        JMenu learnMenu = new JMenu("Learn");
        JMenuItem flashCardsItem = new JMenuItem("Flash Cards");
        JMenuItem timedQuizItem = new JMenuItem("Timed Quiz");

        advisorItem.addActionListener(event -> UiActionGuard.run(
            this,
            "Advisor",
            () -> {
                state.setActiveExperience(ApplicationState.Experience.ADVISOR);
                showInformationDialog(
                    "Advisor",
                    "Advisor is the active experience."
                );
            }
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

    private void addSubjectAction(
        JMenuItem item,
        String subjectName,
        SubjectId subjectId,
        AppController controller
    ) {
        item.addActionListener(event -> UiActionGuard.run(
            this,
            subjectName,
            () -> {
                if (!controller.selectSubject(subjectId)) {
                    UiActionGuard.showNotEnabled(this, subjectName);
                    return;
                }
                showInformationDialog(
                    subjectName,
                    subjectName + " is the active subject."
                );
            }
        ));
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

    private void showInformationDialog(String title, String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
