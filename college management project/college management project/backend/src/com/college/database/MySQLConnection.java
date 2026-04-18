package com.college.database;

import java.sql.*;
import java.util.*;

/**
 * MySQLConnection - Handles MySQL database operations for the college management system.
 * This class provides connection management and basic CRUD operations for all entities.
 * 
 * Note: Update the connection credentials (url, username, password) according to your MySQL setup.
 */
public class MySQLConnection {
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/college_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root"; // Change this to your MySQL password if different

    private static Connection connection;

    /**
     * Initialize database connection
     */
    public static boolean initializeConnection() {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("MySQL Connection established successfully");
            createTables();
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("MySQL Connection error: " + e.getMessage());
            System.out.println("Make sure MySQL is running and the credentials are correct.");
            return false;
        }
    }

    /**
     * Get database connection
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Create necessary database tables
     */
    private static void createTables() {
        try (Statement stmt = connection.createStatement()) {
            // Create students table
            String createStudents = "CREATE TABLE IF NOT EXISTS students (" +
                "id VARCHAR(50) PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "phone VARCHAR(20) NOT NULL," +
                "dateOfBirth DATE," +
                "department VARCHAR(100)," +
                "address TEXT," +
                "enrollmentDate DATE," +
                "CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
            stmt.executeUpdate(createStudents);

            // Create courses table
            String createCourses = "CREATE TABLE IF NOT EXISTS courses (" +
                "id VARCHAR(50) PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "code VARCHAR(20) UNIQUE NOT NULL," +
                "description TEXT," +
                "credits INT," +
                "department VARCHAR(100)," +
                "instructorId VARCHAR(50)," +
                "capacity INT," +
                "createdDate DATE," +
                "CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
            stmt.executeUpdate(createCourses);

            // Create instructors table
            String createInstructors = "CREATE TABLE IF NOT EXISTS instructors (" +
                "id VARCHAR(50) PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "phone VARCHAR(20) NOT NULL," +
                "department VARCHAR(100) NOT NULL," +
                "qualification VARCHAR(100)," +
                "experience INT," +
                "specialization VARCHAR(100)," +
                "hireDate DATE," +
                "CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
            stmt.executeUpdate(createInstructors);

            // Create enrollments table
            String createEnrollments = "CREATE TABLE IF NOT EXISTS enrollments (" +
                "id VARCHAR(50) PRIMARY KEY," +
                "studentId VARCHAR(50) NOT NULL," +
                "courseId VARCHAR(50) NOT NULL," +
                "enrollmentDate DATE," +
                "status VARCHAR(20)," +
                "CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (studentId) REFERENCES students(id)," +
                "FOREIGN KEY (courseId) REFERENCES courses(id)" +
                ")";
            stmt.executeUpdate(createEnrollments);

            // Create grades table
            String createGrades = "CREATE TABLE IF NOT EXISTS grades (" +
                "id VARCHAR(50) PRIMARY KEY," +
                "enrollmentId VARCHAR(50) NOT NULL," +
                "midterm DOUBLE," +
                "final_ DOUBLE," +
                "assignment DOUBLE," +
                "attendance DOUBLE," +
                "overallGrade DOUBLE," +
                "createdDate DATE," +
                "CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (enrollmentId) REFERENCES enrollments(id)" +
                ")";
            stmt.executeUpdate(createGrades);

            System.out.println("All tables created/verified successfully");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("MySQL Connection closed");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Execute a SELECT query and return results
     */
    public static List<Map<String, Object>> executeQuery(String query) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metadata.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Query execution error: " + e.getMessage());
        }
        return results;
    }

    /**
     * Execute an UPDATE/INSERT/DELETE query
     */
    public static int executeUpdate(String query) {
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Update execution error: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Check if database connection is active
     */
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
