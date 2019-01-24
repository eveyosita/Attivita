package com.example.attivita;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class page2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
    }

    public void onClickFinish (View view){
        Button btn_finish = (Button) findViewById(R.id.finish_button);
        Intent intent_finish = new Intent(page2.this, MainActivity.class);
        startActivity(intent_finish);
    }










}
