package ru.unn.agile.MarksAccounting.viewmodel;

import java.util.List;

public interface ILogger {
    void log(final String s);

    List<String> getLog(final WindowType windowType);
}
