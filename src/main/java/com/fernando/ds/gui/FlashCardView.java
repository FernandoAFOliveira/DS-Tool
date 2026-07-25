package com.fernando.ds.gui;

import com.fernando.ds.application.FlashCardContent;

/** Presentation boundary used by the display-independent Flash Cards controller. */
interface FlashCardView {

    void showCard(
        FlashCardContent content,
        int position,
        int total,
        int reviewed,
        boolean answerRevealed,
        boolean hasPrevious,
        boolean hasNext
    );

    void applyTheme(Theme theme);
}
