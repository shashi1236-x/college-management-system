#!/bin/bash
# Compile script for College Management System (macOS/Linux)
# This script compiles the Java backend

echo "================================"
echo "College Management System"
echo "Build Script (macOS/Linux)"
echo "================================"
echo ""

cd backend

echo "Compiling Java source files..."
echo ""

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files
find src -name "*.java" -print0 | xargs -0 javac -d bin -cp "lib/*" 2>&1

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Compilation failed!"
    exit 1
fi

echo ""
echo "Compilation successful!"
echo ""
echo "To run the server, execute:"
echo "  java -cp \"bin:lib/*\" com.college.CollegeManagementServer"
echo ""
