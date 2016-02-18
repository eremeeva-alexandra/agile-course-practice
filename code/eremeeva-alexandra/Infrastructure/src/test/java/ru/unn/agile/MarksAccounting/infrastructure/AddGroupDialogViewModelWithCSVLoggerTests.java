package ru.unn.agile.MarksAccounting.infrastructure;

import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import ru.unn.agile.MarksAccounting.viewmodel.AddGroupDialogViewModel;
import ru.unn.agile.MarksAccounting.viewmodel.AddGroupDialogViewModelTests;
import ru.unn.agile.MarksAccounting.viewmodel.TestDataInitializer;

public class AddGroupDialogViewModelWithCSVLoggerTests extends AddGroupDialogViewModelTests {
    @Override
    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        setAddGroupViewModel(new AddGroupDialogViewModel(tempTableOfMarks,
                new CSVLogger("./CSVTestLogForAddGroupDialog.csv")));
    }
}
