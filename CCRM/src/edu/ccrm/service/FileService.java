package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.nio.file.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class FileService {
    private static final String DATA_DIR = "data/";
    private static final String BACKUP_DIR = "backups/";
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public void exportStudents(List<Student> students) throws IOException {
        Path filePath = Paths.get(DATA_DIR + "students.csv");
        Files.createDirectories(filePath.getParent());

        List<String> lines = new ArrayList<>();
        lines.add("ID,RegNo,FullName,Email,Active");

        for (Student student : students) {
            String line = String.format("%s,%s,%s,%s,%s",
                    student.getId(), student.getRegNo(), student.getFullName(),
                    student.getEmail(), student.isActive());
            lines.add(line);
        }

        Files.write(filePath, lines);
        System.out.println("Exported " + students.size() + " students to " + filePath);
    }

    public void exportCourses(List<Course> courses) throws IOException {
        Path filePath = Paths.get(DATA_DIR + "courses.csv");
        Files.createDirectories(filePath.getParent());

        List<String> lines = new ArrayList<>();
        lines.add("Code,Title,Credits,Department");

        for (Course course : courses) {
            String line = String.format("%s,%s,%d,%s",
                    course.getCode().getCode(), course.getTitle(),
                    course.getCredits(), course.getDepartment());
            lines.add(line);
        }

        Files.write(filePath, lines);
        System.out.println("Exported " + courses.size() + " courses to " + filePath);
    }

    public void importStudents(StudentService studentService) throws IOException {
        Path filePath = Paths.get(DATA_DIR + "students.csv");
        if (!Files.exists(filePath)) {
            System.out.println("No students CSV file found. Export students first.");
            return;
        }

        List<String> lines = Files.readAllLines(filePath);
        int imported = 0;

        for (int i = 1; i < lines.size(); i++) {
            String[] fields = lines.get(i).split(",");
            if (fields.length >= 4) {
                Student student = new Student(fields[0], fields[1], fields[2], fields[3]);
                studentService.addStudent(student);
                imported++;
            }
        }
        System.out.println("Imported " + imported + " students from " + filePath);
    }

    public void importCourses(CourseService courseService) throws IOException {
        Path filePath = Paths.get(DATA_DIR + "courses.csv");
        if (!Files.exists(filePath)) {
            System.out.println("No courses CSV file found. Export courses first.");
            return;
        }

        List<String> lines = Files.readAllLines(filePath);
        int imported = 0;

        for (int i = 1; i < lines.size(); i++) {
            String[] fields = lines.get(i).split(",");
            if (fields.length >= 3) {
                Course course = new Course.Builder()
                        .code(fields[0])
                        .title(fields[1])
                        .credits(Integer.parseInt(fields[2]))
                        .build();
                courseService.addCourse(course);
                imported++;
            }
        }
        System.out.println("Imported " + imported + " courses from " + filePath);
    }

    public void createBackup() throws IOException {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        Path backupPath = Paths.get(BACKUP_DIR + "backup_" + timestamp);
        Path dataPath = Paths.get(DATA_DIR);

        if (!Files.exists(dataPath)) {
            Files.createDirectories(dataPath);
        }

        Files.createDirectories(backupPath);

        if (Files.exists(Paths.get(DATA_DIR + "students.csv"))) {
            Path source = Paths.get(DATA_DIR + "students.csv");
            Path target = backupPath.resolve("students.csv");
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }

        if (Files.exists(Paths.get(DATA_DIR + "courses.csv"))) {
            Path source = Paths.get(DATA_DIR + "courses.csv");
            Path target = backupPath.resolve("courses.csv");
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("Backup created: " + backupPath);
    }

    public void listBackups() throws IOException {
        Path backupDir = Paths.get(BACKUP_DIR);
        if (!Files.exists(backupDir)) {
            System.out.println("No backups found.");
            return;
        }

        List<Path> backups = new ArrayList<>();
        try (var paths = Files.list(backupDir)) {
            paths.filter(Files::isDirectory)
                    .forEach(backups::add);
        }

        System.out.println("Available backups:");
        for (Path backup : backups) {
            System.out.println("• " + backup.getFileName());
        }
    }
}