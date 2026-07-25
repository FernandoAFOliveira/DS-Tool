package com.fernando.ds.application;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fernando.ds.application.ExplorerContent.SubjectContent;
import com.fernando.ds.knowledge.DataStructureKnowledge;
import com.fernando.ds.knowledge.KnowledgeCatalog;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.subject.SubjectProvider;

/** Provides complete Knowledge Core and enabled-subject Explorer content. */
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
     * Converts enabled providers to stable Explorer tab descriptors.
     *
     * @param enabledProviders enabled providers in registry order
     * @return immutable tab descriptors
     */
    public List<ExplorerSubject> getSubjects(
        List<SubjectProvider> enabledProviders
    ) {
        return copyProviders(enabledProviders).stream()
            .map(provider -> new ExplorerSubject(
                provider.id(),
                provider.displayName()
            ))
            .toList();
    }

    /**
     * Resolves language-neutral Overview and all enabled-subject content.
     *
     * @param enabledProviders enabled providers in registry order
     * @param structureId selected abstract structure
     * @return complete Explorer page
     */
    public ExplorerContent getContent(
        List<SubjectProvider> enabledProviders,
        StructureId structureId
    ) {
        Objects.requireNonNull(structureId, "structureId");
        List<SubjectProvider> providers = copyProviders(enabledProviders);
        DataStructureKnowledge knowledge = KnowledgeCatalog.get(structureId);

        return new ExplorerContent(
            knowledge,
            knowledge.relatedStructures().stream()
                .map(KnowledgeCatalog::get)
                .toList(),
            providers.stream()
                .map(provider -> new SubjectContent(
                    provider.id(),
                    provider.displayName(),
                    provider.getRepresentation(structureId),
                    provider.getEducationalContent(structureId)
                ))
                .toList()
        );
    }

    private static List<SubjectProvider> copyProviders(
        List<SubjectProvider> providers
    ) {
        List<SubjectProvider> copy = List.copyOf(
            Objects.requireNonNull(providers, "enabledProviders")
        );
        if (copy.stream().anyMatch(provider -> !provider.isEnabled())) {
            throw new IllegalArgumentException(
                "Explorer subjects must be enabled providers"
            );
        }
        return copy;
    }
}
