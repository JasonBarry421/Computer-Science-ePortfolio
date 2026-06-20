package com.example.EventCalendarMobileApplication.Calendar.View;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NavUtils;

import com.example.EventCalendarMobileApplication.Calendar.Model.Event;
import com.example.EventCalendarMobileApplication.Calendar.ViewModel.EventViewModel;
import com.example.EventCalendarMobileApplication.R;
import com.example.EventCalendarMobileApplication.Session;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class addEventActivity extends AppCompatActivity {
    SwitchCompat mAllDaySwitch;
    EditText mTitleET;
    EditText mStartDateEditText;
    EditText mStartTimeEditText;
    EditText mEndDateEditText;
    EditText mEndTimeEditText;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    int currentDayNum;
    int currentDayOfWeek;
    int currentYear;
    int currentMonthNum;
    boolean is24HourView;
    String amPM;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        initWidgets();

        /*
         ****************************************
         *****        ALL DAY SWITCH        *****
         ****************************************/

        mAllDaySwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            if (isChecked) {
                //on
                // Sets Start and End Times
                mStartTimeEditText.setText("12:00 AM");
                mEndTimeEditText.setText("11:59 PM");

                // Disables Visibility of all Time Changing Buttons
                mStartTimeEditText.setEnabled(false);
                mStartTimeEditText.setVisibility(View.INVISIBLE);
                mEndTimeEditText.setEnabled(false);
                mEndTimeEditText.setVisibility(View.INVISIBLE);

                // Sets End Date to Same Day as Start Date
                mEndDateEditText.setText(String.valueOf(mStartDateEditText.getText()));


            } else {
                //off
                // Enables all Time Changing Buttons
                mStartTimeEditText.setEnabled(true);
                mStartTimeEditText.setVisibility(View.VISIBLE);
                mEndTimeEditText.setEnabled(true);
                mEndTimeEditText.setVisibility(View.VISIBLE);
            }
        });


        /*
         **********************************************
         *****        START DATE EDIT TEXT        *****
         * ********************************************
         */

        // Sets Default Start Date Text
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy");
        LocalDate curStartDate = LocalDate.now();
        String currentStartDate = dateTimeFormatter.format(curStartDate);
        mStartDateEditText.setText(currentStartDate);

        // Listen for Start Date Edit Text Press
        mStartDateEditText.setOnClickListener(view -> {

            calendar = Calendar.getInstance();
            currentMonthNum = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);
            currentDayNum = calendar.get(Calendar.DAY_OF_MONTH);
            currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Allows User to Pick a Date
            datePickerDialog = new DatePickerDialog(addEventActivity.this, (datePicker, year, monthOfYear, dayOfMonth) -> {
                //Gets Day of Week (first three letters) for Chosen Day
                SimpleDateFormat simpleDayFormat = new SimpleDateFormat("EEE", Locale.US);
                Date dayAbbreviated = new Date(year, monthOfYear, dayOfMonth - 1);
                String chosenDayOfWeek = simpleDayFormat.format(dayAbbreviated);

                // Gets First Three Letters of Month for Chosen Month
                SimpleDateFormat simpleMonthFormat = new SimpleDateFormat("MMM", Locale.US);
                Date monthAbbreviated = new Date(year, monthOfYear, dayOfMonth);
                String chosenMonth = simpleMonthFormat.format(monthAbbreviated);

                SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("dd", Locale.US);
                Date day = new Date(year, monthOfYear, dayOfMonth);
                String chosenDate = dayOfMonthFormat.format(day);

                String dateText = chosenDayOfWeek + ", " + chosenMonth + " " + chosenDate + ", " + year;
                mStartDateEditText.setText(dateText);
            }, currentYear, currentMonthNum, currentDayNum);
            datePickerDialog.show();
        });

        /*
         **********************************************
         *****        START TIME EDIT TEXT        *****
         **********************************************
         */

        // Sets Default Start Time Text
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        LocalTime curStartTime = LocalTime.now();
        String currentStartTime = timeFormatter.format(curStartTime);
        mStartTimeEditText.setText(currentStartTime);

        // Listen for Start Time Edit Text
        mStartTimeEditText.setOnClickListener(view -> {

            // Gets Current Time
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);
            is24HourView = false;

            // Creates a Dialog that allows you to choose the Time
            timePickerDialog = new TimePickerDialog(addEventActivity.this, (timePicker, hourOfDay, minute) -> {
                // PM
                if (hourOfDay >= 12) {
                    hourOfDay = hourOfDay - 12;
                    amPM = "PM";
                }
                // AM
                else {
                    amPM = "AM";
                }
                // Ensure Minute takes Two Spaces
                @SuppressLint("DefaultLocale") String minuteString = String.format("%02d", minute);

                // Set Chosen Time
                String time = hourOfDay + ":" + minuteString + " " + amPM;

                mStartTimeEditText.setText(time);
            }, currentHour, currentMinute, false);
            timePickerDialog.show();
        });

        /*
         **********************************************
         *****         END DATE EDIT TEXT         *****
         **********************************************
         */

        // Sets Default Start Date Text
        LocalDate curEndDate = LocalDate.now();
        String currentEndDate = dateTimeFormatter.format(curEndDate);
        mEndDateEditText.setText(currentEndDate);

        // Listen for End Date Edit Text Press
        mEndDateEditText.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            currentMonthNum = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);
            currentDayNum = calendar.get(Calendar.DAY_OF_MONTH);
            currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Allows User to Pick a Date
            datePickerDialog = new DatePickerDialog(addEventActivity.this, (datePicker, year, monthOfYear, dayOfMonth) -> {

                //Gets Day of Week (first three letters) for Chosen Day
                SimpleDateFormat simpleDayFormat = new SimpleDateFormat("EEE", Locale.US);
                Date dayAbbreviated = new Date(year, monthOfYear, dayOfMonth - 1);
                String chosenDayOfWeek = simpleDayFormat.format(dayAbbreviated);

                // Gets First Three Letters of Month for Chosen Month
                SimpleDateFormat simpleMonthFormat = new SimpleDateFormat("MMM", Locale.US);
                Date monthAbbreviated = new Date(year, monthOfYear, dayOfMonth);
                String chosenMonth = simpleMonthFormat.format(monthAbbreviated);

                SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("dd", Locale.US);
                Date day = new Date(year, monthOfYear, dayOfMonth);
                String chosenDate = dayOfMonthFormat.format(day);

                // Output Chosen Date
                String dateText = chosenDayOfWeek + ", " + chosenMonth + " " + chosenDate + ", " + year;

                mEndDateEditText.setText(dateText);
            }, currentYear, currentMonthNum, currentDayNum);
            datePickerDialog.show();
        });

        /*
         **********************************************
         *****         END TIME EDIT TEXT         *****
         **********************************************
         */

        // Sets Default End Time Text
        LocalTime curEndTime = LocalTime.now().plusHours(1);
        String currentEndTime = timeFormatter.format(curEndTime);
        mEndTimeEditText.setText(currentEndTime);

        // Listens for End Time Edit Text Press
        mEndTimeEditText.setOnClickListener(view -> {

            // Gets Current Time
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);
            is24HourView = false;

            // Creates a Dialog that allows you to choose the Time
            timePickerDialog = new TimePickerDialog(addEventActivity.this, (timePicker, hourOfDay, minute) -> {
                // PM
                if (hourOfDay > 12) {
                    hourOfDay = hourOfDay - 12;
                    amPM = "PM";
                }
                // AM
                else {
                    amPM = "AM";
                }

                // Ensure Minute takes Two Spaces
                @SuppressLint("DefaultLocale") String minuteString = String.format("%02d", minute);

                // Set Chosen Time
                String time = hourOfDay + ":" + minuteString + " " + amPM;
                mEndTimeEditText.setText(time);
            }, currentHour, currentMinute, false);
            timePickerDialog.show();
        });
    }

    private void initWidgets() {
        mTitleET = findViewById(R.id.addEventTitle);
        mAllDaySwitch = findViewById(R.id.allDaySwitch);
        mStartDateEditText = findViewById(R.id.startDateEditText);
        mStartTimeEditText = findViewById(R.id.startTimeEditText);
        mEndDateEditText = findViewById(R.id.endDateEditText);
        mEndTimeEditText = findViewById(R.id.endTimeEditText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_event_menu, menu);

        MenuItem item = menu.findItem(R.id.saveButtonSymbol);
        View actionView = item.getActionView();

        if (actionView != null) {
            Button saveButton = actionView.findViewById(R.id.saveButton);
            saveButton.setOnClickListener(v -> handleSaveEvent());
        }

        return true;
    }

    private void handleSaveEvent() {

        String mTitle = mTitleET.getText().toString();
        String startDate = mStartDateEditText.getText().toString();
        String endDate = mEndDateEditText.getText().toString();
        String startTime = mStartTimeEditText.getText().toString();
        String endTime = mEndTimeEditText.getText().toString();

        // Format Start and End Dates
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        // As Local Dates
        LocalDate parsedStartDate = LocalDate.parse(startDate, inputFormatter);
        LocalDate parsedEndDate = LocalDate.parse(endDate, inputFormatter);

        // As Strings
        String cleanStartDate = parsedStartDate.format(outputFormatter);
        String cleanEndDate = parsedEndDate.format(outputFormatter);

        // Display Error Message if the End Date is before the Start Date
        if (parsedEndDate.isBefore(parsedStartDate)) {
            Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_LONG).show();
            return;
        }

        // Format Start and End Times
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        LocalTime sTime = LocalTime.parse(startTime, timeFormatter);
        LocalTime eTime = LocalTime.parse(endTime, timeFormatter);

        // Display Error Message if the End Time is before the Start Time
        if (eTime.isBefore(sTime)) {
            Toast.makeText(this, "End time must be after start time.", Toast.LENGTH_LONG).show();
            return;
        }

        // Set a Default Title if none is provided
        if (mTitle.isEmpty()) {
            mTitle = "Event on " + cleanStartDate + " at " + startTime;
        }

        // Get new Start and End Dates
        LocalDateTime newStart = LocalDateTime.of(parsedStartDate, sTime);
        LocalDateTime newEnd = LocalDateTime.of(parsedEndDate, eTime);

        Map<String, Object> event = new HashMap<>();
        event.put("userID", Session.currentUserId);
        event.put("title", mTitle);
        event.put("startDate", cleanStartDate);
        event.put("startTime", startTime);
        event.put("endDate", cleanEndDate);
        event.put("endTime", endTime);

        checkConflictsThenSave(event, newStart, newEnd);
    }


    private void checkConflictsThenSave(Map<String, Object> event, LocalDateTime newStart, LocalDateTime newEnd) {

        EventViewModel vm = new EventViewModel(this);

        // Read through Events
        vm.readEvents(events -> {

            // Create a Conflict List
            List<String> conflicts = new ArrayList<>();

            // Create Formatting Templates for String Comparisons
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

            // For each Event
            for (Event e : events) {

                // Get Start Dates and Times
                LocalDate existingStartDate = LocalDate.parse(e.getStartDate(), dateFormatter);
                LocalTime existingStartTime = LocalTime.parse(e.getStartTime(), timeFormatter);
                LocalDateTime existingStart = LocalDateTime.of(existingStartDate, existingStartTime);


                // Get End Dates and Times
                LocalDate existingEndDate = LocalDate.parse(e.getEndDate(), dateFormatter);
                LocalTime existingEndTime = LocalTime.parse(e.getEndTime(), timeFormatter);
                LocalDateTime existingEnd = LocalDateTime.of(existingEndDate, existingEndTime);

                // Check for Overlap
                boolean overlaps = newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
                if (overlaps) {
                    // Add the Title of the Overlapping Event to the Conflicts List
                    conflicts.add(e.getTitle());
                }
            }

            // If there are conflicts
            if (!conflicts.isEmpty()) {
                // Display Conflict Dialog
                showConflictDialog(conflicts, event);
            }
            // Otherwise
            else {
                // Save the Event
                saveEvent(event);
            }
        });
    }

    private void saveEvent(Map<String, Object> event) {
        EventViewModel vm = new EventViewModel(this);

        // Add Event to Database
        vm.addEvent(event, documentReference -> {

            String eventId = documentReference.getId();

            // Return to Monthly Calendar View
            Intent intent = new Intent(this, activity_calendar_month.class);
            intent.putExtra("EVENT_ID", eventId);

            startActivity(intent);
            finish();
        });
    }

    private void showConflictDialog(List<String> conflicts, Map<String, Object> event) {

        // Begin Creating the Overlap Message
        StringBuilder message = new StringBuilder();
        message.append("This event overlaps with:\n\n");

        // Add Title of Overlapping Events to Overlap Message
        for (String title : conflicts) {
            message.append("• ").append(title).append("\n");
        }

        // Add Last Section of Overlap Message
        message.append("\nAdd anyway?");

        new AlertDialog.Builder(this)
                // Set Title and Message
                .setTitle("Event Conflict")
                .setMessage(message.toString())

                // Allow users to add event anyways
                .setPositiveButton("Add Event", (d, w) -> saveEvent(event))

                // Allow users to not add the event
                .setNegativeButton("Cancel", (d, w) -> d.dismiss())

                // Display Dialog
                .show();
    }
}