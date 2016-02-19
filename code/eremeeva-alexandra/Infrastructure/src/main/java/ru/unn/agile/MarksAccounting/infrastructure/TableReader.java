package ru.unn.agile.MarksAccounting.infrastructure;

import ru.unn.agile.MarksAccounting.model.*;
import ru.unn.agile.MarksAccounting.viewmodel.TableTags;

import java.io.FileReader;
import java.util.GregorianCalendar;

public final class TableReader {
    private TableReader() { }

    public static TableOfMarks readFile(final String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        TableOfMarks tableOfMarks = new TableOfMarks();
        Group currentGroup;
        while (reader.ready()) {
            currentGroup = readGroup(reader, tableOfMarks);
            readSubjects(reader, currentGroup, tableOfMarks);
            readStudents(reader, currentGroup, tableOfMarks);
        }
        reader.close();
        return tableOfMarks;
    }

    private static Group readGroup(final FileReader reader,
                                   final TableOfMarks tableOfMarks) throws Exception {
        String line;
        if (LineReader.readLine(reader).equals(TableTags.GROUP.getTag())) {
            line = LineReader.readLine(reader);
            tableOfMarks.addGroup(new Group(line));
            return new Group(line);
        }
        throw new RuntimeException("Bad format");
    }

    private static void readSubjects(final FileReader reader, final Group currentGroup,
                                 final TableOfMarks tableOfMarks) throws Exception {
        if (TableTags.SUBJECTS.getTag().equals(LineReader.readLine(reader))) {
            String line = LineReader.readLine(reader);
            while (!line.equals(TableTags.END_OF_SUBJECTS.getTag())) {
                if (line.equals(TableTags.MARK.getTag()) || line.equals(TableTags.STUDENT.getTag())
                        || line.equals(TableTags.GROUP.getTag())
                        || line.equals(TableTags.SUBJECTS.getTag())) {
                    throw  new RuntimeException("Bad format");
                }
                tableOfMarks.addAcademicSubject(currentGroup, line);
                line = LineReader.readLine(reader);
            }
            return;
        }
        throw new RuntimeException("Bad format");
    }

    private static void readStudents(final FileReader reader, final Group currentGroup,
                                 final TableOfMarks tableOfMarks) throws Exception {
        String line = LineReader.readLine(reader);
        Student currentStudent;
        while (!line.equals(TableTags.END_OF_GROUP.getTag())) {
            if (line.equals(TableTags.STUDENT.getTag())) {
                currentStudent = new Student(LineReader.readLine(reader));
                tableOfMarks.addStudent(currentGroup, currentStudent);
                readMarks(reader, currentGroup, currentStudent, tableOfMarks);
                line = LineReader.readLine(reader);
            } else {
                throw new RuntimeException("Bad format");
            }
        }
    }

    private static void readMarks(final FileReader reader, final Group currentGroup,
                              final Student currentStudent, final TableOfMarks tableOfMarks)
            throws Exception {
        String line = LineReader.readLine(reader);
        while (!line.equals(TableTags.END_OF_STUDENT.getTag())) {
            if (!line.equals(TableTags.MARK.getTag())) {
                throw new RuntimeException("Bad format");
            }
            readMark(reader, currentGroup, currentStudent, tableOfMarks);
            line = LineReader.readLine(reader);
        }
    }

    private static void readMark(final FileReader reader, final Group currentGroup,
                             final Student currentStudent, final TableOfMarks tableOfMarks)
            throws Exception {
        String line = LineReader.readLine(reader);
        GregorianCalendar gregorianCalendar = DateParser.parseDate(LineReader.readLine(reader));
        int markValue = Integer.parseInt(LineReader.readLine(reader));
        tableOfMarks.addNewMark(new Mark(markValue, line, gregorianCalendar),
                currentStudent, currentGroup);
    }
}
