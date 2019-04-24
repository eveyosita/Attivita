package com.example.attivita.fragment;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.attivita.R;
import com.example.attivita.adapter.PagerAdapter;

public class MyEventFragment extends Fragment implements SubCreateFragment.OnFragmentInteractionListener,SubJoinFragment.OnFragmentInteractionListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_event, container, false);

        TabLayout tabLayout = (TabLayout)v.findViewById(R.id.tablayout);
        final ViewPager viewPager = (ViewPager)v.findViewById(R.id.page);

        PagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager(),2);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return v;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
