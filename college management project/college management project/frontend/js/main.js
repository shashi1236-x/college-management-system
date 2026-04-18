/* Main JavaScript for College Management System */

// API Configuration
const API_BASE_URL = "http://127.0.0.1:8080/api";

// Default timeout for fetch requests (increased to 15 seconds for slower connections)
const FETCH_TIMEOUT = 15000;

// Backend status
let backendRunning = false;
let backendCheckInterval = null;
const BACKEND_CHECK_MS = 5000;

// Check if backend is running
async function checkBackendStatus() {
    try {
        const response = await fetch(`${API_BASE_URL}/ping`, {
            method: 'GET',
            signal: AbortSignal.timeout(2000) // Quick check
        });
        backendRunning = response.ok;
    } catch (error) {
        backendRunning = false;
    }

    if (backendRunning) {
        hideWarning();
    } else {
        showBackendWarning();
    }
    return backendRunning;
}

// Start periodic backend availability checks
function startBackendHealthPolling() {
    checkBackendStatus();

    if (backendCheckInterval) {
        clearInterval(backendCheckInterval);
    }

    backendCheckInterval = setInterval(async () => {
        const wasRunning = backendRunning;
        const isRunning = await checkBackendStatus();
        if (!wasRunning && isRunning) {
            showSuccess('Backend detected. Reloading page to connect to API...');
            setTimeout(() => {
                window.location.reload();
            }, 1000);
        }
    }, BACKEND_CHECK_MS);
}

// Disabled backend warning completely
function showBackendWarning() {
    return; // do nothing
}

function hideWarning() {
    return; // do nothing
}

function startBackend() {
    return; // do nothing
}

// Utility function for API calls
async function apiCall(endpoint, method = 'GET', data = null) {
    try {
        const config = {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        };

        if (data && (method === 'POST' || method === 'PUT')) {
            config.body = JSON.stringify(data);
        }

        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), FETCH_TIMEOUT);

        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            ...config,
            signal: controller.signal
        });

        clearTimeout(timeoutId);

        if (!response.ok) {
            if (response.status === 404) {
                throw new Error(`Endpoint not found: ${endpoint}`);
            } else if (response.status === 500) {
                throw new Error('Server error. Please check if the backend is running.');
            } else {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
        }

        return await response.json();
    } catch (error) {
        console.error('API Call Error:', error);
        backendRunning = false;
        showBackendWarning();
        
        // Check if it's a network error (backend not running)
        if (error.name === 'AbortError') {
            console.error('Request timeout - Backend may not be running');
            throw new Error('Backend server is not responding. Make sure it is running on http://127.0.0.1:8080/api');
        }
        
        throw error;
    }
}

// Success notification
function showSuccess(message) {
    const notification = createNotification(message, 'success');
    document.body.appendChild(notification);
    setTimeout(() => notification.remove(), 3000);
}

// Error notification
function showError(message) {
    const notification = createNotification(message, 'error');
    document.body.appendChild(notification);
    setTimeout(() => notification.remove(), 3000);
}

// Helper to create notifications
function createNotification(message, type) {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        background-color: ${type === 'success' ? '#27ae60' : '#e74c3c'};
        color: white;
        border-radius: 5px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.2);
        z-index: 9999;
        animation: slideIn 0.3s ease;
    `;
    return notification;
}

// Generate unique ID
function generateId(prefix) {
    return `${prefix}-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
}

// Format date to YYYY-MM-DD
function formatDate(date) {
    if (!date) return '';
    const d = new Date(date);
    const month = `${d.getMonth() + 1}`.padStart(2, '0');
    const day = `${d.getDate()}`.padStart(2, '0');
    const year = d.getFullYear();
    return `${year}-${month}-${day}`;
}

// Format date for display
function formatDisplayDate(date) {
    if (!date) return '';
    const d = new Date(date);
    return d.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
}

