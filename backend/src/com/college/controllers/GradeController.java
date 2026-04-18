package com.college.controllers;

import com.college.models.Grade;
import com.college.services.GradeService;
import com.college.utils.APIResponse;

import java.util.List;

/**
 * GradeController - REST API endpoints for grade management
 */
public class GradeController {
    private final GradeService gradeService;

    public GradeController() {
        this.gradeService = new GradeService();
    }

    public APIResponse<?> getAllGrades() {
        try {
            List<Grade> grades = gradeService.getAllGrades();
            return APIResponse.success("Grades retrieved successfully", grades);
        } catch (Exception e) {
            return APIResponse.error("Error retrieving grades: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> getGrade(String id) {
        try {
            Grade grade = gradeService.getGradeById(id);
            if (grade != null) {
                return APIResponse.success("Grade retrieved successfully", grade);
            }
            return APIResponse.error("Grade not found", null);
        } catch (Exception e) {
            return APIResponse.error("Error retrieving grade: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> createGrade(Grade grade) {
        try {
            if (gradeService.addGrade(grade)) {
                return APIResponse.success("Grade created successfully", grade);
            }
            return APIResponse.error("Failed to create grade", null);
        } catch (Exception e) {
            return APIResponse.error("Error creating grade: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> updateGrade(String id, Grade grade) {
        try {
            if (gradeService.getGradeById(id) == null) {
                return APIResponse.error("Grade not found", null);
            }
            if (gradeService.updateGrade(id, grade)) {
                return APIResponse.success("Grade updated successfully", grade);
            }
            return APIResponse.error("Failed to update grade", null);
        } catch (Exception e) {
            return APIResponse.error("Error updating grade: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> deleteGrade(String id) {
        try {
            if (gradeService.getGradeById(id) == null) {
                return APIResponse.error("Grade not found", null);
            }
            if (gradeService.deleteGrade(id)) {
                return APIResponse.success("Grade deleted successfully", null);
            }
            return APIResponse.error("Failed to delete grade", null);
        } catch (Exception e) {
            return APIResponse.error("Error deleting grade: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> searchGrades(String term) {
        try {
            List<Grade> grades = gradeService.searchGrades(term);
            return APIResponse.success("Grade search results", grades);
        } catch (Exception e) {
            return APIResponse.error("Error searching grades: " + e.getMessage(), null);
        }
    }
}
