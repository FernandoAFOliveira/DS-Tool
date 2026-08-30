package com.fernando.ds.subject;

import java.util.List;
import java.util.Objects;

import com.fernando.ds.model.DataStructure;

/**
 * Describes a planned subject that is intentionally unavailable.
 */
public final class UnavailableSubjectProvider implements SubjectProvider {

    private final SubjectId id;
    private final String displayName;

    /**
     * Creates an unavailable provider descriptor.
     *
     * @param id stable subject identifier
     * @param displayName user-visible subject name
     */
    public UnavailableSubjectProvider(SubjectId id, String displayName) {
        this.id = Objects.requireNonNull(id, "id");
        this.displayName = Objects.requireNonNull(displayName, "displayName");
    }

    @Override
    public SubjectId id() {
        return id;
    }

    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public List<DataStructure> getDataStructures() {
        return List.of();
    }
}
