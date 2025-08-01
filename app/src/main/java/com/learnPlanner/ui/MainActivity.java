package com.learnPlanner.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

import com.learnPlanner.R;
import com.learnPlanner.database.Repository;
import com.learnPlanner.entities.Assessment;
import com.learnPlanner.entities.Course;
import com.learnPlanner.entities.Term;

public class MainActivity extends AppCompatActivity {
    public static int numAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        Term term = new Term(1, "First Term", "09/01/23", "02/29/24");
        Term termSemester = new Term(2, "Second Term", "03/01/24", "08/31/24");
        //Course course = new Course(1, "Mobile Development", "Enrolled", "Carolyn Sher-DeCusa", "111-111-1111", "mail@wgu.edu", "Make in Android Studio", "06/01/2024", "08/31/2024", 1, 1);
        //Assessment assessment = new Assessment("Mobile Development Performance Test", "Performance", "Evaluation can take 1-3 days", "08/01/2024", "08/01/2024", 1);
        Repository repository = new Repository(getApplication());
        repository.insert(term);
        repository.insert(termSemester);
        //repository.insert(course);
        //repository.insert(assessment);
        button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(MainActivity.this, ListTerm.class);
                  startActivity(intent);
              }
          }
        );
    }
}