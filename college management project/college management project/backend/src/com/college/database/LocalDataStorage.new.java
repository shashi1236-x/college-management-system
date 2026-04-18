package com.college.database;

import java.io.*;
import java.util.*;

/**
 * LocalDataStorage - Manages in-memory data storage for the college management system.
 * This provides a simple file-based storage mechanism that can be replaced with a database later.
 */
public class LocalDataStorage {
    private static LocalDataStorage instance;
    private List<Map<String, Object>> students;
    private List<Map<String, Object>> courses;
    private List<Map<String, Object>> instructors;
    private List<Map<String, Object>> enrollments;
    private List<Map<String, Object>> grades;
    private String dataDirectory;

    private LocalDataStorage() {
        this.dataDirectory = "data";
        this.students = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.instructors = new ArrayList<>();
        this.enrollments = new ArrayList<>();
        this.grades = new ArrayList<>();

        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        loadSampleData();
    }

    public static synchronized LocalDataStorage getInstance() {
        if (instance == null) {
            instance = new LocalDataStorage();
        }
        return instance;
    }

    public void addStudent(Map<String, Object> student) {
        students.add(student);
    }

    public List<Map<String, Object>> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Map<String, Object> getStudentById(String id) {
        return students.stream()
            .filter(s -> id.equals(s.get("id")))
            .findFirst()
            .orElse(null);
    }

