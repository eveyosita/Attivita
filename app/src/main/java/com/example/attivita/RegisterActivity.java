package com.example.attivita;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Calendar;


public class RegisterActivity extends AppCompatActivity {
     EditText id_text;
     EditText pass_text;
     EditText conpass_text;
     EditText fname_text;
     EditText lname_text;
     Spinner depart_spin;
     Button finish_btn;
     String depart;
     ArrayList<String> department = new ArrayList<String>();
    Button mButtonDialog;
    private static final String MY_PREFS = "prefs";


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
        depart_spin = (Spinner) findViewById(R.id.departRegist_spinner);

        createDepartment();
        ArrayAdapter<String> adapterDepart = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, department);
        depart_spin.setAdapter(adapterDepart);

        depart_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               depart = department.get(position);

//                Toast.makeText(RegisterActivity.this,
//                        "Select : " + department.get(position),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = id_text.getText().toString();
                String pass = pass_text.getText().toString();
                String conpass = conpass_text.getText().toString();
                String fname = fname_text.getText().toString();
                String lname = lname_text.getText().toString();
                String year = "25"+id.substring(2,4);
                int y = Calendar.getInstance().get(Calendar.YEAR);
                String yy = (Integer.toString(y+543)).substring(2);


                if(id.substring(0,2).equals("07") && Integer.parseInt(id.substring(2,4)) <= Integer.parseInt(yy) ){
                    if(pass.equals(conpass)) {

                        setStudent(id, pass, fname, lname, depart, year);

                        String s = "รหัสนักศึกษา : "+id+"\nชื่อ : "+fname+"\nนามสกุล : "+lname+"\nสาขาวิชา : "
                                +depart+"\nชั้นปี : "+year+"\n";
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                        builder.setMessage(s);

                        builder.setPositiveButton("ยันยัน", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
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
                    }
                } else {
                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void createDepartment() {
        department.add("เลือกสาขาวิชา");
        department.add("com");
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

    public void setStudent(String id, String pass, String fname, String lname,String depart, String year){

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<student> call = apiService.createuser(id,pass,fname,lname,depart,year);


//        Call<ResponseRegist> call = RetrofitClient
//                .getInstance()
//                .getApi()
//                .createuser(id,pass,fname,lname,nname,depart,year,email,tel);

        call.enqueue(new Callback<student>() {
            @Override
            public void onResponse(Call<student> call, Response<student> response) {
                student res = response.body();
                if (res.isStatus()){
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


                }

                Toast.makeText(RegisterActivity.this, "Succees", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<student> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }




}
