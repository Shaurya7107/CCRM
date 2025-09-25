package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.exception.MaxCreditLimitExceededException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentService {
    private List<Enrollment> enrollments;
    private static final int MAX_CREDITS_PER_SEMESTER = 18;

    public EnrollmentService() {
        this.enrollments = new ArrayList<>();
    }

    public void enrollStudent(Student student, Course course)
            throws DuplicateEnrollmentException, MaxCreditLimitExceededException {
        if (findEnrollment(student, course) != null) {
            throw new DuplicateEnrollmentException("Student " + student.getFullName() + " already enrolled in " + course.getTitle());
        }

        int currentCredits = getCurrentSemesterCredits(student);
        if (currentCredits + course.getCredits() > MAX_CREDITS_PER_SEMESTER) {
            throw new MaxCreditLimitExceededException(
                    "Credit limit exceeded for " + student.getFullName() +
                            ". Current: " + currentCredits + ", Course: " + course.getCredits() + ", Max: " + MAX_CREDITS_PER_SEMESTER
            );
        }

        Enrollment enrollment = new Enrollment(student, course);
        enrollments.add(enrollment);
        System.out.println("Enrolled " + student.getFullName() + " in " + course.getTitle());
    }

    public void assignGrade(Student student, Course course, Grade grade) {
        Enrollment enrollment = findEnrollment(student, course);
        if (enrollment != null) {
            enrollment.assignGrade(grade);
            System.out.println("Assigned grade " + grade + " to " + student.getFullName() + " for " + course.getTitle());
        } else {
            throw new RuntimeException("Student not enrolled in this course");
        }
    }

    public double calculateGPA(Student student) {
        List<Enrollment> studentEnrollments = getEnrollmentsByStudent(student);
        if (studentEnrollments.isEmpty()) return 0.0;

        double totalPoints = 0;
        int totalCredits = 0;

        for (Enrollment enrollment : studentEnrollments) {
            if (enrollment.getGrade() != null) {
                totalPoints += enrollment.getGrade().getPoints() * enrollment.getCourse().getCredits();
                totalCredits += enrollment.getCourse().getCredits();
            }
        }

        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    public void generateTranscript(Student student) {
        List<Enrollment> studentEnrollments = getEnrollmentsByStudent(student);

        System.out.println("\n=== Transcript for " + student.getFullName() + " ===");
        System.out.println("Reg No: " + student.getRegNo());
        System.out.println("GPA: " + String.format("%.2f", calculateGPA(student)));
        System.out.println("Total Enrollments: " + studentEnrollments.size());
        System.out.println("\nCourses:");

        if (studentEnrollments.isEmpty()) {
            System.out.println("No courses enrolled.");
            return;
        }

        for (Enrollment enrollment : studentEnrollments) {
            Course course = enrollment.getCourse();
            String grade = enrollment.getGrade() != null ? enrollment.getGrade().name() : "Not Graded";
            System.out.println("• " + course.getCode() + " - " + course.getTitle() +
                    " | Credits: " + course.getCredits() + " | Grade: " + grade);
        }
    }

    private int getCurrentSemesterCredits(Student student) {
        return getEnrollmentsByStudent(student).stream()
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();
    }

    private List<Enrollment> getEnrollmentsByStudent(Student student) {
        return enrollments.stream()
                .filter(e -> e.getStudent().equals(student))
                .toList();
    }

    private Enrollment findEnrollment(Student student, Course course) {
        return enrollments.stream()
                .filter(e -> e.getStudent().equals(student) && e.getCourse().equals(course))
                .findFirst()
                .orElse(null);
    }

    public List<Enrollment> getAllEnrollments() {
        return new ArrayList<>(enrollments);
    }
}