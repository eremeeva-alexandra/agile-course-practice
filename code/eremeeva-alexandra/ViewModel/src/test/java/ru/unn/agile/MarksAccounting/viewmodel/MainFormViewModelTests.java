package ru.unn.agile.MarksAccounting.viewmodel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.unn.agile.MarksAccounting.model.Group;
import ru.unn.agile.MarksAccounting.model.Student;
import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class MainFormViewModelTests {
    private MainFormViewModel mainFormViewModel;

    @Before
    public void setUp() {
        initModel();
    }

    @After
    public void tearDown() {
        mainFormViewModel = null;
    }

    private void initModel() {
        mainFormViewModel = new MainFormViewModel(new TestLogger());
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        mainFormViewModel.setTableOfMarks(tempTableOfMarks);
    }

    protected void setMainFormViewModel(final MainFormViewModel mainFormViewModel) {
        this.mainFormViewModel = mainFormViewModel;
    }

    protected MainFormViewModel getMainFormViewModel() {
        return this.mainFormViewModel;
    }

    @Test
    public void canGetRightChangingComboBoxModelWhenTableIsEmpty() {
        String[] actions = {"Add group"};
        ComboBoxModel<String> trueComboBoxModel = new JComboBox<String>(actions).getModel();

        TableOfMarks tableOfMarks = new TableOfMarks();
        mainFormViewModel.setTableOfMarks(tableOfMarks);

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(trueComboBoxModel,
                mainFormViewModel.getChangingComboBoxModel()));
    }

    @Test
    public void canGetRightChangingComboBoxModelWhenGroupsExist() {
        String[] actions = {"Add group", "Add subject", "Add student", "Delete group"};
        ComboBoxModel<String> trueComboBoxModel = new JComboBox<String>(actions).getModel();

        TableOfMarks tableOfMarks = new TableOfMarks();
        tableOfMarks.addGroup(new Group("1"));
        mainFormViewModel.setTableOfMarks(tableOfMarks);

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(trueComboBoxModel,
                mainFormViewModel.getChangingComboBoxModel()));
    }

    @Test
    public void canGetRightChangingComboBoxModelWhenSubjectsExist() {
        String[] actions = {"Add group", "Add subject", "Add student", "Delete group",
                "Delete subject"};
        ComboBoxModel<String> trueComboBoxModel = new JComboBox<String>(actions).getModel();

        TableOfMarks tableOfMarks = new TableOfMarks();
        tableOfMarks.addGroup(new Group("1"));
        tableOfMarks.addAcademicSubject(new Group("1"), "Maths");
        mainFormViewModel.setTableOfMarks(tableOfMarks);

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(trueComboBoxModel,
                mainFormViewModel.getChangingComboBoxModel()));
    }

    @Test
    public void canGetRightChangingComboBoxModelWhenStudentsExist() {
        String[] actions = {"Add group", "Add subject", "Add student", "Delete group",
                "Delete student"};
        ComboBoxModel<String> trueComboBoxModel = new JComboBox<String>(actions).getModel();

        TableOfMarks tableOfMarks = new TableOfMarks();
        tableOfMarks.addGroup(new Group("1"));
        tableOfMarks.addStudent(new Group("1"), new Student("Petrov"));
        mainFormViewModel.setTableOfMarks(tableOfMarks);

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(trueComboBoxModel,
                mainFormViewModel.getChangingComboBoxModel()));
    }

    @Test
    public void canGetRightChangingComboBoxModelWhenCanAddMark() {
        String[] actions = {"Add group", "Add subject", "Add student", "Add mark", "Delete group",
                "Delete subject", "Delete student"};
        ComboBoxModel<String> trueComboBoxModel = new JComboBox<String>(actions).getModel();

        TableOfMarks tableOfMarks = new TableOfMarks();
        tableOfMarks.addGroup(new Group("2"));
        tableOfMarks.addStudent(new Group("2"), new Student("Sidorov"));
        tableOfMarks.addAcademicSubject(new Group("2"), "History");
        mainFormViewModel.setTableOfMarks(tableOfMarks);

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(trueComboBoxModel,
                mainFormViewModel.getChangingComboBoxModel()));
    }

    @Test
    public void canGetRightChangingComboBoxModelWhenMarksExist() {
        String[] actions = {"Add group", "Add subject", "Add student", "Add mark", "Delete group",
                "Delete subject", "Delete student", "Delete mark"};
        ComboBoxModel<String> trueComboBoxModel = new JComboBox<String>(actions).getModel();

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(trueComboBoxModel,
                mainFormViewModel.getChangingComboBoxModel()));
    }

    @Test
    public void canGetGroupComboBoxModel() {
        String[] groupNumbers = {"1", "2", "3"};
        ComboBoxModel<String> trueComboBoxModel = new JComboBox<String>(groupNumbers).getModel();

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(trueComboBoxModel,
                mainFormViewModel.getGroupComboBoxModel()));
    }

    @Test
    public void canGetSubjectComboBoxModel() {
        String[] subjects = {"History", "Maths"};
        ComboBoxModel<String> trueComboBoxModel = new JComboBox<String>(subjects).getModel();

        mainFormViewModel.setGroupInCurrentTable("1");

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(trueComboBoxModel,
                mainFormViewModel.getSubjectComboBoxModel()));
    }

    @Test
    public void getEmptyScubjectComboBoxWhenSubjectsDoNotExist() {
        ComboBoxModel<String> trueComboBoxModel =
                new JComboBox<String>(new String[0]).getModel();

        mainFormViewModel = new MainFormViewModel(new TestLogger());

        assertTrue(ComboBoxModelsEqualer.comboBoxModelsEqualing(trueComboBoxModel,
                mainFormViewModel.getSubjectComboBoxModel()));
    }

    @Test
    public void canSetGroupInCurrentTableWhenGroupsExist() {
        mainFormViewModel.setGroupInCurrentTable("1");

        assertEquals("1", mainFormViewModel.getGroupInCurrentTable());
    }

    @Test
    public void canSetGroupInCurrentTableWhenNoGroupExists() {
        mainFormViewModel.setGroupInCurrentTable(null);

        assertEquals("", mainFormViewModel.getGroupInCurrentTable());
    }

    @Test
    public void canSetSubjectInCurrentTableWhenSubjectsExist() {
        mainFormViewModel.setSubjectInCurrentTable("Maths");

        assertEquals("Maths", mainFormViewModel.getSubjectInCurrentTable());
    }

    @Test
    public void canSetSubjectInCurrentTableWhenNoSubjectExists() {
        mainFormViewModel.setSubjectInCurrentTable(null);

        assertEquals("", mainFormViewModel.getSubjectInCurrentTable());
    }

    @Test
    public void canGetTableModel() {
        DefaultTableModel trueTableModel = new DefaultTableModel(3, 2);
        trueTableModel.setValueAt("Students", 0, 0);
        trueTableModel.setValueAt("Petrov", 1, 0);
        trueTableModel.setValueAt("Sidorov", 2, 0);
        trueTableModel.setValueAt("10-05-2015", 0, 1);
        trueTableModel.setValueAt("", 1, 1);
        trueTableModel.setValueAt("4", 2, 1);

        mainFormViewModel.setGroupInCurrentTable("1");
        mainFormViewModel.setSubjectInCurrentTable("Maths");
        DefaultTableModel tableModel = mainFormViewModel.getTableModel();

        assertTrue(tableModelsEqualing(trueTableModel, tableModel));
    }

    private boolean tableModelsEqualing(final TableModel expectedTableModel,
                                        final TableModel realTableModel) {
        for (int i = 0; i < realTableModel.getRowCount(); i++) {
            for (int j = 0; j < realTableModel.getColumnCount(); j++) {
                if (!expectedTableModel.getValueAt(i, j).equals(realTableModel.getValueAt(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    public void canCreateViewModelWithLogger() {
        TestLogger logger = new TestLogger();
        MainFormViewModel viewModelWithLogger = new MainFormViewModel(logger);

        assertNotNull(viewModelWithLogger);
    }

    @Test
    public void viewModelConstructorThrowsExceptionWithNullLogger() {
        try {
            new MainFormViewModel(null);
            fail();
        } catch (IllegalArgumentException exception) {
            assertEquals("Logger can't be null", exception.getMessage());
        } catch (Exception exception) {
            fail();
        }
    }

    @Test
    public void logIsEmptyAtTheBeginning() {
        List<String> log = mainFormViewModel.getLog();

        assertEquals(0, log.size());
    }

    @Test
    public void canPutSeveralLogMessages() {
        mainFormViewModel.logActionChanging("add group");
        mainFormViewModel.setGroupInCurrentTable("1");
        mainFormViewModel.logActionChanging("add student");

        assertEquals(3, mainFormViewModel.getLog().size());
    }

    @Test
    public void canLogGroupChanging() {
        mainFormViewModel.setGroupInCurrentTable("1");

        List<String> messagesInLog = mainFormViewModel.getLog();

        assertEquals(1, messagesInLog.size());
        assertTrue(messagesInLog.get(0).matches(".*"
                + LogMessage.GROUP_CHANGED + "1."));
    }

    @Test
    public void canNotLogWhenGroupWasNotChanged() {
        mainFormViewModel.setGroupInCurrentTable(null);
        mainFormViewModel.setGroupInCurrentTable("4");
        mainFormViewModel.setGroupInCurrentTable("4");

        List<String> messagesInLog = mainFormViewModel.getLog();

        assertEquals(1, messagesInLog.size());
        assertTrue(messagesInLog.get(0).matches(".*"
                + LogMessage.GROUP_CHANGED + "4."));
    }

    @Test
    public void canLogSubjectChanging() {
        mainFormViewModel.setSubjectInCurrentTable("History");

        List<String> messagesInLog = mainFormViewModel.getLog();

        assertEquals(1, messagesInLog.size());
        assertTrue(messagesInLog.get(0).matches(".*"
                + LogMessage.SUBJECT_CHANGED + "History."));
    }

    @Test
    public void canNotLogWhenSubjectWasNotChanged() {
        mainFormViewModel.setSubjectInCurrentTable(null);
        mainFormViewModel.setSubjectInCurrentTable("Art");
        mainFormViewModel.setSubjectInCurrentTable("Art");

        List<String> messagesInLog = mainFormViewModel.getLog();

        assertEquals(1, messagesInLog.size());
        assertTrue(messagesInLog.get(0).matches(".*"
                + LogMessage.SUBJECT_CHANGED + "Art."));
    }

    @Test
    public void canLogActionChanging() {
        mainFormViewModel.logActionChanging("Add group");

        List<String> messagesInLog = mainFormViewModel.getLog();

        assertEquals(1, messagesInLog.size());
        assertTrue(messagesInLog.get(0).matches(".*"
                + LogMessage.ACTION_CHANGED + "add group."));
    }

    @Test
    public void canLogDialog() {
        mainFormViewModel.logDialogActivating();
        mainFormViewModel.getLogger().log("Changing cancelled");
        mainFormViewModel.logDialogDeactivating();

        List<String> messagesInLog = mainFormViewModel.getLog();

        assertEquals(3, messagesInLog.size());
        assertTrue(messagesInLog.get(0).matches(".*" + LogMessage.DIALOG_ACTIVATED));
        assertTrue(messagesInLog.get(2).matches(".*" + LogMessage.DIALOG_DEACTIVATED));
    }

    @Test
    public void canLogSavingTable() {
        mainFormViewModel.logTableSaving("./TestTable.txt");

        List<String> messagesInLog = mainFormViewModel.getLog();

        assertEquals(1, messagesInLog.size());
        assertTrue(messagesInLog.get(0).matches(".*"
                + LogMessage.TABLE_SAVED + "./TestTable.txt."));
    }

    @Test
    public void canLogOpeningTable() {
        mainFormViewModel.logTableOpening("./TestTable.txt");

        List<String> messagesInLog = mainFormViewModel.getLog();

        assertEquals(1, messagesInLog.size());
        assertTrue(messagesInLog.get(0).matches(".*"
                + LogMessage.TABLE_OPENED + "./TestTable.txt."));
    }
}
