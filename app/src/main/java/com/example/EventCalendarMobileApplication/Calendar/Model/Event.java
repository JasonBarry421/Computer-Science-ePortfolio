package com.example.EventCalendarMobileApplication.Calendar.Model;


public class Event {

    private String mEventID;
    private String mUser_ID;
    private String mTitleString;
    private String mStartDate;
    private String mStartTime;
    private String mEndDate;
    private String mEndTime;

    public Event() {
        // Required by Firestore
    }

    public Event(
            String eventID,
            String userID,
            String title,
            String startDate,
            String startTime,
            String endDate,
            String endTime) {

        this.mEventID = eventID;
        this.mUser_ID = userID;
        this.mTitleString = title;
        this.mStartDate = startDate;
        this.mStartTime = startTime;
        this.mEndDate = endDate;
        this.mEndTime = endTime;
    }

    // getters and setters
    public String getEventID(){
        return mEventID;
    }
    public void setEventID(String eventID){ this.mEventID = eventID; }
    public String getUserID(){
        return mUser_ID;
    }
    public void setUserID(String userID){
        this.mUser_ID = userID;
    }

    public String getTitle(){
        return mTitleString;
    }

    public void setTitle(String title){
        this.mTitleString = title;
    }
    public String getStartDate() {
        return mStartDate;
    }
    public void setStartDate(String startDate){
        this.mStartDate = startDate;
    }
    public String getEndDate() {
        return mEndDate;
    }
    public void setEndDate(String endDate){
        this.mEndDate = endDate;
    }
    public String getStartTime() {
        return mStartTime;
    }
    public void setStartTime(String startTime){
        this.mStartTime = startTime;
    }
    public String getEndTime() {
        return mEndTime;
    }
    public void setEndTime(String endTime){
        this.mEndTime = endTime;
    }
}