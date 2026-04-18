-- College Management System Database Schema
-- MySQL Database Setup Script
-- Run this script to create the database and tables

-- Create database
CREATE DATABASE IF NOT EXISTS college_management;
USE college_management;

-- Create students table
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

-- Create instructors table
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

-- Create courses table
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
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (instructorId) REFERENCES instructors(id)
);

-- Create enrollments table
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

-- Create grades table
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

-- Create indexes for better performance
CREATE INDEX idx_students_email ON students(email);
CREATE INDEX idx_students_department ON students(department);
CREATE INDEX idx_instructors_email ON instructors(email);
CREATE INDEX idx_instructors_department ON instructors(department);
CREATE INDEX idx_courses_code ON courses(code);
CREATE INDEX idx_courses_instructor ON courses(instructorId);
CREATE INDEX idx_enrollments_student ON enrollments(studentId);
CREATE INDEX idx_enrollments_course ON enrollments(courseId);
CREATE INDEX idx_grades_enrollment ON grades(enrollmentId);

-- Insert sample data (optional)
-- You can uncomment and modify these INSERT statements to add sample data

/*
-- Sample students
INSERT INTO students (id, name, email, phone, dateOfBirth, department, address, enrollmentDate) VALUES
('STU001', 'John Doe', 'john.doe@email.com', '123-456-7890', '2000-01-15', 'Computer Science', '123 Main St', '2023-09-01'),
('STU002', 'Jane Smith', 'jane.smith@email.com', '123-456-7891', '1999-05-20', 'Mathematics', '456 Oak Ave', '2023-09-01');

-- Sample instructors
INSERT INTO instructors (id, name, email, phone, department, qualification, experience, specialization, hireDate) VALUES
('INS001', 'Dr. Robert Johnson', 'robert.johnson@university.edu', '123-456-7800', 'Computer Science', 'PhD', 15, 'Software Engineering', '2010-08-15'),
('INS002', 'Prof. Sarah Wilson', 'sarah.wilson@university.edu', '123-456-7801', 'Mathematics', 'PhD', 12, 'Applied Mathematics', '2012-01-10');

-- Sample courses
INSERT INTO courses (id, name, code, description, credits, department, instructorId, capacity, createdDate) VALUES
('CRS001', 'Introduction to Programming', 'CS101', 'Basic programming concepts', 3, 'Computer Science', 'INS001', 30, '2023-09-01'),
('CRS002', 'Calculus I', 'MATH101', 'Differential and integral calculus', 4, 'Mathematics', 'INS002', 25, '2023-09-01');

-- Sample enrollments
INSERT INTO enrollments (id, studentId, courseId, enrollmentDate, status) VALUES
('ENR001', 'STU001', 'CRS001', '2023-09-01', 'Active'),
('ENR002', 'STU002', 'CRS002', '2023-09-01', 'Active');

-- Sample grades
INSERT INTO grades (id, enrollmentId, midterm, final_, assignment, attendance, overallGrade, createdDate) VALUES
('GRD001', 'ENR001', 85.0, 90.0, 88.0, 95.0, 89.2, '2023-12-15'),
('GRD002', 'ENR002', 78.0, 82.0, 85.0, 90.0, 83.4, '2023-12-15');
*/