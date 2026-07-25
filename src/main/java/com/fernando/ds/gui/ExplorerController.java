package com.fernando.ds.gui;

import java.util.List;
import java.util.Objects;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.application.ExplorerContent;
import com.fernando.ds.application.ExplorerService;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.subject.SubjectProvider;
import com.fernando.ds.subject.SubjectProviderRegistry;

/**
 * Coordinates the Explorer without applying Advisor recommendation filters.
 */
final class ExplorerController {

    private final ApplicationState state;
    private final SubjectProviderRegistry subjectProviders;
    private final ExplorerService explorerService;
    private final ExplorerView view;

    ExplorerController(
        ApplicationState state,
        SubjectProviderRegistry subjectProviders,
        ExplorerService explorerService,
        ExplorerView view
    ) {
        this.state = Objects.requireNonNull(state, "state");
        this.subjectProviders = Objects.requireNonNull(
            subjectProviders,
            "subjectProviders"
        );
        this.explorerService = Objects.requireNonNull(
            explorerService,
            "explorerService"
        );
        this.view = Objects.requireNonNull(view, "view");
    }

    void activate() {
        activate(activeSubject());
    }

    /**
     * Renders Explorer using a requested provider before subject commit.
     */
    void activate(SubjectProvider provider) {
        Objects.requireNonNull(provider, "provider");
        view.showSubjects(explorerService.getSubjects(enabledProviders()));
        view.showStructures(
            explorerService.getAvailableStructures(provider),
            state.getSelectedStructureId().orElse(null)
        );
        state.getSelectedStructureId()
            .map(id -> explorerService.getContent(enabledProviders(), id))
            .ifPresentOrElse(view::showContent, view::showWelcome);
    }

    void selectStructure(DataStructure representation) {
        Objects.requireNonNull(representation, "representation");
        ExplorerContent content = explorerService.getContent(
            enabledProviders(),
            representation.getStructureId()
        );

        // Render first so a presentation failure cannot commit partial state.
        view.showContent(content);
        state.selectStructure(representation.getStructureId());
    }

    void applyTheme(Theme theme) {
        view.applyTheme(Objects.requireNonNull(theme, "theme"));
        state.getSelectedStructureId()
            .map(id -> explorerService.getContent(enabledProviders(), id))
            .ifPresentOrElse(view::showContent, view::showWelcome);
    }

    private SubjectProvider activeSubject() {
        return subjectProviders.get(state.getActiveSubject());
    }

    private List<SubjectProvider> enabledProviders() {
        return subjectProviders.getEnabled();
    }
}
