package com.college.models;

/**
 * Student model class representing a student entity in the college system.
 */
public class Student {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String department;
    private String address;
    private String enrollmentDate;

    public Student() {}

    public Student(String id, String name, String email, String phone, String dateOfBirth, 
                   String department, String address, String enrollmentDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.department = department;
        this.address = address;
        this.enrollmentDate = enrollmentDate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(String enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", department='" + department + '\'' +
                ", address='" + address + '\'' +
                ", enrollmentDate='" + enrollmentDate + '\'' +
                '}';
    }
}
