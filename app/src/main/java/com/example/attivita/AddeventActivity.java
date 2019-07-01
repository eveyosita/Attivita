package com.example.attivita;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.attivita.model.Event;
import com.example.attivita.model.ResponseJoinevent;
import com.example.attivita.model.ResponseStatus;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddeventActivity extends AppCompatActivity {

    Button button_back, button_finishaddevent, button_depart, button_cancel_depart,
            button_accept_depart, button_year, button_cancel_year, button_accept_year;
    EditText eventname, edittexe_startdate, edittexe_enddate, strattime, endtime, eventdetail, amount, department,
            year_ed;
    Spinner category;
    String depart,year, cate, id,Startdate,Enddate,dateS,dateE;
    int cate_posit,eventId;
    CheckBox cb_match, cb_bio, cb_chem, cb_phy, cb_stat, cb_envi, cb_com, cb_micro, cb_appmatch,
            cb_it, cb_tphy, cb_dsci;
    CheckBox cb_1, cb_2, cb_3, cb_4, cb_etc;


    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 2;
    TextView textView_location;
    RelativeLayout relatlayout_location;
    String event_placename;
    double event_latitude;
    double event_longitude;
    String event_address;

    private static final String MY_PREFS = "prefs";

    private static final String TAG = "MainActivity";

    private Button mDisplayDateStart, mDisplayDateEnd;
    Button tstart, tend;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour, currentMinute;
    String amPm;

    ArrayList<String> category_event = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);
        getSupportActionBar().hide();

        button_back = findViewById(R.id.button_back);
        eventname = findViewById(R.id.eventname_editText);
        eventdetail = findViewById(R.id.descrip_editText);
        amount =  findViewById(R.id.number_editText);
        edittexe_startdate =  findViewById(R.id.Sdate_editText);
        edittexe_enddate =  findViewById(R.id.Edate_editText);
        strattime =  findViewById(R.id.Stime_editText);
        endtime = findViewById(R.id.Etime_editText);
        department =  findViewById(R.id.depart_editText);
        year_ed =  findViewById(R.id.year_editText);
        category =  findViewById(R.id.cate_spinner);
        mDisplayDateStart = findViewById(R.id.calendarStart);
        mDisplayDateEnd =  findViewById(R.id.calendarEnd);
        tstart =  findViewById(R.id.timeStart);
        tend =  findViewById(R.id.timeEnd);
        button_depart =  findViewById(R.id.button_select_depart);
        button_year =  findViewById(R.id.button_select_year);
        relatlayout_location = findViewById(R.id.relatlayout_location);
        textView_location =  findViewById(R.id.tv_locat);
        button_finishaddevent = findViewById(R.id.finishaddevent_button);

        // ปุ่มกลับ
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // set วันที่เริ่มต้น
        mDisplayDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(AddeventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // TODO: 2019-03-06  set textview เอาวันที่ที่เลือกมาแสดง
                                dateS = day + "/" + (month + 1) + "/" + (year+543);
                                Startdate = year + "-" + (month + 1) + "-" + day;

                                edittexe_startdate.setText(dateS);
                            }
                        }, year, month, day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


        // set วันที่สิ้นสุด
        mDisplayDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(AddeventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // TODO: 2019-03-06  set textview เอาวันที่ที่เลือกมาแสดง
                                dateE = day + "/" + (month + 1) + "/" + (year+543);
                                Enddate = year + "-" + (month + 1) + "-" + day;

                                edittexe_enddate.setText(dateE);
                            }
                        }, year, month, day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

      
        // ปุ่มเลือกเอก
        button_depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddeventActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                View v = inflater.inflate(R.layout.item_depart_dialog, null);

                builder.setView(v);

                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                button_cancel_depart = (Button)v.findViewById(R.id.button_dialog_cancel);
                cb_match = (CheckBox)v.findViewById(R.id.checkBox_match);
                cb_bio = (CheckBox)v.findViewById(R.id.checkBox_bio);
                cb_chem = (CheckBox)v.findViewById(R.id.checkBox_chem);
                cb_phy = (CheckBox)v.findViewById(R.id.checkBox_phy);
                cb_stat = (CheckBox)v.findViewById(R.id.checkBox_stat);
                cb_envi = (CheckBox)v.findViewById(R.id.checkBox_envi);
                cb_com = (CheckBox)v.findViewById(R.id.checkBox_com);
                cb_micro = (CheckBox)v.findViewById(R.id.checkBox_micro);
                cb_appmatch = (CheckBox)v.findViewById(R.id.checkBox_appmatch);
                cb_it = (CheckBox)v.findViewById(R.id.checkBox_it);
                cb_tphy = (CheckBox)v.findViewById(R.id.checkBox_tphy);
                cb_dsci = (CheckBox)v.findViewById(R.id.checkBox_dsci);
                button_accept_depart = (Button)v.findViewById(R.id.button_dialog_accept);


                button_accept_depart.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        depart = setCheckboxDepart();
                        alertDialog.cancel();
                    }

                });

                button_cancel_depart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        // ปุ่มเลือกชั้นปี
        button_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddeventActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                View v = inflater.inflate(R.layout.item_year_dialog, null);

                builder.setView(v);

                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                button_cancel_year = v.findViewById(R.id.button_dialog_cancelyear);
                cb_1 = v.findViewById(R.id.checkBox_1);
                cb_2 = v.findViewById(R.id.checkBox_2);
                cb_3 = v.findViewById(R.id.checkBox_3);
                cb_4 = v.findViewById(R.id.checkBox_4);
                cb_etc = v.findViewById(R.id.checkBox_etc);
                button_accept_year = v.findViewById(R.id.button_dialog_acceptyear);


                button_accept_year.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        year = setCheckboxYear();
                        alertDialog.cancel();
                    }

                });

                button_cancel_year.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        // สร้าง array ประเภท
        createCategory();
        ArrayAdapter<String> adapterCategory_event = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, category_event);
        category.setAdapter(adapterCategory_event);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cate = category_event.get(position);
                cate_posit = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // set เวลาเริ่มต้น
        tstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AddeventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        strattime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        // set เวลาสิ้นสุด
        tend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AddeventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        endtime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        // ปุ่ม set สถานที่
        relatlayout_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(AddeventActivity.this, "Yahhh", Toast.LENGTH_LONG).show();
                openPlacePicker();

            }
        });

        SharedPreferences shared = AddeventActivity.this.getSharedPreferences(MY_PREFS,
                Context.MODE_PRIVATE);

        id = shared.getString("studentId",null);

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ResponseStatus> call = apiService.getStatusStudent(id);

        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                ResponseStatus res = response.body();
                if (res.isStatus()){

                    if (res.getStatus_verify().equals("no")){
                        button_finishaddevent.setText("กำลังตรวจสอบการยืนยันตัวตน");
                        button_finishaddevent.setEnabled(false);
                    } else {
                        button_finishaddevent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                // ปุ่มเสร็จสิ้น
                                String ename = eventname.getText().toString();
                                String detail = eventdetail.getText().toString();
                                String amout = amount.getText().toString();
                                String stime = strattime.getText().toString();
                                String etime = endtime.getText().toString();

                                if(!checkEventname() || !checkEventdetail() || !checkEventamount() || !checkEventstartdate()
                                        || !checkEventenddate() || !checkEventstrattime() || !checkEventendtime() ||
                                        !checkEventdepartment() || !checkEventyear()){
                                    Toast.makeText(AddeventActivity.this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
                                } else {
                                    setEvent(ename,id,stime,etime,cate_posit,detail
                                            ,amout);

                                }

                            }
                        });
                    }
                    System.out.println("EVENTJOIN"+res.getMessage());
                } else {
                    System.out.println("EVENTJOIN"+res.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                Toast.makeText(AddeventActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
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
                event_placename = String.format("%s", place.getName());
                event_latitude = place.getLatLng().latitude;
                event_longitude = place.getLatLng().longitude;
                event_address = String.format("%s", place.getAddress());
                stBuilder.append(event_placename);
                System.out.println("LAT "+event_latitude+" LOG "+event_longitude);
                textView_location.setText(stBuilder.toString());
            }
        }
    }


    public void setEvent(String ename, String id, String stime, String etime,
                         int cate, String detail,  String amount){

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<Event> call = apiService.createevent(ename,id,Startdate,Enddate,stime,etime,cate,detail
                ,amount,depart,year,event_placename,event_latitude,event_longitude,event_address);


        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                Event res = response.body();

                if (res.getStatus() == 100){
                    String message = res.getMessage();
                    Toast.makeText(AddeventActivity.this, message, Toast.LENGTH_SHORT).show();

                } else if (res.getStatus() == 200){
                    String message = res.getMessage();
                    Toast.makeText(AddeventActivity.this, message, Toast.LENGTH_SHORT).show();
                    eventId = res.getEventId();
                    joinEvent(eventId);
                    finish();

                } else if (res.getStatus() == 101){
                    String message = res.getMessage();
                    Toast.makeText(AddeventActivity.this, message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(AddeventActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    public void joinEvent(int eventid){

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ResponseJoinevent> call = apiService.joinevent(id,eventid);

        call.enqueue(new Callback<ResponseJoinevent>() {
            @Override
            public void onResponse(Call<ResponseJoinevent> call, Response<ResponseJoinevent> response) {
                ResponseJoinevent res = response.body();

                if (!res.isStatus()){
//                    btn_joinevent.setText("เข้ารวมแล้ว");
//                    btn_joinevent.setEnabled(false);
                    Toast.makeText(AddeventActivity.this, res.getMessage(), Toast.LENGTH_LONG).show();
                } else {
//                    boolean_joinevent = false;
//                    int amount = Integer.valueOf(textview_amount.getText().toString());
//                    amount++;
//                    String amount_s = String.valueOf(amount);
//                    textview_amount.setText(amount_s);

                }
            }

            @Override
            public void onFailure(Call<ResponseJoinevent> call, Throwable t) {
                Toast.makeText(AddeventActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

    public String setCheckboxDepart(){
        String total = "";
        String s_total = "";
        StringBuilder result = new StringBuilder();
        result.append("Selected Items:");
        if(cb_match.isChecked()){
            result.append("\nMatch");
            total+="A ";
            s_total += "คณิตศาสตร์ ";
        }
        if(cb_bio.isChecked()){
            result.append("\nBio");
            total+="B ";
            s_total += "ชีววิทยา ";
        }
        if(cb_chem.isChecked()){
            result.append("\nChem");
            total+="C ";
            s_total += "เคมี ";
        }
        if(cb_phy.isChecked()){
            result.append("\nPhy");
            total+="D ";
            s_total += "ฟิสิกส์ ";
        }
        if(cb_stat.isChecked()){
            result.append("\nStat");
            total+="E ";
            s_total += "สถิติ ";
        }
        if(cb_envi.isChecked()){
            result.append("\nEnvi");
            total+="F ";
            s_total += "วิทยาศาสตร์สิ่งแวดล้อม ";
        }
        if(cb_com.isChecked()){
            result.append("\nCom");
            total+="G ";
            s_total += "วิทยาการคอมพิวเตอร์ ";
        }
        if(cb_micro.isChecked()){
            result.append("\nMicro");
            total+="H ";
            s_total += "จุลชีววิทยา ";
        }
        if(cb_appmatch.isChecked()){
            result.append("\nAppMatch");
            total+="I ";
            s_total += "คณิตศาสตร์ประยุกต์ ";
        }
        if(cb_it.isChecked()){
            result.append("\nIT");
            total+="J ";
            s_total += "เทคโนโลยีสารสนเทศ ";
        }
        if(cb_tphy.isChecked()){
            result.append("\nTPhy");
            total+="K ";
            s_total += "ครูฟิสิกส์ ";
        }
        if(cb_dsci.isChecked()){
            result.append("\nDSci");
            total+="L";
            s_total += "วิทยาการข้อมูล ";
        }
        result.append("\nTotal: "+total);
        Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
        department.setText(s_total);
        return total;
    }

    public String setCheckboxYear(){
        String total = "";
        StringBuilder result = new StringBuilder();
        result.append("Selected Items:");
        if(cb_1.isChecked()) {
            result.append("\nปี1");
            total += "1 ";
        }
        if(cb_2.isChecked()) {
            result.append("\nปี2");
            total += "2 ";
        }
        if(cb_3.isChecked()) {
            result.append("\nปี3");
            total += "3 ";
        }
        if(cb_4.isChecked()) {
            result.append("\nปี4");
            total += "4 ";
        }
        if(cb_etc.isChecked()) {
            result.append("\nปีอื่นๆ");
            total += "5 ";
        }
        result.append("\nTotal: "+total);
        Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
        year_ed.setText(total);
        return total;
    }

    private boolean checkEventname() {
        String eventName = eventname.getText().toString().trim();
        if (eventName.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkEventdetail() {
        String eventDetail = eventdetail.getText().toString().trim();
        if (eventDetail.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkEventamount() {
        String eventAmount = amount.getText().toString().trim();
        if (eventAmount.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkEventstartdate() {
        String eventStartdate = edittexe_startdate.getText().toString().trim();
        if (eventStartdate.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkEventenddate() {
        String eventEnddate = edittexe_enddate.getText().toString().trim();
        if (eventEnddate.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkEventstrattime() {
        String eventStrattime = strattime.getText().toString().trim();
        if (eventStrattime.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkEventendtime() {
        String eventEndtime = endtime.getText().toString().trim();
        if (eventEndtime.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkEventdepartment() {
        String eventDepartment = department.getText().toString().trim();
        if (eventDepartment.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkEventyear() {
        String eventYear = year_ed.getText().toString().trim();
        if (eventYear.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
