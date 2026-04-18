/* Dashboard JavaScript */

async function loadDashboardData() {
    try {
        // Fetch data from API
        const [studentsResponse, coursesResponse, instructorsResponse, enrollmentsResponse] = await Promise.all([
            apiCall('/students'),
            apiCall('/courses'),
            apiCall('/instructors'),
            apiCall('/enrollments')
        ]);

        // Extract data from API responses
        const students = studentsResponse.success ? studentsResponse.data : [];
        const courses = coursesResponse.success ? coursesResponse.data : [];
        const instructors = instructorsResponse.success ? instructorsResponse.data : [];
        const enrollments = enrollmentsResponse.success ? enrollmentsResponse.data : [];

        // Update stat cards
        document.getElementById('totalStudents').textContent = students.length;
        document.getElementById('totalCourses').textContent = courses.length;
        document.getElementById('totalInstructors').textContent = instructors.length;
        document.getElementById('activeEnrollments').textContent = enrollments.filter(e => e.status === 'Active').length;

    } catch (error) {
        console.error('Error loading dashboard data:', error);
        showError('Failed to load dashboard data. Please check if the backend server is running.');

        // Fallback to local storage if API fails
        const students = LocalStorage.get('students') || [];
        const courses = LocalStorage.get('courses') || [];
        const instructors = LocalStorage.get('instructors') || [];
        const enrollments = LocalStorage.get('enrollments') || [];

        document.getElementById('totalStudents').textContent = students.length;
        document.getElementById('totalCourses').textContent = courses.length;
        document.getElementById('totalInstructors').textContent = instructors.length;
        document.getElementById('activeEnrollments').textContent = enrollments.filter(e => e.status === 'Active').length;
    }
}

// Load data when page loads
document.addEventListener('DOMContentLoaded', loadDashboardData);
