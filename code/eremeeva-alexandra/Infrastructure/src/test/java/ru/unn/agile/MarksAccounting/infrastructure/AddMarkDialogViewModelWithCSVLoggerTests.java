package ru.unn.agile.MarksAccounting.infrastructure;

import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import ru.unn.agile.MarksAccounting.viewmodel.*;

public class AddMarkDialogViewModelWithCSVLoggerTests extends AddMarkDialogViewModelTests {
    @Override
    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        setAddMarkViewModel(new AddMarkDialogViewModel(tempTableOfMarks,
                new CSVLogger("./CSVTestLogForAddMarkDialog.csv")));
    }
}
