package com.fernando.ds.subject;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Resolves the configured provider for each subject known to the application.
 */
public final class SubjectProviderRegistry {

    private final Map<SubjectId, SubjectProvider> providers;

    /**
     * Creates a registry with one provider for each configured subject.
     *
     * @param providers providers to register
     */
    public SubjectProviderRegistry(Collection<SubjectProvider> providers) {
        Objects.requireNonNull(providers, "providers");
        Map<SubjectId, SubjectProvider> configured =
            new EnumMap<>(SubjectId.class);

        for (SubjectProvider provider : providers) {
            Objects.requireNonNull(provider, "provider");
            SubjectProvider previous = configured.put(provider.id(), provider);
            if (previous != null) {
                throw new IllegalArgumentException(
                    "Duplicate subject provider: " + provider.id()
                );
            }
        }

        this.providers = Map.copyOf(configured);
    }

    /**
     * Resolves a configured provider.
     *
     * @param id subject to resolve
     * @return its configured provider
     */
    public SubjectProvider get(SubjectId id) {
        Objects.requireNonNull(id, "id");
        SubjectProvider provider = providers.get(id);
        if (provider == null) {
            throw new IllegalArgumentException(
                "No subject provider configured for: " + id
            );
        }
        return provider;
    }

    /** @return configured providers in stable subject-identifier order */
    public List<SubjectProvider> getAll() {
        return providers.values().stream()
            .sorted((left, right) ->
                Integer.compare(left.id().ordinal(), right.id().ordinal())
            )
            .toList();
    }

    /** @return enabled providers in stable subject-identifier order */
    public List<SubjectProvider> getEnabled() {
        return getAll().stream()
            .filter(SubjectProvider::isEnabled)
            .toList();
    }
}
