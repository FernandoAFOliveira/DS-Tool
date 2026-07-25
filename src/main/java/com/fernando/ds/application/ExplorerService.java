package com.fernando.ds.application;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fernando.ds.application.ExplorerContent.RelatedStructure;
import com.fernando.ds.knowledge.KnowledgeCatalog;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.subject.SubjectProvider;

/**
 * Provides complete Knowledge Core content through an active subject provider.
 */
public final class ExplorerService {

    /**
     * Lists every Knowledge Core structure represented by the active subject.
     *
     * @param provider active subject provider
     * @return representations in stable Knowledge Core order
     */
    public List<DataStructure> getAvailableStructures(
        SubjectProvider provider
    ) {
        Objects.requireNonNull(provider, "provider");
        return KnowledgeCatalog.getAll().stream()
            .map(knowledge -> provider.getRepresentation(knowledge.id()))
            .flatMap(Optional::stream)
            .toList();
    }

    /**
     * Resolves complete Explorer content for one abstract structure.
     *
     * @param provider active subject provider
     * @param structureId selected abstract structure
     * @return content, or empty when the subject has no representation
     */
    public Optional<ExplorerContent> getContent(
        SubjectProvider provider,
        StructureId structureId
    ) {
        Objects.requireNonNull(provider, "provider");
        Objects.requireNonNull(structureId, "structureId");

        return provider.getRepresentation(structureId)
            .map(representation -> new ExplorerContent(
                provider.displayName(),
                KnowledgeCatalog.get(structureId),
                representation,
                KnowledgeCatalog.get(structureId).relatedStructures().stream()
                    .map(relatedId -> new RelatedStructure(
                        KnowledgeCatalog.get(relatedId),
                        provider.getRepresentation(relatedId)
                    ))
                    .toList()
            ));
    }
}
