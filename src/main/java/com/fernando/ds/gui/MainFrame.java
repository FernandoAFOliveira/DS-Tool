package com.fernando.ds.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.application.ExplorerService;
import com.fernando.ds.application.LearningQuestionSource;
import com.fernando.ds.subject.CSubjectProvider;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProvider;
import com.fernando.ds.subject.SubjectProviderRegistry;
import com.fernando.ds.subject.UnavailableSubjectProvider;
import com.fernando.ds.util.ContentLoader;

public class MainFrame extends JFrame {

    private static final String ADVISOR_CARD = "advisor";
    private static final String EXPLORER_CARD = "explorer";
    private static final String FLASH_CARDS_CARD = "flashCards";

    private final ApplicationState state = new ApplicationState();
    private final SubjectProviderRegistry subjectProviders;
    private final SubjectSelectionController subjectSelectionController;
    private final AppController advisorController;
    private final CardLayout experienceLayout = new CardLayout();
    private final JPanel experiences = new JPanel(experienceLayout);
    private ExplorerPanel explorerPanel;
    private ExplorerController explorerController;
    private FlashCardPanel flashCardPanel;
    private FlashCardController flashCardController;
    private JMenuItem advisorItem;
    private JMenuItem explorerItem;
    private JMenuItem flashCardsItem;
    private final Map<SubjectId, JMenuItem> subjectItems =
        new EnumMap<>(SubjectId.class);

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

        subjectProviders = new SubjectProviderRegistry(java.util.List.of(
            new JavaSubjectProvider(),
            new CSubjectProvider(),
            new UnavailableSubjectProvider(SubjectId.CPP, "C++"),
            new UnavailableSubjectProvider(SubjectId.PYTHON, "Python")
        ));
        subjectSelectionController = new SubjectSelectionController(
            state,
            subjectProviders
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

        advisorController = new AppController(
            state,
            subjectProviders,
            questionPanel,
            dsListPanel,
            diagramPanel,
            explanationPanel
        );

        experiences.add(mainPanel, ADVISOR_CARD);
        setJMenuBar(createMenuBar());
        UiActionGuard.run(this, "Advisor", advisorController::initialize);
        add(experiences, BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createSubjectMenu());
        menuBar.add(createExperienceMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem resetItem = new JMenuItem("Reset selections");
        JMenuItem exitItem = new JMenuItem("Exit");

        resetItem.addActionListener(event ->
            UiActionGuard.run(this, "Reset selections", () -> {
                advisorController.reset();
                if (explorerController != null) {
                    explorerController.activate();
                }
            })
        );
        exitItem.addActionListener(event ->
            UiActionGuard.run(this, "Exit", this::dispose)
        );

        fileMenu.add(resetItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        return fileMenu;
    }

    private JMenu createSubjectMenu() {
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
                provider.id()
            );
            subjectItems.put(provider.id(), item);
            subjectMenu.add(item);
        }

        return subjectMenu;
    }

