package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.List;
import java.util.stream.Collectors;

public class SearchService implements Searchable<Course> {
    private CourseService courseService;
    private StudentService studentService;

    public SearchService(CourseService courseService, StudentService studentService) {
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @Override
    public List<Course> search(String keyword) {
        return courseService.getAllCourses().stream()
                .filter(course ->
                        course.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                                course.getCode().getCode().toLowerCase().contains(keyword.toLowerCase()) ||
                                (course.getDepartment() != null &&
                                        course.getDepartment().toLowerCase().contains(keyword.toLowerCase())) ||
                                (course.getInstructor() != null &&
                                        course.getInstructor().getFullName().toLowerCase().contains(keyword.toLowerCase()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> filterByDepartment(String department) {
        return courseService.getAllCourses().stream()
                .filter(course -> course.getDepartment() != null &&
                        course.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    public List<Course> filterBySemester(Semester semester) {
        return courseService.getAllCourses().stream()
                .filter(course -> course.getSemester() == semester)
                .collect(Collectors.toList());
    }

    public List<Course> filterByCredits(int minCredits, int maxCredits) {
        return courseService.getAllCourses().stream()
                .filter(course -> course.getCredits() >= minCredits &&
                        course.getCredits() <= maxCredits)
                .collect(Collectors.toList());
    }

    public List<Student> searchStudents(String keyword) {
        return studentService.getAllStudents().stream()
                .filter(student ->
                        student.getFullName().toLowerCase().contains(keyword.toLowerCase()) ||
                                student.getRegNo().toLowerCase().contains(keyword.toLowerCase()) ||
                                student.getEmail().toLowerCase().contains(keyword.toLowerCase())
                )
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsByGpaRange(double minGPA, double maxGPA, EnrollmentService enrollmentService) {
        return studentService.getAllStudents().stream()
                .filter(student -> {
                    double gpa = enrollmentService.calculateGPA(student);
                    return gpa >= minGPA && gpa <= maxGPA;
                })
                .sorted((s1, s2) -> Double.compare(
                        enrollmentService.calculateGPA(s2),
                        enrollmentService.calculateGPA(s1)
                ))
                .collect(Collectors.toList());
    }
}