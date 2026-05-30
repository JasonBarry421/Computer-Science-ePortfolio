package com.example.EventCalendarMobileApplication.Calendar.View;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventCalendarMobileApplication.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public final View parent;
    private final ArrayList<LocalDate> daysOfMonth;
    public final TextView dayOfMonth;
    private final CalendarAdapter.onDayClickListener dayClickListener;
    public final LinearLayout eventContainer;
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.onDayClickListener dayClickListener, ArrayList<LocalDate> daysOfMonth) {
        super(itemView);
        this.parent = itemView.findViewById(R.id.calendarRecyclerView);
        this.daysOfMonth = daysOfMonth;
        dayOfMonth = itemView.findViewById(R.id.dayText);
        this.dayClickListener = dayClickListener;
        itemView.setOnClickListener(this);
        eventContainer = itemView.findViewById(R.id.monthlyEventContainer);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            dayClickListener.onDayClick(position, daysOfMonth.get(position));
        }
    }
}