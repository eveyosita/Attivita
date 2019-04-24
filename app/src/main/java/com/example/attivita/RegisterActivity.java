package com.example.attivita;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {
     EditText id_text;
     EditText pass_text;
     EditText conpass_text;
     EditText fname_text;
     EditText lname_text,email_text;
     Spinner depart_spin;
     Button finish_btn;
     String stdid,passw,conpass,fname,lname,year,depart,email;
     int posit=-1;

     ArrayList<String> department = new ArrayList<String>();
    Button mButtonDialog;
    private static final String MY_PREFS = "prefs";

    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        finish_btn = (Button) findViewById(R.id.finish_button);

        id_text = (EditText) findViewById(R.id.id2_editText);
        pass_text = (EditText) findViewById(R.id.password2_editText);
        conpass_text = (EditText) findViewById(R.id.conpassword2_editText);
        fname_text = (EditText) findViewById(R.id.fname_editText);
        lname_text = (EditText) findViewById(R.id.lname_editText);
        email_text = (EditText) findViewById(R.id.email_editText);
        depart_spin = (Spinner) findViewById(R.id.departRegist_spinner);

        auth = FirebaseAuth.getInstance();

        createDepartment();
        ArrayAdapter<String> adapterDepart = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, department);
        depart_spin.setAdapter(adapterDepart);

        depart_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position != 0){
                    depart = department.get(position);
                    posit = position;
                } else {
                    posit = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stdid = id_text.getText().toString();
                passw = pass_text.getText().toString();
                conpass = conpass_text.getText().toString();
                fname = fname_text.getText().toString();
                lname = lname_text.getText().toString();
                email = email_text.getText().toString();

                System.out.println("Email : "+ email);

                if(stdid.isEmpty() || passw.isEmpty() || conpass.isEmpty() || fname.isEmpty()
                        || lname.isEmpty() || email.isEmpty() || posit==0){

                    Toast.makeText(RegisterActivity.this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();

                }  else {
                    year = "25"+stdid.substring(2,4);
                    int y = Calendar.getInstance().get(Calendar.YEAR);
                    String yy = (Integer.toString(y+543)).substring(2);
                    if(stdid.substring(0,2).equals("07") && Integer.parseInt(stdid.substring(2,4)) <= Integer.parseInt(yy)
                            && passw.length()>=6 ) {
                        if(passw.length() >=8 && passw.equals(conpass)){
                            String s = "รหัสนักศึกษา : " + stdid + "\nชื่อ : " + fname + "\nนามสกุล : " + lname + "\nสาขาวิชา : "
                                    + depart + "\nชั้นปี : " + year + "\n";
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage(s);

                            builder.setPositiveButton("ยันยัน", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    setStudent();
                                    //finish();
//                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
//                            startActivity(i);
                                }
                            });
                            builder.setNegativeButton("แก้ไข", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                            //finish();
//                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
//                    startActivity(i);
                        } else {
                            Toast.makeText(RegisterActivity.this, "รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "รหัสนักศึกษาไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }

    private void createDepartment() {
        department.add("เลือกสาขาวิชา");
        department.add("คณิตศาสตร์");
        department.add("ชีววิทยา");
        department.add("เคมี");
        department.add("ฟิสิกส์");
        department.add("สถิติ");
        department.add("วิทยาศาสตร์สิ่งแวดล้อม");
        department.add("วิทยาการคอมพิวเตอร์");
        department.add("จุลชีววิทยา");
        department.add("คณิตศาสตร์ประยุกต์");
        department.add("เทคโนโลยีสารสนเทศ");
        department.add("ครูฟิสิกส์");
        department.add("วิทยาการข้อมูล");

    }

    public void setStudent(){

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<student> call = apiService.createuser(stdid,passw,fname,lname,depart,year,email);

        call.enqueue(new Callback<student>() {
            @Override
            public void onResponse(Call<student> call, Response<student> response) {
                student res = response.body();
                if (res.isStatus()){
                    auth.createUserWithEmailAndPassword(email, passw)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = auth.getCurrentUser();
                                        String userid = firebaseUser.getUid();

                                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("id", userid);
                                        hashMap.put("username", email);
                                        hashMap.put("imagerUrl", "defult");

                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "กรอกข้อมูลผิดพลาด", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("studentId", res.getStudentid());
                    editor.putString("password", res.getPassword());
                    editor.putString("firstname", res.getFirstname());
                    editor.putString("lastname", res.getLastname());
                    editor.putString("department", res.getDepartment());
                    editor.putString("year", res.getYear());
                    editor.putString("email", res.getEmail());
                    editor.putBoolean("status", true);
                    editor.commit();
                    Toast.makeText(RegisterActivity.this, "ลงทะเบียนเสร็จสิ้น", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<student> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }




}
