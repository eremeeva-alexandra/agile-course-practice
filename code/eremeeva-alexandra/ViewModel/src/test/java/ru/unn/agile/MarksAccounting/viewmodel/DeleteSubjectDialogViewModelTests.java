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

public class DeleteSubjectDialogViewModelTests extends DialogViewModelWithLoggerTests {
    private DialogViewModel deleteSubjectViewModel;
    private TableOfMarks tableOfMarks;

    @Before
    public void setUp() {
        initModel();
        initTable();
        deleteSubjectViewModel.getLogger().log(LogMessage.DIALOG_ACTIVATED);
        setDialogViewModel(deleteSubjectViewModel);
    }

    @After
    public void tearDown() {
        deleteSubjectViewModel = null;
        tableOfMarks = null;
    }

    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        deleteSubjectViewModel = new DeleteSubjectDialogViewModel(tempTableOfMarks,
                new TestLogger());
    }

    private void initTable() {
        tableOfMarks = TestDataInitializer.initTableOfMarks();
    }

    protected void setDeleteSubjectViewModel(final DialogViewModel deleteSubjectViewModel) {
        this.deleteSubjectViewModel = deleteSubjectViewModel;
    }

    @Test
    public void canSetInitialValues() {
        assertTrue(deleteSubjectViewModel.isDialogGroupBoxVisible());
        assertFalse(deleteSubjectViewModel.isDialogStudentBoxVisible());
        assertTrue(deleteSubjectViewModel.isDialogSubjectBoxVisible());
        assertFalse(deleteSubjectViewModel.isDialogDateTextBoxVisible());
        assertFalse(deleteSubjectViewModel.isDialogInputTextBoxVisible());
        assertTrue(deleteSubjectViewModel.getDialogGroup().isEmpty());
        assertTrue(deleteSubjectViewModel.getDialogStudent().isEmpty());
        assertTrue(deleteSubjectViewModel.getDialogSubject().isEmpty());
        assertTrue(deleteSubjectViewModel.getDialogDate().isEmpty());
        assertTrue(deleteSubjectViewModel.getDialogInputTextBox().isEmpty());
        assertEquals(DialogType.DELETE_SUBJECT, deleteSubjectViewModel.getDialogType());
    }

    @Test
    public void canGetGroupsComboBoxModel() {
        String[] groupNumbersWhereSubjectsExist = {"1", "2"};
        ComboBoxModel<String> expectedComboBoxModel = new JComboBox<String>(
                groupNumbersWhereSubjectsExist).getModel();

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(expectedComboBoxModel,
                deleteSubjectViewModel.getDialogGroupsComboBoxModel()));
    }

    @Test
    public void canGetSubjectsComboBoxModel() {
        String[] subjectsInFirstGroup = {"History", "Maths"};
        ComboBoxModel<String> expectedComboBoxModel = new JComboBox<String>(
                subjectsInFirstGroup).getModel();

        deleteSubjectViewModel.setDialogGroup("1");

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(expectedComboBoxModel,
                deleteSubjectViewModel.getDialogSubjectsComboBoxModel()));
    }

    @Test
    public void canDeleteSubject() {
        try {
            tableOfMarks.deleteAcademicSubject(new Group("1"), "Maths");

            deleteSubjectViewModel.setDialogGroup("1");
            deleteSubjectViewModel.setDialogSubject("Maths");
            deleteSubjectViewModel.changeTableOfMarks();

            assertEquals(tableOfMarks, deleteSubjectViewModel.getTableOfMarks());
        } catch (Exception e) {
            fail();
        }
    }

    @Test(expected = GroupDoesNotExistException.class)
    public void canNotDeleteSubjectWhenGroupDoesNotExist() {
        try {
            deleteSubjectViewModel.setDialogGroup("116");
            deleteSubjectViewModel.setDialogSubject("History");
            deleteSubjectViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = AcademicSubjectDoesNotExistException.class)
    public void canNotDeleteSubjectWhenStudentDoesNotExist() {
        try {
            deleteSubjectViewModel.setDialogGroup("2");
            deleteSubjectViewModel.setDialogSubject("Geography");
            deleteSubjectViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }


    protected void createViewModelWithNullLogger() {
        new DeleteSubjectDialogViewModel(tableOfMarks, null);
    }

    @Test
    public void canLogTableChanging() {
        try {
            deleteSubjectViewModel.setDialogGroup("2");
            deleteSubjectViewModel.setDialogSubject("Science");
            deleteSubjectViewModel.changeTableOfMarks();

            List<String> messagesInLog = deleteSubjectViewModel.getLog();

            assertTrue(messagesInLog.get(messagesInLog.size() -  1).matches(".*"
                    + "Deleting subject Science from group 2"
                    + LogMessage.COMPLETED_CHANGING));
            assertTrue(messagesInLog.get(messagesInLog.size() - 2).matches(
                    ".*" + LogMessage.TRIED_CHANGING + "to delete subject."));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void canLogCancellingChanges() {
        deleteSubjectViewModel.logCancelledChangingTable();

        List<String> messagesInLog = deleteSubjectViewModel.getLog();

        assertTrue(messagesInLog.get(messagesInLog.size() -  1).matches(
                ".*" + "Deleting subject" + LogMessage.CANCELLED_CHANGING));
    }
}
