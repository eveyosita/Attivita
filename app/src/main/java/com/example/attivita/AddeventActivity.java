package com.example.attivita;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AddeventActivity extends AppCompatActivity {

    Button but_finishaddevent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);
        getSupportActionBar().hide();

        but_finishaddevent = (Button) findViewById(R.id.finishaddevent_button);
        but_finishaddevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AddeventActivity.this, MainActivity.class);
                startActivity(i);

            }
        });
    }
}
