package com.example.attivita.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {
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
    private String categoryId;

    @SerializedName("eventdetail")
    @Expose
    private String eventdetail;

    @SerializedName("locationId")
    @Expose
    private String locationId;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("department")
    @Expose
    private String department;

    @SerializedName("year")
    @Expose
    private String year;


    public Event(int eventId, String eventname, String studentId, String startdate, String enddate, String strattime, String endtime,
                 String categoryId, String eventdetail, String locationId, String amount, String department, String year) {
        this.eventId = eventId;
        this.eventname = eventname;
        this.studentId = studentId;
        this.startdate = startdate;
        this.enddate = enddate;
        this.strattime = strattime;
        this.endtime = endtime;
        this.categoryId = categoryId;
        this.eventdetail = eventdetail;
        this.locationId = locationId;
        this.amount = amount;
        this.department = department;
        this.year = year;

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
    public String getCategoryId() {
        return categoryId;
    }
    public String getEventdetail() {
        return eventdetail;
    }
    public String getLocationId() {
        return locationId;
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

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setEventdetail(String eventdetail) {
        this.eventdetail = eventdetail;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
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
}
