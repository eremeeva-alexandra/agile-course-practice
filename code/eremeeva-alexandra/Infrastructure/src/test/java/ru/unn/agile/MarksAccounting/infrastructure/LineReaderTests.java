package ru.unn.agile.MarksAccounting.infrastructure;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.FileReader;

public class LineReaderTests {
    @Test
    public void canReadLine() {
        try {
            FileReader fileReader = new FileReader("./TestTable.txt");
            assertEquals("@Group", LineReader.readLine(fileReader));
        } catch (Exception e) {
            fail();
        }
    }
}
