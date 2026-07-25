package com.fernando.ds.knowledge;

import java.util.List;
import java.util.Objects;

import com.fernando.ds.model.RemovalOrder;

/**
 * Immutable, programming-language-neutral knowledge about one data-structure
 * concept.
 *
 * <p>The numeric ratings preserve the Advisor's existing relative ranking.
 * The cost and characteristic text supply the conceptual knowledge used by
 * present and future experiences.</p>
 *
 * @param id stable language-neutral identifier
 * @param displayName language-neutral concept name
 * @param keyValueMapping whether the concept maps unique keys to values
 * @param allowsDuplicates whether stored values may repeat
 * @param indexedAccess whether efficient positional access is characteristic
 * @param ordering conceptual ordering guarantee
 * @param removalOrder removal behavior used by the Advisor filter
 * @param lookupRating established relative lookup rating
 * @param insertionRemovalRating established relative update rating
 * @param memoryRating established relative memory rating
 * @param lookupCost conceptual lookup-cost description
 * @param insertionRemovalCost conceptual update-cost description
 * @param memoryConsiderations conceptual memory characteristics
 * @param iterationCharacteristics conceptual iteration behavior
 * @param commonUseCases common language-neutral uses
 * @param relatedStructures related abstract concepts
 */
public record DataStructureKnowledge(
    StructureId id,
    String displayName,
    boolean keyValueMapping,
    boolean allowsDuplicates,
    boolean indexedAccess,
    Ordering ordering,
    RemovalOrder removalOrder,
    int lookupRating,
    int insertionRemovalRating,
    int memoryRating,
    String lookupCost,
    String insertionRemovalCost,
    String memoryConsiderations,
    String iterationCharacteristics,
    List<String> commonUseCases,
    List<StructureId> relatedStructures
) {

    public DataStructureKnowledge {
        Objects.requireNonNull(id, "id");
        displayName = requireText(displayName, "displayName");
        Objects.requireNonNull(ordering, "ordering");
        Objects.requireNonNull(removalOrder, "removalOrder");
        requireRating(lookupRating, "lookupRating");
        requireRating(insertionRemovalRating, "insertionRemovalRating");
        requireRating(memoryRating, "memoryRating");
        lookupCost = requireText(lookupCost, "lookupCost");
        insertionRemovalCost = requireText(
            insertionRemovalCost,
            "insertionRemovalCost"
        );
        memoryConsiderations = requireText(
            memoryConsiderations,
            "memoryConsiderations"
        );
        iterationCharacteristics = requireText(
            iterationCharacteristics,
            "iterationCharacteristics"
        );
        commonUseCases = List.copyOf(
            Objects.requireNonNull(commonUseCases, "commonUseCases")
        );
        relatedStructures = List.copyOf(
            Objects.requireNonNull(relatedStructures, "relatedStructures")
        );

        if (commonUseCases.isEmpty()) {
            throw new IllegalArgumentException(
                "commonUseCases must not be empty"
            );
        }
        if (commonUseCases.stream().anyMatch(value ->
            value == null || value.isBlank())) {
            throw new IllegalArgumentException(
                "commonUseCases must contain non-blank text"
            );
        }
        if (relatedStructures.contains(id)) {
            throw new IllegalArgumentException(
                "A structure cannot be related to itself"
            );
        }
    }

    /** @return whether this concept maintains sorted order */
    public boolean isSorted() {
        return ordering == Ordering.SORTED;
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }

    private static void requireRating(int value, String name) {
        if (value < 0 || value > 10) {
            throw new IllegalArgumentException(
                name + " must be between 0 and 10"
            );
        }
    }
}
