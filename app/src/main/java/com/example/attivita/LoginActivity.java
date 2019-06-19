package com.example.attivita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attivita.model.StudentPHP;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

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
                    checkLogin();
                } else {
                    Toast.makeText(LoginActivity.this, "กรุณากรอกรหัสนักศึกษาและรหัสผ่านให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    public void checkLogin(){
        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<StudentPHP> call = apiService.loginuser(id,pass);
        call.enqueue(new Callback<StudentPHP>() {
            @Override
            public void onResponse(Call<StudentPHP> call, Response<StudentPHP> response) {
                final StudentPHP res = response.body();
                if(res.isStatus()) {

                    Toast.makeText(LoginActivity.this, "Succees", Toast.LENGTH_SHORT).show();

                    auth.signInWithEmailAndPassword(res.getEmail(), pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        editor.putString("studentId", res.getStudentid());
                                        editor.putString("password", res.getPassword());
                                        editor.putString("firstname", res.getFirstname());
                                        editor.putString("lastname", res.getLastname());
                                        editor.putString("department", res.getDepartment());
                                        editor.putString("year", res.getYear());
                                        editor.putString("email", res.getEmail());
                                        editor.putBoolean("status", true);
                                        editor.commit();
                                        Toast.makeText(LoginActivity.this, "YAHH", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);

                                    } else {
                                        Toast.makeText(LoginActivity.this, "SADD", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onFailure(Call<StudentPHP> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }





}
