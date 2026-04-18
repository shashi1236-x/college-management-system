# Setup Guide

## System Requirements

- Windows, macOS, or Linux
- Java 21 or higher
- Web browser (Chrome, Firefox, Safari, Edge)
- MySQL 5.7+ (optional, for database persistence)

## Option 1: Frontend Only (Recommended for Learning)

### Quick Start

1. **Unzip the project:**
   ```
   college-management-system/
   ```

2. **Open in browser:**
   - Navigate to `frontend/index.html`
   - Double-click or drag to browser
   - Or use: `file:///path/to/frontend/index.html`

3. **Start using:**
   - Register students, courses, instructors
   - Manage enrollments and grades
   - View reports

**Data is stored in your browser's local storage automatically.**

### Advantages
- No server setup needed
- Works offline (mostly)
- Instant start
- Perfect for learning

### Limitations
- Data lost if browser cache cleared
- Single browser only (not shared)
- No backend API

---

## Option 2: Frontend + Java Backend (Recommended for Full Feature Set)

### Prerequisites

#### Windows
1. Download Java 21+ from oracle.com
2. Install Java Development Kit (JDK)
3. Add to PATH: `C:\Program Files\Java\jdk-21\bin`

#### macOS
```bash
brew install openjdk@21
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt-get install openjdk-21-jdk
```

### Installation Steps

1. **Unzip the project:**
   ```bash
   unzip college-management-system.zip
   cd college-management-system
   ```

2. **Compile Backend:**
   ```bash
   cd backend
   
   # Windows
   dir /S *.java > sources.txt
   javac -d bin @sources.txt
   
   # macOS/Linux
   find src -name "*.java" > sources.txt
   javac -d bin $(cat sources.txt)
   ```

3. **Start Backend Server:**
   ```bash
   # Windows
   java -cp "bin" com.college.CollegeManagementServer
   
   # macOS/Linux
   java -cp "bin:lib/*" com.college.CollegeManagementServer
   ```

   You should see:
   ```
   ================================
   College Management System - Server
   ================================
   Server started successfully on http://localhost:8080
   ```

4. **Open Frontend:**
   - Open `frontend/index.html` in browser
   - Or navigate to: `file:///path/to/frontend/index.html`

5. **Verify Connection:**
   - Open browser console (F12)
   - Should see no connection errors
   - Make API calls to verify

### Testing the Backend

Open new terminal/command prompt:
```bash
# Test if server is running
curl http://localhost:8080/api/ping

# Should return:
# {"success":true,"message":"Pong",...}
```

---

## Option 3: Frontend + Backend + MySQL Database

### Additional Prerequisites

#### Windows
1. Download MySQL Server from mysql.com
2. Install MySQL Server
3. Remember your password
4. MySQL runs on `localhost:3306`

#### macOS
```bash
brew install mysql
brew services start mysql
mysql -u root
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt-get install mysql-server
sudo mysql_secure_installation
```

### Setup Steps

1. **Create Database:**
   ```bash
   mysql -u root -p
   
   CREATE DATABASE college_management;
   USE college_management;
   ```

2. **Update Database Credentials:**
   - Edit: `backend/src/com/college/database/MySQLConnection.java`
   
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/college_management";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "your_password";  // Your MySQL password
   ```

3. **Enable MySQL Mode:**
   - Edit: `backend/src/com/college/utils/Constants.java`
   
   ```java
   public static final boolean USE_LOCAL_STORAGE = false;
   public static final boolean USE_MYSQL = true;
   ```

4. **Recompile Backend:**
   ```bash
   cd backend
   javac -d bin -cp "lib/*" src/com/college/**/*.java
   ```

5. **Start Backend:**
   ```bash
   java -cp "bin:lib/*" com.college.CollegeManagementServer
   ```

   Should show:
   ```
   MySQL Connection established successfully
   All tables created/verified successfully
   ```

6. **Open Frontend:**
   - Open `frontend/index.html` in browser

### Verify MySQL Connection

In new terminal:
```bash
mysql -u root -p -D college_management

