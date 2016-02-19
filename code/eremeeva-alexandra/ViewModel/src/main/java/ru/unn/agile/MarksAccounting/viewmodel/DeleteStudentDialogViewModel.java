package ru.unn.agile.MarksAccounting.viewmodel;

import ru.unn.agile.MarksAccounting.model.Group;
import ru.unn.agile.MarksAccounting.model.Student;
import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import javax.swing.*;
import java.text.ParseException;

public class DeleteStudentDialogViewModel extends DeletingDialogViewModel {
    public DeleteStudentDialogViewModel(final TableOfMarks currentTableOfMarks,
                                        final ILogger dialogLogger) {
        setLogger(dialogLogger);
        if (dialogLogger == null) {
            throw new IllegalArgumentException("Logger can't be null");
        }
        setDialogDateTextBoxVisible(false);
        setDialogGroupBoxVisible(true);
        setDialogStudentBoxVisible(true);
        setDialogSubjectBoxVisible(false);
        setDialogInputTextBoxVisible(false);
        setDialogType(DialogType.DELETE_STUDENT);
        setTableOfMarks(currentTableOfMarks);
        setDialogFieldsByDefault();
    }

    @Override
    public ComboBoxModel<String> getDialogGroupsComboBoxModel() {
        String[] groupNumbers = getTableOfMarks().getGroupsWhereStudentsExistAsArrayOfStrings();
        setDialogGroup(groupNumbers[0]);
        return new JComboBox<String>(groupNumbers).getModel();
    }

    @Override
    public ComboBoxModel<String> getDialogStudentsComboBoxModel() {
        return getComboBoxModelOfAllStudents();
    }

    @Override
    protected void deleteObjectFromTableOfMarks() throws ParseException {
        logTriedChangingTable();
        getTableOfMarks().deleteStudent(new Group(getDialogGroup()),
                new Student(getDialogStudent()));
        logCompletedChangingTable();
    }

    @Override
    protected void logTriedChangingTable() {
        getLogger().log(LogMessage.TRIED_CHANGING + "to delete student.");
    }

    @Override
    protected void logCompletedChangingTable() {
        getLogger().log("Deleting student " + getDialogStudent() + " of group " + getDialogGroup()
                + LogMessage.COMPLETED_CHANGING);
    }

    @Override
    public void logCancelledChangingTable() {
        getLogger().log("Deleting student" + LogMessage.CANCELLED_CHANGING);
    }
}
