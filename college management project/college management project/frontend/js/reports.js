/* Reports JavaScript */

function generateReport() {
    const reportType = document.getElementById('reportType').value;
    const department = document.getElementById('reportDepartment').value;

    let reportContent = '';

    switch (reportType) {
        case 'student-performance':
            reportContent = generateStudentPerformanceReport(department);
            break;
        case 'course-analytics':
            reportContent = generateCourseAnalyticsReport(department);
            break;
        case 'enrollment-summary':
            reportContent = generateEnrollmentSummaryReport(department);
            break;
        case 'instructor-workload':
            reportContent = generateInstructorWorkloadReport(department);
            break;
        default:
            reportContent = '<p>Select a report type to generate.</p>';
    }

    document.getElementById('reportContent').innerHTML = reportContent;
    updateQuickStatistics();
}

function generateStudentPerformanceReport(department = '') {
    const students = LocalStorage.get('students') || [];
    const enrollments = LocalStorage.get('enrollments') || [];
    const grades = LocalStorage.get('grades') || [];
    const courses = LocalStorage.get('courses') || [];

    let filteredStudents = students;
    if (department) {
        filteredStudents = students.filter(s => s.department === department);
    }

    let html = '<h3>Student Performance Report</h3>';
    html += department ? `<p>Department: ${department}</p>` : '<p>All Departments</p>';
    html += '<table style="width: 100%; border-collapse: collapse;">';
    html += '<thead><tr style="background-color: #2c3e50; color: white;">';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Student</th>';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Courses</th>';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Average Grade</th>';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Status</th>';
    html += '</tr></thead><tbody>';

    filteredStudents.forEach(student => {
        const studentEnrollments = enrollments.filter(e => e.studentId === student.id);
        const studentGrades = grades.filter(g => 
            studentEnrollments.some(e => e.id === g.enrollmentId)
        );

        const avgGrade = studentGrades.length > 0 
            ? Math.round(studentGrades.reduce((sum, g) => sum + g.overallGrade, 0) / studentGrades.length)
            : 0;

        const status = avgGrade >= 60 ? 'Passing' : 'Failing';
        const statusColor = status === 'Passing' ? '#27ae60' : '#e74c3c';

        html += `<tr style="border: 1px solid #ddd;">`;
        html += `<td style="padding: 10px;">${student.name}</td>`;
        html += `<td style="padding: 10px;">${studentEnrollments.length}</td>`;
        html += `<td style="padding: 10px;">${avgGrade}</td>`;
        html += `<td style="padding: 10px; color: white; background-color: ${statusColor};">${status}</td>`;
        html += `</tr>`;
    });

    html += '</tbody></table>';
    return html;
}

function generateCourseAnalyticsReport(department = '') {
    const courses = LocalStorage.get('courses') || [];
    const instructors = LocalStorage.get('instructors') || [];
    const enrollments = LocalStorage.get('enrollments') || [];

    let filteredCourses = courses;
    if (department) {
        filteredCourses = courses.filter(c => c.department === department);
    }

    let html = '<h3>Course Analytics Report</h3>';
    html += department ? `<p>Department: ${department}</p>` : '<p>All Departments</p>';
    html += '<table style="width: 100%; border-collapse: collapse;">';
    html += '<thead><tr style="background-color: #2c3e50; color: white;">';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Course</th>';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Code</th>';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Instructor</th>';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Enrolled</th>';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Capacity</th>';
    html += '</tr></thead><tbody>';

    filteredCourses.forEach(course => {
        const instructor = instructors.find(i => i.id === course.instructorId);
        const enrolled = enrollments.filter(e => e.courseId === course.id && e.status === 'Active').length;

        html += `<tr style="border: 1px solid #ddd;">`;
        html += `<td style="padding: 10px;">${course.name}</td>`;
        html += `<td style="padding: 10px;">${course.code}</td>`;
        html += `<td style="padding: 10px;">${instructor ? instructor.name : '-'}</td>`;
        html += `<td style="padding: 10px;">${enrolled}</td>`;
        html += `<td style="padding: 10px;">${course.capacity || '-'}</td>`;
        html += `</tr>`;
    });

    html += '</tbody></table>';
    return html;
}

