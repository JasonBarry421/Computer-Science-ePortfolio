package com.example.EventCalendarMobileApplication.Calendar.ViewModel;

import android.content.Context;

import com.example.EventCalendarMobileApplication.Calendar.Repository.EventRepository;
import com.example.EventCalendarMobileApplication.Session;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

public class EventViewModel {

    private EventRepository repository;

    public EventViewModel(Context context) {
        repository = new EventRepository();
    }
    public void addEvent(Map<String, Object> event, OnSuccessListener<DocumentReference> callback) {
        repository.addEvent(event, callback);
    }

    public void readEvents(EventRepository.FirestoreCallback callback) {
        repository.readEvents(Session.currentUserId, callback);
    }
    public void eventsForDate(String date, EventRepository.FirestoreCallback callback) {
        repository.eventsForDate(date, Session.currentUserId, callback);
    }
    public void updateEvent(String id, Map<String,Object> data) {
        repository.updateEvent(id, data);
    }
    public void deleteEvent(String id) {
        repository.deleteEvent(id);
    }
}