/* Instructors JavaScript */

let currentEditingInstructorId = null;

function openAddInstructorForm() {
    currentEditingInstructorId = null;
    resetInstructorForm();
    document.getElementById('instructorId').value = generateId('INS');
    document.getElementById('instructorFormModal').style.display = 'block';
}

function closeAddInstructorForm() {
    document.getElementById('instructorFormModal').style.display = 'none';
    currentEditingInstructorId = null;
}

function resetInstructorForm() {
    document.getElementById('instructorName').value = '';
    document.getElementById('instructorEmail').value = '';
    document.getElementById('instructorPhone').value = '';
    document.getElementById('instructorDepartment').value = '';
    document.getElementById('instructorQualification').value = '';
    document.getElementById('instructorExperience').value = '';
    document.getElementById('instructorSpecialization').value = '';
}

function submitInstructor(event) {
    event.preventDefault();

    const instructorData = {
        id: document.getElementById('instructorId').value,
        name: document.getElementById('instructorName').value,
        email: document.getElementById('instructorEmail').value,
        phone: document.getElementById('instructorPhone').value,
        department: document.getElementById('instructorDepartment').value,
        qualification: document.getElementById('instructorQualification').value,
        experience: document.getElementById('instructorExperience').value,
        specialization: document.getElementById('instructorSpecialization').value,
        hireDate: new Date().toISOString().split('T')[0]
    };

    // Validation
    if (!instructorData.name || !instructorData.email || !instructorData.phone || !instructorData.department) {
        showError('Please fill in all required fields');
        return;
    }

    if (!isValidEmail(instructorData.email)) {
        showError('Invalid email address');
        return;
    }

    if (!isValidPhone(instructorData.phone)) {
        showError('Invalid phone number');
        return;
    }

async function submitInstructor(event) {
    event.preventDefault();

    const instructorData = {
        id: document.getElementById('instructorId').value,
        name: document.getElementById('instructorName').value,
        email: document.getElementById('instructorEmail').value,
        phone: document.getElementById('instructorPhone').value,
        department: document.getElementById('instructorDepartment').value,
        qualification: document.getElementById('instructorQualification').value,
        experience: document.getElementById('instructorExperience').value,
        specialization: document.getElementById('instructorSpecialization').value,
        hireDate: new Date().toISOString().split('T')[0]
    };

    // Validation
    if (!instructorData.name || !instructorData.email || !instructorData.phone || !instructorData.department) {
        showError('Please fill in all required fields');
        return;
    }

    if (!isValidEmail(instructorData.email)) {
        showError('Invalid email address');
        return;
    }

    if (!isValidPhone(instructorData.phone)) {
        showError('Invalid phone number');
        return;
    }

    try {
        if (currentEditingInstructorId) {
            // Update existing instructor
            const response = await apiCall(`/instructors/${currentEditingInstructorId}`, 'PUT', instructorData);
            if (response.success) {
                showSuccess('Instructor updated successfully');
            } else {
                showError(response.message || 'Failed to update instructor');
                return;
            }
        } else {
            // Add new instructor
            const response = await apiCall('/instructors', 'POST', instructorData);
            if (response.success) {
                showSuccess('Instructor added successfully');
            } else {
                showError(response.message || 'Failed to add instructor');
                return;
            }
        }

        closeAddInstructorForm();
        await loadInstructors();
    } catch (error) {
        console.error('Error saving instructor:', error);
        showError('Failed to save instructor. Please check if the backend server is running.');
    }
}
}

