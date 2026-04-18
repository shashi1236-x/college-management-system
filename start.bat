@echo off
REM Start College Management System
REM This script starts the backend server and opens the frontend

echo ================================
echo College Management System
echo Starting Backend Server
echo ================================
echo.

REM Navigate to backend directory
cd "backend"

REM Call the backend start script
call start.bat

pause
echo Backend server should be running on http://localhost:8080
echo Frontend opened in browser.
echo.
echo Press any key to stop the server...
pause >nul

echo Stopping server...
taskkill /fi "WINDOWTITLE eq College Management Backend*" /t /f >nul 2>&1

echo Server stopped.
pause