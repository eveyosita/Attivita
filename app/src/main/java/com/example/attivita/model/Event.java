package com.example.attivita.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("eventId")
    @Expose
    private int eventId;

    @SerializedName("eventname")
    @Expose
    private String eventname;

    @SerializedName("studentId")
    @Expose
    private String studentId;

    @SerializedName("startdate")
    @Expose
    private String startdate;

    @SerializedName("enddate")
    @Expose
    private String enddate;

    @SerializedName("strattime")
    @Expose
    private String strattime;

    @SerializedName("endtime")
    @Expose
    private String endtime;

    @SerializedName("categoryId")
    @Expose
    private int categoryId;

    @SerializedName("eventdetail")
    @Expose
    private String eventdetail;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("department")
    @Expose
    private String department;

    @SerializedName("year")
    @Expose
    private String year;

    @SerializedName("placename")
    @Expose
    private String placename;

    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("longitude")
    @Expose
    private double longitude;

    @SerializedName("address")
    @Expose
    private String address;

    public Event(){}

    public Event(int eventId,String eventname, String studentId, String startdate, String enddate, String strattime, String endtime,
                 int categoryId, String eventdetail, String amount, String department, String year,
                 String placename, double latitude, double longitude, String address) {

        this.eventId = eventId;
        this.eventname = eventname;
        this.studentId = studentId;
        this.startdate = startdate;
        this.enddate = enddate;
        this.strattime = strattime;
        this.endtime = endtime;
        this.categoryId = categoryId;
        this.eventdetail = eventdetail;
        this.amount = amount;
        this.department = department;
        this.year = year;
        this.placename = placename;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;

    }

    public int getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public int getEventId() {
        return eventId;
    }
    public String getEventname() {
        return eventname;
    }
    public String getStudentId() {
        return studentId;
    }
    public String getStartdate() {
        return startdate;
    }
    public String getEnddate() {
        return enddate;
    }
    public String getStrattime() {
        return strattime;
    }
    public String getEndtime() {
        return endtime;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public String getEventdetail() {
        return eventdetail;
    }
    public String getAmount() {
        return amount;
    }
    public String getDepartment() {
        return department;
    }
    public String getYear() {
        return year;
    }

    public String getPlacename() {
        return placename;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String massage) {
        this.message = message;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public void setStrattime(String strattime) {
        this.strattime = strattime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setEventdetail(String eventdetail) {
        this.eventdetail = eventdetail;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}


