package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.application.ApplicationState.NavigationKind;
import com.fernando.ds.application.ExplorerContent;
import com.fernando.ds.application.ExplorerService;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.library.QuestionInfo.QuestionId;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.model.Preference;
import com.fernando.ds.subject.CSubjectProvider;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProviderRegistry;

class ExplorerControllerTest {

    @Test
    void startsAtWelcomeAndListsTheCompleteCatalogWithoutASelection() {
        ApplicationState state = new ApplicationState(Locale.US);
        RecordingView view = new RecordingView();

        controller(state, view).activate();

        assertTrue(view.welcomeShown);
        assertEquals(9, view.structures.size());
        assertEquals(null, view.highlighted);
        assertFalse(state.getSelectedStructureId().isPresent());
    }

    @Test
    void restoresTheSharedSelectionAndItsContent() {
        ApplicationState state = new ApplicationState(Locale.US);
        state.selectStructure(StructureId.ORDERED_MAP);
        RecordingView view = new RecordingView();

        controller(state, view).activate();

        assertEquals(StructureId.ORDERED_MAP, view.highlighted);
        assertEquals(StructureId.ORDERED_MAP, view.content.knowledge().id());
        assertFalse(view.welcomeShown);
    }

    @Test
    void selectionUpdatesOnlyTheSharedStructure() {
        ApplicationState state = new ApplicationState(Locale.US);
        state.setPreference(QuestionId.INDEXED, Preference.YES);
        state.navigateToQuestion(QuestionId.MEMORY);
        RecordingView view = new RecordingView();
        ExplorerController controller = controller(state, view);
        DataStructure selected = javaProvider().getRepresentation(
            StructureId.HASH_SET
        ).orElseThrow();

        controller.selectStructure(selected);

        assertEquals(
            StructureId.HASH_SET,
            state.getSelectedStructureId().orElseThrow()
        );
        assertEquals(NavigationKind.QUESTION, state.getNavigation().kind());
        assertEquals(
            Preference.YES,
            state.getRecommendationAnswers().getIndexedPreference()
        );
        assertEquals(StructureId.HASH_SET, view.content.knowledge().id());
    }

    @Test
    void failedRenderingDoesNotCommitSelectionAndLaterActionStillWorks() {
        ApplicationState state = new ApplicationState(Locale.US);
        state.selectStructure(StructureId.QUEUE);
        RecordingView view = new RecordingView();
        ExplorerController controller = controller(state, view);
        DataStructure stack = javaProvider().getRepresentation(
            StructureId.STACK
        ).orElseThrow();

        view.failContent = true;
        assertThrows(
            IllegalStateException.class,
            () -> controller.selectStructure(stack)
        );
        assertEquals(
            StructureId.QUEUE,
            state.getSelectedStructureId().orElseThrow()
        );

        view.failContent = false;
        controller.selectStructure(stack);
        assertEquals(
            StructureId.STACK,
            state.getSelectedStructureId().orElseThrow()
        );
    }

    @Test
    void canRenderARequestedCProviderBeforeSubjectCommit() {
        ApplicationState state = new ApplicationState(Locale.US);
        state.selectStructure(StructureId.HASH_MAP);
        RecordingView view = new RecordingView();
        ExplorerController controller = controller(state, view);

        controller.activate(new CSubjectProvider());

        assertEquals(SubjectId.JAVA, state.getActiveSubject());
        assertEquals("C", view.content.subjectDisplayName());
        assertEquals(
            "Hash table",
            view.content.representation().getDisplayName()
        );
        assertEquals(StructureId.HASH_MAP, view.highlighted);
    }

    private static ExplorerController controller(
        ApplicationState state,
        RecordingView view
    ) {
        return new ExplorerController(
            state,
            new SubjectProviderRegistry(List.of(javaProvider())),
            new ExplorerService(),
            view
        );
    }

    private static JavaSubjectProvider javaProvider() {
        return new JavaSubjectProvider();
    }

    private static final class RecordingView implements ExplorerView {

        private List<DataStructure> structures = List.of();
        private StructureId highlighted;
        private ExplorerContent content;
        private boolean welcomeShown;
        private boolean failContent;

        @Override
        public void showStructures(
            List<DataStructure> values,
            StructureId selectedStructureId
        ) {
            structures = values;
            highlighted = selectedStructureId;
        }

        @Override
        public void showWelcome() {
            welcomeShown = true;
            content = null;
        }

        @Override
        public void showContent(ExplorerContent value) {
            if (failContent) {
                throw new IllegalStateException("render failed");
            }
            content = value;
            welcomeShown = false;
        }

        @Override
        public void applyTheme(Theme theme) {
        }
    }
}
