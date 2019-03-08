package com.example.attivita.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class student {
    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("massage")
    @Expose
    private String massage;

    @SerializedName("studentId")
    @Expose
    private String studentid;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("firstname")
    @Expose
    private String firstname;

    @SerializedName("lastname")
    @Expose
    private String lastname;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("department")
    @Expose
    private String department;

    @SerializedName("year")
    @Expose
    private String year;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("telnumber")
    @Expose
    private String telnumber;



    public student(String studentid, String password, String firstname, String lastname, String nickname, String department, String year, String email, String telnumber) {
        this.studentid = studentid;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.nickname = nickname;
        this.department = department;
        this.year = year;
        this.email = email;
        this.telnumber = telnumber;
    }

    public student(String studentid, String password,String firstname, String lastname,String department, String year){
        this.studentid = studentid;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.department = department;
        this.year = year;
    }

    public student(){
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getStudentid(){
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname(){
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname(){
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDepartment(){
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear(){
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelnumber(){
        return telnumber;
    }

    public void setTelnumber(String telnumber) {
        this.telnumber = telnumber;
    }
}
