package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.library.QuestionInfo.QuestionId;
import com.fernando.ds.model.DSArrayList;
import com.fernando.ds.model.DSHashMap;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.model.Preference;

class ApplicationStateUiTest {

    @Test
    void silentSliderUpdateRendersWithoutEmittingAUserChange() throws Exception {
        onEdt(() -> {
            CompactSliderPanel slider =
                new CompactSliderPanel("Lookup", 0, 5, 5);
            AtomicInteger changes = new AtomicInteger();
            slider.setChangeListener(value -> changes.incrementAndGet());

            slider.setValueSilently(2);

            assertEquals(2, slider.getValue());
            assertEquals(0, changes.get());
            return null;
        });
    }

    @Test
    void questionPanelRendersAnswersWithoutEmittingUserChanges() throws Exception {
        onEdt(() -> {
            ApplicationState state = new ApplicationState();
            state.setPreference(QuestionId.KEY_VALUE, Preference.YES);
            state.setWeight(QuestionId.LOOKUP, 1);
            state.setWeight(QuestionId.ADD_DELETE, 2);
            state.setWeight(QuestionId.MEMORY, 3);

            QuestionPanel panel = new QuestionPanel();
            AtomicInteger changes = new AtomicInteger();
            panel.setPreferenceSelectionListener((question, value) ->
                changes.incrementAndGet()
            );
            panel.setWeightSelectionListener((question, value) ->
                changes.incrementAndGet()
            );
            panel.setRemovalOrderSelectionListener((question, value) ->
                changes.incrementAndGet()
            );

            panel.applyAnswers(state.getRecommendationAnswers());

            List<CompactSliderPanel> sliders = descendants(
                panel,
                CompactSliderPanel.class
            );
            List<Integer> values = sliders.stream()
                .map(CompactSliderPanel::getValue)
                .sorted()
                .toList();
            long disabledDuplicateChoices = descendants(
                panel,
                JRadioButton.class
            ).stream()
                .filter(button -> !button.isEnabled())
                .count();

            assertEquals(List.of(1, 2, 3), values);
            assertEquals(3, disabledDuplicateChoices);
            assertEquals(0, changes.get());
            return null;
        });
    }

    @Test
    void listRefreshRestoresSelectionWithoutEmittingAUserSelection()
        throws Exception {
        onEdt(() -> {
            DSListPanel panel = new DSListPanel();
            AtomicInteger changes = new AtomicInteger();
            panel.setSelectionListener(selected -> changes.incrementAndGet());
            List<DataStructure> structures =
                List.of(new DSArrayList(), new DSHashMap());

            panel.updateList(structures, "HashMap");

            JList<?> list = descendants(panel, JList.class).getFirst();
            assertEquals("HashMap", list.getSelectedValue().toString());
            assertEquals(0, changes.get());

            list.setSelectedIndex(0);
            assertEquals(1, changes.get());
            assertFalse(list.isSelectionEmpty());
            return null;
        });
    }

    private static <T> T onEdt(ThrowingSupplier<T> action) throws Exception {
        AtomicReference<T> result = new AtomicReference<>();
        AtomicReference<Throwable> failure = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            try {
                result.set(action.get());
            } catch (Throwable throwable) {
                failure.set(throwable);
            }
        });
        if (failure.get() != null) {
            if (failure.get() instanceof Exception exception) {
                throw exception;
            }
            throw (Error) failure.get();
        }
        return result.get();
    }

    private static <T extends Component> List<T> descendants(
        Container root,
        Class<T> type
    ) {
        List<T> matches = new ArrayList<>();
        for (Component component : root.getComponents()) {
            if (type.isInstance(component)) {
                matches.add(type.cast(component));
            }
            if (component instanceof JScrollPane scrollPane) {
                Component view = scrollPane.getViewport().getView();
                if (type.isInstance(view)) {
                    matches.add(type.cast(view));
                }
            } else if (component instanceof Container container) {
                matches.addAll(descendants(container, type));
            }
        }
        return matches;
    }

    @FunctionalInterface
    private interface ThrowingSupplier<T> {
        T get() throws Exception;
    }
}
