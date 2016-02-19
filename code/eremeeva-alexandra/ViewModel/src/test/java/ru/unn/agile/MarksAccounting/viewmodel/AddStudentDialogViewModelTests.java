package ru.unn.agile.MarksAccounting.viewmodel;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ru.unn.agile.MarksAccounting.model.*;
import javax.swing.*;
import java.text.ParseException;
import java.util.List;

public class AddStudentDialogViewModelTests extends DialogViewModelWithLoggerTests {
    private DialogViewModel addStudentViewModel;
    private TableOfMarks tableOfMarks;

    @Before
    public void setUp() {
        initModel();
        initTable();
        addStudentViewModel.getLogger().log(LogMessage.DIALOG_ACTIVATED);
        setDialogViewModel(addStudentViewModel);
    }

    @After
    public void tearDown() {
        addStudentViewModel = null;
        tableOfMarks = null;
    }

    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        addStudentViewModel = new AddStudentDialogViewModel(tempTableOfMarks, new TestLogger());
    }

    private void initTable() {
        tableOfMarks = TestDataInitializer.initTableOfMarks();
    }

    protected void setAddStudentViewModel(final AddStudentDialogViewModel addStudentViewModel) {
        this.addStudentViewModel = addStudentViewModel;
    }

    @Test
    public void canSetInitialValues() {
        assertTrue(addStudentViewModel.isDialogGroupBoxVisible());
        assertFalse(addStudentViewModel.isDialogStudentBoxVisible());
        assertFalse(addStudentViewModel.isDialogSubjectBoxVisible());
        assertFalse(addStudentViewModel.isDialogDateTextBoxVisible());
        assertTrue(addStudentViewModel.isDialogInputTextBoxVisible());
        assertTrue(addStudentViewModel.getDialogGroup().isEmpty());
        assertTrue(addStudentViewModel.getDialogStudent().isEmpty());
        assertTrue(addStudentViewModel.getDialogSubject().isEmpty());
        assertTrue(addStudentViewModel.getDialogDate().isEmpty());
        assertTrue(addStudentViewModel.getDialogInputTextBox().isEmpty());
        assertEquals(DialogType.ADD_STUDENT, addStudentViewModel.getDialogType());
    }

    @Test
    public void canGetGroupsComboBoxModel() {
        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(
                TestDataInitializer.getAllGroupsInComboBoxModel(),
                addStudentViewModel.getDialogGroupsComboBoxModel()));
    }

    @Test
    public void canAddStudent() {
        try {
            tableOfMarks.addStudent(new Group("2"), new Student("Smirnov"));

            addStudentViewModel.setDialogGroup("2");
            addStudentViewModel.setDialogInputTextBox("Smirnov");
            addStudentViewModel.changeTableOfMarks();

            assertEquals(tableOfMarks, addStudentViewModel.getTableOfMarks());
        } catch (Exception e) {
            fail();
        }
    }

    @Test(expected = GroupDoesNotExistException.class)
    public void canNotAddStudentWhenGroupDoesNotExist() {
        try {
            addStudentViewModel.setDialogGroup("116");
            addStudentViewModel.setDialogInputTextBox("Smirnov");
            addStudentViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = StudentAlreadyExistsException.class)
    public void canNotAddStudentWhenStudentAlreadyExists() {
        try {
            addStudentViewModel.setDialogGroup("1");
            addStudentViewModel.setDialogInputTextBox("Sidorov");
            addStudentViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = StudentAlreadyExistsException.class)
    public void canNotStudentMarkWhenStudentWithoutSpacesAlreadyExists() {
        try {
            addStudentViewModel.setDialogGroup("1");
            addStudentViewModel.setDialogInputTextBox(" Petrov    ");
            addStudentViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = InputIsEmptyException.class)
    public void canNotAddStudentWhenInputIsEmptyOrSpaces() {
        try {
            addStudentViewModel.setDialogGroup("3");
            addStudentViewModel.setDialogInputTextBox("   ");
            addStudentViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    protected void createViewModelWithNullLogger() {
        new AddStudentDialogViewModel(tableOfMarks, null);
    }

    @Test
    public void canLogTableChanging() {
        try {
            addStudentViewModel.setDialogGroup("2");
            addStudentViewModel.setDialogInputTextBox("Sidorov");
            addStudentViewModel.changeTableOfMarks();

            List<String> messagesInLog = addStudentViewModel.getLog();

            assertTrue(messagesInLog.get(messagesInLog.size() -  1).matches(".*"
                    + "Adding student Sidorov to group 2"
                    + LogMessage.COMPLETED_CHANGING));
            assertTrue(messagesInLog.get(messagesInLog.size() - 2).matches(
                    ".*" + LogMessage.TRIED_CHANGING + "to add student."));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void canLogCancellingChanges() {
        addStudentViewModel.logCancelledChangingTable();

        List<String> messagesInLog = addStudentViewModel.getLog();

        assertTrue(messagesInLog.get(messagesInLog.size() -  1).matches(
                ".*" + "Adding student" + LogMessage.CANCELLED_CHANGING));
    }
}
