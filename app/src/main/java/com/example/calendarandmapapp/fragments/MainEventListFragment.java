package com.example.calendarandmapapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calendarandmapapp.R;
import com.example.calendarandmapapp.databinding.FragmentEventListItemBinding;
import com.example.calendarandmapapp.databinding.FragmentMainEventListBinding;
import com.example.calendarandmapapp.viewmodels.EventViewModel;
import com.example.calendarandmapapp.viewmodels.UserViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import adapters.EventsAdapter;


public class MainEventListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMainEventListBinding binding = FragmentMainEventListBinding.inflate(inflater, container, false);
        EventViewModel eventViewModel = new ViewModelProvider(getActivity()).get(EventViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        MobileAds.initialize(getActivity());

        binding.bannerAd.loadAd(
                new AdRequest.Builder().build()
        );

        userViewModel.getUser().observe(getViewLifecycleOwner(), (user) -> {
            if (user == null) return;

            binding.fab.setOnClickListener(view -> {
                eventViewModel.setSelectedEvent(null);
                NavHostFragment.findNavController(this).navigate(R.id.action_mainEventListFragment_to_mapsFragment);
            });
            binding.logoutButton.setOnClickListener(view -> {
                userViewModel.logout();
                eventViewModel.clearEvents();
                NavHostFragment.findNavController(this).navigate(R.id.action_mainEventListFragment_to_loginFragment);
            });

            binding.eventsRecycler.setAdapter(
                    new EventsAdapter(eventViewModel.getEvents(user.uid),
                            event -> {
                                eventViewModel.setSelectedEvent(event);
                                NavHostFragment.findNavController(this).navigate(R.id.action_mainEventListFragment_to_eventFragment);
                            }
                    )
            );
            binding.eventsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        });
        return binding.getRoot();
    }
}