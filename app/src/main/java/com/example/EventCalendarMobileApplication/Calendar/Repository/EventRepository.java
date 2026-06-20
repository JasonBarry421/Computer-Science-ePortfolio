package com.example.EventCalendarMobileApplication.Calendar.Repository;

import android.content.Context;
import com.example.EventCalendarMobileApplication.Calendar.Model.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Map;

public class EventRepository {
    private Context context;

    //Initialize Firestore
    private FirebaseFirestore db;
    public EventRepository() {
        db = FirebaseFirestore.getInstance();
    }

    // Add Event
    public void addEvent(Map<String, Object> event, OnSuccessListener<DocumentReference> callback) {
        FirebaseFirestore.getInstance()
                .collection("events")
                .add(event)
                .addOnSuccessListener(callback);
    }

    // Read Events
    public void readEvents(String userId, FirestoreCallback callback) {

        db.collection("events")
                .whereEqualTo("userID", userId)
                .get()
                .addOnCompleteListener(task -> {

                    ArrayList<Event> events = new ArrayList<>();

                    if(task.isSuccessful()) {
                        for(DocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            if (event != null) {
                                event.setEventID(document.getId());
                                events.add(event);
                            }
                        }

                        callback.onCallback(events);
                    }
                });
    }

    //Create callback interface
    public interface FirestoreCallback {
        void onCallback(ArrayList<Event> events);
    }

    // Get Events based on provided Date
    public void eventsForDate(String date, String userId, FirestoreCallback callback) {

        db.collection("events")
                .whereEqualTo("userID", userId)
                .whereEqualTo("startDate", date)
                .get()
                .addOnCompleteListener(task -> {

                    ArrayList<Event> events = new ArrayList<>();

                    if(task.isSuccessful()) {
                        for(DocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            if (event != null) {
                                event.setEventID(document.getId());
                                events.add(event);
                            }
                        }
                        callback.onCallback(events);
                    }
                });
    }

    // For Updating an Event
    public void updateEvent(String eventId, Map<String, Object> event) {
        FirebaseFirestore.getInstance()
                .collection("events")
                .document(eventId)
                .set(event);
    }

    // For Deleting an Event
    public void deleteEvent(String eventId) {
        FirebaseFirestore.getInstance()
                .collection("events")
                .document(eventId)
                .delete();
    }
}