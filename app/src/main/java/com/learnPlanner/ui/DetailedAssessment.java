package com.learnPlanner.ui;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.learnPlanner.R;
import com.learnPlanner.database.Repository;
import com.learnPlanner.entities.Assessment;
import com.learnPlanner.entities.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailedAssessment extends AppCompatActivity {
    Repository repository;
    EditText editTitle;
    String assessmentName;
    String assessmentType;
    String startDate;
    String endDate;
    AlertDialog.Builder builder;
    DatePickerDialog.OnDateSetListener pickStart;
    DatePickerDialog.OnDateSetListener pickEnd;
    final Calendar calendar = Calendar.getInstance();
    final Calendar currentCalendar = Calendar.getInstance();
    int courseId;
    int assessmentId;
    Date courseStart;
    Date courseEnd;
    Assessment assessment;
    String dateFormat = "MM/dd/yy";
    SimpleDateFormat newDateFormat = new SimpleDateFormat(dateFormat, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_assessment);
        repository = new Repository(getApplication());
        builder = new AlertDialog.Builder(this);
        Button saveButton = findViewById(R.id.assessment_save_button);
        Button startDateButton = findViewById(R.id.assessment_start_button);
        Button endDateButton = findViewById(R.id.assessment_end_button);

        //Gets references to the activities input fields
        editTitle = findViewById(R.id.detailed_assessment_edit);
        assessmentName = getIntent().getStringExtra("assessmentName");
        assessmentType = getIntent().getStringExtra("assessmentType");
        editTitle.setText(assessmentName);
        startDate = getIntent().getStringExtra("assessmentStart");
        endDate = getIntent().getStringExtra("assessmentEnd");
        assessmentId = getIntent().getIntExtra("assessmentId", 0);
        courseId = getIntent().getIntExtra("courseId", 0);
        setCourseDates();

        repository = new Repository(getApplication());
        startDateButton.setText(startDate);
        endDateButton.setText(endDate);

        String currentDaysDate = newDateFormat.format(new Date());

        if ( assessmentId == 0){
            startDateButton.setText(currentDaysDate);
            endDateButton.setText(currentDaysDate);
        }
        else{
            startDateButton.setText(startDate);
            endDateButton.setText(endDate);
        }

        startDateButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Date date;
                   String stringFromButton = startDateButton.getText().toString();
                   try{
                       calendar.setTime(newDateFormat.parse(stringFromButton));
                   }
                   catch (ParseException e){
                       throw new RuntimeException(e);
                   }
                   new DatePickerDialog(DetailedAssessment.this, pickStart, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
               }
           }
        );

        endDateButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Date date;
                     String stringFromButton = endDateButton.getText().toString();
                     try{
                         currentCalendar.setTime(newDateFormat.parse(stringFromButton));
                     }
                     catch (ParseException e){
                         throw new RuntimeException(e);
                     }
                     new DatePickerDialog(DetailedAssessment.this, pickEnd, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH)).show();
                 }
             }
        );

        pickStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDateButton.setText(newDateFormat.format(calendar.getTime()));
            }
        };

        pickEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentCalendar.set(Calendar.YEAR, year);
                currentCalendar.set(Calendar.MONTH, month);
                currentCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDateButton.setText(newDateFormat.format(currentCalendar.getTime()));
            }
        };

        Spinner typeSpinner = findViewById(R.id.assessment_type_spinner);
        ArrayAdapter<CharSequence> typeArray=ArrayAdapter.createFromResource(this,R.array.list_type,android.R.layout.simple_spinner_item);
        typeArray.setDropDownViewResource(android.R.layout.simple_spinner_item);
        typeSpinner.setAdapter(typeArray);
        if(assessmentType == null){
            typeSpinner.setSelection(0);
        }
        else if(assessmentType.equals("Objective")){
            typeSpinner.setSelection(0);
        }
        else if(assessmentType.equals("Performance")){
            typeSpinner.setSelection(1);
        }
        else{
            return;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  startDate = startDateButton.getText().toString();
                  endDate = endDateButton.getText().toString();
                  assessmentType = typeSpinner.getSelectedItem().toString();
                  if (!validateDates()) {
                      Toast.makeText(DetailedAssessment.this, "Assessment dates cannot be outside of the course start and end dates.", Toast.LENGTH_LONG).show();
                      return;
                  }
                  assessment = new Assessment(assessmentId, editTitle.getText().toString(), assessmentType, startDate, endDate, courseId);
                  if (assessmentId == 0){
                      repository.insert(assessment);
                      Toast.makeText(DetailedAssessment.this, "Assessment was added successfully.", Toast.LENGTH_LONG).show();
                  }
                  else{
                      repository.update(assessment);
                      Toast.makeText(DetailedAssessment.this, "Assessment was saved successfully.", Toast.LENGTH_LONG).show();
                  }
                  finish();
              }
          }
        );
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_list_assessment, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        else if(itemId == R.id.assessment_menu_delete){
            for (Assessment assessment : repository.getAllAssessments()){
                if (assessment.getAssessmentId() == assessmentId){
                    repository.delete(assessment);
                    Toast.makeText(DetailedAssessment.this, assessment.getTitle() + " was deleted successfully.", Toast.LENGTH_LONG).show();
                    DetailedAssessment.this.finish();
                }
            }
            finish();
            return true;
        } else if(itemId == R.id.assessment_menu_start){
            String formatForDate = "MM/dd/yy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatForDate, Locale.US);
            Date date = null;
            try {
                date = simpleDateFormat.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = date.getTime();
            Intent intent = new Intent(DetailedAssessment.this, Receiver.class);
            intent.putExtra("key", "Assessment " + editTitle.getText().toString() + " starts today.");
            PendingIntent sender = PendingIntent.getBroadcast(DetailedAssessment.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            Toast.makeText(DetailedAssessment.this, "An alert was made for the start of this assessment.", Toast.LENGTH_LONG).show();
            finish();
            return true;
        }  else if(itemId == R.id.assessment_menu_end){
            String formatForDate = "MM/dd/yy";
            SimpleDateFormat simpleDate = new SimpleDateFormat(formatForDate, Locale.US);
            Date date = null;
            try {
                date = simpleDate.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = date.getTime();
            Intent intent = new Intent(DetailedAssessment.this, Receiver.class);
            intent.putExtra("key", "Assessment " + editTitle.getText().toString() + " ends today.");
            PendingIntent sender = PendingIntent.getBroadcast(DetailedAssessment.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            Toast.makeText(DetailedAssessment.this, "An alert was made for the end of this assessment.", Toast.LENGTH_LONG).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCourseDates() {
        String courseStartString = "";
        String courseEndString = "";
        List<Course> courseList= repository.getAllCourses();
        for (Course course:courseList) {
            if (course.getCourseId() == courseId) {
                courseStartString = course.getStartDate();
                courseEndString = course.getEndDate();
                break;
            }
        }
        if (courseStartString.isEmpty()) {
            courseStartString = newDateFormat.format(new Date());
        }
        if (courseEndString.isEmpty()) {
            courseEndString = newDateFormat.format(new Date());
        }
        try {
            courseStart = newDateFormat.parse(courseStartString);
            courseEnd = newDateFormat.parse(courseEndString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateDates() {
        try {
            Date start = newDateFormat.parse(startDate);
            Date end = newDateFormat.parse(endDate);
            if (start.before(this.courseStart) || end.after(this.courseEnd) || start.after(end)) {
                return false;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}