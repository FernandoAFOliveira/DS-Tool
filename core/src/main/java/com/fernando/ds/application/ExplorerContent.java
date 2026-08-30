package com.fernando.ds.application;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fernando.ds.knowledge.DataStructureKnowledge;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.subject.SubjectId;
import com.fernando.ds.subject.SubjectStructureContent;

/**
 * Language-neutral Overview and enabled-subject details for one Explorer page.
 *
 * @param knowledge language-neutral structure knowledge
 * @param relatedStructures related language-neutral concepts
 * @param subjectContents enabled subject content in stable provider order
 */
public record ExplorerContent(
    DataStructureKnowledge knowledge,
    List<DataStructureKnowledge> relatedStructures,
    List<SubjectContent> subjectContents
) {

    public ExplorerContent {
        Objects.requireNonNull(knowledge, "knowledge");
        relatedStructures = List.copyOf(
            Objects.requireNonNull(relatedStructures, "relatedStructures")
        );
        subjectContents = List.copyOf(
            Objects.requireNonNull(subjectContents, "subjectContents")
        );
    }

    /**
     * Subject-specific content for one enabled provider tab.
     *
     * @param subjectId stable subject identity
     * @param subjectDisplayName user-visible tab label
     * @param representation provider representation when available
     * @param educationalContent provider educational content when available
     */
    public record SubjectContent(
        SubjectId subjectId,
        String subjectDisplayName,
        Optional<DataStructure> representation,
        Optional<SubjectStructureContent> educationalContent
    ) {

        public SubjectContent {
            Objects.requireNonNull(subjectId, "subjectId");
            Objects.requireNonNull(
                subjectDisplayName,
                "subjectDisplayName"
            );
            if (subjectDisplayName.isBlank()) {
                throw new IllegalArgumentException(
                    "subjectDisplayName must not be blank"
                );
            }
            representation = Objects.requireNonNull(
                representation,
                "representation"
            );
            educationalContent = Objects.requireNonNull(
                educationalContent,
                "educationalContent"
            );
        }
    }
}
