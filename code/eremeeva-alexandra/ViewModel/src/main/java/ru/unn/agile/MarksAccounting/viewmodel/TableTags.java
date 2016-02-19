package ru.unn.agile.MarksAccounting.viewmodel;

public enum TableTags {
    GROUP("@Group"),
    END_OF_GROUP("@EndOfGroup"),
    SUBJECTS("@Subjects"),
    END_OF_SUBJECTS("@EndOfSubjects"),
    STUDENT("@Student"),
    END_OF_STUDENT("@EndOfStudent"),
    MARK("@Mark");

    private String tag;

    private TableTags(final String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
