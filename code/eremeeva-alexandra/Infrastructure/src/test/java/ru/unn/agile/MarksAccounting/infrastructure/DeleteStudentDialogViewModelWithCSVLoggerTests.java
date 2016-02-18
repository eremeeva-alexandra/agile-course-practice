package ru.unn.agile.MarksAccounting.infrastructure;


import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import ru.unn.agile.MarksAccounting.viewmodel.*;

public class DeleteStudentDialogViewModelWithCSVLoggerTests
        extends DeleteStudentDialogViewModelTests {
    @Override
    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        setDeleteStudentViewModel(new DeleteStudentDialogViewModel(tempTableOfMarks,
                new CSVLogger("./CSVTestLogForDeleteStudentDialog.csv")));
    }
}
