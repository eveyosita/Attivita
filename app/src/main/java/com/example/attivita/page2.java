package com.example.attivita;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attivita.APIInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Calendar;


public class page2 extends AppCompatActivity {
     EditText id_text;
     EditText pass_text;
     EditText fname_text;
     EditText lname_text;
     EditText birthdate_text;
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
        birthdate_text = (EditText) findViewById(R.id.birthdate_editText);
        tel_text = (EditText) findViewById(R.id.tel_editText);

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = id_text.getText().toString();
                String pass = pass_text.getText().toString();
                String fname = fname_text.getText().toString();
                String lname = lname_text.getText().toString();
                String birth = birthdate_text.getText().toString();
                String tel = tel_text.getText().toString();
                int y = Calendar.getInstance().get(Calendar.YEAR);
                String yy = (Integer.toString(y+543)).substring(2);
                
                if(id.substring(0,2).equals("07") && Integer.parseInt(id.substring(2,4)) <= Integer.parseInt(yy) ){

                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .createuser(id, pass, fname, lname, birth, tel);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(page2.this, "Succees", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(page2.this, "Fail"+t.getMessage(), Toast.LENGTH_LONG).show();
                        System.err.println("ERRORRRRR : "+ t.getMessage());
                    }
                });

                Intent i = new Intent(page2.this, MainActivity.class);
                startActivity(i);

            } else {
                Toast.makeText(page2.this, "Error", Toast.LENGTH_LONG).show();
            }

            }
        });


    }











}
