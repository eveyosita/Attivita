package com.example.attivita;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }
    public void onClickRegister (View view){
        Button btn_regist = (Button) findViewById(R.id.register_button);
        Intent intent_regist = new Intent(LoginActivity.this,page2.class);
        startActivity(intent_regist);
    }

    public void onClickLogin (View view){
        Button btn_login = (Button) findViewById(R.id.login_button);
        Intent intent_login = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent_login);
    }








}
