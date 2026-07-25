package com.fernando.ds.subject;

import java.util.List;

import com.fernando.ds.library.DataStructureLibrary;
import com.fernando.ds.model.DataStructure;

/**
 * Supplies the existing Java data-structure representations.
 */
public final class JavaSubjectProvider implements SubjectProvider {

    @Override
    public SubjectId id() {
        return SubjectId.JAVA;
    }

    @Override
    public String displayName() {
        return "Java";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public List<DataStructure> getDataStructures() {
        return DataStructureLibrary.getAll();
    }
}
