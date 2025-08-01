package com.learnPlanner.entities;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.widget.EditText;
import android.widget.Spinner;

@Entity(tableName = "assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int assessmentId;
    private String title;
    private String type;
    private String startDate;
    private String endDate;
    private int courseId; //Foreign key
    public Assessment(int assessmentId, String title, String type, String startDate, String endDate, int courseId) {
        this.setAssessmentId(assessmentId);
        this.setTitle(title);
        this.setType(type);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setCourseId(courseId);
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getAssessmentId() {
        return this.assessmentId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getType() {
        return this.type;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void updateInputFields(EditText assessmentName, EditText assessmentInfo, Spinner assessmentType, EditText startDate, EditText endDate) {
        setTitle(assessmentName.getText().toString().trim());
        setType(assessmentType.getSelectedItem().toString());
        setStartDate(startDate.getText().toString());
        setEndDate(endDate.getText().toString());
    }
}