package ru.unn.agile.MarksAccounting.viewmodel;

import ru.unn.agile.MarksAccounting.model.Group;
import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import java.text.ParseException;

public class AddGroupDialogViewModel extends AddingDialogViewModel {
    public AddGroupDialogViewModel(final TableOfMarks currentTableOfMarks,
                                   final ILogger dialogLogger) {
        setLogger(dialogLogger);
        if (dialogLogger == null) {
            throw new IllegalArgumentException("Logger can't be null");
        }
        setDialogDateTextBoxVisible(false);
        setDialogGroupBoxVisible(false);
        setDialogStudentBoxVisible(false);
        setDialogSubjectBoxVisible(false);
        setDialogInputTextBoxVisible(true);
        setDialogType(DialogType.ADD_GROUP);
        setTableOfMarks(currentTableOfMarks);
        setDialogFieldsByDefault();
    }

    @Override
    protected void addObjectToTableOfMarks() throws ParseException {
        logTriedChangingTable();
        getTableOfMarks().addGroup(new Group(getDialogInputTextBox()));
        logCompletedChangingTable();
    }

    @Override
    protected void logTriedChangingTable() {
        getLogger().log(LogMessage.TRIED_CHANGING + "to add group.");
    }

    @Override
    protected void logCompletedChangingTable() {
        getLogger().log("Adding group " + getDialogInputTextBox()
                + LogMessage.COMPLETED_CHANGING);
    }

    @Override
    public void logCancelledChangingTable() {
        getLogger().log("Adding group" + LogMessage.CANCELLED_CHANGING);
    }
}
