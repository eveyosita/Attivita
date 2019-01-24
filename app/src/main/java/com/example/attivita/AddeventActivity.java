package com.example.attivita;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AddeventActivity extends AppCompatActivity {

    Button finishaddevent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);

//        finishaddevent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                finishaddevent = (Button) findViewById(R.id.finishaddevent_button);
//                Intent i = new Intent(AddeventActivity.this, HomeFragment.class);
//                startActivity(i);
//
//            }
//        });
    }
}
