package com.fernando.ds.application;

import java.util.Objects;

import com.fernando.ds.subject.SubjectId;

/**
 * Enabled subject descriptor used to configure Explorer detail tabs.
 *
 * @param id stable subject identity
 * @param displayName user-visible tab label
 */
public record ExplorerSubject(SubjectId id, String displayName) {

    public ExplorerSubject {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(displayName, "displayName");
        if (displayName.isBlank()) {
            throw new IllegalArgumentException(
                "displayName must not be blank"
            );
        }
    }
}
