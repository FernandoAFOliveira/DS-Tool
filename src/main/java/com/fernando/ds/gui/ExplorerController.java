package com.fernando.ds.gui;

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
        SubjectProvider provider = activeSubject();
        view.showStructures(
            explorerService.getAvailableStructures(provider),
            state.getSelectedStructureId().orElse(null)
        );
        state.getSelectedStructureId()
            .flatMap(id -> explorerService.getContent(provider, id))
            .ifPresentOrElse(view::showContent, view::showWelcome);
    }

    void selectStructure(DataStructure representation) {
        Objects.requireNonNull(representation, "representation");
        ExplorerContent content = explorerService.getContent(
            activeSubject(),
            representation.getStructureId()
        ).orElseThrow(() -> new IllegalStateException(
            "Selected structure has no active-subject representation"
        ));

        // Render first so a presentation failure cannot commit partial state.
        view.showContent(content);
        state.selectStructure(representation.getStructureId());
    }

    void applyTheme(Theme theme) {
        view.applyTheme(Objects.requireNonNull(theme, "theme"));
        state.getSelectedStructureId()
            .flatMap(id -> explorerService.getContent(activeSubject(), id))
            .ifPresentOrElse(view::showContent, view::showWelcome);
    }

    private SubjectProvider activeSubject() {
        return subjectProviders.get(state.getActiveSubject());
    }
}
