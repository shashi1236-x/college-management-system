-- Sample Data for College Management System
-- Run this after creating the database and tables

USE college_management;

-- Insert sample instructors
INSERT INTO instructors (id, name, email, phone, department, qualification, experience, specialization, hireDate) VALUES
('INS001', 'Dr. Robert Johnson', 'robert.johnson@university.edu', '123-456-7800', 'Computer Science', 'PhD', 15, 'Software Engineering', '2010-08-15'),
('INS002', 'Prof. Sarah Wilson', 'sarah.wilson@university.edu', '123-456-7801', 'Mathematics', 'PhD', 12, 'Applied Mathematics', '2012-01-10'),
('INS003', 'Dr. Michael Chen', 'michael.chen@university.edu', '123-456-7802', 'Physics', 'PhD', 10, 'Quantum Physics', '2014-09-01'),
('INS004', 'Prof. Emily Davis', 'emily.davis@university.edu', '123-456-7803', 'Chemistry', 'PhD', 8, 'Organic Chemistry', '2016-01-15'),
('INS005', 'Dr. James Brown', 'james.brown@university.edu', '123-456-7804', 'Biology', 'PhD', 14, 'Molecular Biology', '2010-03-20');

-- Insert sample students
INSERT INTO students (id, name, email, phone, dateOfBirth, department, address, enrollmentDate) VALUES
('STU001', 'John Doe', 'john.doe@email.com', '123-456-7890', '2000-01-15', 'Computer Science', '123 Main St, City, State 12345', '2023-09-01'),
('STU002', 'Jane Smith', 'jane.smith@email.com', '123-456-7891', '1999-05-20', 'Mathematics', '456 Oak Ave, City, State 12346', '2023-09-01'),
('STU003', 'Alice Johnson', 'alice.johnson@email.com', '123-456-7892', '2001-03-10', 'Physics', '789 Pine St, City, State 12347', '2023-09-01'),
('STU004', 'Bob Wilson', 'bob.wilson@email.com', '123-456-7893', '2000-07-25', 'Chemistry', '321 Elm St, City, State 12348', '2023-09-01'),
('STU005', 'Carol Brown', 'carol.brown@email.com', '123-456-7894', '1999-11-30', 'Biology', '654 Maple Ave, City, State 12349', '2023-09-01'),
('STU006', 'David Lee', 'david.lee@email.com', '123-456-7895', '2000-09-12', 'Computer Science', '987 Cedar St, City, State 12350', '2023-09-01'),
('STU007', 'Eva Garcia', 'eva.garcia@email.com', '123-456-7896', '2001-02-28', 'Mathematics', '147 Birch Ln, City, State 12351', '2023-09-01'),
('STU008', 'Frank Miller', 'frank.miller@email.com', '123-456-7897', '2000-12-05', 'Physics', '258 Spruce St, City, State 12352', '2023-09-01'),
('STU009', 'Grace Taylor', 'grace.taylor@email.com', '123-456-7898', '1999-08-18', 'Chemistry', '369 Willow Ave, City, State 12353', '2023-09-01'),
('STU010', 'Henry Anderson', 'henry.anderson@email.com', '123-456-7899', '2001-06-22', 'Biology', '741 Oak Ln, City, State 12354', '2023-09-01');

