package com.fernando.ds.model;

import java.util.Objects;

import com.fernando.ds.knowledge.DataStructureKnowledge;
import com.fernando.ds.knowledge.KnowledgeCatalog;
import com.fernando.ds.knowledge.StructureId;

/**
 * Java-specific representation of an abstract data-structure concept.
 *
 * <p>Recommendation capabilities and costs belong to the Knowledge Core. This
 * model retains the established Java name and presentation identity used by
 * the desktop application.</p>
 */
public abstract class DataStructure implements Comparable<DataStructure> {

    private final StructureId structureId;
    private final String name;
    private final boolean legacy;
    private double lastCalculatedScore;

    protected DataStructure(
        StructureId structureId,
        String name,
        boolean legacy
    ) {
        this.structureId = Objects.requireNonNull(structureId, "structureId");
        this.name = Objects.requireNonNull(name, "name");
        this.legacy = legacy;
    }

    /** @return the language-neutral concept represented by this Java type */
    public StructureId getStructureId() {
        return structureId;
    }

    public String getDisplayName() {
        return name;
    }

    /**
     * @deprecated scores now belong to abstract recommendation results
     */
    @Deprecated
    public double getLastCalculatedScore() {
        return lastCalculatedScore;
    }

    /**
     * @deprecated scores now belong to abstract recommendation results
     */
    @Deprecated
    public void setLastCalculatedScore(double score) {
        lastCalculatedScore = score;
    }

    public String getName() {
        return name;
    }

    public boolean isLegacy() {
        return legacy;
    }

    /**
     * @deprecated use Knowledge Core data for recommendation behavior
     */
    @Deprecated
    public boolean isDuplicates() {
        return knowledge().allowsDuplicates();
    }

    /**
     * @deprecated use Knowledge Core data for recommendation behavior
     */
    @Deprecated
    public boolean isKeys() {
        return knowledge().keyValueMapping();
    }

    /**
     * @deprecated use Knowledge Core data for recommendation behavior
     */
    @Deprecated
    public boolean isIndexed() {
        return knowledge().indexedAccess();
    }

    /**
     * @deprecated use Knowledge Core data for recommendation behavior
     */
    @Deprecated
    public RemovalOrder getRemovalOrder() {
        return knowledge().removalOrder();
    }

    /**
     * @deprecated use Knowledge Core data for recommendation behavior
     */
    @Deprecated
    public int getLookup() {
        return knowledge().lookupRating();
    }

    /**
     * @deprecated use Knowledge Core data for recommendation behavior
     */
    @Deprecated
    public int getAddDelete() {
        return knowledge().insertionRemovalRating();
    }

    /**
     * @deprecated use Knowledge Core data for recommendation behavior
     */
    @Deprecated
    public int getMemory() {
        return knowledge().memoryRating();
    }

    /**
     * @deprecated use Knowledge Core data for recommendation behavior
     */
    @Deprecated
    public int getSorted() {
        return knowledge().isSorted() ? 10 : 0;
    }

    /**
     * @deprecated recommendation ordering now belongs to
     *             {@code RecommendationService}
     */
    @Override
    @Deprecated
    public int compareTo(DataStructure other) {
        int scoreCompare = Double.compare(
            other.lastCalculatedScore,
            lastCalculatedScore
        );
        return scoreCompare != 0
            ? scoreCompare
            : name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name;
    }

    private DataStructureKnowledge knowledge() {
        return KnowledgeCatalog.get(structureId);
    }
}