// Parse date from input
function parseDate(dateString) {
    if (!dateString) return null;
    const date = new Date(dateString);
    return date;
}

// Local Storage Management
const LocalStorage = {
    get: (key) => {
        try {
            const item = localStorage.getItem(key);
            return item ? JSON.parse(item) : null;
        } catch (error) {
            console.error('LocalStorage Get Error:', error);
            return null;
        }
    },

    set: (key, value) => {
        try {
            localStorage.setItem(key, JSON.stringify(value));
            return true;
        } catch (error) {
            console.error('LocalStorage Set Error:', error);
            return false;
        }
    },

    remove: (key) => {
        try {
            localStorage.removeItem(key);
            return true;
        } catch (error) {
            console.error('LocalStorage Remove Error:', error);
            return false;
        }
    },

    clear: () => {
        try {
            localStorage.clear();
            return true;
        } catch (error) {
            console.error('LocalStorage Clear Error:', error);
            return false;
        }
    }
};

// Initialize local storage with sample data if empty
function initializeLocalStorage() {
    if (!LocalStorage.get('instructors') || LocalStorage.get('instructors').length === 0) {
        LocalStorage.set('instructors', [
            {
                id: 'INS001',
                name: 'Dr. Robert Johnson',
                email: 'robert.johnson@university.edu',
                phone: '123-456-7800',
                department: 'Computer Science',
                qualification: 'PhD',
                experience: 15,
                specialization: 'Software Engineering',
                hireDate: '2010-08-15'
            },
            {
                id: 'INS002',
                name: 'Prof. Sarah Wilson',
                email: 'sarah.wilson@university.edu',
                phone: '123-456-7801',
                department: 'Mathematics',
                qualification: 'PhD',
                experience: 12,
                specialization: 'Applied Mathematics',
                hireDate: '2012-01-10'
            },
            {
                id: 'INS003',
                name: 'Dr. Michael Chen',
                email: 'michael.chen@university.edu',
                phone: '123-456-7802',
                department: 'Physics',
                qualification: 'PhD',
                experience: 10,
                specialization: 'Quantum Physics',
                hireDate: '2014-09-01'
            },
            {
                id: 'INS004',
                name: 'Prof. Emily Davis',
                email: 'emily.davis@university.edu',
                phone: '123-456-7803',
                department: 'Chemistry',
                qualification: 'PhD',
                experience: 8,
                specialization: 'Organic Chemistry',
                hireDate: '2016-01-15'
            },
            {
                id: 'INS005',
                name: 'Dr. James Brown',
                email: 'james.brown@university.edu',
                phone: '123-456-7804',
                department: 'Biology',
                qualification: 'PhD',
                experience: 14,
                specialization: 'Molecular Biology',
                hireDate: '2010-03-20'
            }
        ]);
    }

    if (!LocalStorage.get('students') || LocalStorage.get('students').length === 0) {
        LocalStorage.set('students', [
            {
                id: 'STU001',
                name: 'John Doe',
                email: 'john.doe@email.com',
                phone: '123-456-7890',
                dateOfBirth: '2000-01-15',
                department: 'Computer Science',
                address: '123 Main St, City, State 12345',
                enrollmentDate: '2023-09-01'
            },
            {
                id: 'STU002',
                name: 'Jane Smith',
                email: 'jane.smith@email.com',
                phone: '123-456-7891',
                dateOfBirth: '1999-05-20',
                department: 'Mathematics',
                address: '456 Oak Ave, City, State 12346',
                enrollmentDate: '2023-09-01'
            },
            {
                id: 'STU003',
                name: 'Alice Johnson',
                email: 'alice.johnson@email.com',
                phone: '123-456-7892',
                dateOfBirth: '2001-03-10',
                department: 'Physics',
                address: '789 Pine St, City, State 12347',
                enrollmentDate: '2023-09-01'
            },
            {
                id: 'STU004',
                name: 'Bob Wilson',
                email: 'bob.wilson@email.com',
                phone: '123-456-7893',
                dateOfBirth: '2000-07-25',
                department: 'Chemistry',
                address: '321 Elm St, City, State 12348',
                enrollmentDate: '2023-09-01'
            },
            {
                id: 'STU005',
                name: 'Carol Brown',
                email: 'carol.brown@email.com',
                phone: '123-456-7894',
                dateOfBirth: '1999-11-30',
                department: 'Biology',
                address: '654 Maple Ave, City, State 12349',
                enrollmentDate: '2023-09-01'
            },
            {
                id: 'STU006',
                name: 'David Lee',
                email: 'david.lee@email.com',
                phone: '123-456-7895',
                dateOfBirth: '2000-09-12',
                department: 'Computer Science',
                address: '987 Cedar St, City, State 12350',
                enrollmentDate: '2023-09-01'
            },
            {
                id: 'STU007',
                name: 'Eva Garcia',
                email: 'eva.garcia@email.com',
                phone: '123-456-7896',
                dateOfBirth: '2001-02-28',
                department: 'Mathematics',
                address: '147 Birch Ln, City, State 12351',
                enrollmentDate: '2023-09-01'
            },
            {
                id: 'STU008',
                name: 'Frank Miller',
                email: 'frank.miller@email.com',
                phone: '123-456-7897',
                dateOfBirth: '2000-12-05',
                department: 'Physics',
                address: '258 Spruce St, City, State 12352',
                enrollmentDate: '2023-09-01'
            },
            {
                id: 'STU009',
                name: 'Grace Taylor',
                email: 'grace.taylor@email.com',
                phone: '123-456-7898',
                dateOfBirth: '1999-08-18',
                department: 'Chemistry',
                address: '369 Willow Ave, City, State 12353',
                enrollmentDate: '2023-09-01'
            },
            {
                id: 'STU010',
                name: 'Henry Anderson',
                email: 'henry.anderson@email.com',
                phone: '123-456-7899',
                dateOfBirth: '2001-06-22',
                department: 'Biology',
                address: '741 Oak Ln, City, State 12354',
                enrollmentDate: '2023-09-01'
            }
        ]);
    }

    if (!LocalStorage.get('courses') || LocalStorage.get('courses').length === 0) {
        LocalStorage.set('courses', [
            {
                id: 'CRS001',
                name: 'Introduction to Programming',
                code: 'CS101',
                description: 'Basic programming concepts using Java',
                credits: 3,
                department: 'Computer Science',
                instructorId: 'INS001',
                capacity: 30,
                createdDate: '2023-09-01'
            },
            {
                id: 'CRS002',
                name: 'Data Structures and Algorithms',
                code: 'CS201',
                description: 'Advanced data structures and algorithm design',
                credits: 4,
                department: 'Computer Science',
                instructorId: 'INS001',
                capacity: 25,
                createdDate: '2023-09-01'
            },
            {
                id: 'CRS003',
                name: 'Calculus I',
                code: 'MATH101',
                description: 'Fundamental concepts of calculus',
                credits: 4,
                department: 'Mathematics',
                instructorId: 'INS002',
                capacity: 35,
                createdDate: '2023-09-01'
            },
            {
                id: 'CRS004',
                name: 'Linear Algebra',
                code: 'MATH201',
                description: 'Matrix theory and linear transformations',
                credits: 3,
                department: 'Mathematics',
                instructorId: 'INS002',
                capacity: 30,
                createdDate: '2023-09-01'
            },
            {
                id: 'CRS005',
                name: 'Classical Mechanics',
                code: 'PHYS101',
                description: 'Newtonian mechanics and kinematics',
                credits: 4,
                department: 'Physics',
                instructorId: 'INS003',
                capacity: 28,
                createdDate: '2023-09-01'
            },
            {
                id: 'CRS006',
                name: 'Quantum Physics',
                code: 'PHYS301',
                description: 'Introduction to quantum mechanics',
                credits: 4,
                department: 'Physics',
                instructorId: 'INS003',
                capacity: 20,
                createdDate: '2023-09-01'
            },
            {
                id: 'CRS007',
                name: 'Organic Chemistry',
                code: 'CHEM201',
                description: 'Structure and reactions of organic compounds',
                credits: 4,
                department: 'Chemistry',
                instructorId: 'INS004',
                capacity: 25,
                createdDate: '2023-09-01'
            },
            {
                id: 'CRS008',
                name: 'Biochemistry',
                code: 'BIO201',
                description: 'Chemical processes in living organisms',
                credits: 3,
                department: 'Biology',
                instructorId: 'INS005',
                capacity: 30,
                createdDate: '2023-09-01'
            },
            {
                id: 'CRS009',
                name: 'Database Systems',
                code: 'CS301',
                description: 'Relational databases and SQL',
                credits: 3,
                department: 'Computer Science',
                instructorId: 'INS001',
                capacity: 25,
                createdDate: '2023-09-01'
            },
            {
                id: 'CRS010',
                name: 'Web Development',
                code: 'CS401',
                description: 'Frontend and backend web technologies',
                credits: 3,
                department: 'Computer Science',
                instructorId: 'INS001',
                capacity: 30,
                createdDate: '2023-09-01'
            }
        ]);
    }

    if (!LocalStorage.get('enrollments') || LocalStorage.get('enrollments').length === 0) {
        LocalStorage.set('enrollments', [
            {
                id: 'ENR001',
                studentId: 'STU001',
                courseId: 'CRS001',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR002',
                studentId: 'STU001',
                courseId: 'CRS003',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR003',
                studentId: 'STU002',
                courseId: 'CRS003',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR004',
                studentId: 'STU002',
                courseId: 'CRS004',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR005',
                studentId: 'STU003',
                courseId: 'CRS005',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR006',
                studentId: 'STU004',
                courseId: 'CRS007',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR007',
                studentId: 'STU005',
                courseId: 'CRS008',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR008',
                studentId: 'STU006',
                courseId: 'CRS001',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR009',
                studentId: 'STU006',
                courseId: 'CRS002',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR010',
                studentId: 'STU007',
                courseId: 'CRS003',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR011',
                studentId: 'STU008',
                courseId: 'CRS005',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR012',
                studentId: 'STU009',
                courseId: 'CRS007',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR013',
                studentId: 'STU010',
                courseId: 'CRS008',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR014',
                studentId: 'STU001',
                courseId: 'CRS009',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            },
            {
                id: 'ENR015',
                studentId: 'STU006',
                courseId: 'CRS010',
                enrollmentDate: '2023-09-01',
                status: 'Active'
            }
        ]);
    }

    if (!LocalStorage.get('grades') || LocalStorage.get('grades').length === 0) {
        LocalStorage.set('grades', [
            {
                id: 'GRD001',
                enrollmentId: 'ENR001',
                midterm: 85.0,
                final_: 90.0,
                assignment: 88.0,
                attendance: 95.0,
                overallGrade: 87.5,
                createdDate: '2023-12-15'
            },
            {
                id: 'GRD002',
                enrollmentId: 'ENR002',
                midterm: 78.0,
                final_: 82.0,
                assignment: 85.0,
                attendance: 90.0,
                overallGrade: 81.0,
                createdDate: '2023-12-15'
            },
            {
                id: 'GRD003',
                enrollmentId: 'ENR003',
                midterm: 92.0,
                final_: 88.0,
                assignment: 90.0,
                attendance: 95.0,
                overallGrade: 90.5,
                createdDate: '2023-12-15'
            },
            {
                id: 'GRD004',
                enrollmentId: 'ENR004',
                midterm: 75.0,
                final_: 80.0,
                assignment: 78.0,
                attendance: 85.0,
                overallGrade: 78.0,
                createdDate: '2023-12-15'
            },
            {
                id: 'GRD005',
                enrollmentId: 'ENR005',
                midterm: 88.0,
                final_: 85.0,
                assignment: 87.0,
                attendance: 92.0,
                overallGrade: 86.5,
                createdDate: '2023-12-15'
            },
            {
                id: 'GRD006',
                enrollmentId: 'ENR006',
                midterm: 82.0,
                final_: 87.0,
                assignment: 84.0,
                attendance: 88.0,
                overallGrade: 84.0,
                createdDate: '2023-12-15'
            },
            {
                id: 'GRD007',
                enrollmentId: 'ENR007',
                midterm: 90.0,
                final_: 92.0,
                assignment: 89.0,
                attendance: 95.0,
                overallGrade: 91.0,
                createdDate: '2023-12-15'
            },
            {
                id: 'GRD008',
                enrollmentId: 'ENR008',
                midterm: 76.0,
                final_: 79.0,
                assignment: 80.0,
                attendance: 90.0,
                overallGrade: 78.5,
                createdDate: '2023-12-15'
            },
            {
                id: 'GRD009',
                enrollmentId: 'ENR009',
                midterm: 89.0,
                final_: 91.0,
                assignment: 88.0,
                attendance: 93.0,
                overallGrade: 89.5,
                createdDate: '2023-12-15'
            },
            {
                id: 'GRD010',
                enrollmentId: 'ENR010',
                midterm: 83.0,
                final_: 86.0,
                assignment: 85.0,
                attendance: 87.0,
                overallGrade: 84.5,
                createdDate: '2023-12-15'
            }
        ]);
    }
}

