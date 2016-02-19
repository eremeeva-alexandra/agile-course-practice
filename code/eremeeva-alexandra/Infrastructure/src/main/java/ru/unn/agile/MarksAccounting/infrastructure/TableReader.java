package ru.unn.agile.MarksAccounting.infrastructure;

import ru.unn.agile.MarksAccounting.model.*;
import ru.unn.agile.MarksAccounting.viewmodel.TableTags;

import java.io.FileReader;
import java.util.GregorianCalendar;

public final class TableReader {
    private TableReader() { }

    public static TableOfMarks readFile(final String fileName) throws Exception {
        FileReader fileReader = new FileReader(fileName);
        TableOfMarks tableOfMarks = new TableOfMarks();
        Group currentGroup;
        while (fileReader.ready()) {
            currentGroup = readGroup(fileReader, tableOfMarks);
            readSubjects(fileReader, currentGroup, tableOfMarks);
            readStudents(fileReader, currentGroup, tableOfMarks);
        }
        fileReader.close();
        return tableOfMarks;
    }

    private static Group readGroup(final FileReader fileReader,
                                   final TableOfMarks tableOfMarks) throws Exception {
        String line;
        if (LineReader.readLine(fileReader).equals(TableTags.GROUP.getTag())) {
            line = LineReader.readLine(fileReader);
            tableOfMarks.addGroup(new Group(line));
            return new Group(line);
        }
        throw new RuntimeException("Bad format");
    }

    private static void readSubjects(final FileReader fileReader, final Group currentGroup,
                                 final TableOfMarks tableOfMarks) throws Exception {
        if (TableTags.SUBJECTS.getTag().equals(LineReader.readLine(fileReader))) {
            String line = LineReader.readLine(fileReader);
            while (!line.equals(TableTags.END_OF_SUBJECTS.getTag())) {
                if (line.equals(TableTags.MARK.getTag()) || line.equals(TableTags.STUDENT.getTag())
                        || line.equals(TableTags.GROUP.getTag())
                        || line.equals(TableTags.SUBJECTS.getTag())) {
                    throw  new RuntimeException("Bad format");
                }
                tableOfMarks.addAcademicSubject(currentGroup, line);
                line = LineReader.readLine(fileReader);
            }
            return;
        }
        throw new RuntimeException("Bad format");
    }

    private static void readStudents(final FileReader fileReader, final Group currentGroup,
                                 final TableOfMarks tableOfMarks) throws Exception {
        String line = LineReader.readLine(fileReader);
        Student currentStudent;
        while (!line.equals(TableTags.END_OF_GROUP.getTag())) {
            if (line.equals(TableTags.STUDENT.getTag())) {
                currentStudent = new Student(LineReader.readLine(fileReader));
                tableOfMarks.addStudent(currentGroup, currentStudent);
                readMarks(fileReader, currentGroup, currentStudent, tableOfMarks);
                line = LineReader.readLine(fileReader);
            } else {
                throw new RuntimeException("Bad format");
            }
        }
    }

    private static void readMarks(final FileReader fileReader, final Group currentGroup,
                              final Student currentStudent, final TableOfMarks tableOfMarks)
            throws Exception {
        String line = LineReader.readLine(fileReader);
        while (!line.equals(TableTags.END_OF_STUDENT.getTag())) {
            if (!line.equals(TableTags.MARK.getTag())) {
                throw new RuntimeException("Bad format");
            }
            readMark(fileReader, currentGroup, currentStudent, tableOfMarks);
            line = LineReader.readLine(fileReader);
        }
    }

    private static void readMark(final FileReader fileReader, final Group currentGroup,
                             final Student currentStudent, final TableOfMarks tableOfMarks)
            throws Exception {
        String line = LineReader.readLine(fileReader);
        GregorianCalendar gregorianCalendar = DateParser.parseDate(LineReader.readLine(fileReader));
        int markValue = Integer.parseInt(LineReader.readLine(fileReader));
        tableOfMarks.addNewMark(new Mark(markValue, line, gregorianCalendar),
                currentStudent, currentGroup);
    }
}
