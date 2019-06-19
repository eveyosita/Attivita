package com.example.attivita.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.R;
import com.example.attivita.SettingActivity;
import com.example.attivita.model.StudentPHP;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    TextView fnamePro,lnamePro,departPro,idPro;

    Button btn_setting;
    RelativeLayout layout_profile;
    CircleImageView circleImageView_profile;


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
        circleImageView_profile =  v.findViewById(R.id.circleImageView_profile);
        fnamePro =  v.findViewById(R.id.firstname_pro);
        lnamePro =  v.findViewById(R.id.lastname_pro);


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

        return v;
    }

}
