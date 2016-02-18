package ru.unn.agile.MarksAccounting.infrastructure;

import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import ru.unn.agile.MarksAccounting.viewmodel.*;

public class AddStudentDialogViewModelWithCSVLoggerTests extends AddStudentDialogViewModelTests {
    @Override
    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        setAddStudentViewModel(new AddStudentDialogViewModel(tempTableOfMarks,
                new CSVLogger("./CSVTestLogForAddStudentDialog.csv")));
    }
}
