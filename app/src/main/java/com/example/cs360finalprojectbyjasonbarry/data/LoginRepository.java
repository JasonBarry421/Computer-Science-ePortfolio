package com.example.cs360finalprojectbyjasonbarry.data;

import android.content.Context;
import android.util.Log;

import com.example.cs360finalprojectbyjasonbarry.data.model.LoggedInUser;
import com.example.cs360finalprojectbyjasonbarry.ui.login.LoginActivity;
import com.example.cs360finalprojectbyjasonbarry.ui.login.LoginDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
   // private LoginDataSource dataSource;
    private LoggedInUser user = null;
    public ArrayList<Users> mUserList = new ArrayList<Users>();
    ;
    private LoginDatabase loginDatabase;
    private Context context;

    // Constructor
    public LoginRepository(Context context) {
        this.context = context;

        // Get usersList from the Login Database
        LoginDatabase loginDB = new LoginDatabase(context);
        ArrayList<Users> usersList = loginDB.readUsers();

        // Adds data from database to ArrayList
        for (Users u : usersList) {
            Log.i("USERS", "Username: " + u.getUsername() + "\nPassword: " + u.getPassword()
                    + "\nPhone Number: " + u.getPhoneNumber());
            mUserList.add(u);
        }

        // Closes Database
        loginDB.close();
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        //dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    // Enables Login
    public Boolean login(String username, String password) {
        for (Users user : mUserList) {
            // If user in mUserList
            if ((Objects.equals(username, user.getUsername())) && (Objects.equals(password, user.getPassword()))) {
                return true;
            }
        }
        return false;
    }

    // Enables Creation of a New Account
    public Boolean createAccount(String username, String password, String phoneNumber) {

        // Declares a New User
        Users attemptNewUser = new Users(username, password, phoneNumber);

        // Get usersList from the Login Database
        LoginDatabase loginDB = new LoginDatabase(context);
        ArrayList<Users> usersList = loginDB.readUsers();

        // For user in usersList
        for (Users u : usersList) {
            // If user already in userList
            if (((Objects.equals(username, u.getUsername())) && (Objects.equals(password, u.getPassword())))
                    || (Objects.equals(phoneNumber, u.getPhoneNumber()))) {
                return false;
            }
        }

        // Adds User
        loginDB.addUsers(username, password, phoneNumber);
        usersList.add(attemptNewUser);

        // Closes Database
        loginDB.close();

        return true;
    }
}