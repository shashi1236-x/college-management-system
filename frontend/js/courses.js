/* Courses JavaScript */

let currentEditingCourseId = null;

function openAddCourseForm() {
    currentEditingCourseId = null;
    resetCourseForm();
    document.getElementById('courseId').value = generateId('CRS');
    loadInstructorSelect();
    document.getElementById('courseFormModal').style.display = 'block';
}

function closeAddCourseForm() {
    document.getElementById('courseFormModal').style.display = 'none';
    currentEditingCourseId = null;
}

function resetCourseForm() {
    document.getElementById('courseName').value = '';
    document.getElementById('courseCode').value = '';
    document.getElementById('courseDescription').value = '';
    document.getElementById('courseCredits').value = '';
    document.getElementById('courseDepartment').value = '';
    document.getElementById('courseInstructor').value = '';
    document.getElementById('courseCapacity').value = '';
}

async function loadInstructorSelect() {
    try {
        const response = await apiCall('/instructors');
        const instructors = response.success ? response.data : [];

        const select = document.getElementById('courseInstructor');
        select.innerHTML = '<option value="">Select an instructor</option>';

        instructors.forEach(instructor => {
            const option = document.createElement('option');
            option.value = instructor.id;
            option.textContent = instructor.name;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading instructors:', error);
        // Fallback to local storage
        const instructors = LocalStorage.get('instructors') || [];
        const select = document.getElementById('courseInstructor');
        select.innerHTML = '<option value="">Select an instructor</option>';

        instructors.forEach(instructor => {
            const option = document.createElement('option');
            option.value = instructor.id;
            option.textContent = instructor.name;
            select.appendChild(option);
        });
    }
}

function submitCourse(event) {
    event.preventDefault();

    const courseData = {
        id: document.getElementById('courseId').value,
        name: document.getElementById('courseName').value,
        code: document.getElementById('courseCode').value,
        description: document.getElementById('courseDescription').value,
        credits: document.getElementById('courseCredits').value,
        department: document.getElementById('courseDepartment').value,
        instructorId: document.getElementById('courseInstructor').value,
        capacity: document.getElementById('courseCapacity').value,
        createdDate: new Date().toISOString().split('T')[0]
    };

    // Validation
    if (!courseData.name || !courseData.code) {
        showError('Please fill in all required fields');
        return;
    }

async function submitCourse(event) {
    event.preventDefault();

    const courseData = {
        id: document.getElementById('courseId').value,
        name: document.getElementById('courseName').value,
        code: document.getElementById('courseCode').value,
        description: document.getElementById('courseDescription').value,
        credits: document.getElementById('courseCredits').value,
        department: document.getElementById('courseDepartment').value,
        instructorId: document.getElementById('courseInstructor').value,
        capacity: document.getElementById('courseCapacity').value,
        createdDate: new Date().toISOString().split('T')[0]
    };

    // Validation
    if (!courseData.name || !courseData.code) {
        showError('Please fill in all required fields');
        return;
    }

    try {
        if (currentEditingCourseId) {
            // Update existing course
            const response = await apiCall(`/courses/${currentEditingCourseId}`, 'PUT', courseData);
            if (response.success) {
                showSuccess('Course updated successfully');
            } else {
                showError(response.message || 'Failed to update course');
                return;
            }
        } else {
            // Add new course
            const response = await apiCall('/courses', 'POST', courseData);
            if (response.success) {
                showSuccess('Course added successfully');
            } else {
                showError(response.message || 'Failed to add course');
                return;
            }
        }

        closeAddCourseForm();
        await loadCourses();
    } catch (error) {
        console.error('Error saving course:', error);
        showError('Failed to save course. Please check if the backend server is running.');
    }
}
}

async function loadCourses(searchTerm = '') {
    try {
        let coursesResponse = await apiCall('/courses');
        let instructorsResponse = await apiCall('/instructors');

        let courses = coursesResponse.success ? coursesResponse.data : [];
        let instructors = instructorsResponse.success ? instructorsResponse.data : [];

        if (searchTerm) {
            courses = searchArray(courses, searchTerm, ['name', 'code', 'department']);
        }

        // If API fails, fallback to local storage
        if (!coursesResponse.success || !instructorsResponse.success) {
            console.warn('API failed, using local storage fallback');
            courses = LocalStorage.get('courses') || [];
            instructors = LocalStorage.get('instructors') || [];
            if (searchTerm) {
                courses = searchArray(courses, searchTerm, ['name', 'code', 'department']);
            }
        }

        const tableBody = document.getElementById('coursesTable');
        tableBody.innerHTML = '';

        if (courses.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="8" class="no-data">No courses found. Click "Add New Course" to get started.</td></tr>';
            return;
        }

        courses.forEach(course => {
            const instructor = instructors.find(i => i.id === course.instructorId);
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${course.id}</td>
                <td>${course.name}</td>
                <td>${course.code}</td>
                <td>${course.credits || '-'}</td>
                <td>${course.department || '-'}</td>
                <td>${instructor ? instructor.name : '-'}</td>
                <td>${course.capacity || '-'}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editCourse('${course.id}')">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteCourse('${course.id}')">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading courses:', error);
        showError('Failed to load courses. Please check if the backend server is running.');

        // Fallback to local storage
        let courses = LocalStorage.get('courses') || [];
        const instructors = LocalStorage.get('instructors') || [];

        if (searchTerm) {
            courses = searchArray(courses, searchTerm, ['name', 'code', 'department']);
        }

        const tableBody = document.getElementById('coursesTable');
        tableBody.innerHTML = '';

        if (courses.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="8" class="no-data">No courses found. Click "Add New Course" to get started.</td></tr>';
            return;
        }

        courses.forEach(course => {
            const instructor = instructors.find(i => i.id === course.instructorId);
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${course.id}</td>
                <td>${course.name}</td>
                <td>${course.code}</td>
                <td>${course.credits || '-'}</td>
                <td>${course.department || '-'}</td>
                <td>${instructor ? instructor.name : '-'}</td>
                <td>${course.capacity || '-'}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editCourse('${course.id}')">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteCourse('${course.id}')">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    }
}

async function editCourse(courseId) {
    try {
        const response = await apiCall(`/courses/${courseId}`);
        if (response.success && response.data) {
            const course = response.data;
            currentEditingCourseId = courseId;
            await loadInstructorSelect();

            document.getElementById('courseId').value = course.id;
            document.getElementById('courseName').value = course.name;
            document.getElementById('courseCode').value = course.code;
            document.getElementById('courseDescription').value = course.description || '';
            document.getElementById('courseCredits').value = course.credits || '';
            document.getElementById('courseDepartment').value = course.department || '';
            document.getElementById('courseInstructor').value = course.instructorId || '';
            document.getElementById('courseCapacity').value = course.capacity || '';

            document.getElementById('courseFormModal').style.display = 'block';
        } else {
            showError('Course not found');
        }
    } catch (error) {
        console.error('Error loading course:', error);
        showError('Failed to load course data. Please check if the backend server is running.');

        // Fallback to local storage
        const courses = LocalStorage.get('courses') || [];
        const course = courses.find(c => c.id === courseId);

        if (!course) {
            showError('Course not found');
            return;
        }

        currentEditingCourseId = courseId;
        loadInstructorSelect();

        document.getElementById('courseId').value = course.id;
        document.getElementById('courseName').value = course.name;
        document.getElementById('courseCode').value = course.code;
        document.getElementById('courseDescription').value = course.description || '';
        document.getElementById('courseCredits').value = course.credits || '';
        document.getElementById('courseDepartment').value = course.department || '';
        document.getElementById('courseInstructor').value = course.instructorId || '';
        document.getElementById('courseCapacity').value = course.capacity || '';

        document.getElementById('courseFormModal').style.display = 'block';
    }
}

async function deleteCourse(courseId) {
    if (confirm('Are you sure you want to delete this course?')) {
        try {
            const response = await apiCall(`/courses/${courseId}`, 'DELETE');
            if (response.success) {
                showSuccess('Course deleted successfully');
                await loadCourses();
            } else {
                showError(response.message || 'Failed to delete course');
            }
        } catch (error) {
            console.error('Error deleting course:', error);
            showError('Failed to delete course. Please check if the backend server is running.');

            // Fallback to local storage
            let courses = LocalStorage.get('courses') || [];
            courses = courses.filter(c => c.id !== courseId);
            LocalStorage.set('courses', courses);

            // Also remove associated enrollments
            let enrollments = LocalStorage.get('enrollments') || [];
            enrollments = enrollments.filter(e => e.courseId !== courseId);
            LocalStorage.set('enrollments', enrollments);

            showSuccess('Course deleted successfully');
            loadCourses();
        }
    }
}

// Setup
document.addEventListener('DOMContentLoaded', async function() {
    await loadCourses();

    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keyup', async (e) => {
            await loadCourses(e.target.value);
        });
    }

    window.onclick = function(event) {
        const modal = document.getElementById('courseFormModal');
        if (event.target === modal) {
            closeAddCourseForm();
        }
    };
});
