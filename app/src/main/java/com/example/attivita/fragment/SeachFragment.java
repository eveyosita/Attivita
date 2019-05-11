package com.example.attivita.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

public class SeachFragment extends Fragment implements View.OnClickListener{
    ArrayList<Event> eventList = new ArrayList<>();

    EditText editText_search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        v.findViewById(R.id.imageView1).setOnClickListener(this);
        v.findViewById(R.id.imageView2).setOnClickListener(this);
        v.findViewById(R.id.imageView3).setOnClickListener(this);
        v.findViewById(R.id.imageView4).setOnClickListener(this);
        v.findViewById(R.id.imageView5).setOnClickListener(this);
        v.findViewById(R.id.imageView6).setOnClickListener(this);
        v.findViewById(R.id.imageView7).setOnClickListener(this);
        v.findViewById(R.id.imageView8).setOnClickListener(this);
        v.findViewById(R.id.button_search).setOnClickListener(this);

        editText_search = v.findViewById(R.id.edittext_search);
        editText_search.getText().toString();


        return v;
    }

    public void sendType(String cateId, String namecate){

        Intent intent = new Intent(getContext(), search_by_category.class);
        intent.putExtra("categoryId", cateId);
        intent.putExtra("categoryName", namecate);
        startActivity(intent);

    }

    private void search() {
        String search = editText_search.getText().toString();
        if(search.equals("")){
            Toast.makeText(getContext(), "ยังไมไ่ด้พิมพ์ข้อความ", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(getContext(), search_by_category.class);
            intent.putExtra("textSearch",search);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView1:
                sendType("0","ขายของ");
                break;
            case R.id.imageView2:
                sendType("1","จิตอาสา");
                break;
            case R.id.imageView3:
                sendType("2","ติวหนังสือ");
                break;
            case R.id.imageView4:
                sendType("3","ทำบุญไหว้พระ");
                break;
            case R.id.imageView5:
                sendType("4","ท่องเที่ยว");
                break;
            case R.id.imageView6:
                sendType("5","เล่นเกมส์");
                break;
            case R.id.imageView7:
                sendType("6","สังสรรค์");
                break;
            case R.id.imageView8:
                sendType("7","ออกกำลังกาย");
                break;
            case R.id.button_search:
                search();
                break;

        }
    }

}

//        GridView listView = (GridView) v.findViewById(R.id.listseach);
//        final ArrayList<Item> arrayList = new ArrayList<>();
//
//        arrayList.add(new Item(R.drawable.shopping_bag,"ขายของ"));
//        arrayList.add(new Item(R.drawable.volunteer,"จิตอาสา"));
//        arrayList.add(new Item(R.drawable.open_book,"ติวหนังสือ"));
//        arrayList.add(new Item(R.drawable.temple,"ทำบุญไหว้พระ"));
//        arrayList.add(new Item(R.drawable.airplane,"ท่องเที่ยว"));
//        arrayList.add(new Item(R.drawable.gamepad,"เล่นเกมส์"));
//        arrayList.add(new Item(R.drawable.dish,"สังสรรค์"));
//        arrayList.add(new Item(R.drawable.dumbbell,"ออกกำลังกาย"));
//
//
////        int[] resId = { R.drawable.dish
////                , R.drawable.dumbbell, R.drawable.open_book
////                , R.drawable.airplane, R.drawable.shopping_bag
////                , R.drawable.temple, R.drawable.volunteer
////                , R.drawable.gamepad };
////
////        String[] list = { "สังสรรค์", "ออกกำลังกาย", "ติวหนังสือ"
////                , "ท่องเที่ยว", "ขายของ", "ทำบุญไหว้พระ", "จิตอาสา"
////                , "เล่นเกมส์" };
//
//        CustomAdapter adapter = new CustomAdapter(getContext(),R.layout.item_seach, arrayList);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), ""+i, Toast.LENGTH_SHORT).show();
//                String cateId = Integer.toString(i);
//                Intent intent = new Intent(getContext(), search_by_category.class);
//                intent.putExtra("categoryId", cateId);
//                intent.putExtra("categoryName", arrayList.get(i).getTx());
//                startActivity(intent);
//            }
//        });
