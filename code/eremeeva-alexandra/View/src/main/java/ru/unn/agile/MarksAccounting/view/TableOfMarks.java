package ru.unn.agile.MarksAccounting.view;

import ru.unn.agile.MarksAccounting.infrastructure.CSVLogger;
import ru.unn.agile.MarksAccounting.infrastructure.TableReader;
import ru.unn.agile.MarksAccounting.infrastructure.TableSaver;
import ru.unn.agile.MarksAccounting.viewmodel.DialogType;
import ru.unn.agile.MarksAccounting.viewmodel.MainFormViewModel;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public final class TableOfMarks {
    private MainFormViewModel mainFormViewModel;
    private JPanel mainPanel;
    private JComboBox<String> groupsComboBox;
    private JComboBox<String> subjectsComboBox;
    private JLabel groupLabel;
    private JLabel subjectLabel;
    private JPanel auxiliaryPanel;
    private JPanel dataPanel;
    private JScrollPane scrollTable;
    private JComboBox<String> changingComboBox;
    private JButton changingButton;
    private JTable dataTable;
    private JPanel changingPanel;
    private JButton saveButton;
    private JButton saveAsButton;
    private JList<String> logList;
    private JButton openButton;
    private File currentFile;

    private TableOfMarks() { }

    private TableOfMarks(final MainFormViewModel mainFormViewModel) {
        this.mainFormViewModel = mainFormViewModel;
        currentFile = null;
        this.changingComboBox.setModel(mainFormViewModel.getChangingComboBoxModel());
        TableOfMarks.this.mainFormViewModel.logActionChanging("add group");
        backBind();

        changingButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                bind();
                TableOfMarks.this.mainFormViewModel.logDialogActivating();
                makeDialog(DialogType.getType(changingComboBox.getSelectedItem().toString()));
                TableOfMarks.this.mainFormViewModel.logDialogDeactivating();
                updateComboBoxes();
                backBind();
            }
        });

        groupsComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    bind();
                    subjectsComboBox.setModel(
                            TableOfMarks.this.mainFormViewModel.getSubjectComboBoxModel());
                    backBind();
                }
            }
        });

        subjectsComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    bind();
                    backBind();
                }
            }
        });

        changingComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    TableOfMarks.this.mainFormViewModel.logActionChanging(
                            changingComboBox.getSelectedItem().toString());
                    backBind();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                if (currentFile == null) {
                    chooseFileForSaving();
                }
                saveTable();
            }
        });

        saveAsButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                chooseFileForSaving();
                saveTable();
            }
        });

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(filter);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    currentFile = fileChooser.getSelectedFile();
                }
                openTable();
            }
        });
    }

    public static void main(final String[] args) {
        String logPath = "./TableOfMarksLog";
        String ext = ".csv";
        File logFile = new File(logPath + ext);
        long i = 0;
        while (logFile.exists()) {
            logPath = "./TableOfMarksLog" + Long.toString(i);
            logFile = new File(logPath + ext);
            i++;
        }
        CSVLogger logger = new CSVLogger(logPath + ext);
        JFrame frame = new JFrame("Table of marks");
        frame.setContentPane(new TableOfMarks(new MainFormViewModel(logger)).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void bind() {
        mainFormViewModel.setGroupInCurrentTable(groupsComboBox.getSelectedItem());
        mainFormViewModel.setSubjectInCurrentTable(subjectsComboBox.getSelectedItem());
    }

    private void backBind() {
        DefaultTableModel tableModel;
        tableModel = mainFormViewModel.getTableModel();
        dataTable.setModel(tableModel);
        dataTable.setTableHeader(null);

        List<String> log = mainFormViewModel.getLog();
        String[] items = log.toArray(new String[log.size()]);
        logList.setListData(items);
    }

    private void makeDialog(final DialogType dialogType) {
        ChangingDialog changingDialog = new ChangingDialog(mainFormViewModel.getTableOfMarks(),
                dialogType, mainFormViewModel.getLogger());
        changingDialog.pack();
        changingDialog.setVisible(true);
    }

    private void chooseFileForSaving() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
        }
    }

    private void saveTable() {
        if (currentFile != null) {
            try {
                TableSaver.save(currentFile.toString(),
                        TableOfMarks.this.mainFormViewModel.getTableOfMarks());
                TableOfMarks.this.mainFormViewModel.logTableSaving(currentFile.toString());
                backBind();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Can't save file!");
            }
        }
    }

    private void openTable() {
        if (currentFile != null) {
            try {
                TableOfMarks.this.mainFormViewModel.setTableOfMarks(TableReader.readFile(
                        currentFile.toString()));
                TableOfMarks.this.mainFormViewModel.logTableOpening(currentFile.toString());
                updateComboBoxes();
                TableOfMarks.this.mainFormViewModel.logActionChanging("add group");
                backBind();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Can't open file!");
            }
        }
    }

    private void updateComboBoxes() {
        changingComboBox.setModel(
                TableOfMarks.this.mainFormViewModel.getChangingComboBoxModel());
        groupsComboBox.setModel(
                TableOfMarks.this.mainFormViewModel.getGroupComboBoxModel());
        subjectsComboBox.setModel(
                TableOfMarks.this.mainFormViewModel.getSubjectComboBoxModel());
    }

    private void createUIComponents() {
        dataTable = new JTable() {
            @Override
            public boolean isCellEditable(final int rowNumber, final int colNumber) {
                return false;
            }
        };
    }
}
