package com.example.EventCalendarMobileApplication.Calendar.View;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarFormatting {
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

    // Format String to enable LocalDate - String comparisons
    public static String formatString(String givenString){

        // Get Month Substring
        String month = givenString.substring(0,3).toUpperCase();

        // Transform Month Substring into a Value (for Date Time Formatting Later)
        switch (month) {
            case "JAN":
                month = "01";
                break;
            case "FEB":
                month = "02";
                break;
            case "MAR":
                month = "03";
                break;
            case "APR":
                month = "04";
                break;
            case "MAY":
                month = "05";
                break;
            case "JUN":
                month = "06";
                break;
            case "JUL":
                month = "07";
                break;
            case "AUG":
                month = "08";
                break;
            case "SEP":
                month = "09";
                break;
            case "OCT":
                month = "10";
                break;
            case "NOV":
                month = "11";
                break;
            case "DEC":
                month = "12";
                break;
        }

        // Transform Day Substring into a Value (for Date Time Formatting Later)
        String day = givenString.substring(4, 6);

        // Transform Year Substring into a Value (for Date Time Formatting Later)
        String year = givenString.substring(8,12);

        // Return Date
        return year + "-" + month + "-" + day;
    }
}
