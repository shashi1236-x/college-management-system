package com.college.models;

/**
 * Instructor model class representing an instructor entity in the college system.
 */
public class Instructor {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String qualification;
    private int experience;
    private String specialization;
    private String hireDate;

    public Instructor() {}

    public Instructor(String id, String name, String email, String phone, String department,
                     String qualification, int experience, String specialization, String hireDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.qualification = qualification;
        this.experience = experience;
        this.specialization = specialization;
        this.hireDate = hireDate;
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

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getHireDate() { return hireDate; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }

    @Override
    public String toString() {
        return "Instructor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", experience=" + experience +
                '}';
    }
}
