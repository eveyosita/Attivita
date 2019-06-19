package com.example.attivita;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.attivita.model.Student;


public class SharedPrefManager {

    private static final String MY_PREFS = "prefs";

    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx){
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx){
        if (mInstance == null){
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public Student getStudent(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        return new Student(
                sharedPreferences.getString("studentId",null),
                sharedPreferences.getString("firstname",null),
                sharedPreferences.getString("lastname",null),
                sharedPreferences.getString("nickname",null),
                sharedPreferences.getString("department",null),
                sharedPreferences.getString("email",null),
                sharedPreferences.getString("telnumber",null),
                sharedPreferences.getString("profile_pic",null)

        );
    }

}
