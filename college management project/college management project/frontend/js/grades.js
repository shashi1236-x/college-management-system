/* Grades JavaScript */

let currentEditingGradeId = null;

function openAddGradeForm() {
    currentEditingGradeId = null;
    resetGradeForm();
    document.getElementById('gradeId').value = generateId('GRD');
    loadGradeEnrollmentSelect();
    document.getElementById('gradeFormModal').style.display = 'block';
}

function closeAddGradeForm() {
    document.getElementById('gradeFormModal').style.display = 'none';
    currentEditingGradeId = null;
}

function resetGradeForm() {
    document.getElementById('gradeEnrollment').value = '';
    document.getElementById('gradeMidterm').value = '';
    document.getElementById('gradeFinal').value = '';
    document.getElementById('gradeAssignment').value = '0';
    document.getElementById('gradeAttendance').value = '0';
}

async function loadGradeEnrollmentSelect() {
    try {
        const enrollmentsResponse = await apiCall('/enrollments');
        const studentsResponse = await apiCall('/students');
        const coursesResponse = await apiCall('/courses');

        const enrollments = enrollmentsResponse.success ? enrollmentsResponse.data : [];
        const students = studentsResponse.success ? studentsResponse.data : [];
        const courses = coursesResponse.success ? coursesResponse.data : [];

        const select = document.getElementById('gradeEnrollment');
        select.innerHTML = '<option value="">Select an enrollment</option>';

        enrollments.forEach(enrollment => {
            const student = students.find(s => s.id === enrollment.studentId);
            const course = courses.find(c => c.id === enrollment.courseId);

            if (student && course) {
                const option = document.createElement('option');
                option.value = enrollment.id;
                option.textContent = `${student.name} - ${course.name}`;
                select.appendChild(option);
            }
        });
    } catch (error) {
        console.error('Error loading grade enrollment selects:', error);
        // Fallback to local storage
        const enrollments = LocalStorage.get('enrollments') || [];
        const students = LocalStorage.get('students') || [];
        const courses = LocalStorage.get('courses') || [];

        const select = document.getElementById('gradeEnrollment');
        select.innerHTML = '<option value="">Select an enrollment</option>';

        enrollments.forEach(enrollment => {
            const student = students.find(s => s.id === enrollment.studentId);
            const course = courses.find(c => c.id === enrollment.courseId);

            if (student && course) {
                const option = document.createElement('option');
                option.value = enrollment.id;
                option.textContent = `${student.name} - ${course.name}`;
                select.appendChild(option);
            }
        });
    }
}

function submitGrade(event) {
    event.preventDefault();

    const midterm = parseFloat(document.getElementById('gradeMidterm').value);
    const final = parseFloat(document.getElementById('gradeFinal').value);
    const assignment = parseFloat(document.getElementById('gradeAssignment').value) || 0;
    const attendance = parseFloat(document.getElementById('gradeAttendance').value) || 0;

    const gradeData = {
        id: document.getElementById('gradeId').value,
        enrollmentId: document.getElementById('gradeEnrollment').value,
        midterm: midterm,
        final: final,
        assignment: assignment,
        attendance: attendance,
        overallGrade: calculateOverallGrade(midterm, final, assignment, attendance),
        createdDate: new Date().toISOString().split('T')[0]
    };

    // Validation
    if (!gradeData.enrollmentId || !gradeData.midterm || !gradeData.final) {
        showError('Please fill in all required fields');
        return;
    }

    if (midterm < 0 || midterm > 100 || final < 0 || final > 100 || 
        assignment < 0 || assignment > 100 || attendance < 0 || attendance > 100) {
        showError('Grades must be between 0 and 100');
        return;
    }

    try {
        if (currentEditingGradeId) {
            const response = await apiCall(`/grades/${currentEditingGradeId}`, 'PUT', gradeData);
            if (response.success) {
                showSuccess('Grade updated successfully');
            } else {
                showError(response.message || 'Failed to update grade');
                return;
            }
        } else {
            const response = await apiCall('/grades', 'POST', gradeData);
            if (response.success) {
                showSuccess('Grade added successfully');
            } else {
                showError(response.message || 'Failed to add grade');
                return;
            }
        }

        closeAddGradeForm();
        await loadGrades();
    } catch (error) {
        console.error('Error saving grade:', error);
        showError('Failed to save grade. Please check if the backend server is running.');
    }
}

