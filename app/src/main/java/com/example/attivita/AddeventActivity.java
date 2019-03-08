package com.example.attivita;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddeventActivity extends AppCompatActivity {

    Button but_finishaddevent;
    EditText eventname;
    EditText startdate;
    EditText enddate;
    EditText strattime;
    EditText endtime;
    Spinner category;
    EditText eventdetail;
    EditText amount;
    EditText department;
    EditText year_ed;
    Button bt_depart;
    TextView dp;
    String depart;
    String cate;
    int cate_posit;
    String id;

    private static final String TAG = "MainActivity";

    private Button mDisplayDateStart;
    private DatePickerDialog.OnDateSetListener mDateStartSetListener;

    private Button mDisplayDateEnd;
    private DatePickerDialog.OnDateSetListener mDateEndSetListener;

    Button tstart;
    Button tend;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;

    ArrayList<String> department_event = new ArrayList<String>();
    ArrayList<String> category_event = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);
        getSupportActionBar().hide();

        eventname = (EditText) findViewById(R.id.eventname_editText);
        eventdetail = (EditText) findViewById(R.id.descrip_editText);
        amount = (EditText) findViewById(R.id.number_editText);
        startdate = (EditText) findViewById(R.id.Sdate_editText);
        enddate = (EditText) findViewById(R.id.Edate_editText);
        strattime = (EditText) findViewById(R.id.Stime_editText);
        endtime = (EditText) findViewById(R.id.Etime_editText);
        department = (EditText) findViewById(R.id.depart_editText);
        dp = (TextView) findViewById(R.id.depart_textView);
        year_ed = (EditText) findViewById(R.id.year_editText);
        category = (Spinner) findViewById(R.id.cate_spinner);
        mDisplayDateStart = (Button) findViewById(R.id.calendarStart);
        mDisplayDateEnd = (Button) findViewById(R.id.calendarEnd);
        bt_depart = (Button) findViewById(R.id.button_select_depart);


        mDisplayDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddeventActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateStartSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateStartSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + year + "-" + month + "-" + day);

                String date = year + "-" + month + "-" + day;
                startdate.setText(date);
            }
        };

        mDisplayDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddeventActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateEndSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateEndSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + year + "-" + month + "-" + day);

                String date = year + "-" + month + "-" + day;
                enddate.setText(date);
            }
        };

        bt_depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddeventActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                View v = inflater.inflate(R.layout.item_depart_dialog, null);

                builder.setView(v);

                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                final Button button_cancel = (Button)v.findViewById(R.id.button_dialog_cancel);
                final CheckBox cb_all = (CheckBox)v.findViewById(R.id.checkBox_all);
                final CheckBox cb_match = (CheckBox)v.findViewById(R.id.checkBox_match);
                final CheckBox cb_bio = (CheckBox)v.findViewById(R.id.checkBox_bio);
                final CheckBox cb_chem = (CheckBox)v.findViewById(R.id.checkBox_chem);
                final CheckBox cb_phy = (CheckBox)v.findViewById(R.id.checkBox_phy);
                final CheckBox cb_stat = (CheckBox)v.findViewById(R.id.checkBox_stat);
                final CheckBox cb_envi = (CheckBox)v.findViewById(R.id.checkBox_envi);
                final CheckBox cb_com = (CheckBox)v.findViewById(R.id.checkBox_com);
                final CheckBox cb_micro = (CheckBox)v.findViewById(R.id.checkBox_micro);
                final CheckBox cb_appmatch = (CheckBox)v.findViewById(R.id.checkBox_appmatch);
                final CheckBox cb_it = (CheckBox)v.findViewById(R.id.checkBox_it);
                final CheckBox cb_tphy = (CheckBox)v.findViewById(R.id.checkBox_tphy);
                final CheckBox cb_dsci = (CheckBox)v.findViewById(R.id.checkBox_dsci);
                final Button button_accept = (Button)v.findViewById(R.id.button_dialog_accept);

                button_accept.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        int totalamount = 0;
                        StringBuilder result = new StringBuilder();
                        result.append("Selected Items:");
                        if(cb_all.isChecked()){
                            result.append("\nPizza 100Rs");
                            totalamount+=100;
                        }
                        if(cb_appmatch.isChecked()){
                            result.append("\nCoffe 50Rs");
                            totalamount+=50;
                        }
                        if(cb_chem.isChecked()){
                            result.append("\nBurger 120Rs");
                            totalamount+=120;
                        }
                        result.append("\nTotal: "+totalamount+"Rs");
                        //Displaying the message on the toast
                        Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
                    }

                });

                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });


        createCategory();
        ArrayAdapter<String> adapterCategory_event = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, category_event);
        category.setAdapter(adapterCategory_event);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cate = category_event.get(position);
                cate_posit = position;

//                Toast.makeText(RegisterActivity.this,
//                        "Select : " + department.get(position),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        but_finishaddevent = (Button) findViewById(R.id.finishaddevent_button);
        but_finishaddevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ename = eventname.getText().toString();
                String detail = eventdetail.getText().toString();
                String amout = amount.getText().toString();
                String sdate = startdate.getText().toString();
                String edate = enddate.getText().toString();
                String stime = strattime.getText().toString();
                String etime = endtime.getText().toString();
                String year = year_ed.getText().toString();
                int locat = 1;

                setEvent(ename,id,sdate,edate,stime,etime,cate_posit,detail
                        ,locat,amout,depart,year);

                Intent i = new Intent(AddeventActivity.this, MainActivity.class);
                startActivity(i);

            }
        });

        tstart = findViewById(R.id.timeStart);
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

        tend = findViewById(R.id.timeEnd);
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
    }

    private void createCategory(){
        category_event.add("ทั้งหมด");
        category_event.add("ขายของ");
        category_event.add("จิตอาสา");
        category_event.add("ติวหนังสือ");
        category_event.add("ทำบุญไหว้พระ");
        category_event.add("ท่องเที่ยว");
        category_event.add("เล่นเกมส์");
        category_event.add("สังสรรค์");
        category_event.add("ออกกำลังกาย");
    }

    private void createDepartment() {
        department_event.add("เลือกสาขาวิชา");
        department_event.add("คณิตศาสตร์");
        department_event.add("ชีววิทยา");
        department_event.add("เคมี");
        department_event.add("ฟิสิกส์");
        department_event.add("สถิติ");
        department_event.add("วิทยาศาสตร์สิ่งแวดล้อม");
        department_event.add("วิทยาการคอมพิวเตอร์");
        department_event.add("จุลชีววิทยา");
        department_event.add("คณิตศาสตร์ประยุกต์");
        department_event.add("เทคโนโลยีสารสนเทศ");
        department_event.add("ครูฟิสิกส์");
        department_event.add("วิทยาการข้อมูล");

    }

    public void setEvent(String ename, String id, String sdate, String edate, String stime, String etime,
                           int cate, String detail, int locat, String amout, String depart, String year){

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ResponseBody> call = apiService.createevent(ename,id,sdate,edate,stime,etime,cate,detail
                ,locat,amout,depart,year);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                Toast.makeText(AddeventActivity.this, "Succees", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddeventActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }
}