SHOW TABLES;
SELECT COUNT(*) FROM students;
```

---

## Troubleshooting

### Issue: "Port 8080 already in use"
**Solution:** Change port in `Constants.java`:
```java
public static final int SERVER_PORT = 8081;  // Use different port
```

### Issue: "CORS error in browser console"
**Solution:** 
- Ensure backend is running on `http://localhost:8080`
- Check frontend is accessing correct API URL
- Clear browser cache and reload

### Issue: "MySQL connection refused"
**Solutions:**
- Check MySQL is running: `mysql -u root -p`
- Verify credentials in MySQLConnection.java
- Check port (default 3306): `SHOW GLOBAL VARIABLES LIKE 'port';`
- Create database: `CREATE DATABASE college_management;`

### Issue: "Java command not found"
**Solution:** Install Java and add to PATH:
- Windows: Set JAVA_HOME environment variable
- Linux/Mac: Install via package manager

### Issue: "Frontend can't find backend"
**Solution:**
1. Ensure backend is running: `java -cp ... com.college.CollegeManagementServer`
2. Check server is on http://localhost:8080
3. Check frontend API_BASE_URL in `js/main.js`
4. Look at browser console for actual error

### Issue: "Data not saving"
**Solutions:**
- Frontend only: Check browser allows local storage
- With backend: Verify server is running
- MySQL: Check credentials and database exists
- Clear browser cache and retry

---

## Directory Structure After Setup

```
college-management-system/
├── frontend/                    # Frontend files (HTML, CSS, JS)
├── backend/                     # Java source code
│   ├── src/                     # Source files
│   ├── bin/                     # Compiled classes (after compile)
│   ├── lib/                     # Dependencies
│   └── config/                  # Configuration files
├── documentation/               # Setup guides and API docs
└── README.md                    # Main documentation
```

---

## File Management

### Frontend Files Location
- `frontend/index.html` - Main dashboard
- `frontend/students.html` - Student management
- `frontend/css/style.css` - Main styles
- `frontend/js/main.js` - Main JavaScript utilities

### Backend Files Location
- `backend/src/com/college/models/` - Data models (Student, Course, etc.)
- `backend/src/com/college/controllers/` - API controllers
- `backend/src/com/college/services/` - Business logic
- `backend/src/com/college/database/` - Database handlers

### Configuration Files
- `backend/src/com/college/utils/Constants.java` - Global constants
- `backend/src/com/college/database/MySQLConnection.java` - Database config

---

## Common Commands

### Start Frontend Only
```bash
# On Windows, macOS, or Linux
# Simply open frontend/index.html in web browser
```

### Start Backend Server
```bash
cd backend

# Windows
java -cp "bin" com.college.CollegeManagementServer

# macOS/Linux
java -cp "bin:lib/*" com.college.CollegeManagementServer
```

### Compile Backend
```bash
cd backend

# Windows
javac -d bin -cp "lib/*" src/com/college/**/*.java

# macOS/Linux
find src -name "*.java" | xargs javac -d bin -cp "lib/*"
```

### Check Java Version
```bash
java -version
```

### Export Data
- Use browser console
- Or use `/api/export` endpoint
- Or use MySQL export tools

---

## Next Steps

1. **Read User Guide:** See `documentation/USER_GUIDE.md`
2. **API Documentation:** See `documentation/API_DOCUMENTATION.md`
3. **MySQL Setup:** See `documentation/MYSQL_SETUP.md` (if using database)
4. **Explore Code:** Check backend source for customization

---

## Support

For issues:
1. Check troubleshooting section above
2. Review browser console (F12)
3. Check backend console output
4. Verify all prerequisites are installed
5. Review relevant documentation

---

## Customization

### Change Server Port
Edit `Constants.java`:
```java
public static final int SERVER_PORT = 9000;  // Use your preferred port
```

### Change Database
Edit `MySQLConnection.java`:
```java
private static final String DB_URL = "jdbc:mysql://your-host:3306/your-db";
private static final String DB_USER = "your-user";
private static final String DB_PASSWORD = "your-password";
```

### Modify Styling
Edit CSS files in `frontend/css/`:
- `style.css` - Main styles
- Individual module CSS files

### Add New Features
Extend backend classes:
- Add new models in `models/`
- Create services in `services/`
- Create controllers in `controllers/`

For detailed instructions, see project documentation.

---

Ready to get started? Pick your option above and follow the steps!
