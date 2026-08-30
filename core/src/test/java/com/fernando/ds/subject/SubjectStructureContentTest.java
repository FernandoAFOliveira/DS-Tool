package com.fernando.ds.subject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fernando.ds.subject.SubjectStructureContent.CodeExample;
import com.fernando.ds.subject.SubjectStructureContent.Section;

class SubjectStructureContentTest {

    @Test
    void validatesAndDefensivelyCopiesPlainContent() {
        List<String> paragraphs = new ArrayList<>(List.of("Prose"));
        List<Section> sections = new ArrayList<>(List.of(
            new Section("Notes", paragraphs, List.of("Item"))
        ));
        List<CodeExample> examples = new ArrayList<>(List.of(
            new CodeExample("Example", "value++;")
        ));

        SubjectStructureContent content =
            new SubjectStructureContent(sections, examples);
        paragraphs.add("Changed");
        sections.clear();
        examples.clear();

        assertEquals(List.of("Prose"), content.sections()
            .getFirst()
            .paragraphs());
        assertEquals(1, content.sections().size());
        assertEquals(1, content.codeExamples().size());
        assertThrows(
            UnsupportedOperationException.class,
            () -> content.sections().add(
                new Section("Other", List.of("Text"), List.of())
            )
        );
    }

    @Test
    void rejectsEmptyOrBlankContent() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new SubjectStructureContent(List.of(), List.of())
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new Section("Title", List.of(), List.of())
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new CodeExample("Example", " ")
        );
    }

    @Test
    void modelHasNoGuiOrResourcePathTypes() {
        for (Class<?> type : List.of(
            SubjectStructureContent.class,
            Section.class,
            CodeExample.class
        )) {
            assertTrue(type.isRecord());
            for (RecordComponent component : type.getRecordComponents()) {
                String typeName = component.getGenericType().getTypeName();
                assertTrue(!typeName.startsWith("java.awt."));
                assertTrue(!typeName.startsWith("javax.swing."));
                assertTrue(!typeName.startsWith("javafx."));
                assertTrue(!typeName.toLowerCase().contains("path"));
            }
        }
    }
}
