package com.example.calendarandmapapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.calendarandmapapp.R;
import com.example.calendarandmapapp.databinding.FragmentEventCreationBinding;
import com.example.calendarandmapapp.databinding.FragmentMapsBinding;
import com.example.calendarandmapapp.models.Event;
import com.example.calendarandmapapp.models.Location;
import com.example.calendarandmapapp.viewmodels.EventViewModel;
import com.example.calendarandmapapp.viewmodels.LocationViewModel;
import com.example.calendarandmapapp.viewmodels.UserViewModel;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventCreationFragment extends Fragment {

    private boolean isSaving = false;
    private boolean update = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEventCreationBinding binding = FragmentEventCreationBinding
                .inflate(inflater, container, false);
        //
        FragmentMapsBinding mapsBinding = FragmentMapsBinding.inflate(inflater, container, false);
        EventViewModel eventViewModel = new ViewModelProvider(getActivity()).get(EventViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        LocationViewModel locationViewModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);

        Location selectedLocation = locationViewModel.getLocation().getValue();

       Event selectedEvent = eventViewModel.getSelectedEvent().getValue();
        if(selectedEvent != null){
            update = true;
           binding.eventTitle.setText(selectedEvent.getTitle() + "");
           binding.editTextDate.setText(selectedEvent.getDateString() + "");
           binding.editTextStartTime.setText(selectedEvent.getStartTimeString()+ "");
           binding.editTextEndTime.setText(selectedEvent.getEndTimeString() + "");
           binding.eventDescription.setText(selectedEvent.getDescription()+ "");
           binding.locationString.setText(selectedEvent.locationString + "");

           binding.datePicker.init(selectedEvent.getYear(), selectedEvent.getMonth(), selectedEvent.getDay(), new DatePicker.OnDateChangedListener() {
               @Override
               public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

               }
           });
           binding.startTimePicker.setHour(selectedEvent.getStartHour());
           binding.startTimePicker.setMinute(selectedEvent.getStartMinute());
           binding.endTimePicker.setHour(selectedEvent.getEndHour());
           binding.endTimePicker.setMinute(selectedEvent.getEndMinute());

            binding.deleteEventButton.setVisibility(View.VISIBLE);
            binding.deleteEventButton.setOnClickListener(view ->{
                binding.deleteEventButton.setEnabled(false);
                eventViewModel.deleteEvent(selectedEvent);
                //
                if(eventViewModel.getErrorMessage()!= null){
                    System.out.println("DELETE ERROR " + eventViewModel.getErrorMessage().toString());
                    //This doesn't work
                    binding.eventCreationError.setText(eventViewModel.getErrorMessage().toString());
                    binding.deleteEventButton.setEnabled(true);
                }
                else{
                    System.out.println("NO DELETE ERROR");
                    NavHostFragment.findNavController(this).navigateUp();
                }
            });
        }
        else if(selectedLocation!= null){
            System.out.println("Selected loc not null" + selectedLocation.locationString);
            binding.locationString.setText(selectedLocation.locationString + "");
        }


        //REENABLE BUTTON for update and create
        eventViewModel.getSaving().observe(getViewLifecycleOwner(), saving -> {
            Log.d("EventCreation", "SAVING");
            if (!isSaving && saving) {
                Log.d("EventCreation", "if");
                Log.d(saving + " ", " " + isSaving);
                isSaving = true;
                binding.saveEventButton.setEnabled(false);
            }
            else if (isSaving && !saving) { // and no
                Log.d("EventCreation", "else if");
                if(eventViewModel.getErrorMessage().getValue() != null){
                    Log.d("EventCreation", "else if if");
                    binding.eventCreationError.setText(eventViewModel.getErrorMessage().toString());
                    binding.saveEventButton.setEnabled(true);
                }
                else {
                    Log.d("EventCreation", "NAV TO MAIN LIST");
                    NavController controller = NavHostFragment.findNavController(this);
                    controller.navigate(R.id.action_eventCreationFragment_to_mainEventListFragment);
                }
            }
        });

        eventViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            binding.eventCreationError.setText(message);
            binding.saveEventButton.setEnabled(true);
        });

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if(user == null) return;
//            if(!finalLocationString.equals("none")){
//                binding.locationString.setText(finalLocationString);
//            }
            binding.saveEventButton.setOnClickListener(view ->{
                binding.saveEventButton.setEnabled(false);

                //
                String location = locationViewModel.getLocation().getValue().locationString;
                //
                //GET DATE/TIME
                int   day  = binding.datePicker.getDayOfMonth();
                int   month= binding.datePicker.getMonth();
                int   year = binding.datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String formatedDate = sdf.format(calendar.getTime());

                long timeMs = binding.startTimePicker.getHour()*60*60*1000 + binding.startTimePicker.getMinute()*60*1000;

                Time time = new Time(timeMs);
                int startHour = binding.startTimePicker.getHour();
                int startMinute = binding.startTimePicker.getMinute();
                String startAmPm = "AM";
                if(startHour > 12){
                    startHour = startHour - 12;
                    startAmPm = "PM";
                }
                System.out.println("Start hour " + startHour);

                int endHour = binding.endTimePicker.getHour();
                int endMinute = binding.endTimePicker.getMinute();
                String endAmPm = "AM";

                if(endHour > 12){
                    endHour = endHour - 12;
                    endAmPm = "PM";
                }

                String startTimeString = String.format("%02d:%02d", startHour, startMinute) + " " + startAmPm;

                String endTimeString = String.format("%02d:%02d", endHour, endMinute)+ " " + endAmPm;
                System.out.println(startTimeString);

                String myDate = formatedDate + " " + String.format("%02d:%02d", startHour, startMinute) + ":00";
                System.out.println("My DATE" + myDate);
                SimpleDateFormat pointInTimeSDF = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                System.out.println(pointInTimeSDF);

                Date pointInTimeDate = null;
                try {
                    pointInTimeDate = pointInTimeSDF.parse(myDate);
                } catch (ParseException e) {
                    pointInTimeDate = new Date();
                    pointInTimeDate.setTime(0);
                }
                long millis = pointInTimeDate.getTime();
                System.out.println(millis);
                if(!update){
                    Log.d("event creation", "CREATION");
                    eventViewModel.createEvent(binding.eventTitle.getText().toString(),
                            formatedDate,
                           startTimeString,
                            endTimeString,
                            binding.eventDescription.getText().toString(), user.uid, startHour, startMinute, endHour, endMinute, day, month, year, millis, location);
                }
                else{
                    System.out.println("Update event");
                    eventViewModel.deleteEvent(selectedEvent);
                    eventViewModel.createEvent(binding.eventTitle.getText().toString(),
                            formatedDate,
                            startTimeString,
                            endTimeString,
                            binding.eventDescription.getText().toString(), user.uid, startHour, startMinute, endHour, endMinute, day, month, year, millis, location);
                }
            });

        });
        return binding.getRoot();
    }
}