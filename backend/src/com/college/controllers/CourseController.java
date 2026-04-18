package com.college.controllers;

import com.college.models.Course;
import com.college.services.CourseService;
import com.college.utils.APIResponse;

import java.util.List;

/**
 * CourseController - REST API endpoints for course management
 */
public class CourseController {
    private final CourseService courseService;

    public CourseController() {
        this.courseService = new CourseService();
    }

    public APIResponse<?> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            return APIResponse.success("Courses retrieved successfully", courses);
        } catch (Exception e) {
            return APIResponse.error("Error retrieving courses: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> getCourse(String id) {
        try {
            Course course = courseService.getCourseById(id);
            if (course != null) {
                return APIResponse.success("Course retrieved successfully", course);
            }
            return APIResponse.error("Course not found", null);
        } catch (Exception e) {
            return APIResponse.error("Error retrieving course: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> createCourse(Course course) {
        try {
            if (courseService.addCourse(course)) {
                return APIResponse.success("Course created successfully", course);
            }
            return APIResponse.error("Failed to create course", null);
        } catch (Exception e) {
            return APIResponse.error("Error creating course: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> updateCourse(String id, Course course) {
        try {
            if (courseService.getCourseById(id) == null) {
                return APIResponse.error("Course not found", null);
            }
            if (courseService.updateCourse(id, course)) {
                return APIResponse.success("Course updated successfully", course);
            }
            return APIResponse.error("Failed to update course", null);
        } catch (Exception e) {
            return APIResponse.error("Error updating course: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> deleteCourse(String id) {
        try {
            if (courseService.getCourseById(id) == null) {
                return APIResponse.error("Course not found", null);
            }
            if (courseService.deleteCourse(id)) {
                return APIResponse.success("Course deleted successfully", null);
            }
            return APIResponse.error("Failed to delete course", null);
        } catch (Exception e) {
            return APIResponse.error("Error deleting course: " + e.getMessage(), null);
        }
    }

    public APIResponse<?> searchCourses(String term) {
        try {
            List<Course> courses = courseService.searchCourses(term);
            return APIResponse.success("Course search results", courses);
        } catch (Exception e) {
            return APIResponse.error("Error searching courses: " + e.getMessage(), null);
        }
    }
}
