package com.example.attivita.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseJoinevent {
    @SerializedName("status")
    @Expose
    boolean status ;

    @SerializedName("message")
    @Expose
    String message ;

    @SerializedName("studentId")
    @Expose
    String studentId ;

    @SerializedName("eventId")
    @Expose
    int eventId ;

    public ResponseJoinevent(String studentId){
        this.studentId = studentId;
    }

    public ResponseJoinevent(int eventId){
        this.eventId = eventId;
    }


    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getEventId() {
        return eventId;
    }
}
