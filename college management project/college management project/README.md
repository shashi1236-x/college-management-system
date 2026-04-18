# College Management System

A comprehensive college management system built with HTML/CSS/JavaScript frontend and Java backend.

## Features

- **Student Management**: Add, edit, delete, and search students
- **Course Management**: Manage courses with details like code, credits, instructor assignment
- **Instructor Management**: Manage faculty members with qualifications and experience
- **Enrollment Management**: Enroll students in courses
- **Grade Management**: Track and manage student grades
- **Reports & Analytics**: Generate reports on student performance, courses, enrollments
- **Responsive Design**: Works on desktop, tablet, and mobile devices
- **Local Storage**: Uses browser local storage for frontend data persistence
- **REST API**: Backend provides REST API endpoints for all operations
- **Database Flexibility**: Can use local storage or connect to MySQL database

## Project Structure

```
college-management-system/
├── frontend/
│   ├── index.html                 # Dashboard
│   ├── students.html              # Student Management
│   ├── courses.html               # Course Management
│   ├── instructors.html           # Instructor Management
│   ├── enrollments.html           # Enrollment Management
│   ├── grades.html                # Grade Management
│   ├── reports.html               # Reports & Analytics
│   ├── css/
│   │   ├── style.css              # Global styles
│   │   ├── dashboard.css          # Dashboard styles
│   │   ├── students.css           # Student page styles
│   │   ├── courses.css            # Course page styles
│   │   ├── instructors.css        # Instructor page styles
│   │   ├── enrollments.css        # Enrollment page styles
│   │   ├── grades.css             # Grade page styles
│   │   └── reports.css            # Reports page styles
│   └── js/
│       ├── main.js                # Main utility functions
│       ├── dashboard.js           # Dashboard functionality
│       ├── students.js            # Student management
│       ├── courses.js             # Course management
│       ├── instructors.js         # Instructor management
│       ├── enrollments.js         # Enrollment management
│       ├── grades.js              # Grade management
│       └── reports.js             # Reports functionality
├── backend/
│   ├── src/com/college/
│   │   ├── CollegeManagementServer.java    # Main server
│   │   ├── models/
│   │   │   ├── Student.java
│   │   │   ├── Course.java
│   │   │   ├── Instructor.java
│   │   │   ├── Enrollment.java
│   │   │   └── Grade.java
│   │   ├── controllers/
│   │   │   └── StudentController.java
│   │   ├── services/
│   │   │   └── StudentService.java
│   │   ├── database/
│   │   │   ├── LocalDataStorage.java
│   │   │   └── MySQLConnection.java
│   │   └── utils/
│   │       ├── APIResponse.java
│   │       └── Constants.java
│   ├── config/
│   │   └── database.properties     # Database configuration
│   └── lib/
│       └── [Dependencies]
├── documentation/
│   ├── API_DOCUMENTATION.md
│   ├── SETUP_GUIDE.md
│   ├── MYSQL_SETUP.md
│   └── USER_GUIDE.md
└── README.md
```

## Quick Start

### Frontend Only (Without Backend)

1. Open `frontend/index.html` in your web browser
2. Data is stored in browser's local storage automatically
3. No backend server needed for basic functionality

### With Java Backend Server

#### Prerequisites
- Java 21 or higher
- Maven (for dependency management)

#### Setup & Run

1. **Compile the backend:**
   ```bash
   cd backend
   javac -d bin -cp "lib/*" src/com/college/**/*.java
   ```

2. **Run the server:**
   ```bash
   java -cp "bin:lib/*" com.college.CollegeManagementServer
   ```

3. **Server will start on:**
   ```
   http://localhost:8080
   API Endpoints: http://localhost:8080/api
   ```

4. Open `frontend/index.html` in your browser

### With MySQL Database

#### Prerequisites
- MySQL Server running locally
- MySQL JDBC Driver (included in lib/)

#### Setup

1. **Create database:**
   ```sql
   CREATE DATABASE college_management;
   ```

