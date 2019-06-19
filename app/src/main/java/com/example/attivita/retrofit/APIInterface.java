package com.example.attivita.retrofit;

import com.example.attivita.model.Event;
import com.example.attivita.model.ResponseJoinevent;
import com.example.attivita.model.StudentPHP;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("createuser.php")
    Call<StudentPHP> createuser(
           @Field("studentId") String studentid,
           @Field("password") String password,
           @Field("firstname") String firstname,
           @Field("lastname") String lastname,
           @Field("department") String department,
           @Field("year") String year,
           @Field("email") String email,
           @Field("identity_pic") String identity_pic
    );

    @FormUrlEncoded
    @POST("loginuser.php")
    Call<StudentPHP> loginuser(
            @Field("studentId") String studentid,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("showprofile.php")
    Call<StudentPHP> showprofile(
            @Field("studentId") String studentid
    );

    @FormUrlEncoded
    @POST("createevent.php")
    Call<Event> createevent(
            @Field("eventname") String eventname,
            @Field("studentId") String studentId,
            @Field("startdate") String startdate,
            @Field("enddate") String enddate,
            @Field("strattime") String strattime,
            @Field("endtime") String endtime,
            @Field("categoryId") int categoryId,
            @Field("eventdetail") String eventdetail,
            @Field("amount") String amount,
            @Field("department") String department,
            @Field("year") String year,
            @Field("placename") String placename,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("readevent.php")
    Call<ArrayList<Event>> readevent(
            @Field("studentId") String studentId
    );

    @FormUrlEncoded
    @POST("readevent_bycategory.php")
    Call<ArrayList<Event>> readevent_bycategory(
            @Field("categoryId") int categoryId
    );

    @FormUrlEncoded
    @POST("searchevent.php")
    Call<ArrayList<Event>> searchevent(
            @Field("studentId") String studentId,
            @Field("search_query") String search_query
    );

    @FormUrlEncoded
    @POST("joinevent.php")
    Call<ResponseJoinevent> joinevent(
            @Field("studentId") String studentid,
            @Field("eventId") int eventId
    );

    @FormUrlEncoded
    @POST("canceljoin.php")
    Call<ResponseJoinevent> canceljoin(
            @Field("studentId") String studentid,
            @Field("eventId") int eventId
    );

    @FormUrlEncoded
    @POST("checkjoinevent.php")
    Call<ArrayList<ResponseJoinevent>> checkjoinevent(
            @Field("eventId") int eventId
    );

    @FormUrlEncoded
    @POST("readeventjoint.php")
    Call<ArrayList<Event>> readeventjoint(
            @Field("studentId") String studentid
    );

    @FormUrlEncoded
    @POST("readeventcreate.php")
    Call<ArrayList<Event>> readeventcreate(
            @Field("studentId") String studentid
    );

    @FormUrlEncoded
    @POST("updateprofile.php")
    Call<StudentPHP> updateprofile(
            @Field("studentId") String studentid,
            @Field("nickname") String nickname,
            @Field("telnumber") String telnumber,
            @Field("profile_pic") String profile_pic
    );

    @FormUrlEncoded
    @POST("readstudent.php")
    Call<StudentPHP> readstudent(
            @Field("studentId") String studentid
    );

    @FormUrlEncoded
    @POST("getstudent.php")
    Call<StudentPHP> getstudent(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("getevent.php")
    Call<Event> getevent(
            @Field("eventId") int eventId
    );

    @FormUrlEncoded
    @POST("editEvent.php")
    Call<Event> editEvent(
            @Field("eventId") int eventId,
            @Field("eventname") String eventname,
            @Field("startdate") String startdate,
            @Field("enddate") String enddate,
            @Field("strattime") String strattime,
            @Field("endtime") String endtime,
            @Field("categoryId") int categoryId,
            @Field("eventdetail") String eventdetail,
            @Field("amount") String amount,
            @Field("placename") String placename,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("address") String address
    );
}
