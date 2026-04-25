package com.fernando.ds.library;

public class QuestionLibrary {

    public static final QuestionInfo[] QUESTIONS = {
        new QuestionInfo(
            "Need key-value mapping?",
            "Choose yes if each item should be connected to another value. For example, use key-value mapping when storing a student name with a grade, a word with its count, or an ID number with a person.",
            QuestionInfo.QuestionType.YES_NO,
            QuestionInfo.QuestionId.KEY_VALUE
        ),
        new QuestionInfo(
            "Allow duplicate elements?",
            "Choose yes if the same value may appear more than once. Choose no if each value should appear only once.",
            QuestionInfo.QuestionType.YES_NO,
            QuestionInfo.QuestionId.DUPLICATES
        ),
        new QuestionInfo(
            "Need sorted order?",
            "Choose yes if the structure should keep values in order or support ordered searches such as finding the next higher, next lower, first, or last value.",
            QuestionInfo.QuestionType.YES_NO,
            QuestionInfo.QuestionId.SORTED
        ),
        new QuestionInfo(
            "Lookup speed",
            "This measures how important it is to find an item quickly. Higher values favor structures like HashMap and HashSet.",
            QuestionInfo.QuestionType.SCALE,
            QuestionInfo.QuestionId.LOOKUP
        ),
        new QuestionInfo(
            "Add/Delete speed",
            "This measures how important fast insertion and removal are. Higher values favor structures that handle updates efficiently.",
            QuestionInfo.QuestionType.SCALE,
            QuestionInfo.QuestionId.ADD_DELETE
        ),
        new QuestionInfo(
            "Memory efficiency",
            "This measures how important lower memory usage is compared to speed or ordering features.",
            QuestionInfo.QuestionType.SCALE,
            QuestionInfo.QuestionId.MEMORY
        )
    };

    public static QuestionInfo[] getAll() {
        return QUESTIONS;
    }

    private QuestionLibrary() {
        // Prevent object creation.
    }
}