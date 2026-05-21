package com.example.cs360finalprojectbyjasonbarry;
import android.content.Context;

import java.util.ArrayList;

public class Event {
    private static EventDatabase eventDatabase;
    public static ArrayList<Event> eventsList = new ArrayList<>();
    private static Context context;

    public Event (Context context){
        eventDatabase = new EventDatabase(context);
    }

    public String mTitleString;
    public String mStartDateString;
    public String mEndDateString;
    public String mStartTimeString;
    public String mEndTimeString;


    public Event(String title, String startDate, String startTime, String endDate, String endTime) {
        mTitleString = title;
        mStartDateString = startDate;
        mStartTimeString = startTime;
        mEndDateString = endDate;
        mEndTimeString = endTime;
    }

    public void addEvent(Event event){
        eventsList.add(event);
    }

    public String getTitle(){
        return mTitleString;
    }

    public void setTitle(String title){
        this.mTitleString = title;
    }

    public String getStartDate(){
        return mStartDateString;
    }

    public void setStartDate(String startDate){
        this.mStartDateString = startDate;
    }

    public String getEndDate(){
        return mEndDateString;
    }

    public void setEndDate(String mEndDate){
        this.mEndDateString = mEndDate;
    }

    public String getStartTime(){
        return mStartTimeString;
    }

    public void setStartTime(String startTime){
        this.mStartTimeString = startTime;
    }

    public String getEndTime(){
        return mEndTimeString;
    }

    public void setEndTime(String endTime){
        this.mEndTimeString = endTime;
    }
}