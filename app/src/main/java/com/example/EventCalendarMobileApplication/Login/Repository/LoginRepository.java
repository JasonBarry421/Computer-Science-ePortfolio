package com.example.EventCalendarMobileApplication.Login.Repository;

import android.content.Context;
import com.example.EventCalendarMobileApplication.Calendar_Database;

public class LoginRepository {
    private static volatile LoginRepository instance;
    private Calendar_Database calendarDatabase;
    private Context context;

    // Constructor
    public LoginRepository(Context context) {
        this.context = context;
    }

    public int authenticateUser(String username, String password) {
        calendarDatabase = new Calendar_Database(context);
        return calendarDatabase.authenticateUser(username, password);
    }

    public Boolean createAccount(String username, String password, String phoneNumber) {

        Calendar_Database calendarDB = new Calendar_Database(context);

        if (calendarDB.userExists(username, phoneNumber)) {
            return false;
        }

        calendarDB.addUsers(username, password, phoneNumber);

        calendarDB.close();

        return true;
    }
}