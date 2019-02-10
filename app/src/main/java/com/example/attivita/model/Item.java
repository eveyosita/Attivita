package com.example.attivita.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Item {

    int im;
    String tx;

    public Item (int im,String tx){
        this.im = im;
        this.tx = tx;
    }

    public int getIm(){
        return im;
    }

    public String getTx(){
        return tx;
    }

    public void setIm(int im){
        this.im = im;
    }

    public void setTx(String tx){
        this.tx = tx;
    }



}
