package ru.unn.agile.MarksAccounting.viewmodel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.unn.agile.MarksAccounting.model.Group;
import ru.unn.agile.MarksAccounting.model.GroupAlreadyExistsException;
import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import java.text.ParseException;
import java.util.List;

public class AddGroupDialogViewModelTests extends DialogViewModelWithLoggerTests {
    private DialogViewModel addGroupViewModel;
    private TableOfMarks tableOfMarks;

    @Before
    public void setUp() {
        initModel();
        initTable();
        addGroupViewModel.getLogger().log(LogMessage.DIALOG_ACTIVATED);
        setDialogViewModel(addGroupViewModel);
    }

    @After
    public void tearDown() {
        addGroupViewModel = null;
        tableOfMarks = null;
    }

    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        addGroupViewModel = new AddGroupDialogViewModel(tempTableOfMarks, new TestLogger());
    }

    protected void setAddGroupViewModel(final AddGroupDialogViewModel addGroupViewModel) {
        this.addGroupViewModel = addGroupViewModel;
    }

    private void initTable() {
        tableOfMarks = TestDataInitializer.initTableOfMarks();
    }

    @Test
    public void canSetInitialValues() {
        assertFalse(addGroupViewModel.isDialogGroupBoxVisible());
        assertFalse(addGroupViewModel.isDialogStudentBoxVisible());
        assertFalse(addGroupViewModel.isDialogSubjectBoxVisible());
        assertFalse(addGroupViewModel.isDialogDateTextBoxVisible());
        assertTrue(addGroupViewModel.isDialogInputTextBoxVisible());
        assertTrue(addGroupViewModel.getDialogGroup().isEmpty());
        assertTrue(addGroupViewModel.getDialogStudent().isEmpty());
        assertTrue(addGroupViewModel.getDialogSubject().isEmpty());
        assertTrue(addGroupViewModel.getDialogDate().isEmpty());
        assertTrue(addGroupViewModel.getDialogInputTextBox().isEmpty());
        assertEquals(DialogType.ADD_GROUP, addGroupViewModel.getDialogType());
    }

    @Test
    public void canAddGroup() {
        try {
            tableOfMarks.addGroup(new Group("4"));

            addGroupViewModel.setDialogType(DialogType.ADD_GROUP);
            addGroupViewModel.setDialogInputTextBox("4");
            addGroupViewModel.changeTableOfMarks();

            assertEquals(tableOfMarks, addGroupViewModel.getTableOfMarks());
        } catch (Exception e) {
            fail();
        }
    }

    @Test(expected = GroupAlreadyExistsException.class)
    public void canNotAddGroupWhenGroupExists() {
        try {
            addGroupViewModel.setDialogType(DialogType.ADD_GROUP);
            addGroupViewModel.setDialogInputTextBox("1");
            addGroupViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = GroupAlreadyExistsException.class)
    public void canNotAddGroupWhenGroupWithoutSpacesExists() {
        try {
            addGroupViewModel.setDialogInputTextBox("   1 ");
            addGroupViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = InputIsEmptyException.class)
    public void canNotAddGroupWhenInputIsEmptyOrSpaces() {
        try {
            addGroupViewModel.setDialogInputTextBox("   ");
            addGroupViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    protected void createViewModelWithNullLogger() {
        new AddGroupDialogViewModel(tableOfMarks, null);
    }

    @Test
    public void canLogTableChanging() {
        try {
            addGroupViewModel.setDialogInputTextBox("111");
            addGroupViewModel.changeTableOfMarks();

            List<String> messagesInLog = addGroupViewModel.getLog();

            assertTrue(messagesInLog.get(messagesInLog.size() -  1).matches(
                    ".*" + "Adding group 111" + LogMessage.COMPLETED_CHANGING));
            assertTrue(messagesInLog.get(messagesInLog.size() -  2).matches(
                    ".*" + LogMessage.TRIED_CHANGING + "to add group."));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void canLogCancellingChanges() {
        addGroupViewModel.logCancelledChangingTable();

        List<String> messagesInLog = addGroupViewModel.getLog();

        assertTrue(messagesInLog.get(messagesInLog.size() -  1).matches(
                ".*" + "Adding group" + LogMessage.CANCELLED_CHANGING));
    }
}
