package com.college.services;

import com.college.database.LocalDataStorage;
import com.college.database.MySQLConnection;
import com.college.models.Instructor;
import com.college.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * InstructorService - Business logic for instructor operations
 */
public class InstructorService {
    private final LocalDataStorage storage;
    private final ObjectMapper objectMapper;
    private final boolean useMySQL;

    public InstructorService() {
        this.storage = LocalDataStorage.getInstance();
        this.objectMapper = new ObjectMapper();
        this.useMySQL = Constants.USE_MYSQL;
    }

    public boolean addInstructor(Instructor instructor) {
        if (useMySQL) {
            return addInstructorMySQL(instructor);
        }
        try {
            Map<String, Object> instructorMap = objectMapper.convertValue(instructor, Map.class);
            storage.addInstructor(instructorMap);
            return true;
        } catch (Exception e) {
            System.out.println("Error adding instructor: " + e.getMessage());
            return false;
        }
    }

    private boolean addInstructorMySQL(Instructor instructor) {
        String query = "INSERT INTO instructors (id, name, email, phone, department, qualification, experience, specialization, hireDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, instructor.getId());
            stmt.setString(2, instructor.getName());
            stmt.setString(3, instructor.getEmail());
            stmt.setString(4, instructor.getPhone());
            stmt.setString(5, instructor.getDepartment());
            stmt.setString(6, instructor.getQualification());
            stmt.setInt(7, instructor.getExperience());
            stmt.setString(8, instructor.getSpecialization());
            stmt.setDate(9, instructor.getHireDate() != null && !instructor.getHireDate().isEmpty() ? Date.valueOf(instructor.getHireDate()) : null);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding instructor to MySQL: " + e.getMessage());
            return false;
        }
    }

    public List<Instructor> getAllInstructors() {
        if (useMySQL) {
            return getAllInstructorsMySQL();
        }
        List<Instructor> instructors = new ArrayList<>();
        for (Map<String, Object> instructorMap : storage.getAllInstructors()) {
            try {
                instructors.add(objectMapper.convertValue(instructorMap, Instructor.class));
            } catch (Exception e) {
                System.out.println("Error converting instructor: " + e.getMessage());
            }
        }
        return instructors;
    }

    private List<Instructor> getAllInstructorsMySQL() {
        List<Instructor> instructors = new ArrayList<>();
        String query = "SELECT * FROM instructors ORDER BY name";
        try (Statement stmt = MySQLConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Instructor instructor = mapInstructorFromResultSet(rs);
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching instructors from MySQL: " + e.getMessage());
        }
        return instructors;
    }

    public Instructor getInstructorById(String id) {
        if (useMySQL) {
            return getInstructorByIdMySQL(id);
        }
        try {
            Map<String, Object> instructorMap = storage.getInstructorById(id);
            if (instructorMap != null) {
                return objectMapper.convertValue(instructorMap, Instructor.class);
            }
        } catch (Exception e) {
            System.out.println("Error getting instructor: " + e.getMessage());
        }
        return null;
    }

    private Instructor getInstructorByIdMySQL(String id) {
        String query = "SELECT * FROM instructors WHERE id = ?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapInstructorFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching instructor from MySQL: " + e.getMessage());
        }
        return null;
    }

    public boolean updateInstructor(String id, Instructor instructor) {
        if (useMySQL) {
            return updateInstructorMySQL(id, instructor);
        }
        try {
            instructor.setId(id);
            Map<String, Object> instructorMap = objectMapper.convertValue(instructor, Map.class);
            storage.updateInstructor(id, instructorMap);
            return true;
        } catch (Exception e) {
            System.out.println("Error updating instructor: " + e.getMessage());
            return false;
        }
    }

    private boolean updateInstructorMySQL(String id, Instructor instructor) {
        String query = "UPDATE instructors SET name=?, email=?, phone=?, department=?, qualification=?, experience=?, specialization=?, hireDate=? WHERE id=?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, instructor.getName());
            stmt.setString(2, instructor.getEmail());
            stmt.setString(3, instructor.getPhone());
            stmt.setString(4, instructor.getDepartment());
            stmt.setString(5, instructor.getQualification());
            stmt.setInt(6, instructor.getExperience());
            stmt.setString(7, instructor.getSpecialization());
            stmt.setDate(8, instructor.getHireDate() != null && !instructor.getHireDate().isEmpty() ? Date.valueOf(instructor.getHireDate()) : null);
            stmt.setString(9, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating instructor in MySQL: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteInstructor(String id) {
        if (useMySQL) {
            return deleteInstructorMySQL(id);
        }
        try {
            storage.deleteInstructor(id);
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting instructor: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteInstructorMySQL(String id) {
        String query = "DELETE FROM instructors WHERE id = ?";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting instructor from MySQL: " + e.getMessage());
            return false;
        }
    }

    public List<Instructor> searchInstructors(String term) {
        if (useMySQL) {
            return searchInstructorsMySQL(term);
        }
        List<Instructor> all = getAllInstructors();
        List<Instructor> results = new ArrayList<>();
        String lowerTerm = term.toLowerCase();
        for (Instructor instructor : all) {
            if ((instructor.getName() != null && instructor.getName().toLowerCase().contains(lowerTerm)) ||
                (instructor.getEmail() != null && instructor.getEmail().toLowerCase().contains(lowerTerm)) ||
                (instructor.getPhone() != null && instructor.getPhone().toLowerCase().contains(lowerTerm)) ||
                (instructor.getDepartment() != null && instructor.getDepartment().toLowerCase().contains(lowerTerm))) {
                results.add(instructor);
            }
        }
        return results;
    }

    private List<Instructor> searchInstructorsMySQL(String term) {
        List<Instructor> instructors = new ArrayList<>();
        String query = "SELECT * FROM instructors WHERE name LIKE ? OR email LIKE ? OR phone LIKE ? OR department LIKE ? ORDER BY name";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
            String search = "%" + term + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, search);
            stmt.setString(4, search);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    instructors.add(mapInstructorFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching instructors in MySQL: " + e.getMessage());
        }
        return instructors;
    }

    private Instructor mapInstructorFromResultSet(ResultSet rs) throws SQLException {
        Instructor instructor = new Instructor();
        instructor.setId(rs.getString("id"));
        instructor.setName(rs.getString("name"));
        instructor.setEmail(rs.getString("email"));
        instructor.setPhone(rs.getString("phone"));
        instructor.setDepartment(rs.getString("department"));
        instructor.setQualification(rs.getString("qualification"));
        instructor.setExperience(rs.getInt("experience"));
        instructor.setSpecialization(rs.getString("specialization"));
        instructor.setHireDate(rs.getDate("hireDate") != null ? rs.getDate("hireDate").toString() : null);
        return instructor;
    }
}
