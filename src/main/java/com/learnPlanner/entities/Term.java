package com.learnPlanner.entities;
import android.widget.EditText;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int termId;
    private String title;
    private String startDate;
    private String endDate;

    public Term(int termId, String title, String startDate, String endDate) {
        this.setTermId(termId);
        this.setTitle(title);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getTermId() {
        return this.termId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void updateFields(EditText termName, EditText startDate, EditText endDate) {
        this.setTitle(termName.getText().toString().trim());
        this.setStartDate(startDate.getText().toString());
        this.setEndDate(endDate.getText().toString());
    }
}