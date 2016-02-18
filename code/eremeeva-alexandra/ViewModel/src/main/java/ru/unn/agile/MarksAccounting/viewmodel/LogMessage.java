package ru.unn.agile.MarksAccounting.viewmodel;

public enum LogMessage {
    GROUP_CHANGED("Group was changed to "),
    SUBJECT_CHANGED("Subject was changed to "),
    STUDENT_CHANGED("Student was changed to "),
    DATE_CHANGED("Date was changed to "),
    INPUT_CHANGED("Input value was changed to "),
    ACTION_CHANGED("Action was changed to "),
    TRIED_CHANGING("Tried "),
    COMPLETED_CHANGING(" completed."),
    CANCELLED_CHANGING(" cancelled."),
    ERROR("Error occurred: "),
    DIALOG_ACTIVATED("Dialog window activated."),
    DIALOG_DEACTIVATED("Dialog window deactivated."),
    TABLE_SAVED("Table saved to file "),
    TABLE_OPENED("Table readed from file ");

    private String message;

    private LogMessage(final String newMessage) {
        message = newMessage;
    }

    public String getMessage() {
        return message;
    }
}
