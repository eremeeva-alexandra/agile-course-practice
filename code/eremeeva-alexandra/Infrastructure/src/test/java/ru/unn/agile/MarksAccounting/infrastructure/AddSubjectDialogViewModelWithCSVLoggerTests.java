package ru.unn.agile.MarksAccounting.infrastructure;

import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import ru.unn.agile.MarksAccounting.viewmodel.AddSubjectDialogViewModel;
import ru.unn.agile.MarksAccounting.viewmodel.AddSubjectDialogViewModelTests;
import ru.unn.agile.MarksAccounting.viewmodel.TestDataInitializer;

public class AddSubjectDialogViewModelWithCSVLoggerTests extends AddSubjectDialogViewModelTests {
    @Override
    protected void initModel() {
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        setAddSubjectViewModel(new AddSubjectDialogViewModel(tempTableOfMarks,
                new CSVLogger("./CSVTestLogForAddSubjectDialog.csv")));
    }
}
