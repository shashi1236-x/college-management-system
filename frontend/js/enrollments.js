/* Enrollments JavaScript */

let currentEditingEnrollmentId = null;

function openAddEnrollmentForm() {
    currentEditingEnrollmentId = null;
    resetEnrollmentForm();
    document.getElementById('enrollmentId').value = generateId('ENR');
    loadEnrollmentSelects();
    document.getElementById('enrollmentDate').valueAsDate = new Date();
    document.getElementById('enrollmentFormModal').style.display = 'block';
}

function closeAddEnrollmentForm() {
    document.getElementById('enrollmentFormModal').style.display = 'none';
    currentEditingEnrollmentId = null;
}

function resetEnrollmentForm() {
    document.getElementById('enrollmentStudent').value = '';
    document.getElementById('enrollmentCourse').value = '';
    document.getElementById('enrollmentDate').valueAsDate = new Date();
    document.getElementById('enrollmentStatus').value = 'Active';
}

async function loadEnrollmentSelects() {
    try {
        // Load students
        const studentsResponse = await apiCall('/students');
        const students = studentsResponse.success ? studentsResponse.data : [];
        const studentSelect = document.getElementById('enrollmentStudent');
        studentSelect.innerHTML = '<option value="">Select a student</option>';
        students.forEach(student => {
            const option = document.createElement('option');
            option.value = student.id;
            option.textContent = student.name;
            studentSelect.appendChild(option);
        });

        // Load courses
        const coursesResponse = await apiCall('/courses');
        const courses = coursesResponse.success ? coursesResponse.data : [];
        const courseSelect = document.getElementById('enrollmentCourse');
        courseSelect.innerHTML = '<option value="">Select a course</option>';
        courses.forEach(course => {
            const option = document.createElement('option');
            option.value = course.id;
            option.textContent = course.name;
            courseSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading enrollment selects:', error);
        // Fallback to local storage
        const students = LocalStorage.get('students') || [];
        const studentSelect = document.getElementById('enrollmentStudent');
        studentSelect.innerHTML = '<option value="">Select a student</option>';
        students.forEach(student => {
            const option = document.createElement('option');
            option.value = student.id;
            option.textContent = student.name;
            studentSelect.appendChild(option);
        });

        const courses = LocalStorage.get('courses') || [];
        const courseSelect = document.getElementById('enrollmentCourse');
        courseSelect.innerHTML = '<option value="">Select a course</option>';
        courses.forEach(course => {
            const option = document.createElement('option');
            option.value = course.id;
            option.textContent = course.name;
            courseSelect.appendChild(option);
        });
    }
}

function submitEnrollment(event) {
    event.preventDefault();

    const enrollmentData = {
        id: document.getElementById('enrollmentId').value,
        studentId: document.getElementById('enrollmentStudent').value,
        courseId: document.getElementById('enrollmentCourse').value,
        enrollmentDate: document.getElementById('enrollmentDate').value,
        status: document.getElementById('enrollmentStatus').value
    };

    // Validation
    if (!enrollmentData.studentId || !enrollmentData.courseId || !enrollmentData.enrollmentDate) {
        showError('Please fill in all required fields');
        return;
    }

async function submitEnrollment(event) {
    event.preventDefault();

    const enrollmentData = {
        id: document.getElementById('enrollmentId').value,
        studentId: document.getElementById('enrollmentStudent').value,
        courseId: document.getElementById('enrollmentCourse').value,
        enrollmentDate: document.getElementById('enrollmentDate').value,
        status: document.getElementById('enrollmentStatus').value
    };

    // Validation
    if (!enrollmentData.studentId || !enrollmentData.courseId || !enrollmentData.enrollmentDate) {
        showError('Please fill in all required fields');
        return;
    }

    try {
        if (currentEditingEnrollmentId) {
            // Update existing enrollment
            const response = await apiCall(`/enrollments/${currentEditingEnrollmentId}`, 'PUT', enrollmentData);
            if (response.success) {
                showSuccess('Enrollment updated successfully');
            } else {
                showError(response.message || 'Failed to update enrollment');
                return;
            }
        } else {
            // Add new enrollment
            const response = await apiCall('/enrollments', 'POST', enrollmentData);
            if (response.success) {
                showSuccess('Enrollment added successfully');
            } else {
                showError(response.message || 'Failed to add enrollment');
                return;
            }
        }

        closeAddEnrollmentForm();
        await loadEnrollments();
    } catch (error) {
        console.error('Error saving enrollment:', error);
        showError('Failed to save enrollment. Please check if the backend server is running.');
    }
}

async function loadEnrollments(searchTerm = '') {
    try {
        let enrollmentsResponse = await apiCall('/enrollments');
        let studentsResponse = await apiCall('/students');
        let coursesResponse = await apiCall('/courses');

        let enrollments = enrollmentsResponse.success ? enrollmentsResponse.data : [];
        let students = studentsResponse.success ? studentsResponse.data : [];
        let courses = coursesResponse.success ? coursesResponse.data : [];

        if (searchTerm) {
            enrollments = enrollments.filter(e => {
                const student = students.find(s => s.id === e.studentId);
                const course = courses.find(c => c.id === e.courseId);
                const studentName = student ? student.name.toLowerCase() : '';
                const courseName = course ? course.name.toLowerCase() : '';
                const term = searchTerm.toLowerCase();
                return studentName.includes(term) || courseName.includes(term);
            });
        }

        // If API fails, fallback to local storage
        if (!enrollmentsResponse.success || !studentsResponse.success || !coursesResponse.success) {
            console.warn('API failed, using local storage fallback');
            enrollments = LocalStorage.get('enrollments') || [];
            students = LocalStorage.get('students') || [];
            courses = LocalStorage.get('courses') || [];
            if (searchTerm) {
                enrollments = enrollments.filter(e => {
                    const student = students.find(s => s.id === e.studentId);
                    const course = courses.find(c => c.id === e.courseId);
                    const studentName = student ? student.name.toLowerCase() : '';
                    const courseName = course ? course.name.toLowerCase() : '';
                    const term = searchTerm.toLowerCase();
                    return studentName.includes(term) || courseName.includes(term);
                });
            }
        }

        const tableBody = document.getElementById('enrollmentsTable');
        tableBody.innerHTML = '';

        if (enrollments.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="6" class="no-data">No enrollments found. Click "Add New Enrollment" to get started.</td></tr>';
            return;
        }

        enrollments.forEach(enrollment => {
            const student = students.find(s => s.id === enrollment.studentId);
            const course = courses.find(c => c.id === enrollment.courseId);
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${enrollment.id}</td>
                <td>${student ? student.name : 'Unknown Student'}</td>
                <td>${course ? course.name : 'Unknown Course'}</td>
                <td>${formatDisplayDate(enrollment.enrollmentDate)}</td>
                <td><span class="status status-${enrollment.status.toLowerCase()}">${enrollment.status}</span></td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editEnrollment('${enrollment.id}')">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteEnrollment('${enrollment.id}')">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading enrollments:', error);
        showError('Failed to load enrollments. Please check if the backend server is running.');

        // Fallback to local storage
        let enrollments = LocalStorage.get('enrollments') || [];
        const students = LocalStorage.get('students') || [];
        const courses = LocalStorage.get('courses') || [];

        if (searchTerm) {
            enrollments = enrollments.filter(e => {
                const student = students.find(s => s.id === e.studentId);
                const course = courses.find(c => c.id === e.courseId);
                const studentName = student ? student.name.toLowerCase() : '';
                const courseName = course ? course.name.toLowerCase() : '';
                const term = searchTerm.toLowerCase();
                return studentName.includes(term) || courseName.includes(term);
            });
        }

        const tableBody = document.getElementById('enrollmentsTable');
        tableBody.innerHTML = '';

        if (enrollments.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="6" class="no-data">No enrollments found. Click "Add New Enrollment" to get started.</td></tr>';
            return;
        }

        enrollments.forEach(enrollment => {
            const student = students.find(s => s.id === enrollment.studentId);
            const course = courses.find(c => c.id === enrollment.courseId);
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${enrollment.id}</td>
                <td>${student ? student.name : 'Unknown Student'}</td>
                <td>${course ? course.name : 'Unknown Course'}</td>
                <td>${formatDisplayDate(enrollment.enrollmentDate)}</td>
                <td><span class="status status-${enrollment.status.toLowerCase()}">${enrollment.status}</span></td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editEnrollment('${enrollment.id}')">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteEnrollment('${enrollment.id}')">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    }
}

async function editEnrollment(enrollmentId) {
    try {
        const response = await apiCall(`/enrollments/${enrollmentId}`);
        if (response.success && response.data) {
            const enrollment = response.data;
            currentEditingEnrollmentId = enrollmentId;
            await loadEnrollmentSelects();

            document.getElementById('enrollmentId').value = enrollment.id;
            document.getElementById('enrollmentStudent').value = enrollment.studentId;
            document.getElementById('enrollmentCourse').value = enrollment.courseId;
            document.getElementById('enrollmentDate').value = enrollment.enrollmentDate;
            document.getElementById('enrollmentStatus').value = enrollment.status;

            document.getElementById('enrollmentFormModal').style.display = 'block';
        } else {
            showError('Enrollment not found');
        }
    } catch (error) {
        console.error('Error loading enrollment:', error);
        showError('Failed to load enrollment data. Please check if the backend server is running.');

        // Fallback to local storage
        const enrollments = LocalStorage.get('enrollments') || [];
        const enrollment = enrollments.find(e => e.id === enrollmentId);

        if (!enrollment) {
            showError('Enrollment not found');
            return;
        }

        currentEditingEnrollmentId = enrollmentId;
        loadEnrollmentSelects();

        document.getElementById('enrollmentId').value = enrollment.id;
        document.getElementById('enrollmentStudent').value = enrollment.studentId;
        document.getElementById('enrollmentCourse').value = enrollment.courseId;
        document.getElementById('enrollmentDate').value = enrollment.enrollmentDate;
        document.getElementById('enrollmentStatus').value = enrollment.status;

        document.getElementById('enrollmentFormModal').style.display = 'block';
    }
}

async function deleteEnrollment(enrollmentId) {
    if (confirm('Are you sure you want to delete this enrollment?')) {
        try {
            const response = await apiCall(`/enrollments/${enrollmentId}`, 'DELETE');
            if (response.success) {
                showSuccess('Enrollment deleted successfully');
                await loadEnrollments();
            } else {
                showError(response.message || 'Failed to delete enrollment');
            }
        } catch (error) {
            console.error('Error deleting enrollment:', error);
            showError('Failed to delete enrollment. Please check if the backend server is running.');

            // Fallback to local storage
            let enrollments = LocalStorage.get('enrollments') || [];
            enrollments = enrollments.filter(e => e.id !== enrollmentId);
            LocalStorage.set('enrollments', enrollments);

            // Also remove associated grades
            let grades = LocalStorage.get('grades') || [];
            grades = grades.filter(g => g.enrollmentId !== enrollmentId);
            LocalStorage.set('grades', grades);

            showSuccess('Enrollment deleted successfully');
            loadEnrollments();
        }
    }
}

// Setup
document.addEventListener('DOMContentLoaded', async function() {
    await loadEnrollments();

    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keyup', async (e) => {
            await loadEnrollments(e.target.value);
        });
    }

    window.onclick = function(event) {
        const modal = document.getElementById('enrollmentFormModal');
        if (event.target === modal) {
            closeAddEnrollmentForm();
        }
    };
});
