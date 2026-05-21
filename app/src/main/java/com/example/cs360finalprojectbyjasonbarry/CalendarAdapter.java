package com.example.cs360finalprojectbyjasonbarry;

import static com.example.cs360finalprojectbyjasonbarry.CalendarStuff.formatString;
import static com.example.cs360finalprojectbyjasonbarry.CalendarStuff.selectedDate;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<LocalDate> daysOfMonth;

    private final onDayClickListener dayClickListener;

    private static EventDatabase eventDatabase;
    private Context context;

    public CalendarAdapter(Context context, ArrayList<LocalDate> daysOfMonth, onDayClickListener dayClickListener) {
        this.context = context;
        this.daysOfMonth = daysOfMonth;
        this.dayClickListener = dayClickListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_recycler_layout, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.125);
        return new CalendarViewHolder(view, dayClickListener, daysOfMonth);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        final LocalDate date = daysOfMonth.get(position);
        final String data = date.toString();

        eventDatabase = new EventDatabase(context);
        List<Event> events = eventDatabase.readEvents();
        eventDatabase.close();


        if (date.getMonth().equals(selectedDate.getMonth())) {
            holder.dayOfMonth.setTextColor(Color.RED);
        } else
            holder.dayOfMonth.setTextColor(Color.LTGRAY);

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

        if (events != null) {
            for (Event no : events) {

                String start = no.getStartDate();
                String s = formatString(start);

                // Highlights Days with an Event
                if (data.equals(s)) {
                    holder.parent.setBackgroundColor(Color.CYAN);
                }
            }
        }
        // Highlights Selected Day
        if (date.equals(selectedDate)) {
            holder.parent.setBackgroundColor(Color.LTGRAY);
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface onDayClickListener {
        void onResume(Bundle savedInstanceState);

        void onDayClick(int position, LocalDate date);
    }
}