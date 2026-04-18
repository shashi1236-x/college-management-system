# College Management System - Database Connections

## Database Architecture Overview

The College Management System uses a layered architecture for database operations:

```
Frontend (HTML/JS) → API Controllers → Services → Database Layer
```

## Database Layer Components

### 1. MySQLConnection.java
**Location**: `backend/src/com/college/database/MySQLConnection.java`

**Purpose**: Handles all MySQL database operations including:
- Connection management
- Table creation
- Query execution
- Connection lifecycle

**Key Methods**:
- `initializeConnection()` - Establishes database connection and creates tables
- `executeQuery(String)` - Executes SELECT queries and returns results
- `executeUpdate(String)` - Executes INSERT/UPDATE/DELETE queries
- `getConnection()` - Returns active database connection
- `closeConnection()` - Closes database connection

### 2. LocalDataStorage.java
**Location**: `backend/src/com/college/database/LocalDataStorage.java`

**Purpose**: Provides in-memory storage for development/testing when MySQL is not available.

**Key Methods**:
- `addStudent()`, `getAllStudents()`, `updateStudent()`, `deleteStudent()`
- Similar methods for other entities

### 3. Service Classes
**Location**: `backend/src/com/college/services/`

**Purpose**: Contains business logic and abstracts database operations.

**Database Selection Logic**:
```java
private boolean useMySQL = Constants.USE_MYSQL;

public boolean addStudent(Student student) {
    if (useMySQL) {
        return addStudentMySQL(student);
    } else {
        // Use LocalDataStorage
        Map<String, Object> studentMap = objectMapper.convertValue(student, Map.class);
        storage.addStudent(studentMap);
        return true;
    }
}
```

## Connection Flow

### MySQL Connection Flow:
1. **Application Start**: `CollegeManagementServer.main()` calls `MySQLConnection.initializeConnection()`
2. **Table Creation**: `createTables()` method creates all required tables if they don't exist
3. **Service Operations**: Services check `Constants.USE_MYSQL` and route to appropriate methods
4. **Query Execution**: MySQL methods use `PreparedStatement` for secure queries
5. **Result Processing**: `ResultSet` data is converted to model objects

### Local Storage Flow:
1. **Application Start**: `LocalDataStorage.getInstance()` creates singleton instance
2. **Data Operations**: All data stored in memory using `HashMap<String, Map<String, Object>>`
3. **Persistence**: Data persists only during application runtime

## Model Classes

All model classes follow the same pattern with getters/setters:

- **Student.java** - Student entity with fields: id, name, email, phone, dateOfBirth, department, address, enrollmentDate
- **Course.java** - Course entity with fields: id, name, code, description, credits, department, instructorId, capacity, createdDate
- **Instructor.java** - Instructor entity with fields: id, name, email, phone, department, qualification, experience, specialization, hireDate
- **Enrollment.java** - Enrollment entity linking students to courses
- **Grade.java** - Grade entity with automatic calculation methods

## API Controller Layer

**Location**: `backend/src/com/college/controllers/`

Controllers handle HTTP requests and delegate to services:

```java
public APIResponse<?> createStudent(Student student) {
    // Validation
    List<String> errors = studentService.validateStudent(student);
    if (!errors.isEmpty()) {
        return APIResponse.error("Validation failed", null);
    }

    // Database operation via service
    if (studentService.addStudent(student)) {
        return APIResponse.success("Student created successfully", student);
    }
    return APIResponse.error("Failed to create student", null);
}
```

## Configuration

**Location**: `backend/src/com/college/utils/Constants.java`

Database selection is controlled by:
```java
public static final boolean USE_LOCAL_STORAGE = false;
public static final boolean USE_MYSQL = true;
```

## Database Schema Relationships

```
students (1) ──── (many) enrollments (many) ──── (1) courses
                        │
                        │
                        ↓
                     grades
```

- **students**: Independent entity
- **instructors**: Independent entity
- **courses**: References instructors via instructorId
- **enrollments**: Links students to courses
- **grades**: References enrollments

## Security Considerations

- **Prepared Statements**: All MySQL queries use PreparedStatement to prevent SQL injection
- **Input Validation**: All data is validated in service layer before database operations
- **Connection Management**: Connections are properly closed using try-with-resources
- **Error Handling**: Database errors are caught and logged without exposing sensitive information

## Performance Optimizations

- **Indexes**: Created on frequently queried columns (email, department, etc.)
- **Connection Reuse**: Single connection instance reused for all operations
- **Batch Operations**: Prepared statements allow efficient query execution
- **Lazy Loading**: Data loaded only when requested

## Files Connection Summary

| Component | File | Purpose | Database Connection |
|-----------|------|---------|-------------------|
| Database Connection | `MySQLConnection.java` | MySQL operations | Direct JDBC connection |
| Local Storage | `LocalDataStorage.java` | In-memory storage | No external connection |
| Business Logic | `StudentService.java` | Student operations | Routes to appropriate storage |
| API Layer | `StudentController.java` | HTTP endpoints | Calls service methods |
| Models | `Student.java` | Data structures | Used by services |
| Configuration | `Constants.java` | App settings | Controls database selection |
| Main Server | `CollegeManagementServer.java` | HTTP server | Initializes database connections |

## Usage Examples

### Adding a Student (MySQL):
```java
// Service layer
public boolean addStudentMySQL(Student student) {
    String query = "INSERT INTO students (id, name, email, phone, ...) VALUES (?, ?, ?, ?, ...)";
    try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(query)) {
        stmt.setString(1, student.getId());
        stmt.setString(2, student.getName());
        // ... set other parameters
        return stmt.executeUpdate() > 0;
    }
}
```

### Getting Students (MySQL):
```java
public List<Student> getAllStudentsMySQL() {
    List<Student> students = new ArrayList<>();
    String query = "SELECT * FROM students ORDER BY name";
    try (ResultSet rs = MySQLConnection.getConnection().createStatement().executeQuery(query)) {
        while (rs.next()) {
            Student student = new Student();
            student.setId(rs.getString("id"));
            student.setName(rs.getString("name"));
            // ... map other fields
            students.add(student);
        }
    }
    return students;
}
```

This architecture ensures clean separation of concerns, easy testing, and seamless switching between database types.