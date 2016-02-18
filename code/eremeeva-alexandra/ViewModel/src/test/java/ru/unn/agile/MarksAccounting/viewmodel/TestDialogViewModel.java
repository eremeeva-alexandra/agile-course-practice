package ru.unn.agile.MarksAccounting.viewmodel;

import java.text.ParseException;

public class TestDialogViewModel extends DialogViewModel {
    public TestDialogViewModel(final ILogger dialogLogger) {
        setLogger(dialogLogger);
        if (dialogLogger == null) {
            throw new IllegalArgumentException("Logger can't be null");
        }
        setDialogDateTextBoxVisible(false);
        setDialogGroupBoxVisible(false);
        setDialogStudentBoxVisible(false);
        setDialogSubjectBoxVisible(false);
        setDialogInputTextBoxVisible(false);
        setDialogFieldsByDefault();
    }

    @Override
    public void changeTableOfMarks() throws ParseException { }
}
