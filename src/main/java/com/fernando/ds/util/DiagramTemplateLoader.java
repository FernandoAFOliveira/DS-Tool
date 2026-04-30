package com.fernando.ds.util;

import com.fernando.ds.gui.Theme;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

public final class DiagramTemplateLoader {

    // Private constructor to prevent instantiation
    private DiagramTemplateLoader() {}

    public static MermaidResult getProcessedMermaid(String diagramName, Theme theme) {

        String template = loadResource("/diagrams/templates/" + diagramName.toLowerCase() + ".mmd.template");
        if (template == null) {
            throw new IllegalArgumentException(
                "Diagram template not found: " + diagramName +
                " (expected at /diagrams/templates/" + diagramName.toLowerCase() + ".mmd.template)"
            );
        }

        Map<String, String> themeMap = loadThemeAsMap(theme);
        String mmd = performSubstitution(template, themeMap);
        return new MermaidResult(mmd, themeMap.getOrDefault("background", "#FFFFFF"));
    }

        
    // Keep helper methods private if they aren't used elsewhere
    private static String loadResource(String path) { 
        try (InputStream inputStream = DiagramTemplateLoader.class.getResourceAsStream(path)) {
            return inputStream != null ? new String(inputStream.readAllBytes()) : null;
        } catch (IOException e) {
            return null;
        }
    }

    private static String performSubstitution(String template, Map<String, String> themeValues) {
        String processed = template;
        for (Map.Entry<String, String> entry : themeValues.entrySet()) {
            String key = "{{" + entry.getKey().trim() + "}}";
            processed = processed.replace(key, entry.getValue().trim());
        }
        
        return processed.replaceAll("\\{\\{.*?\\}\\}", "#CCCCCC");
    }


    private static Map<String, String> loadThemeAsMap(Theme theme) {
        Map<String, String> themeMap = new HashMap<>();
        
        // UPDATED: Now looks in /diagrams/themes/
        String path = "/diagrams/themes/" + theme.name().toLowerCase() + ".json";
        String jsonContent = loadResource(path);

        if (jsonContent != null) {
            // Clean the JSON string
            String cleanJson = jsonContent.replaceAll("[{}\"]", "");
            String[] pairs = cleanJson.split(",");

            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2); // Use limit 2 to handle colors with colons if any
                if (keyValue.length == 2) {
                    themeMap.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        } else {
            System.err.println("CRITICAL: Theme JSON not found at: " + path);
        }
        return themeMap;
    }
}