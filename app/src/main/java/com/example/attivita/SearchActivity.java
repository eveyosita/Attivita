package com.example.attivita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.adapter.EventListAdapter;
import com.example.attivita.model.Event;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Event> eventList = new ArrayList<>();
    ListView listView;
    TextView text_background;
    SwipeRefreshLayout swipeRefreshLayout;
    Handler handle;
    Runnable runable;
    int cateId;
    String textSearch;

    private static final String MY_PREFS = "prefs";
    String studentId,studentDepart,studentyear;

    EventListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        listView =  findViewById(R.id.list_event);
        text_background = findViewById(R.id.textBackgroung_searchbycat);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        Intent getIntent = getIntent();
        textSearch = getIntent.getStringExtra("textSearch");

        SharedPreferences shared = this.getSharedPreferences(MY_PREFS,
                Context.MODE_PRIVATE);
        studentId = shared.getString("studentId",null);
        studentDepart = shared.getString("department",null);
        studentyear = shared.getString("year",null);

        setEventIDList();


//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                handle = new Handler();
//                runable = new Runnable() {
//
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                        handle.removeCallbacks(runable); // stop runable.
//                        setEventIDList();
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                };
//                handle.postDelayed(runable, 1000); // delay 3 s.
//            }
//        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(SearchActivity.this, EventDetails.class);
//                startActivity(intent);
//            }
//        });
    }

    public void setEventIDList() {

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ArrayList<Event>> call = apiService.searchevent(studentId,textSearch);
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                ArrayList<Event> res = response.body();
                eventList.clear();

                if (res.size() != 0) {

                    for (Event r : res) {
                        String[] depart = r.getDepartment().split(" ");
                        String[] year = r.getYear().split(" ");

                        if(checkDepartment(depart)){
                            if(checkYear(year)){
                                eventList.add(new Event(r.getEventId(),r.getEventname(), r.getStudentId()
                                        , r.getStartdate(), r.getEnddate(), r.getStrattime()
                                        , r.getEndtime() , r.getCategoryId() , r.getEventdetail()
                                        , r.getLocationId() , r.getAmount() , r.getDepartment()
                                        , r.getYear() , r.getPlacename() , r.getLatitude()
                                        , r.getLongitude() , r.getAddress()));
                            }
                        }
                    }
                        EventListAdapter adapter = new EventListAdapter(SearchActivity.this,R.layout.item_event, eventList);
                        listView.setAdapter(adapter);

                } else {
                    text_background.setText("ไม่พบกิจกรรมที่ค้นหา");
                }
                //  resultFromCategory(categoryId);
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Refresh Fail...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkYear(String[] year){
        int studentyear_int = Integer.parseInt(studentyear);

        int y = Calendar.getInstance().get(Calendar.YEAR)+543;



        for(int i = 0 ; i<year.length ; i++ ) {
            int year_int = Integer.parseInt(year[i])-1;

            if(year_int <= 3 && y - year_int == studentyear_int){
                System.out.println("DDYStu "+year_int);
                return true;
            } else if(year_int == 4 && y - year_int >= studentyear_int ) {
                System.out.println("DDYGrat "+year_int);
                return true;
            }
        }
        return false;
    }

    public boolean checkDepartment(String[] depart){
        String department="";
        boolean check = false;
        for(int i = 0 ; i<depart.length ; i++ ) {
            switch (depart[i]) {
                case "A":

                    department="คณิตศาสตร์";
                    if(department.equals(studentDepart)){ System.out.println("DDD2 "+depart[i]);
                        check = department.equals(studentDepart);
                        break;
                    }

                case "B":

                    department="ชีววิทยา";
                    if(department.equals(studentDepart)){System.out.println("DDD2 "+depart[i]);
                        check = department.equals(studentDepart);
                        break;
                    }

                case "C":

                    department="เคมี";
                    if(department.equals(studentDepart)){System.out.println("DDD2 "+depart[i]);
                        check = department.equals(studentDepart);
                        break;
                    }

                case "D":

                    department="ฟิสิกส์";
                    if(department.equals(studentDepart)){System.out.println("DDD2 "+depart[i]);
                        check = department.equals(studentDepart);
                        break;
                    }

                case "E":

                    department="สถิติ";
                    if(department.equals(studentDepart)){System.out.println("DDD2 "+depart[i]);
                        check = department.equals(studentDepart);
                        break;
                    }

                case "F":

                    department="วิทยาศาสตร์สิ่งแวดล้อม";
                    if(department.equals(studentDepart)){System.out.println("DDD2 "+depart[i]);
                        check = department.equals(studentDepart);
                        break;
                    }

                case "G":

                    department="วิทยาการคอมพิวเตอร์";
                    if(department.equals(studentDepart)){System.out.println("DDD2 "+depart[i]);
                        check = department.equals(studentDepart);
                        break;
                    }

                case "H":

                    department="จุลชีววิทยา";
                    if(department.equals(studentDepart)){System.out.println("DDD2 "+depart[i]);
                        check = department.equals(studentDepart);
                        break;
                    }

                case "I":

                    department="คณิตศาสตร์ประยุกต์";
                    if(department.equals(studentDepart)){System.out.println("DDD2 "+depart[i]);
                        check = department.equals(studentDepart);
                        break;
                    }

                case "J":

                    department="เทคโนโลยีสารสนเทศ";
                    if(department.equals(studentDepart)){System.out.println("DDD2 "+depart[i]);
                        check = department.equals(studentDepart);
                        break;
                    }

                case "K":

                    department="ครูฟิสิกส์";
                    if(department.equals(studentDepart)){System.out.println("DDD2 "+depart[i]);
                        check = department.equals(studentDepart);
                        break;
                    }

                case "L":

                    department="วิทยาการข้อมูล";
                    if(department.equals(studentDepart)){System.out.println("DDD2 "+depart[i]);
                        check = department.equals(studentDepart);
                        break;
                    }
            }
        }
        return check;
    }
}
