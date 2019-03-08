package com.example.attivita.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://pilot.cp.su.ac.th/usr/u07580553/attivita/";
//    private static RetrofitClient mInstance;
//    private Retrofit retrofit;
    public static retrofit2.Retrofit RETROFIT = null;

    public static retrofit2.Retrofit getClient(){
        if (RETROFIT == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new LoggingInterceptor())
                    .build();



            Gson gson = new GsonBuilder()
                    .setLenient().create();

            RETROFIT = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return RETROFIT;
    }



//    private RetrofitClient() {
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//    }
//
//    public static synchronized RetrofitClient getInstance() {
//        if (mInstance == null) {
//            mInstance = new RetrofitClient();
//        }
//        return mInstance;
//    }
//
//    public APIInterface getApi() {
//        return retrofit.create(APIInterface.class);
//    }
}
