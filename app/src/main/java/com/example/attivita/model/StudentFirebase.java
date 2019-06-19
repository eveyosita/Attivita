package com.example.attivita.model;

public class StudentFirebase {
    String id;
    String username;
    double latitude;
    double longitude;

    public StudentFirebase(String id, String username, double latitude, double longitude) {
        this.id = id;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public StudentFirebase() {

    }



    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
