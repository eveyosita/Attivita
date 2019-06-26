package com.example.attivita.model;

public class StudentFirebase {
    String id;
    String username;
    double latitude;
    double longitude;
    boolean requesthelp;
    String helped;
    boolean status_helpful;
    String detail_helpful;

    public StudentFirebase(String id, String username, double latitude, double longitude, boolean requesthelp, String helped, boolean status_helpful) {
        this.id = id;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.requesthelp = requesthelp;
        this.helped = helped;
        this.status_helpful = status_helpful;
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

    public boolean isRequesthelp() {
        return requesthelp;
    }

    public boolean isStatus_helpful() {
        return status_helpful;
    }

    public String getHelped() {
        return helped;
    }

    public String getDetail_helpful() {
        return detail_helpful;
    }
}
