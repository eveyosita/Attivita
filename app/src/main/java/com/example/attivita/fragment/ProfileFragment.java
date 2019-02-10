package com.example.attivita.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.R;
import com.example.attivita.RegisterActivity;
import com.example.attivita.model.student;
import com.example.attivita.retrofit.RetrofitClient;

;import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    String id;
    student stu;
    TextView fnamePro;
    TextView lnamePro;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Intent i = getActivity().getIntent();
        id = i.getStringExtra("id");
        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();

        stu = new student();
        setStulist(id);

        System.out.println("Firstname "+stu.getFirstname());
        System.out.println("Lastname "+stu.getLastname());

      /*  fnamePro = (TextView) v.findViewById(R.id.firstname_pro);
        fnamePro.setText(stu.getFirstname());

        lnamePro = (TextView) v.findViewById(R.id.lastname_pro);
        lnamePro.setText(stu.getLastname());*/

        //setFnamePro(stu.getFirstname());
        //setLnamePro(stu.getLastname());



        return v;
    }

   /* public void setFnamePro(String firstname){


    }

    public void setLnamePro(String lastname){
        lnamePro = (TextView) getView().findViewById(R.id.lastname_pro);
        lnamePro.setText(lastname);
    }*/

    public void setStulist(String id){
        Call<student> call = RetrofitClient
                .getInstance()
                .getApi()
                .showprofile(id);

        call.enqueue(new Callback<student>() {
            @Override
            public void onResponse(Call<student> call, Response<student> response) {

                student res = response.body();
                System.out.println("Student "+response.body().getFirstname() +" "+ response.body().getLastname());
                System.out.println("Firstname "+res.getFirstname());
                System.out.println("Lastname "+res.getLastname());

                stu.setFirstname(res.getFirstname());
                stu.setLastname(res.getLastname());

                Toast.makeText(getContext(), "Succees", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<student> call, Throwable t) {
                Toast.makeText(getContext(), "Fail"+t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("ERRORRRRR : "+ t.getMessage());
            }
        });
    }

}
