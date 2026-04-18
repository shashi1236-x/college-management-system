@echo off
REM Compile script for College Management System (Windows)
REM This script compiles the Java backend

echo ================================
echo College Management System
echo Build Script (Windows)
echo ================================
echo.

REM Path to JDK binaries (Java is assumed to be in PATH)
REM If not in PATH, uncomment and set JAVA_BIN below
REM set "JAVA_BIN=C:\Program Files\Java\jdk-21\bin"

REM Check if we're in the backend directory
if not exist src (
    echo ERROR: src directory not found!
    echo Please run this script from the backend directory.
    pause
    exit /b 1
)

echo Compiling Java source files...
echo.

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Compile all Java files at once
javac -d bin -cp "lib/*" src\com\college\*.java src\com\college\controllers\*.java src\com\college\database\*.java src\com\college\models\*.java src\com\college\services\*.java src\com\college\utils\*.java

if errorlevel 1 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo ================================
echo Compilation successful!
echo ================================
echo.
echo To run the server, execute:
echo   run.bat
echo.
pause
