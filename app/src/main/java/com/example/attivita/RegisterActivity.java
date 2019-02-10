package com.example.attivita;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.attivita.model.student;
import com.example.attivita.retrofit.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Calendar;


public class RegisterActivity extends AppCompatActivity {
     EditText id_text;
     EditText pass_text;
     EditText fname_text;
     EditText lname_text;
     EditText nickname_text;
     Spinner depart_text;
    EditText email_text;
     EditText tel_text;
     Button finish_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        finish_btn = (Button) findViewById(R.id.finish_button);

        id_text = (EditText) findViewById(R.id.id2_editText);
        pass_text = (EditText) findViewById(R.id.password2_editText);
        fname_text = (EditText) findViewById(R.id.fname_editText);
        lname_text = (EditText) findViewById(R.id.lname_editText);
        nickname_text = (EditText) findViewById(R.id.nickname_editText);
        depart_text = (Spinner) findViewById(R.id.departRegist_spinner);
        email_text = (EditText) findViewById(R.id.email_editText);
        tel_text = (EditText) findViewById(R.id.tel_editText);

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = id_text.getText().toString();
                String pass = pass_text.getText().toString();
                String fname = fname_text.getText().toString();
                String lname = lname_text.getText().toString();
                String nname = nickname_text.getText().toString();
                String depart = depart_text.toString();
                String year = id.substring(2,4);
                String email = email_text.toString();
                String tel = tel_text.getText().toString();
                int y = Calendar.getInstance().get(Calendar.YEAR);
                String yy = (Integer.toString(y+543)).substring(2);
                
                if(id.substring(0,2).equals("07") && Integer.parseInt(id.substring(2,4)) <= Integer.parseInt(yy) ){

                setStudent(id,pass,fname,lname,nname,depart,year,email,tel);

                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);

            } else {
                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
            }
        });
    }

    public void setStudent(String id,String pass,String fname,String lname,String nname,String depart,String year,String email,String tel){
        Call<student> call = RetrofitClient
                .getInstance()
                .getApi()
                .createuser(id,pass,fname,lname,nname,depart,year,email,tel);

        call.enqueue(new Callback<student>() {
            @Override
            public void onResponse(Call<student> call, Response<student> response) {
                //ResponseBody res = response.body();
                //int status = Integer.parseInt(""+res);
                // Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
                Toast.makeText(RegisterActivity.this, "Succees", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<student> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Fail"+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }









}
