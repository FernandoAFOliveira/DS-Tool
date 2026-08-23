package com.fernando.ds.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fernando.ds.diagram.DiagramCatalog;
import com.fernando.ds.diagram.DiagramDescriptor;
import com.fernando.ds.diagram.DiagramId;
import com.fernando.ds.gui.Theme;
import com.fernando.ds.knowledge.StructureId;

/** Loads language-neutral Mermaid templates and applies semantic theme colors. */
public final class DiagramTemplateLoader {

    private static final String TEMPLATE_ROOT = "/diagrams/templates/";
    private static final String SHARED_INIT =
        loadRequiredResource("/diagrams/shared-init.mmd.fragment");
    private static final String SHARED_STYLES =
        loadRequiredResource("/diagrams/shared-styles.mmd.fragment");

    private DiagramTemplateLoader() {
    }

    /**
     * Loads a standalone shared diagram such as the welcome diagram.
     *
     * @param diagramName language-neutral resource basename
     * @param theme active diagram theme
     * @return processed Mermaid content
     */
    public static MermaidResult getProcessedMermaid(
        String diagramName,
        Theme theme
    ) {
        return processResource(
            Objects.requireNonNull(diagramName, "diagramName"),
            theme
        );
    }

    /** Loads the required primary diagram for an abstract structure. */
    public static MermaidResult getProcessedMermaid(
        StructureId structureId,
        Theme theme
    ) {
        return processDescriptor(
            DiagramCatalog.primary(structureId),
            theme
        );
    }

    /**
     * Loads a requested concept view, safely falling back to its primary
     * diagram when an optional view is absent or invalid.
     */
    public static MermaidResult getProcessedMermaid(
        StructureId structureId,
        DiagramId diagramId,
        Theme theme
    ) {
        return getProcessedMermaid(
            DiagramCatalog.get(structureId),
            diagramId,
            theme
        );
    }

    static MermaidResult getProcessedMermaid(
        List<DiagramDescriptor> available,
        DiagramId requestedId,
        Theme theme
    ) {
        Objects.requireNonNull(available, "available");
        Objects.requireNonNull(requestedId, "requestedId");
        if (available.isEmpty()
            || available.getFirst().id() != DiagramId.STRUCTURE) {
            throw new IllegalArgumentException(
                "Diagram list must begin with a primary structure view"
            );
        }

        DiagramDescriptor primary = available.getFirst();
        DiagramDescriptor requested = available.stream()
            .filter(diagram -> diagram.id() == requestedId)
            .findFirst()
            .orElse(primary);
        try {
            return processDescriptor(requested, theme);
        } catch (IllegalArgumentException exception) {
            if (requested == primary) {
                throw exception;
            }
            return processDescriptor(primary, theme);
        }
    }

    private static MermaidResult processDescriptor(
        DiagramDescriptor descriptor,
        Theme theme
    ) {
        return processResource(descriptor.resourceName(), theme);
    }

    private static MermaidResult processResource(
        String diagramName,
        Theme theme
    ) {
        Objects.requireNonNull(theme, "theme");
        String path = TEMPLATE_ROOT
            + diagramName.toLowerCase()
            + ".mmd.template";
        String body = loadResource(path);
        if (body == null) {
            throw new IllegalArgumentException(
                "Diagram template not found: " + diagramName
            );
        }

        Map<String, String> themeMap = loadThemeAsMap(theme);
        String template = SHARED_INIT + System.lineSeparator()
            + body + System.lineSeparator() + SHARED_STYLES;
        String mermaid = performSubstitution(template, themeMap);
        return new MermaidResult(
            mermaid,
            themeMap.getOrDefault("background", "#FFFFFF")
        );
    }

    private static String loadRequiredResource(String path) {
        String resource = loadResource(path);
        if (resource == null) {
            throw new IllegalStateException(
                "Required diagram resource not found: " + path
            );
        }
        return resource;
    }

    private static String loadResource(String path) {
        try (InputStream inputStream =
            DiagramTemplateLoader.class.getResourceAsStream(path)) {
            return inputStream == null
                ? null
                : new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
                );
        } catch (IOException exception) {
            return null;
        }
    }

    private static String performSubstitution(
        String template,
        Map<String, String> themeValues
    ) {
        String processed = template;
        for (Map.Entry<String, String> entry : themeValues.entrySet()) {
            String key = "{{" + entry.getKey().trim() + "}}";
            processed = processed.replace(key, entry.getValue().trim());
        }
        return processed.replaceAll("\\{\\{.*?\\}\\}", "#CCCCCC");
    }

    private static Map<String, String> loadThemeAsMap(Theme theme) {
        String path = "/diagrams/themes/"
            + theme.name().toLowerCase()
            + ".json";
        String jsonContent = loadResource(path);
        if (jsonContent == null) {
            throw new IllegalArgumentException(
                "Diagram theme not found: " + theme
            );
        }

        Map<String, String> themeMap = new HashMap<>();
        String cleanJson = jsonContent.replaceAll("[{}\"]", "");
        for (String pair : cleanJson.split(",")) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                themeMap.put(
                    keyValue[0].trim(),
                    keyValue[1].trim()
                );
            }
        }
        return themeMap;
    }
}
