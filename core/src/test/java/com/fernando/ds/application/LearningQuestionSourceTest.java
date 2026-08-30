package com.fernando.ds.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fernando.ds.knowledge.KnowledgeCatalog;
import com.fernando.ds.knowledge.StructureId;

class LearningQuestionSourceTest {

    @Test
    void derivesOneConceptFirstQuestionPerKnowledgeEntryInStableOrder() {
        List<LearningQuestion> questions =
            new LearningQuestionSource().getAll();

        assertEquals(
            KnowledgeCatalog.getAll().stream()
                .map(knowledge -> knowledge.id())
                .toList(),
            questions.stream()
                .map(LearningQuestion::structureId)
                .toList()
        );
        assertEquals(9, questions.size());

        LearningQuestion hashMap = questions.stream()
            .filter(question -> question.structureId() == StructureId.HASH_MAP)
            .findFirst()
            .orElseThrow();
        assertEquals("What is a Hash map?", hashMap.prompt());
        assertEquals(
            KnowledgeCatalog.get(StructureId.HASH_MAP).description(),
            hashMap.answer()
        );
        assertFalse(hashMap.prompt().contains("HashMap"));
        assertFalse(hashMap.answer().contains("HashMap"));
    }

    @Test
    void returnsAnImmutableQuestionList() {
        assertThrows(
            UnsupportedOperationException.class,
            () -> new LearningQuestionSource().getAll().clear()
        );
    }
}
