package com.example.attivita;

import android.content.Intent;
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
    String categoryId;
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
        categoryId = getIntent.getStringExtra("categoryId");
        EventTitle.setText(categoryTitle);
        setEventIDList();
        System.out.println("EventSize2 "+eventList.size());


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
        Call<ArrayList<Event>> call = apiService.readevent_bycategory();
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                ArrayList<Event> res = response.body();
                eventList.clear();

                if (res.size() != 0) {

                    for (Event r : res) {
                        eventList.add(new Event(r.getEventId(),r.getEventname(), r.getStudentId()
                                , r.getStartdate(), r.getEnddate(), r.getStrattime() , r.getEndtime()
                                , r.getCategoryId() , r.getEventdetail() , r.getLocationId() , r.getAmount()
                                , r.getDepartment() , r.getYear() , r.getPlacename() , r.getLatitude()
                                , r.getLongitude() , r.getAddress()));
                    }

                } else {
                    Toast.makeText(search_by_category.this, "No event now", Toast.LENGTH_LONG).show();
                }
                resultFromCategory(categoryId);
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                Toast.makeText(search_by_category.this, "Refresh Fail...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void resultFromCategory(String cateId){
        ArrayList<Event> eventbycate = new ArrayList<>();

        for(int i = 0 ;  i < eventList.size() ; i++){
            String category = Integer.toString(eventList.get(i).getCategoryId()) ;
            if(category.equals(cateId)){
                eventbycate.add(eventList.get(i));
            }
        }
        adapter = new EventListAdapter(search_by_category.this, R.layout.item_event, eventbycate);
        listView.setAdapter(adapter);
    }
}
