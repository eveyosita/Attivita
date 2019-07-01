package com.example.attivita.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseStatus {
    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("status_verify")
    @Expose
    private String status_verify;

    ResponseStatus(){

    }

    public String getMessage() {
        return message;
    }

    public String getStatus_verify() {
        return status_verify;
    }

    public boolean isStatus() {
        return status;
    }
}
