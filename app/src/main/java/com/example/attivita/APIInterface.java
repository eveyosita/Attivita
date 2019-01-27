package com.example.attivita;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("createuser.php")
    Call<ResponseBody> createuser(
            @Field("studentId") String studentid,
            @Field("password") String password,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("birthdate") String birthdate,
            @Field("telnumber") String telnumber
    );

    @FormUrlEncoded
    @POST("loginuser.php")
    Call<ResponseBody> loginuser(
            @Field("studentId") String studentid,
            @Field("password") String password
    );


}
