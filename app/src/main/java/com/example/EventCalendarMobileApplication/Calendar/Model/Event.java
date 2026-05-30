package com.example.EventCalendarMobileApplication.Calendar.Model;
import android.content.Context;

import com.example.EventCalendarMobileApplication.Calendar_Database;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private Calendar_Database calendarDatabase;
    private Context context;

    public Event (Context context){
        calendarDatabase = new Calendar_Database(context);
    }

    public int mID;
    public int mUser_ID;
    public String mTitleString;
    public LocalDate mStartDate;
    public LocalDate mEndDate;
    public LocalTime mStartTime;
    public LocalTime mEndTime;

    public Event(int id, int userID, String title, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime){
        mID = id;
        mUser_ID = userID;
        mTitleString = title;
        mStartDate = startDate;
        mStartTime = startTime;
        mEndDate = endDate;
        mEndTime = endTime;
    }


    public int getID(){
        return mID;
    }
    public void setID(int id){ this.mID = id; }
    public int getUserID(){
        return mUser_ID;
    }
    public void setUserID(int userID){
        this.mUser_ID = userID;
    }

    public String getTitle(){
        return mTitleString;
    }

    public void setTitle(String title){
        this.mTitleString = title;
    }
    public LocalDate getStartDate() {
        return mStartDate;
    }
    public void setStartDate(LocalDate startDate){
        this.mStartDate = startDate;
    }
    public LocalDate getEndDate() {
        return mEndDate;
    }
    public void setEndDate(LocalDate endDate){
        this.mEndDate = endDate;
    }
    public LocalTime getStartTime() {
        return mStartTime;
    }
    public void setStartTime(LocalTime startTime){
        this.mStartTime = startTime;
    }
    public LocalTime getEndTime() {
        return mEndTime;
    }
    public void setEndTime(LocalTime endTime){
        this.mEndTime = endTime;
    }
}