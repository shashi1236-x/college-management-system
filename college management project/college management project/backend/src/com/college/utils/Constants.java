package com.college.utils;

/**
 * Constants - Application-wide constants
 */
public class Constants {
    // API Configuration
    public static final String API_BASE_PATH = "/api";
    public static final String API_VERSION = "v1";
    
    // Server Configuration
    public static final int SERVER_PORT = 8080;
    public static final String SERVER_HOST = "localhost";
    
    // CORS Configuration
    public static final String ALLOWED_ORIGINS = "*";
    public static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    public static final String ALLOWED_HEADERS = "Content-Type, Authorization";
    
    // Database Configuration
    public static final boolean USE_LOCAL_STORAGE = true; // Set to true for local storage, false for MySQL
    public static final boolean USE_MYSQL = false; // Set to true to use MySQL database
    
    // Status Constants
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_DROPPED = "Dropped";
    
    // Validation Constants
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_EMAIL_LENGTH = 100;
    
    // Pagination Constants
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    
    // Response Messages
    public static final String MSG_SUCCESS = "Operation completed successfully";
    public static final String MSG_ERROR = "An error occurred";
    public static final String MSG_NOT_FOUND = "Resource not found";
    public static final String MSG_INVALID_INPUT = "Invalid input provided";
    public static final String MSG_UNAUTHORIZED = "Unauthorized access";
    public static final String MSG_FORBIDDEN = "Access forbidden";
}
