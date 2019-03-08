package com.example.attivita.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attivita.adapter.CustomAdapter;
import com.example.attivita.adapter.EventListAdapter;
import com.example.attivita.model.Event;
import com.example.attivita.model.Item;
import com.example.attivita.R;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.example.attivita.search_by_category;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeachFragment extends Fragment {
    ArrayList<Event> eventList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        GridView listView = (GridView) v.findViewById(R.id.listseach);
        final ArrayList<Item> arrayList = new ArrayList<>();

        arrayList.add(new Item(R.drawable.shopping_bag,"ขายของ"));
        arrayList.add(new Item(R.drawable.volunteer,"จิตอาสา"));
        arrayList.add(new Item(R.drawable.open_book,"ติวหนังสือ"));
        arrayList.add(new Item(R.drawable.temple,"ทำบุญไหว้พระ"));
        arrayList.add(new Item(R.drawable.airplane,"ท่องเที่ยว"));
        arrayList.add(new Item(R.drawable.gamepad,"เล่นเกมส์"));
        arrayList.add(new Item(R.drawable.dish,"สังสรรค์"));
        arrayList.add(new Item(R.drawable.dumbbell,"ออกกำลังกาย"));


//        int[] resId = { R.drawable.dish
//                , R.drawable.dumbbell, R.drawable.open_book
//                , R.drawable.airplane, R.drawable.shopping_bag
//                , R.drawable.temple, R.drawable.volunteer
//                , R.drawable.gamepad };
//
//        String[] list = { "สังสรรค์", "ออกกำลังกาย", "ติวหนังสือ"
//                , "ท่องเที่ยว", "ขายของ", "ทำบุญไหว้พระ", "จิตอาสา"
//                , "เล่นเกมส์" };

        CustomAdapter adapter = new CustomAdapter(getContext(),R.layout.item_seach, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), ""+i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), search_by_category.class);
                intent.putExtra("categoryId", i);
                intent.putExtra("categoryName", arrayList.get(i).getTx());
                startActivity(intent);
            }
        });

        return v;
    }

    }
