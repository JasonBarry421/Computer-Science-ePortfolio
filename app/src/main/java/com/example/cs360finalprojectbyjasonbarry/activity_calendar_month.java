package com.example.cs360finalprojectbyjasonbarry;

import static com.example.cs360finalprojectbyjasonbarry.CalendarStuff.daysInMonthArray;
import static com.example.cs360finalprojectbyjasonbarry.CalendarStuff.monthYearFromDate;
import static com.example.cs360finalprojectbyjasonbarry.CalendarStuff.selectedDate;
import static com.example.cs360finalprojectbyjasonbarry.EventDatabase.eventsForDate;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs360finalprojectbyjasonbarry.data.Users;
import com.example.cs360finalprojectbyjasonbarry.ui.login.LoginDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class activity_calendar_month extends AppCompatActivity implements CalendarAdapter.onDayClickListener{
    ImageButton mAddEventButton;
    ImageButton mDeleteEventButton;
    ImageButton mUpdateEventButton;
    private TextView monthYearText;
    public static RecyclerView calendarRecylcerView;
    private ListView eventListView;
    private EventDatabase eventDB;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        selectedDate = LocalDate.now();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_month);

        initWidgets();
        setMonthView();

        // Sets Add Event Button for Reference
        mAddEventButton = findViewById(R.id.AddEventButton);

        // Make it so that clicking an event removes AddEventButton
        //            Replace it with an update event button

        mAddEventButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_calendar_month.this, addEventActivity.class);
                startActivity(intent);
            }
        }));

        // NEEDS FIX:
        //            - Send Update Event info
        mUpdateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_calendar_month.this, updateEventActivity.class);
                startActivity(intent);
            }
        });


        //CRASHES WHEN SELECTED
        //
        String day = String.valueOf(selectedDate);
        mDeleteEventButton = findViewById(R.id.deleteEventButton);
        mDeleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventDB = new EventDatabase(context);
                ArrayList<Event> eventsList = eventDB.readEvents();

                for (Event e : eventsList) {
                    Log.i("Events", e.getStartDate() + "\n\n\n");

                    if (e.getStartDate().equals(String.valueOf(selectedDate))){
                        eventDB.deleteEvent((e.getTitle()));
                        Log.i("DELETED", "Event");
                    }
                }
            }
        });


    }

    private void initWidgets() {
        calendarRecylcerView = findViewById(R.id.calendarRecyclerView);
        eventListView = findViewById(R.id.eventListView);
        monthYearText = findViewById(R.id.monthAndYear);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(selectedDate);

        setCalendarAdapter();
        setEventAdapter();
    }

    private void setCalendarAdapter() {
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(this, daysInMonth, this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecylcerView.setLayoutManager(layoutManager);
        calendarRecylcerView.setAdapter(calendarAdapter);
    }

    private void setEventAdapter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String date = formatter.format(selectedDate);

        ArrayList<Event> dailyEvents = eventsForDate(this, date);

        EventAdapter eventAdapter = new EventAdapter(this, dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    @Override
    public void onResume(Bundle savedInstanceState) {
        super.onResume();
        setMonthView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        if (item.getItemId() == R.id.settingsSymbol) {
            //Enters Settings when button clicked
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_with_settings, menu);
        return true;
    }

    public void previousMonthClick(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthClick(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onDayClick(int position, LocalDate date) {
        selectedDate = date;
        setMonthView();
    }
}