package com.example.attivita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attivita.R;
import com.example.attivita.model.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class EventListAdapter extends BaseAdapter {
    Context mContext;
    private int mLayoutResId;
    private ArrayList<Event> resultList;
    public EventListAdapter(Context context, int mLayoutResId, ArrayList<Event> resultList){
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
        TextView nameevent = (TextView)itemLayout.findViewById(R.id.name_event);
        TextView amountevent = (TextView)itemLayout.findViewById(R.id.amount_event);
        TextView dateevent = (TextView)itemLayout.findViewById(R.id.date_event);
        TextView timeevent = (TextView)itemLayout.findViewById(R.id.time_event);
        TextView cateegoryevent = (TextView)itemLayout.findViewById(R.id.cateegory_event);

        String name = "ชื่อกิจกรรม : "+resultList.get(i).getEventname();
        String amout = "จำนวนคน : "+resultList.get(i).getAmount();
        String getCategory = getCategory(resultList.get(i).getCategoryId());
        String category = "ประเภทกิจกรรม : "+getCategory;

        String dateStart = resultList.get(i).getStartdate();
        String dateEnd = resultList.get(i).getEnddate();
        String getOnlyDateStart = dateStart.substring(8);
        String getOnlyDateEnd = dateEnd.substring(8);
        String getOnlyMonth = getMonth(dateEnd.substring(5,7));
        String getOnlyYear = ""+(Integer.parseInt(dateEnd.substring(0,4))+543);
        String date;
        if(getOnlyDateStart.equals(getOnlyDateEnd)){
            date = "วันที่จัดกิจจกรม : "+getOnlyDateStart +  " " + getOnlyMonth + " " +getOnlyYear;
        } else {
            date = "วันที่จัดกิจจกรม : "+getOnlyDateStart + " - " + getOnlyDateEnd + " " + getOnlyMonth + " " +getOnlyYear;
        }


        String timeStart = resultList.get(i).getStrattime();
        timeStart = timeStart.substring(0,5);
        String timeend = resultList.get(i).getEndtime();
        timeend = timeend.substring(0,5);
        String time = "เวลาที่จัดกิจจกรม : "+timeStart+" - "+timeend;

        nameevent.setText(name);
        amountevent.setText(amout);
        cateegoryevent.setText(category);
        dateevent.setText(date);
        timeevent.setText(time);

        return itemLayout;
    }
    String getMonth(String month){
        switch (month){
            case "01":
                return "มกราคม";
            case "02":
                return "กุมภาพันธ์";
            case "03":
                return "มีนาคม";
            case "04":
                return "เมษายม";
            case "05":
                return "พฤษภาคม";
            case "06":
                return "มิถุนายม";
            case "07":
                return "กรกฎาคม";
            case "08":
                return "สิงหาคม";
            case "09":
                return "กันยายน";
            case "10":
                return "ตุลาคม";
            case "11":
                return "พฤศจิกายน";
            case "12":
                return "ธันวาคม";
            default: break;
        }
        return "";
    }

    String getCategory(int cate){
        switch (cate){
            case 0 :
                return "ขายของ";
            case 1:
                return "จิตอาสา";
            case 2:
                return "ติวหนังสือ";
            case 3:
                return "ทำบุญไหว้พระ";
            case 4:
                return "ท่องเที่ยว";
            case 5:
                return "เล่นเกมส์";
            case 6:
                return "สังสรรค์";
            case 7:
                return "ออกกำลังกาย";
            default: break;
        }
        return "";
    }
}
