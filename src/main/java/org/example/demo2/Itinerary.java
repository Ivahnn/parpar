package org.example.demo2;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Itinerary {
    private int id;
    private int userId;
    private String location;
    private String hotel;
    private LocalDate day;
    private List<Activity> activities;

    // Constructor
    public Itinerary(int id, int userId, String location, String hotel, LocalDate day, List<Activity> activities) {
        this.id = id;
        this.userId = userId;
        this.location = location;
        this.hotel = hotel;
        this.day = day;
        this.activities = activities;
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

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    // Method to get activity summary
    public String getActivitySummary() {
        return activities.stream()
                .map(activity -> activity.getName() + " (" + activity.getTime() + ")")
                .collect(Collectors.joining(", "));
    }
}
