package ru.unn.agile.MarksAccounting.viewmodel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.unn.agile.MarksAccounting.model.*;
import javax.swing.*;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.List;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class AddMarkDialogViewModelTests extends DialogViewModelWithLoggerTests {
    private DialogViewModel addMarkViewModel;
    private TableOfMarks tableOfMarks;

    @Before
    public void setUp() {
        initModel();
        initTable();
        addMarkViewModel.getLogger().log(LogMessage.DIALOG_ACTIVATED);
        setDialogViewModel(addMarkViewModel);
    }

    @After
    public void tearDown() {
        addMarkViewModel = null;
        tableOfMarks = null;
    }

    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        addMarkViewModel = new AddMarkDialogViewModel(tempTableOfMarks, new TestLogger());
    }

    private void initTable() {
        tableOfMarks = TestDataInitializer.initTableOfMarks();
    }

    protected void setAddMarkViewModel(final DialogViewModel addMarkViewModel) {
        this.addMarkViewModel = addMarkViewModel;
    }

    @Test
    public void canSetInitialValues() {
        assertTrue(addMarkViewModel.isDialogGroupBoxVisible());
        assertTrue(addMarkViewModel.isDialogStudentBoxVisible());
        assertTrue(addMarkViewModel.isDialogSubjectBoxVisible());
        assertTrue(addMarkViewModel.isDialogDateTextBoxVisible());
        assertTrue(addMarkViewModel.isDialogInputTextBoxVisible());
        assertTrue(addMarkViewModel.getDialogGroup().isEmpty());
        assertTrue(addMarkViewModel.getDialogStudent().isEmpty());
        assertTrue(addMarkViewModel.getDialogSubject().isEmpty());
        assertTrue(addMarkViewModel.getDialogDate().isEmpty());
        assertTrue(addMarkViewModel.getDialogInputTextBox().isEmpty());
        assertEquals(DialogType.ADD_MARK, addMarkViewModel.getDialogType());
    }

    @Test
    public void canGetGroupsComboBoxModel() {
        String[] groupNumbersWhereCanAddMarks = {"1"};
        ComboBoxModel<String> expectedComboBoxModel = new JComboBox<String>(
                groupNumbersWhereCanAddMarks).getModel();

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(expectedComboBoxModel,
                addMarkViewModel.getDialogGroupsComboBoxModel()));
    }

    @Test
    public void canGetStudentsComboBoxModel() {
        String[] studentNamesWhereCanAddMarks = {"Petrov", "Sidorov"};
        ComboBoxModel<String> expectedComboBoxModel = new JComboBox<String>(
                studentNamesWhereCanAddMarks).getModel();

        addMarkViewModel.setDialogGroup("1");

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(expectedComboBoxModel,
                addMarkViewModel.getDialogStudentsComboBoxModel()));
    }

    @Test
    public void canGetSubjectsComboBoxModel() {
        String[] subjectsWhereCanAddMarks = {"History", "Maths"};
        ComboBoxModel<String> expectedComboBoxModel = new JComboBox<String>(
                subjectsWhereCanAddMarks).getModel();

        addMarkViewModel.setDialogGroup("1");

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(expectedComboBoxModel,
                addMarkViewModel.getDialogSubjectsComboBoxModel()));
    }

    @Test
    public void canAddMark() {
        try {
            tableOfMarks.addNewMark(new Mark(3, "Maths", new GregorianCalendar(2015, 9, 13)),
                    new Student("Petrov"), new Group("1"));

            addMarkViewModel.setDialogGroup("1");
            addMarkViewModel.setDialogStudent("Petrov");
            addMarkViewModel.setDialogSubject("Maths");
            addMarkViewModel.setDialogDate("13-10-2015");
            addMarkViewModel.setDialogInputTextBox("3");
            addMarkViewModel.changeTableOfMarks();

            assertEquals(tableOfMarks, addMarkViewModel.getTableOfMarks());
        } catch (Exception e) {
            fail();
        }
    }

    @Test(expected = GroupDoesNotExistException.class)
    public void canNotAddMarkWhenGroupDoesNotExist() {
        try {
            addMarkViewModel.setDialogGroup("116");
            addMarkViewModel.setDialogStudent("Petrov");
            addMarkViewModel.setDialogSubject("History");
            addMarkViewModel.setDialogDate("29-2-2016");
            addMarkViewModel.setDialogInputTextBox("4");
            addMarkViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = StudentDoesNotExistException.class)
    public void canNotAddMarkWhenStudentDoesNotExist() {
        try {
            addMarkViewModel.setDialogGroup("1");
            addMarkViewModel.setDialogStudent("Ivanov");
            addMarkViewModel.setDialogSubject("Maths");
            addMarkViewModel.setDialogDate("5-3-2015");
            addMarkViewModel.setDialogInputTextBox("10");
            addMarkViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = AcademicSubjectDoesNotExistException.class)
    public void canNotAddMarkWhenSubjectDoesNotExist() {
        try {
            addMarkViewModel.setDialogGroup("1");
            addMarkViewModel.setDialogStudent("Sidorov");
            addMarkViewModel.setDialogSubject("Science");
            addMarkViewModel.setDialogDate("21-06-2015");
            addMarkViewModel.setDialogInputTextBox("4");
            addMarkViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void canNotAddMarkWhenDateFormatIsWrong() {
        try {
            addMarkViewModel.setDialogGroup("1");
            addMarkViewModel.setDialogStudent("Petrov");
            addMarkViewModel.setDialogSubject("History");
            addMarkViewModel.setDialogDate("30/12/2015");
            addMarkViewModel.setDialogInputTextBox("2");
            addMarkViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

    @Test(expected = MarkIsNotPositiveException.class)
    public void canNotAddMarkWhenMarkIsNotPositive() {
        try {
            addMarkViewModel.setDialogGroup("1");
            addMarkViewModel.setDialogStudent("Petrov");
            addMarkViewModel.setDialogSubject("Maths");
            addMarkViewModel.setDialogDate("07-9-2015");
            addMarkViewModel.setDialogInputTextBox("-1");
            addMarkViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = NumberFormatException.class)
    public void canNotAddMarkWhenMarkIsNotAnInteger() {
        try {
            addMarkViewModel.setDialogGroup("1");
            addMarkViewModel.setDialogStudent("Sidorov");
            addMarkViewModel.setDialogSubject("History");
            addMarkViewModel.setDialogDate("1-1-2016");
            addMarkViewModel.setDialogInputTextBox("4.5");
            addMarkViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    @Test(expected = InputIsEmptyException.class)
    public void canNotAddMarkWhenInputIsEmptyOrSpaces() {
        try {
            addMarkViewModel.setDialogGroup("1");
            addMarkViewModel.setDialogStudent("Sidorov");
            addMarkViewModel.setDialogSubject("Maths");
            addMarkViewModel.setDialogDate("13-2-2015");
            addMarkViewModel.setDialogInputTextBox("   ");
            addMarkViewModel.changeTableOfMarks();
        } catch (ParseException e) {
            fail();
        }
    }

    protected void createViewModelWithNullLogger() {
        new AddMarkDialogViewModel(tableOfMarks, null);
    }

    @Test
    public void canLogTableChanging() {
        try {
            addMarkViewModel.setDialogGroup("1");
            addMarkViewModel.setDialogStudent("Sidorov");
            addMarkViewModel.setDialogSubject("Maths");
            addMarkViewModel.setDialogDate("10-03-2016");
            addMarkViewModel.setDialogInputTextBox("4");
            addMarkViewModel.changeTableOfMarks();

            List<String> messagesInLog = addMarkViewModel.getLog();

            assertTrue(messagesInLog.get(messagesInLog.size() -  2).matches(
                    ".*" + LogMessage.TRIED_CHANGING + "to add mark."));
            assertTrue(messagesInLog.get(messagesInLog.size() - 1).matches(
                    ".*" + "Adding mark 4 on date 10-03-2016 and subject Maths to student Sidorov"
                            + " of group 1" + LogMessage.COMPLETED_CHANGING));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void canLogCancellingChanges() {
        addMarkViewModel.logCancelledChangingTable();

        List<String> messagesInLog = addMarkViewModel.getLog();

        assertTrue(messagesInLog.get(messagesInLog.size() -  1).matches(
                ".*" + "Adding mark" + LogMessage.CANCELLED_CHANGING));
    }
}
