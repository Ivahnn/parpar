package org.example.demo2;

import java.time.LocalDate;

public class Itinerary {
    private int id;
    private int userId;
    private String location;
    private String hotel;
    private String topAttraction;
    private String activity;
    private String breakfast;
    private String lunch;
    private String dinner;
    private LocalDate day;

    // Constructor
    public Itinerary(int id, String location, String hotel, String topAttraction, String activity,
                     String breakfast, String lunch, String dinner, LocalDate day) {
        this.id = id;
        this.location = location;
        this.hotel = hotel;
        this.topAttraction = topAttraction;
        this.activity = activity;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.day = day;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getTopAttraction() {
        return topAttraction;
    }

    public void setTopAttraction(String topAttraction) {
        this.topAttraction = topAttraction;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }
}