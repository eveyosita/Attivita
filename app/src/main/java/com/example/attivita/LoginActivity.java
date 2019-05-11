package com.example.attivita;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

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

    SharedPreferences shared;
    SharedPreferences.Editor editor;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        editor = shared.edit();

        et_id = (EditText) findViewById(R.id.id_editText);
        et_pass = (EditText) findViewById(R.id.password_editText);

        auth = FirebaseAuth.getInstance();


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
                int y = Calendar.getInstance().get(Calendar.YEAR);
                String yy = (Integer.toString(y+543)).substring(2);

                if(id.isEmpty() || pass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "กรุณากรอกรหัสนักศึกษาและรหัสผ่าน", Toast.LENGTH_SHORT).show();
                } else if(id.substring(0,2).equals("07") && Integer.parseInt(id.substring(2,4)) <= Integer.parseInt(yy)){
                    checkLogin(id,pass);
                } else {
                    Toast.makeText(LoginActivity.this, "กรุณากรอกรหัสนักศึกษาและรหัสผ่านให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (firebaseUser != null) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
//    }


    public void checkLogin(String id, String pass){
        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<student> call = apiService.loginuser(id,pass);
        call.enqueue(new Callback<student>() {
            @Override
            public void onResponse(Call<student> call, Response<student> response) {
                final student res = response.body();
                if(res.isStatus()){

                    System.out.println("Email : "+res.getEmail());
                    System.out.println("Pass : "+res.getPassword());
                    Toast.makeText(LoginActivity.this, "Succees", Toast.LENGTH_SHORT).show();

//                    auth.signInWithEmailAndPassword(res.getEmail(),res.getPassword())
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    editor.putString("studentId", res.getStudentid());
                                    editor.putString("password", res.getPassword());
                                    editor.putString("firstname", res.getFirstname());
                                    editor.putString("lastname", res.getLastname());
                                    editor.putString("department", res.getDepartment());
                                    editor.putString("year", res.getYear());
                                    editor.putString("email", res.getEmail());
                                    editor.putBoolean("status", true);
                                    editor.commit();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
//                                }
//                            });

//                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(i);
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
