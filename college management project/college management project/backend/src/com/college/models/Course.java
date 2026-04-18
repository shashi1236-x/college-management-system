package com.college.models;

/**
 * Course model class representing a course entity in the college system.
 */
public class Course {
    private String id;
    private String name;
    private String code;
    private String description;
    private int credits;
    private String department;
    private String instructorId;
    private int capacity;
    private String createdDate;

    public Course() {}

    public Course(String id, String name, String code, String description, int credits,
                  String department, String instructorId, int capacity, String createdDate) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.credits = credits;
        this.department = department;
        this.instructorId = instructorId;
        this.capacity = capacity;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", credits=" + credits +
                ", department='" + department + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
