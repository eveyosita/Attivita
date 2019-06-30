package com.example.attivita.model;

public class StudentFirebase {
    String id;
    String username;
    double latitude;
    double longitude;
    double helped_latitude;
    double helped_longitude;
    boolean requesthelp;
    String helped;
    boolean statusAccept_helpful;
    boolean status_helpful;
    String detail_helpful;
    int status_star;

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

    public double getHelped_latitude() {
        return helped_latitude;
    }

    public double getHelped_longitude() {
        return helped_longitude;
    }

    public boolean isStatusAccept_helpful() {
        return statusAccept_helpful;
    }

    public int getStatus_star() {
        return status_star;
    }

    public void setDetail_helpful(String detail_helpful) {
        this.detail_helpful = detail_helpful;
    }

    public void setHelped(String helped) {
        this.helped = helped;
    }

    public void setHelped_latitude(double helped_latitude) {
        this.helped_latitude = helped_latitude;
    }

    public void setHelped_longitude(double helped_longitude) {
        this.helped_longitude = helped_longitude;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus_helpful(boolean status_helpful) {
        this.status_helpful = status_helpful;
    }

    public void setRequesthelp(boolean requesthelp) {
        this.requesthelp = requesthelp;
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
