package com.fernando.ds.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fernando.ds.knowledge.KnowledgeCatalog;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.subject.CSubjectProvider;
import com.fernando.ds.subject.JavaSubjectProvider;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectProvider;

class ExplorerServiceTest {

    private final ExplorerService service = new ExplorerService();
    private final JavaSubjectProvider javaProvider = new JavaSubjectProvider();
    private final CSubjectProvider cProvider = new CSubjectProvider();

    @Test
    void presentsTheCompleteKnowledgeCatalogInStableOrder() {
        assertEquals(
            KnowledgeCatalog.getAll().stream()
                .map(knowledge -> knowledge.id())
                .toList(),
            service.getAvailableStructures(javaProvider).stream()
                .map(structure -> structure.getStructureId())
                .toList()
        );
        assertEquals(9, service.getAvailableStructures(javaProvider).size());
    }

    @Test
    void discoversEnabledSubjectsInProvidedRegistryOrder() {
        assertEquals(
            List.of(
                new ExplorerSubject(SubjectId.JAVA, "Java"),
                new ExplorerSubject(SubjectId.C, "C")
            ),
            service.getSubjects(List.of(javaProvider, cProvider))
        );
    }

    @Test
    void combinesNeutralOverviewWithEveryEnabledSubject() {
        ExplorerContent content = service.getContent(
            List.of(javaProvider, cProvider),
            StructureId.HASH_MAP
        );

        assertEquals(
            KnowledgeCatalog.get(StructureId.HASH_MAP),
            content.knowledge()
        );
        assertEquals(
            KnowledgeCatalog.get(StructureId.HASH_MAP).relatedStructures(),
            content.relatedStructures().stream()
                .map(related -> related.id())
                .toList()
        );
        assertEquals(
            List.of(SubjectId.JAVA, SubjectId.C),
            content.subjectContents().stream()
                .map(subject -> subject.subjectId())
                .toList()
        );
        assertEquals(
            List.of("HashMap", "Hash table"),
            content.subjectContents().stream()
                .map(subject -> subject.representation()
                    .orElseThrow()
                    .getDisplayName())
                .toList()
        );
        assertTrue(content.subjectContents().stream()
            .allMatch(subject -> subject.educationalContent().isPresent()));
        assertTrue(content.subjectContents().stream()
            .flatMap(subject -> subject.representation().stream())
            .allMatch(representation ->
                representation.getStructureId() == content.knowledge().id()
            ));
    }

    @Test
    void overviewIsIdenticalRegardlessOfActiveListProvider() {
        ExplorerContent whileJavaActive = service.getContent(
            List.of(javaProvider, cProvider),
            StructureId.QUEUE
        );
        service.getAvailableStructures(javaProvider);
        ExplorerContent whileCActive = service.getContent(
            List.of(javaProvider, cProvider),
            StructureId.QUEUE
        );
        service.getAvailableStructures(cProvider);

        assertEquals(whileJavaActive.knowledge(), whileCActive.knowledge());
        assertEquals(
            whileJavaActive.relatedStructures(),
            whileCActive.relatedStructures()
        );
    }

    @Test
    void rejectsDisabledProvidersAsExplorerSubjects() {
        SubjectProvider disabled = new SubjectProvider() {
            @Override
            public SubjectId id() {
                return SubjectId.PYTHON;
            }

            @Override
            public String displayName() {
                return "Python";
            }

            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public List<com.fernando.ds.model.DataStructure>
                getDataStructures() {
                return List.of();
            }
        };

        org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> service.getSubjects(List.of(javaProvider, disabled))
        );
    }
}
