package ru.unn.agile.MarksAccounting.infrastructure;

import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import ru.unn.agile.MarksAccounting.viewmodel.*;

public class DeleteSubjectDialogViewModelWithCSVLoggerTests
        extends DeleteSubjectDialogViewModelTests {
    @Override
    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        setDeleteSubjectViewModel(new DeleteSubjectDialogViewModel(tempTableOfMarks,
                new CSVLogger("./CSVTestLogForDeleteSubjectDialog.csv")));
    }
}
