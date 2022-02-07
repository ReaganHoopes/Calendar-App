package com.example.calendarandmapapp.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calendarandmapapp.models.Event;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EventViewModel extends ViewModel {
    ObservableArrayList<Event> events;
    MutableLiveData<Boolean> saving = new MutableLiveData<>();
    MutableLiveData<Event> selectedEvent = new MutableLiveData<>();
    FirebaseFirestore db;
    MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public EventViewModel(){
        db = FirebaseFirestore.getInstance();
        saving.setValue(false);
    }

    public MutableLiveData<String> getErrorMessage(){return errorMessage;}

    public MutableLiveData<Boolean> getSaving() {
        return saving;
    }

    public MutableLiveData<Event> getSelectedEvent(){return selectedEvent;}
    public void setSelectedEvent(Event selectedEvent){this.selectedEvent.setValue(selectedEvent);}

    public void createEvent(String title, String dateString, String startTime, String endTime, String description, String uid, int startHour, int startMinute, int endHour, int endMinute, int day, int month, int year, long pointInTime, String locationString){
        Log.d("Event view model", "create event");
        errorMessage.postValue(null);
        if(title == null || title.isEmpty()){
            Log.d("Event view model", "error");
            errorMessage.postValue("Title cannot be empty");
            return;
        }
        saving.setValue(true);
        Log.d("Event view model", "saving true");
        Log.d("Event view model", dateString);
        Event event = new Event(title, dateString, startTime, endTime, description, uid, System.currentTimeMillis(),startHour, startMinute, endHour, endMinute, day, month, year, pointInTime, locationString);
        Log.d("Event view model", "create event complete");

        //Just never enters here
        db
                .collection("events")
                .document(event.getUid() + "_" + event.getTimestamp())
                .set(event)
                .addOnCompleteListener(task -> {
                    Log.d("Event view model", "IN DB");
                    if(task.isSuccessful()){
                            Log.d("Event view model", "EVENT SUCCESS");
                            events.add(0, event);
                        }
                    else{
                        Log.d("Event view model", task.getException().getMessage());
                        errorMessage.postValue("Event creation failed, try again");
                    }
                    Log.d("Event view model", "Saving set to false");
                    saving.setValue(false);
                    }
                );
    }

    public void updateEvent(Event event, String title, String dateString, String startTimeString, String endTimeString, String description, int startHour, int startMinute, int endHour, int endMinute, int day, int month, int year, long pointInTime, String location){
        errorMessage.postValue(null);
        if(title == null || title.isEmpty()){
            errorMessage.postValue("Title cannot be empty");
            return;
        }
        System.out.println("Updating event");
        saving.setValue(true);
        event.setTitle(title);
        event.setDateString(dateString);
        event.setStartTimeString(startTimeString);
        event.setEndTimeString(endTimeString);
        event.setDescription(description);
        event.setTimestamp(System.currentTimeMillis());
        event.setStartHour(startHour);
        event.setStartMinute(startMinute);
        event.setEndHour(endHour);
        event.setEndMinute(endMinute);
        event.setDay(day);
        event.setMonth(month);
        event.setYear(year);
        event.setPointInTime(pointInTime);
        event.setLocationString(location);

        if(location.equals("none")){
            event.setLat(1000);
            event.setLang(1000);
        }
        else{
            String[] latlng = location.split(",");
            event.setLat(Double.parseDouble(latlng[0]));
            event.setLang(Double.parseDouble(latlng[1]));
        }

        db
                .collection("events")
                .document(event.getUid() + "_" + event.getTimestamp())
                .set(event)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        System.out.println("Index of");
                        int i = events.indexOf(event);
                        events.set(i, event);
                    }
                    else{
                        errorMessage.postValue("Event update failed, try again");
                    }
                    saving.setValue(false);
                });
    }

    public void deleteEvent(Event event){
        errorMessage.postValue(null);
        saving.setValue(true);

        db
                .collection("events")
                .document(event.getUid() + "_" + event.getTimestamp())
                .delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        events.remove(event);
                    }
                    else{
                        errorMessage.postValue(task.getException().getLocalizedMessage());
                    }
                    saving.setValue(false);
                });

    }

    public ObservableArrayList<Event> getEvents(String uid){
        if(events == null){
            events = new ObservableArrayList<>();
            loadEvents(uid);
        }
        return events;
    }

    public void clearEvents(){events = null;}



    private void loadEvents(String uid){

        db
                .collection("events")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Event> events = task.getResult().toObjects(Event.class);
                        events.sort(Comparator.comparingLong(Event::getPointInTime));
                        this.events.addAll(events);
                        for(Event event : events){
                            System.out.println(event.getPointInTime());
                        }
                    }
                    else{
                        //handle error
                    }
                });
    }
}
