# MySQL Database Setup Guide

## Prerequisites

- MySQL Server 5.7 or higher installed
- MySQL command-line client or MySQL Workbench
- Basic SQL knowledge

## Step 1: Create Database

Open MySQL command-line or MySQL Workbench and run:

```sql
-- Create the database
CREATE DATABASE IF NOT EXISTS college_management;

-- Select the database
USE college_management;
```

## Step 2: Create Tables

The tables are created automatically when the server starts, but you can also create them manually:

```sql
-- Students Table
CREATE TABLE IF NOT EXISTS students (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    dateOfBirth DATE,
    department VARCHAR(100),
    address TEXT,
    enrollmentDate DATE,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Courses Table
CREATE TABLE IF NOT EXISTS courses (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) UNIQUE NOT NULL,
    description TEXT,
    credits INT,
    department VARCHAR(100),
    instructorId VARCHAR(50),
    capacity INT,
    createdDate DATE,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Instructors Table
CREATE TABLE IF NOT EXISTS instructors (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    department VARCHAR(100) NOT NULL,
    qualification VARCHAR(100),
    experience INT,
    specialization VARCHAR(100),
    hireDate DATE,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enrollments Table
CREATE TABLE IF NOT EXISTS enrollments (
    id VARCHAR(50) PRIMARY KEY,
    studentId VARCHAR(50) NOT NULL,
    courseId VARCHAR(50) NOT NULL,
    enrollmentDate DATE,
    status VARCHAR(20),
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (studentId) REFERENCES students(id),
    FOREIGN KEY (courseId) REFERENCES courses(id)
);

-- Grades Table
CREATE TABLE IF NOT EXISTS grades (
    id VARCHAR(50) PRIMARY KEY,
    enrollmentId VARCHAR(50) NOT NULL,
    midterm DOUBLE,
    final_ DOUBLE,
    assignment DOUBLE,
    attendance DOUBLE,
    overallGrade DOUBLE,
    createdDate DATE,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (enrollmentId) REFERENCES enrollments(id)
);
```

## Step 3: Configure Java Application

Edit `backend/src/com/college/database/MySQLConnection.java`:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/college_management";
private static final String DB_USER = "root";           // Your MySQL username
private static final String DB_PASSWORD = "root";           // Your MySQL password
```

Replace:
- `localhost` with your MySQL server host (if not local)
- `3306` with your MySQL port (if different)
- `root` with your MySQL username
- Empty string with your MySQL password

## Step 4: Enable MySQL Mode

Edit `backend/src/com/college/utils/Constants.java`:

```java
public static final boolean USE_LOCAL_STORAGE = false;  // Disable local storage
public static final boolean USE_MYSQL = true;           // Enable MySQL
```

## Step 5: Add MySQL JDBC Driver

Ensure MySQL JDBC Driver is in `backend/lib/`:

```
mysql-connector-java-8.0.x.jar
```

Download from: https://dev.mysql.com/downloads/connector/j/

## Step 6: Compile and Run

```bash
# Compile
cd backend
javac -d bin -cp "lib/*" src/com/college/**/*.java

# Run
java -cp "bin:lib/*" com.college.CollegeManagementServer
```

## Verifying Connection

When the server starts, you should see:

```
MySQL Connection established successfully
All tables created/verified successfully
```

## Sample Data

To insert sample data:

```sql
-- Insert sample instructor
INSERT INTO instructors VALUES(
    'INS-001',
    'Dr. John Smith',
    'john.smith@college.edu',
    '1234567890',
    'Computer Science',
    'Ph.D. Computer Science',
    10,
    'AI and ML',
    '2014-01-15',
    CURRENT_TIMESTAMP
);

-- Insert sample course
INSERT INTO courses VALUES(
    'CRS-001',
    'Data Structures',
    'CS201',
    'Advanced data structures and algorithms',
    4,
    'Computer Science',
    'INS-001',
    40,
    CURDATE(),
    CURRENT_TIMESTAMP
);

-- Insert sample student
INSERT INTO students VALUES(
    'STU-001',
    'Alice Johnson',
    'alice@example.com',
    '9876543210',
    '2000-05-20',
    'Computer Science',
    '123 Main St',
    CURDATE(),
    CURRENT_TIMESTAMP
);

-- Insert sample enrollment
INSERT INTO enrollments VALUES(
    'ENR-001',
    'STU-001',
    'CRS-001',
    CURDATE(),
    'Active',
    CURRENT_TIMESTAMP
);

-- Insert sample grade
INSERT INTO grades VALUES(
    'GRD-001',
    'ENR-001',
    85,
    90,
    88,
    95,
    89.3,
    CURDATE(),
    CURRENT_TIMESTAMP
);
```

## Backup Database

```bash
# Backup
mysqldump -u root -p college_management > backup.sql

# Restore
mysql -u root -p college_management < backup.sql
```

## Troubleshooting

### Connection Refused
- Ensure MySQL is running: `sudo service mysql start` (Linux)
- Check port is correct: `SHOW GLOBAL VARIABLES LIKE 'port';`

### Authentication Failed
- Verify username and password
- Reset password: `ALTER USER 'root'@'localhost' IDENTIFIED BY 'newpassword';`

### Driver Not Found
- Download MySQL JDBC Driver
- Place in `backend/lib/` directory
- Ensure filename matches in compilation command

### Tables Not Created
- Check user permissions
- Run table creation script manually
- Check MySQL error logs

## Performance Tips

1. **Add Indexes:**
   ```sql
   CREATE INDEX idx_student_email ON students(email);
   CREATE INDEX idx_course_code ON courses(code);
   CREATE INDEX idx_enrollment_student ON enrollments(studentId);
   ```

2. **Connection Pooling:**
   - Consider using HikariCP for production
   - Modify MySQLConnection.java to implement pooling

3. **Query Optimization:**
   - Add appropriate indexes
   - Monitor slow queries with `SET GLOBAL slow_query_log=ON;`

## Production Deployment

For production:
1. Use strong MySQL password
2. Create restricted database user
3. Enable SSL/TLS for connections
4. Use connection pooling
5. Implement query caching
6. Regular backups
7. Monitor performance

For more information, visit: https://dev.mysql.com/doc/
