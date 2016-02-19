package ru.unn.agile.MarksAccounting.infrastructure;

import ru.unn.agile.MarksAccounting.viewmodel.WindowType;
import ru.unn.agile.MarksAccounting.viewmodel.ILogger;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVLogger implements ILogger {
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private final FileWriter fileWriter;
    private final String fileName;

    private static String getTimeNow() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return format.format(calendar.getTime());
    }

    public CSVLogger(final String filename) {
        this.fileName = filename;

        FileWriter logWriter = null;
        try {
            logWriter = new FileWriter(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileWriter = logWriter;
    }

    public void log(final String message) {
        try {
            fileWriter.write(getTimeNow() + ";" + message + "\n");
            fileWriter.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> getLog(final WindowType windowType) {
        FileReader fileReader;
        ArrayList<String> log = new ArrayList<String>();
        try {
            fileReader = new FileReader(fileName);
            if (windowType == WindowType.DIALOG) {
                log = getLogForLastDialog(fileReader);
            } else {
                log = getLogForMainWindow(fileReader);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return log;
    }

    private ArrayList<String> getLogForLastDialog(final FileReader fileReader) throws IOException {
        String line;
        ArrayList<String> fullLog = new ArrayList<String>();
        ArrayList<String> resultLog = new ArrayList<String>();
        while (fileReader.ready()) {
            line = LineReader.readLine(fileReader);
            fullLog.add(line);
        }
        int i;
        for (i = fullLog.size() - 1; i >= 0; i--) {
            if ("Dialog window activated.".equals(getAction(fullLog.get(i)))) {
                break;
            }
        }
        for (int j = i; j < fullLog.size(); j++) {
            resultLog.add(fullLog.get(j));
        }
        return resultLog;
    }

    private ArrayList<String> getLogForMainWindow(final FileReader fileReader) throws IOException {
        String line;
        ArrayList<String> resLog = new ArrayList<String>();
        while (fileReader.ready()) {
            line = LineReader.readLine(fileReader);
            if ("Dialog window activated.".equals(getAction(line))) {
                resLog.add(line);
                resLog.add(line);
                while (!"Dialog window deactivated.".equals(getAction(line))
                        && fileReader.ready()) {
                    resLog.set(resLog.size() - 1, line);
                    line = LineReader.readLine(fileReader);
                }
            }
            resLog.add(line);
        }
        return resLog;
    }

    private String getAction(final String fullLine) {
        int i;
        for (i = 0; i < fullLine.length(); i++) {
            if (fullLine.charAt(i) == ';') {
                break;
            }
        }
        return fullLine.substring(i + 1);
    }
}
