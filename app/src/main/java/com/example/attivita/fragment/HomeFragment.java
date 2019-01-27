package com.example.attivita.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.AddeventActivity;
import com.example.attivita.AddwarnActivity;
import com.example.attivita.R;

import java.time.Instant;


public class HomeFragment extends Fragment  {
        ImageButton imbut_addwarn;
        ImageButton imbut_addevent;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
             View v = inflater.inflate(R.layout.fragment_home, container, false);
                imbut_addevent = (ImageButton)v.findViewById(R.id.addevent_imBut);
                imbut_addwarn = (ImageButton)v.findViewById(R.id.addwarn_imBut);

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


             return v;
        }


}




