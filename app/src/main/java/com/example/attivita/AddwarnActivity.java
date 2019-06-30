package com.example.attivita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.attivita.adapter.StudentFirebaseAdapter;
import com.example.attivita.model.Eventhelp;
import com.example.attivita.model.StudentFirebase;
import com.example.attivita.model.StudentPHP;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddwarnActivity extends AppCompatActivity {

    Button but_finishwarn,button_back;
    private TextInputLayout editText_eventdetail;
    double latitude_current,longitude_current;
    private int count = 0;
    String studentId;

    private static final String MY_PREFS = "prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwarn);
        getSupportActionBar().hide();

        SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        studentId = shared.getString("studentId",null);


        button_back = findViewById(R.id.button_back);
        but_finishwarn = findViewById(R.id.finishwarn_button);
        editText_eventdetail = findViewById(R.id.editText_eventdetail);

        Intent getIntent = getIntent();
        String Latitude = getIntent.getStringExtra("Latitude_current");
        String Longitude = getIntent.getStringExtra("Longitude_current");
        latitude_current = Double.valueOf(Latitude);
        longitude_current = Double.valueOf(Longitude);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        but_finishwarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestHelp();
                Toast.makeText(AddwarnActivity.this, "ขอความช่วยเหลือเรียบร้อย", Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }

    private void requestHelp(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");

        System.out.println("RRR "+reference);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("RRR2.1");
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    StudentFirebase studentFirebase = snapshot.getValue(StudentFirebase.class);
                    assert studentFirebase != null;
                    assert firebaseUser != null;

                    System.out.println("RRR3 " + studentFirebase.getUsername());
                    if (count == 5)
                        break;
                    if(!studentFirebase.getId().equals(firebaseUser.getUid())){

                        System.out.println("DIS "+distanceFrom_in_Km(latitude_current,longitude_current,studentFirebase.getLatitude(),studentFirebase.getLongitude()));

                        if (distanceFrom_in_Km(latitude_current,longitude_current,studentFirebase.getLatitude(),studentFirebase.getLongitude()) <= 1200.00 && !studentFirebase.isStatus_helpful()){
                            count++;
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Student").child(studentFirebase.getId());
                            Map<String, Object> hashMap = new HashMap<>();
                            hashMap.put("helped_latitude", latitude_current);
                            hashMap.put("helped_longitude", longitude_current);
                            hashMap.put("helped", studentId);
                            hashMap.put("status_helpful", true);
                            hashMap.put("statusAccept_helpful", false);
                            hashMap.put("detail_helpful", editText_eventdetail.getEditText().getText().toString());
                            reference2.updateChildren(hashMap);

                        } else {
                            System.out.println("UpdateERROR");
                        }
                    } else {
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Student").child(firebaseUser.getUid());
                        Map<String, Object> hashMap = new HashMap<>();
                        hashMap.put("requesthelp", true);
                        reference2.updateChildren(hashMap);
                    }
                }
                System.out.println("COUNT "+count);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("RRR2.2");
            }
        });
    }

    private double distanceFrom_in_Km(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }


}
