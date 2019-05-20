package com.example.attivita;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.model.student;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

    private CircleImageView circleImageView_profile;
    TextView textView_studentid,textView_firstname,textView_lastname,textView_nickname
            ,textView_department,textView_telnumber,textView_email;
    Button btn_editprofile,btn_logout,btn_back;
    private static final String MY_PREFS = "prefs";
    String studentid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();

        btn_back = findViewById(R.id.button_back);
        circleImageView_profile = findViewById(R.id.circleImageView_profile);
        btn_logout =  findViewById(R.id.button_logout);
        btn_editprofile = findViewById(R.id.button_editprofile);
        textView_studentid = findViewById(R.id.textStudentid);
        textView_firstname = findViewById(R.id.textFirstname);
        textView_lastname = findViewById(R.id.textLastname);
        textView_nickname = findViewById(R.id.textNickname);
        textView_department = findViewById(R.id.textDepartment);
        textView_telnumber = findViewById(R.id.textTelnumber);
        textView_email = findViewById(R.id.textEmail);


        SharedPreferences shared = getSharedPreferences(MY_PREFS,
                Context.MODE_PRIVATE);

        studentid = shared.getString("studentId",null);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, EditprofileActivity.class));
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("ต้องการออกจากระบบใช่หรือไม่?");
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences shared = getSharedPreferences(MY_PREFS,
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();

                        //FirebaseAuth.getInstance().signOut();

                        editor.clear();
                        editor.commit();

                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                        finish();

                    }
                });
                builder.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<student> call = apiService.readstudent(studentid);

        call.enqueue(new Callback<student>() {
            @Override
            public void onResponse(Call<student> call, Response<student> response) {
                student res = response.body();
                if (res.isStatus()){

                    textView_studentid.setText(res.getStudentid());
                    textView_firstname.setText(res.getFirstname());
                    textView_lastname.setText(res.getLastname());
                    textView_department.setText(res.getDepartment());
                    textView_email.setText(res.getEmail());

                    if( res.getNickname() != null)
                        textView_nickname.setText(res.getNickname());
                    if( res.getTelnumber() != null)
                        textView_telnumber.setText(res.getTelnumber());

                    String picture = res.getProfile_pic();
                    if(picture.isEmpty()){
                        circleImageView_profile.setImageResource(R.drawable.girl);
                    }else{
                        String url = "http://pilot.cp.su.ac.th/usr/u07580553/attivita/picture/profile/"+picture;
                        System.out.println(url);
                        Picasso.get().load(url).into(circleImageView_profile);
                    }

                }
            }
            @Override
            public void onFailure(Call<student> call, Throwable t) {
                Toast.makeText(SettingActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });



    }

}
