package com.example.EventCalendarMobileApplication.Calendar.View;

import static com.example.EventCalendarMobileApplication.Calendar.View.CalendarFormatting.selectedDate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventCalendarMobileApplication.Calendar.Model.Event;
import com.example.EventCalendarMobileApplication.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<LocalDate> daysOfMonth;
    private final onDayClickListener dayClickListener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private int rowHeight;
    private List<Event> events;

    public void setRowHeight(int height) {
        this.rowHeight = height;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public CalendarAdapter(ArrayList<LocalDate> daysOfMonth, onDayClickListener dayClickListener) {
        this.daysOfMonth = daysOfMonth;
        this.dayClickListener = dayClickListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_recycler_layout, parent, false);

        //Sets Row Height (It Should Always Be Above 0)
        if (rowHeight > 0) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            params.height = rowHeight;
            view.setLayoutParams(params);
        }

        return new CalendarViewHolder(view, dayClickListener, daysOfMonth);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        // Clears Old Recycler Views
        holder.eventContainer.removeAllViews();

        // Gets the Date, Stores it as a String using the Desired Format
        final LocalDate date = daysOfMonth.get(position);
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.US);
        //final String dateAsString = date.format(formatter);

        // Sets Day of Month Test
        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

        // Set Background Color
        holder.parent.setBackgroundColor(Color.TRANSPARENT);

        // Set Day and Text Color
        if (!date.getMonth().equals(selectedDate.getMonth())) {
            holder.dayOfMonth.setTextColor(Color.LTGRAY);
        } else {
            holder.dayOfMonth.setTextColor(Color.RED);
        }

        // If there are Events
        if(events != null)
        {
            List<Event> eventsForDay = new ArrayList<>();
            // Get List of Events that occur on each day
            for(Event e : events)
            {
//                String eventDate = e.getStartDate();
                LocalDate eventDate = e.getStartDate();

                if(Objects.equals(eventDate, date))
                {
                    eventsForDay.add(e);
                }
            }
            int maxVisible = 2;
            // For the First Two Events on each Day with Events
            for(int i = 0; i < Math.min(maxVisible, eventsForDay.size()); i++)
            {
                Event e = eventsForDay.get(i);

                // Create an Event Card
                View monthEventCard = LayoutInflater.from(holder.itemView.getContext())
                        .inflate(R.layout.monthly_event_item,
                                holder.eventContainer,
                                false);

                TextView eventText = (TextView) monthEventCard;

                // Display Title of Event in the Card
                eventText.setText(e.getTitle());

                holder.eventContainer.addView(monthEventCard);
            }
            // If there are any Remaining Events on that Day (more than 2)
            int remaining = eventsForDay.size() - maxVisible;
            if(remaining > 0)
            {
                // Create an Event Card
                View moreCard = LayoutInflater.from(holder.itemView.getContext())
                        .inflate(R.layout.monthly_event_item,
                                holder.eventContainer,
                                false);

                TextView moreText = (TextView) moreCard;

                // Display Amount of Remaining Events in Card
                moreText.setText("+ " + remaining + " More");

                holder.eventContainer.addView(moreCard);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            selectedDate = date;

            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            dayClickListener.onDayClick(selectedPosition, date);
        });
    }

    @Override
    public int getItemCount() { return daysOfMonth.size(); }

    public interface onDayClickListener {

        void onDayClick(int position, LocalDate date);
    }
}