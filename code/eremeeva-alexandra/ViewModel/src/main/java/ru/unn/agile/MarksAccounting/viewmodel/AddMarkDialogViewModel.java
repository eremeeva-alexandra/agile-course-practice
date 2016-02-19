package ru.unn.agile.MarksAccounting.viewmodel;

import ru.unn.agile.MarksAccounting.model.*;
import javax.swing.*;
import java.text.ParseException;

public class AddMarkDialogViewModel extends AddingDialogViewModel {
    public AddMarkDialogViewModel(final TableOfMarks currentTableOfMarks,
                                  final ILogger dialogLogger) {
        setLogger(dialogLogger);
        if (dialogLogger == null) {
            throw new IllegalArgumentException("Logger can't be null");
        }
        setDialogDateTextBoxVisible(true);
        setDialogGroupBoxVisible(true);
        setDialogStudentBoxVisible(true);
        setDialogSubjectBoxVisible(true);
        setDialogInputTextBoxVisible(true);
        setDialogType(DialogType.ADD_MARK);
        setTableOfMarks(currentTableOfMarks);
        setDialogFieldsByDefault();
    }

    @Override
    public ComboBoxModel<String> getDialogGroupsComboBoxModel() {
        String[] groupNumbers = getTableOfMarks().getGroupsWhereCanAddMarksAsArrayOfStrings();
        setDialogGroup(groupNumbers[0]);
        return new JComboBox<String>(groupNumbers).getModel();
    }

    @Override
    public ComboBoxModel<String> getDialogStudentsComboBoxModel() {
        return getComboBoxModelOfAllStudents();
    }

    @Override
    public ComboBoxModel<String> getDialogSubjectsComboBoxModel() {
        return getComboBoxModelOfAllSubjects();
    }

    @Override
    protected void addObjectToTableOfMarks() throws ParseException {
        logTriedChangingTable();
        getTableOfMarks().addNewMark(new Mark(Integer.parseInt(getDialogInputTextBox()),
                        getDialogSubject(), DateParser.parseDate(getDialogDate())),
                new Student(getDialogStudent()), new Group(getDialogGroup()));
        logCompletedChangingTable();
    }

    @Override
    protected void logTriedChangingTable() {
        getLogger().log(LogMessage.TRIED_CHANGING + "to add mark.");
    }

    @Override
    protected void logCompletedChangingTable() {
        getLogger().log("Adding mark " + getDialogInputTextBox() + " on date " + getDialogDate()
                + " and subject " + getDialogSubject() + " to student " + getDialogStudent()
                + " of group " + getDialogGroup() + LogMessage.COMPLETED_CHANGING);
    }

    @Override
    public void logCancelledChangingTable() {
        getLogger().log("Adding mark" + LogMessage.CANCELLED_CHANGING);
    }
}
