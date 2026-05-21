package com.example.cs360finalprojectbyjasonbarry;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class EventDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "events.db";
    private static final int VERSION = 12;

    public EventDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static final class EventTable {
        public static final String TABLE = "events_table";
        public static final String COL_ID = "_id";
        public static final String COL_TITLE = "title";
        public static final String START_DATE = "StartDate";
        public static final String START_TIME = "StartTime";
        public static final String END_DATE = "EndDate";
        public static final String END_TIME = "EndTime";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE events_table (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "StartDate TEXT, " +
                "EndDate TEXT, " +
                "StartTime TEXT, " +
                "EndTime TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    // 'Resets' Table on Upgrade
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists " + EventTable.TABLE);
        onCreate(sqLiteDatabase);
    }

    // Reads Events from Database
    public ArrayList<Event> readEvents() {
        ArrayList<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + EventTable.TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(EventTable.COL_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(EventTable.COL_TITLE));
                @SuppressLint("Range") String startDate = cursor.getString(cursor.getColumnIndex(EventTable.START_DATE));
                @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex(EventTable.START_TIME));
                @SuppressLint("Range") String endDate = cursor.getString(cursor.getColumnIndex(EventTable.END_DATE));
                @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex(EventTable.END_TIME));
                eventList.add(new Event(title, startDate, startTime, endDate, endTime));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return eventList;
    }

    // Gets Events for Current Date
    public static ArrayList<Event> eventsForDate(Context context, String date) {

        ArrayList<Event> listOfEvents = new ArrayList<>();
        EventDatabase eventDatabase = new EventDatabase(context);
        SQLiteDatabase db = eventDatabase.getReadableDatabase();

        String sql = "select * from " + EventTable.TABLE + " where "
                + EventTable.START_DATE + " = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(EventTable.COL_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(EventTable.COL_TITLE));
                @SuppressLint("Range") String startDate = cursor.getString(cursor.getColumnIndex(EventTable.START_DATE));
                @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex(EventTable.START_TIME));
                @SuppressLint("Range") String endDate = cursor.getString(cursor.getColumnIndex(EventTable.END_DATE));
                @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex(EventTable.END_TIME));
                listOfEvents.add(new Event(title, startDate, startTime, endDate, endTime));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listOfEvents;
    }

    public boolean updateEvent(String startDate, String eventTitle) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventTable.COL_TITLE, eventTitle);

        int rowsUpdated = db.update(EventTable.TABLE, values, EventTable.START_DATE + " = ?",
                new String[] {eventTitle});
        return rowsUpdated > 0;
    }

    // DOES NOT WORK
    public boolean deleteEvent(String eventTitle) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsDeleted = db.delete(EventTable.TABLE, EventTable.COL_TITLE + " = ?",
                new String[] { eventTitle });
        return rowsDeleted > 0;
    }
}
