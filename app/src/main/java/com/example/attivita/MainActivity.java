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

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public String id ="";
    boolean status_notification;
    SharedPreferences shared;

    private static final String MY_PREFS = "prefs";
    NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        notificationManager = NotificationManagerCompat.from(this);

       // mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
         Intent i = getIntent();
        id = i.getStringExtra("id");
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

                    System.out.println("RRR3 " + studentFirebase.getUsername());

                    if(studentFirebase.getId().equals(firebaseUser.getUid())){

                        System.out.println("DIS ");

                        if (studentFirebase.isStatus_helpful()){

                            buildNotificationHelp(studentFirebase.getHelped(),studentFirebase.getDetail_helpful());
                            break;

                        } else {
                            System.out.println("UpdateERROR");
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

    private void buildNotificationHelp(String studentid, String detail){



        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<StudentPHP> call = apiService.readstudent(studentid);

        call.enqueue(new Callback<StudentPHP>() {
            @Override
            public void onResponse(Call<StudentPHP> call, Response<StudentPHP> response) {
                StudentPHP res = response.body();
                if (res.isStatus()){
                    final String name = res.getFirstname()+" "+res.getLastname();


                }
            }
            @Override
            public void onFailure(Call<StudentPHP> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });

        Intent activityIntent = new Intent(this, RequestHelpActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.star3)
                .setContentTitle("ร้องขอความช่วยเหลือ")
                .setContentText("จากคุณ"+" "+detail)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(1, notification);
        status_notification = true;
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("status_notification", true);
        editor.apply();
        System.out.println("BBB");


    }

    @Override
    public void onStart() {
        super.onStart();
        shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        status_notification = shared.getBoolean("status_notification", false);
        if (!status_notification) {
            checkHelp();
        }
        System.out.println("++ ON START ++ ");
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
        shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        status_notification = shared.getBoolean("status_notification", false);
        if (!status_notification) {
            checkHelp();
        }
        System.out.println("-- ON STOP -- ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        status_notification = shared.getBoolean("status_notification", false);
        if (!status_notification){
            checkHelp();
        }
        System.out.println("- ON DESTROY - ");
    }


}
