package com.fernando.ds.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fernando.ds.knowledge.KnowledgeCatalog;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.subject.JavaSubjectProvider;

class ExplorerServiceTest {

    private final ExplorerService service = new ExplorerService();
    private final JavaSubjectProvider javaProvider = new JavaSubjectProvider();

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
    void combinesSharedKnowledgeWithActiveSubjectRepresentations() {
        ExplorerContent content = service.getContent(
            javaProvider,
            StructureId.HASH_MAP
        ).orElseThrow();

        assertEquals("Java", content.subjectDisplayName());
        assertEquals(
            KnowledgeCatalog.get(StructureId.HASH_MAP),
            content.knowledge()
        );
        assertEquals("HashMap", content.representation().getName());
        assertEquals(
            KnowledgeCatalog.get(StructureId.HASH_MAP).relatedStructures(),
            content.relatedStructures().stream()
                .map(related -> related.knowledge().id())
                .toList()
        );
        assertTrue(content.relatedStructures().stream()
            .allMatch(related -> related.representation().isPresent()));
    }
}
