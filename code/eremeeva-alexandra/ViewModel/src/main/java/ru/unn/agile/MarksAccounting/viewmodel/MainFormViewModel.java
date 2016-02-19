package ru.unn.agile.MarksAccounting.viewmodel;

import ru.unn.agile.MarksAccounting.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MainFormViewModel {
    public static final int ADD_MARK_POSITION = 3;
    private String groupInCurrentTable;
    private String subjectInCurrentTable;
    private TableOfMarks tableOfMarks;
    private DefaultTableModel tableModel;
    private final ILogger logger;

    public MainFormViewModel(final ILogger mainLogger) {
        logger = mainLogger;
        if (logger == null) {
            throw new IllegalArgumentException("Logger can't be null");
        }
        groupInCurrentTable = "";
        subjectInCurrentTable = "";
        tableOfMarks = new TableOfMarks();
        tableModel = new DefaultTableModel(0, 0);
    }

    public ComboBoxModel<String> getChangingComboBoxModel() {
        ArrayList<String> actionsArrayList = new ArrayList<String>();
        actionsArrayList.add(DialogType.ADD_GROUP.getTypeDescription());
        if (tableOfMarks.getGroups().size() > 0) {
            actionsArrayList.add(DialogType.ADD_SUBJECT.getTypeDescription());
            actionsArrayList.add(DialogType.ADD_STUDENT.getTypeDescription());
            actionsArrayList.add(DialogType.DELETE_GROUP.getTypeDescription());
            if (tableOfMarks.doAcademicSubjectsExist()) {
                actionsArrayList.add(DialogType.DELETE_SUBJECT.getTypeDescription());
            }
            if (tableOfMarks.doStudentsExist()) {
                actionsArrayList.add(DialogType.DELETE_STUDENT.getTypeDescription());
                setMarkChangingOnRightPositions(actionsArrayList);
            }
        }
        String[] actionsArray = actionsArrayList.toArray(new String[actionsArrayList.size()]);
        return new JComboBox<String>(actionsArray).getModel();
    }

    private void setMarkChangingOnRightPositions(final ArrayList<String> actionsArrayList) {
        if (tableOfMarks.canAddMark()) {
            actionsArrayList.add(ADD_MARK_POSITION,
                    DialogType.ADD_MARK.getTypeDescription());
            if (tableOfMarks.doMarksExist()) {
                actionsArrayList.add(DialogType.DELETE_MARK.getTypeDescription());
            }
        }
    }

    public ComboBoxModel<String> getGroupComboBoxModel() {
        String[] groupNumbers = tableOfMarks.getGroupsAsArrayOfStrings();
        if (groupNumbers.length != 0) {
            setGroupInCurrentTable(groupNumbers[0]);
        }
        return new JComboBox<String>(groupNumbers).getModel();
    }

    public ComboBoxModel<String> getSubjectComboBoxModel() {
        if (tableOfMarks.doAcademicSubjectsExist()) {
            String[] academicSubjects =
                    tableOfMarks.getAcademicSubjectsAsArray(new Group(groupInCurrentTable));
            if (academicSubjects.length != 0) {
                 setSubjectInCurrentTable(academicSubjects[0]);
            }
            return new JComboBox<String>(academicSubjects).getModel();
        }
        return new JComboBox<String>(new String[0]).getModel();
    }

    public TableOfMarks getTableOfMarks() {
        return tableOfMarks;
    }

    public List<String> getLog() {
        return logger.getLog(WindowType.MAIN);
    }

    public void setTableOfMarks(final TableOfMarks tableOfMarks) {
        this.tableOfMarks = tableOfMarks;
    }

    public void setGroupInCurrentTable(final Object groupInCurrentTable) {
        if (groupInCurrentTable == null) {
            this.groupInCurrentTable = "";
        } else if (!this.groupInCurrentTable.equals(groupInCurrentTable.toString())) {
            this.groupInCurrentTable = groupInCurrentTable.toString();
            logGroupChanging();
        }
    }

    public void setSubjectInCurrentTable(final Object subjectInCurrentTable) {
        if (subjectInCurrentTable == null) {
            this.subjectInCurrentTable = "";
        } else if (!this.subjectInCurrentTable.equals(subjectInCurrentTable.toString())) {
            this.subjectInCurrentTable = subjectInCurrentTable.toString();
            logSubjectChanging();
        }
    }

    public String getGroupInCurrentTable() {
        return groupInCurrentTable;
    }

    public String getSubjectInCurrentTable() {
        return subjectInCurrentTable;
    }

    public DefaultTableModel getTableModel() {
        changeTableModel();
        return tableModel;
    }

    public ILogger getLogger() {
        return  logger;
    }

    private void changeTableModel() {
        tableModel = new DefaultTableModel();
        if (tableOfMarks.doStudentsExist()) {
            Student[] students = tableOfMarks.getStudentsAsArray(new Group(groupInCurrentTable));
            GregorianCalendar[] dates;
            if (tableOfMarks.doMarksInSubjectExist(new Group(groupInCurrentTable),
                    subjectInCurrentTable)) {
                dates = tableOfMarks.getDatesAsArray(new Group(groupInCurrentTable),
                        subjectInCurrentTable);
            } else {
                dates = new GregorianCalendar[0];
            }
            tableModel = new DefaultTableModel(students.length + 1, dates.length + 1);
            tableModel.setValueAt("Students", 0, 0);
            for (int i = 1; i < students.length + 1; i++) {
                tableModel.setValueAt(students[i - 1].getName(), i, 0);
            }
            if (dates.length != 0) {
                for (int i = 1; i < dates.length + 1; i++) {
                    tableModel.setValueAt(DateParser.formatDate(dates[i - 1]), 0, i);
                }
                for (int i = 1; i < students.length + 1; i++) {
                    for (int j = 1; j < dates.length + 1; j++) {
                        try {
                            Mark mark = students[i - 1].getMark(subjectInCurrentTable,
                                    dates[j - 1]);
                            tableModel.setValueAt(Integer.toString(mark.getValue()), i, j);
                        } catch (MarkDoesNotExistException e) {
                            tableModel.setValueAt("", i, j);
                        }
                    }
                }
            }
        }
    }

    public void logGroupChanging() {
        logger.log(LogMessage.GROUP_CHANGED + groupInCurrentTable + ".");
    }

    public void logSubjectChanging() {
        logger.log(LogMessage.SUBJECT_CHANGED + subjectInCurrentTable + ".");
    }

    public void logActionChanging(final String action) {
        logger.log(LogMessage.ACTION_CHANGED + action.toLowerCase(Locale.ENGLISH) + ".");
    }

    public void logDialogActivating() {
        logger.log(LogMessage.DIALOG_ACTIVATED);
    }

    public void logDialogDeactivating() {
        logger.log(LogMessage.DIALOG_DEACTIVATED);
    }

    public void logTableSaving(final String fileName) {
        logger.log(LogMessage.TABLE_SAVED + fileName + ".");
    }

    public void logTableOpening(final String fileName) {
        logger.log(LogMessage.TABLE_OPENED + fileName + ".");
    }
}
