package org.example.demo2;

public class Itinerary {
    private int id;
    private String hotel;
    private String topAttraction;
    private String activity;
    private String breakfast;
    private String lunch;
    private String dinner;
    private String duration;

    public Itinerary() {
    }

    public Itinerary(int id, String hotel, String topAttraction, String activity, String breakfast, String lunch, String dinner, String duration) {
        this.id = id;
        this.hotel = hotel;
        this.topAttraction = topAttraction;
        this.activity = activity;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.duration = duration;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