function generateEnrollmentSummaryReport(department = '') {
    const enrollments = LocalStorage.get('enrollments') || [];
    const students = LocalStorage.get('students') || [];
    const courses = LocalStorage.get('courses') || [];

    let filteredEnrollments = enrollments;
    if (department) {
        filteredEnrollments = enrollments.filter(e => {
            const student = students.find(s => s.id === e.studentId);
            return student && student.department === department;
        });
    }

    const active = filteredEnrollments.filter(e => e.status === 'Active').length;
    const completed = filteredEnrollments.filter(e => e.status === 'Completed').length;
    const dropped = filteredEnrollments.filter(e => e.status === 'Dropped').length;

    let html = '<h3>Enrollment Summary Report</h3>';
    html += department ? `<p>Department: ${department}</p>` : '<p>All Departments</p>';
    html += '<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-top: 20px;">';
    
    html += '<div style="padding: 20px; background-color: #3498db; color: white; border-radius: 5px;">';
    html += `<h4>Active Enrollments</h4><p style="font-size: 24px; margin: 0;">${active}</p></div>`;
    
    html += '<div style="padding: 20px; background-color: #27ae60; color: white; border-radius: 5px;">';
    html += `<h4>Completed</h4><p style="font-size: 24px; margin: 0;">${completed}</p></div>`;
    
    html += '<div style="padding: 20px; background-color: #e74c3c; color: white; border-radius: 5px;">';
    html += `<h4>Dropped</h4><p style="font-size: 24px; margin: 0;">${dropped}</p></div>`;
    
    html += '</div>';
    
    return html;
}

function generateInstructorWorkloadReport(department = '') {
    const instructors = LocalStorage.get('instructors') || [];
    const courses = LocalStorage.get('courses') || [];
    const enrollments = LocalStorage.get('enrollments') || [];

    let filteredInstructors = instructors;
    if (department) {
        filteredInstructors = instructors.filter(i => i.department === department);
    }

    let html = '<h3>Instructor Workload Report</h3>';
    html += department ? `<p>Department: ${department}</p>` : '<p>All Departments</p>';
    html += '<table style="width: 100%; border-collapse: collapse;">';
    html += '<thead><tr style="background-color: #2c3e50; color: white;">';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Instructor</th>';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Department</th>';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Courses Teaching</th>';
    html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Total Students</th>';
    html += '</tr></thead><tbody>';

    filteredInstructors.forEach(instructor => {
        const instructorCourses = courses.filter(c => c.instructorId === instructor.id);
        const totalStudents = instructorCourses.reduce((sum, course) => {
            const courseEnrollments = enrollments.filter(e => e.courseId === course.id && e.status === 'Active');
            return sum + courseEnrollments.length;
        }, 0);

        html += `<tr style="border: 1px solid #ddd;">`;
        html += `<td style="padding: 10px;">${instructor.name}</td>`;
        html += `<td style="padding: 10px;">${instructor.department}</td>`;
        html += `<td style="padding: 10px;">${instructorCourses.length}</td>`;
        html += `<td style="padding: 10px;">${totalStudents}</td>`;
        html += `</tr>`;
    });

    html += '</tbody></table>';
    return html;
}

function updateQuickStatistics() {
    const enrollments = LocalStorage.get('enrollments') || [];
    const grades = LocalStorage.get('grades') || [];
    const courses = LocalStorage.get('courses') || [];

    // Average Grade
    const avgGrade = grades.length > 0 
        ? Math.round(grades.reduce((sum, g) => sum + g.overallGrade, 0) / grades.length)
        : 0;

    // Total Enrollments
    const totalEnrollments = enrollments.length;

    // Active Courses
    const activeCourses = courses.length;

    // Completion Rate
    const completedEnrollments = enrollments.filter(e => e.status === 'Completed').length;
    const completionRate = enrollments.length > 0
        ? Math.round((completedEnrollments / enrollments.length) * 100)
        : 0;

    document.getElementById('avgStudentGrade').textContent = avgGrade;
    document.getElementById('totalEnrollmentsCount').textContent = totalEnrollments;
    document.getElementById('activeCoursesCount').textContent = activeCourses;
    document.getElementById('completionRate').textContent = completionRate + '%';
}

function loadDepartmentFilter() {
    const students = LocalStorage.get('students') || [];
    const departments = [...new Set(students.map(s => s.department).filter(d => d))];

    const select = document.getElementById('reportDepartment');
    select.innerHTML = '<option value="">All Departments</option>';

    departments.forEach(department => {
        const option = document.createElement('option');
        option.value = department;
        option.textContent = department;
        select.appendChild(option);
    });
}

function updateReportOptions() {
    // Could be expanded for more dynamic filtering
}

function exportReport() {
    const reportContent = document.getElementById('reportContent').innerHTML;
    if (reportContent === '<p>Select a report type to generate.</p>') {
        showError('Please generate a report first');
        return;
    }

    const element = document.createElement('div');
    element.innerHTML = reportContent;
    
    const fileName = `report-${new Date().toISOString().split('T')[0]}.html`;
    const blob = new Blob([element.outerHTML], { type: 'text/html' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);

    showSuccess('Report exported successfully');
}

// Setup
document.addEventListener('DOMContentLoaded', function() {
    loadDepartmentFilter();
    updateQuickStatistics();
});
