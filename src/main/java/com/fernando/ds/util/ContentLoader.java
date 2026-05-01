package com.fernando.ds.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.fernando.ds.gui.Theme;

public class ContentLoader {

    private static final Map<String, String> cache = new HashMap<>();

    private ContentLoader() {
        // Utility class
    }

    public static String loadThemedHtml(String htmlPath, Theme theme) {
        String body = loadTextResource(htmlPath);
        String css = loadTextResource(getCssPath(theme));
        return wrap(body, css);
    }

    public static String applyThemeToHtml(String htmlBody, Theme theme) {
        String css = loadTextResource(getCssPath(theme));
        return wrap(htmlBody, css);
    }

    private static String wrap(String body, String css) {
        return """
            <html>
            <head>
            <style>
            %s
            </style>
            </head>
            <body>
            %s
            </body>
            </html>
            """.formatted(css, body);
    }

    private static String getCssPath(Theme theme) {
        return switch (theme) {
            case LIGHT -> "/content/css/light.css";
            case SOFT_BLUE -> "/content/css/soft_blue.css";
            case DARK -> "/content/css/dark.css";
            case DARK_BLUE -> "/content/css/dark_blue.css";
        };
    }

    public static String loadTextResource(String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        try (InputStream is = ContentLoader.class.getResourceAsStream(path)) {

            if (is == null) {
                return "<html><body><h2>Missing content:</h2><p>" + path + "</p></body></html>";
            }

            String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            cache.put(path, text);
            return text;

        } catch (Exception e) {
            e.printStackTrace();
            return "<html><body><h2>Error loading content:</h2><p>" + path + "</p></body></html>";
        }
    }

    public static String loadQuestionHtml(String questionId) {
        String file = "/content/" + questionId.toLowerCase() + ".html";
        return loadTextResource(file);
    }

    public static String loadStructureHtml(String name) {
        String file = "/content/" + name.toLowerCase() + ".html";
        return loadTextResource(file);
    }
}