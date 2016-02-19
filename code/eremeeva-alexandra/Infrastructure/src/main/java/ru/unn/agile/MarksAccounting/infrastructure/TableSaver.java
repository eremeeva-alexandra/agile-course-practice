package ru.unn.agile.MarksAccounting.infrastructure;

import ru.unn.agile.MarksAccounting.model.DateParser;
import ru.unn.agile.MarksAccounting.model.Group;
import ru.unn.agile.MarksAccounting.model.Student;
import ru.unn.agile.MarksAccounting.model.TableOfMarks;
import ru.unn.agile.MarksAccounting.viewmodel.TableTags;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public final class TableSaver {
    private TableSaver() { }

    public static void save(final String fileName,
                            final TableOfMarks tableOfMarks) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        writeFile(fileWriter, tableOfMarks);
        fileWriter.close();
    }

    private static void writeFile(final FileWriter fileWriter,
                                  final TableOfMarks tableOfMarks) throws IOException {
        ArrayList<Group> groups = tableOfMarks.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            fileWriter.write(TableTags.GROUP.getTag() + "\n");
            fileWriter.write(groups.get(i).getNumber());
            fileWriter.write("\n");
            writeSubjects(fileWriter, groups.get(i));
            writeStudents(fileWriter, groups.get(i));
            fileWriter.write(TableTags.END_OF_GROUP.getTag() + "\n");
        }
    }

    private static void writeSubjects(final FileWriter fileWriter,
                                      final Group group) throws IOException {
        fileWriter.write(TableTags.SUBJECTS.getTag() + "\n");
        for (int i = 0; i < group.getAcademicSubjects().size(); i++) {
            fileWriter.write(group.getAcademicSubjects().get(i));
            fileWriter.write("\n");
        }
        fileWriter.write(TableTags.END_OF_SUBJECTS.getTag() + "\n");
    }

    private static void writeStudents(final FileWriter fileWriter,
                                      final Group group) throws IOException {
        for (int i = 0; i < group.getStudents().size(); i++) {
            fileWriter.write(TableTags.STUDENT.getTag() + "\n");
            fileWriter.write(group.getStudents().get(i).getName() + "\n");
            writeMarks(fileWriter, group.getStudents().get(i));
            fileWriter.write(TableTags.END_OF_STUDENT.getTag() + "\n");
        }
    }

    private static void writeMarks(final FileWriter fileWriter,
                                   final Student student) throws IOException {
        for (int i = 0; i < student.getMarks().size(); i++) {
            fileWriter.write(TableTags.MARK.getTag() + "\n");
            fileWriter.write(student.getMarks().get(i).getAcademicSubject() + "\n");
            fileWriter.write(DateParser.formatDate(student.getMarks().get(i).getDate()) + "\n");
            fileWriter.write(Integer.toString(student.getMarks().get(i).getValue()) + "\n");
        }
    }
}
