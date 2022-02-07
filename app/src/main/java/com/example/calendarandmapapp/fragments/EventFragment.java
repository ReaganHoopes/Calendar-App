package com.example.calendarandmapapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calendarandmapapp.R;
import com.example.calendarandmapapp.databinding.FragmentEventBinding;
import com.example.calendarandmapapp.databinding.FragmentEventCreationBinding;
import com.example.calendarandmapapp.models.Event;
import com.example.calendarandmapapp.viewmodels.EventViewModel;
import com.example.calendarandmapapp.viewmodels.UserViewModel;

public class EventFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEventCreationBinding creationBinding = FragmentEventCreationBinding.inflate(inflater, container, false);
        FragmentEventBinding binding = FragmentEventBinding.inflate(inflater, container, false);
        EventViewModel eventViewModel = new ViewModelProvider(getActivity()).get(EventViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        Event selectedEvent = eventViewModel.getSelectedEvent().getValue();
        if(selectedEvent != null){
            binding.eventPageTitle.setText(selectedEvent.getTitle() + "");
            binding.eventPageDate.setText("Date: "+ selectedEvent.getDateString() + "");
            binding.eventPageStartTime.setText("Start Time: " + selectedEvent.getStartTimeString()+ "");
            binding.eventPageEndTime.setText("End Time: " + selectedEvent.getEndTimeString() + "");
            binding.eventPageDescription.setText("Description: " + selectedEvent.getDescription()+ "");
            binding.eventPageLocation.setText("Location: " + selectedEvent.getLocationString());

            binding.editEventButton.setOnClickListener(view ->{
                NavController controller = NavHostFragment.findNavController(this);
                controller.navigate(R.id.action_eventFragment_to_mapsFragment);
            });

            if(!selectedEvent.locationString.equals("none")){
                binding.viewLocationButton.setVisibility(View.VISIBLE);
            }
            binding.viewLocationButton.setOnClickListener(view ->{
                NavController controller = NavHostFragment.findNavController(this);
                controller.navigate(R.id.action_eventFragment_to_viewLocationFragment);
            });
        }
        return binding.getRoot();
    }
}