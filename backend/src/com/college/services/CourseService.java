package com.college.services;

import com.college.database.LocalDataStorage;
import com.college.database.MySQLConnection;
import com.college.models.Course;
import com.college.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CourseService - Business logic for course operations
 */
public class CourseService {
    private final LocalDataStorage storage;
    private final ObjectMapper objectMapper;
    private final boolean useMySQL;

    public CourseService() {
        this.storage = LocalDataStorage.getInstance();
        this.objectMapper = new ObjectMapper();
        this.useMySQL = Constants.USE_MYSQL;
    }

    public boolean addCourse(Course course) {
        if (useMySQL) {
            return addCourseMySQL(course);
        }
        try {
            Map<String, Object> courseMap = objectMapper.convertValue(course, Map.class);
            storage.addCourse(courseMap);
            return true;
        } catch (Exception e) {
            System.out.println("Error adding course: " + e.getMessage());
            return false;
        }
    }

    private boolean addCourseMySQL(Course course) {
        String query = "INSERT INTO courses (id, name, code, description, credits, department, instructorId, capacity, createdDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, course.getId());
            stmt.setString(2, course.getName());
            stmt.setString(3, course.getCode());
            stmt.setString(4, course.getDescription());
            stmt.setInt(5, course.getCredits());
            stmt.setString(6, course.getDepartment());
            stmt.setString(7, course.getInstructorId());
            stmt.setInt(8, course.getCapacity());
            stmt.setDate(9, course.getCreatedDate() != null && !course.getCreatedDate().isEmpty() ? Date.valueOf(course.getCreatedDate()) : null);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding course to MySQL: " + e.getMessage());
            return false;
        }
    }

    public List<Course> getAllCourses() {
        if (useMySQL) {
            return getAllCoursesMySQL();
        }
        List<Course> courses = new ArrayList<>();
        for (Map<String, Object> courseMap : storage.getAllCourses()) {
            try {
                courses.add(objectMapper.convertValue(courseMap, Course.class));
            } catch (Exception e) {
                System.out.println("Error converting course: " + e.getMessage());
            }
        }
        return courses;
    }

    private List<Course> getAllCoursesMySQL() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses ORDER BY name";
        try (Statement stmt = MySQLConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Course course = mapCourseFromResultSet(rs);
                courses.add(course);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching courses from MySQL: " + e.getMessage());
        }
        return courses;
    }

    public Course getCourseById(String id) {
        if (useMySQL) {
            return getCourseByIdMySQL(id);
        }
        try {
            Map<String, Object> courseMap = storage.getCourseById(id);
            if (courseMap != null) {
                return objectMapper.convertValue(courseMap, Course.class);
            }
        } catch (Exception e) {
            System.out.println("Error getting course: " + e.getMessage());
        }
        return null;
    }

    private Course getCourseByIdMySQL(String id) {
        String query = "SELECT * FROM courses WHERE id = ?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapCourseFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching course from MySQL: " + e.getMessage());
        }
        return null;
    }

    public boolean updateCourse(String id, Course course) {
        if (useMySQL) {
            return updateCourseMySQL(id, course);
        }
        try {
            course.setId(id);
            Map<String, Object> courseMap = objectMapper.convertValue(course, Map.class);
            storage.updateCourse(id, courseMap);
            return true;
        } catch (Exception e) {
            System.out.println("Error updating course: " + e.getMessage());
            return false;
        }
    }

    private boolean updateCourseMySQL(String id, Course course) {
        String query = "UPDATE courses SET name=?, code=?, description=?, credits=?, department=?, instructorId=?, capacity=?, createdDate=? WHERE id=?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, course.getName());
            stmt.setString(2, course.getCode());
            stmt.setString(3, course.getDescription());
            stmt.setInt(4, course.getCredits());
            stmt.setString(5, course.getDepartment());
            stmt.setString(6, course.getInstructorId());
            stmt.setInt(7, course.getCapacity());
            stmt.setDate(8, course.getCreatedDate() != null && !course.getCreatedDate().isEmpty() ? Date.valueOf(course.getCreatedDate()) : null);
            stmt.setString(9, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating course in MySQL: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCourse(String id) {
        if (useMySQL) {
            return deleteCourseMySQL(id);
        }
        try {
            storage.deleteCourse(id);
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting course: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteCourseMySQL(String id) {
        String query = "DELETE FROM courses WHERE id = ?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting course from MySQL: " + e.getMessage());
            return false;
        }
    }

    public List<Course> searchCourses(String term) {
        if (useMySQL) {
            return searchCoursesMySQL(term);
        }
        List<Course> all = getAllCourses();
        List<Course> results = new ArrayList<>();
        String lowerTerm = term.toLowerCase();
        for (Course course : all) {
            if ((course.getName() != null && course.getName().toLowerCase().contains(lowerTerm)) ||
                (course.getCode() != null && course.getCode().toLowerCase().contains(lowerTerm)) ||
                (course.getDepartment() != null && course.getDepartment().toLowerCase().contains(lowerTerm))) {
                results.add(course);
            }
        }
        return results;
    }

    private List<Course> searchCoursesMySQL(String term) {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses WHERE name LIKE ? OR code LIKE ? OR department LIKE ? ORDER BY name";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            String search = "%" + term + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, search);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapCourseFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching courses in MySQL: " + e.getMessage());
        }
        return courses;
    }

    private Course mapCourseFromResultSet(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getString("id"));
        course.setName(rs.getString("name"));
        course.setCode(rs.getString("code"));
        course.setDescription(rs.getString("description"));
        course.setCredits(rs.getInt("credits"));
        course.setDepartment(rs.getString("department"));
        course.setInstructorId(rs.getString("instructorId"));
        course.setCapacity(rs.getInt("capacity"));
        course.setCreatedDate(rs.getDate("createdDate") != null ? rs.getDate("createdDate").toString() : null);
        return course;
    }
}
