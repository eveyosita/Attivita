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

public class search_by_category extends AppCompatActivity {
    ArrayList<Event> eventList = new ArrayList<>();
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    Handler handle;
    Runnable runable;
    int cateId;

    private static final String MY_PREFS = "prefs";
    String studentDepart,studentyear;

    EventListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_category);
        getSupportActionBar().hide();
        listView = (ListView) findViewById(R.id.list_event_category);
        TextView EventTitle = (TextView)findViewById(R.id.category_event_title);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        Intent getIntent = getIntent();
        String categoryTitle = getIntent.getStringExtra("categoryName");
        String categoryId = getIntent.getStringExtra("categoryId");
        cateId = Integer.valueOf(categoryId);
        EventTitle.setText(categoryTitle);
        setEventIDList();
        System.out.println("EventSize2 "+eventList.size());

        SharedPreferences shared = this.getSharedPreferences(MY_PREFS,
                Context.MODE_PRIVATE);

        studentDepart = shared.getString("department",null);
        studentyear = shared.getString("year",null);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                handle = new Handler();
                runable = new Runnable() {

                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        handle.removeCallbacks(runable); // stop runable.
                        setEventIDList();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                };
                handle.postDelayed(runable, 1000); // delay 3 s.
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(search_by_category.this, EventDetails.class);
                startActivity(intent);
            }
        });

    }

    public void setEventIDList() {

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ArrayList<Event>> call = apiService.readevent_bycategory(cateId);
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

                    EventListAdapter adapter = new EventListAdapter(search_by_category.this,R.layout.item_event, eventList);
                    listView.setAdapter(adapter);

                } else {
                    Toast.makeText(search_by_category.this, "No event now", Toast.LENGTH_LONG).show();
                }
              //  resultFromCategory(categoryId);
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                Toast.makeText(search_by_category.this, "Refresh Fail...", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    void resultFromCategory(String cateId){
//        ArrayList<Event> eventbycate = new ArrayList<>();
//
//        for(int i = 0 ;  i < eventList.size() ; i++){
//            String category = Integer.toString(eventList.get(i).getCategoryId()) ;
//            if(category.equals(cateId)){
//                eventbycate.add(eventList.get(i));
//            }
//        }
//        adapter = new EventListAdapter(search_by_category.this, R.layout.item_event, eventbycate);
//        listView.setAdapter(adapter);
//    }

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