-- Insert sample courses
INSERT INTO courses (id, name, code, description, credits, department, instructorId, capacity, createdDate) VALUES
('CRS001', 'Introduction to Programming', 'CS101', 'Basic programming concepts using Java', 3, 'Computer Science', 'INS001', 30, '2023-09-01'),
('CRS002', 'Data Structures and Algorithms', 'CS201', 'Advanced data structures and algorithm design', 4, 'Computer Science', 'INS001', 25, '2023-09-01'),
('CRS003', 'Calculus I', 'MATH101', 'Fundamental concepts of calculus', 4, 'Mathematics', 'INS002', 35, '2023-09-01'),
('CRS004', 'Linear Algebra', 'MATH201', 'Matrix theory and linear transformations', 3, 'Mathematics', 'INS002', 30, '2023-09-01'),
('CRS005', 'Classical Mechanics', 'PHYS101', 'Newtonian mechanics and kinematics', 4, 'Physics', 'INS003', 28, '2023-09-01'),
('CRS006', 'Quantum Physics', 'PHYS301', 'Introduction to quantum mechanics', 4, 'Physics', 'INS003', 20, '2023-09-01'),
('CRS007', 'Organic Chemistry', 'CHEM201', 'Structure and reactions of organic compounds', 4, 'Chemistry', 'INS004', 25, '2023-09-01'),
('CRS008', 'Biochemistry', 'BIO201', 'Chemical processes in living organisms', 3, 'Biology', 'INS005', 30, '2023-09-01'),
('CRS009', 'Database Systems', 'CS301', 'Relational databases and SQL', 3, 'Computer Science', 'INS001', 25, '2023-09-01'),
('CRS010', 'Web Development', 'CS401', 'Frontend and backend web technologies', 3, 'Computer Science', 'INS001', 30, '2023-09-01');

-- Insert sample enrollments
INSERT INTO enrollments (id, studentId, courseId, enrollmentDate, status) VALUES
('ENR001', 'STU001', 'CRS001', '2023-09-01', 'Active'),
('ENR002', 'STU001', 'CRS003', '2023-09-01', 'Active'),
('ENR003', 'STU002', 'CRS003', '2023-09-01', 'Active'),
('ENR004', 'STU002', 'CRS004', '2023-09-01', 'Active'),
('ENR005', 'STU003', 'CRS005', '2023-09-01', 'Active'),
('ENR006', 'STU004', 'CRS007', '2023-09-01', 'Active'),
('ENR007', 'STU005', 'CRS008', '2023-09-01', 'Active'),
('ENR008', 'STU006', 'CRS001', '2023-09-01', 'Active'),
('ENR009', 'STU006', 'CRS002', '2023-09-01', 'Active'),
('ENR010', 'STU007', 'CRS003', '2023-09-01', 'Active'),
('ENR011', 'STU008', 'CRS005', '2023-09-01', 'Active'),
('ENR012', 'STU009', 'CRS007', '2023-09-01', 'Active'),
('ENR013', 'STU010', 'CRS008', '2023-09-01', 'Active'),
('ENR014', 'STU001', 'CRS009', '2023-09-01', 'Active'),
('ENR015', 'STU006', 'CRS010', '2023-09-01', 'Active');

-- Insert sample grades
INSERT INTO grades (id, enrollmentId, midterm, final_, assignment, attendance, overallGrade, createdDate) VALUES
('GRD001', 'ENR001', 85.0, 90.0, 88.0, 95.0, 87.5, '2023-12-15'),
('GRD002', 'ENR002', 78.0, 82.0, 85.0, 90.0, 81.0, '2023-12-15'),
('GRD003', 'ENR003', 92.0, 88.0, 90.0, 95.0, 90.5, '2023-12-15'),
('GRD004', 'ENR004', 75.0, 80.0, 78.0, 85.0, 78.0, '2023-12-15'),
('GRD005', 'ENR005', 88.0, 85.0, 87.0, 92.0, 86.5, '2023-12-15'),
('GRD006', 'ENR006', 82.0, 87.0, 84.0, 88.0, 84.0, '2023-12-15'),
('GRD007', 'ENR007', 90.0, 92.0, 89.0, 95.0, 91.0, '2023-12-15'),
('GRD008', 'ENR008', 76.0, 79.0, 80.0, 90.0, 78.5, '2023-12-15'),
('GRD009', 'ENR009', 89.0, 91.0, 88.0, 93.0, 89.5, '2023-12-15'),
('GRD010', 'ENR010', 83.0, 86.0, 85.0, 87.0, 84.5, '2023-12-15');