package com.example.attivita.retrofit;

import com.example.attivita.model.Event;
import com.example.attivita.model.ResponseJoinevent;
import com.example.attivita.model.ResponseRegist;
import com.example.attivita.model.student;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("createuser.php")
    Call<student> createuser(
           @Field("studentId") String studentid,
           @Field("password") String password,
           @Field("firstname") String firstname,
           @Field("lastname") String lastname,
           @Field("department") String department,
           @Field("year") String year,
           @Field("email") String email
    );

    @FormUrlEncoded
    @POST("loginuser.php")
    Call<student> loginuser(
            @Field("studentId") String studentid,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("showprofile.php")
    Call<student> showprofile(
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
            @Field("locationId") int locationId,
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

    @GET("readevent_bycategory.php")
    Call<ArrayList<Event>> readevent_bycategory(
//            @Field("categoryId") int categoryId
    );

    @FormUrlEncoded
    @POST("joinevent.php")
    Call<ResponseJoinevent> joinevent(
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

}
