package com.example.attivita.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.EventDetails;
import com.example.attivita.R;
import com.example.attivita.RequestHelpActivity;
import com.example.attivita.SettingActivity;
import com.example.attivita.ShowRequesthelpActivity;
import com.example.attivita.model.Eventhelp;
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

;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    TextView fnamePro,lnamePro,departPro,idPro,textview_name,textBackgroung_requsethelp,text_star,textview_studentid;

    Button btn_setting;
    RelativeLayout layout_profile,layout_requsethelp;
    CircleImageView circleImageView_profile;

    StudentFirebase student = new StudentFirebase();


    private static final String MY_PREFS = "prefs";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences shared = getContext().getSharedPreferences(MY_PREFS,
                Context.MODE_PRIVATE);

        final String stuid = shared.getString("studentId",null);
//        final String pass = shared.getString("password",null);
//        final String fname = shared.getString("firstname",null);
//        final String lname = shared.getString("lastname",null);
//        final String profile_pic = shared.getString("profile_pic",null);


        layout_profile =  v.findViewById(R.id.layout_profile);
        layout_requsethelp =  v.findViewById(R.id.layout_requsethelp);
        circleImageView_profile =  v.findViewById(R.id.circleImageView_profile);
        fnamePro =  v.findViewById(R.id.firstname_pro);
        lnamePro =  v.findViewById(R.id.lastname_pro);
        textview_studentid =  v.findViewById(R.id.textview_studentid);
        textview_name =  v.findViewById(R.id.textview_name);
        text_star =  v.findViewById(R.id.text_star);
        textBackgroung_requsethelp =  v.findViewById(R.id.textBackgroung_requsethelp);

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<StudentPHP> call = apiService.readstudent(stuid);

        call.enqueue(new Callback<StudentPHP>() {
            @Override
            public void onResponse(Call<StudentPHP> call, Response<StudentPHP> response) {
                StudentPHP res = response.body();
                if (res.isStatus()){

                    fnamePro.setText(res.getFirstname());
                    lnamePro.setText(res.getLastname());

                    String picture = res.getProfile_pic();
                    if(picture == null){
                        circleImageView_profile.setImageResource(R.drawable.girl);
                    }else{
                        String url = "http://pilot.cp.su.ac.th/usr/u07580553/attivita/picture/profile/"+picture;
                        System.out.println(url);
                        Picasso.get().load(url).into(circleImageView_profile);
                    }

                }
            }
            @Override
            public void onFailure(Call<StudentPHP> call, Throwable t) {
                Toast.makeText(getContext(), "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });

        layout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");

        System.out.println("RRR "+reference);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("RRR2.1");
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    final StudentFirebase studentFirebase = snapshot.getValue(StudentFirebase.class);
                    assert studentFirebase != null;
                    assert firebaseUser != null;

                    System.out.println("RRR- " + studentFirebase.getUsername());

                    if(studentFirebase.getId().equals(firebaseUser.getUid())){

                        System.out.println("RRR-- ");

                        String star = String.valueOf(studentFirebase.getStatus_star());
                        text_star.setText(star);
                        textview_studentid.setText(studentFirebase.getUsername());

                        if (studentFirebase.isStatus_helpful()){

                            getStudent(studentFirebase.getHelped());
//                            student.setDetail_helpful(studentFirebase.getDetail_helpful());
//                            student.setHelped_latitude(studentFirebase.getHelped_latitude());
//                            student.setHelped_longitude(studentFirebase.getHelped_longitude());

                            layout_requsethelp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String lati = String.valueOf(studentFirebase.getHelped_latitude());
                                    String logi = String.valueOf(studentFirebase.getHelped_longitude());
                                    Intent intent = new Intent(getContext(), ShowRequesthelpActivity.class);
                                    intent.putExtra("name", textview_name.getText().toString());
                                    intent.putExtra("detail", studentFirebase.getDetail_helpful());
                                    intent.putExtra("latitude", lati);
                                    intent.putExtra("longitude",logi);
                                    intent.putExtra("user",studentFirebase.getHelped());
                                    startActivity(intent);
                                }
                            });
                            System.out.println("RRR---");


                        } else {
                            final String text = "ยังไม่มีการร้องขอความช่วยเหลือในขณะนี้";
                            textBackgroung_requsethelp.setText(text);
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

        return v;
    }

    public void getStudent(String studentid){
        final APIInterface apiService2 = RetrofitClient.getClient().create(APIInterface.class);
        Call<StudentPHP> call2 = apiService2.readstudent(studentid);
        call2.enqueue(new Callback<StudentPHP>() {
            @Override
            public void onResponse(Call<StudentPHP> call, Response<StudentPHP> response) {
                StudentPHP res = response.body();

                if (res.isStatus()){
                    String name = "จากคุณ "+res.getFirstname()+" "+res.getLastname();
                    textview_name.setText(name);
                    System.out.println("EEEE");

                }
            }
            @Override
            public void onFailure(Call<StudentPHP> call, Throwable t) {
                Toast.makeText(getContext(), "Fail.."+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

}
