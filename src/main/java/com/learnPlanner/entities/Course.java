package com.learnPlanner.entities;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int courseId;
    private String title;
    private String status;
    private String instructorName;
    private String instructorPhone;
    private String instructorEmail;
    private String notes;
    private String startDate;
    private String endDate;

    //Foreign Keys
    private int termId;
    private int instructorId;

    public Course(int courseId, String title, String status, String instructorName, String instructorPhone, String instructorEmail, String notes, String startDate, String endDate, int termId, int instructorId) {
        this.setCourseId(courseId);
        this.setTitle(title);
        this.setStatus(status);
        this.setInstructorName(instructorName);
        this.setInstructorPhone(instructorPhone);
        this.setInstructorEmail(instructorEmail);
        this.setNotes(notes);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setTermId(termId);
        this.setInstructorId(instructorId);
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public void setInstructorId(int courseInstructorId) {
        this.instructorId = courseInstructorId;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public int getTermId() {
        return this.termId;
    }
    public String getInstructorName() {return this.instructorName;}
    public String getInstructorPhone() {return this.instructorPhone;}
    public String getInstructorEmail() {return this.instructorEmail;}

    public int getInstructorId() {
        return this.instructorId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getStatus() {
        return this.status;
    }

    public String getNotes() {
        return this.notes;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void updateFields(EditText className, EditText classInfo, Spinner classStatus,
        EditText startDate, EditText endDate) {
        this.setTitle(className.getText().toString().trim());
        this.setNotes(classInfo.getText().toString().trim());
        this.setStatus(classStatus.getSelectedItem().toString());
        this.setStartDate(startDate.getText().toString());
        this.setEndDate(endDate.getText().toString());
    }
}