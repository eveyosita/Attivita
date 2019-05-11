package com.example.attivita.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.LoginActivity;
import com.example.attivita.R;
import com.example.attivita.RegisterActivity;
import com.example.attivita.model.student;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;

;import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    TextView fnamePro,lnamePro,departPro,idPro;
    Button btn_logout;

    private static final String MY_PREFS = "prefs";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences shared = getContext().getSharedPreferences(MY_PREFS,
                Context.MODE_PRIVATE);

        final String stuid = shared.getString("studentId",null);
        final String pass = shared.getString("password",null);
        final String fname = shared.getString("firstname",null);
        final String lname = shared.getString("lastname",null);
        final String depart = shared.getString("department",null);

        fnamePro =  v.findViewById(R.id.firstname_pro);
        lnamePro =  v.findViewById(R.id.lastname_pro);
        btn_logout =  v.findViewById(R.id.button_logout);
        departPro =  v.findViewById(R.id.depart_pro);
        idPro =  v.findViewById(R.id.id_pro);

        fnamePro.setText(fname);
        lnamePro.setText(lname);
//        departPro.setText(depart);
//        idPro.setText(stuid);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("ต้องการออกจากระบบใช่หรือไม่?");
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences shared = getContext().getSharedPreferences(MY_PREFS,
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();

                        //FirebaseAuth.getInstance().signOut();

                        editor.clear();
                        editor.commit();

                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();

                    }
                });
                builder.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                    }
                });
                builder.show();
            }
        });



        return v;
    }


//    }

}
