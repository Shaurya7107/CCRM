# Campus Course & Records Manager (CCRM)

A Java console application for managing students, courses, enrollments, and grades.

## Features
- Student Management (add, list students)
- Course Management (add, list courses) 
- Enrollment System (enroll students, assign grades)
- Transcript Generation with GPA calculation
- File Operations (CSV import/export, backups)
- Reports (GPA distribution, top students)

## Java Version
- JDK 17 or higher

## How to Run
1. Clone the repository
2. Open in Eclipse/IntelliJ
3. Run `Main.java` from `src/edu/ccrm/Main.java`

## Project Structure
src/
├── edu/ccrm/
│ ├── config/AppConfig.java (Singleton)
│ ├── domain/ (Student, Course, Enrollment, Grade enum, Semester enum)
│ ├── service/ (StudentService, CourseService, EnrollmentService, FileService)
│ ├── cli/Menu.java (Main menu system)
│ └── exception/ (Custom exceptions)

## Java Concepts Demonstrated
- OOP (Encapsulation, Inheritance, Polymorphism, Abstraction)
- Design Patterns (Singleton, Builder)
- Exception Handling (Custom exceptions)
- File I/O with NIO.2
- Streams API
- Enums
- Lambda expressions

