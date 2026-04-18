package com.college.controllers;

import com.college.models.Instructor;
import com.college.services.InstructorService;
import com.college.utils.APIResponse;

import java.util.List;

/**
 * InstructorController - REST API endpoints for instructor management
 */
public class InstructorController {
    private final InstructorService instructorService;

    public InstructorController() {
        this.instructorService = new InstructorService();
    }

    public APIResponse<?> getAllInstructors() {
        try {
            List<Instructor> instructors = instructorService.getAllInstructors();
            return APIResponse.success("Instructors retrieved successfully", instructors);
        } catch (Exception e) {
            return APIResponse.error("Error retrieving instructors: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> getInstructor(String id) {
        try {
            Instructor instructor = instructorService.getInstructorById(id);
            if (instructor != null) {
                return APIResponse.success("Instructor retrieved successfully", instructor);
            }
            return APIResponse.error("Instructor not found", null);
        } catch (Exception e) {
            return APIResponse.error("Error retrieving instructor: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> createInstructor(Instructor instructor) {
        try {
            if (instructorService.addInstructor(instructor)) {
                return APIResponse.success("Instructor created successfully", instructor);
            }
            return APIResponse.error("Failed to create instructor", null);
        } catch (Exception e) {
            return APIResponse.error("Error creating instructor: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> updateInstructor(String id, Instructor instructor) {
        try {
            if (instructorService.getInstructorById(id) == null) {
                return APIResponse.error("Instructor not found", null);
            }
            if (instructorService.updateInstructor(id, instructor)) {
                return APIResponse.success("Instructor updated successfully", instructor);
            }
            return APIResponse.error("Failed to update instructor", null);
        } catch (Exception e) {
            return APIResponse.error("Error updating instructor: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> deleteInstructor(String id) {
        try {
            if (instructorService.getInstructorById(id) == null) {
                return APIResponse.error("Instructor not found", null);
            }
            if (instructorService.deleteInstructor(id)) {
                return APIResponse.success("Instructor deleted successfully", null);
            }
            return APIResponse.error("Failed to delete instructor", null);
        } catch (Exception e) {
            return APIResponse.error("Error deleting instructor: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> searchInstructors(String term) {
        try {
            List<Instructor> instructors = instructorService.searchInstructors(term);
            return APIResponse.success("Instructor search results", instructors);
        } catch (Exception e) {
            return APIResponse.error("Error searching instructors: " + e.getMessage(), null);
        }
    }
}
