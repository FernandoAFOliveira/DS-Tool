package com.fernando.ds.application;

import java.util.Objects;

import com.fernando.ds.knowledge.StructureId;

/**
 * Resumable in-memory state for the Flash Cards activity.
 *
 * @param currentStructureId concept shown by the current card
 * @param answerRevealed whether the current answer is visible
 */
public record FlashCardSession(
    StructureId currentStructureId,
    boolean answerRevealed
) {

    public FlashCardSession {
        Objects.requireNonNull(currentStructureId, "currentStructureId");
    }
}
