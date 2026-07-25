package com.fernando.ds.gui;

import java.util.List;

import com.fernando.ds.application.ExplorerContent.SubjectContent;
import com.fernando.ds.subject.SubjectStructureContent;
import com.fernando.ds.subject.SubjectStructureContent.CodeExample;
import com.fernando.ds.subject.SubjectStructureContent.Section;

/** Converts plain provider content to safely escaped presentation HTML. */
final class SubjectContentHtmlRenderer {

    private SubjectContentHtmlRenderer() {
    }

    static String render(SubjectContent subject) {
        StringBuilder html = new StringBuilder();
        html.append("<h1>").append(escape(subject.subjectDisplayName()))
            .append("</h1>");
        subject.representation().ifPresentOrElse(
            representation -> html.append("<h2>Representation</h2><p>")
                .append(escape(representation.getDisplayName()))
                .append("</p>"),
            () -> html.append(
                "<p>No representation is available for this concept.</p>"
            )
        );
        subject.educationalContent().ifPresentOrElse(
            content -> appendContent(html, content),
            () -> html.append(
                "<p>No subject-specific guidance is available for this "
                    + "concept.</p>"
            )
        );
        return html.toString();
    }

    private static void appendContent(
        StringBuilder html,
        SubjectStructureContent content
    ) {
        for (Section section : content.sections()) {
            html.append("<h2>").append(escape(section.title()))
                .append("</h2>");
            section.paragraphs().forEach(paragraph ->
                html.append("<p>").append(escape(paragraph)).append("</p>")
            );
            appendList(html, section.bulletItems());
        }
        for (CodeExample example : content.codeExamples()) {
            html.append("<h2>").append(escape(example.title()))
                .append("</h2><pre><code>")
                .append(escape(example.source()))
                .append("</code></pre>");
        }
    }

    private static void appendList(
        StringBuilder html,
        List<String> items
    ) {
        if (items.isEmpty()) {
            return;
        }
        html.append("<ul>");
        items.forEach(item ->
            html.append("<li>").append(escape(item)).append("</li>")
        );
        html.append("</ul>");
    }

    static String escape(String value) {
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
}
