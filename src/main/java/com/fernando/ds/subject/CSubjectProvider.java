package com.fernando.ds.subject;

import java.util.List;

import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.model.DataStructure;

/**
 * Supplies educational C implementation strategies for the shared
 * data-structure concepts.
 *
 * <p>The displayed names describe common approaches in C. They are not names
 * from a standardized C collections framework.</p>
 */
public final class CSubjectProvider implements SubjectProvider {

    @Override
    public SubjectId id() {
        return SubjectId.C;
    }

    @Override
    public String displayName() {
        return "C";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public List<DataStructure> getDataStructures() {
        return List.of(
            representation(StructureId.DYNAMIC_ARRAY, "Dynamic array"),
            representation(StructureId.STACK, "Array-backed stack"),
            representation(StructureId.QUEUE, "Circular-buffer queue"),
            representation(StructureId.PRIORITY_QUEUE, "Binary heap"),
            representation(StructureId.DEQUE, "Circular-buffer deque"),
            representation(StructureId.HASH_SET, "Hash-table set"),
            representation(StructureId.ORDERED_SET, "Balanced-tree set"),
            representation(StructureId.HASH_MAP, "Hash table"),
            representation(StructureId.ORDERED_MAP, "Balanced-tree map")
        );
    }

    private static DataStructure representation(
        StructureId structureId,
        String displayName
    ) {
        return new CRepresentation(structureId, displayName);
    }

    private static final class CRepresentation extends DataStructure {

        private CRepresentation(
            StructureId structureId,
            String displayName
        ) {
            super(structureId, displayName, false);
        }
    }
}
