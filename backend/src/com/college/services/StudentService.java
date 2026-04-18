package com.college.services;

import com.college.models.Student;
import com.college.database.LocalDataStorage;
import com.college.database.MySQLConnection;
import com.college.utils.Constants;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;

/**
 * StudentService - Business logic for student operations
 * Supports both local storage and MySQL database
 */
public class StudentService {
    private LocalDataStorage storage;
    private ObjectMapper objectMapper;
    private boolean useMySQL;

    public StudentService() {
        this.storage = LocalDataStorage.getInstance();
        this.objectMapper = new ObjectMapper();
        this.useMySQL = Constants.USE_MYSQL;
    }

    /**
     * Add a new student
     */
    public boolean addStudent(Student student) {
        if (useMySQL) {
            return addStudentMySQL(student);
        } else {
            try {
                Map<String, Object> studentMap = objectMapper.convertValue(student, Map.class);
                storage.addStudent(studentMap);
                return true;
            } catch (Exception e) {
                System.out.println("Error adding student: " + e.getMessage());
                return false;
            }
        }
    }

    /**
     * Add student to MySQL database
     */
    private boolean addStudentMySQL(Student student) {
        String query = "INSERT INTO students (id, name, email, phone, dateOfBirth, department, address, enrollmentDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, student.getId());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPhone());
            stmt.setDate(5, student.getDateOfBirth() != null ? java.sql.Date.valueOf(student.getDateOfBirth().toString()) : null);
            stmt.setString(6, student.getDepartment());
            stmt.setString(7, student.getAddress());
            stmt.setDate(8, student.getEnrollmentDate() != null ? java.sql.Date.valueOf(student.getEnrollmentDate().toString()) : null);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding student to MySQL: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        if (useMySQL) {
            return getAllStudentsMySQL();
        } else {
            List<Student> students = new ArrayList<>();
            for (Map<String, Object> studentMap : storage.getAllStudents()) {
                try {
                    Student student = objectMapper.convertValue(studentMap, Student.class);
                    students.add(student);
                } catch (Exception e) {
                    System.out.println("Error converting student: " + e.getMessage());
                }
            }
            return students;
        }
    }

    /**
     * Get all students from MySQL database
     */
    private List<Student> getAllStudentsMySQL() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students ORDER BY name";
        try (Statement stmt = MySQLConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getString("id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));
                student.setDateOfBirth(rs.getString("dateOfBirth"));
                student.setDepartment(rs.getString("department"));
                student.setAddress(rs.getString("address"));
                student.setEnrollmentDate(rs.getString("enrollmentDate"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println("Error getting students from MySQL: " + e.getMessage());
        }
        return students;
    }

    /**
     * Get student by ID
     */
    public Student getStudentById(String id) {
        if (useMySQL) {
            return getStudentByIdMySQL(id);
        } else {
            try {
                Map<String, Object> studentMap = storage.getStudentById(id);
                if (studentMap != null) {
                    return objectMapper.convertValue(studentMap, Student.class);
                }
            } catch (Exception e) {
                System.out.println("Error getting student: " + e.getMessage());
            }
            return null;
        }
    }

    /**
     * Get student by ID from MySQL database
     */
    private Student getStudentByIdMySQL(String id) {
        String query = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getString("id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));
                student.setDateOfBirth(rs.getString("dateOfBirth"));
                student.setDepartment(rs.getString("department"));
                student.setAddress(rs.getString("address"));
                student.setEnrollmentDate(rs.getString("enrollmentDate"));
                return student;
            }
        } catch (SQLException e) {
            System.out.println("Error getting student from MySQL: " + e.getMessage());
        }
        return null;
    }

    /**
     * Update student
     */
    public boolean updateStudent(String id, Student student) {
        if (useMySQL) {
            return updateStudentMySQL(id, student);
        } else {
            try {
                student.setId(id);
                Map<String, Object> studentMap = objectMapper.convertValue(student, Map.class);
                storage.updateStudent(id, studentMap);
                return true;
            } catch (Exception e) {
                System.out.println("Error updating student: " + e.getMessage());
                return false;
            }
        }
    }

    /**
     * Update student in MySQL database
     */
    private boolean updateStudentMySQL(String id, Student student) {
        String query = "UPDATE students SET name=?, email=?, phone=?, dateOfBirth=?, department=?, address=?, enrollmentDate=? WHERE id=?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getPhone());
            stmt.setDate(4, student.getDateOfBirth() != null ? java.sql.Date.valueOf(student.getDateOfBirth().toString()) : null);
            stmt.setString(5, student.getDepartment());
            stmt.setString(6, student.getAddress());
            stmt.setDate(7, student.getEnrollmentDate() != null ? java.sql.Date.valueOf(student.getEnrollmentDate().toString()) : null);
            stmt.setString(8, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating student in MySQL: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete student
     */
    public boolean deleteStudent(String id) {
        if (useMySQL) {
            return deleteStudentMySQL(id);
        } else {
            try {
                storage.deleteStudent(id);
                return true;
            } catch (Exception e) {
                System.out.println("Error deleting student: " + e.getMessage());
                return false;
            }
        }
    }

    /**
     * Delete student from MySQL database
     */
    private boolean deleteStudentMySQL(String id) {
        String query = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting student from MySQL: " + e.getMessage());
            return false;
        }
    }

    /**
     * Search students by criteria
     */
    public List<Student> searchStudents(String searchTerm) {
        List<Student> allStudents = getAllStudents();
        List<Student> results = new ArrayList<>();
        String term = searchTerm.toLowerCase();

        for (Student student : allStudents) {
            if (student.getName().toLowerCase().contains(term) ||
                student.getEmail().toLowerCase().contains(term) ||
                student.getPhone().contains(term) ||
                (student.getDepartment() != null && student.getDepartment().toLowerCase().contains(term))) {
                results.add(student);
            }
        }
        return results;
    }

    /**
     * Validate student data
     */
    public List<String> validateStudent(Student student) {
        List<String> errors = new ArrayList<>();

        if (student.getName() == null || student.getName().trim().isEmpty()) {
            errors.add("Student name is required");
        }

        if (student.getEmail() == null || !isValidEmail(student.getEmail())) {
            errors.add("Valid email is required");
        }

        if (student.getPhone() == null || student.getPhone().trim().isEmpty()) {
            errors.add("Phone number is required");
        }

        return errors;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Get students by department
     */
    public List<Student> getStudentsByDepartment(String department) {
        List<Student> allStudents = getAllStudents();
        List<Student> results = new ArrayList<>();

        for (Student student : allStudents) {
            if (student.getDepartment() != null && student.getDepartment().equals(department)) {
                results.add(student);
            }
        }
        return results;
    }
}