    private JMenu createExperienceMenu() {
        JMenu experienceMenu = new JMenu("Experience");
        advisorItem = new JMenuItem("Advisor (active)");
        explorerItem = new JMenuItem("Explorer");
        JMenu learnMenu = new JMenu("Learn");
        flashCardsItem = new JMenuItem("Flash Cards");
        JMenuItem timedQuizItem = new JMenuItem("Timed Quiz");

        advisorItem.addActionListener(event -> UiActionGuard.run(
            this,
            "Advisor",
            this::showAdvisor
        ));
        explorerItem.addActionListener(event -> UiActionGuard.run(
            this,
            "Explorer",
            this::showExplorer
        ));
        flashCardsItem.addActionListener(event -> UiActionGuard.run(
            this,
            "Flash Cards",
            this::showFlashCards
        ));
        addNotEnabledAction(timedQuizItem, "Timed Quiz");

        learnMenu.add(flashCardsItem);
        learnMenu.add(timedQuizItem);
        experienceMenu.add(advisorItem);
        experienceMenu.add(explorerItem);
        experienceMenu.add(learnMenu);
        return experienceMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("View");
        JMenu themeMenu = new JMenu("Theme");

        addThemeItem(themeMenu, "Light", Theme.LIGHT);
        addThemeItem(themeMenu, "Soft Blue", Theme.SOFT_BLUE);
        addThemeItem(themeMenu, "Dark", Theme.DARK);
        addThemeItem(themeMenu, "Dark Blue", Theme.DARK_BLUE);

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
        Theme theme
    ) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(event -> UiActionGuard.run(
            this,
            label + " theme",
            () -> applyTheme(theme)
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
        SubjectId subjectId
    ) {
        item.addActionListener(event -> UiActionGuard.run(
            this,
            subjectName,
            () -> {
                if (!subjectSelectionController.select(
                    subjectId,
                    this::renderActiveExperience
                )) {
                    UiActionGuard.showNotEnabled(this, subjectName);
                    return;
                }
                updateSubjectLabels();
                showInformationDialog(
                    subjectName,
                    subjectName + " is the active subject."
                );
            }
        ));
    }

    private void renderActiveExperience(SubjectProvider provider) {
        switch (state.getActiveExperience()) {
            case ADVISOR -> advisorController.activate(provider);
            case EXPLORER -> explorerController.activate(provider);
            case LEARN -> flashCardController.activate(provider);
        }
    }

    private void updateSubjectLabels() {
        for (SubjectProvider provider : subjectProviders.getAll()) {
            JMenuItem item = subjectItems.get(provider.id());
            String activeLabel = provider.id() == state.getActiveSubject()
                ? " (active)"
                : "";
            item.setText(provider.displayName() + activeLabel);
        }
    }

    private void applyTheme(Theme theme) {
        ThemeManager.applyTheme(this, theme);
        advisorController.applyTheme(theme);
        if (explorerController != null) {
            explorerController.applyTheme(theme);
        }
        if (flashCardController != null) {
            flashCardController.applyTheme(theme);
        }
    }

    private void showAdvisor() {
        if (state.getActiveExperience()
            == ApplicationState.Experience.ADVISOR) {
            showInformationDialog(
                "Advisor",
                "Advisor is the active experience."
            );
            return;
        }

        advisorController.activate();
        experienceLayout.show(experiences, ADVISOR_CARD);
        state.setActiveExperience(ApplicationState.Experience.ADVISOR);
        updateExperienceLabels();
    }

    private void showExplorer() {
        if (explorerController == null) {
            explorerPanel = new ExplorerPanel();
            explorerController = new ExplorerController(
                state,
                subjectProviders,
                new ExplorerService(),
                explorerPanel
            );
            explorerPanel.setSelectionListener(dataStructure ->
                UiActionGuard.run(
                    this,
                    "Explorer",
                    () -> explorerController.selectStructure(dataStructure)
                )
            );
            Theme currentTheme = Theme.fromAppearance(state.getAppearance());
            ThemeManager.applyThemeToComponent(explorerPanel, currentTheme);
            explorerController.applyTheme(currentTheme);
            experiences.add(explorerPanel, EXPLORER_CARD);
        }

        explorerController.activate();
        experienceLayout.show(experiences, EXPLORER_CARD);
        state.setActiveExperience(ApplicationState.Experience.EXPLORER);
        updateExperienceLabels();
    }

    private void showFlashCards() {
        if (flashCardController == null) {
            flashCardPanel = new FlashCardPanel();
            flashCardController = new FlashCardController(
                state,
                subjectProviders,
                new LearningQuestionSource(),
                flashCardPanel
            );
            flashCardPanel.setPreviousListener(() -> UiActionGuard.run(
                this,
                "Flash Cards",
                flashCardController::showPrevious
            ));
            flashCardPanel.setRevealListener(() -> UiActionGuard.run(
                this,
                "Flash Cards",
                flashCardController::revealAnswer
            ));
            flashCardPanel.setNextListener(() -> UiActionGuard.run(
                this,
                "Flash Cards",
                flashCardController::showNext
            ));
            Theme currentTheme = Theme.fromAppearance(state.getAppearance());
            ThemeManager.applyThemeToComponent(
                flashCardPanel,
                currentTheme
            );
            flashCardController.applyTheme(currentTheme);
            experiences.add(flashCardPanel, FLASH_CARDS_CARD);
        }

        flashCardController.activate();
        experienceLayout.show(experiences, FLASH_CARDS_CARD);
        state.setActiveExperience(ApplicationState.Experience.LEARN);
        updateExperienceLabels();
    }

    private void updateExperienceLabels() {
        boolean advisorActive = state.getActiveExperience()
            == ApplicationState.Experience.ADVISOR;
        boolean explorerActive = state.getActiveExperience()
            == ApplicationState.Experience.EXPLORER;
        boolean learnActive = state.getActiveExperience()
            == ApplicationState.Experience.LEARN;
        advisorItem.setText(advisorActive ? "Advisor (active)" : "Advisor");
        explorerItem.setText(
            explorerActive ? "Explorer (active)" : "Explorer"
        );
        flashCardsItem.setText(
            learnActive ? "Flash Cards (active)" : "Flash Cards"
        );
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
