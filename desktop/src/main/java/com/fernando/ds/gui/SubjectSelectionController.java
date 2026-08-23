package com.fernando.ds.gui;

import java.util.Objects;
import java.util.function.Consumer;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProvider;
import com.fernando.ds.subject.SubjectProviderRegistry;

/**
 * Coordinates subject selection without depending on a graphical toolkit.
 */
final class SubjectSelectionController {

    private final ApplicationState state;
    private final SubjectProviderRegistry subjectProviders;

    SubjectSelectionController(
        ApplicationState state,
        SubjectProviderRegistry subjectProviders
    ) {
        this.state = Objects.requireNonNull(state, "state");
        this.subjectProviders = Objects.requireNonNull(
            subjectProviders,
            "subjectProviders"
        );
    }

    /**
     * Renders an enabled subject before committing it to shared state.
     *
     * @param subjectId requested subject
     * @param renderer active-experience renderer for the requested provider
     * @return whether the requested subject is enabled
     */
    boolean select(
        SubjectId subjectId,
        Consumer<SubjectProvider> renderer
    ) {
        Objects.requireNonNull(subjectId, "subjectId");
        Objects.requireNonNull(renderer, "renderer");

        SubjectProvider provider = subjectProviders.get(subjectId);
        if (!provider.isEnabled()) {
            return false;
        }
        if (state.getActiveSubject() == subjectId) {
            return true;
        }

        renderer.accept(provider);
        state.setActiveSubject(subjectId);
        return true;
    }
}
