package com.example.attivita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attivita.model.ResponseRegist;
import com.example.attivita.model.student;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btn_regist;
    Button btn_login;
    EditText et_id;
    EditText et_pass;
    String id;
    String pass;
    private static final String MY_PREFS = "prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        et_id = (EditText) findViewById(R.id.id_editText);
        et_pass = (EditText) findViewById(R.id.password_editText);

        et_id.addTextChangedListener(myTextWatcher);

        btn_regist = (Button) findViewById(R.id.register_button);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);

            }
        });



        btn_login = (Button) findViewById(R.id.login_button);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                id = et_id.getText().toString();
                pass = et_pass.getText().toString();

                checkLogin(id,pass);

            }
        });

    }

    public TextWatcher myTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int count = 8 - et_id.length();
            if(count<8){

            }
        }
    };


    public void checkLogin(String id, String pass){
        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<student> call = apiService.loginuser(id,pass);
        call.enqueue(new Callback<student>() {
            @Override
            public void onResponse(Call<student> call, Response<student> response) {
                student res = response.body();
                if(res.isStatus()){
                    SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("studentId", res.getStudentid());
                    editor.putString("password", res.getPassword());
                    editor.putString("firstname", res.getFirstname());
                    editor.putString("lastname", res.getLastname());
                    editor.putString("department", res.getDepartment());
                    editor.putString("year", res.getYear());
                    editor.putBoolean("status", true);
                    editor.commit();

                    Toast.makeText(LoginActivity.this, "Succees", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, res.getMassage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<student> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }





}
