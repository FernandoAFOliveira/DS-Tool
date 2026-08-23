package com.fernando.ds.gui;

/** Displays provider-owned subject context without owning subject state. */
public interface SubjectContextView {

    /** Displays the provider's user-facing name as the current context. */
    void showSubjectContext(String subjectDisplayName);
}
