package com.example.EventCalendarMobileApplication.Calendar.View;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventCalendarMobileApplication.R;

public class DailyCalendarViewHolder extends RecyclerView.ViewHolder {
    public final View parent;
    public final TextView hourOfDay;
    public final LinearLayout eventContainer;

    public DailyCalendarViewHolder(@NonNull View itemView) {
        super(itemView);
        this.parent = itemView.findViewById(R.id.dailyRecyclerView);
        hourOfDay = itemView.findViewById(R.id.eventHourlyText);
        eventContainer = itemView.findViewById(R.id.eventContainer);
    }
}
