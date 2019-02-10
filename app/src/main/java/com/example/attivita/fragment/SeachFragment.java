package com.example.attivita.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attivita.adapter.CustomAdapter;
import com.example.attivita.model.Item;
import com.example.attivita.R;

import java.util.ArrayList;

public class SeachFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        ListView listView = (ListView) v.findViewById(R.id.listseach);
        ArrayList<Item> arrayList = new ArrayList<>();

        arrayList.add(new Item(R.drawable.dish,"สังสรรค์"));
        arrayList.add(new Item(R.drawable.dumbbell,"ออกกำลังกาย"));
        arrayList.add(new Item(R.drawable.open_book,"ติวหนังสือ"));
        arrayList.add(new Item(R.drawable.airplane,"ท่องเที่ยว"));
        arrayList.add(new Item(R.drawable.shopping_bag,"ขายของ"));
        arrayList.add(new Item(R.drawable.temple,"ทำบุญไหว้พระ"));
        arrayList.add(new Item(R.drawable.volunteer,"จิตอาสา"));
        arrayList.add(new Item(R.drawable.gamepad,"เล่นเกมส์"));

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
            }
        });

        return v;
    }

}
