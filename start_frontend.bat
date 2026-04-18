@echo off
REM Quick start script for frontend only (with local storage fallback)
REM Opens the frontend directly - backend not required

echo ================================
echo College Management System
echo Frontend Only Mode
echo ================================
echo.

echo Opening frontend in browser...
echo Note: Using local storage data. Backend server not started.
echo.

start "" "frontend\index.html"

echo Frontend opened. Sample data is available in browser local storage.
pause