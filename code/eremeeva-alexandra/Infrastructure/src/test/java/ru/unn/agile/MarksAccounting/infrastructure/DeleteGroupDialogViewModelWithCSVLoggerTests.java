package ru.unn.agile.MarksAccounting.infrastructure;

import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import ru.unn.agile.MarksAccounting.viewmodel.*;

public class DeleteGroupDialogViewModelWithCSVLoggerTests extends DeleteGroupDialogViewModelTests {
    @Override
    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        setDeleteGroupViewModel(new DeleteGroupDialogViewModel(tempTableOfMarks,
                new CSVLogger("./CSVTestLogForDeleteGroupDialog.csv")));
    }
}
