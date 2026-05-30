package com.example.EventCalendarMobileApplication.Calendar.View;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventCalendarMobileApplication.Calendar.Model.Event;
import com.example.EventCalendarMobileApplication.R;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DailyCalendarAdapter extends RecyclerView.Adapter<DailyCalendarViewHolder> {
    private final ArrayList<LocalTime> hoursOfDay;
    private final onHourClickListener hourClickListener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private List<Event> events;
    @SuppressLint("NotifyDataSetChanged")
    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public DailyCalendarAdapter(ArrayList<LocalTime> hoursOfDay, onHourClickListener hourClickListener) {
        this.hoursOfDay = hoursOfDay;
        this.hourClickListener = hourClickListener;
    }

    @NonNull
    @Override
    public DailyCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_recycler_layout, parent, false);

        return new DailyCalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyCalendarViewHolder holder, int position) {
        // Clears Old Recycler Views
        holder.eventContainer.removeAllViews();

        final LocalTime currentHourOfDay = hoursOfDay.get(position);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

        // Sets Hour of Day Test
        holder.hourOfDay.setText(currentHourOfDay.format(formatter));

        // If there are Events
        if(events != null)
        {
            for(Event e : events)
            {
                LocalTime eventTime = e.getStartTime();
                int startTimeHour = eventTime.getHour();
                int currentHour = currentHourOfDay.getHour();

                // Adds Events to Proper Container based on Start Time
                if(startTimeHour == currentHour)
                {
                    View eventView = LayoutInflater
                            .from(holder.itemView.getContext())
                            .inflate(R.layout.daily_event_item,
                                    holder.eventContainer,
                                    false);

                    TextView eventText = (TextView) eventView;

                    eventView.setOnClickListener(v -> hourClickListener.onEventClick(e));

                    eventText.setText(e.getTitle());

                    holder.eventContainer.addView(eventView);
                }
            }
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            hourClickListener.onHourClick(selectedPosition, currentHourOfDay);
        });
    }

    @Override
    public int getItemCount() { return hoursOfDay.size(); }

    public interface onHourClickListener {

        void onHourClick(int position, LocalTime hour);
        void onEventClick(Event event);
    }

}