// Logout function
function logout() {
    if (confirm('Are you sure you want to logout?')) {
        LocalStorage.clear();
        alert('Logged out successfully!');
        // In a real application, redirect to login page
        // window.location.href = 'login.html';
    }
}

// Export data to CSV
function exportToCSV(data, filename) {
    if (!data || data.length === 0) {
        showError('No data to export');
        return;
    }

    const keys = Object.keys(data[0]);
    let csv = keys.join(',') + '\n';

    data.forEach(row => {
        const values = keys.map(key => {
            const value = row[key];
            // Escape quotes and wrap in quotes if contains comma
            return typeof value === 'string' && value.includes(',')
                ? `"${value.replace(/"/g, '""')}"`
                : value;
        });
        csv += values.join(',') + '\n';
    });

    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
}

// Validate email
function isValidEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

// Validate phone
function isValidPhone(phone) {
    const re = /^[0-9\-\+\(\)\s]{10,}$/;
    return re.test(phone);
}

// Search in array
function searchArray(array, searchTerm, searchFields) {
    if (!searchTerm) return array;
    const term = searchTerm.toLowerCase();
    return array.filter(item =>
        searchFields.some(field =>
            String(item[field] || '').toLowerCase().includes(term)
        )
    );
}

// Sort array
function sortArray(array, key, order = 'asc') {
    return [...array].sort((a, b) => {
        const aVal = a[key];
        const bVal = b[key];

        if (aVal < bVal) return order === 'asc' ? -1 : 1;
        if (aVal > bVal) return order === 'asc' ? 1 : -1;
        return 0;
    });
}

// Calculate grade from scores
function calculateOverallGrade(midterm, final, assignment, attendance) {
    // Weights: Midterm 20%, Final 50%, Assignment 20%, Attendance 10%
    const overall = (midterm * 0.2) + (final * 0.5) + (assignment * 0.2) + (attendance * 0.1);
    return Math.round(overall);
}

// Get letter grade
function getLetterGrade(score) {
    if (score >= 90) return 'A';
    if (score >= 80) return 'B';
    if (score >= 70) return 'C';
    if (score >= 60) return 'D';
    return 'F';
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    initializeLocalStorage();
    startBackendHealthPolling();
    console.log('College Management System Initialized');
});