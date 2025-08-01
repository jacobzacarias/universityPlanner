package com.learnPlanner.ui;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.learnPlanner.R;
import com.learnPlanner.database.Repository;
import com.learnPlanner.entities.Assessment;
import com.learnPlanner.entities.Course;
import com.learnPlanner.entities.Term;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailedCourse extends AppCompatActivity {
    Repository repository;
    int courseId;
    int termId;
    int instructorId;
    Course course;
    String title;
    String startDate;
    String endDate;
    String instructorName;
    String instructorPhone;
    String instructorEmail;
    String status;
    String information;
    AlertDialog.Builder builder;
    EditText notesEdit;
    EditText courseNameEdit;
    EditText instructorNameEdit;
    EditText instructorPhoneEdit;
    EditText instructorEmailEdit;
    Button saveButton;
    DatePickerDialog.OnDateSetListener courseDateStart;
    DatePickerDialog.OnDateSetListener courseDateEnd;
    Date termStart;
    Date termEnd;
    String dateFormat = "MM/dd/yy";
    SimpleDateFormat newDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
    final Calendar calendar = Calendar.getInstance();
    final Calendar currentCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_course);
        RecyclerView recyclerView = findViewById(R.id.course_recycler_view);
        repository = new Repository(getApplication());
        builder = new AlertDialog.Builder(this);
        final AdapterAssessment adapterAssessment = new AdapterAssessment(this);
        recyclerView.setAdapter(adapterAssessment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        saveButton = findViewById(R.id.course_save_button);
        Button startDateButton = findViewById(R.id.course_start_button);
        Button endDateButton = findViewById(R.id.course_end_button);

        courseNameEdit = findViewById(R.id.course_edit_name);
        title = getIntent().getStringExtra("course_title");
        courseNameEdit.setText(title);
        instructorNameEdit = findViewById(R.id.instructor_edit_name);
        instructorName = getIntent().getStringExtra("instructor_name");
        instructorNameEdit.setText(instructorName);
        instructorEmailEdit = findViewById(R.id.email_edit_text);
        instructorEmail = getIntent().getStringExtra("email");
        instructorEmailEdit.setText(instructorEmail);
        instructorPhoneEdit = findViewById(R.id.phone_edit_text);
        instructorPhone = getIntent().getStringExtra("number");
        instructorPhoneEdit.setText(instructorPhone);
        notesEdit = findViewById(R.id.course_notes_edit_text);
        information = getIntent().getStringExtra("note");
        notesEdit.setText(information);
        startDate = getIntent().getStringExtra("start_date");
        endDate = getIntent().getStringExtra("end_date");
        status = getIntent().getStringExtra("list_progress");

        courseId = getIntent().getIntExtra("courseId", 0);
        termId = getIntent().getIntExtra("termId", 0);
        setTermDates();

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButtonCourse);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailedCourse.this, DetailedAssessment.class);
                    intent.putExtra("courseId", courseId);
                    startActivity(intent);
                }
            }
        );

        repository = new Repository(getApplication());
        startDateButton.setText(startDate);
        endDateButton.setText(endDate);

        String currentDaysDate = newDateFormat.format(new Date());

        List<Assessment> associatedAssessments = new ArrayList<>();
        for (Assessment assessment : repository.getAllAssessments()) {
            if (assessment.getCourseId() == courseId) {
                associatedAssessments.add(assessment);
            }
        }
        adapterAssessment.setAssessments(associatedAssessments);

        if (courseId == 0) {
            startDateButton.setText(currentDaysDate);
            endDateButton.setText(currentDaysDate);
        }
        else{
            startDateButton.setText(startDate);
            endDateButton.setText(endDate);
        }

        startDateButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Date date;
                   String stringFromButton = startDateButton.getText().toString();
                   try{
                       calendar.setTime(newDateFormat.parse(stringFromButton));
                   }
                   catch (ParseException e){
                       throw new RuntimeException(e);
                   }
                   new DatePickerDialog(DetailedCourse.this, courseDateStart, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
               }
           }
        );

        endDateButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Date date;
                     String stringFromButton = endDateButton.getText().toString();
                     try{
                         currentCalendar.setTime(newDateFormat.parse(stringFromButton));
                     }
                     catch (ParseException e){
                         throw new RuntimeException(e);
                     }
                     new DatePickerDialog(DetailedCourse.this, courseDateEnd, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH)).show();
                 }
             }
        );

        courseDateStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDateButton.setText(newDateFormat.format(calendar.getTime()));
            }
        };

        courseDateEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentCalendar.set(Calendar.YEAR, year);
                currentCalendar.set(Calendar.MONTH, month);
                currentCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDateButton.setText(newDateFormat.format(currentCalendar.getTime()));
            }
        };
        Spinner progressSpinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> progressArray=ArrayAdapter.createFromResource(this,R.array.list_progress,android.R.layout.simple_spinner_item);
        progressArray.setDropDownViewResource(android.R.layout.simple_spinner_item);
        progressSpinner.setAdapter(progressArray);
        if(status == null){
            progressSpinner.setSelection(1);
        }
        else if(status.equals("Currently Enrolled")){
            progressSpinner.setSelection(0);
        }
        else if(status.equals("Removed")){
            progressSpinner.setSelection(2);
        }
        else if(status.equals("Scheduled")){
            progressSpinner.setSelection(3);
        }
        else if(status.equals("Passed")){
            progressSpinner.setSelection(1);
        }
        else{
            return;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  startDate = startDateButton.getText().toString();
                  endDate = endDateButton.getText().toString();
                  status = progressSpinner.getSelectedItem().toString();
                  if (!validateDates()) {
                      Toast.makeText(DetailedCourse.this, "Course dates cannot be outside of the term start and end dates.", Toast.LENGTH_LONG).show();
                      return;
                  }
                  course = new Course(courseId, courseNameEdit.getText().toString(), status, instructorNameEdit.getText().toString(), instructorPhoneEdit.getText().toString(), instructorEmailEdit.getText().toString(), notesEdit.getText().toString(), startDate, endDate, termId, instructorId);
                  if (courseId == 0){
                      repository.insert(course);
                      Toast.makeText(DetailedCourse.this, "The course was added successfully.", Toast.LENGTH_LONG).show();
                  }
                  else{
                      repository.update(course);
                      Toast.makeText(DetailedCourse.this, "The course was saved successfully.", Toast.LENGTH_LONG).show();
                  }
                  finish();
              }
            }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerViewOnResume = findViewById(R.id.course_recycler_view);
        final AdapterAssessment adapterAssessmentOnResume = new AdapterAssessment(this);
        recyclerViewOnResume.setAdapter(adapterAssessmentOnResume);
        recyclerViewOnResume.setLayoutManager(new LinearLayoutManager(this));
        List<Assessment> assessmentListOnResume = new ArrayList<>();
        for (Assessment assessment : repository.getAllAssessments()) {
            if (assessment.getCourseId() == courseId) {
                assessmentListOnResume.add(assessment);
            }
        }
        adapterAssessmentOnResume.setAssessments(assessmentListOnResume);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_list_course, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
            return true;
        } else if (itemId == R.id.course_share_menu) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, notesEdit.getText().toString());
            intent.setType("text/plain");
            Intent intentActivity = Intent.createChooser(intent, null);
            startActivity(intentActivity);
            finish();
            return true;
        } else if(itemId == R.id.course_delete_menu){
            for (Course courses : repository.getAllCourses()) {
                if (courses.getCourseId() == courseId) {
                    course = courses;
                }
            }
            int associatedAssessments = 0;
            for (Assessment assessment : repository.getAllAssessments()) {
                if (assessment.getCourseId() == courseId) {
                    associatedAssessments += 1;
                }
            }
            if (associatedAssessments == 0) {
                repository.delete(course);
                Toast.makeText(DetailedCourse.this, "Course " + courseNameEdit.getText().toString() + " was successfully deleted.", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(DetailedCourse.this, "This course contains one assessment or more and cannot be deleted.", Toast.LENGTH_LONG).show();
            }
            DetailedCourse.this.finish();
            return true;
        } else if(itemId == R.id.course_start_menu){
            String formatForDate = "MM/dd/yy";
            SimpleDateFormat simpleDate = new SimpleDateFormat(formatForDate, Locale.US);
            Date date = null;
            try {
                date = simpleDate.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = date.getTime();
            Intent intent = new Intent(DetailedCourse.this, Receiver.class);
            intent.putExtra("key", courseNameEdit.getText().toString() + " starts today.");
            PendingIntent sender = PendingIntent.getBroadcast(DetailedCourse.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            Toast.makeText(DetailedCourse.this, "An alert was made for the start of this course.", Toast.LENGTH_LONG).show();
            finish();
            return true;
        }  else if(itemId == R.id.course_end_menu){
            String formatForDate = "MM/dd/yy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatForDate, Locale.US);
            Date date = null;
            try {
                date = simpleDateFormat.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = date.getTime();
            Intent intent = new Intent(DetailedCourse.this, Receiver.class);
            intent.putExtra("key", courseNameEdit.getText().toString() + " ends today.");
            PendingIntent sender = PendingIntent.getBroadcast(DetailedCourse.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            Toast.makeText(DetailedCourse.this, "An alert was made for the end of this course.", Toast.LENGTH_LONG).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTermDates() {
        String termStartString = "";
        String termEndString = "";
        List<Term> termList= repository.getAllTerms();
        for (Term term:termList) {
            if (term.getTermId() == termId) {
                termStartString = term.getStartDate();
                termEndString = term.getEndDate();
                break;
            }
        }
        if (termStartString.isEmpty()) {
            termStartString = newDateFormat.format(new Date());
        }
        if (termEndString.isEmpty()) {
            termEndString = newDateFormat.format(new Date());
        }
        try {
            termStart = newDateFormat.parse(termStartString);
            termEnd = newDateFormat.parse(termEndString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateDates() {
        try {
            Date start = newDateFormat.parse(startDate);
            Date end = newDateFormat.parse(endDate);
            if (start.before(this.termStart) || end.after(this.termEnd) || start.after(end)) {
                return false;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}