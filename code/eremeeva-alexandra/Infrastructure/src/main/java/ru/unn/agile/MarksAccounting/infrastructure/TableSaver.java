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
        FileWriter writer = new FileWriter(fileName);
        writeFile(writer, tableOfMarks);
        writer.close();
    }

    private static void writeFile(final FileWriter writer,
                                  final TableOfMarks tableOfMarks) throws IOException {
        ArrayList<Group> groups = tableOfMarks.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            writer.write(TableTags.GROUP.getTag() + "\n");
            writer.write(groups.get(i).getNumber());
            writer.write("\n");
            writeSubjects(writer, groups.get(i));
            writeStudents(writer, groups.get(i));
            writer.write(TableTags.END_OF_GROUP.getTag() + "\n");
        }
    }

    private static void writeSubjects(final FileWriter writer,
                                      final Group group) throws IOException {
        writer.write(TableTags.SUBJECTS.getTag() + "\n");
        for (int i = 0; i < group.getAcademicSubjects().size(); i++) {
            writer.write(group.getAcademicSubjects().get(i));
            writer.write("\n");
        }
        writer.write(TableTags.END_OF_SUBJECTS.getTag() + "\n");
    }

    private static void writeStudents(final FileWriter writer,
                                      final Group group) throws IOException {
        for (int i = 0; i < group.getStudents().size(); i++) {
            writer.write(TableTags.STUDENT.getTag() + "\n");
            writer.write(group.getStudents().get(i).getName() + "\n");
            writeMarks(writer, group.getStudents().get(i));
            writer.write(TableTags.END_OF_STUDENT.getTag() + "\n");
        }
    }

    private static void writeMarks(final FileWriter writer,
                                   final Student student) throws IOException {
        for (int i = 0; i < student.getMarks().size(); i++) {
            writer.write(TableTags.MARK.getTag() + "\n");
            writer.write(student.getMarks().get(i).getAcademicSubject() + "\n");
            writer.write(DateParser.formatDate(student.getMarks().get(i).getDate()) + "\n");
            writer.write(Integer.toString(student.getMarks().get(i).getValue()) + "\n");
        }
    }
}
