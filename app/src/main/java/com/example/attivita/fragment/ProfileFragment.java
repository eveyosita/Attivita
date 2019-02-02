package com.example.attivita.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.attivita.LoginActivity;
import com.example.attivita.R;
import com.example.attivita.RetrofitClient;

import org.json.JSONObject;;

import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Intent i = getActivity().getIntent();
        String id = i.getStringExtra("id");
        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();



//        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().showprofile(id);




        return v;
    }


}
