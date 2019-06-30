package com.example.attivita.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Eventhelp {

    private String studentId;
    private double latitude;
    private double longitude;

    public Eventhelp(){}

    public Eventhelp(String studentId, double latitude, double longitude) {

        this.studentId = studentId;
        this.latitude = latitude;
        this.longitude = longitude;


    }

    public String getStudentId() {
        return studentId;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}


