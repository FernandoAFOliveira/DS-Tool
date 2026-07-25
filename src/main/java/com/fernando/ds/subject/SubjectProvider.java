package com.fernando.ds.subject;

import java.util.List;

import com.fernando.ds.model.DataStructure;

/**
 * Supplies the language-specific data-structure representations for a subject.
 *
 * <p>Providers contain no Swing or JavaFX behavior. Recommendation concepts
 * remain in the existing shared model until the Knowledge Core milestone.</p>
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
}
