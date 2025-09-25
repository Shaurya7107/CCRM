package edu.ccrm.cli;

import edu.ccrm.domain.*;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.FileService;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.exception.MaxCreditLimitExceededException;

import java.util.Scanner;
import java.util.List;

public class Menu {
    private Scanner scanner;
    private StudentService studentService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;
    private FileService fileService;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.enrollmentService = new EnrollmentService();
        this.fileService = new FileService();
    }

    public void displayMainMenu() {
        while (true) {
            System.out.println("\n=== Campus Course & Records Manager ===");
            System.out.println("1. Manage Students");
            System.out.println("2. Manage Courses");
            System.out.println("3. Manage Enrollments");
            System.out.println("4. File Operations");
            System.out.println("5. Reports");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    manageStudents();
                    break;
                case 2:
                    manageCourses();
                    break;
                case 3:
                    manageEnrollments();
                    break;
                case 4:
                    manageFileOperations();
                    break;
                case 5:
                    generateReports();
                    break;
                case 6:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void manageStudents() {
        while (true) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add Student");
            System.out.println("2. List Students");
            System.out.println("3. Back");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    listStudents();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void addStudent() {
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter registration number: ");
        String regNo = scanner.nextLine();
        System.out.print("Enter full name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        Student student = new Student(id, regNo, name, email);
        studentService.addStudent(student);
        System.out.println("Student added successfully!");
    }

    private void listStudents() {
        System.out.println("\n--- All Students ---");
        studentService.getAllStudents().forEach(Student::displayProfile);
    }

    private void manageCourses() {
        while (true) {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Add Course");
            System.out.println("2. List Courses");
            System.out.println("3. Back");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addCourse();
                    break;
                case 2:
                    listCourses();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void addCourse() {
        System.out.print("Enter course code: ");
        String code = scanner.nextLine();
        System.out.print("Enter course title: ");
        String title = scanner.nextLine();
        System.out.print("Enter credits: ");
        int credits = scanner.nextInt();
        scanner.nextLine();

        Course course = new Course.Builder()
                .code(code)
                .title(title)
                .credits(credits)
                .build();

        courseService.addCourse(course);
        System.out.println("Course added successfully!");
    }

    private void listCourses() {
        System.out.println("\n--- All Courses ---");
        courseService.getAllCourses().forEach(course -> {
            System.out.println("Code: " + course.getCode() + ", Title: " + course.getTitle());
        });
    }

    private void manageEnrollments() {
        while (true) {
            System.out.println("\n--- Enrollment Management ---");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Assign Grade");
            System.out.println("3. Generate Transcript");
            System.out.println("4. Back");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    enrollStudent();
                    break;
                case 2:
                    assignGrade();
                    break;
                case 3:
                    generateTranscript();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void enrollStudent() {
        System.out.println("\nAvailable Students:");
        studentService.getAllStudents().forEach(s -> {
            System.out.println("ID: " + s.getId() + " | Name: " + s.getFullName());
        });

        System.out.println("\nAvailable Courses:");
        courseService.getAllCourses().forEach(c -> {
            System.out.println("Code: " + c.getCode().getCode() + " | Title: " + c.getTitle());
        });

        System.out.print("\nEnter student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().toUpperCase();

        Student student = studentService.findStudentById(studentId);
        Course course = findCourseByCode(courseCode);

        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        if (course == null) {
            System.out.println("Course not found!");
            return;
        }

        try {
            enrollmentService.enrollStudent(student, course);
            System.out.println("Enrollment successful!");
        } catch (DuplicateEnrollmentException e) {
            System.out.println("Enrollment failed: " + e.getMessage());
        } catch (MaxCreditLimitExceededException e) {
            System.out.println("Enrollment failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void assignGrade() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().toUpperCase();
        System.out.print("Enter grade (S,A,B,C,D,E,F): ");
        String gradeStr = scanner.nextLine().toUpperCase();

        Student student = studentService.findStudentById(studentId);
        Course course = findCourseByCode(courseCode);
        Grade grade;

        try {
            grade = Grade.valueOf(gradeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid grade!");
            return;
        }

        if (student == null || course == null) {
            System.out.println("Student or course not found!");
            return;
        }

        try {
            enrollmentService.assignGrade(student, course, grade);
            System.out.println("Grade assigned successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void generateTranscript() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        Student student = studentService.findStudentById(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        enrollmentService.generateTranscript(student);
    }

    private void manageFileOperations() {
        while (true) {
            System.out.println("\n--- File Operations ---");
            System.out.println("1. Export Students to CSV");
            System.out.println("2. Export Courses to CSV");
            System.out.println("3. Import Students from CSV");
            System.out.println("4. Import Courses from CSV");
            System.out.println("5. Create Backup");
            System.out.println("6. List Backups");
            System.out.println("7. Back");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        fileService.exportStudents(studentService.getAllStudents());
                        break;
                    case 2:
                        fileService.exportCourses(courseService.getAllCourses());
                        break;
                    case 3:
                        fileService.importStudents(studentService);
                        break;
                    case 4:
                        fileService.importCourses(courseService);
                        break;
                    case 5:
                        fileService.createBackup();
                        break;
                    case 6:
                        fileService.listBackups();
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void generateReports() {
        while (true) {
            System.out.println("\n--- Reports ---");
            System.out.println("1. GPA Distribution");
            System.out.println("2. Top Students");
            System.out.println("3. Course Statistics");
            System.out.println("4. Back");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showGPADistribution();
                    break;
                case 2:
                    showTopStudents();
                    break;
                case 3:
                    showCourseStatistics();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void showGPADistribution() {
        System.out.println("\n--- GPA Distribution ---");
        studentService.getAllStudents().stream()
                .filter(Student::isActive)
                .forEach(s -> {
                    double gpa = enrollmentService.calculateGPA(s);
                    System.out.println(s.getFullName() + ": " + String.format("%.2f", gpa));
                });
    }

    private void showTopStudents() {
        System.out.println("\n--- Top Students ---");
        studentService.getAllStudents().stream()
                .filter(Student::isActive)
                .sorted((s1, s2) -> Double.compare(
                        enrollmentService.calculateGPA(s2),
                        enrollmentService.calculateGPA(s1)
                ))
                .limit(3)
                .forEach(s -> {
                    double gpa = enrollmentService.calculateGPA(s);
                    System.out.println(s.getFullName() + " - GPA: " + String.format("%.2f", gpa));
                });
    }

    private void showCourseStatistics() {
        System.out.println("\n--- Course Statistics ---");
        courseService.getAllCourses().forEach(course -> {
            long enrollmentCount = enrollmentService.getAllEnrollments().stream()
                    .filter(e -> e.getCourse().equals(course))
                    .count();
            System.out.println(course.getTitle() + ": " + enrollmentCount + " enrollments");
        });
    }

    private Course findCourseByCode(String code) {
        return courseService.getAllCourses().stream()
                .filter(c -> c.getCode().getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}