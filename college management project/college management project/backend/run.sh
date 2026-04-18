#!/bin/bash
# Run script for College Management System (macOS/Linux)

echo "================================"
echo "College Management System"
echo "Server Startup (macOS/Linux)"
echo "================================"
echo ""

cd backend

if [ ! -d "bin" ]; then
    echo "ERROR: bin directory not found!"
    echo "Please run compile.sh first"
    exit 1
fi

echo "Starting server on http://localhost:8080"
echo "Press Ctrl+C to stop"
echo ""

java -cp "bin:lib/*" com.college.CollegeManagementServer
