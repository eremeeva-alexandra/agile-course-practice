package ru.unn.agile.MarksAccounting.view;

import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import ru.unn.agile.MarksAccounting.viewmodel.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.List;

public class ChangingDialog extends JDialog {
    private DialogViewModel dialogViewModel;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> groupComboBox;
    private JComboBox<String> studentComboBox;
    private JComboBox<String> subjectComboBox;
    private JTextField inputTextField;
    private JTextField dateField;
    private JLabel dialogTitle;
    private JLabel dateLabel;
    private JLabel subjectLabel;
    private JLabel studentLabel;
    private JLabel groupLabel;
    private JPanel inputPanel;
    private JPanel datePanel;
    private JList<String> logList;

    public ChangingDialog(final TableOfMarks tableOfMarks,
                          final DialogType typeOfDialog, final ILogger logger) {
        this.dialogTitle.setText(typeOfDialog.getTypeDescription());
        initDialogViewModel(tableOfMarks, typeOfDialog, logger);
        setContentPane(this.contentPane);
        setModal(true);
        getRootPane().setDefaultButton(this.buttonOK);
        this.groupComboBox.setModel(dialogViewModel.getDialogGroupsComboBoxModel());
        this.studentComboBox.setModel(dialogViewModel.getDialogStudentsComboBoxModel());
        backBind();

        groupComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    bind();
                    studentComboBox.setModel(dialogViewModel.getDialogStudentsComboBoxModel());
                    backBind();
                }
            }
        });

        studentComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    bind();
                    backBind();
                }
            }
        });

        subjectComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    bind();
                }
            }
        });

        dateField.addFocusListener(new FocusListener() {
            public void focusGained(final FocusEvent e) {
                dateField.getHeight();
            }

            public void focusLost(final FocusEvent e) {
                bind();
                backBind();
            }
        });

        inputTextField.addFocusListener(new FocusListener() {
            public void focusGained(final FocusEvent e) {
                inputTextField.getHeight();
            }

            public void focusLost(final FocusEvent e) {
                bind();
                backBind();
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                bind();
                try {
                    ChangingDialog.this.dialogViewModel.changeTableOfMarks();
                    backBind();
                    dispose();
                } catch (NumberFormatException exception) {
                    ChangingDialog.this.dialogViewModel.logErrorWhileChangingTable(
                            "Mark is not an integer!");
                    backBind();
                    JOptionPane.showMessageDialog(null, "Mark must be an integer value!");
                } catch (ParseException exception) {
                    ChangingDialog.this.dialogViewModel.logErrorWhileChangingTable(
                            "Wrong format of date!");
                    backBind();
                    JOptionPane.showMessageDialog(null, "Wrong format of date!");
                } catch (RuntimeException exception) {
                    ChangingDialog.this.dialogViewModel.logErrorWhileChangingTable(
                            exception.getMessage());
                    backBind();
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ChangingDialog.this.dialogViewModel.logCancelledChangingTable();
                dispose();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                ChangingDialog.this.dialogViewModel.logCancelledChangingTable();
                dispose();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ChangingDialog.this.dialogViewModel.logCancelledChangingTable();
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void bind() {
        dialogViewModel.setDialogGroup(groupComboBox.getSelectedItem());
        dialogViewModel.setDialogDate(dateField.getText());
        dialogViewModel.setDialogStudent(studentComboBox.getSelectedItem());
        dialogViewModel.setDialogSubject(subjectComboBox.getSelectedItem());
        try {
            dialogViewModel.setDialogInputTextBox(inputTextField.getText());
        } catch (InputIsTagException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
            dialogViewModel.logErrorWhileChangingTable(exception.getMessage());
            inputTextField.setText("");
            dialogViewModel.setDialogInputTextBox(inputTextField.getText());
            List<String> log = dialogViewModel.getLog();
            String[] items = log.toArray(new String[log.size()]);
            logList.setListData(items);
        }
    }

    private void backBind() {
        groupComboBox.setVisible(dialogViewModel.isDialogGroupBoxVisible());
        studentComboBox.setVisible(dialogViewModel.isDialogStudentBoxVisible());
        subjectComboBox.setModel(dialogViewModel.getDialogSubjectsComboBoxModel());
        subjectComboBox.setVisible(dialogViewModel.isDialogSubjectBoxVisible());
        dateField.setVisible(dialogViewModel.isDialogDateTextBoxVisible());
        inputTextField.setVisible(dialogViewModel.isDialogInputTextBoxVisible());
        groupLabel.setVisible(dialogViewModel.isDialogGroupBoxVisible());
        studentLabel.setVisible(dialogViewModel.isDialogStudentBoxVisible());
        subjectLabel.setVisible(dialogViewModel.isDialogSubjectBoxVisible());
        dateLabel.setVisible(dialogViewModel.isDialogDateTextBoxVisible());
        List<String> log = dialogViewModel.getLog();
        String[] items = log.toArray(new String[log.size()]);
        logList.setListData(items);
    }

    private void initDialogViewModel(final TableOfMarks tableOfMarks,
                                     final DialogType typeOfDialog, final ILogger logger) {
        switch (typeOfDialog) {
            case ADD_GROUP:
                dialogViewModel = new AddGroupDialogViewModel(tableOfMarks, logger);
                break;
            case ADD_STUDENT:
                dialogViewModel = new AddStudentDialogViewModel(tableOfMarks, logger);
                break;
            case ADD_SUBJECT:
                dialogViewModel = new AddSubjectDialogViewModel(tableOfMarks, logger);
                break;
            case ADD_MARK:
                dialogViewModel = new AddMarkDialogViewModel(tableOfMarks, logger);
                break;
            case DELETE_GROUP:
                dialogViewModel = new DeleteGroupDialogViewModel(tableOfMarks, logger);
                break;
            case DELETE_STUDENT:
                dialogViewModel = new DeleteStudentDialogViewModel(tableOfMarks, logger);
                break;
            case DELETE_SUBJECT:
                dialogViewModel = new DeleteSubjectDialogViewModel(tableOfMarks, logger);
                break;
            case DELETE_MARK:
                dialogViewModel = new DeleteMarkDialogViewModel(tableOfMarks, logger);
                break;
            default:
                break;
        }
    }
}
