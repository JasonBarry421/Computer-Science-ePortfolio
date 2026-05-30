package com.example.EventCalendarMobileApplication.Calendar.View;

import static com.example.EventCalendarMobileApplication.Calendar.View.DailyCalendarFormatting.hoursInDayArray;
import static com.example.EventCalendarMobileApplication.Calendar.View.DailyCalendarFormatting.monthDayFromDate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventCalendarMobileApplication.Calendar.Model.Event;
import com.example.EventCalendarMobileApplication.Calendar.ViewModel.EventViewModel;
import com.example.EventCalendarMobileApplication.R;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class activity_calendar_daily extends AppCompatActivity implements DailyCalendarAdapter.onHourClickListener {
    private TextView monthDayText;
    ImageButton mAddEventButton;
    public LocalDate selectedDate;
    public LocalTime selectedTime;
    public Event selectedEvent;
    public static RecyclerView dailyCalendarRecyclerView;
    private DailyCalendarAdapter adapter;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_daily);

        // Sets Date based on what Day was Clicked in Monthly View
        Intent getCurrentDayIntent = getIntent();
        String dateString = getCurrentDayIntent.getStringExtra("DATE");
        if (dateString != null) {
            selectedDate = LocalDate.parse(dateString);
        } else {
            selectedDate = LocalDate.now();
        }

        initWidgets();

        // Sets up the Daily Calendar's Recycler View
        ArrayList<LocalTime> hoursInDay = hoursInDayArray();
        adapter = new DailyCalendarAdapter(hoursInDay, this);
        dailyCalendarRecyclerView.setAdapter(adapter);

        // Populates UI
        setDayView();

        // Sets Add Event Button for Reference
        mAddEventButton = findViewById(R.id.AddEventButtonDailyVersion);

        // Listens for Add Event Button Click
        mAddEventButton.setOnClickListener((view -> {
            // Goes to Add Event Activity
            Intent intent = new Intent(activity_calendar_daily.this, addEventActivity.class);
            startActivity(intent);
        }));
    }

    private void initWidgets() {
        dailyCalendarRecyclerView = findViewById(R.id.dailyRecyclerView);
        monthDayText = findViewById(R.id.monthAndDay);
        dailyCalendarRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );
    }

    private void setDayView() {
        monthDayText.setText(monthDayFromDate(selectedDate));
        updateDailyEvents();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateDailyEvents() {
        // Format Selected Date for String Comparisons
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String date = formatter.format(selectedDate);

        EventViewModel eventViewModel = new EventViewModel(this);

        // Gets List of all Events for the Selected Date
        ArrayList<Event> dailyEvents = eventViewModel.eventsForDate(date);

        // Filters out all empty events
        List<Event> filteredEvents = new ArrayList<>();
        for (Event e : dailyEvents) {
            if (e.getStartDate().equals(selectedDate)){
                filteredEvents.add(e);
            }
        }

        // Sort Events by Start Time
        filteredEvents.sort((e1, e2) -> {
            LocalTime t1 = e1.getStartTime();
            LocalTime t2 = e2.getStartTime();
            return t1.compareTo(t2);
        });

        adapter.setEvents(filteredEvents);
    }

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
            //Enters Settings when Button Clicked
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

        MenuItem item = menu.findItem(R.id.editMenuButton);
        View actionView = item.getActionView();

        if (actionView != null) {
            Button editButton = actionView.findViewById(R.id.editButton);
            editButton.setOnClickListener(v -> {
                    // Declare Intention for Going into Update Event Activity
                    Intent intent = new Intent(this, updateEventActivity.class);

                    // Puts Event Values in order to Auto Populate Update Event Activity
                    intent.putExtra("EVENT_ID", selectedEvent.getID());
                    intent.putExtra("EVENT_USER_ID", selectedEvent.getUserID());
                    intent.putExtra("EVENT_TITLE", selectedEvent.getTitle());
                    intent.putExtra("EVENT_START_TIME", selectedEvent.getStartTime().toString());
                    intent.putExtra("EVENT_START_DATE", selectedEvent.getStartDate().toString());
                    intent.putExtra("EVENT_END_TIME", selectedEvent.getEndTime().toString());
                    intent.putExtra("EVENT_END_DATE", selectedEvent.getEndDate().toString());

                    // Go to Update Event Activity
                    startActivity(intent);
            });
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editMenu = menu.findItem(R.id.editMenuButton);
        editMenu.setVisible(selectedEvent != null);
        return super.onPrepareOptionsMenu(menu);
    }

    public void previousDayClick(View view) {
        selectedDate = selectedDate.minusDays(1);
        setDayView();
    }

    public void nextDayClick(View view) {
        selectedDate = selectedDate.plusDays(1);
        setDayView();
    }

    @Override
    public void onHourClick(int position, LocalTime hour) {
        selectedTime = hour;
        setDayView();
    }

    @Override
    public void onEventClick(Event event) {
        // Sets visibility of the Edit Button on the Menu
        if (selectedEvent != null && selectedEvent.equals(event)) {
            selectedEvent = null; // toggle off
        } else {
            selectedEvent = event;
        }

        invalidateOptionsMenu();
    }
}