package com.example.EventCalendarMobileApplication.Calendar.Repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.EventCalendarMobileApplication.Calendar_Database;
import com.example.EventCalendarMobileApplication.Calendar.Model.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EventRepository {

    private Calendar_Database calendarDatabase;
    private Context context;
    private final Calendar_Database dbHelper;

    public EventRepository(Context context) {
        dbHelper = new Calendar_Database(context);
    }

    public void addEvent(Event event){
        eventsList.add(event);
    }

    public ArrayList<Event> readEvents(int userId) {

        ArrayList<Event> eventList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + Calendar_Database.EventTable.TABLE +
                        " WHERE " +
                        Calendar_Database.EventTable.COL_USER_ID + " = ?",

                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                Calendar_Database.EventTable.COL_EVENT_ID));

                int retrievedUserId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                Calendar_Database.EventTable.COL_USER_ID));

                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                Calendar_Database.EventTable.COL_TITLE));

                // Gets the Start Date with Desired Formatting
                @SuppressLint("Range") LocalDate startDate =
                        LocalDate.parse(
                                cursor.getString(
                                        cursor.getColumnIndex(
                                                Calendar_Database.EventTable.START_DATE
                                        )
                                ),
                                DateTimeFormatter.ofPattern("MMM dd, yyyy")
                        );

                // Deals with Scenarios where the Time has an Additional Space
                @SuppressLint("Range") String rawStartTime =
                        cursor.getString(
                                cursor.getColumnIndex(Calendar_Database.EventTable.START_TIME)
                        ).trim();

                // Gets the Start Time with Desired Formatting
                LocalTime startTime =
                        LocalTime.parse(
                                rawStartTime,
                                DateTimeFormatter.ofPattern("h:mm a")
                        );

                // Gets the End Date with Desired Formatting
                @SuppressLint("Range") LocalDate endDate =
                        LocalDate.parse(
                                cursor.getString(
                                        cursor.getColumnIndex(
                                                Calendar_Database.EventTable.END_DATE
                                        )
                                ),
                                DateTimeFormatter.ofPattern("MMM dd, yyyy")
                        );

                // Deals with Scenarios where the Time has an Additional Space
                @SuppressLint("Range") String rawEndTime =
                        cursor.getString(
                                cursor.getColumnIndex(Calendar_Database.EventTable.END_TIME)
                        ).trim();

                // Gets the End Time with Desired Formatting
                LocalTime endTime =
                        LocalTime.parse(
                                rawEndTime,
                                DateTimeFormatter.ofPattern("h:mm a")
                        );

                eventList.add(new Event(
                        id,
                        retrievedUserId,
                        title,
                        startDate,
                        startTime,
                        endDate,
                        endTime
                ));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return eventList;
    }

    public ArrayList<Event> eventsForDate(String date, int userId) {

        ArrayList<Event> listOfEvents = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(

                "SELECT * FROM " +
                        Calendar_Database.EventTable.TABLE +

                        " WHERE " +
                        Calendar_Database.EventTable.START_DATE + " = ?" +

                        " AND " +
                        Calendar_Database.EventTable.COL_USER_ID + " = ?",

                new String[]{
                        date,
                        String.valueOf(userId)
                }
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                Calendar_Database.EventTable.COL_EVENT_ID));

                int retrievedUserId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                Calendar_Database.EventTable.COL_USER_ID));

                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                Calendar_Database.EventTable.COL_TITLE));

                // Gets the Start Date with Desired Formatting
                @SuppressLint("Range") LocalDate startDate =
                        LocalDate.parse(
                                cursor.getString(
                                        cursor.getColumnIndex(
                                                Calendar_Database.EventTable.START_DATE
                                        )
                                ),
                                DateTimeFormatter.ofPattern("MMM dd, yyyy")
                        );

                // Deals with Scenarios where the Time has an Additional Space
                @SuppressLint("Range") String rawStartTime =
                        cursor.getString(
                                cursor.getColumnIndex(Calendar_Database.EventTable.START_TIME)
                        ).trim();

                // Gets the Start Time with Desired Formatting
                LocalTime startTime =
                        LocalTime.parse(
                                rawStartTime,
                                DateTimeFormatter.ofPattern("h:mm a")
                        );

                // Gets the End Date with Desired Formatting
                @SuppressLint("Range") LocalDate endDate =
                        LocalDate.parse(
                                cursor.getString(
                                        cursor.getColumnIndex(
                                                Calendar_Database.EventTable.END_DATE
                                        )
                                ),
                                DateTimeFormatter.ofPattern("MMM dd, yyyy")
                        );
                
                // Deals with Scenarios where the Time has an Additional Space
                @SuppressLint("Range") String rawEndTime =
                        cursor.getString(
                                cursor.getColumnIndex(Calendar_Database.EventTable.END_TIME)
                        ).trim();

                // Gets the End Time with Desired Formatting
                LocalTime endTime =
                        LocalTime.parse(
                                rawEndTime,
                                DateTimeFormatter.ofPattern("h:mm a")
                        );

                listOfEvents.add(new Event(
                        id,
                        retrievedUserId,
                        title,
                        startDate,
                        startTime,
                        endDate,
                        endTime
                ));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listOfEvents;
    }

    public static ArrayList<Event> eventsList = new ArrayList<>();

}