async function loadGrades(searchTerm = '') {
    try {
        const gradesResponse = await apiCall('/grades');
        const enrollmentsResponse = await apiCall('/enrollments');
        const studentsResponse = await apiCall('/students');
        const coursesResponse = await apiCall('/courses');

        let grades = gradesResponse.success ? gradesResponse.data : [];
        const enrollments = enrollmentsResponse.success ? enrollmentsResponse.data : [];
        const students = studentsResponse.success ? studentsResponse.data : [];
        const courses = coursesResponse.success ? coursesResponse.data : [];

        if (searchTerm) {
            grades = grades.filter(g => {
                const enrollment = enrollments.find(e => e.id === g.enrollmentId);
                if (!enrollment) return false;
                
                const student = students.find(s => s.id === enrollment.studentId);
                const course = courses.find(c => c.id === enrollment.courseId);
                
                const studentName = student ? student.name.toLowerCase() : '';
                const courseName = course ? course.name.toLowerCase() : '';
                const term = searchTerm.toLowerCase();
                
                return studentName.includes(term) || courseName.includes(term);
            });
        }

        // If API fails, fallback to local storage
        if (!gradesResponse.success || !enrollmentsResponse.success || !studentsResponse.success || !coursesResponse.success) {
            console.warn('API failed, using local storage fallback');
            grades = LocalStorage.get('grades') || [];
            const enrollmentsBackup = LocalStorage.get('enrollments') || [];
            const studentsBackup = LocalStorage.get('students') || [];
            const coursesBackup = LocalStorage.get('courses') || [];

            if (searchTerm) {
                grades = grades.filter(g => {
                    const enrollment = enrollmentsBackup.find(e => e.id === g.enrollmentId);
                    if (!enrollment) return false;
                    
                    const student = studentsBackup.find(s => s.id === enrollment.studentId);
                    const course = coursesBackup.find(c => c.id === enrollment.courseId);
                    
                    const studentName = student ? student.name.toLowerCase() : '';
                    const courseName = course ? course.name.toLowerCase() : '';
                    const term = searchTerm.toLowerCase();
                    
                    return studentName.includes(term) || courseName.includes(term);
                });
            }

            const tableBody = document.getElementById('gradesTable');
            tableBody.innerHTML = '';

            if (grades.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="9" class="no-data">No grades found. Click "Add Grade" to get started.</td></tr>';
                return;
            }

            grades.forEach(grade => {
                const enrollment = enrollmentsBackup.find(e => e.id === grade.enrollmentId);
                if (!enrollment) return;
                
                const student = studentsBackup.find(s => s.id === enrollment.studentId);
                const course = coursesBackup.find(c => c.id === enrollment.courseId);
                
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${grade.id}</td>
                    <td>${student ? student.name : '-'}</td>
                    <td>${course ? course.name : '-'}</td>
                    <td>${grade.midterm}</td>
                    <td>${grade.final}</td>
                    <td>${grade.assignment || '-'}</td>
                    <td>${grade.attendance}%</td>
                    <td><strong>${grade.overallGrade} (${getLetterGrade(grade.overallGrade)})</strong></td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="editGrade('${grade.id}')">Edit</button>
                        <button class="btn btn-sm btn-danger" onclick="deleteGrade('${grade.id}')">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
            return;
        }

        const tableBody = document.getElementById('gradesTable');
        tableBody.innerHTML = '';

        if (grades.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="9" class="no-data">No grades found. Click "Add Grade" to get started.</td></tr>';
            return;
        }

        grades.forEach(grade => {
            const enrollment = enrollments.find(e => e.id === grade.enrollmentId);
            if (!enrollment) return;
            
            const student = students.find(s => s.id === enrollment.studentId);
            const course = courses.find(c => c.id === enrollment.courseId);
            
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${grade.id}</td>
                <td>${student ? student.name : '-'}</td>
                <td>${course ? course.name : '-'}</td>
                <td>${grade.midterm}</td>
                <td>${grade.final}</td>
                <td>${grade.assignment || '-'}</td>
                <td>${grade.attendance}%</td>
                <td><strong>${grade.overallGrade} (${getLetterGrade(grade.overallGrade)})</strong></td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editGrade('${grade.id}')">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteGrade('${grade.id}')">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading grades:', error);
        showError('Failed to load grades. Please check if the backend server is running.');

        // Fallback to local storage
        const grades = LocalStorage.get('grades') || [];
        const enrollments = LocalStorage.get('enrollments') || [];
        const students = LocalStorage.get('students') || [];
        const courses = LocalStorage.get('courses') || [];

        if (grades.length === 0) {
            const tableBody = document.getElementById('gradesTable');
            tableBody.innerHTML = '<tr><td colspan="9" class="no-data">No grades found. Click "Add Grade" to get started.</td></tr>';
            return;
        }

        const tableBody = document.getElementById('gradesTable');
        tableBody.innerHTML = '';

        grades.forEach(grade => {
            const enrollment = enrollments.find(e => e.id === grade.enrollmentId);
            if (!enrollment) return;
            
            const student = students.find(s => s.id === enrollment.studentId);
            const course = courses.find(c => c.id === enrollment.courseId);
            
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${grade.id}</td>
                <td>${student ? student.name : '-'}</td>
                <td>${course ? course.name : '-'}</td>
                <td>${grade.midterm}</td>
                <td>${grade.final}</td>
                <td>${grade.assignment || '-'}</td>
                <td>${grade.attendance}%</td>
                <td><strong>${grade.overallGrade} (${getLetterGrade(grade.overallGrade)})</strong></td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editGrade('${grade.id}')">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteGrade('${grade.id}')">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    }
}
                <button class="btn btn-sm btn-danger" onclick="deleteGrade('${grade.id}')">Delete</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function editGrade(gradeId) {
    const grades = LocalStorage.get('grades') || [];
    const grade = grades.find(g => g.id === gradeId);

    if (!grade) {
        showError('Grade not found');
        return;
    }

    currentEditingGradeId = gradeId;
    loadGradeEnrollmentSelect();
    
    document.getElementById('gradeId').value = grade.id;
    document.getElementById('gradeEnrollment').value = grade.enrollmentId;
    document.getElementById('gradeMidterm').value = grade.midterm;
    document.getElementById('gradeFinal').value = grade.final;
    document.getElementById('gradeAssignment').value = grade.assignment || '0';
    document.getElementById('gradeAttendance').value = grade.attendance || '0';

    document.getElementById('gradeFormModal').style.display = 'block';
}

function deleteGrade(gradeId) {
    if (confirm('Are you sure you want to delete this grade?')) {
        let grades = LocalStorage.get('grades') || [];
        grades = grades.filter(g => g.id !== gradeId);
        LocalStorage.set('grades', grades);

        showSuccess('Grade deleted successfully');
        loadGrades();
    }
}

// Setup
document.addEventListener('DOMContentLoaded', function() {
    loadGrades();

    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keyup', (e) => {
            loadGrades(e.target.value);
        });
    }

    window.onclick = function(event) {
        const modal = document.getElementById('gradeFormModal');
        if (event.target === modal) {
            closeAddGradeForm();
        }
    };
});
