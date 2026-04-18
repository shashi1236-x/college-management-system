/* Students JavaScript */

let currentEditingStudentId = null;

function openAddStudentForm() {
    currentEditingStudentId = null;
    const formElement = document.getElementById('studentForm');
    if (formElement && typeof formElement.reset === 'function') {
        formElement.reset();
    } else {
        resetStudentForm();
    }
    document.getElementById('studentId').value = generateId('STU');
    document.getElementById('studentFormModal').style.display = 'block';
}

function closeAddStudentForm() {
    document.getElementById('studentFormModal').style.display = 'none';
    currentEditingStudentId = null;
}

function resetStudentForm() {
    document.getElementById('studentName').value = '';
    document.getElementById('studentEmail').value = '';
    document.getElementById('studentPhone').value = '';
    document.getElementById('studentDOB').value = '';
    document.getElementById('studentDepartment').value = '';
    document.getElementById('studentAddress').value = '';
}

async function submitStudent(event) {
    event.preventDefault();

    const studentData = {
        id: document.getElementById('studentId').value,
        name: document.getElementById('studentName').value,
        email: document.getElementById('studentEmail').value,
        phone: document.getElementById('studentPhone').value,
        dateOfBirth: document.getElementById('studentDOB').value,
        department: document.getElementById('studentDepartment').value,
        address: document.getElementById('studentAddress').value,
        enrollmentDate: new Date().toISOString().split('T')[0]
    };

    // Validation
    if (!studentData.name || !studentData.email || !studentData.phone) {
        showError('Please fill in all required fields');
        return;
    }

    if (!isValidEmail(studentData.email)) {
        showError('Invalid email address');
        return;
    }

    if (!isValidPhone(studentData.phone)) {
        showError('Invalid phone number');
        return;
    }

    try {
        if (currentEditingStudentId) {
            // Update existing student
            const response = await apiCall(`/students/${currentEditingStudentId}`, 'PUT', studentData);
            if (response.success) {
                showSuccess('Student updated successfully');
            } else {
                showError(response.message || 'Failed to update student');
                return;
            }
        } else {
            // Add new student
            const response = await apiCall('/students', 'POST', studentData);
            if (response.success) {
                showSuccess('Student added successfully');
            } else {
                showError(response.message || 'Failed to add student');
                return;
            }
        }

        closeAddStudentForm();
        await loadStudents();
    } catch (error) {
        console.error('Error saving student:', error);
        showError('Failed to save student: ' + (error.message || 'Unknown error'));
    }
}

async function loadStudents(searchTerm = '') {
    try {
        let endpoint = '/students';
        if (searchTerm) {
            endpoint += `?search=${encodeURIComponent(searchTerm)}`;
        }

        const response = await apiCall(endpoint);
        let students = response.success ? response.data : [];

        // If API fails, fallback to local storage
        if (!response.success) {
            console.warn('API failed, using local storage fallback');
            students = LocalStorage.get('students') || [];
            if (searchTerm) {
                students = searchArray(students, searchTerm, ['name', 'email', 'phone', 'department']);
            }
        }

        const tableBody = document.getElementById('studentsTable');
        tableBody.innerHTML = '';

        if (students.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="6" class="no-data">No students found. Click "Add New Student" to get started.</td></tr>';
            return;
        }

        students.forEach(student => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${student.id}</td>
                <td>${student.name}</td>
                <td>${student.email}</td>
                <td>${student.phone}</td>
                <td>${student.department || '-'}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editStudent('${student.id}')">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteStudent('${student.id}')">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading students:', error);
        showError('Failed to load students. Please check if the backend server is running.');

        // Fallback to local storage
        let students = LocalStorage.get('students') || [];
        if (searchTerm) {
            students = searchArray(students, searchTerm, ['name', 'email', 'phone', 'department']);
        }

        const tableBody = document.getElementById('studentsTable');
        tableBody.innerHTML = '';

        if (students.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="6" class="no-data">No students found. Click "Add New Student" to get started.</td></tr>';
            return;
        }

        students.forEach(student => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${student.id}</td>
                <td>${student.name}</td>
                <td>${student.email}</td>
                <td>${student.phone}</td>
                <td>${student.department || '-'}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editStudent('${student.id}')">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteStudent('${student.id}')">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    }
}

async function editStudent(studentId) {
    try {
        const response = await apiCall(`/students/${studentId}`);
        if (response.success && response.data) {
            const student = response.data;
            currentEditingStudentId = studentId;
            document.getElementById('studentId').value = student.id;
            document.getElementById('studentName').value = student.name;
            document.getElementById('studentEmail').value = student.email;
            document.getElementById('studentPhone').value = student.phone;
            document.getElementById('studentDOB').value = student.dateOfBirth || '';
            document.getElementById('studentDepartment').value = student.department || '';
            document.getElementById('studentAddress').value = student.address || '';

            document.getElementById('studentFormModal').style.display = 'block';
        } else {
            showError('Student not found');
        }
    } catch (error) {
        console.error('Error loading student:', error);
        showError('Failed to load student data. Please check if the backend server is running.');

        // Fallback to local storage
        const students = LocalStorage.get('students') || [];
        const student = students.find(s => s.id === studentId);

        if (!student) {
            showError('Student not found');
            return;
        }

        currentEditingStudentId = studentId;
        document.getElementById('studentId').value = student.id;
        document.getElementById('studentName').value = student.name;
        document.getElementById('studentEmail').value = student.email;
        document.getElementById('studentPhone').value = student.phone;
        document.getElementById('studentDOB').value = student.dateOfBirth || '';
        document.getElementById('studentDepartment').value = student.department || '';
        document.getElementById('studentAddress').value = student.address || '';

        document.getElementById('studentFormModal').style.display = 'block';
    }
}

async function deleteStudent(studentId) {
    if (confirm('Are you sure you want to delete this student?')) {
        try {
            const response = await apiCall(`/students/${studentId}`, 'DELETE');
            if (response.success) {
                showSuccess('Student deleted successfully');
                await loadStudents();
            } else {
                showError(response.message || 'Failed to delete student');
            }
        } catch (error) {
            console.error('Error deleting student:', error);
            showError('Failed to delete student. Please check if the backend server is running.');

            // Fallback to local storage
            let students = LocalStorage.get('students') || [];
            students = students.filter(s => s.id !== studentId);
            LocalStorage.set('students', students);

            // Also remove associated enrollments
            let enrollments = LocalStorage.get('enrollments') || [];
            enrollments = enrollments.filter(e => e.studentId !== studentId);
            LocalStorage.set('enrollments', enrollments);

            showSuccess('Student deleted successfully');
            loadStudents();
        }
    }
}

// Setup search
document.addEventListener('DOMContentLoaded', function() {
    loadStudents();

    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keyup', (e) => {
            loadStudents(e.target.value);
        });
    }

    // Close modal when clicking outside
    window.onclick = function(event) {
        const modal = document.getElementById('studentFormModal');
        if (event.target === modal) {
            closeAddStudentForm();
        }
    };
});
