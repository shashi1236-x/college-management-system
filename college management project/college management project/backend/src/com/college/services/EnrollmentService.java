package com.college.services;

import com.college.database.LocalDataStorage;
import com.college.database.MySQLConnection;
import com.college.models.Enrollment;
import com.college.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * EnrollmentService - Business logic for enrollment operations
 */
public class EnrollmentService {
    private final LocalDataStorage storage;
    private final ObjectMapper objectMapper;
    private final boolean useMySQL;

    public EnrollmentService() {
        this.storage = LocalDataStorage.getInstance();
        this.objectMapper = new ObjectMapper();
        this.useMySQL = Constants.USE_MYSQL;
    }

    public boolean addEnrollment(Enrollment enrollment) {
        if (useMySQL) {
            return addEnrollmentMySQL(enrollment);
        }
        try {
            Map<String, Object> enrollmentMap = objectMapper.convertValue(enrollment, Map.class);
            storage.addEnrollment(enrollmentMap);
            return true;
        } catch (Exception e) {
            System.out.println("Error adding enrollment: " + e.getMessage());
            return false;
        }
    }

    private boolean addEnrollmentMySQL(Enrollment enrollment) {
        String query = "INSERT INTO enrollments (id, studentId, courseId, enrollmentDate, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, enrollment.getId());
            stmt.setString(2, enrollment.getStudentId());
            stmt.setString(3, enrollment.getCourseId());
            stmt.setDate(4, enrollment.getEnrollmentDate() != null && !enrollment.getEnrollmentDate().isEmpty() ? Date.valueOf(enrollment.getEnrollmentDate()) : null);
            stmt.setString(5, enrollment.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding enrollment to MySQL: " + e.getMessage());
            return false;
        }
    }

    public List<Enrollment> getAllEnrollments() {
        if (useMySQL) {
            return getAllEnrollmentsMySQL();
        }
        List<Enrollment> enrollments = new ArrayList<>();
        for (Map<String, Object> enrollmentMap : storage.getAllEnrollments()) {
            try {
                enrollments.add(objectMapper.convertValue(enrollmentMap, Enrollment.class));
            } catch (Exception e) {
                System.out.println("Error converting enrollment: " + e.getMessage());
            }
        }
        return enrollments;
    }

    private List<Enrollment> getAllEnrollmentsMySQL() {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT * FROM enrollments ORDER BY enrollmentDate DESC";
        try (Statement stmt = MySQLConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                enrollments.add(mapEnrollmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching enrollments from MySQL: " + e.getMessage());
        }
        return enrollments;
    }

    public Enrollment getEnrollmentById(String id) {
        if (useMySQL) {
            return getEnrollmentByIdMySQL(id);
        }
        try {
            Map<String, Object> enrollmentMap = storage.getEnrollmentById(id);
            if (enrollmentMap != null) {
                return objectMapper.convertValue(enrollmentMap, Enrollment.class);
            }
        } catch (Exception e) {
            System.out.println("Error getting enrollment: " + e.getMessage());
        }
        return null;
    }

    private Enrollment getEnrollmentByIdMySQL(String id) {
        String query = "SELECT * FROM enrollments WHERE id = ?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapEnrollmentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching enrollment from MySQL: " + e.getMessage());
        }
        return null;
    }

    public boolean updateEnrollment(String id, Enrollment enrollment) {
        if (useMySQL) {
            return updateEnrollmentMySQL(id, enrollment);
        }
        try {
            enrollment.setId(id);
            Map<String, Object> enrollmentMap = objectMapper.convertValue(enrollment, Map.class);
            storage.updateEnrollment(id, enrollmentMap);
            return true;
        } catch (Exception e) {
            System.out.println("Error updating enrollment: " + e.getMessage());
            return false;
        }
    }

    private boolean updateEnrollmentMySQL(String id, Enrollment enrollment) {
        String query = "UPDATE enrollments SET studentId=?, courseId=?, enrollmentDate=?, status=? WHERE id=?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, enrollment.getStudentId());
            stmt.setString(2, enrollment.getCourseId());
            stmt.setDate(3, enrollment.getEnrollmentDate() != null && !enrollment.getEnrollmentDate().isEmpty() ? Date.valueOf(enrollment.getEnrollmentDate()) : null);
            stmt.setString(4, enrollment.getStatus());
            stmt.setString(5, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating enrollment in MySQL: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEnrollment(String id) {
        if (useMySQL) {
            return deleteEnrollmentMySQL(id);
        }
        try {
            storage.deleteEnrollment(id);
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting enrollment: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteEnrollmentMySQL(String id) {
        String query = "DELETE FROM enrollments WHERE id = ?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting enrollment from MySQL: " + e.getMessage());
            return false;
        }
    }

    public List<Enrollment> searchEnrollments(String term) {
        if (useMySQL) {
            return searchEnrollmentsMySQL(term);
        }
        List<Enrollment> all = getAllEnrollments();
        List<Enrollment> results = new ArrayList<>();
        String lowerTerm = term.toLowerCase();
        for (Enrollment enrollment : all) {
            if ((enrollment.getStudentId() != null && enrollment.getStudentId().toLowerCase().contains(lowerTerm)) ||
                (enrollment.getCourseId() != null && enrollment.getCourseId().toLowerCase().contains(lowerTerm)) ||
                (enrollment.getStatus() != null && enrollment.getStatus().toLowerCase().contains(lowerTerm))) {
                results.add(enrollment);
            }
        }
        return results;
    }

    private List<Enrollment> searchEnrollmentsMySQL(String term) {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT e.* FROM enrollments e " +
                       "LEFT JOIN students s ON e.studentId = s.id " +
                       "LEFT JOIN courses c ON e.courseId = c.id " +
                       "WHERE s.name LIKE ? OR c.name LIKE ? OR e.status LIKE ? ORDER BY e.enrollmentDate DESC";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            String search = "%" + term + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, search);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapEnrollmentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching enrollments in MySQL: " + e.getMessage());
        }
        return enrollments;
    }

    private Enrollment mapEnrollmentFromResultSet(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(rs.getString("id"));
        enrollment.setStudentId(rs.getString("studentId"));
        enrollment.setCourseId(rs.getString("courseId"));
        enrollment.setEnrollmentDate(rs.getDate("enrollmentDate") != null ? rs.getDate("enrollmentDate").toString() : null);
        enrollment.setStatus(rs.getString("status"));
        return enrollment;
    }
}
