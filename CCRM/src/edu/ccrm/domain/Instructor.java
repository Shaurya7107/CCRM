package edu.ccrm.domain;

public class Instructor extends Person {
    private String department;

    public Instructor(String id, String fullName, String email, String department) {
        super(id, fullName, email);
        this.department = department;
    }

    @Override
    public void displayProfile() {
        System.out.println("Instructor ID: " + id);
        System.out.println("Name: " + fullName);
        System.out.println("Email: " + email);
        System.out.println("Department: " + department);
    }

    public String getDepartment() { return department; }
}