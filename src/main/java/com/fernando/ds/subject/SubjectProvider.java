package com.fernando.ds.subject;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.model.DataStructure;

/**
 * Supplies language-specific representations for abstract data-structure
 * concepts.
 *
 * <p>Providers contain no recommendation, Swing, or JavaFX behavior.</p>
 */
public interface SubjectProvider {

    /** @return the stable application identifier for this subject */
    SubjectId id();

    /** @return the user-visible subject name */
    String displayName();

    /** @return whether this provider can currently supply a working subject */
    boolean isEnabled();

    /**
     * Returns fresh structure representations for one recommendation pass.
     *
     * @return structures supported by this subject, or an empty list when the
     *         subject is not enabled
     */
    List<DataStructure> getDataStructures();

    /**
     * Resolves this subject's representation of an abstract structure.
     *
     * @param structureId abstract structure to resolve
     * @return the representation, or empty when this subject does not supply it
     */
    default Optional<DataStructure> getRepresentation(
        StructureId structureId
    ) {
        Objects.requireNonNull(structureId, "structureId");
        return getDataStructures().stream()
            .filter(structure ->
                structure.getStructureId() == structureId
            )
            .findFirst();
    }
}
