package ru.unn.agile.MarksAccounting.viewmodel;

import org.junit.After;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import ru.unn.agile.MarksAccounting.model.*;
import javax.swing.*;
import java.text.ParseException;
import java.util.List;

public class AddSubjectDialogViewModelTests extends DialogViewModelWithLoggerTests {
    private DialogViewModel addSubjectViewModel;
    private TableOfMarks tableOfMarks;

    @Before
    public void setUp() {
        initModel();
        initTable();
        addSubjectViewModel.getLogger().log(LogMessage.DIALOG_ACTIVATED);
        setDialogViewModel(addSubjectViewModel);
    }

    @After
    public void tearDown() {
        addSubjectViewModel = null;
        tableOfMarks = null;
    }

    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        addSubjectViewModel = new AddSubjectDialogViewModel(tempTableOfMarks, new TestLogger());
    }

    protected void setAddSubjectViewModel(final DialogViewModel addSubjectViewModel) {
        this.addSubjectViewModel = addSubjectViewModel;
    }

    private void initTable() {
        tableOfMarks = TestDataInitializer.initTableOfMarks();
    }

    @Test
    public void canSetInitialValues() {
        assertTrue(addSubjectViewModel.isDialogGroupBoxVisible());
        assertFalse(addSubjectViewModel.isDialogStudentBoxVisible());
        assertFalse(addSubjectViewModel.isDialogSubjectBoxVisible());
        assertFalse(addSubjectViewModel.isDialogDateTextBoxVisible());
        assertTrue(addSubjectViewModel.isDialogInputTextBoxVisible());
        assertTrue(addSubjectViewModel.getDialogGroup().isEmpty());
        assertTrue(addSubjectViewModel.getDialogStudent().isEmpty());
        assertTrue(addSubjectViewModel.getDialogSubject().isEmpty());
        assertTrue(addSubjectViewModel.getDialogDate().isEmpty());
        assertTrue(addSubjectViewModel.getDialogInputTextBox().isEmpty());
        assertEquals(DialogType.ADD_SUBJECT, addSubjectViewModel.getDialogType());
    }

    @Test
    public void canGetGroupsComboBoxModel() {
        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(
                TestDataInitializer.getAllGroupsInComboBoxModel(),
                addSubjectViewModel.getDialogGroupsComboBoxModel()));
    }

    @Test
    public void canAddSubject() {
        try {
            tableOfMarks.addAcademicSubject(new Group("2"), "History");

            addSubjectViewModel.setDialogGroup("2");
            addSubjectViewModel.setDialogInputTextBox("History");
            addSubjectViewModel.changeTableOfMarks();

            assertEquals(tableOfMarks, addSubjectViewModel.getTableOfMarks());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = GroupDoesNotExistException.class)
    public void canNotAddSubjectWhenGroupDoesNotExist() {
        try {
            addSubjectViewModel.setDialogGroup("116");
            addSubjectViewModel.setDialogInputTextBox("Geography");
            addSubjectViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = SubjectAlreadyExistsException.class)
    public void canNotAddSubjectWhenSubjectAlreadyExists() {
        try {
            addSubjectViewModel.setDialogGroup("1");
            addSubjectViewModel.setDialogInputTextBox("Maths");
            addSubjectViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = SubjectAlreadyExistsException.class)
    public void canNotAddSubjectWhenSubjectWithoutSpacesAlreadyExists() {
        try {
            addSubjectViewModel.setDialogGroup("1");
            addSubjectViewModel.setDialogInputTextBox(" History    ");
            addSubjectViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = InputIsEmptyException.class)
    public void canNotAddStudentWhenInputIsEmptyOrSpaces() {
        try {
            addSubjectViewModel.setDialogGroup("2");
            addSubjectViewModel.setDialogInputTextBox("   ");
            addSubjectViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    protected void createViewModelWithNullLogger() {
        new AddSubjectDialogViewModel(tableOfMarks, null);
    }

    @Test
    public void canLogTableChanging() {
        try {
            addSubjectViewModel.setDialogGroup("2");
            addSubjectViewModel.setDialogInputTextBox("Maths");
            addSubjectViewModel.changeTableOfMarks();

            List<String> messagesInLog = addSubjectViewModel.getLog();

            assertTrue(messagesInLog.get(messagesInLog.size() -  1).matches(".*"
                    + "Adding subject Maths to group 2"
                    + LogMessage.COMPLETED_CHANGING));
            assertTrue(messagesInLog.get(messagesInLog.size() - 2).matches(
                    ".*" + LogMessage.TRIED_CHANGING + "to add subject."));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void canLogCancellingChanges() {
        addSubjectViewModel.logCancelledChangingTable();

        List<String> messagesInLog = addSubjectViewModel.getLog();

        assertTrue(messagesInLog.get(messagesInLog.size() -  1).matches(
                ".*" + "Adding subject" + LogMessage.CANCELLED_CHANGING));
    }
}
