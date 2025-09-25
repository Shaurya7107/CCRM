package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String regNo;
    private boolean active;
    private List<Enrollment> enrollments;

    public Student(String id, String regNo, String fullName, String email) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.active = true;
        this.enrollments = new ArrayList<>();
    }

    @Override
    public void displayProfile() {
        System.out.println("Student ID: " + id);
        System.out.println("Registration No: " + regNo);
        System.out.println("Name: " + fullName);
        System.out.println("Email: " + email);
        System.out.println("Status: " + (active ? "Active" : "Inactive"));
    }

    public void enrollInCourse(Course course) {
        enrollments.add(new Enrollment(this, course));
    }

    public String getRegNo() { return regNo; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public List<Enrollment> getEnrollments() { return enrollments; }
}