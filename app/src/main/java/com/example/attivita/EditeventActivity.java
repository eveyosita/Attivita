package com.example.attivita;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.attivita.model.Event;
import com.example.attivita.model.ResponseJoinevent;
import com.example.attivita.model.student;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditeventActivity extends AppCompatActivity {

    protected TextView button_eventDatestart,button_eventDateend,button_eventTimestart,button_eventTimeend
            ,textview_eventAddress,edittext_amountmax,edittext_eventDetail,button_acceptedit;
    EditText edittext_eventname;
    Button btn_joinevent;
    ImageView btn_back;
    LinearLayout layout_address;
    Spinner spinner_category;
    String Startdate,Enddate,timeStart,timeEnd,eventplacename,eventaddress;
    int Eventid,category_posit;
    double eventlatitude,eventlongitude;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour, currentMinute;
    String amPm;

    ArrayList<ResponseJoinevent> joineventList = new ArrayList<>();
    Event event = new Event();
    protected ArrayList<String> category_event = new ArrayList<String>();

    private static final String MY_PREFS = "prefs";
    private int PLACE_PICKER_REQUEST = 2;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent);
        getSupportActionBar().hide();

        SharedPreferences shared = this.getSharedPreferences(MY_PREFS,
                Context.MODE_PRIVATE);

//        studentId = shared.getString("studentId",null);

        Intent getIntent = getIntent();
        String eventId = getIntent.getStringExtra("eventId");

        edittext_eventname = findViewById(R.id.edittext_eventname);
        button_eventDatestart = findViewById(R.id.button_eventDatestart);
        button_eventDateend = findViewById(R.id.button_eventDateend);
        button_eventTimestart = findViewById(R.id.button_eventTimestart);
        button_eventTimeend = findViewById(R.id.button_eventTimeend);
        textview_eventAddress = findViewById(R.id.textview_eventAddress);
        layout_address = findViewById(R.id.layout_address);
        spinner_category = findViewById(R.id.spinner_category);
        edittext_amountmax = findViewById(R.id.edittext_amountmax);
        edittext_eventDetail = findViewById(R.id.edittext_eventDetail);
        btn_joinevent = findViewById(R.id.button_joinevent);
        btn_back = findViewById(R.id.button_back);
        button_acceptedit = findViewById(R.id.button_acceptedit);
        Eventid = Integer.valueOf(eventId);

        getEvent();

        System.out.println("NAME : "+event.getEventname());

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_eventDatestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(EditeventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // TODO: 2019-03-06  set textview เอาวันที่ที่เลือกมาแสดง
                                String m = String.valueOf(month + 1);
                                String monthString = getMonth(m);
                                String date = day + "/" + monthString + "/" + (year+543);
                                Startdate = year + "-" + (month + 1) + "-" + day;

                                button_eventDatestart.setText(date);
                            }
                        }, year, month, day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


        // set วันที่สิ้นสุด
        button_eventDateend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(EditeventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // TODO: 2019-03-06  set textview เอาวันที่ที่เลือกมาแสดง
                                String m = String.valueOf(month + 1);
                                String monthString = getMonth(m);
                                String date = day + "/" + monthString + "/" + (year+543);
                                Enddate = year + "-" + (month + 1) + "-" + day;

                                button_eventDateend.setText(date);
                            }
                        }, year, month, day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        // set เวลาเริ่มต้น
        button_eventTimestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(EditeventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        button_eventTimestart.setText(String.format("%02d:%02d", hourOfDay, minutes)+" น.");
                        timeStart = button_eventTimestart.getText().toString().substring(0,5);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        // set เวลาสิ้นสุด
        button_eventTimeend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(EditeventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        button_eventTimeend.setText(String.format("%02d:%02d", hourOfDay, minutes)+" น.");
                        timeEnd = button_eventTimeend.getText().toString().substring(0,5);
                        System.out.println("TIME : " +timeEnd);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        createCategory();
        ArrayAdapter<String> adapterCategory_event = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, category_event);
        spinner_category.setAdapter(adapterCategory_event);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                category_posit = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        layout_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlacePicker();

            }
        });

        button_acceptedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            setEvent();
            }
        });

    }

    public void setEvent(){

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<Event> call = apiService.editEvent(Eventid,edittext_eventname.getText().toString(),Startdate
                ,Enddate,timeStart,timeEnd,category_posit,edittext_eventDetail.getText().toString()
                ,edittext_amountmax.getText().toString(),eventplacename,eventlatitude,eventlongitude,eventaddress);


        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                Event res = response.body();

                if (res.getStatus() == 200){
                    String message = res.getMessage();
                    Toast.makeText(EditeventActivity.this, message, Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(EditeventActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    public void getEvent(){
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

                edittext_eventname.setText(event.getEventname());
                edittext_amountmax.setText(event.getAmount());
                edittext_eventDetail.setText(event.getEventdetail());
                textview_eventAddress.setText(event.getPlacename());
                spinner_category.setSelection(event.getCategoryId());
                String dateStart = event.getStartdate().substring(8)+"/"+getMonth(event.getStartdate().substring(6,7))
                        +"/"+(Integer.valueOf(event.getStartdate().substring(0,4))+543);
          
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(EditeventActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
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

    private void createCategory(){
        category_event.add("ขายของ");
        category_event.add("จิตอาสา");
        category_event.add("ติวหนังสือ");
        category_event.add("ทำบุญไหว้พระ");
        category_event.add("ท่องเที่ยว");
        category_event.add("เล่นเกมส์");
        category_event.add("สังสรรค์");
        category_event.add("ออกกำลังกาย");
    }

    private void openPlacePicker() {

        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil
                    .getErrorDialog(e.getConnectionStatusCode(), this, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, "Google Play Services is not available.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                StringBuilder stBuilder = new StringBuilder();
                eventplacename = String.format("%s", place.getName());
                eventlatitude = place.getLatLng().latitude;
                eventlongitude = place.getLatLng().longitude;
                eventaddress = String.format("%s", place.getAddress());
                stBuilder.append(eventplacename);

                textview_eventAddress.setText(stBuilder.toString());
            }
        }
    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//    }



}
