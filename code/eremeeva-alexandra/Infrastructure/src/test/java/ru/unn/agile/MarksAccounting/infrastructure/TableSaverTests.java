package ru.unn.agile.MarksAccounting.infrastructure;

import org.junit.Test;
import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import static org.junit.Assert.*;
import ru.unn.agile.MarksAccounting.viewmodel.TestDataInitializer;

public class TableSaverTests {
    @Test
    public void canSaveTable() {
        TableOfMarks tableOfMarks = TestDataInitializer.initTableOfMarks();

        try {
            TableSaver.save("./TestingTableSaver.txt", tableOfMarks);
            assertEquals(tableOfMarks, TableReader.readFile("./TestingTableSaver.txt"));
        } catch (Exception e) {
            fail();
        }
    }
}
