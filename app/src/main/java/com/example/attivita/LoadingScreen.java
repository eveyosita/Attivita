package com.example.attivita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class LoadingScreen extends AppCompatActivity {
    private static final String MY_PREFS = "prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_loading);
        getSupportActionBar().hide();

        SharedPreferences shared = getSharedPreferences(MY_PREFS,
                Context.MODE_PRIVATE);
        final boolean booleanValue = shared.getBoolean("status", false);
            Thread timer = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if(booleanValue){
                            startActivity(new Intent(LoadingScreen.this,MainActivity.class));

                        }else{
                            startActivity(new Intent(LoadingScreen.this,LoginActivity.class));
                        }
                        finish();

                    }
                }
            };
            timer.start();
    }
}