    public void updateStudent(String id, Map<String, Object> updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (id.equals(students.get(i).get("id"))) {
                students.set(i, updatedStudent);
                return;
            }
        }
    }

    public void deleteStudent(String id) {
        students.removeIf(s -> id.equals(s.get("id")));
    }

    public void addCourse(Map<String, Object> course) {
        courses.add(course);
    }

    public List<Map<String, Object>> getAllCourses() {
        return new ArrayList<>(courses);
    }

    public Map<String, Object> getCourseById(String id) {
        return courses.stream()
            .filter(c -> id.equals(c.get("id")))
            .findFirst()
            .orElse(null);
    }

    public void updateCourse(String id, Map<String, Object> updatedCourse) {
        for (int i = 0; i < courses.size(); i++) {
            if (id.equals(courses.get(i).get("id"))) {
                courses.set(i, updatedCourse);
                return;
            }
        }
    }

    public void deleteCourse(String id) {
        courses.removeIf(c -> id.equals(c.get("id")));
    }

    public void addInstructor(Map<String, Object> instructor) {
        instructors.add(instructor);
    }

    public List<Map<String, Object>> getAllInstructors() {
        return new ArrayList<>(instructors);
    }

    public Map<String, Object> getInstructorById(String id) {
        return instructors.stream()
            .filter(i -> id.equals(i.get("id")))
            .findFirst()
            .orElse(null);
    }

    public void updateInstructor(String id, Map<String, Object> updatedInstructor) {
        for (int i = 0; i < instructors.size(); i++) {
            if (id.equals(instructors.get(i).get("id"))) {
                instructors.set(i, updatedInstructor);
                return;
            }
        }
    }

    public void deleteInstructor(String id) {
        instructors.removeIf(i -> id.equals(i.get("id")));
    }

    public void addEnrollment(Map<String, Object> enrollment) {
        enrollments.add(enrollment);
    }

    public List<Map<String, Object>> getAllEnrollments() {
        return new ArrayList<>(enrollments);
    }

    public Map<String, Object> getEnrollmentById(String id) {
        return enrollments.stream()
            .filter(e -> id.equals(e.get("id")))
            .findFirst()
            .orElse(null);
    }

    public void updateEnrollment(String id, Map<String, Object> updatedEnrollment) {
        for (int i = 0; i < enrollments.size(); i++) {
            if (id.equals(enrollments.get(i).get("id"))) {
                enrollments.set(i, updatedEnrollment);
                return;
            }
        }
    }

    public void deleteEnrollment(String id) {
        enrollments.removeIf(e -> id.equals(e.get("id")));
    }

    public void addGrade(Map<String, Object> grade) {
        grades.add(grade);
    }

    public List<Map<String, Object>> getAllGrades() {
        return new ArrayList<>(grades);
    }

    public Map<String, Object> getGradeById(String id) {
        return grades.stream()
            .filter(g -> id.equals(g.get("id")))
            .findFirst()
            .orElse(null);
    }

    public void updateGrade(String id, Map<String, Object> updatedGrade) {
        for (int i = 0; i < grades.size(); i++) {
            if (id.equals(grades.get(i).get("id"))) {
                grades.set(i, updatedGrade);
                return;
            }
        }
    }

    public void deleteGrade(String id) {
        grades.removeIf(g -> id.equals(g.get("id")));
    }

    private void loadSampleData() {
        addInstructor("INS001", "Dr. Maria Rodriguez", "maria.rodriguez@university.edu", "555-0101", "Engineering", "PhD", 18, "Mechanical Engineering", "2008-08-20");
        addInstructor("INS002", "Prof. David Kim", "david.kim@university.edu", "555-0102", "Business", "MBA", 14, "Finance", "2010-01-15");
        addInstructor("INS003", "Dr. Lisa Thompson", "lisa.thompson@university.edu", "555-0103", "Arts", "PhD", 12, "Literature", "2012-09-01");
        addInstructor("INS004", "Prof. Ahmed Hassan", "ahmed.hassan@university.edu", "555-0104", "Science", "PhD", 16, "Environmental Science", "2009-03-10");
        addInstructor("INS005", "Dr. Jennifer Liu", "jennifer.liu@university.edu", "555-0105", "Health Sciences", "MD", 20, "Public Health", "2006-06-15");

        addStudent("STU001", "Emma Davis", "emma.davis@student.edu", "555-1001", "2002-03-22", "Engineering", "100 Tech Blvd, Innovation City, IC 10001", "2024-08-25");
        addStudent("STU002", "Carlos Martinez", "carlos.martinez@student.edu", "555-1002", "2001-07-14", "Business", "200 Commerce St, Business Town, BT 20002", "2024-08-25");
        addStudent("STU003", "Sophia Patel", "sophia.patel@student.edu", "555-1003", "2003-01-08", "Arts", "300 Culture Ave, Arts District, AD 30003", "2024-08-25");
        addStudent("STU004", "Noah Johnson", "noah.johnson@student.edu", "555-1004", "2002-11-30", "Science", "400 Research Dr, Science Park, SP 40004", "2024-08-25");
        addStudent("STU005", "Ava Chen", "ava.chen@student.edu", "555-1005", "2001-09-17", "Health Sciences", "500 Health Way, Medical Center, MC 50005", "2024-08-25");
        addStudent("STU006", "Liam Wilson", "liam.wilson@student.edu", "555-1006", "2002-05-12", "Engineering", "600 Engineering Ln, Tech Hub, TH 60006", "2024-08-25");

        addCourse("CRS001", "Thermodynamics", "ENGR201", "Principles of heat transfer and energy systems", 4, "Engineering", "INS001", 35, "2024-08-25");
        addCourse("CRS002", "Corporate Finance", "BUS301", "Financial management and investment strategies", 3, "Business", "INS002", 40, "2024-08-25");
        addCourse("CRS003", "Modern Poetry", "ARTS101", "Analysis of contemporary poetic forms", 3, "Arts", "INS003", 25, "2024-08-25");
        addCourse("CRS004", "Climate Change Science", "SCI202", "Environmental impacts and mitigation strategies", 4, "Science", "INS004", 30, "2024-08-25");
        addCourse("CRS005", "Epidemiology", "HLTH301", "Disease patterns and public health interventions", 4, "Health Sciences", "INS005", 28, "2024-08-25");
        addCourse("CRS006", "Robotics Engineering", "ENGR401", "Design and control of robotic systems", 4, "Engineering", "INS001", 20, "2024-08-25");

        addEnrollment("ENR001", "STU001", "CRS001", "2024-08-25", "Active");
        addEnrollment("ENR002", "STU001", "CRS006", "2024-08-25", "Active");
        addEnrollment("ENR003", "STU002", "CRS002", "2024-08-25", "Active");
        addEnrollment("ENR004", "STU003", "CRS003", "2024-08-25", "Active");
        addEnrollment("ENR005", "STU004", "CRS004", "2024-08-25", "Active");
        addEnrollment("ENR006", "STU005", "CRS005", "2024-08-25", "Active");
        addEnrollment("ENR007", "STU006", "CRS001", "2024-08-25", "Active");
        addEnrollment("ENR008", "STU006", "CRS006", "2024-08-25", "Active");

        addGrade("GRD001", "ENR001", 88.0, 92.0, 85.0, 95.0, 90.0, "2024-12-15");
        addGrade("GRD002", "ENR002", 91.0, 89.0, 93.0, 88.0, 90.3, "2024-12-15");
        addGrade("GRD003", "ENR003", 85.0, 87.0, 82.0, 90.0, 86.0, "2024-12-15");
        addGrade("GRD004", "ENR004", 92.0, 94.0, 89.0, 96.0, 92.8, "2024-12-15");
        addGrade("GRD005", "ENR005", 89.0, 91.0, 87.0, 93.0, 90.0, "2024-12-15");
        addGrade("GRD006", "ENR006", 94.0, 96.0, 91.0, 98.0, 94.8, "2024-12-15");
    }

    private void addInstructor(String id, String name, String email, String phone, String department,
            String qualification, int experience, String specialization, String hireDate) {
        Map<String, Object> instructor = new HashMap<>();
        instructor.put("id", id);
        instructor.put("name", name);
        instructor.put("email", email);
        instructor.put("phone", phone);
        instructor.put("department", department);
        instructor.put("qualification", qualification);
        instructor.put("experience", experience);
        instructor.put("specialization", specialization);
        instructor.put("hireDate", hireDate);
        instructors.add(instructor);
    }

    private void addStudent(String id, String name, String email, String phone, String dateOfBirth,
            String department, String address, String enrollmentDate) {
        Map<String, Object> student = new HashMap<>();
        student.put("id", id);
        student.put("name", name);
        student.put("email", email);
        student.put("phone", phone);
        student.put("dateOfBirth", dateOfBirth);
        student.put("department", department);
        student.put("address", address);
        student.put("enrollmentDate", enrollmentDate);
        students.add(student);
    }

    private void addCourse(String id, String name, String code, String description, int credits,
            String department, String instructorId, int capacity, String createdDate) {
        Map<String, Object> course = new HashMap<>();
        course.put("id", id);
        course.put("name", name);
        course.put("code", code);
        course.put("description", description);
        course.put("credits", credits);
        course.put("department", department);
        course.put("instructorId", instructorId);
        course.put("capacity", capacity);
        course.put("createdDate", createdDate);
        courses.add(course);
    }

    private void addEnrollment(String id, String studentId, String courseId, String enrollmentDate, String status) {
        Map<String, Object> enrollment = new HashMap<>();
        enrollment.put("id", id);
        enrollment.put("studentId", studentId);
        enrollment.put("courseId", courseId);
        enrollment.put("enrollmentDate", enrollmentDate);
        enrollment.put("status", status);
        enrollments.add(enrollment);
    }

    private void addGrade(String id, String enrollmentId, double midterm, double finalScore,
            double assignment, double attendance, double overallGrade, String createdDate) {
        Map<String, Object> grade = new HashMap<>();
        grade.put("id", id);
        grade.put("enrollmentId", enrollmentId);
        grade.put("midterm", midterm);
        grade.put("final", finalScore);
        grade.put("assignment", assignment);
        grade.put("attendance", attendance);
        grade.put("overallGrade", overallGrade);
        grade.put("createdDate", createdDate);
        grades.add(grade);
    }

    public Map<String, Object> exportAllData() {
        Map<String, Object> allData = new LinkedHashMap<>();
        allData.put("students", students);
        allData.put("courses", courses);
        allData.put("instructors", instructors);
        allData.put("enrollments", enrollments);
        allData.put("grades", grades);
        return allData;
    }

    public void clearAllData() {
        students.clear();
        courses.clear();
        instructors.clear();
        enrollments.clear();
        grades.clear();
    }

    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("totalStudents", students.size());
        stats.put("totalCourses", courses.size());
        stats.put("totalInstructors", instructors.size());
        stats.put("totalEnrollments", enrollments.size());
        stats.put("totalGrades", grades.size());
        return stats;
    }
}