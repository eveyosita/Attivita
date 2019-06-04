package com.example.attivita;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.model.student;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditprofileActivity extends AppCompatActivity implements View.OnClickListener{

    private CircleImageView circleImageView_profile;
    TextView textView_studentid,textView_firstname,textView_lastname,textView_department,textView_email;
    EditText edittText_nickname,edittText_telnumber;
    Button button_finish;
    String studentid,image_confirm;

    private static final int REQUEST_CODE_LACEPICKER = 1;
    private int SELECT_IMAGE = 1001;
    private int CROP_IMAGE = 2001;
    private static final String MY_PREFS = "prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        getSupportActionBar().hide();

        findViewById(R.id.button_back).setOnClickListener(this);
        findViewById(R.id.button_finisheditprofile).setOnClickListener(this);
        findViewById(R.id.textEditPicprofile).setOnClickListener(this);

        textView_studentid = findViewById(R.id.textStudentid);
        textView_firstname = findViewById(R.id.textFirstname);
        textView_lastname = findViewById(R.id.textLastname);
        edittText_nickname = findViewById(R.id.edittextNickname);
        textView_department = findViewById(R.id.textDepartment);
        edittText_telnumber = findViewById(R.id.edittextTelnumber);
        textView_email = findViewById(R.id.textEmail);
        button_finish = findViewById(R.id.button_finisheditprofile);
        circleImageView_profile = findViewById(R.id.circleImageView_profile);

        SharedPreferences shared = getSharedPreferences(MY_PREFS,
                Context.MODE_PRIVATE);

        studentid = shared.getString("studentId",null);
//        final String firstname = shared.getString("firstname",null);
//        final String lastname = shared.getString("lastname",null);
//        final String department = shared.getString("department",null);
//        final String nickname = shared.getString("nickname",null);
//        final String telnumber = shared.getString("telnumber",null);
//        final String profile_pic = shared.getString("profile_pic",null);

//        textView_studentid.setText(studentid);
//        textView_firstname.setText(firstname);
//        textView_lastname.setText(lastname);
//        textView_department.setText(department);
//
//        if(nickname != null)
//            edittText_nickname.setText(nickname);
//
//        if( telnumber != null)
//            edittText_telnumber.setText(telnumber);
//
//
//        String picture = profile_pic;
//        if(picture == null || picture.isEmpty()){
//            circleImageView_profile.setImageResource(R.drawable.girl);
//        }else{
//            String url = "http://pilot.cp.su.ac.th/usr/u07580553/attivita/picture/profile/"+picture;
//            System.out.println(url);
//            Picasso.get().load(url).into(circleImageView_profile);
//        }
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
                circleImageView_profile.setImageBitmap(bitmap);

            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(EditprofileActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancleEditprofile(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(EditprofileActivity.this);
        builder.setMessage("ต้องการยกเลิกการแก้ไขข้อมูลส่วนตัว ?");
        builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(),
                        "ยกเลิกการแก้ไขข้อมูลส่วนตัว", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public void setProfile(){

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<student> call = apiService.updateprofile(studentid,edittText_nickname.getText().toString(),
                edittText_telnumber.getText().toString(),image_confirm);

        call.enqueue(new Callback<student>() {
            @Override
            public void onResponse(Call<student> call, Response<student> response) {
                student res = response.body();
                if (res.isStatus()){

                    SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("studentId", res.getStudentid());
                    editor.putString("firstname", res.getFirstname());
                    editor.putString("lastname", res.getLastname());
                    editor.putString("department", res.getDepartment());
                    editor.putString("email", res.getEmail());
                    editor.putString("nickname", res.getNickname());
                    editor.putString("telnumber", res.getTelnumber());
                    editor.putString("profile_pic", res.getProfile_pic());
                    editor.putBoolean("status", true);
                    editor.commit();
                    Toast.makeText(EditprofileActivity.this, "แก้ไขข้อมูลเสร็จสิ้น", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<student> call, Throwable t) {
                Toast.makeText(EditprofileActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                cancleEditprofile();
                break;
            case R.id.button_finisheditprofile:
                setProfile();
                finish();
                break;
            case R.id.textEditPicprofile:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Image from Gallery"), SELECT_IMAGE);
                break;

        }

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
                        edittText_nickname.setText(res.getNickname());
                    if( res.getTelnumber() != null)
                        edittText_telnumber.setText(res.getTelnumber());

                    String picture = res.getProfile_pic();
                    if(picture == null){
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
                Toast.makeText(EditprofileActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });



    }
}
