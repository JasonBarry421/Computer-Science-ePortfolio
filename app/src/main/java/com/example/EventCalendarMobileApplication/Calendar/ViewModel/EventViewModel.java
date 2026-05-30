package com.example.EventCalendarMobileApplication.Calendar.ViewModel;

import android.content.Context;

import com.example.EventCalendarMobileApplication.Calendar.Model.Event;
import com.example.EventCalendarMobileApplication.Calendar.Repository.EventRepository;
import com.example.EventCalendarMobileApplication.Session;

import java.util.ArrayList;

public class EventViewModel {

    private EventRepository repository;

    public EventViewModel(Context context) {
        repository = new EventRepository(context);
    }

    public void addEvent(Event event) {
        repository.addEvent(event);
    }

    public ArrayList<Event> readEvents() {
        return repository.readEvents(Session.currentUserId);
    }

    public ArrayList<Event> eventsForDate(String date) {
        return repository.eventsForDate(date, Session.currentUserId);
    }
}