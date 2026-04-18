# College Management System - Database Setup Guide

## Overview
The College Management System supports two database options:
1. **Local Storage** - Browser-based storage (default, no setup required)
2. **MySQL Database** - Full relational database with persistence

## MySQL Database Setup

### Prerequisites
- MySQL Server 8.0 or later installed
- MySQL JDBC Driver (included in the project)
- Basic knowledge of MySQL commands

### Step 1: Install MySQL Server
Download and install MySQL from https://dev.mysql.com/downloads/mysql/

### Step 2: Create Database
Run the provided SQL script to create the database and tables:

```bash
mysql -u root -p < database/schema.sql
```

Or execute the SQL commands manually in MySQL Workbench or command line.

### Step 3: Configure Database Connection
Update the connection settings in `MySQLConnection.java`:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/college_management";
private static final String DB_USER = "your_username"; // default: root
private static final String DB_PASSWORD = "your_password"; // set your MySQL password
```

### Step 4: Enable MySQL in Application
The application is already configured to use MySQL. The constants in `Constants.java` are set to:
- `USE_LOCAL_STORAGE = false`
- `USE_MYSQL = true`

### Step 5: Add MySQL JDBC Driver
The MySQL JDBC driver JAR file should be in the `backend/lib/` directory. If not, download it from:
https://dev.mysql.com/downloads/connector/j/

## Database Schema

### Tables Overview

#### students
- Stores student information
- Primary Key: id (VARCHAR)
- Foreign Keys: None
- Indexes: email, department

#### instructors
- Stores instructor information
- Primary Key: id (VARCHAR)
- Foreign Keys: None
- Indexes: email, department

#### courses
- Stores course information
- Primary Key: id (VARCHAR)
- Foreign Key: instructorId → instructors(id)
- Indexes: code, instructorId

#### enrollments
- Links students to courses
- Primary Key: id (VARCHAR)
- Foreign Keys: studentId → students(id), courseId → courses(id)
- Indexes: studentId, courseId

#### grades
- Stores student grades for enrolled courses
- Primary Key: id (VARCHAR)
- Foreign Key: enrollmentId → enrollments(id)
- Index: enrollmentId

### Grade Calculation
The overall grade is calculated as:
- Midterm: 20%
- Final: 50%
- Assignment: 20%
- Attendance: 10%

Formula: `overallGrade = (midterm * 0.2) + (final * 0.5) + (assignment * 0.2) + (attendance * 0.1)`

## Switching Between Database Types

### To Use Local Storage:
1. Edit `Constants.java`:
   ```java
   public static final boolean USE_LOCAL_STORAGE = true;
   public static final boolean USE_MYSQL = false;
   ```
2. Restart the server

### To Use MySQL:
1. Ensure MySQL is set up (steps 1-4 above)
2. Edit `Constants.java`:
   ```java
   public static final boolean USE_LOCAL_STORAGE = false;
   public static final boolean USE_MYSQL = true;
   ```
3. Restart the server

## Service Layer Architecture

The service classes (StudentService, CourseService, etc.) automatically detect the database type and use the appropriate storage method:

- **Local Storage**: Uses `LocalDataStorage` class for in-memory operations
- **MySQL**: Uses `MySQLConnection` class for database operations

All CRUD operations are abstracted, so the controllers and frontend work identically with both storage types.

## Troubleshooting

### Connection Issues
- Verify MySQL server is running: `sudo systemctl status mysql`
- Check credentials in `MySQLConnection.java`
- Ensure database `college_management` exists
- Test connection: `mysql -u username -p -h localhost college_management`

### Driver Issues
- Ensure `mysql-connector-java-8.x.x.jar` is in `backend/lib/`
- Check classpath when compiling/running

### Data Migration
To migrate from local storage to MySQL:
1. Export data from local storage (browser developer tools)
2. Use the API endpoints to recreate data in MySQL
3. Switch database configuration

## Performance Considerations

### MySQL Optimizations
- Indexes are created on frequently queried columns
- Use connection pooling for production (not implemented in this version)
- Consider partitioning for large datasets

### Local Storage Limitations
- Data persists only in browser
- Limited to ~5-10MB storage
- No concurrent access
- Data lost if browser storage is cleared

## Backup and Recovery

### MySQL Backup
```bash
mysqldump -u username -p college_management > backup.sql
```

### MySQL Restore
```bash
mysql -u username -p college_management < backup.sql
```

### Local Storage
- Data is stored in browser's localStorage
- No built-in backup mechanism
- Export data manually through the application