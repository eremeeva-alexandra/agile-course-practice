package ru.unn.agile.MarksAccounting.viewmodel;

import ru.unn.agile.MarksAccounting.model.Group;
import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import javax.swing.*;
import java.text.ParseException;

public class DeleteGroupDialogViewModel extends DeletingDialogViewModel {
    public DeleteGroupDialogViewModel(final TableOfMarks currentTableOfMarks,
                                      final ILogger dialogLogger) {
        setLogger(dialogLogger);
        if (dialogLogger == null) {
            throw new IllegalArgumentException("Logger can't be null");
        }
        setDialogDateTextBoxVisible(false);
        setDialogGroupBoxVisible(true);
        setDialogStudentBoxVisible(false);
        setDialogSubjectBoxVisible(false);
        setDialogInputTextBoxVisible(false);
        setDialogType(DialogType.DELETE_GROUP);
        setTableOfMarks(currentTableOfMarks);
        setDialogFieldsByDefault();
    }

    @Override
    public ComboBoxModel<String> getDialogGroupsComboBoxModel() {
        return getComboBoxModelOfAllGroups();
    }

    @Override
    protected void deleteObjectFromTableOfMarks() throws ParseException {
        logTriedChangingTable();
        getTableOfMarks().deleteGroup(new Group(getDialogGroup()));
        logCompletedChangingTable();
    }

    @Override
    protected void logTriedChangingTable() {
        getLogger().log(LogMessage.TRIED_CHANGING + "to delete group.");
    }

    @Override
    protected void logCompletedChangingTable() {
        getLogger().log("Deleting group " + getDialogGroup()
                + LogMessage.COMPLETED_CHANGING);
    }

    @Override
    public void logCancelledChangingTable() {
        getLogger().log("Deleting group" + LogMessage.CANCELLED_CHANGING);
    }
}
