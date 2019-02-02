package com.example.attivita;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attivita.fragment.ProfileFragment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btn_regist;
    Button btn_login;
    EditText et_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        btn_regist = (Button) findViewById(R.id.register_button);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, page2.class);
                startActivity(i);

            }
        });

        et_id = (EditText) findViewById(R.id.id_editText);


        btn_login = (Button) findViewById(R.id.login_button);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_pass = (EditText) findViewById(R.id.password_editText);


                String id = et_id.getText().toString();
                String pass = et_pass.getText().toString();

                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .loginuser(id,pass);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(LoginActivity.this, "Succees", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "StudentId or Password ", Toast.LENGTH_LONG).show();

                    }


                });
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        });

//        String id = et_id.getText().toString();
//        Intent i = new Intent(LoginActivity.this, ProfileFragment.class);
//        i.putExtra("id",id);


    }





}
