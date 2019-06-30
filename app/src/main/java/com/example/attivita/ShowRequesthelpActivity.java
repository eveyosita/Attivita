package com.example.attivita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.attivita.model.StudentFirebase;
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

public class ShowRequesthelpActivity extends AppCompatActivity implements OnMapReadyCallback {

    double latitude,longitude;
    private TextView textview_namerequest,textview_detailrequest;
    private Button button_acceptrequest;

    String user;

    private GoogleMap mMap;
    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_requesthelp);
        getSupportActionBar().hide();

        textview_namerequest = findViewById(R.id.textview_namerequest);
        textview_detailrequest = findViewById(R.id.textview_detailrequest);
        button_acceptrequest = findViewById(R.id.button_acceptrequest);

        Intent getIntent = getIntent();
        String name = getIntent.getStringExtra("name");
        String detail = getIntent.getStringExtra("detail");
        String lati = getIntent.getStringExtra("latitude");
        String logi = getIntent.getStringExtra("longitude");
        user = getIntent.getStringExtra("user");

        latitude = Double.valueOf(lati);
        longitude = Double.valueOf(logi);

        textview_namerequest.setText(name);
        textview_detailrequest.setText(detail);

        button_acceptrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successHelp();
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

    private void successHelp(){
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
                            hashMap.put("statusAccept_helpful", false);
                            reference2.updateChildren(hashMap);

                            System.out.println("RRR---");

                        } else {
                            System.out.println("UpdateERROR");
                        }
                    } else {
                        if(studentFirebase.getUsername().equals(user)){
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Student").child(studentFirebase.getId());
                            Map<String, Object> hashMap = new HashMap<>();
                            hashMap.put("requesthelp", false);
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

        LatLng ny = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        mMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ny, 17));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}
