package com.college.services;

import com.college.database.LocalDataStorage;
import com.college.database.MySQLConnection;
import com.college.models.Grade;
import com.college.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GradeService - Business logic for grade operations
 */
public class GradeService {
    private final LocalDataStorage storage;
    private final ObjectMapper objectMapper;
    private final boolean useMySQL;

    public GradeService() {
        this.storage = LocalDataStorage.getInstance();
        this.objectMapper = new ObjectMapper();
        this.useMySQL = Constants.USE_MYSQL;
    }

    public boolean addGrade(Grade grade) {
        grade.calculateOverallGrade();
        if (useMySQL) {
            return addGradeMySQL(grade);
        }
        try {
            Map<String, Object> gradeMap = objectMapper.convertValue(grade, Map.class);
            storage.addGrade(gradeMap);
            return true;
        } catch (Exception e) {
            System.out.println("Error adding grade: " + e.getMessage());
            return false;
        }
    }

    private boolean addGradeMySQL(Grade grade) {
        String query = "INSERT INTO grades (id, enrollmentId, midterm, final_, assignment, attendance, overallGrade, createdDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, grade.getId());
            stmt.setString(2, grade.getEnrollmentId());
            stmt.setDouble(3, grade.getMidterm());
            stmt.setDouble(4, grade.getFinal());
            stmt.setDouble(5, grade.getAssignment());
            stmt.setDouble(6, grade.getAttendance());
            stmt.setDouble(7, grade.getOverallGrade());
            stmt.setDate(8, grade.getCreatedDate() != null && !grade.getCreatedDate().isEmpty() ? Date.valueOf(grade.getCreatedDate()) : null);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding grade to MySQL: " + e.getMessage());
            return false;
        }
    }

    public List<Grade> getAllGrades() {
        if (useMySQL) {
            return getAllGradesMySQL();
        }
        List<Grade> grades = new ArrayList<>();
        for (Map<String, Object> gradeMap : storage.getAllGrades()) {
            try {
                grades.add(objectMapper.convertValue(gradeMap, Grade.class));
            } catch (Exception e) {
                System.out.println("Error converting grade: " + e.getMessage());
            }
        }
        return grades;
    }

    private List<Grade> getAllGradesMySQL() {
        List<Grade> grades = new ArrayList<>();
        String query = "SELECT * FROM grades ORDER BY createdDate DESC";
        try (Statement stmt = MySQLConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                grades.add(mapGradeFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching grades from MySQL: " + e.getMessage());
        }
        return grades;
    }

    public Grade getGradeById(String id) {
        if (useMySQL) {
            return getGradeByIdMySQL(id);
        }
        try {
            Map<String, Object> gradeMap = storage.getGradeById(id);
            if (gradeMap != null) {
                return objectMapper.convertValue(gradeMap, Grade.class);
            }
        } catch (Exception e) {
            System.out.println("Error getting grade: " + e.getMessage());
        }
        return null;
    }

    private Grade getGradeByIdMySQL(String id) {
        String query = "SELECT * FROM grades WHERE id = ?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapGradeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching grade from MySQL: " + e.getMessage());
        }
        return null;
    }

    public boolean updateGrade(String id, Grade grade) {
        grade.calculateOverallGrade();
        if (useMySQL) {
            return updateGradeMySQL(id, grade);
        }
        try {
            grade.setId(id);
            Map<String, Object> gradeMap = objectMapper.convertValue(grade, Map.class);
            storage.updateGrade(id, gradeMap);
            return true;
        } catch (Exception e) {
            System.out.println("Error updating grade: " + e.getMessage());
            return false;
        }
    }

    private boolean updateGradeMySQL(String id, Grade grade) {
        String query = "UPDATE grades SET enrollmentId=?, midterm=?, final_=?, assignment=?, attendance=?, overallGrade=?, createdDate=? WHERE id=?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, grade.getEnrollmentId());
            stmt.setDouble(2, grade.getMidterm());
            stmt.setDouble(3, grade.getFinal());
            stmt.setDouble(4, grade.getAssignment());
            stmt.setDouble(5, grade.getAttendance());
            stmt.setDouble(6, grade.getOverallGrade());
            stmt.setDate(7, grade.getCreatedDate() != null && !grade.getCreatedDate().isEmpty() ? Date.valueOf(grade.getCreatedDate()) : null);
            stmt.setString(8, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating grade in MySQL: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteGrade(String id) {
        if (useMySQL) {
            return deleteGradeMySQL(id);
        }
        try {
            storage.deleteGrade(id);
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting grade: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteGradeMySQL(String id) {
        String query = "DELETE FROM grades WHERE id = ?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting grade from MySQL: " + e.getMessage());
            return false;
        }
    }

    public List<Grade> searchGrades(String term) {
        if (useMySQL) {
            return searchGradesMySQL(term);
        }
        List<Grade> all = getAllGrades();
        List<Grade> results = new ArrayList<>();
        String lowerTerm = term.toLowerCase();
        for (Grade grade : all) {
            if ((grade.getEnrollmentId() != null && grade.getEnrollmentId().toLowerCase().contains(lowerTerm)) ||
                String.valueOf(grade.getOverallGrade()).contains(lowerTerm)) {
                results.add(grade);
            }
        }
        return results;
    }

    private List<Grade> searchGradesMySQL(String term) {
        List<Grade> grades = new ArrayList<>();
        String query = "SELECT g.* FROM grades g " +
                       "LEFT JOIN enrollments e ON g.enrollmentId = e.id " +
                       "LEFT JOIN students s ON e.studentId = s.id " +
                       "LEFT JOIN courses c ON e.courseId = c.id " +
                       "WHERE s.name LIKE ? OR c.name LIKE ? OR g.overallGrade LIKE ? ORDER BY g.createdDate DESC";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            String search = "%" + term + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, search);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    grades.add(mapGradeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching grades in MySQL: " + e.getMessage());
        }
        return grades;
    }

    private Grade mapGradeFromResultSet(ResultSet rs) throws SQLException {
        Grade grade = new Grade();
        grade.setId(rs.getString("id"));
        grade.setEnrollmentId(rs.getString("enrollmentId"));
        grade.setMidterm(rs.getDouble("midterm"));
        grade.setFinal(rs.getDouble("final_"));
        grade.setAssignment(rs.getDouble("assignment"));
        grade.setAttendance(rs.getDouble("attendance"));
        grade.setOverallGrade(rs.getDouble("overallGrade"));
        grade.setCreatedDate(rs.getDate("createdDate") != null ? rs.getDate("createdDate").toString() : null);
        return grade;
    }
}
