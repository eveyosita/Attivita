package com.example.attivita.retrofit;

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
            @Field("nickname") String nickname,
           @Field("department") String department,
           @Field("year") String year,
           @Field("email") String email,
            @Field("telnumber") String telnumber
    );

    @FormUrlEncoded
    @POST("loginuser.php")
    Call<ResponseBody> loginuser(
            @Field("studentId") String studentid,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("showprofile.php")
    Call<student> showprofile(
            @Field("studentId") String studentid
    );


}
