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
import com.example.calendarandmapapp.databinding.FragmentMapsBinding;
import com.example.calendarandmapapp.models.Event;
import com.example.calendarandmapapp.models.Location;
import com.example.calendarandmapapp.viewmodels.EventViewModel;
import com.example.calendarandmapapp.viewmodels.LocationViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    private Marker marker;
    private FusedLocationProviderClient client;
    private Boolean update = false;

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

        FragmentMapsBinding binding = FragmentMapsBinding.inflate(inflater, container, false);

        EventViewModel eventViewModel = new ViewModelProvider(getActivity()).get(EventViewModel.class);
        Event selectedEvent = eventViewModel.getSelectedEvent().getValue();
        if(selectedEvent != null) {
            binding.textView4.setText("The initial marker is the location of your event if you had one. If you wish" +
                    " to change this, drag the marker to the desired location and press the save button. If you" +
                    " wish to keep the location the same, just press the save button. If your event didn't have a location you may click the map to add one. Otherwise you may press the save button without setting a location.");
            update = true;
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.viewLocMap);
            System.out.println("Selected Event!!");
            LatLng prevLoc = new LatLng(selectedEvent.getLat(), selectedEvent.getLang());
            if(selectedEvent.locationString.equals("none")){
                update = false;
            }
            else {

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
                    //
                });
            }
        }

        //

        LocationViewModel locationViewModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
        Location selectedLocation = locationViewModel.getLocation().getValue();



        binding.saveLocationButton.setOnClickListener(view ->{
            NavHostFragment.findNavController(this).navigate(R.id.action_mapsFragment_to_eventCreationFragment);
            if(marker == null){
                locationViewModel.setLocation("none", 1000, 1000);
            }
            else{
                double lat = marker.getPosition().latitude;
                double lang = marker.getPosition().longitude;
                String locationString = lat + ", " + lang;
                System.out.println("SAVING This.marker: " + lat + " " + lang);
                locationViewModel.setLocation(locationString, lat, lang);
            }

        });
        //


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
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

        //


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
                if(!update ) {
                    client.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                            .addOnSuccessListener((location) -> {
                                CameraPosition pos = new CameraPosition.Builder()
                                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
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
                            });
                }

                map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDrag(@NonNull Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(@NonNull Marker mark) {
                        LatLng position = mark.getPosition();
                        marker.setPosition(position);
                    }

                    @Override
                    public void onMarkerDragStart(@NonNull Marker marker) {

                    }
                });


                if(!update) {
                    map.setOnMapClickListener(latLng -> {

                        if (marker != null) {
                            marker.setPosition(latLng);
                        } else {
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(latLng)
                                    .draggable(true)
                                    .alpha(.5f);
                            this.marker = map.addMarker(markerOptions);
                        }
                    });
                }

            });
        }
    }
}