package com.college.controllers;

import com.college.models.Enrollment;
import com.college.services.EnrollmentService;
import com.college.utils.APIResponse;

import java.util.List;

/**
 * EnrollmentController - REST API endpoints for enrollment management
 */
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController() {
        this.enrollmentService = new EnrollmentService();
    }

    public APIResponse<?> getAllEnrollments() {
        try {
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
            return APIResponse.success("Enrollments retrieved successfully", enrollments);
        } catch (Exception e) {
            return APIResponse.error("Error retrieving enrollments: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> getEnrollment(String id) {
        try {
            Enrollment enrollment = enrollmentService.getEnrollmentById(id);
            if (enrollment != null) {
                return APIResponse.success("Enrollment retrieved successfully", enrollment);
            }
            return APIResponse.error("Enrollment not found", null);
        } catch (Exception e) {
            return APIResponse.error("Error retrieving enrollment: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> createEnrollment(Enrollment enrollment) {
        try {
            if (enrollmentService.addEnrollment(enrollment)) {
                return APIResponse.success("Enrollment created successfully", enrollment);
            }
            return APIResponse.error("Failed to create enrollment", null);
        } catch (Exception e) {
            return APIResponse.error("Error creating enrollment: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> updateEnrollment(String id, Enrollment enrollment) {
        try {
            if (enrollmentService.getEnrollmentById(id) == null) {
                return APIResponse.error("Enrollment not found", null);
            }
            if (enrollmentService.updateEnrollment(id, enrollment)) {
                return APIResponse.success("Enrollment updated successfully", enrollment);
            }
            return APIResponse.error("Failed to update enrollment", null);
        } catch (Exception e) {
            return APIResponse.error("Error updating enrollment: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> deleteEnrollment(String id) {
        try {
            if (enrollmentService.getEnrollmentById(id) == null) {
                return APIResponse.error("Enrollment not found", null);
            }
            if (enrollmentService.deleteEnrollment(id)) {
                return APIResponse.success("Enrollment deleted successfully", null);
            }
            return APIResponse.error("Failed to delete enrollment", null);
        } catch (Exception e) {
            return APIResponse.error("Error deleting enrollment: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> searchEnrollments(String term) {
        try {
            List<Enrollment> enrollments = enrollmentService.searchEnrollments(term);
            return APIResponse.success("Enrollment search results", enrollments);
        } catch (Exception e) {
            return APIResponse.error("Error searching enrollments: " + e.getMessage(), null);
        }
    }
}
