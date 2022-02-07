package com.example.calendarandmapapp.viewmodels;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calendarandmapapp.models.Location;

public class LocationViewModel extends ViewModel {
    MutableLiveData<Location> selectedLocation = new MutableLiveData<Location>();

    public void setLocation(String locationString, double lat, double lang){
        Location location = new Location(locationString,lat, lang);
        this.selectedLocation.setValue(location);
    }
    public MutableLiveData<Location> getLocation(){
        return selectedLocation;
    }


}
