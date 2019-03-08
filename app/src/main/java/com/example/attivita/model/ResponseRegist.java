package com.example.attivita.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseRegist {
    @SerializedName("status")
    @Expose
    boolean status ;

    @SerializedName("message")
    @Expose
    String message ;
    public String getMessage() {
        return message;
    }

    public boolean getStatus() {
        return status;
    }
}
