package ru.unn.agile.MarksAccounting.viewmodel;

import org.junit.After;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import ru.unn.agile.MarksAccounting.model.Group;
import ru.unn.agile.MarksAccounting.model.GroupDoesNotExistException;
import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import javax.swing.*;
import java.text.ParseException;
import java.util.List;

public class DeleteGroupDialogViewModelTests extends DialogViewModelWithLoggerTests {
    private DialogViewModel deleteGroupViewModel;
    private TableOfMarks tableOfMarks;

    @Before
    public void setUp() {
        initModel();
        initTable();
        deleteGroupViewModel.getLogger().log(LogMessage.DIALOG_ACTIVATED);
        setDialogViewModel(deleteGroupViewModel);
    }

    @After
    public void tearDown() {
        deleteGroupViewModel = null;
        tableOfMarks = null;
    }

    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        deleteGroupViewModel = new DeleteGroupDialogViewModel(tempTableOfMarks, new TestLogger());
    }

    private void initTable() {
        tableOfMarks = TestDataInitializer.initTableOfMarks();
    }

    protected void setDeleteGroupViewModel(final DialogViewModel deleteGroupViewModel) {
        this.deleteGroupViewModel = deleteGroupViewModel;
    }

    @Test
    public void canSetInitialValues() {
        assertTrue(deleteGroupViewModel.isDialogGroupBoxVisible());
        assertFalse(deleteGroupViewModel.isDialogStudentBoxVisible());
        assertFalse(deleteGroupViewModel.isDialogSubjectBoxVisible());
        assertFalse(deleteGroupViewModel.isDialogDateTextBoxVisible());
        assertFalse(deleteGroupViewModel.isDialogInputTextBoxVisible());
        assertTrue(deleteGroupViewModel.getDialogGroup().isEmpty());
        assertTrue(deleteGroupViewModel.getDialogStudent().isEmpty());
        assertTrue(deleteGroupViewModel.getDialogSubject().isEmpty());
        assertTrue(deleteGroupViewModel.getDialogDate().isEmpty());
        assertTrue(deleteGroupViewModel.getDialogInputTextBox().isEmpty());
        assertEquals(DialogType.DELETE_GROUP, deleteGroupViewModel.getDialogType());
    }

    @Test
    public void canGetGroupsComboBoxModel() {
        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(
                TestDataInitializer.getAllGroupsInComboBoxModel(),
                deleteGroupViewModel.getDialogGroupsComboBoxModel()));
    }

    @Test
    public void canDeleteGroup() {
        try {
            tableOfMarks.deleteGroup(new Group("1"));

            deleteGroupViewModel.setDialogGroup("1");
            deleteGroupViewModel.changeTableOfMarks();

            assertEquals(tableOfMarks, deleteGroupViewModel.getTableOfMarks());
        } catch (Exception e) {
            fail();
        }
    }

    @Test(expected = GroupDoesNotExistException.class)
    public void canNotDeleteGroupWhenGroupDoesNotExist() {
        try {
            deleteGroupViewModel.setDialogGroup("116");
            deleteGroupViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    protected void createViewModelWithNullLogger() {
        new DeleteGroupDialogViewModel(tableOfMarks, null);
    }

    @Test
    public void canLogTableChanging() {
        try {
            deleteGroupViewModel.setDialogGroup("1");
            deleteGroupViewModel.changeTableOfMarks();

            List<String> messagesInLog = deleteGroupViewModel.getLog();

            assertTrue(messagesInLog.get(messagesInLog.size() -  1).matches(".*"
                    + "Deleting group 1" + LogMessage.COMPLETED_CHANGING));
            assertTrue(messagesInLog.get(messagesInLog.size() - 2).matches(
                    ".*" + LogMessage.TRIED_CHANGING + "to delete group."));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void canLogCancellingChanges() {
        deleteGroupViewModel.logCancelledChangingTable();

        List<String> messagesInLog = deleteGroupViewModel.getLog();

        assertTrue(messagesInLog.get(messagesInLog.size() -  1).matches(
                ".*" + "Deleting group" + LogMessage.CANCELLED_CHANGING));
    }
}