async function loadInstructors(searchTerm = '') {
    try {
        let endpoint = '/instructors';
        if (searchTerm) {
            endpoint += `?search=${encodeURIComponent(searchTerm)}`;
        }

        const response = await apiCall(endpoint);
        let instructors = response.success ? response.data : [];

        // If API fails, fallback to local storage
        if (!response.success) {
            console.warn('API failed, using local storage fallback');
            instructors = LocalStorage.get('instructors') || [];
            if (searchTerm) {
                instructors = searchArray(instructors, searchTerm, ['name', 'email', 'phone', 'department']);
            }
        }

        const tableBody = document.getElementById('instructorsTable');
        tableBody.innerHTML = '';

        if (instructors.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="8" class="no-data">No instructors found. Click "Add New Instructor" to get started.</td></tr>';
            return;
        }

        instructors.forEach(instructor => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${instructor.id}</td>
                <td>${instructor.name}</td>
                <td>${instructor.email}</td>
                <td>${instructor.phone}</td>
                <td>${instructor.department}</td>
                <td>${instructor.qualification || '-'}</td>
                <td>${instructor.experience || '-'}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editInstructor('${instructor.id}')">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteInstructor('${instructor.id}')">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading instructors:', error);
        showError('Failed to load instructors. Please check if the backend server is running.');

        // Fallback to local storage
        let instructors = LocalStorage.get('instructors') || [];
        if (searchTerm) {
            instructors = searchArray(instructors, searchTerm, ['name', 'email', 'phone', 'department']);
        }

        const tableBody = document.getElementById('instructorsTable');
        tableBody.innerHTML = '';

        if (instructors.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="8" class="no-data">No instructors found. Click "Add New Instructor" to get started.</td></tr>';
            return;
        }

        instructors.forEach(instructor => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${instructor.id}</td>
                <td>${instructor.name}</td>
                <td>${instructor.email}</td>
                <td>${instructor.phone}</td>
                <td>${instructor.department}</td>
                <td>${instructor.qualification || '-'}</td>
                <td>${instructor.experience || '-'}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editInstructor('${instructor.id}')">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteInstructor('${instructor.id}')">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    }
}

async function editInstructor(instructorId) {
    try {
        const response = await apiCall(`/instructors/${instructorId}`);
        if (response.success && response.data) {
            const instructor = response.data;
            currentEditingInstructorId = instructorId;
            document.getElementById('instructorId').value = instructor.id;
            document.getElementById('instructorName').value = instructor.name;
            document.getElementById('instructorEmail').value = instructor.email;
            document.getElementById('instructorPhone').value = instructor.phone;
            document.getElementById('instructorDepartment').value = instructor.department;
            document.getElementById('instructorQualification').value = instructor.qualification || '';
            document.getElementById('instructorExperience').value = instructor.experience || '';
            document.getElementById('instructorSpecialization').value = instructor.specialization || '';

            document.getElementById('instructorFormModal').style.display = 'block';
        } else {
            showError('Instructor not found');
        }
    } catch (error) {
        console.error('Error loading instructor:', error);
        showError('Failed to load instructor data. Please check if the backend server is running.');

        // Fallback to local storage
        const instructors = LocalStorage.get('instructors') || [];
        const instructor = instructors.find(i => i.id === instructorId);

        if (!instructor) {
            showError('Instructor not found');
            return;
        }

        currentEditingInstructorId = instructorId;
        document.getElementById('instructorId').value = instructor.id;
        document.getElementById('instructorName').value = instructor.name;
        document.getElementById('instructorEmail').value = instructor.email;
        document.getElementById('instructorPhone').value = instructor.phone;
        document.getElementById('instructorDepartment').value = instructor.department;
        document.getElementById('instructorQualification').value = instructor.qualification || '';
        document.getElementById('instructorExperience').value = instructor.experience || '';
        document.getElementById('instructorSpecialization').value = instructor.specialization || '';

        document.getElementById('instructorFormModal').style.display = 'block';
    }
}

async function deleteInstructor(instructorId) {
    if (confirm('Are you sure you want to delete this instructor?')) {
        try {
            const response = await apiCall(`/instructors/${instructorId}`, 'DELETE');
            if (response.success) {
                showSuccess('Instructor deleted successfully');
                await loadInstructors();
            } else {
                showError(response.message || 'Failed to delete instructor');
            }
        } catch (error) {
            console.error('Error deleting instructor:', error);
            showError('Failed to delete instructor. Please check if the backend server is running.');

            // Fallback to local storage
            let instructors = LocalStorage.get('instructors') || [];
            instructors = instructors.filter(i => i.id !== instructorId);
            LocalStorage.set('instructors', instructors);

            // Also remove from courses
            let courses = LocalStorage.get('courses') || [];
            courses.forEach(course => {
                if (course.instructorId === instructorId) {
                    course.instructorId = null;
                }
            });
            LocalStorage.set('courses', courses);

            showSuccess('Instructor deleted successfully');
            loadInstructors();
        }
    }
}

// Setup
document.addEventListener('DOMContentLoaded', async function() {
    await loadInstructors();

    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keyup', async (e) => {
            await loadInstructors(e.target.value);
        });
    }

    window.onclick = function(event) {
        const modal = document.getElementById('instructorFormModal');
        if (event.target === modal) {
            closeAddInstructorForm();
        }
    };
});
