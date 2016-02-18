package ru.unn.agile.MarksAccounting.infrastructure;

import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import ru.unn.agile.MarksAccounting.viewmodel.MainFormViewModel;
import ru.unn.agile.MarksAccounting.viewmodel.MainFormViewModelTests;
import ru.unn.agile.MarksAccounting.viewmodel.TestDataInitializer;

public class MainFormViewModelWithCSVLoggerTests extends MainFormViewModelTests {
    @Override
    public void setUp() {
        setMainFormViewModel(new MainFormViewModel(new CSVLogger("./CSVTestLogForMain.csv")));
        TableOfMarks tempTableOfMarks = TestDataInitializer.initTableOfMarks();
        getMainFormViewModel().setTableOfMarks(tempTableOfMarks);
    }
}
