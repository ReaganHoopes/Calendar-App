package com.example.calendarandmapapp.models;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Event {
    public String title;
    public String dateString;
    public String startTimeString;
    public String endTimeString;
    public String description;
    public String uid;
    public long timestamp;
    public int startHour;
    public int startMinute;
    public int endHour;
    public int endMinute;
    public int day;
    public int month;
    public int year;
    public String locationString;
    public double lat;
    public double lang;

    long pointInTime;

    public Event(){}

    public Event(String title, String dateString, String startTimeString, String endTimeString, String description, String uid, long timestamp, int startHour, int startMinute, int endHour, int endMinute, int day, int month, int year, long pointInTime, String locationString){
        this.title = title;
        this.dateString = dateString;
        this.startTimeString = startTimeString;
        this.endTimeString = endTimeString;
        this.description = description;
        this.uid = uid;
        this.timestamp = timestamp;

        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;

        this.day = day;
        this.month = month;
        this.year = year;

        this.pointInTime = pointInTime;

        this.locationString = locationString;
        if(locationString.equals("none")){
            this.lat = 1000;
            this.lang = 1000;
        }
        else{
            String[] latlng = locationString.split(",");
            this.lat = Double.parseDouble(latlng[0]);
            this.lang = Double.parseDouble(latlng[1]);
        }
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setDateString(String dateString){this.dateString = dateString;}
    public String getDateString(){
        return this.dateString;
    }
    public void setStartTimeString(String startTimeString){
        this.startTimeString = startTimeString;
    }
    public String getStartTimeString(){
        return this.startTimeString;
    }
    public void setEndTimeString(String endTimeString){
        this.endTimeString = endTimeString;
    }
    public String getEndTimeString(){
        return this.endTimeString;
    }
    public void setDescription(String description){this.description =description; }
    public String getDescription(){
        return this.description;
    }
    public String getUid(){
        return this.uid;
    }
    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }
    public long getTimestamp(){
        return this.timestamp;
    }

    public int getStartHour(){
        return this.startHour;
    }
    public int getStartMinute(){
        return this.startMinute;
    }
    public int getEndHour(){
        return this.endHour;
    }
    public int getEndMinute(){
        return endMinute;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getPointInTime() {
        return pointInTime;
    }

    public void setPointInTime(long pointInTime) {
        this.pointInTime = pointInTime;
    }

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }
}
