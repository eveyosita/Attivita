package com.example.attivita;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.attivita.model.StudentPHP;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
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
     ImageView photo_btn;
     String stdid,passw,conpass,fname,lname,year,depart,email,image_confirm = "";
     int posit=-1;

     ArrayList<String> department = new ArrayList<String>();
    Button mButtonDialog;
    private static final String MY_PREFS = "prefs";

    private int SELECT_IMAGE = 1001;
    private int CROP_IMAGE = 2001;

    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        finish_btn = (Button) findViewById(R.id.finish_button);
        photo_btn = findViewById(R.id.photo_Button);
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

        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Image from Gallery"), SELECT_IMAGE);

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
                        || lname.isEmpty() || email.isEmpty() || posit==0 || image_confirm.isEmpty()){

                    Toast.makeText(RegisterActivity.this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();

                }  else {
                    year = "25"+stdid.substring(2,4);
                    int y = Calendar.getInstance().get(Calendar.YEAR);
                    String yy = (Integer.toString(y+543)).substring(2);
                    if(stdid.substring(0,2).equals("07") && Integer.parseInt(stdid.substring(2,4)) <= Integer.parseInt(yy)) {
                        if(checkPassword() && checkEmail()) {
                            String s = "รหัสนักศึกษา : " + stdid + "\nชื่อ : " + fname + "\nนามสกุล : " + lname + "\nสาขาวิชา : "
                                    + depart + "\nชั้นปี : " + year + "\n";
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage(s);

                            builder.setPositiveButton("ยันยัน", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    setStudent();

                                }
                            });
                            builder.setNegativeButton("แก้ไข", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
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

    public String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void CropImage(Uri uri) {
        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 360);
            CropIntent.putExtra("outputY", 360);
            CropIntent.putExtra("aspectX", 1);
            CropIntent.putExtra("aspectY", 1);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);
            startActivityForResult(CropIntent, CROP_IMAGE);
        } catch (ActivityNotFoundException ex) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                if (data != null) {
                    CropImage(data.getData());
                }
            } else if (requestCode == CROP_IMAGE) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                image_confirm = imageToString(bitmap);


            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(RegisterActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    public void setStudent(){

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<StudentPHP> call = apiService.createuser(stdid,passw,fname,lname,depart,year,email,image_confirm);

        call.enqueue(new Callback<StudentPHP>() {
            @Override
            public void onResponse(Call<StudentPHP> call, Response<StudentPHP> response) {
                StudentPHP res = response.body();
                if (res.isStatus()){

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
                    editor.putBoolean("status_notification", false);
                    editor.apply();

                    auth.createUserWithEmailAndPassword(email, passw)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = auth.getCurrentUser();

                                        assert firebaseUser != null;
                                        String userid = firebaseUser.getUid();

                                        reference = FirebaseDatabase.getInstance().getReference("Student").child(userid);

                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("id", userid);
                                        hashMap.put("username", stdid);
                                        hashMap.put("latitude", "defult");
                                        hashMap.put("longitude", "defult");
                                        hashMap.put("requesthelp", false);
                                        hashMap.put("helped", "defult");
                                        hashMap.put("status_helpful", false);
                                        hashMap.put("detail_helpful", "defult");

                                        System.out.println("DDD");

                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(RegisterActivity.this, "ลงทะเบียนเสร็จสิ้น", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "กรอกข้อมูลผิดพลาด", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });




                }


            }

            @Override
            public void onFailure(Call<StudentPHP> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    private boolean checkPassword() {
        if (passw.length() >=6 && passw.equals(conpass)) {
            return true;
        } else {
            Toast.makeText(RegisterActivity.this, "รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean checkEmail() {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            Toast.makeText(RegisterActivity.this, "รูปแบบอีเมลไม่ถูกต้อง", Toast.LENGTH_LONG).show();
            return false;
        }
    }


}
