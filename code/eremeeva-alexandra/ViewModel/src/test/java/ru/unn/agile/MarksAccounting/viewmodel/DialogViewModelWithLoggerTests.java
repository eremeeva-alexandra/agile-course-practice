package ru.unn.agile.MarksAccounting.viewmodel;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ru.unn.agile.MarksAccounting.model.GroupAlreadyExistsException;
import java.util.List;

public class DialogViewModelWithLoggerTests {
    private DialogViewModel dialogViewModel;

    public void setDialogViewModel(final DialogViewModel dialogViewModel) {
        this.dialogViewModel = dialogViewModel;
    }

    @Before
    public void setUp() {
        dialogViewModel = new TestDialogViewModel(new TestLogger());
        dialogViewModel.getLogger().log(LogMessage.DIALOG_ACTIVATED);
    }

    @After
    public void tearDown() {
        dialogViewModel = null;
    }

    @Test(expected = InputIsTagException.class)
    public void canNotSetTagAsInput() {
        dialogViewModel.setDialogInputTextBox("@Group");
    }

    @Test
    public void canPutSeveralLogMessages() {
        dialogViewModel.setDialogInputTextBox("5");
        dialogViewModel.setDialogGroup("1");
        dialogViewModel.setDialogStudent("Smirnov");

        assertEquals(4, dialogViewModel.getLog().size());
    }

    @Test
    public void viewModelConstructorThrowsExceptionWithNullLogger() {
        try {
            createViewModelWithNullLogger();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Logger can't be null", e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    protected void createViewModelWithNullLogger() {
        new TestDialogViewModel(null);
    }

    @Test
    public void canLogGroupChanging() {
        dialogViewModel.setDialogGroup("2");

        List<String> messagesInLog = dialogViewModel.getLog();

        assertEquals(2, messagesInLog.size());
        assertTrue(messagesInLog.get(1).matches(".*"
                + LogMessage.GROUP_CHANGED + "2."));
    }

    @Test
    public void canNotLogWhenGroupWasNotChanged() {
        dialogViewModel.setDialogGroup(null);
        dialogViewModel.setDialogGroup("3");
        dialogViewModel.setDialogGroup("3");

        List<String> messagesInLog = dialogViewModel.getLog();

        assertEquals(2, messagesInLog.size());
        assertTrue(messagesInLog.get(1).matches(".*"
                + LogMessage.GROUP_CHANGED + "3."));
    }

    @Test
    public void canLogSubjectChanging() {
        dialogViewModel.setDialogSubject("Maths");

        List<String> messagesInLog = dialogViewModel.getLog();

        assertEquals(2, messagesInLog.size());
        assertTrue(messagesInLog.get(1).matches(".*"
                + LogMessage.SUBJECT_CHANGED + "Maths."));
    }

    @Test
    public void canNotLogWhenSubjectWasNotChanged() {
        dialogViewModel.setDialogSubject(null);
        dialogViewModel.setDialogSubject("Science");
        dialogViewModel.setDialogSubject("Science");

        List<String> messagesInLog = dialogViewModel.getLog();

        assertEquals(2, messagesInLog.size());
        assertTrue(messagesInLog.get(1).matches(".*"
                + LogMessage.SUBJECT_CHANGED + "Science."));
    }

    @Test
    public void canLogStudentChanging() {
        dialogViewModel.setDialogStudent("Smirnov");

        List<String> messagesInLog = dialogViewModel.getLog();

        assertEquals(2, messagesInLog.size());
        assertTrue(messagesInLog.get(1).matches(".*"
                + LogMessage.STUDENT_CHANGED + "Smirnov."));
    }

    @Test
    public void canNotLogWhenStudentWasNotChanged() {
        dialogViewModel.setDialogStudent(null);
        dialogViewModel.setDialogStudent("Sidorov");
        dialogViewModel.setDialogStudent("Sidorov");

        List<String> messagesInLog = dialogViewModel.getLog();

        assertEquals(2, messagesInLog.size());
        assertTrue(messagesInLog.get(1).matches(".*"
                + LogMessage.STUDENT_CHANGED + "Sidorov."));
    }

    @Test
    public void canLogDateChanging() {
        dialogViewModel.setDialogDate("11-12-2015");

        List<String> messagesInLog = dialogViewModel.getLog();

        assertEquals(2, messagesInLog.size());
        assertTrue(messagesInLog.get(1).matches(".*"
                + LogMessage.DATE_CHANGED + "11-12-2015."));
    }

    @Test
    public void canNotLogWhenDateWasNotChanged() {
        dialogViewModel.setDialogDate("");
        dialogViewModel.setDialogDate("02-02-2016");
        dialogViewModel.setDialogDate("02-02-2016");

        List<String> messagesInLog = dialogViewModel.getLog();

        assertEquals(2, messagesInLog.size());
        assertTrue(messagesInLog.get(1).matches(".*"
                + LogMessage.DATE_CHANGED + "02-02-2016."));
    }

    @Test
    public void canLogInputChanging() {
        dialogViewModel.setDialogInputTextBox("5");

        List<String> messagesInLog = dialogViewModel.getLog();

        assertEquals(2, messagesInLog.size());
        assertTrue(messagesInLog.get(1).matches(".*"
                + LogMessage.INPUT_CHANGED + "5."));
    }

    @Test
    public void canNotLogWhenInputWasNotChanged() {
        dialogViewModel.setDialogInputTextBox("");
        dialogViewModel.setDialogInputTextBox("6");
        dialogViewModel.setDialogInputTextBox("6");

        List<String> messagesInLog = dialogViewModel.getLog();

        assertEquals(2, messagesInLog.size());
        assertTrue(messagesInLog.get(1).matches(".*"
                + LogMessage.INPUT_CHANGED + "6."));
    }

    @Test
    public void canLogErrorMessage() {
        dialogViewModel.logErrorWhileChangingTable(
                new GroupAlreadyExistsException("Group already exists!").getMessage());

        List<String> messagesInLog = dialogViewModel.getLog();

        assertEquals(2, messagesInLog.size());
        assertTrue(messagesInLog.get(1).matches(".*"
                + LogMessage.ERROR + "Group already exists!"));
    }
}
