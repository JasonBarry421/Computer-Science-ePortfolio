package com.example.EventCalendarMobileApplication.Calendar.View;

import static com.example.EventCalendarMobileApplication.Calendar.View.CalendarFormatting.daysInMonthArray;
import static com.example.EventCalendarMobileApplication.Calendar.View.CalendarFormatting.monthYearFromDate;
import static com.example.EventCalendarMobileApplication.Calendar.View.CalendarFormatting.selectedDate;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventCalendarMobileApplication.Calendar.ViewModel.EventViewModel;
import com.example.EventCalendarMobileApplication.Calendar.Model.Event;
import com.example.EventCalendarMobileApplication.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class activity_calendar_month extends AppCompatActivity implements CalendarAdapter.onDayClickListener {
    ImageButton mAddEventButton;
    private TextView monthYearText;
    public static RecyclerView calendarRecyclerView;
    private EventViewModel eventViewModel;
    private Event selectedEvent;
    private CalendarAdapter calendarAdapter;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        selectedDate = LocalDate.now();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_month);

        eventViewModel = new EventViewModel(this);

        initWidgets();
        setMonthView();

        // Sets Add Event Button for Reference
        mAddEventButton = findViewById(R.id.AddEventButton);

        // Listens for Add Event Button Click
        mAddEventButton.setOnClickListener((view -> {
            // Goes to Add Event Activity
            Intent intent = new Intent(activity_calendar_month.this, addEventActivity.class);
            startActivity(intent);
        }));
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthAndYear);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));

        setCalendarAdapter();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setCalendarAdapter() {

        ArrayList<LocalDate> daysInMonth =
                daysInMonthArray(selectedDate);

        calendarAdapter =
                new CalendarAdapter(daysInMonth, this);

        calendarRecyclerView.post(() -> {

            int height = calendarRecyclerView.getHeight();
            int rowHeight = height / 6;

            calendarAdapter.setRowHeight(rowHeight);

            calendarRecyclerView.setLayoutManager(
                    new GridLayoutManager(this, 7));

            calendarRecyclerView.setAdapter(calendarAdapter);
        });

        eventViewModel.readEvents(events -> calendarAdapter.setEvents(events));
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
        return true;
    }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editMenu = menu.findItem(R.id.editMenuButton);
        editMenu.setVisible(selectedEvent != null);
        return super.onPrepareOptionsMenu(menu);
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
        // Goes into Daily View based on Selected Date
        Intent intent = new Intent(this, activity_calendar_daily.class);
        intent.putExtra("DATE", date.toString());
        startActivity(intent);
    }
}