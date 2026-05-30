package com.example.EventCalendarMobileApplication.Calendar.View;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.EventCalendarMobileApplication.Calendar.Model.Event;
import com.example.EventCalendarMobileApplication.Calendar.ViewModel.EventViewModel;
import com.example.EventCalendarMobileApplication.Calendar_Database;
import com.example.EventCalendarMobileApplication.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NavUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class updateEventActivity extends AppCompatActivity {
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
    private Context context;
    List<String> conflicts = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);
        initWidgets();

        //Gets all Variables of Event (except IDs which are retrieved later)
        Intent intent = getIntent();
        String title = intent.getStringExtra("EVENT_TITLE");
        if (title == null) {
            title = "";
        }
        // Start Date
        String rawStartDate = intent.getStringExtra("EVENT_START_DATE");
        if (rawStartDate != null) {

            LocalDate sDate = LocalDate.parse(rawStartDate);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM dd, yyyy");

            String startDate = sDate.format(formatter);

            mStartDateEditText.setText(startDate);

        } else {
            mStartDateEditText.setText("");
        }
        // Start Time
        String rawStartTime = intent.getStringExtra("EVENT_START_TIME");
        if (rawStartTime != null) {

            LocalTime sTime = LocalTime.parse(rawStartTime);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

            String startTime = sTime.format(formatter);

            mStartTimeEditText.setText(startTime);

        } else {
            mStartTimeEditText.setText("");
        }
        // End Date
        String rawEndDate = intent.getStringExtra("EVENT_END_DATE");
        if (rawEndDate != null) {

            LocalDate eDate = LocalDate.parse(rawEndDate);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM dd, yyyy");

            String endDate = eDate.format(formatter);

            mEndDateEditText.setText(endDate);

        } else {
            mEndDateEditText.setText("");
        }
        // End Time
        String rawEndTime = intent.getStringExtra("EVENT_END_TIME");
        if (rawEndTime != null) {

            LocalTime eTime = LocalTime.parse(rawEndTime);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

            String endTime = eTime.format(formatter);

            mEndTimeEditText.setText(endTime);

        } else {
            mEndTimeEditText.setText("");
        }

        mTitleET.setText(title);

        /*
         ****************************************
         *****        ALL DAY SWITCH        *****
         ****************************************/

        mAllDaySwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            if (isChecked) {
                // On
                // Set Times for Database
                mStartTimeEditText.setText("12:00 AM");
                mEndTimeEditText.setText("11:59 PM");

                // Disables all Time Changing Buttons
                mStartTimeEditText.setEnabled(false);
                mStartTimeEditText.setVisibility(View.INVISIBLE);
                mEndTimeEditText.setEnabled(false);
                mEndTimeEditText.setVisibility(View.INVISIBLE);

                // Sets End Date to Same Day as Start Date
                mEndDateEditText.setText(String.valueOf(mStartDateEditText.getText()));


            } else {
                // Off
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

        // Listen for Start Date Edit Text Press
        mStartDateEditText.setOnClickListener(view -> {

            calendar = Calendar.getInstance();
            currentMonthNum = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);
            currentDayNum = calendar.get(Calendar.DAY_OF_MONTH);
            currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Allows User to Pick a Date
            datePickerDialog = new DatePickerDialog(updateEventActivity.this, (datePicker, year, monthOfYear, dayOfMonth) -> {
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

        // Listen for Start Time Edit Text
        mStartTimeEditText.setOnClickListener(view -> {

            // Gets Current Time
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);
            is24HourView = false;

            // Creates a Dialog that allows you to choose the Time
            timePickerDialog = new TimePickerDialog(updateEventActivity.this, (timePicker, hourOfDay, minute) -> {
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

        // Listen for End Date Edit Text Press
        mEndDateEditText.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            currentMonthNum = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);
            currentDayNum = calendar.get(Calendar.DAY_OF_MONTH);
            currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Allows User to Pick a Date
            datePickerDialog = new DatePickerDialog(updateEventActivity.this, (datePicker, year, monthOfYear, dayOfMonth) -> {

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

        // Listens for End Time Edit Text Press
        mEndTimeEditText.setOnClickListener(view -> {

            // Gets Current Time
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);
            is24HourView = false;

            // Creates a Dialog that allows you to choose the Time
            timePickerDialog = new TimePickerDialog(updateEventActivity.this, (timePicker, hourOfDay, minute) -> {
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
        mTitleET = findViewById(R.id.updateEventTitle);
        mAllDaySwitch = findViewById(R.id.updateAllDaySwitch);
        mStartDateEditText = findViewById(R.id.updateStartDateEditText);
        mStartTimeEditText = findViewById(R.id.updateStartTimeEditText);
        mEndDateEditText = findViewById(R.id.updateEndDateEditText);
        mEndTimeEditText = findViewById(R.id.updateEndTimeEditText);
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

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_event_menu, menu);

        /*
         *************************************
         *****        SAVE BUTTON        *****
         *************************************
         */

        MenuItem item = menu.findItem(R.id.saveButtonSymbolInEditLayout);
        View actionView = item.getActionView();

        if (actionView != null) {

            Button saveButton = actionView.findViewById(R.id.saveButton);

            saveButton.setOnClickListener(v -> {
                if (item.getItemId() == R.id.saveButtonSymbolInEditLayout) {

                    // Transforms everything back into strings (instead of Edit Texts)
                    String mTitle = mTitleET.getText().toString();
                    String startDate = mStartDateEditText.getText().toString();
                    String endDate = mEndDateEditText.getText().toString();
                    String startTime = mStartTimeEditText.getText().toString();
                    String endTime = mEndTimeEditText.getText().toString();

                    // Gets Dates for Checking if the End Date is after the Start Date
                    DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("E, MMM dd, yyyy");
                    LocalDate parsedStartDate = LocalDate.parse(startDate, displayFormatter);
                    LocalDate parsedEndDate = LocalDate.parse(endDate, displayFormatter);

                    // If the End Date is before the Start Date
                    if (parsedEndDate.isBefore(parsedStartDate)) {
                        // Displays Error Message
                        Toast.makeText(updateEventActivity.this, "End date must be after start date.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Formats the Start and End Dates so they're ready for the Database
                    DateTimeFormatter databaseFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                    startDate = parsedStartDate.format(databaseFormatter);
                    endDate = parsedEndDate.format(databaseFormatter);

                    // Gets Times for Checking if the End Time is after the Start Time
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                    LocalTime sTime = LocalTime.parse(startTime, formatter);
                    LocalTime eTime = LocalTime.parse(endTime, formatter);

                    // If the End Time is before the Start Time
                    if (eTime.isBefore(sTime)) {
                        // Displays Error Message
                        Toast.makeText(updateEventActivity.this, "End time must be after start time.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Gives a Default Event Name if none was entered
                    if (mTitle.isEmpty()){
                        mTitle = "Event on " + startDate + " at " + startTime;
                    }

                    // Transforms Dates and Times into Date Time objects (for Overlap Checking)
                    LocalDateTime newStart = LocalDateTime.of(parsedStartDate, sTime);
                    LocalDateTime newEnd = LocalDateTime.of(parsedEndDate, eTime);

                    // Put Updated Values
                    ContentValues values = new ContentValues();
                    values.put(Calendar_Database.EventTable.COL_TITLE, mTitle);
                    values.put(Calendar_Database.EventTable.START_DATE, startDate);
                    values.put(Calendar_Database.EventTable.END_DATE, endDate);
                    values.put(Calendar_Database.EventTable.START_TIME, startTime);
                    values.put(Calendar_Database.EventTable.END_TIME, endTime);

                    // Creates a List of Events based on the Start Date
                    EventViewModel eventViewModel = new EventViewModel(this);
                    ArrayList<Event> listOfEvents = eventViewModel.readEvents();

                    // Searches through the Event List
                    for (Event e : listOfEvents) {

                        // Gets the Start Date and Start Time of Each Event in the List
                        LocalDateTime existingStart = LocalDateTime.of(e.getStartDate(), e.getStartTime());

                        // Gets the End Date and End Time of Each Event in the List
                        LocalDateTime existingEnd = LocalDateTime.of(e.getEndDate(), e.getEndTime());

                        // Checks if an Overlap Occurs
                        boolean overlaps = newStart.isBefore((existingEnd)) && newEnd.isAfter((existingStart));

                        // If an overlap occurs
                        if (overlaps) {
                            conflicts.add(e.getTitle());
                        }
                    }
                    // If overlaps occur
                    if (!conflicts.isEmpty()){
                        // Build Error Message
                        StringBuilder message = new StringBuilder();

                        message.append("This event overlaps with:\n\n");

                        // Display All Events which conflict
                        for (String title : conflicts) { message.append("• ").append(title).append("\n");}

                        message.append("\nUpdate anyway?");

                        // Displays Conflict Alert Dialog Box
                        new AlertDialog.Builder(updateEventActivity.this)
                                .setTitle("Event Conflict")
                                .setMessage(message.toString())
                                // Sets the Positive Button if the User still WANTS to Save the Event
                                .setPositiveButton("Update Event", (dialog, which) -> {saveEvent(values);})
                                // Sets the Negative Button if the User DOESN'T WANT to Save the Event
                                .setNegativeButton("Cancel", (dialog, which) -> {dialog.dismiss();})
                                .show();

                        return;
                    }
                    saveEvent(values);
                }
            });
        }

        /*
         ***************************************
         *****        DELETE BUTTON        *****
         ***************************************
         */

        MenuItem deleteItem = menu.findItem(R.id.deleteMenuButton);
        View deleteActionView = deleteItem.getActionView();

        if (deleteActionView != null) {

            Button deleteButton = deleteActionView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(v -> {
                    // Get the IDs for the Event to Delete
                    Intent intent = getIntent();
                    int eventID = intent.getIntExtra("EVENT_ID", -1);
                    int userID = intent.getIntExtra("EVENT_USER_ID", -1);

                    // Open Database
                    Calendar_Database calendarDatabase = new Calendar_Database(this);
                    SQLiteDatabase db = calendarDatabase.getWritableDatabase();

                    // Where the Event and User IDs Correspond
                    String whereClause = "_id=? AND user_id=?";
                    String[] whereArgs = {
                            String.valueOf(eventID),
                            String.valueOf(userID)
                    };

                    // Update the Event
                    db.delete(
                            Calendar_Database.EventTable.TABLE,
                            whereClause,
                            whereArgs
                    );

                    // Close the Database
                    db.close();

                    // Return to Calendar
                    Intent exitActivityIntent = new Intent(this, activity_calendar_month.class);
                    startActivity(exitActivityIntent);
            });
        }
        return true;
    }

    private void saveEvent(ContentValues values) {

        Calendar_Database calendarDatabase = new Calendar_Database(this);

        // Get the IDs for the Event to Delete
        Intent intent = getIntent();
        int eventID = intent.getIntExtra("EVENT_ID", -1);
        int userID = intent.getIntExtra("EVENT_USER_ID", -1);

        // Opens the Database
        SQLiteDatabase db = calendarDatabase.getWritableDatabase();

        // Where the Event and User IDs Correspond
        String whereClause = "_id=? AND user_id=?";
        String[] whereArgs = {
                String.valueOf(eventID),
                String.valueOf(userID)
        };

        // Update the Event
        db.update(Calendar_Database.EventTable.TABLE, values, whereClause, whereArgs);

        // Closes the Database
        db.close();

        // Returns to the Monthly Calendar
        Intent returnToCalendarIntent = new Intent(this, activity_calendar_month.class);
        startActivity(returnToCalendarIntent);
    }
}
