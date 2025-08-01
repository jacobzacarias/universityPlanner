package com.learnPlanner.ui;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.learnPlanner.R;
import com.learnPlanner.database.Repository;
import com.learnPlanner.entities.Term;

import java.util.List;
public class ListTerm extends AppCompatActivity {
    private Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        RecyclerView recyclerView = findViewById(R.id.item_recycler_view);
        final AdapterTerm adapterTerm = new AdapterTerm(this);
        recyclerView.setAdapter(adapterTerm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repository = new Repository(getApplication());
        List<Term> allTerms = repository.getAllTerms();
        adapterTerm.setTerm(allTerms);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListTerm.this, DetailedTerm.class);
                startActivity(intent);
                }
            }
        );
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_list_term, menu);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerViewOnResume = findViewById(R.id.item_recycler_view);
        final AdapterTerm adapterTermResume = new AdapterTerm(this);
        recyclerViewOnResume.setAdapter(adapterTermResume);
        recyclerViewOnResume.setLayoutManager(new LinearLayoutManager(this));
        List<Term> allTerms = repository.getAllTerms();
        adapterTermResume.setTerm(allTerms);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}