package com.fernando.ds.application;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fernando.ds.knowledge.DataStructureKnowledge;
import com.fernando.ds.model.DataStructure;

/**
 * Shared knowledge and active-subject representation for one Explorer view.
 *
 * @param subjectDisplayName active subject's user-visible name
 * @param knowledge language-neutral structure knowledge
 * @param representation active-subject representation
 * @param relatedStructures related concepts and any available representations
 */
public record ExplorerContent(
    String subjectDisplayName,
    DataStructureKnowledge knowledge,
    DataStructure representation,
    List<RelatedStructure> relatedStructures
) {

    public ExplorerContent {
        Objects.requireNonNull(subjectDisplayName, "subjectDisplayName");
        if (subjectDisplayName.isBlank()) {
            throw new IllegalArgumentException(
                "subjectDisplayName must not be blank"
            );
        }
        Objects.requireNonNull(knowledge, "knowledge");
        Objects.requireNonNull(representation, "representation");
        relatedStructures = List.copyOf(
            Objects.requireNonNull(relatedStructures, "relatedStructures")
        );
    }

    /**
     * One related abstract structure and its optional active-subject name.
     *
     * @param knowledge related language-neutral knowledge
     * @param representation active-subject representation when available
     */
    public record RelatedStructure(
        DataStructureKnowledge knowledge,
        Optional<DataStructure> representation
    ) {

        public RelatedStructure {
            Objects.requireNonNull(knowledge, "knowledge");
            representation = Objects.requireNonNull(
                representation,
                "representation"
            );
        }
    }
}
