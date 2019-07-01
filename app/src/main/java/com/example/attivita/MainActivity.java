package com.example.attivita;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.fragment.HomeFragment;
import com.example.attivita.fragment.MessageFragment;
import com.example.attivita.fragment.MyEventFragment;
import com.example.attivita.fragment.ProfileFragment;
import com.example.attivita.fragment.SeachFragment;
import com.example.attivita.model.StudentFirebase;
import com.example.attivita.model.StudentPHP;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.attivita.notification.App.CHANNEL_1_ID;
import static com.example.attivita.notification.App.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public String id ="";
    String studentid;
    boolean status_notification;
    boolean status_notificationAccept;
    SharedPreferences shared;

    private static final String MY_PREFS = "prefs";
    NotificationManagerCompat notificationManager;

    StudentPHP student = new StudentPHP();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        notificationManager = NotificationManagerCompat.from(this);

        shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        studentid = shared.getString("studentId",null);

       // mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
         Intent i = getIntent();
        id = i.getStringExtra("id");


        System.out.println("FIRST "+student.getFirstname());


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectFragment = new HomeFragment();
                    break;
                case R.id.nav_search:
                    selectFragment = new SeachFragment();
                    break;
                case R.id.nav_message:
                    selectFragment = new MessageFragment();
                    break;
                case R.id.nav_notifications:
                    selectFragment = new MyEventFragment();
                    break;
                case R.id.nav_profile:
                    selectFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).commit();

            return true;
        }
    };

    private void checkHelp(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");

        System.out.println("RRR "+reference);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("RRR2.1");
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    StudentFirebase studentFirebase = snapshot.getValue(StudentFirebase.class);
                    assert studentFirebase != null;
                    assert firebaseUser != null;



                    if(studentFirebase.getId().equals(firebaseUser.getUid())){

                        System.out.println("RRR3 " + studentFirebase.getUsername());


                        if (studentFirebase.isStatus_helpful() && !studentFirebase.isStatusAccept_helpful()){
                            System.out.println("RRR4 ");
                            final String detail = studentFirebase.getDetail_helpful();

                            final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
                            Call<StudentPHP> call = apiService.readstudent(studentFirebase.getHelped());
                            call.enqueue(new Callback<StudentPHP>() {
                                @Override
                                public void onResponse(Call<StudentPHP> call, Response<StudentPHP> response) {
                                    StudentPHP res = response.body();

                                    if (res.isStatus()){

                                        student = new StudentPHP(res.getFirstname(),res.getLastname());
                                        buildNotificationHelp(detail);
                                        status_notification = true;
                                        SharedPreferences.Editor editor = shared.edit();
                                        editor.putBoolean("status_notification", status_notification);
                                        editor.apply();
                                        System.out.println("EEEE");

                                    }
                                }
                                @Override
                                public void onFailure(Call<StudentPHP> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                                    System.err.println("ERRORRRRR : "+ t.getMessage());
                                }
                            });

                        } else {
                            System.out.println("UpdateERROR");
                        }
                    }
                    if(!studentFirebase.getId().equals(firebaseUser.getUid())) {

                        System.out.println("SSS3 " + studentFirebase.getUsername());

                        if (studentFirebase.getHelped().equals(studentid) && studentFirebase.isStatusAccept_helpful()){

                            System.out.println("SSS4 ");

                            final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
                            Call<StudentPHP> call = apiService.readstudent(studentFirebase.getUsername());
                            call.enqueue(new Callback<StudentPHP>() {
                                @Override
                                public void onResponse(Call<StudentPHP> call, Response<StudentPHP> response) {
                                    StudentPHP res = response.body();

                                    if (res.isStatus()){

                                        final String name = res.getFirstname()+" "+res.getLastname();
                                        buildNotificationAcceptHelp(name);
                                        status_notificationAccept = true;
                                        SharedPreferences.Editor editor = shared.edit();
                                        editor.putBoolean("status_notificationAccept", status_notificationAccept);
                                        editor.apply();
                                        System.out.println("EEEE2");

                                    }
                                }
                                @Override
                                public void onFailure(Call<StudentPHP> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                                    System.err.println("ERRORRRRR : "+ t.getMessage());
                                }
                            });
                        } else {
                            System.out.println("OUT");
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

    public void buildNotificationHelp(String detail){

        String name = student.getFirstname()+" "+student.getLastname();
        System.out.println("BB");

        Intent activityIntent = new Intent(this, RequestHelpActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.star3)
                .setContentTitle("ร้องขอความช่วยเหลือ")
                .setContentText("จากคุณ "+name+" "+detail)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(1, notification);
        System.out.println("BBB");

    }

    public void buildNotificationAcceptHelp(String name){

        System.out.println("BB2");

        Notification notification2 = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.star3)
                .setContentTitle("ได้รับการช่วยเหลือ")
                .setContentText("จากคุณ "+name)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(2, notification2);
        System.out.println("BBB2");

    }

    @Override
    public void onStart() {
        super.onStart();
        status_notification = shared.getBoolean("status_notification", false);
        status_notificationAccept = shared.getBoolean("status_notificationAccept", false);
        if (!status_notification ) {
            checkHelp();
        }
        if (!status_notificationAccept ) {
            checkHelp();
        }


        System.out.println("++ ON START ++ "+status_notification +status_notificationAccept);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("+ ON RESUME +");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
//        shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
//        status_notification = shared.getBoolean("status_notification", false);
//        status_notificationAccept = shared.getBoolean("status_notificationAccept", false);
//        if (!status_notification) {
//            checkHelp();
//        }
//        if (!status_notificationAccept) {
//            checkHelp();
//        }
//        System.out.println("-- ON STOP -- ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        status_notification = shared.getBoolean("status_notification", false);
        status_notificationAccept = shared.getBoolean("status_notificationAccept", false);
        if (!status_notification ) {
            checkHelp();
        }
        if (!status_notificationAccept ) {
            checkHelp();
        }
        System.out.println("- ON DESTROY - "+status_notification +status_notificationAccept);
    }


}
