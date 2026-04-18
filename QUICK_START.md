# INSTALLATION & QUICK START

## What's Included

✅ Complete Frontend (HTML, CSS, JavaScript)
✅ Complete Java Backend 
✅ Database Configuration (Local & MySQL)
✅ API SERVER (Running on Port 8080)
✅ Documentation & Setup Guides
✅ Build & Run Scripts

---

## QUICK START (Easiest Way)

### Just Want to Try It?

1. **Double-click** `frontend/index.html`
2. Start exploring!
3. Data saves automatically in browser

**No installation required!** ✨

---

## WITH BACKEND SERVER

### Windows Users

1. **Compile:** Double-click `backend/compile.bat`
2. **Run:** Double-click `backend/run.bat`
3. **Open:** Open `frontend/index.html` in browser

### macOS/Linux Users

```bash
cd backend
chmod +x compile.sh run.sh

./compile.sh              # First time only
./run.sh                  # Start server
```

Then open `frontend/index.html` in browser

---

## WITH MYSQL DATABASE

### Setup Database

1. Install MySQL: https://dev.mysql.com/downloads/
2. Run MySQL
3. Open terminal and create database:
   ```sql
   CREATE DATABASE college_management;
   ```

4. Edit: `backend/src/com/college/database/MySQLConnection.java`
   - Update URL, username, password with YOUR credentials

5. Edit: `backend/src/com/college/utils/Constants.java`
   ```java
   USE_LOCAL_STORAGE = false;  // Turn off
   USE_MYSQL = true;           // Turn on
   ```

6. Recompile and run backend

---

## FOLDER STRUCTURE

```
📁 college-management-system/
├── 📁 frontend/              ← Open index.html here!
│   ├── *.html               (Dashboard, Students, Courses, etc.)
│   ├── css/                 (Styling)
│   └── js/                  (JavaScript logic)
│
├── 📁 backend/              ← Compile & run from here
│   ├── src/                 (Java source code)
│   ├── lib/                 (Libraries)
│   ├── compile.bat/sh       (Compile script)
│   ├── run.bat/sh           (Run script)
│   └── pom.xml              (Maven config)
│
├── 📁 documentation/        (Setup guides, API docs)
│   └── *.md                 (Markdown files)
│
├── README.md                (Main documentation)
└── QUICK_START.md           (This file!)
```

---

## FEATURES

✨ Student Management
✨ Course Management
✨ Instructor Management
✨ Enrollment Management
✨ Grade Tracking
✨ Reports & Analytics
✨ Search & Filter
✨ Responsive Design
✨ Local Storage Persistence
✨ REST API
✨ MySQL Ready

---

## TROUBLESHOOTING

### "Opening HTML doesn't work"
→ Right-click → Open with Browser

### "Backend won't start"
→ Make sure Java is installed: `java -version`

### "Port 8080 in use"
→ Change port in `Constants.java` (line: SERVER_PORT)

### "MySQL connection error"
→ Check MySQL running & credentials in `MySQLConnection.java`

### "Data not saving"
→ Check browser allows local storage (not incognito mode)

---

## NEXT STEPS

1. **Read:** `documentation/SETUP_GUIDE.md` (detailed setup)
2. **Learn:** `documentation/USER_GUIDE.md` (how to use)
3. **API:** `documentation/API_DOCUMENTATION.md` (for developers)
4. **DB:** `documentation/MYSQL_SETUP.md` (database setup)

---

## FILE ROLES

### Frontend Files
- `index.html` - Main dashboard
- `students.html` - Add/manage students
- `courses.html` - Add/manage courses
- `instructors.html` - Add/manage instructors
- `enrollments.html` - Manage enrollments
- `grades.html` - Manage grades
- `reports.html` - View reports
- `css/*.css` - All styling
- `js/*.js` - All functionality

### Backend Files
- `CollegeManagementServer.java` - Main server
- `models/*.java` - Data structures
- `services/*.java` - Business logic
- `controllers/*.java` - API endpoints
- `database/*.java` - Database handlers
- `utils/*.java` - Helper functions

---

## DATA STORAGE

### Frontend Only
- Local browser storage
- Lost if cache cleared
- Per-browser only

### With Backend
- RAM (default)
- Lost on server restart

### With MySQL
- Permanent storage
- Shared across users
- Survives restarts

---

## DEFAULT CREDENTIALS

None required by default!

- Frontend: No login
- Backend API: No authentication
- MySQL: Use your own credentials

---

## IMPORTANT NOTES

⚠️ This is a learning/development version
⚠️ For production: Add authentication, validation, SSL
⚠️ Backup your data regularly
⚠️ Change MySQL password in production
⚠️ Disable CORS for production

---

## SYSTEM REQUIREMENTS

✓ Any modern web browser
✓ Java 21+ (for backend)
✓ MySQL 5.7+ (optional)
✓ 50MB free disk space

---

## SUPPORT

1. Check troubleshooting above
2. Read documentation folder
3. Check browser console (F12) for errors
4. Verify all prerequisites installed

---

## GETTING HELP

- **Setup Issue?** → Read `documentation/SETUP_GUIDE.md`
- **How to Use?** → Read `documentation/USER_GUIDE.md`
- **API Questions?** → Read `documentation/API_DOCUMENTATION.md`
- **MySQL Help?** → Read `documentation/MYSQL_SETUP.md`

---

## TIPS

💡 Start with frontend only (no setup needed)
💡 Use Firefox DevTools to inspect data
💡 Check browser console for errors
💡 Restart server if issues occur
💡 Clear browser cache & reload
💡 Use MySQL for team projects

---

## ENJOY! 🎉

You now have a fully functional college management system!

Start by:
1. Opening `frontend/index.html`
2. Adding a few students and courses
3. Exploring the dashboard & reports
4. Trying the backend server
5. Connecting to MySQL (optional)

Questions? Check the documentation folder!

---

**Happy Learning! 📚**

Version 1.0
College Management System 2024
