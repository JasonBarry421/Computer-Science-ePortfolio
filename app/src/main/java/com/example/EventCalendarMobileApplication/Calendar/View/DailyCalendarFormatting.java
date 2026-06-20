package com.example.EventCalendarMobileApplication.Calendar.View;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DailyCalendarFormatting {

    // Gets Month Day for the Top of the Calendar
    public static String monthDayFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
        return date.format(formatter);
    }

    // Gets the Hours for Each Day
    public static ArrayList<LocalTime> hoursInDayArray() {

        ArrayList<LocalTime> hours = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            hours.add(LocalTime.of(i,0));
        }

        return hours;
    }
}
