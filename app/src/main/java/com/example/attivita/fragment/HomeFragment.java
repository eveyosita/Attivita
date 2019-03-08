package com.example.attivita.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attivita.AddeventActivity;
import com.example.attivita.AddwarnActivity;
import com.example.attivita.EventDetails;
import com.example.attivita.R;
import com.example.attivita.adapter.EventListAdapter;
import com.example.attivita.model.Event;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment  {
    SwipeRefreshLayout swipeRefreshLayout;
    Handler handle;
    Runnable runable;
    ImageButton imbut_addwarn;
    ImageButton imbut_addevent;
    ListView listView;
    ArrayList<Event> eventList = new ArrayList<>();
    ProgressDialog dialog;
    private static final String MY_PREFS = "prefs";


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
             View v = inflater.inflate(R.layout.fragment_home, container, false);
                imbut_addevent = (ImageButton)v.findViewById(R.id.addevent_imBut);
                imbut_addwarn = (ImageButton)v.findViewById(R.id.addwarn_imBut);
                listView = (ListView) v.findViewById(R.id.list_event);

            imbut_addwarn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getContext(), AddwarnActivity.class);
                            startActivity(i);

                        }
             });

             imbut_addevent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getContext(), AddeventActivity.class);
                            startActivity(i);
                            //Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
                        }
             });

            SharedPreferences shared = getContext().getSharedPreferences(MY_PREFS,
                    Context.MODE_PRIVATE);

//            final String stdid = shared.getString("studentId",null);
//            String year = "25"+stdid.substring(2,4);
             setEventIDList();


            swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
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
                    Intent intent = new Intent(getContext(), EventDetails.class);
                    startActivity(intent);
                }
            });


             return v;
        }

    public void setEventIDList() {

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ArrayList<Event>> call = apiService.readevent();
        call.enqueue(new Callback<ArrayList<Event>>() {
                         @Override
                         public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                             ArrayList<Event> res = response.body();
                             eventList.clear();

                             if (res.size() != 0) {

                                 for (Event r : res) {
                                     eventList.add(new Event(r.getEventId(), r.getEventname(), r.getStudentId()
                                             , r.getStartdate(), r.getEnddate(), r.getStrattime() , r.getEndtime()
                                             , r.getCategoryId() , r.getEventdetail() , r.getLocationId() , r.getAmount() , r.getDepartment() , r.getYear()));
                                 }
                                 EventListAdapter adapter = new EventListAdapter(getContext(),R.layout.item_event, eventList);
                                 listView.setAdapter(adapter);
                             } else {
                                 Toast.makeText(getContext(), "No event now", Toast.LENGTH_LONG).show();
                             }
                         }

                         @Override
                         public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                             Toast.makeText(getContext(), "Refresh Fail...", Toast.LENGTH_SHORT).show();
                         }
                     }

        );

    }

}




