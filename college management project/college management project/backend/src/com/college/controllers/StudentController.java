package com.college.controllers;

import com.college.models.*;
import com.college.services.StudentService;
import com.college.database.LocalDataStorage;
import com.college.utils.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

/**
 * StudentController - REST API endpoints for student management
 */
public class StudentController {
    private StudentService studentService;
    private ObjectMapper objectMapper;

    public StudentController() {
        this.studentService = new StudentService();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * GET /api/students - Get all students
     */
    public APIResponse<?> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return APIResponse.success("Students retrieved successfully", students);
        } catch (Exception e) {
            return APIResponse.error("Error retrieving students: " + e.getMessage(), null);
        }
    }

    /**
     * GET /api/students/{id} - Get student by ID
     */
    public APIResponse<?> getStudent(String id) {
        try {
            Student student = studentService.getStudentById(id);
            if (student != null) {
                return APIResponse.success("Student retrieved successfully", student);
            }
            return APIResponse.error("Student not found", null);
        } catch (Exception e) {
            return APIResponse.error("Error retrieving student: " + e.getMessage(), null);
        }
    }

    /**
     * POST /api/students - Create new student
     */
    public APIResponse<?> createStudent(Student student) {
        try {
            // Validate
            List<String> errors = studentService.validateStudent(student);
            if (!errors.isEmpty()) {
                return APIResponse.error("Validation failed: " + String.join(", ", errors), null);
            }

            if (studentService.addStudent(student)) {
                return APIResponse.success("Student created successfully", student);
            }
            return APIResponse.error("Failed to create student", null);
        } catch (Exception e) {
            return APIResponse.error("Error creating student: " + e.getMessage(), null);
        }
    }

    /**
     * PUT /api/students/{id} - Update student
     */
    public APIResponse<?> updateStudent(String id, Student student) {
        try {
            Student existing = studentService.getStudentById(id);
            if (existing == null) {
                return APIResponse.error("Student not found", null);
            }

            List<String> errors = studentService.validateStudent(student);
            if (!errors.isEmpty()) {
                return APIResponse.error("Validation failed: " + String.join(", ", errors), null);
            }

            if (studentService.updateStudent(id, student)) {
                return APIResponse.success("Student updated successfully", student);
            }
            return APIResponse.error("Failed to update student", null);
        } catch (Exception e) {
            return APIResponse.error("Error updating student: " + e.getMessage(), null);
        }
    }

    /**
     * DELETE /api/students/{id} - Delete student
     */
    public APIResponse<?> deleteStudent(String id) {
        try {
            Student existing = studentService.getStudentById(id);
            if (existing == null) {
                return APIResponse.error("Student not found", null);
            }

            if (studentService.deleteStudent(id)) {
                return APIResponse.success("Student deleted successfully", null);
            }
            return APIResponse.error("Failed to delete student", null);
        } catch (Exception e) {
            return APIResponse.error("Error deleting student: " + e.getMessage(), null);
        }
    }

    /**
     * GET /api/students?search=term - Search students
     */
    public APIResponse<?> searchStudents(String searchTerm) {
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return APIResponse.error("Search term is required", null);
            }

            List<Student> results = studentService.searchStudents(searchTerm);
            return APIResponse.success("Search completed successfully", results);
        } catch (Exception e) {
            return APIResponse.error("Error searching students: " + e.getMessage(), null);
        }
    }

    /**
     * GET /api/students/by-department/{department} - Get students by department
     */
    public APIResponse<?> getStudentsByDepartment(String department) {
        try {
            List<Student> students = studentService.getStudentsByDepartment(department);
            return APIResponse.success("Students retrieved successfully", students);
        } catch (Exception e) {
            return APIResponse.error("Error retrieving students: " + e.getMessage(), null);
        }
    }
}
