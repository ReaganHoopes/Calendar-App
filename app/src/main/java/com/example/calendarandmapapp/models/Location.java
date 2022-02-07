package com.example.calendarandmapapp.models;

public class Location {
    public String locationString;
    public double lat;
    public double lang;

    public Location(){}

    public Location(String locationString, double lat, double lang){
        this.locationString = locationString;
        this.lat = lat;
        this.lang = lang;

    }
}
