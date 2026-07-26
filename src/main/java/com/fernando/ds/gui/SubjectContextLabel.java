package com.fernando.ds.gui;

import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

/** Accessible text label for provider-owned subject display names. */
final class SubjectContextLabel extends JLabel {

    private final String prefix;

    SubjectContextLabel(String prefix) {
        this.prefix = Objects.requireNonNull(prefix, "prefix");
        setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
    }

    void showSubject(String subjectDisplayName) {
        setText(prefix + Objects.requireNonNull(
            subjectDisplayName,
            "subjectDisplayName"
        ));
    }
}
