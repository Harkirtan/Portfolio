package com.example.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.CalendarView;
import java.util.Calendar;
import java.util.Date;

/**
 * Activity to display a calendar view and a recycler view to show runs on that day
 */
public class CalendarActivity extends AppCompatActivity {

    private DatabaseViewModel viewModel;
    private CalendarViewModel calendarViewModel;
    RecyclerView recyclerViewCalendar;
    RunAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendarView = findViewById(R.id.calendarView);
        recyclerViewCalendar = findViewById(R.id.calendarRecycler);
        setAdapter();
        viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(DatabaseViewModel.class);
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        final Observer<Long> dateObserver = date -> {
            calendarView.setDate(date);
            viewModel.getTodayRuns(date).observe(CalendarActivity.this, runs -> adapter.setData(runs));
        };
        calendarViewModel.getDate().observe(this, dateObserver);


        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            Date d = calendar.getTime();
            calendarViewModel.setDate(d.getTime());
            viewModel.getTodayRuns(d.getTime()).observe(CalendarActivity.this, runs -> adapter.setData(runs));
        });
    }

    /**
     * Set adapter for recycler view
     */
    private void setAdapter(){
        adapter = new RunAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewCalendar.setLayoutManager(layoutManager);
        recyclerViewCalendar.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCalendar.setAdapter(adapter);
    }
}