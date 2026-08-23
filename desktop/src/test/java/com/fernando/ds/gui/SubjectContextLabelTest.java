package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fernando.ds.application.ApplicationState;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.subject.CSubjectProvider;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProvider;
import com.fernando.ds.subject.SubjectProviderRegistry;

class SubjectContextLabelTest {

    @Test
    void displaysInitialAndSuccessfulSwitchesUsingProviderNames() {
        ApplicationState state = new ApplicationState();
        SubjectProviderRegistry registry = registry();
        SubjectSelectionController controller =
            new SubjectSelectionController(state, registry);
        SubjectContextLabel label =
            new SubjectContextLabel("Active subject: ");
        label.showSubject(
            registry.get(state.getActiveSubject()).displayName()
        );

        assertEquals("Active subject: Java", label.getText());
        selectAndUpdate(controller, registry, label, SubjectId.C);
        assertEquals("Active subject: C", label.getText());
        selectAndUpdate(controller, registry, label, SubjectId.JAVA);
        assertEquals("Active subject: Java", label.getText());
    }

    @Test
    void syntheticDisplayNameAndThemeRefreshPreserveReadableText() {
        SubjectContextLabel label =
            new SubjectContextLabel("Active subject: ");
        label.showSubject(syntheticProvider().displayName());

        ThemeManager.applyThemeToComponent(label, Theme.DARK_BLUE);

        assertEquals("Active subject: Synthetic Subject", label.getText());
        assertEquals(Theme.DARK_BLUE.getForeground(), label.getForeground());
    }

    @Test
    void contextLabelUsesTheDynamicProviderDisplayName() {
        SubjectContextLabel label = new SubjectContextLabel(
            "Subject context: "
        );

        label.showSubject(syntheticProvider().displayName());

        assertEquals(
            "Subject context: Synthetic Subject",
            label.getText()
        );
    }

    @Test
    void failedRenderingLeavesPreviousIndicatorAndStateUnchanged() {
        ApplicationState state = new ApplicationState();
        SubjectProviderRegistry registry = registry();
        SubjectSelectionController controller =
            new SubjectSelectionController(state, registry);
        SubjectContextLabel label =
            new SubjectContextLabel("Active subject: ");
        label.showSubject("Java");

        assertThrows(
            IllegalStateException.class,
            () -> controller.select(SubjectId.C, provider -> {
                throw new IllegalStateException("render failed");
            })
        );

        assertEquals(SubjectId.JAVA, state.getActiveSubject());
        assertEquals("Active subject: Java", label.getText());
    }

    private static void selectAndUpdate(
        SubjectSelectionController controller,
        SubjectProviderRegistry registry,
        SubjectContextLabel label,
        SubjectId id
    ) {
        if (controller.select(id, provider -> {
        })) {
            label.showSubject(registry.get(id).displayName());
        }
    }

    private static SubjectProviderRegistry registry() {
        return new SubjectProviderRegistry(List.of(
            new JavaSubjectProvider(),
            new CSubjectProvider()
        ));
    }

    private static SubjectProvider syntheticProvider() {
        return new SubjectProvider() {
            @Override
            public SubjectId id() {
                return SubjectId.PYTHON;
            }

            @Override
            public String displayName() {
                return "Synthetic Subject";
            }

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public List<DataStructure> getDataStructures() {
                return List.of();
            }
        };
    }
}
