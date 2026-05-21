package com.example.cs360finalprojectbyjasonbarry;

import static com.example.cs360finalprojectbyjasonbarry.CalendarStuff.formatString;
import static com.example.cs360finalprojectbyjasonbarry.CalendarStuff.selectedDate;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    private EventDatabase eventDatabase;
    private final Context context;

    public EventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get Event at current Position
        Event event = getItem(position);
        String eventDate = formatString(event.getStartDate());

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_list_layout, parent, false);

        TextView eventTitleTV = convertView.findViewById(R.id.eventTextView);

        // Read Events from Database
        EventDatabase eventDatabase = new EventDatabase(context);
        List<Event> events = eventDatabase.readEvents();

                // If there is an Event on the Selected Date
                if (eventDate.equals(String.valueOf(selectedDate))) {
                    String eventTitle = event.getTitle();
                    String startDate = event.getStartDate();
                    String startTime = event.getStartTime();
                    String endDate = event.getEndDate();
                    String endTime = event.getEndTime();

                    // Outputs Event
                    eventTitleTV.setTextColor(Color.RED);
                    String eventInfo = "\n" + eventTitle + "\n\n" + startDate + "\t\t"
                            + startTime + "\n\n" + endDate + "\t\t" + endTime + "\n\n\n\n\n\n";
                    eventTitleTV.setText(eventInfo);

                    convertView.setBackgroundColor(Color.LTGRAY);
                    eventTitleTV.setText(eventInfo);
                    convertView.setBackgroundColor(Color.LTGRAY);
                } else{
                    eventTitleTV.setText("");
                }
        eventDatabase.close();
        return convertView;
    }
}