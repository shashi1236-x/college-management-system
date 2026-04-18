package com.college.models;

/**
 * Grade model class representing a grade entity in the college system.
 */
public class Grade {
    private String id;
    private String enrollmentId;
    private double midterm;
    private double final_;
    private double assignment;
    private double attendance;
    private double overallGrade;
    private String createdDate;

    public Grade() {}

    public Grade(String id, String enrollmentId, double midterm, double final_,
                 double assignment, double attendance, double overallGrade, String createdDate) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.midterm = midterm;
        this.final_ = final_;
        this.assignment = assignment;
        this.attendance = attendance;
        this.overallGrade = overallGrade;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(String enrollmentId) { this.enrollmentId = enrollmentId; }

    public double getMidterm() { return midterm; }
    public void setMidterm(double midterm) { this.midterm = midterm; }

    public double getFinal() { return final_; }
    public void setFinal(double final_) { this.final_ = final_; }

    public double getAssignment() { return assignment; }
    public void setAssignment(double assignment) { this.assignment = assignment; }

    public double getAttendance() { return attendance; }
    public void setAttendance(double attendance) { this.attendance = attendance; }

    public double getOverallGrade() { return overallGrade; }
    public void setOverallGrade(double overallGrade) { this.overallGrade = overallGrade; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    /**
     * Calculate overall grade from components
     * Weights: Midterm 20%, Final 50%, Assignment 20%, Attendance 10%
     */
    public void calculateOverallGrade() {
        this.overallGrade = (midterm * 0.2) + (final_ * 0.5) + (assignment * 0.2) + (attendance * 0.1);
    }

    /**
     * Get letter grade based on overall grade
     */
    public char getLetterGrade() {
        if (overallGrade >= 90) return 'A';
        if (overallGrade >= 80) return 'B';
        if (overallGrade >= 70) return 'C';
        if (overallGrade >= 60) return 'D';
        return 'F';
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id='" + id + '\'' +
                ", enrollmentId='" + enrollmentId + '\'' +
                ", overallGrade=" + overallGrade +
                ", letterGrade=" + getLetterGrade() +
                '}';
    }
}
