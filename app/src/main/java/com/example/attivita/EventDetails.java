package com.example.attivita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.adapter.EventListAdapter;
import com.example.attivita.model.Event;
import com.example.attivita.model.ResponseCheckin;
import com.example.attivita.model.ResponseJoinevent;
import com.example.attivita.model.StudentFirebase;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetails extends AppCompatActivity implements OnMapReadyCallback {

    TextView textview_eventname,textview_eventDate,textview_eventTime,textview_eventAddress,textview_eventnameaddress
            ,textview_eventCategory,textview_amount,textview_amountmax,textview_eventDetail,btn_Cancel;
    Button btn_joinevent;
    ImageView btn_backhome;
    String studentId;
    int Eventid,eventamount,eventcategoryid;
    double eventlatitude,eventlongitude;
    public boolean boolean_joinevent = false;
    private GoogleMap mMap;
    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    ArrayList<ResponseJoinevent> joineventList = new ArrayList<>();

    private static final String MY_PREFS = "prefs";

    Date Dates = new Date();
    String timeend,eventEnddate,time_current,date_current;
    ResponseCheckin RSEventC, RSJoinC ;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        getSupportActionBar().hide();

        SharedPreferences shared = this.getSharedPreferences(MY_PREFS,
                Context.MODE_PRIVATE);

        studentId = shared.getString("studentId",null);

        Intent getIntent = getIntent();
        String eventId = getIntent.getStringExtra("eventId");
        String eventname = getIntent.getStringExtra("eventName");
        String eventStuId = getIntent.getStringExtra("eventStuId");
        String eventDetail = getIntent.getStringExtra("eventDetail");
        String eventAmount = getIntent.getStringExtra("eventAmount");
        String eventStartdate = getIntent.getStringExtra("eventStartdate");
        eventEnddate = getIntent.getStringExtra("eventEnddate");
        String eventStarttime = getIntent.getStringExtra("eventStarttime");
        String eventEndtime = getIntent.getStringExtra("eventEndtime");
        String eventDepart = getIntent.getStringExtra("eventDepart");
        String eventCategoryId = getIntent.getStringExtra("eventCategoryId");
        String eventYear = getIntent.getStringExtra("eventYear");
        String eventLocation = getIntent.getStringExtra("eventLocation");
        String eventPlacename = getIntent.getStringExtra("eventPlacename");
        String eventLatitude = getIntent.getStringExtra("eventLatitude");
        String eventLongitude = getIntent.getStringExtra("eventLongitude");
        String eventAddress = getIntent.getStringExtra("eventAddress");
        String eventstatus_checkin = getIntent.getStringExtra("eventstatus_checkin");

        textview_eventname = findViewById(R.id.textview_eventname);
        textview_eventDate = findViewById(R.id.textview_eventDate);
        textview_eventTime = findViewById(R.id.textview_eventTime);
        textview_eventAddress = findViewById(R.id.textview_eventAddress);
        textview_eventnameaddress = findViewById(R.id.textview_eventNameaddress);
        textview_eventCategory = findViewById(R.id.textview_eventCategory);
        textview_amount = findViewById(R.id.textview_amount);
        textview_amountmax = findViewById(R.id.textview_amountmax);
        textview_eventDetail = findViewById(R.id.textview_eventDetail);
        btn_joinevent = findViewById(R.id.button_joinevent);
        btn_backhome = findViewById(R.id.button_backhome);
        btn_Cancel = findViewById(R.id.buttonCancel);

        Eventid = Integer.valueOf(eventId);
        eventamount = Integer.valueOf(eventAmount);
        eventlatitude = Double.valueOf(eventLatitude);
        eventlongitude = Double.valueOf(eventLongitude);

        String getOnlyDateStart = eventStartdate.substring(8);
        String getOnlyDateEnd = eventEnddate.substring(8);
        String getOnlyMonth = getMonth(eventEnddate.substring(5,7));
        String getOnlyYear = ""+(Integer.parseInt(eventEnddate.substring(0,4))+543);
        String date;
        if(getOnlyDateStart.equals(getOnlyDateEnd)){
            date = getOnlyDateStart + " " + getOnlyMonth + " " +getOnlyYear;
        } else {
            date = getOnlyDateStart + " - " + getOnlyDateEnd + " " + getOnlyMonth + " " +getOnlyYear;
        }

        String timeStart = eventStarttime.substring(0,5);
        timeend = eventEndtime.substring(0,5);
        String time = timeStart+" - "+timeend +" น.";

        String category = getCategory(eventCategoryId);

        textview_eventname.setText(eventname);
        textview_eventDate.setText(date);
        textview_eventTime.setText(time);
        textview_eventAddress.setText(eventAddress);
        textview_eventnameaddress.setText(eventPlacename);
        textview_eventCategory.setText(category);
        textview_amountmax.setText(eventAmount);
        textview_eventDetail.setText(eventDetail);

        System.out.println("STC "+eventstatus_checkin);

        btn_joinevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinEvent();
                checkAmount();

            }
        });

        if(btn_Cancel.getText().toString().equals("ยกเลิก")){
            btn_Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelJoinEvent();
                }
            });
        }

        btn_backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
        date_current = formatter.format(Dates);
        time_current = formatter2.format(Dates);

        if(eventstatus_checkin.equals("0")){
            System.out.println("HEY 0");
            if(date_current.compareTo(eventStartdate) >= 0 && time_current.compareTo(timeStart) >= 0){  //ในวัน
                System.out.println("IN");
                setEventStatus_chackin(1);
            } else if(date_current.compareTo(eventEnddate) > 0 && time_current.compareTo(timeend) > 0){   //นอกวัน
                System.out.println("OUT");
                getJoinStatuscheckin0();
            } else if(date_current.compareTo(eventStartdate) <= 0 && time_current.compareTo(timeStart) < 0){   //ยังไม่ถึงวัน
                System.out.println("COMING");
                checkCountJoinEvent();
            }
        }
        if(eventstatus_checkin.equals("1")){
            System.out.println("HEY 1");
            if(date_current.compareTo(eventEnddate) > 0 && time_current.compareTo(timeend) > 0){   //นอกวัน
                System.out.println("OUT 1");
                setEventStatus_chackin(0);
            } else if(date_current.compareTo(eventStartdate) >= 0 && time_current.compareTo(timeStart) >= 0) {   //ในวัน
                System.out.println("IN 1");
                getJoinStatuscheckin1();
            }

        }

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

    }

    private String getMonth(String month){
        switch (month){
            case "01":
                return "มกราคม";
            case "02":
                return "กุมภาพันธ์";
            case "03":
                return "มีนาคม";
            case "04":
                return "เมษายม";
            case "05":
                return "พฤษภาคม";
            case "06":
                return "มิถุนายม";
            case "07":
                return "กรกฎาคม";
            case "08":
                return "สิงหาคม";
            case "09":
                return "กันยายน";
            case "10":
                return "ตุลาคม";
            case "11":
                return "พฤศจิกายน";
            case "12":
                return "ธันวาคม";
            default: break;
        }
        return "";
    }

    private String getCategory(String cate){
        switch (cate){
            case "0" :
                return "ขายของ";
            case "1":
                return "จิตอาสา";
            case "2":
                return "ติวหนังสือ";
            case "3":
                return "ทำบุญไหว้พระ";
            case "4":
                return "ท่องเที่ยว";
            case "5":
                return "เล่นเกมส์";
            case "6":
                return "สังสรรค์";
            case "7":
                return "ออกกำลังกาย";
            default: break;
        }
        return "";
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

    public void setEventStatus_chackin(int status_chackin){
        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ResponseCheckin> call = apiService.setEventStatuscheckin(Eventid,status_chackin);

        call.enqueue(new Callback<ResponseCheckin>() {
            @Override
            public void onResponse(Call<ResponseCheckin> call, Response<ResponseCheckin> response) {
                ResponseCheckin res = response.body();
                if (res.isStatus()){
                    RSEventC = new ResponseCheckin(res.getStatus_chackin());
                    System.out.println("EVENTCHECK"+res.getMessage());
                } else {
                    System.out.println("EVENTCHECK"+res.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseCheckin> call, Throwable t) {
                Toast.makeText(EventDetails.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    public void setJoinStatus_chackin(int status_chackin){
        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ResponseCheckin> call = apiService.setJoinStatuscheckin(Eventid,studentId,status_chackin);

        call.enqueue(new Callback<ResponseCheckin>() {
            @Override
            public void onResponse(Call<ResponseCheckin> call, Response<ResponseCheckin> response) {
                ResponseCheckin res = response.body();
                if (res.isStatus()){
                    RSJoinC = new ResponseCheckin(res.getStatus_chackin());
                    System.out.println("EVENTJOIN"+res.getMessage());
                } else {
                    System.out.println("EVENTJOIN"+res.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseCheckin> call, Throwable t) {
                Toast.makeText(EventDetails.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    public void getJoinStatuscheckin0(){
        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ResponseCheckin> call = apiService.getJoinStatuscheckin(Eventid,studentId);

        call.enqueue(new Callback<ResponseCheckin>() {
            @Override
            public void onResponse(Call<ResponseCheckin> call, Response<ResponseCheckin> response) {
                ResponseCheckin res = response.body();
                if (res.isStatus()){
                   // RSJoinC = new ResponseCheckin(res.getStatus_chackin());

                    if(res.getStatus_chackin().equals("0")){

                        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");

                        System.out.println("EEE "+reference);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                System.out.println("EEE2.1");
                                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                                    StudentFirebase studentFirebase = snapshot.getValue(StudentFirebase.class);
                                    assert studentFirebase != null;
                                    assert firebaseUser != null;

                                    System.out.println("EEE3 " + studentFirebase.getUsername());

                                    if(studentFirebase.getId().equals(firebaseUser.getUid())){
                                        if(studentFirebase.getStatus_star()>=1){
                                            int star = studentFirebase.getStatus_star()-1;
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("status_star", star);
                                            reference.updateChildren(hashMap);
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
                    System.out.println("EVENTCHECK"+res.getMessage());
                } else {
                    System.out.println("EVENTCHECK"+res.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseCheckin> call, Throwable t) {
                Toast.makeText(EventDetails.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    public void getJoinStatuscheckin1(){
        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ResponseCheckin> call = apiService.getJoinStatuscheckin(Eventid,studentId);

        call.enqueue(new Callback<ResponseCheckin>() {
            @Override
            public void onResponse(Call<ResponseCheckin> call, Response<ResponseCheckin> response) {
                ResponseCheckin res = response.body();
                if (res.isStatus()){
                    //RSJoinC = new ResponseCheckin(res.getStatus_chackin());
                    System.out.println("IN 1-1"+ res.getStatus_chackin());
                    if(res.getStatus_chackin().equals("0")){

                        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");

                        System.out.println("RRR "+reference);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                System.out.println("RRR2.1");
                                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                                    final StudentFirebase studentFirebase = snapshot.getValue(StudentFirebase.class);
                                    assert studentFirebase != null;
                                    assert firebaseUser != null;

                                    System.out.println("RRR3 " + studentFirebase.getUsername());

                                    if(studentFirebase.getId().equals(firebaseUser.getUid())){

                                        System.out.println("DIS "+distanceFrom_in_Km(eventlatitude,eventlongitude,studentFirebase.getLatitude(),studentFirebase.getLongitude()));

                                        if (distanceFrom_in_Km(eventlatitude,eventlongitude,studentFirebase.getLatitude(),studentFirebase.getLongitude()) <= 200.00 ){
                                            btn_Cancel.setText("ยืนยันการเข้าร่วม");
                                            btn_Cancel.setTextColor(getColor(R.color.colorRed));
                                            btn_Cancel.setBackgroundResource(R.drawable.backgroundlinegary);
                                            btn_joinevent.setText("เข้าร่วมแล้ว");
                                            btn_joinevent.setEnabled(false);
                                            btn_Cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    setJoinStatus_chackin(1);
                                                    btn_Cancel.setText("ยืนยันเข้าร่วมเรียบร้อย");
                                                    btn_Cancel.setTextColor(getColor(R.color.colorGray));
                                                    btn_Cancel.setEnabled(false);

                                                    if(studentFirebase.getStatus_star()<5){
                                                        int star = studentFirebase.getStatus_star()+1;
                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("status_star", star);
                                                        reference.updateChildren(hashMap);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                System.out.println("RRR2.2");
                            }
                        });
                    } else if(res.getStatus_chackin().equals("1")){
                        btn_joinevent.setText("เข้าร่วมแล้ว");
                        btn_joinevent.setEnabled(false);
                        btn_Cancel.setText("ยืนยันเข้าร่วมเรียบร้อย");
                        btn_Cancel.setTextColor(getColor(R.color.colorGray));
                        btn_Cancel.setBackgroundResource(R.drawable.backgroundlinegary);
                        btn_Cancel.setEnabled(false);
                    }
                    System.out.println("EVENTCHECK"+res.getMessage());
                } else {
                    System.out.println("EVENTCHECK"+res.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseCheckin> call, Throwable t) {
                Toast.makeText(EventDetails.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    private boolean checkAmount(){
        int amountmax = Integer.valueOf(textview_amountmax.getText().toString());
        if(joineventList.size() < amountmax){
            return true;
        } else {
            btn_joinevent.setText("ผู้เข้าร่วมครบแล้ว");
            btn_joinevent.setEnabled(false);
            return false;
        }
    }

    private boolean checkJoinEvent(){
        System.out.println("Size :"+ joineventList.size() +" std :"+ studentId);
        for(int i=0 ; i<joineventList.size() ; i++){
            System.out.println("Size :"+ i +" std :"+ joineventList.get(i).getStudentId());
            if(joineventList.get(i).getStudentId().equals(studentId) ){
                return true;
            }
        }
        return false;
    }

    public void joinEvent(){
        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ResponseJoinevent> call = apiService.joinevent(studentId,Eventid);
        call.enqueue(new Callback<ResponseJoinevent>() {
            @Override
            public void onResponse(Call<ResponseJoinevent> call, Response<ResponseJoinevent> response) {
                ResponseJoinevent res = response.body();
                if (!res.isStatus()){
                    btn_joinevent.setText("เข้าร่วมแล้ว");
                    btn_joinevent.setEnabled(false);
                    btn_Cancel.setText("ยกเลิก");
                    btn_Cancel.setBackgroundResource(R.drawable.backgroundlinegary);

                    Toast.makeText(EventDetails.this, res.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    checkCountJoinEvent();
                }
            }
            @Override
            public void onFailure(Call<ResponseJoinevent> call, Throwable t) {
                Toast.makeText(EventDetails.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    public void checkCountJoinEvent(){
        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ArrayList<ResponseJoinevent>> call = apiService.checkjoinevent(Eventid);
        call.enqueue(new Callback<ArrayList<ResponseJoinevent>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseJoinevent>> call, Response<ArrayList<ResponseJoinevent>> response) {
                ArrayList<ResponseJoinevent>  res = response.body();
                joineventList.clear();
                if (res.size() != 0) {
                    for (ResponseJoinevent r : res) {
                        joineventList.add(new ResponseJoinevent(r.getStudentId()));
                    }
                    String amount_s = String.valueOf(joineventList.size());
                    textview_amount.setText(amount_s);
                    if(checkJoinEvent()){
                        btn_joinevent.setText("เข้าร่วมแล้ว");
                        btn_joinevent.setEnabled(false);
                        btn_Cancel.setText("ยกเลิก");
                        btn_Cancel.setBackgroundResource(R.drawable.backgroundlinegary);
                    } else {
                        checkAmount();
                    }
                } else {
                    String amount_s = String.valueOf(joineventList.size());
                    textview_amount.setText(amount_s);
                    Toast.makeText(EventDetails.this, "No event now", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ResponseJoinevent>> call, Throwable t) {
                Toast.makeText(EventDetails.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    public void cancelJoinEvent(){
        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ResponseJoinevent> call = apiService.canceljoin(studentId,Eventid);
        call.enqueue(new Callback<ResponseJoinevent>() {
            @Override
            public void onResponse(Call<ResponseJoinevent> call, Response<ResponseJoinevent> response) {
                ResponseJoinevent res = response.body();

                if (res.isStatus()){
                    btn_joinevent.setText("เข้าร่วม");
                    btn_joinevent.setEnabled(true);
                    btn_Cancel.setText("");
                    btn_Cancel.setBackgroundResource(0);
                    checkAmount();

                    Toast.makeText(EventDetails.this, res.getMessage(), Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(EventDetails.this, res.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseJoinevent> call, Throwable t) {
                Toast.makeText(EventDetails.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
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
        System.out.println(" 4-ON RES-4");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        System.out.println(" 4-ON STA-4");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        System.out.println(" 4-ON STO-4");
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
        System.out.println(" 4-ON PAU-4");
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
        System.out.println(" 4-ON DES-4");
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
        System.out.println(" 4-ON LOW-4");
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

        LatLng ny = new LatLng(eventlatitude, eventlongitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        mMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ny, 17));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }

}
