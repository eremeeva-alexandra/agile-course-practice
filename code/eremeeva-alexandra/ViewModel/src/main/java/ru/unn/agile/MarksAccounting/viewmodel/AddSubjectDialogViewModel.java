package ru.unn.agile.MarksAccounting.viewmodel;

import ru.unn.agile.MarksAccounting.model.Group;
import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import javax.swing.*;
import java.text.ParseException;

public class AddSubjectDialogViewModel extends AddingDialogViewModel {
    public AddSubjectDialogViewModel(final TableOfMarks currentTableOfMarks,
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
        setDialogType(DialogType.ADD_SUBJECT);
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
        getTableOfMarks().addAcademicSubject(new Group(getDialogGroup()),
                getDialogInputTextBox());
        logCompletedChangingTable();
    }

    @Override
    protected void logTriedChangingTable() {
        getLogger().log(LogMessage.TRIED_CHANGING.getMessage() + "to add subject.");
    }

    @Override
    protected void logCompletedChangingTable() {
        getLogger().log("Adding subject " + getDialogInputTextBox()
                + " to group " + getDialogGroup() + LogMessage.COMPLETED_CHANGING.getMessage());
    }

    @Override
    public void logCancelledChangingTable() {
        getLogger().log("Adding subject" + LogMessage.CANCELLED_CHANGING.getMessage());
    }
}
