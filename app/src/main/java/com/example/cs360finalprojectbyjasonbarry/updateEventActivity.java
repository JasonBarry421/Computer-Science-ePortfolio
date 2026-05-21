package com.example.cs360finalprojectbyjasonbarry;

import static com.example.cs360finalprojectbyjasonbarry.CalendarStuff.formatString;
import static com.example.cs360finalprojectbyjasonbarry.CalendarStuff.selectedDate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NavUtils;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    String day;
    String amPM;
    private LocalDate selectedDate;
    private EventDatabase eventDatabase;
    private Context context;

//    public updateEventActivity(@NonNull Context context, List<Event> events)
//    {
//        super(context, 0, events);
//        this.context = context;
//    }


    // NEEDS FIX
    //              - Needs to be auto-populated with event info
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        initWidgets();

        /*
         ****************************************
         *****        ALL DAY SWITCH        *****
         ****************************************/

        mAllDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    //on
                    // Disables all Time Changing Buttons
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

        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date curStartDateAsDate = Date.from(curStartDate.atStartOfDay(defaultZoneId).toInstant());

        // Listen for Start Date Edit Text Press
        mStartDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                currentMonthNum = calendar.get(Calendar.MONTH);
                currentYear = calendar.get(Calendar.YEAR);
                currentDayNum = calendar.get(Calendar.DAY_OF_MONTH);
                currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                // Allows User to Pick a Date
                datePickerDialog = new DatePickerDialog(updateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
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
                    }
                }, currentYear, currentMonthNum, currentDayNum);
                datePickerDialog.show();
            }
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
        mStartTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Gets Current Time
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);
                is24HourView = false;

                // Creates a Dialog that allows you to choose the Time
                timePickerDialog = new TimePickerDialog(updateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
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
                        String minuteString = String.format("%02d", minute);

                        // Set Chosen Time
                        String time = hourOfDay + " : " + minuteString + " " + amPM;

                        mStartTimeEditText.setText(time);
                    }
                }, currentHour, currentMinute, is24HourView);
                timePickerDialog.show();
            }
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
        mEndDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentMonthNum = calendar.get(Calendar.MONTH);
                currentYear = calendar.get(Calendar.YEAR);
                currentDayNum = calendar.get(Calendar.DAY_OF_MONTH);
                currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                // Allows User to Pick a Date
                datePickerDialog = new DatePickerDialog(updateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

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
                    }
                }, currentYear, currentMonthNum, currentDayNum);
                datePickerDialog.show();
            }
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
        mEndTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Gets Current Time
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);
                is24HourView = false;

                // Creates a Dialog that allows you to choose the Time
                timePickerDialog = new TimePickerDialog(updateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
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
                        String minuteString = String.format("%02d", minute);

                        // Set Chosen Time
                        String time = hourOfDay + " : " + minuteString + " " + amPM;
                        mEndTimeEditText.setText(time);
                    }
                }, currentHour, currentMinute, is24HourView);
                timePickerDialog.show();
            }
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
        if (item.getItemId() == R.id.saveButtonSymbol) {

            String mTitle = mTitleET.getText().toString();
            String startDate = mStartDateEditText.getText().toString();

            startDate = startDate.replace("Mon, ", "");
            startDate = startDate.replace("Tue, ", "");
            startDate = startDate.replace("Wed, ", "");
            startDate = startDate.replace("Thu, ", "");
            startDate = startDate.replace("Fri, ", "");
            startDate = startDate.replace("Sat, ", "");
            startDate = startDate.replace("Sun, ", "");

            String endDate = mEndDateEditText.getText().toString();

            endDate = endDate.replace("Mon, ", "");
            endDate = endDate.replace("Tue, ", "");
            endDate = endDate.replace("Wed, ", "");
            endDate = endDate.replace("Thu, ", "");
            endDate = endDate.replace("Fri, ", "");
            endDate = endDate.replace("Sat, ", "");
            endDate = startDate.replace("Sun, ", "");

            String startTime = mStartTimeEditText.getText().toString() + " ";

            String endTime = mEndTimeEditText.getText().toString() + " ";

            Event event = new Event(mTitle, startDate, endDate, startTime, endTime);

            eventDatabase = new EventDatabase(this);

            SQLiteDatabase db = eventDatabase.getWritableDatabase();
            ContentValues values = new ContentValues();

            String sqlTitle = EventDatabase.EventTable.COL_TITLE;
            String sqlStartDate = EventDatabase.EventTable.START_DATE;
            String sqlEndDate = EventDatabase.EventTable.END_DATE;
            String sqlStartTime = EventDatabase.EventTable.START_TIME;
            String sqlEndTime = EventDatabase.EventTable.END_TIME;

            values.put(sqlTitle, mTitle);
            values.put(sqlStartDate, startDate);
            values.put(sqlEndDate, endDate);
            values.put(sqlStartTime, startTime);
            values.put(sqlEndTime, endTime);

            String sqlTableName = EventDatabase.EventTable.TABLE;
            long newRowId = db.insert(sqlTableName, null, values);

            db.close();

            Intent intent = new Intent(this, activity_calendar_month.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_event_menu, menu);

        MenuItem item = menu.findItem(R.id.saveButtonSymbol);
        Button button = (Button) item.getActionView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.performIdentifierAction(item.getItemId(), 0);
            }
        });
        return true;
    }
}
//    public void updateEvent(String title, String startDate, String startTime, String endDate, String endTime){
//
//        // Get Event at current Position
//        Event event = getItem(position);
//        String eventDate = formatString(event.getStartDate());
//
//        TextView eventTitleTV = convertView.findViewById(R.id.eventTextView);
//
//        // Read Events from Database
//        EventDatabase eventDatabase = new EventDatabase(context);
//        List<Event> events = eventDatabase.readEvents();
//
//        // If there is an Event on the Selected Date
//        if (eventDate.equals(String.valueOf(selectedDate))) {
//            String eventTitle = event.getTitle();
//            String startDate = event.getStartDate();
//            String startTime = event.getStartTime();
//            String endDate = event.getEndDate();
//            String endTime = event.getEndTime();
//
//            // Outputs Event
//            mTitleET.setText(eventTitle);
//            mStartDateEditText.setText(startDate);
//            mStartTimeEditText.setText(startTime);
//            mEndDateEditText.setText(endDate);
//            mEndTimeEditText.setText(startTime);
//        } else{
//            eventTitleTV.setText("");
//        }
//        eventDatabase.close();
//    }
//}