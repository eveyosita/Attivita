package com.example.attivita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attivita.model.Item;
import com.example.attivita.R;

import java.util.ArrayList;

public class CustomAdapter  extends BaseAdapter {
    Context mContext;
    private int mLayoutResId;
    private ArrayList<Item> resultList;

    public CustomAdapter(Context context, int mLayoutResId, ArrayList<Item> resultList){
        this.mContext = context;
        this.mLayoutResId = mLayoutResId;
        this.resultList = resultList;
    }
    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemLayout = inflater.inflate(mLayoutResId, null);
        ImageView imageCat = (ImageView)itemLayout.findViewById(R.id.imageView1);
        TextView textCat = (TextView)itemLayout.findViewById(R.id.textView1);

        textCat.setText(resultList.get(i).getTx());
       // imageCat.setImageResource(resultList.get(i).getIm());


        return itemLayout;
    }
}