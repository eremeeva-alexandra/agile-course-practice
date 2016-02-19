package ru.unn.agile.MarksAccounting.viewmodel;

import ru.unn.agile.MarksAccounting.model.*;
import javax.swing.*;
import java.text.ParseException;

public class AddStudentDialogViewModel extends AddingDialogViewModel {
    public AddStudentDialogViewModel(final TableOfMarks currentTableOfMarks,
                                     final ILogger dialogLogger) {
        setLogger(dialogLogger);
        if (dialogLogger == null) {
            throw new IllegalArgumentException("Logger can't be null");
        }
        setDialogDateTextBoxVisible(false);
        setDialogGroupBoxVisible(true);
        setDialogStudentBoxVisible(false);
        setDialogSubjectBoxVisible(false);
        setDialogInputTextBoxVisible(true);
        setDialogType(DialogType.ADD_STUDENT);
        setTableOfMarks(currentTableOfMarks);
        setDialogFieldsByDefault();
    }

    @Override
    public ComboBoxModel<String> getDialogGroupsComboBoxModel() {
        return getComboBoxModelOfAllGroups();
    }

    @Override
    protected void addObjectToTableOfMarks() throws ParseException {
        logTriedChangingTable();
        getTableOfMarks().addStudent(new Group(getDialogGroup()),
                new Student(getDialogInputTextBox()));
        logCompletedChangingTable();
    }

    @Override
    protected void logTriedChangingTable() {
        getLogger().log(LogMessage.TRIED_CHANGING + "to add student.");
    }

    @Override
    protected void logCompletedChangingTable() {
        getLogger().log("Adding student " + getDialogInputTextBox()
                + " to group " + getDialogGroup() + LogMessage.COMPLETED_CHANGING);
    }

    @Override
    public void logCancelledChangingTable() {
        getLogger().log("Adding student" + LogMessage.CANCELLED_CHANGING);
    }
}
