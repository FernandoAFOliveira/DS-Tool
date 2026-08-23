package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fernando.ds.util.ContentLoader;

class DiagramPanelPresentationTest {

    @Test
    void selectorRowAppearsOnlyForMultipleDiagrams() {
        assertFalse(DiagramPanel.selectorVisibleFor(0));
        assertFalse(DiagramPanel.selectorVisibleFor(1));
        assertTrue(DiagramPanel.selectorVisibleFor(2));
    }

    @Test
    void sharedPageUsesResponsiveContainedCenteredSizing() {
        String page = ContentLoader.loadTextResource("/diagram.html");

        assertTrue(page.contains("viewport.clientWidth"));
        assertTrue(page.contains("viewport.clientHeight"));
        assertTrue(page.contains("Math.min("));
        assertTrue(page.contains(
            "preserveAspectRatio\", \"xMidYMid meet"
        ));
        assertTrue(page.contains("justify-content: center"));
        assertTrue(page.contains("align-items: center"));
        assertTrue(page.contains("new ResizeObserver("));
        assertTrue(page.contains(
            "requestAnimationFrame(fitDiagram)"
        ));
    }

    @Test
    void everyRenderAndResizeCanRecalculateFit() {
        String page = ContentLoader.loadTextResource("/diagram.html");

        assertTrue(page.contains(
            "window.addEventListener(\"resize\", fitDiagram)"
        ));
        assertTrue(page.contains(
            "() => window.requestAnimationFrame(fitDiagram)"
        ));
        assertTrue(page.contains("window.renderDiagram = function("));
    }

    @Test
    void conceptViewsHideBrandingAndUseSeparateWideHeading() {
        String page = ContentLoader.loadTextResource("/diagram.html");

        assertTrue(page.contains("#header.concept"));
        assertTrue(page.contains(
            "header.classList.toggle(\"concept\", !showBranding)"
        ));
        assertTrue(page.contains("id=\"concept-heading\""));
        assertTrue(page.contains("font-size: 28px"));
        assertTrue(page.contains("font-size: 18px"));
        assertFalse(page.contains("max-height: 85vh"));
    }
}
