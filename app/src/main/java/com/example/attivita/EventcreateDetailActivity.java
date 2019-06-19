package com.example.attivita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.model.Event;
import com.example.attivita.model.ResponseJoinevent;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventcreateDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView textview_eventname,textview_eventDate,textview_eventTime,textview_eventAddress,textview_eventnameaddress
            ,textview_eventCategory,textview_amount,textview_amountmax,textview_eventDetail,btn_Edit;
    Button btn_joinevent;
    ImageView btn_back;
    String studentId;
    String eventId;
    int Eventid,eventamount;
    double eventlatitude,eventlongitude;
    private GoogleMap mMap;
    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    ArrayList<ResponseJoinevent> joineventList = new ArrayList<>();
    Event event = new Event();

    private static final String MY_PREFS = "prefs";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventcreate_detail);
        getSupportActionBar().hide();

        SharedPreferences shared = this.getSharedPreferences(MY_PREFS,
                Context.MODE_PRIVATE);

        studentId = shared.getString("studentId",null);

        Intent getIntent = getIntent();
        eventId = getIntent.getStringExtra("eventId");

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
        btn_back = findViewById(R.id.button_backhome);
        btn_Edit = findViewById(R.id.buttonEdit);

        Eventid = Integer.valueOf(eventId);
        getEvent();

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        checkCountJoinEvent();

        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventcreateDetailActivity.this, EditeventActivity.class);
                intent.putExtra("eventId", eventId);
                startActivity(intent);

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getEvent(){

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<Event> call = apiService.getevent(Eventid);

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                Event res = response.body();

                if (res.getStatus() == 200) {
                    event.setEventId(res.getEventId());
                    event.setEventname(res.getEventname());
                    event.setStudentId(res.getStudentId());
                    event.setStartdate(res.getStartdate());
                    event.setEnddate(res.getEnddate());
                    event.setStrattime(res.getStrattime());
                    event.setEndtime(res.getEndtime());
                    event.setCategoryId(res.getCategoryId());
                    event.setEventdetail(res.getEventdetail());
                    event.setAmount(res.getAmount());
                    event.setDepartment(res.getDepartment());
                    event.setYear(res.getYear());
                    event.setPlacename(res.getPlacename());
                    event.setLatitude(res.getLatitude());
                    event.setLongitude(res.getLongitude());
                    event.setAddress(res.getAddress());
                }
                System.out.println("Event :" + event.getEventname());

                String time = event.getStrattime().substring(0,5) +" - "+event.getEndtime().substring(0,5)+" น.";

                String getOnlyDateStart = event.getStartdate().substring(8);
                String getOnlyDateEnd = event.getEnddate().substring(8);
                String getOnlyMonthStart = getMonth(event.getStartdate().substring(6,7));
                String getOnlyMonthEnd = getMonth(event.getEnddate().substring(6,7));
                String getOnlyYear = ""+(Integer.valueOf(event.getStartdate().substring(0,4))+543);
                String date;
                if(getOnlyMonthStart.equals(getOnlyMonthEnd)){
                    if(getOnlyDateStart.equals(getOnlyDateEnd)){
                        date = getOnlyDateStart + " " + getOnlyMonthStart + " " +getOnlyYear;
                    } else {
                        date = getOnlyDateStart +" - "+getOnlyDateEnd+ " " + getOnlyMonthStart + " " +getOnlyYear;
                    }
                } else {
                    date = getOnlyDateStart + " " + getOnlyMonthStart +" - "+getOnlyDateEnd + " " + getOnlyMonthEnd + " " +getOnlyYear;
                }

                textview_eventname.setText(event.getEventname());
                textview_amountmax.setText(event.getAmount());
                textview_eventDetail.setText(event.getEventdetail());
                textview_eventnameaddress.setText(event.getPlacename());
                textview_eventAddress.setText(event.getAddress());
                textview_eventCategory.setText(getCategory(event.getCategoryId()));
                textview_eventDate.setText(date);
                textview_eventTime.setText(time);
                eventamount = Integer.valueOf(event.getAmount());
                eventlatitude = event.getLatitude();
                eventlongitude = event.getLongitude();


            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(EventcreateDetailActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    private String getMonth(String month){
        switch (month){
            case "1":
                return "ม.ค";
            case "2":
                return "ก.พ";
            case "3":
                return "มี.ค";
            case "4":
                return "เม.ย";
            case "5":
                return "พ.ค";
            case "6":
                return "มิ.ย";
            case "7":
                return "ก.ค";
            case "8":
                return "ส.ค";
            case "9":
                return "ก.ย";
            case "10":
                return "ต.ค";
            case "11":
                return "พ.ย";
            case "12":
                return "ธ.ค";
            default: break;
        }
        return "";
    }

    private String getCategory(int cate){
        switch (cate){
            case 0 :
                return "ขายของ";
            case 1:
                return "จิตอาสา";
            case 2:
                return "ติวหนังสือ";
            case 3:
                return "ทำบุญไหว้พระ";
            case 4:
                return "ท่องเที่ยว";
            case 5:
                return "เล่นเกมส์";
            case 6:
                return "สังสรรค์";
            case 7:
                return "ออกกำลังกาย";
            default: break;
        }
        return "";
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

                } else {
                    String amount_s = String.valueOf(joineventList.size());
                    textview_amount.setText(amount_s);

                }

            }

            @Override
            public void onFailure(Call<ArrayList<ResponseJoinevent>> call, Throwable t) {
                Toast.makeText(EventcreateDetailActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
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

        System.out.println("LAT : "+eventlatitude);
        System.out.println("LONG : "+eventlongitude);
        LatLng ny = new LatLng(eventlatitude, eventlongitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        mMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ny, 17));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }

}
