@echo off
REM Run script for College Management System (Windows)

REM Path to JDK binaries (Java is assumed to be in PATH)
REM If not in PATH, uncomment and set JAVA_BIN below
REM set "JAVA_BIN=C:\Program Files\Java\jdk-21\bin"

echo ================================
echo College Management System
echo Server Startup (Windows)
echo ================================
echo.

REM Check if we're already in backend directory
if not exist bin (
    REM If bin doesn't exist here, try going to backend directory
    if exist backend\bin (
        cd backend
    ) else (
        echo ERROR: bin directory not found!
        echo Please run compile.bat first
        pause
        exit /b 1
    )
)

echo Starting server on http://localhost:8080
echo Press Ctrl+C to stop
echo.

java -cp "bin;lib/*" com.college.CollegeManagementServer

pause
