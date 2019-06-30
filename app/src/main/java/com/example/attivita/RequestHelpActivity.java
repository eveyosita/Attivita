package com.example.attivita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.TagLostException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.model.Eventhelp;
import com.example.attivita.model.StudentFirebase;
import com.example.attivita.model.StudentPHP;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestHelpActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView textview_namerequest,textview_detailrequest;
    Button button_acceptrequest,button_cancelrequest;

    private GoogleMap mMap;
    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    SharedPreferences shared;
    private static final String MY_PREFS = "prefs";

    Eventhelp eventhelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_help);
        getSupportActionBar().hide();

        textview_namerequest = findViewById(R.id.textview_namerequest);
        textview_detailrequest = findViewById(R.id.textview_detailrequest);
        button_acceptrequest = findViewById(R.id.button_acceptrequest);
        button_cancelrequest = findViewById(R.id.button_cancelrequest);

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

                    System.out.println("RRR- " + studentFirebase.getUsername());

                    if(studentFirebase.getId().equals(firebaseUser.getUid())){

                        System.out.println("RRR-- ");
                        textview_detailrequest.setText(studentFirebase.getDetail_helpful());
                        eventhelp = new Eventhelp(studentFirebase.getHelped(),studentFirebase.getHelped_latitude(),studentFirebase.getHelped_longitude());
                        getStudent(studentFirebase.getHelped());

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("RRR2.2");
            }
        });

        button_acceptrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptHelp();
                finish();
            }
        });

        button_cancelrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelHelp();
                finish();
            }
        });

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

    }

    private void cancelHelp(){
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

                    System.out.println("RRR- " + studentFirebase.getUsername());

                    if(studentFirebase.getId().equals(firebaseUser.getUid())){

                        System.out.println("RRR-- ");

                        if (studentFirebase.isStatus_helpful()){
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Student").child(firebaseUser.getUid());
                            Map<String, Object> hashMap = new HashMap<>();
                            hashMap.put("helped", "defult");
                            hashMap.put("helped_latitude", 0.0);
                            hashMap.put("helped_longitude", 0.0);
                            hashMap.put("status_helpful", false);
                            hashMap.put("detail_helpful", "defult");
                            reference2.updateChildren(hashMap);

                            shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putBoolean("status_notification", false);
                            editor.apply();
                            System.out.println("RRR---");

                        } else {
                            System.out.println("UpdateERROR");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("RRR2.2");
            }
        });
    }

    private void acceptHelp() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");

        System.out.println("RRR " + reference);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("RRR2.1");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StudentFirebase studentFirebase = snapshot.getValue(StudentFirebase.class);
                    assert studentFirebase != null;
                    assert firebaseUser != null;

                    System.out.println("RRR- " + studentFirebase.getUsername());

                    if (studentFirebase.getId().equals(firebaseUser.getUid())) {

                        System.out.println("RRR-- ");

                        if (studentFirebase.isStatus_helpful()) {
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Student").child(firebaseUser.getUid());
                            Map<String, Object> hashMap = new HashMap<>();
                            hashMap.put("statusAccept_helpful", true);
                            reference2.updateChildren(hashMap);

                            shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putBoolean("status_notification", false);
                            editor.apply();
                            System.out.println("RRR---");

                        } else {
                            System.out.println("UpdateERROR");
                        }
                    } else {
                        if(studentFirebase.getHelped().equals(eventhelp.getStudentId())){
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Student").child(studentFirebase.getId());
                            Map<String, Object> hashMap = new HashMap<>();
                            hashMap.put("helped", "defult");
                            hashMap.put("helped_latitude", 0.0);
                            hashMap.put("helped_longitude", 0.0);
                            hashMap.put("status_helpful", false);
                            hashMap.put("detail_helpful", "defult");
                            reference2.updateChildren(hashMap);
                            reference2.updateChildren(hashMap);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("RRR2.2");
            }
        });
    }

    public void getStudent(String studentid){
        final APIInterface apiService2 = RetrofitClient.getClient().create(APIInterface.class);
        Call<StudentPHP> call2 = apiService2.readstudent(studentid);
        call2.enqueue(new Callback<StudentPHP>() {
            @Override
            public void onResponse(Call<StudentPHP> call, Response<StudentPHP> response) {
                StudentPHP res = response.body();

                if (res.isStatus()){
                    String name = "จากคุณ "+res.getFirstname()+" "+res.getLastname();
                    textview_namerequest.setText(name);
                    System.out.println("EEEE");

                }
            }
            @Override
            public void onFailure(Call<StudentPHP> call, Throwable t) {
                Toast.makeText(RequestHelpActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }


    // google mapView

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMinZoomPreference(12);
        mMap.setIndoorEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        LatLng ny = new LatLng(eventhelp.getLatitude(), eventhelp.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        mMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ny, 17));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }

}
