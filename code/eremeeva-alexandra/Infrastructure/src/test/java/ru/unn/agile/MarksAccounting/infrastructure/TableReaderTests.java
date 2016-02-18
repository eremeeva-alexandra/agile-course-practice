package ru.unn.agile.MarksAccounting.infrastructure;

import static org.junit.Assert.*;
import org.junit.Test;
import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import ru.unn.agile.MarksAccounting.viewmodel.TestDataInitializer;

public class TableReaderTests {
    @Test
    public void canReadFile() {
        TableOfMarks tableOfMarks = TestDataInitializer.initTableOfMarks();

        try {
            assertEquals(tableOfMarks, TableReader.readFile("./TestTable.txt"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void canNotReadBadFormattedFile() {
        try {
            TableReader.readFile("./WrongTestFile.txt");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}
