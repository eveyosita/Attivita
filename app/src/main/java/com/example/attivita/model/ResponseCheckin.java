package com.example.attivita.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCheckin {
    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("eventId")
    @Expose
    private int eventId;

    @SerializedName("status_checkin")
    @Expose
    private String status_checkin;

    public ResponseCheckin(){
    }

    public ResponseCheckin(String status_checkin){
        this.status_checkin = status_checkin;
    }

    public String getStatus_chackin() {
        return status_checkin;
    }

    public int getEventId() {
        return eventId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
