package com.fernando.ds.library;

public class QuestionInfo {

    private final String shortText;
    private final String longText;
    private final QuestionType type;
    private final QuestionId id;

    public QuestionInfo(String shortText, String longText, QuestionType type, QuestionId id) {
        this.shortText = shortText;
        this.longText = longText;
        this.type = type;
        this.id = id;   
    }

    public enum QuestionId {
        KEY_VALUE,
        DUPLICATES,
        SORTED,
        LOOKUP,
        ADD_DELETE,
        MEMORY
    }

    public enum QuestionType {
        YES_NO,
        SCALE
    }

    public String getShortText() {
        return shortText;
    }

    public String getLongText() {
        return longText;
    }

    public QuestionType getType() {
        return type;
    }

    public QuestionId getId() {
        return id;
    }
}