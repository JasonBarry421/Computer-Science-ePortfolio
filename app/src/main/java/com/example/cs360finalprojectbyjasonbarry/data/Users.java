package com.example.cs360finalprojectbyjasonbarry.data;

public class Users {
    String mUsername;
    String mPassword;
    String mPhoneNumber;

    public Users(String username, String password){
        mUsername = username;
        mPassword = password;
    }

    public Users(String username, String password, String phoneNumber){
        mUsername = username;
        mPassword = password;
        mPhoneNumber = phoneNumber;
    }

    public String getUsername(){
        return mUsername;
    }

    public String getPassword(){
        return mPassword;
    }

    public String getPhoneNumber(){
        return mPhoneNumber;
    }

}