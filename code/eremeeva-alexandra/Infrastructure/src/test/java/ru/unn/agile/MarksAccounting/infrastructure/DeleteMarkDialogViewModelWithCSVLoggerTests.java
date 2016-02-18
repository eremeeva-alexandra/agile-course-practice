package ru.unn.agile.MarksAccounting.infrastructure;

import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import ru.unn.agile.MarksAccounting.viewmodel.*;

public class DeleteMarkDialogViewModelWithCSVLoggerTests extends DeleteMarkDialogViewModelTests {
    @Override
    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        setDeleteMarkViewModel(new DeleteMarkDialogViewModel(tempTableOfMarks,
                new CSVLogger("./CSVTestLogForDeleteMarkDialog.csv")));
    }
}
