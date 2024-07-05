package org.example.demo2;

import java.time.LocalDate;

public class Itinerary {
    private String location;
    private String hotel;
    private String topAttraction;
    private String activity;
    private LocalDate day;

    public Itinerary(String location, String hotel, String topAttraction, String activity, LocalDate day) {
        this.location = location;
        this.hotel = hotel;
        this.topAttraction = topAttraction;
        this.activity = activity;
        this.day = day;
    }

    // Getters and setters for all fields
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getHotel() { return hotel; }
    public void setHotel(String hotel) { this.hotel = hotel; }

    public String getTopAttraction() { return topAttraction; }
    public void setTopAttraction(String topAttraction) { this.topAttraction = topAttraction; }

    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }

    public LocalDate getDay() { return day; }
    public void setDay(LocalDate day) { this.day = day; }
}