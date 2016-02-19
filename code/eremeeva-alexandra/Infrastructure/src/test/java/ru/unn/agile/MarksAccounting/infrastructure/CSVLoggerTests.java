package ru.unn.agile.MarksAccounting.infrastructure;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ru.unn.agile.MarksAccounting.viewmodel.WindowType;
import ru.unn.agile.MarksAccounting.viewmodel.LogMessage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class CSVLoggerTests {
    private static final String FILENAME = "./CSVLoggerTests.csv";
    private CSVLogger csvLogger;

    @Before
    public void setUp() {
        csvLogger = new CSVLogger(FILENAME);
    }

    @Test
    public void canCreateLogger() {
        assertNotNull(csvLogger);
    }

    @Test
    public void canCreateFileForLogging() {
        try {
            new FileReader(FILENAME);
        } catch (FileNotFoundException e) {
            fail("File " + FILENAME + " wasn't found!");
        }
    }

    @Test
    public void canWriteLogMessage() {
        String testMessage = "Test message";

        csvLogger.log(testMessage);

        String message = csvLogger.getLog(WindowType.MAIN).get(0);
        assertTrue(message.matches(".*" + testMessage));
    }

    @Test
    public void canWriteSeveralLogMessage() {
        String[] messages = {"Test message 1", "Test message 2"};

        csvLogger.log(messages[0]);
        csvLogger.log(messages[1]);

        List<String> messagesInLog = csvLogger.getLog(WindowType.MAIN);
        for (int i = 0; i < messagesInLog.size(); i++) {
            assertTrue(messagesInLog.get(i).matches(".*" + messages[i]));
        }
    }

    @Test
    public void doesLogContainDateAndTime() {
        String testMessage = "Test message";

        csvLogger.log(testMessage);

        String message = csvLogger.getLog(WindowType.MAIN).get(0);
        assertTrue(message.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2};.*"));
    }

    @Test
    public void canExtractMessagesForMainWindow() {
        String[] messages = createMessages();
        for (int i = 0; i < messages.length; i++) {
            csvLogger.log(messages[i]);
        }

        List<String> messagesInLog = csvLogger.getLog(WindowType.MAIN);

        assertEquals(5, messagesInLog.size());
        for (int i = 0; i < messagesInLog.size(); i++) {
            int j = i;
            if (j > 1) {
                j += 2;
            }
            assertTrue(messagesInLog.get(i).matches(".*" + messages[j]));
        }
    }

    @Test
    public void canExtractMessagesForDialogWindow() {
        String[] messages = createMessages();
        for (int i = 0; i < messages.length - 2; i++) {
            csvLogger.log(messages[i]);
        }

        List<String> messagesInLog = csvLogger.getLog(WindowType.DIALOG);

        assertEquals(4, messagesInLog.size());
        for (int i = 0; i < messagesInLog.size(); i++) {
            int j = i + 1;
            assertTrue(messagesInLog.get(i).matches(".*" + messages[j]));
        }
    }

    private String[] createMessages() {
        String[] messages = {LogMessage.ACTION_CHANGED,
                LogMessage.DIALOG_ACTIVATED,
                LogMessage.GROUP_CHANGED,
                LogMessage.TRIED_CHANGING,
                LogMessage.COMPLETED_CHANGING,
                LogMessage.DIALOG_DEACTIVATED,
                LogMessage.TABLE_SAVED};
        return messages;
    }

}
