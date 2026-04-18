package com.college;

import com.sun.net.httpserver.*;
import com.college.controllers.*;
import com.college.database.*;
import com.college.models.*;
import com.college.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * CollegeManagementServer - Main server application
 *
 * This server provides REST API endpoints for the College Management System.
 * It can run with local in-memory storage or a MySQL database.
 *
 * Start the server: java com.college.CollegeManagementServer
 * API Base URL: http://localhost:8080/api
 */
public class CollegeManagementServer {
    private static HttpServer server;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static StudentController studentController = new StudentController();
    private static CourseController courseController = new CourseController();
    private static InstructorController instructorController = new InstructorController();
    private static EnrollmentController enrollmentController = new EnrollmentController();
    private static GradeController gradeController = new GradeController();
    private static LocalDataStorage storage = LocalDataStorage.getInstance();

    public static void main(String[] args) throws IOException {
        System.out.println("================================");
        System.out.println("College Management System - Server");
        System.out.println("================================\n");

        final boolean[] mysqlInitialized = {false};
        if (Constants.USE_MYSQL) {
            mysqlInitialized[0] = MySQLConnection.initializeConnection();
            if (!mysqlInitialized[0]) {
                System.err.println("MySQL initialization failed. Falling back to local storage mode.");
            }
        }

        try {
            server = HttpServer.create(new InetSocketAddress(Constants.SERVER_HOST, Constants.SERVER_PORT), 0);
            server.setExecutor(Executors.newFixedThreadPool(10));

            registerEndpoints();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (Constants.USE_MYSQL && mysqlInitialized[0]) {
                    MySQLConnection.closeConnection();
                }
            }));

            server.start();
            System.out.println("Server started successfully on http://" + Constants.SERVER_HOST + ":" + Constants.SERVER_PORT);
            System.out.println("API Base URL: http://" + Constants.SERVER_HOST + ":" + Constants.SERVER_PORT + Constants.API_BASE_PATH);
            System.out.println("\nDatabase Mode: " + (Constants.USE_MYSQL ? (mysqlInitialized[0] ? "MYSQL" : "MYSQL FAILED - LOCAL STORAGE") : "LOCAL STORAGE"));
            System.out.println("\nAvailable Endpoints:");
            System.out.println("  GET    /api/students");
            System.out.println("  POST   /api/students");
            System.out.println("  GET    /api/students/{id}");
            System.out.println("  PUT    /api/students/{id}");
            System.out.println("  DELETE /api/students/{id}");
            System.out.println("  GET    /api/courses");
            System.out.println("  POST   /api/courses");
            System.out.println("  GET    /api/courses/{id}");
            System.out.println("  PUT    /api/courses/{id}");
            System.out.println("  DELETE /api/courses/{id}");
            System.out.println("  GET    /api/instructors");
            System.out.println("  POST   /api/instructors");
            System.out.println("  GET    /api/instructors/{id}");
            System.out.println("  PUT    /api/instructors/{id}");
            System.out.println("  DELETE /api/instructors/{id}");
            System.out.println("  GET    /api/enrollments");
            System.out.println("  POST   /api/enrollments");
            System.out.println("  GET    /api/enrollments/{id}");
            System.out.println("  PUT    /api/enrollments/{id}");
            System.out.println("  DELETE /api/enrollments/{id}");
            System.out.println("  GET    /api/grades");
            System.out.println("  POST   /api/grades");
            System.out.println("  GET    /api/grades/{id}");
            System.out.println("  PUT    /api/grades/{id}");
            System.out.println("  DELETE /api/grades/{id}");
            System.out.println("  GET    /api/stats");
            System.out.println("  GET    /api/export");
            System.out.println("  GET    /api/ping");
            System.out.println("\nPress Ctrl+C to stop the server");
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void registerEndpoints() {
        server.createContext("/api/students", exchange -> handleStudents(exchange));
        server.createContext("/api/courses", exchange -> handleCourses(exchange));
        server.createContext("/api/instructors", exchange -> handleInstructors(exchange));
        server.createContext("/api/enrollments", exchange -> handleEnrollments(exchange));
        server.createContext("/api/grades", exchange -> handleGrades(exchange));
        server.createContext("/api/stats", exchange -> handleStats(exchange));
        server.createContext("/api/export", exchange -> handleExport(exchange));
        server.createContext("/api/ping", exchange -> handlePing(exchange));
        server.createContext("/", exchange -> handleCORS(exchange));
    }

    private static void handleCORS(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", Constants.ALLOWED_ORIGINS);
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", Constants.ALLOWED_METHODS);
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", Constants.ALLOWED_HEADERS);

        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(200, 0);
        }
        exchange.close();
    }

    private static void handlePing(HttpExchange exchange) throws IOException {
        addCORSHeaders(exchange);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", System.currentTimeMillis());
        response.put("message", "College Management System API is running");
        sendResponse(exchange, response, 200);
    }

    private static void handleStudents(HttpExchange exchange) throws IOException {
        addCORSHeaders(exchange);
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        try {
            switch (method) {
                case "GET":
                    if (path.contains("/api/students/")) {
                        String id = path.substring(path.lastIndexOf("/") + 1);
                        sendResponse(exchange, studentController.getStudent(id), 200);
                    } else if (query != null && query.contains("search=")) {
                        String searchTerm = getSearchTerm(query);
                        sendResponse(exchange, studentController.searchStudents(searchTerm), 200);
                    } else {
                        sendResponse(exchange, studentController.getAllStudents(), 200);
                    }
                    break;
                case "POST":
                    String body = readBody(exchange);
                    Student student = objectMapper.readValue(body, Student.class);
                    sendResponse(exchange, studentController.createStudent(student), 201);
                    break;
                case "PUT":
                    String id = path.substring(path.lastIndexOf("/") + 1);
                    String putBody = readBody(exchange);
                    Student updatedStudent = objectMapper.readValue(putBody, Student.class);
                    sendResponse(exchange, studentController.updateStudent(id, updatedStudent), 200);
                    break;
                case "DELETE":
                    String deleteId = path.substring(path.lastIndexOf("/") + 1);
                    sendResponse(exchange, studentController.deleteStudent(deleteId), 200);
                    break;
                case "OPTIONS":
                    exchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    sendResponse(exchange, APIResponse.error("Method not allowed"), 405);
            }
        } catch (Exception e) {
            sendResponse(exchange, APIResponse.error("Error: " + e.getMessage()), 400);
        }
    }

    private static void handleCourses(HttpExchange exchange) throws IOException {
        addCORSHeaders(exchange);
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        try {
            switch (method) {
                case "GET":
                    if (path.contains("/api/courses/")) {
                        String id = path.substring(path.lastIndexOf("/") + 1);
                        sendResponse(exchange, courseController.getCourse(id), 200);
                    } else if (query != null && query.contains("search=")) {
                        String searchTerm = getSearchTerm(query);
                        sendResponse(exchange, courseController.searchCourses(searchTerm), 200);
                    } else {
                        sendResponse(exchange, courseController.getAllCourses(), 200);
                    }
                    break;
                case "POST":
                    String body = readBody(exchange);
                    Course course = objectMapper.readValue(body, Course.class);
                    sendResponse(exchange, courseController.createCourse(course), 201);
                    break;
                case "PUT":
                    String id = path.substring(path.lastIndexOf("/") + 1);
                    String putBody = readBody(exchange);
                    Course updatedCourse = objectMapper.readValue(putBody, Course.class);
                    sendResponse(exchange, courseController.updateCourse(id, updatedCourse), 200);
                    break;
                case "DELETE":
                    String deleteId = path.substring(path.lastIndexOf("/") + 1);
                    sendResponse(exchange, courseController.deleteCourse(deleteId), 200);
                    break;
                case "OPTIONS":
                    exchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    sendResponse(exchange, APIResponse.error("Method not allowed"), 405);
            }
        } catch (Exception e) {
            sendResponse(exchange, APIResponse.error("Error: " + e.getMessage()), 400);
        }
    }

    private static void handleInstructors(HttpExchange exchange) throws IOException {
        addCORSHeaders(exchange);
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        try {
            switch (method) {
                case "GET":
                    if (path.contains("/api/instructors/")) {
                        String id = path.substring(path.lastIndexOf("/") + 1);
                        sendResponse(exchange, instructorController.getInstructor(id), 200);
                    } else if (query != null && query.contains("search=")) {
                        String searchTerm = getSearchTerm(query);
                        sendResponse(exchange, instructorController.searchInstructors(searchTerm), 200);
                    } else {
                        sendResponse(exchange, instructorController.getAllInstructors(), 200);
                    }
                    break;
                case "POST":
                    String body = readBody(exchange);
                    Instructor instructor = objectMapper.readValue(body, Instructor.class);
                    sendResponse(exchange, instructorController.createInstructor(instructor), 201);
                    break;
                case "PUT":
                    String id = path.substring(path.lastIndexOf("/") + 1);
                    String putBody = readBody(exchange);
                    Instructor updatedInstructor = objectMapper.readValue(putBody, Instructor.class);
                    sendResponse(exchange, instructorController.updateInstructor(id, updatedInstructor), 200);
                    break;
                case "DELETE":
                    String deleteId = path.substring(path.lastIndexOf("/") + 1);
                    sendResponse(exchange, instructorController.deleteInstructor(deleteId), 200);
                    break;
                case "OPTIONS":
                    exchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    sendResponse(exchange, APIResponse.error("Method not allowed"), 405);
            }
        } catch (Exception e) {
            sendResponse(exchange, APIResponse.error("Error: " + e.getMessage()), 400);
        }
    }

    private static void handleEnrollments(HttpExchange exchange) throws IOException {
        addCORSHeaders(exchange);
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        try {
            switch (method) {
                case "GET":
                    if (path.contains("/api/enrollments/")) {
                        String id = path.substring(path.lastIndexOf("/") + 1);
                        sendResponse(exchange, enrollmentController.getEnrollment(id), 200);
                    } else if (query != null && query.contains("search=")) {
                        String searchTerm = getSearchTerm(query);
                        sendResponse(exchange, enrollmentController.searchEnrollments(searchTerm), 200);
                    } else {
                        sendResponse(exchange, enrollmentController.getAllEnrollments(), 200);
                    }
                    break;
                case "POST":
                    String body = readBody(exchange);
                    Enrollment enrollment = objectMapper.readValue(body, Enrollment.class);
                    sendResponse(exchange, enrollmentController.createEnrollment(enrollment), 201);
                    break;
                case "PUT":
                    String id = path.substring(path.lastIndexOf("/") + 1);
                    String putBody = readBody(exchange);
                    Enrollment updatedEnrollment = objectMapper.readValue(putBody, Enrollment.class);
                    sendResponse(exchange, enrollmentController.updateEnrollment(id, updatedEnrollment), 200);
                    break;
                case "DELETE":
                    String deleteId = path.substring(path.lastIndexOf("/") + 1);
                    sendResponse(exchange, enrollmentController.deleteEnrollment(deleteId), 200);
                    break;
                case "OPTIONS":
                    exchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    sendResponse(exchange, APIResponse.error("Method not allowed"), 405);
            }
        } catch (Exception e) {
            sendResponse(exchange, APIResponse.error("Error: " + e.getMessage()), 400);
        }
    }

    private static void handleGrades(HttpExchange exchange) throws IOException {
        addCORSHeaders(exchange);
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        try {
            switch (method) {
                case "GET":
                    if (path.contains("/api/grades/")) {
                        String id = path.substring(path.lastIndexOf("/") + 1);
                        sendResponse(exchange, gradeController.getGrade(id), 200);
                    } else if (query != null && query.contains("search=")) {
                        String searchTerm = getSearchTerm(query);
                        sendResponse(exchange, gradeController.searchGrades(searchTerm), 200);
                    } else {
                        sendResponse(exchange, gradeController.getAllGrades(), 200);
                    }
                    break;
                case "POST":
                    String body = readBody(exchange);
                    Grade grade = objectMapper.readValue(body, Grade.class);
                    sendResponse(exchange, gradeController.createGrade(grade), 201);
                    break;
                case "PUT":
                    String id = path.substring(path.lastIndexOf("/") + 1);
                    String putBody = readBody(exchange);
                    Grade updatedGrade = objectMapper.readValue(putBody, Grade.class);
                    sendResponse(exchange, gradeController.updateGrade(id, updatedGrade), 200);
                    break;
                case "DELETE":
                    String deleteId = path.substring(path.lastIndexOf("/") + 1);
                    sendResponse(exchange, gradeController.deleteGrade(deleteId), 200);
                    break;
                case "OPTIONS":
                    exchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    sendResponse(exchange, APIResponse.error("Method not allowed"), 405);
            }
        } catch (Exception e) {
            sendResponse(exchange, APIResponse.error("Error: " + e.getMessage()), 400);
        }
    }

    private static void handleStats(HttpExchange exchange) throws IOException {
        addCORSHeaders(exchange);
        try {
            Map<String, Integer> stats = new LinkedHashMap<>();

            if (Constants.USE_MYSQL && MySQLConnection.isConnected()) {
                stats.put("totalStudents", getCount("students"));
                stats.put("totalCourses", getCount("courses"));
                stats.put("totalInstructors", getCount("instructors"));
                stats.put("totalEnrollments", getCount("enrollments"));
                stats.put("totalGrades", getCount("grades"));
                stats.put("activeEnrollments", getCount("enrollments", "status='Active'"));
            } else {
                stats = storage.getStatistics();
                stats.put("activeEnrollments", (stats.getOrDefault("totalEnrollments", 0)));
            }

            sendResponse(exchange, APIResponse.success("Statistics retrieved", stats), 200);
        } catch (Exception e) {
            sendResponse(exchange, APIResponse.error("Error retrieving stats"), 500);
        }
    }

    private static int getCount(String tableName) {
        return getCount(tableName, null);
    }

    private static int getCount(String tableName, String whereClause) {
        if (!MySQLConnection.isConnected()) {
            return 0;
        }
        String query = "SELECT COUNT(*) AS total FROM " + tableName;
        if (whereClause != null && !whereClause.trim().isEmpty()) {
            query += " WHERE " + whereClause;
        }
        try (Statement stmt = MySQLConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error counting " + tableName + ": " + e.getMessage());
        }
        return 0;
    }

    private static void handleExport(HttpExchange exchange) throws IOException {
        addCORSHeaders(exchange);
        try {
            Map<String, Object> allData = new LinkedHashMap<>();

            if (Constants.USE_MYSQL && MySQLConnection.isConnected()) {
                allData.put("students", MySQLConnection.executeQuery("SELECT * FROM students"));
                allData.put("courses", MySQLConnection.executeQuery("SELECT * FROM courses"));
                allData.put("instructors", MySQLConnection.executeQuery("SELECT * FROM instructors"));
                allData.put("enrollments", MySQLConnection.executeQuery("SELECT * FROM enrollments"));
                allData.put("grades", MySQLConnection.executeQuery("SELECT * FROM grades"));
            } else {
                allData = storage.exportAllData();
            }

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.getResponseHeaders().add("Content-Disposition", "attachment; filename=\"data.json\"");
            String json = objectMapper.writeValueAsString(allData);
            exchange.sendResponseHeaders(200, json.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.close();
        } catch (Exception e) {
            sendResponse(exchange, APIResponse.error("Error exporting data"), 500);
        }
    }

    private static String getSearchTerm(String query) throws UnsupportedEncodingException {
        if (query == null || !query.contains("search=")) {
            return "";
        }
        String searchTerm = query.substring(query.indexOf("search=") + 7);
        return URLDecoder.decode(searchTerm, StandardCharsets.UTF_8.name());
    }

    private static void addCORSHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", Constants.ALLOWED_ORIGINS);
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", Constants.ALLOWED_METHODS);
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", Constants.ALLOWED_HEADERS);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
    }

    private static void sendResponse(HttpExchange exchange, Object response, int statusCode) throws IOException {
        String json = objectMapper.writeValueAsString(response);
        byte[] responseBytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }

    private static String readBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        StringBuilder body = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            body.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
        }
        return body.toString();
    }
}
