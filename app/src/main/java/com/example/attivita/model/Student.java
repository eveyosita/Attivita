package com.example.attivita.model;

public class Student {

    private String studentid;
    private String firstname;
    private String lastname;
    private String nickname;
    private String department;
    private String email;
    private String telnumber;
    private String profile_pic;

    public Student(String studentid, String firstname, String lastname, String nickname, String department, String email, String telnumber, String profile_pic) {
        this.studentid = studentid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.nickname = nickname;
        this.department = department;
        this.email = email;
        this.telnumber = telnumber;
        this.profile_pic = profile_pic;
    }

    public String getStudentid() {
        return studentid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getTelnumber() {
        return telnumber;
    }

    public String getProfile_pic() {
        return profile_pic;
    }
}