2. **Update database credentials in:**
   ```
   backend/src/com/college/database/MySQLConnection.java
   ```
   
   Update these lines:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/college_management";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "your_password"; // Your MySQL password
   ```

3. **Run the server:**
   ```bash
   java -cp "bin:lib/*" com.college.CollegeManagementServer
   ```

The tables will be created automatically on first run.

## API Endpoints

### Students
- `GET /api/students` - Get all students
- `POST /api/students` - Create new student
- `GET /api/students/{id}` - Get student by ID
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student
- `GET /api/students?search=term` - Search students

### System
- `GET /api/stats` - Get system statistics
- `GET /api/export` - Export all data as JSON
- `GET /api/ping` - Health check

## Data Format Examples

### Student
```json
{
  "id": "STU-1234567890",
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "dateOfBirth": "2000-01-15",
  "department": "Computer Science",
  "address": "123 Main St",
  "enrollmentDate": "2024-01-01"
}
```

### Course
```json
{
  "id": "CRS-1234567890",
  "name": "Introduction to Programming",
  "code": "CS101",
  "description": "Basic programming concepts",
  "credits": 3,
  "department": "Computer Science",
  "instructorId": "INS-1234567890",
  "capacity": 40,
  "createdDate": "2024-01-01"
}
```

## Grade Calculation

Overall Grade = (Midterm × 0.2) + (Final × 0.5) + (Assignment × 0.2) + (Attendance × 0.1)

Letter Grades:
- A: 90-100
- B: 80-89
- C: 70-79
- D: 60-69
- F: Below 60

## Features

- ✅ Responsive Design (Mobile, Tablet, Desktop)
- ✅ Data Validation
- ✅ Search & Filter
- ✅ Real-time Statistics
- ✅ Report Generation
- ✅ CSV Export
- ✅ User-friendly Interface
- ✅ CORS Support
- ✅ Error Handling
- ✅ Local Storage Persistence
- ✅ MySQL Integration Ready

## Browser Support

- Chrome/Chromium 80+
- Firefox 75+
- Safari 13+
- Edge 80+

## Technologies Used

### Frontend
- HTML5
- CSS3
- JavaScript ES6+
- Local Storage API

### Backend
- Java 21+
- HTTP Server (Built-in)
- RESTful API
- Jackson JSON Library

### Database (Optional)
- MySQL 5.7+
- JDBC

## Installation Instructions

1. **Unzip the file:**
   ```bash
   unzip college-management-system.zip
   cd college-management-system
   ```

2. **For Frontend Only:**
   - Open `frontend/index.html` directly in browser

3. **For Full Setup (Backend + Frontend):**
   - Compile and run the backend server
   - Open `frontend/index.html` in browser

## Configuration Files

### Backend Configuration
Edit `backend/src/com/college/utils/Constants.java` to modify:
- Server port
- Database settings
- API configuration

### Database Configuration
Edit `backend/src/com/college/database/MySQLConnection.java` to:
- Change MySQL host, port, database name
- Update credentials
- Modify connection pool settings

## Support & Documentation

See the `documentation/` folder for:
- `API_DOCUMENTATION.md` - Detailed API reference
- `SETUP_GUIDE.md` - Complete setup instructions
- `MYSQL_SETUP.md` - MySQL database setup guide
- `USER_GUIDE.md` - User manual

## Future Enhancements

- [ ] User authentication and authorization
- [ ] Advanced reporting features
- [ ] Notification system
- [ ] File upload for documents
- [ ] Integration with payment systems
- [ ] Mobile application
- [ ] Real-time notifications
- [ ] Advanced analytics

## Troubleshooting

**Q: Frontend works but backend won't connect?**
A: Make sure the server is running on http://localhost:8080 and check the browser console for errors.

**Q: MySQL connection fails?**
A: Verify MySQL is running, credentials are correct, and the database exists.

**Q: Data not persisting?**
A: Check browser local storage settings (privacy/incognito mode disables it).

## License

This project is provided as-is for educational and commercial use.

## Author

College Management System - 2024

For more information or questions, refer to the documentation folder.
