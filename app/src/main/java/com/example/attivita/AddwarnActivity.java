package com.example.attivita;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AddwarnActivity extends AppCompatActivity {

    Button but_finishwarn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwarn);
        getSupportActionBar().hide();

        but_finishwarn = (Button) findViewById(R.id.finishwarn_button);

        but_finishwarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddwarnActivity.this, MainActivity.class);
                startActivity(i);

            }
        });
    }
}
