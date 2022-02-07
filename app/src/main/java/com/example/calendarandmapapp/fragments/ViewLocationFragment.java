package com.example.calendarandmapapp.fragments;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calendarandmapapp.R;
import com.example.calendarandmapapp.databinding.FragmentViewLocationBinding;
import com.example.calendarandmapapp.models.Event;
import com.example.calendarandmapapp.viewmodels.EventViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewLocationFragment extends Fragment {


    private GoogleMap mMap;
    private Marker marker;
    private FusedLocationProviderClient client;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentViewLocationBinding binding = FragmentViewLocationBinding.inflate(inflater, container, false);


        EventViewModel eventViewModel = new ViewModelProvider(getActivity()).get(EventViewModel.class);
        Event selectedEvent = eventViewModel.getSelectedEvent().getValue();
        if(selectedEvent!= null) {
            System.out.println("Selected event isn't null");
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.viewLocMap);
            System.out.println("Selected Event!!");
            LatLng prevLoc = new LatLng(selectedEvent.getLat(), selectedEvent.getLang());

            mapFragment.getMapAsync((map) -> {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);

                MarkerOptions ops = new MarkerOptions().position(prevLoc).draggable(true);
//                marker.setPosition(prevLoc);
                this.marker = map.addMarker(ops);
                System.out.println("This.marker w/ ops: " + this.marker.getPosition().latitude + " " + this.marker.getPosition().longitude);


                CameraPosition pos = new CameraPosition.Builder()
                        .target(prevLoc)
                        .zoom(15)
                        .build();

                map.animateCamera(CameraUpdateFactory.newCameraPosition(pos),
                        1000,
                        new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                                System.out.println("I AM FINISHED");
                            }

                            @Override
                            public void onCancel() {
                                System.out.println("I GOT INTERRUPTED");
                            }
                        }
                );

                binding.returnToEventButton.setOnClickListener(view -> {
                    NavHostFragment.findNavController(this).navigateUp();
                });
            });
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityResultLauncher<String> launcher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        setUpMap();
                    } else {
                        System.out.println("User denied permissions");
                    }
                }
        );
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }
    private void setUpMap(){
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.viewLocMap);

        if (mapFragment != null) {
            mapFragment.getMapAsync((map)->{
                if(ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                map.setMyLocationEnabled(true);
                client = new FusedLocationProviderClient(this.getActivity());
            });
        }
    }
}