package com.example.cs360finalprojectbyjasonbarry.ui.login;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cs360finalprojectbyjasonbarry.data.Users;

import java.util.ArrayList;

public class LoginDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "login.db";
    private static final int VERSION = 3;

    public LoginDatabase(Context context){
        super(context, DATABASE_NAME, null, VERSION);

        //addInitialUser();
    }

    public static final class LoginTable{
        public static final String TABLE = "login_table";
        public static final String COL_ID = "_id";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String PHONE_NUMBER = "phoneNumber";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE login_table (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " + "password TEXT, " + "phoneNumber TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists " + LoginTable.TABLE);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Users> readUsers(){

        ArrayList<Users> usersArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + LoginTable.TABLE, null);

        if (cursor.moveToFirst()){
            do{
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"));
                usersArrayList.add(new Users(username, password, phoneNumber));
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (usersArrayList.isEmpty()){
            addInitialUser();
        }
        return usersArrayList;
    }

    // FOR DEBUGGING
    public long addInitialUser() {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(LoginTable.USERNAME, "example@example.com");
        values.put(LoginTable.PASSWORD, "example");
        values.put(LoginTable.PHONE_NUMBER, "1234567890");

        return db.insert(LoginDatabase.LoginTable.TABLE, null, values);
    }

    public boolean addUsers(String username, String password, String phoneNumber){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LoginTable.USERNAME, username);
        values.put(LoginTable.PASSWORD, password);
        values.put(LoginTable.PHONE_NUMBER, phoneNumber);

        db.insert(LoginDatabase.LoginTable.TABLE, null, values);

        return true;
    }
}