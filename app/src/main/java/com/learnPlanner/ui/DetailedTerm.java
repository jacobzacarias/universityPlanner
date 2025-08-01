package com.learnPlanner.ui;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.learnPlanner.R;
import com.learnPlanner.database.Repository;
import com.learnPlanner.entities.Course;
import com.learnPlanner.entities.Term;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailedTerm extends AppCompatActivity {
    Repository repository;
    int termId;
    EditText editTitle;
    String termTitle;
    Term term;
    String termStartDate;
    String termEndDate;
    DatePickerDialog.OnDateSetListener termDates;
    DatePickerDialog.OnDateSetListener termDatesEnd;
    final Calendar calendar = Calendar.getInstance();
    final Calendar currentCalendar = Calendar.getInstance();
    Term currentTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_term);
        RecyclerView recyclerView = findViewById(R.id.term_recycler_view);
        repository = new Repository(getApplication());
        final AdapterCourse adapterCourse = new AdapterCourse(this);
        recyclerView.setAdapter(adapterCourse);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button saveButton = findViewById(R.id.term_save_button);
        Button startDateButton = findViewById(R.id.term_start_button);
        Button endDateButton = findViewById(R.id.term_end_button);

        editTitle = findViewById(R.id.term_title);
        termTitle = getIntent().getStringExtra("title");
        editTitle.setText(termTitle);
        termId = getIntent().getIntExtra("id", 0);
        repository = new Repository(getApplication());
        termStartDate = getIntent().getStringExtra("start date");
        termEndDate = getIntent().getStringExtra("end date");

        endDateButton.setText(termEndDate);
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat newDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        String currentDaysDate = newDateFormat.format(new Date());

        List<Course> associatedCourses = new ArrayList<>();
        for (Course course : repository.getAllCourses()) {
            if (course.getTermId() == termId) {
                associatedCourses.add(course);
            }
        }
        adapterCourse.setCourse(associatedCourses);

        if (termId == 0) {
//        if (termId < 0) {
            startDateButton.setText(currentDaysDate);
            endDateButton.setText(currentDaysDate);
        } else {
            startDateButton.setText(termStartDate);
            endDateButton.setText(termEndDate);
        }

        startDateButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Date date;
                   String stringFromButton = startDateButton.getText().toString();
                   try {
                       calendar.setTime(newDateFormat.parse(stringFromButton));
                   } catch (ParseException e) {
                       throw new RuntimeException(e);
                   }
                   new DatePickerDialog(DetailedTerm.this, termDates, calendar.get(Calendar.YEAR),
                           calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
               }
           }
        );

        endDateButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Date date;
                 String stringFromButton = endDateButton.getText().toString();
                 try {
                     currentCalendar.setTime(newDateFormat.parse(stringFromButton));
                 } catch (ParseException e) {
                     throw new RuntimeException(e);
                 }
                 new DatePickerDialog(DetailedTerm.this, termDatesEnd, currentCalendar.get(Calendar.YEAR),
                         currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH)).show();
                 }
             }
        );

        termDates = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDateButton.setText(newDateFormat.format(calendar.getTime()));
            }
        };

        termDatesEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentCalendar.set(Calendar.YEAR, year);
                currentCalendar.set(Calendar.MONTH, month);
                currentCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDateButton.setText(newDateFormat.format(currentCalendar.getTime()));
            }
        };

        saveButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  termStartDate = startDateButton.getText().toString();
                  termEndDate = endDateButton.getText().toString();
                  term = new Term(termId, editTitle.getText().toString(), termStartDate, termEndDate);
                  if (termId == 0) {
                      repository.insert(term);
                      Toast.makeText(DetailedTerm.this, "The term was added successfully.", Toast.LENGTH_LONG).show();
                  } else {
                      repository.update(term);
                      Toast.makeText(DetailedTerm.this, "The term was updated successfully.", Toast.LENGTH_LONG).show();
                  }
                  finish();
              }
            }
        );

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButtonTerm);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailedTerm.this, DetailedCourse.class);
                    intent.putExtra("termId", termId);
                    startActivity(intent);
                }
            }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerViewOnResume = findViewById(R.id.term_recycler_view);
        final AdapterCourse adapterCourseOnResume = new AdapterCourse(this);
        recyclerViewOnResume.setAdapter(adapterCourseOnResume);
        recyclerViewOnResume.setLayoutManager(new LinearLayoutManager(this));
        List<Course> courseListOnResume = new ArrayList<>();
        for (Course course : repository.getAllCourses()) {
            if (course.getTermId() == termId) {
                courseListOnResume.add(course);
            }
        }
        adapterCourseOnResume.setCourse(courseListOnResume);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_list_term, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
            return true;
        } else if (itemId == R.id.term_delete_menu) {
            for(Term terms : repository.getAllTerms()){
                if(terms.getTermId() == termId){
                    currentTerm = terms;
                }
            }
            int associatedCourses = 0;
            for(Course course : repository.getAllCourses()) {
                if (course.getTermId() == termId) {
                    associatedCourses += 1;
                }
            }
            if(associatedCourses == 0){
                repository.delete(currentTerm);
                Toast.makeText(DetailedTerm.this, "Term " + currentTerm.getTitle() + " was successfully deleted.",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(DetailedTerm.this, "This term contains courses and cannot be deleted.", Toast.LENGTH_LONG).show();
            }
            DetailedTerm.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}