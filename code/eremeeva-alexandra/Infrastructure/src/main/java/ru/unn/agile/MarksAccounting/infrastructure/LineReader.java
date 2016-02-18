package ru.unn.agile.MarksAccounting.infrastructure;

import java.io.FileReader;
import java.io.IOException;

public final class LineReader {
    private LineReader() { }

    public static String readLine(final FileReader reader) throws IOException {
        String line = "";
        int symbol = reader.read();
        while ((char) symbol != '\n') {
            line += (char) symbol;
            symbol = reader.read();
        }
        return line;
    }
}
