package com.example.EventCalendarMobileApplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.EventCalendarMobileApplication.Calendar.Model.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Calendar_Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SQLite_Database.db";
    private static final int VERSION = 8;

    public Calendar_Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createUsersTable =
                "CREATE TABLE " + LoginTable.TABLE + " (" +
                        LoginTable.COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        LoginTable.USERNAME + " TEXT, " +
                        LoginTable.PASSWORD + " TEXT, " +
                        LoginTable.PHONE_NUMBER + " TEXT)";

        sqLiteDatabase.execSQL(createUsersTable);

        String createEventsTable =
                "CREATE TABLE " + EventTable.TABLE + " (" +
                        EventTable.COL_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        EventTable.COL_USER_ID + " INTEGER, " +
                        EventTable.COL_TITLE + " TEXT, " +
                        EventTable.START_DATE + " TEXT, " +
                        EventTable.START_TIME + " TEXT, " +
                        EventTable.END_DATE + " TEXT, " +
                        EventTable.END_TIME + " TEXT)";

        sqLiteDatabase.execSQL(createEventsTable);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + LoginTable.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EventTable.TABLE);

        onCreate(db);
    }

    /******************************
    ***       LOGIN TABLE       ***
    *******************************/
    public static final class LoginTable{
        public static final String TABLE = "login_table";
        public static final String COL_USER_ID = "_id";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String PHONE_NUMBER = "phoneNumber";
    }

    // Checks if the User already has an account based on their User Name OR Phone Number
    public boolean userExists(String username, String phoneNumber) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM " + LoginTable.TABLE +
                        " WHERE username = ? OR phoneNumber = ?",
                new String[]{username, phoneNumber}
        );

        boolean exists = cursor.moveToFirst();

        cursor.close();

        return exists;
    }


    // Authenticate Users when Logging in
    public int authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT _id FROM " + LoginTable.TABLE + " WHERE username = ? AND password = ?", new String[]{username, password});

        int userID = -1;

        if (cursor.moveToFirst()) {
            userID = cursor.getInt(0);
        }

        cursor.close();
        return userID;
    }

    public boolean addUsers(String username, String password, String phoneNumber){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LoginTable.USERNAME, username);
        values.put(LoginTable.PASSWORD, password);
        values.put(LoginTable.PHONE_NUMBER, phoneNumber);

        db.insert(LoginTable.TABLE, null, values);

        return true;
    }


    /********************************
     ***       EVENTS TABLE       ***
     ********************************/
    public static final class EventTable {
        public static final String TABLE = "events_table";
        public static final String COL_EVENT_ID  = "_id";
        public static final String COL_USER_ID = "user_id";
        public static final String COL_TITLE = "title";
        public static final String START_DATE = "StartDate";
        public static final String START_TIME = "StartTime";
        public static final String END_DATE = "EndDate";
        public static final String END_TIME = "EndTime";
    }

    // Reads Events from Database
    public ArrayList<Event> readEvents() {
        ArrayList<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + EventTable.TABLE +
                        " WHERE " + EventTable.COL_USER_ID + " = ?",
                new String[]{String.valueOf(Session.currentUserId)}
        );

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(EventTable.COL_EVENT_ID ));
                @SuppressLint("Range") int userID = cursor.getInt(cursor.getColumnIndex(EventTable.COL_USER_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(EventTable.COL_TITLE));
                @SuppressLint("Range") LocalDate startDate =
                        LocalDate.parse(
                                cursor.getString(
                                        cursor.getColumnIndex(
                                                EventTable.START_DATE
                                        )
                                ),
                                DateTimeFormatter.ofPattern("MMM dd, yyyy")
                        );
                @SuppressLint("Range") LocalTime startTime =
                        LocalTime.parse(
                                cursor.getString(
                                        cursor.getColumnIndex(
                                                EventTable.START_TIME
                                        )
                                ),
                                DateTimeFormatter.ofPattern("h:mm a")
                        );
                @SuppressLint("Range") LocalDate endDate =
                        LocalDate.parse(
                                cursor.getString(
                                        cursor.getColumnIndex(
                                                EventTable.END_DATE
                                        )
                                ),
                                DateTimeFormatter.ofPattern("MMM dd, yyyy")
                        );
                @SuppressLint("Range") LocalTime endTime =
                        LocalTime.parse(
                                cursor.getString(
                                        cursor.getColumnIndex(
                                                EventTable.END_TIME
                                        )
                                ),
                                DateTimeFormatter.ofPattern("h:mm a")
                        );
                eventList.add(new Event(id, userID, title, startDate, startTime, endDate, endTime));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return eventList;
    }

    // Gets Events for Current Date
    public static ArrayList<Event> eventsForDate(Context context, String date) {

        ArrayList<Event> listOfEvents = new ArrayList<>();
        Calendar_Database calendarDatabase = new Calendar_Database(context);
        SQLiteDatabase db = calendarDatabase.getReadableDatabase();

        String sql = "select * from " + EventTable.TABLE + " where " + EventTable.START_DATE + " = ?";

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + EventTable.TABLE +
                        " WHERE " + EventTable.COL_USER_ID + " = ?",
                new String[]{String.valueOf(Session.currentUserId)}
        );

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(EventTable.COL_EVENT_ID));
                @SuppressLint("Range") int userID = cursor.getInt(cursor.getColumnIndex(EventTable.COL_USER_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(EventTable.COL_TITLE));
                @SuppressLint("Range") LocalDate startDate =
                        LocalDate.parse(
                                cursor.getString(
                                        cursor.getColumnIndex(
                                                EventTable.START_DATE
                                        )
                                ),
                                DateTimeFormatter.ofPattern("MMM dd, yyyy")
                        );
                @SuppressLint("Range") String rawStartTime =
                        cursor.getString(
                                cursor.getColumnIndex(EventTable.START_TIME)
                        ).trim();

                LocalTime startTime =
                        LocalTime.parse(
                                rawStartTime,
                                DateTimeFormatter.ofPattern("h:mm a")
                        );
                @SuppressLint("Range") LocalDate endDate =
                        LocalDate.parse(
                                cursor.getString(
                                        cursor.getColumnIndex(
                                                EventTable.END_DATE
                                        )
                                ),
                                DateTimeFormatter.ofPattern("MMM dd, yyyy")
                        );
                @SuppressLint("Range") String rawEndTime =
                        cursor.getString(
                                cursor.getColumnIndex(EventTable.END_TIME)
                        ).trim();

                LocalTime endTime =
                        LocalTime.parse(
                                rawEndTime,
                                DateTimeFormatter.ofPattern("h:mm a")
                        );

                listOfEvents.add(new Event(id, userID, title, startDate, startTime, endDate, endTime));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listOfEvents;
    }
}
