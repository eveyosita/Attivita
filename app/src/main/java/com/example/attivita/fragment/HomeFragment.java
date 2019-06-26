package com.example.attivita.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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
import com.example.attivita.model.StudentFirebase;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
    private ArrayList<Event> eventList = new ArrayList<>();
    ProgressDialog dialog;

    private LocationManager locationManager;
    private Double Latitude_current = 0.0;
    private Double Longitude_current = 0.0;
    private static final int REQUEST_LOCATION = 1;

    private static final String MY_PREFS = "prefs";

    String studentDepart,studentyear;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
             View v = inflater.inflate(R.layout.fragment_home, container, false);
                imbut_addevent = v.findViewById(R.id.addevent_imBut);
                imbut_addwarn = v.findViewById(R.id.addwarn_imBut);
                listView = v.findViewById(R.id.list_event);

            imbut_addwarn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String Latitude = String.valueOf(Latitude_current);
                            String Longitude = String.valueOf(Longitude_current);
                            Intent i = new Intent(getContext(), AddwarnActivity.class);
                            i.putExtra("Latitude_current", Latitude);
                            i.putExtra("Longitude_current", Longitude);
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

            final String stdid = shared.getString("studentId",null);
            studentDepart = shared.getString("department",null);
            studentyear = shared.getString("year",null);
//            String year = "25"+stdid.substring(2,4);
             setEventIDList(stdid);


            swipeRefreshLayout = v.findViewById(R.id.swipe_refresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    handle = new Handler();
                    runable = new Runnable() {

                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            handle.removeCallbacks(runable); // stop runable.
                            setEventIDList(stdid);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    };
                    handle.postDelayed(runable, 1000); // delay 3 s.
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String eventId = String.valueOf(eventList.get(i).getEventId());
                    String eventCategoryId = String.valueOf(eventList.get(i).getCategoryId());
                    String eventAmount = String.valueOf(eventList.get(i).getAmount());
                    String eventLatitude = String.valueOf(eventList.get(i).getLatitude());
                    String eventLongitude = String.valueOf(eventList.get(i).getLongitude());

                    Intent intent = new Intent(getContext(), EventDetails.class);
                    intent.putExtra("eventId", eventId);
                    intent.putExtra("eventName", eventList.get(i).getEventname());
                    intent.putExtra("eventStuId", eventList.get(i).getStudentId());
                    intent.putExtra("eventDetail", eventList.get(i).getEventdetail());
                    intent.putExtra("eventAmount", eventAmount);
                    intent.putExtra("eventStartdate", eventList.get(i).getStartdate());
                    intent.putExtra("eventEnddate", eventList.get(i).getEnddate());
                    intent.putExtra("eventStarttime", eventList.get(i).getStrattime());
                    intent.putExtra("eventEndtime", eventList.get(i).getEndtime());
                    intent.putExtra("eventDepart", eventList.get(i).getDepartment());
                    intent.putExtra("eventCategoryId", eventCategoryId);
                    intent.putExtra("eventYear", eventList.get(i).getYear());
                    intent.putExtra("eventPlacename", eventList.get(i).getPlacename());
                    intent.putExtra("eventLatitude", eventLatitude);
                    intent.putExtra("eventLongitude", eventLongitude);
                    intent.putExtra("eventAddress", eventList.get(i).getAddress());
                    startActivity(intent);
                }
            });

            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
                setLocationCurrent();
            }


             return v;
        }

    public void setEventIDList(String studid) {

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ArrayList<Event>> call = apiService.readevent(studid);
        call.enqueue(new Callback<ArrayList<Event>>() {
                         @Override
                         public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                             ArrayList<Event> res = response.body();
                             eventList.clear();

                             if (res.size() != 0) {
                                 for (Event r : res) {
//                                     System.out.println("DDDN "+studentDepart);
//                                     System.out.println("DDYN "+studentyear);
//                                     System.out.println("DDD1 "+r.getYear());
                                     String[] depart = r.getDepartment().split(" ");
                                     String[] year = r.getYear().split(" ");

//                                     for (int i = 0 ; i<depart.length ; i++){
//                                         System.out.println("DDD "+depart[i]);
//                                     }
//                                     for (int i = 0 ; i<year.length ; i++){
//                                         System.out.println("DDY "+year[i]);
//                                     }

                                     if(checkDepartment(depart)){
                                         if(checkYear(year)){


                                         eventList.add(new Event(r.getEventId(),r.getEventname(), r.getStudentId()
                                                 , r.getStartdate(), r.getEnddate(), r.getStrattime()
                                                 , r.getEndtime() , r.getCategoryId() , r.getEventdetail()
                                                 , r.getAmount() , r.getDepartment()
                                                 , r.getYear() , r.getPlacename() , r.getLatitude()
                                                 , r.getLongitude() , r.getAddress()));
                                        }
                                     }
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

    private void setLocationCurrent(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Student").child(firebaseUser.getUid());
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("latitude", Latitude_current);
        hashMap.put("longitude", Longitude_current);
        reference.updateChildren(hashMap);
        System.out.println("LOOO");

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission
                (getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                Latitude_current = latti;
                Longitude_current = longi;


            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                Latitude_current = latti;
                Longitude_current = longi;


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                Latitude_current = latti;
                Longitude_current = longi;

            } else {

                Toast.makeText(getContext(), "Unble to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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




