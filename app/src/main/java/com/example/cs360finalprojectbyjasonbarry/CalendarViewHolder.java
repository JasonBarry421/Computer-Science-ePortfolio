package com.example.cs360finalprojectbyjasonbarry;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public final View parent;
    private final ArrayList<LocalDate> daysOfMonth;
    public final TextView dayOfMonth;
    private final CalendarAdapter.onDayClickListener dayClickListener;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.onDayClickListener dayClickListener, ArrayList<LocalDate> daysOfMonth) {
        super(itemView);
        this.parent = itemView.findViewById(R.id.parentView);
        this.daysOfMonth = daysOfMonth;
        dayOfMonth = itemView.findViewById(R.id.dayText);
        this.dayClickListener = dayClickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        dayClickListener.onDayClick(getAdapterPosition(), daysOfMonth.get(getAdapterPosition()));
    }
}
