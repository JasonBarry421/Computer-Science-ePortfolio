package com.example.cs360finalprojectbyjasonbarry;

import android.util.Log;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarStuff {
    public static LocalDate selectedDate;

    // Gets Month Year for the Top of the Calendar
    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        return date.format(formatter);
    }

    // Gets the Days for Each Month
    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate prevMonth = selectedDate.minusMonths(1);
        int daysInPreviousMonth = prevMonth.lengthOfMonth();

        // FIRST DAY OF WEEK = SUNDAY
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        // Deals with an Edge Case where the FirstOfMonth is on a Sunday
        if (firstOfMonth.getDayOfWeek().getValue() == 7) {
            for (int i = 1; i <= 42; i++) {
                if (i > daysInMonth) {
                    // Adds Next Month Days to Array
                    daysInMonthArray.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth().plus(1), i - daysInMonth));
                } else {
                    // Adds Current Month Days to Array
                    daysInMonthArray.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i));
                }
            }

        } else {
            for (int i = 1; i <= 42; i++) {

                if (i <= dayOfWeek) {
                    // Gets Days for Previous Month
                    int day = daysInPreviousMonth - dayOfWeek + i;

                    // Adds Previous Month Days to Array
                    daysInMonthArray.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth().minus(1), day));
                } else if (i > dayOfWeek + daysInMonth) {
                    // Adds Next Month Days to Array
                    daysInMonthArray.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth().plus(1), i - dayOfWeek - daysInMonth));
                } else {
                    // Adds Current Month Days to Array
                    daysInMonthArray.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i - dayOfWeek));
                }
            }
        }
        return daysInMonthArray;
    }

    // FIRST DAY OF WEEK = MONDAY
    //  LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
    //  int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() - 1;
    //
        /*
        // Deals with an Edge Case where the FirstOfMonth is on a Monday
        if (firstOfMonth.getDayOfWeek().getValue() == 1) {
            for (int i = 1; i <= 42; i++){
                if (i > daysInMonth){
                    daysInMonthArray.add(String.valueOf(i - daysInMonth));
                }else{
                    daysInMonthArray.add(String.valueOf(i));
                }
            }

        }else{
            for(int i = 1; i <= 42; i++){

                if (i <= dayOfWeek){
                    // Gets Days for Previous Month
                    int day = daysInPreviousMonth - dayOfWeek + i;

                    // Adds Previous Month Days to Array
                    daysInMonthArray.add(String.valueOf(day));
                }
                else if (i > dayOfWeek + daysInMonth){
                    daysInMonthArray.add(String.valueOf(i - dayOfWeek - daysInMonth));
                }
                else {
                    daysInMonthArray.add(String.valueOf(i - dayOfWeek));
                }
            }
        }
        */

    // return daysInMonthArray;
    //}

    // Format String to enable LocalDate - String comparisons
    public static String formatString(String givenString){
        String month = givenString.substring(0,3).toUpperCase();

        if (month.equals("JAN")){
            month = "01";
        } else if (month.equals("FEB")){
            month = "02";
        } else if (month.equals("MAR")){
            month = "03";
        } else if (month.equals("APR")){
            month = "04";
        } else if (month.equals("MAY")){
            month = "05";
        } else if (month.equals("JUN")){
            month = "06";
        } else if (month.equals("JUL")){
            month = "07";
        } else if (month.equals("AUG")){
            month = "08";
        } else if (month.equals("SEP")){
            month = "09";
        } else if (month.equals("OCT")){
            month = "10";
        } else if (month.equals("NOV")){
            month = "11";
        } else if (month.equals("DEC")){
            month = "12";
        }

        String day = givenString.substring(4, 6);
        if (day.equals("1 ")){
            Log.i("TRYING", "1 ");
            day = "01";
        } if (day.equals(" 1")){
            Log.i("TRYING", " 1");
            day = "01";
        }

        String year = givenString.substring(8,12);

        return year + "-" + month + "-" + day;
    }
}
