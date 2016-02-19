package ru.unn.agile.MarksAccounting.viewmodel;

import ru.unn.agile.MarksAccounting.model.*;
import javax.swing.*;
import java.text.ParseException;

public class DeleteMarkDialogViewModel extends DeletingDialogViewModel {
    public DeleteMarkDialogViewModel(final TableOfMarks currentTableOfMarks,
                                     final ILogger dialogLogger) {
        setLogger(dialogLogger);
        if (dialogLogger == null) {
            throw new IllegalArgumentException("Logger can't be null");
        }
        setDialogDateTextBoxVisible(true);
        setDialogGroupBoxVisible(true);
        setDialogStudentBoxVisible(true);
        setDialogSubjectBoxVisible(true);
        setDialogInputTextBoxVisible(false);
        setDialogType(DialogType.DELETE_MARK);
        setTableOfMarks(currentTableOfMarks);
        setDialogFieldsByDefault();
    }

    @Override
    public ComboBoxModel<String> getDialogGroupsComboBoxModel() {
        String[] groupNumbers = getTableOfMarks().getGroupsWhereMarksExistAsArrayOfStrings();
        setDialogGroup(groupNumbers[0]);
        return new JComboBox<String>(groupNumbers).getModel();
    }

    @Override
    public ComboBoxModel<String> getDialogStudentsComboBoxModel() {
        String[] studentNames = getTableOfMarks().getStudentsWhereMarksExistAsArrayOfStrings(
                new Group(getDialogGroup()));
        setDialogStudent(studentNames[0]);
        return new JComboBox<String>(studentNames).getModel();
    }

    @Override
    public ComboBoxModel<String> getDialogSubjectsComboBoxModel() {
        String[] subjects = getTableOfMarks().getAcademicSubjectsWhereMarksExistAsArray(
                new Group(getDialogGroup()), new Student(getDialogStudent()));
        setDialogStudent(subjects[0]);
        return new JComboBox<String>(subjects).getModel();
    }

    @Override
    protected void deleteObjectFromTableOfMarks() throws ParseException {
        logTriedChangingTable();
        getTableOfMarks().deleteMark(new Group(getDialogGroup()),
                new Student(getDialogStudent()), getDialogSubject(),
                DateParser.parseDate(getDialogDate()));
        logCompletedChangingTable();
    }

    @Override
    protected void logTriedChangingTable() {
        getLogger().log(LogMessage.TRIED_CHANGING + "to delete mark.");
    }

    @Override
    protected void logCompletedChangingTable() {
        getLogger().log("Deleting mark on date " + getDialogDate() + " from subject "
                + getDialogSubject() + " and student " + getDialogStudent() + " of group "
                + getDialogGroup() + LogMessage.COMPLETED_CHANGING);
    }

    @Override
    public void logCancelledChangingTable() {
        getLogger().log("Deleting mark" + LogMessage.CANCELLED_CHANGING);
    }
}
