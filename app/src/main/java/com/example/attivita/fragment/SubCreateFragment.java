package com.example.attivita.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.EditeventActivity;
import com.example.attivita.EventDetails;
import com.example.attivita.EventcreateDetailActivity;
import com.example.attivita.R;
import com.example.attivita.adapter.EventListAdapter;
import com.example.attivita.model.Event;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubCreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubCreateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ArrayList<Event> eventList = new ArrayList<>();
    ListView listView;
    TextView textbg_subcreate;

    private static final String MY_PREFS = "prefs";

    public SubCreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubCreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubCreateFragment newInstance(String param1, String param2) {
        SubCreateFragment fragment = new SubCreateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_create, container, false);

        listView = view.findViewById(R.id.list_createvent);
        textbg_subcreate = view.findViewById(R.id.textBackgroung_subcreate);

        SharedPreferences shared = getContext().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);

        final String studentId = shared.getString("studentId",null);
        setEventList(studentId);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String eventId = String.valueOf(eventList.get(i).getEventId());
                String eventCategoryId = String.valueOf(eventList.get(i).getCategoryId());
                String eventAmount = String.valueOf(eventList.get(i).getAmount());
                String eventLatitude = String.valueOf(eventList.get(i).getLatitude());
                String eventLongitude = String.valueOf(eventList.get(i).getLongitude());

                Intent intent = new Intent(getContext(), EventcreateDetailActivity.class);
                intent.putExtra("eventId", eventId);
                intent.putExtra("eventName", eventList.get(i).getEventname());
                intent.putExtra("eventStuId", eventList.get(i).getStudentId());
                intent.putExtra("eventDetail", eventList.get(i).getEventdetail());
                intent.putExtra("eventAmount", eventAmount);
                intent.putExtra("eventStartdate", eventList.get(i).getStartdate());
                intent.putExtra("eventEnddate", eventList.get(i).getEnddate());
                intent.putExtra("eventStarttime", eventList.get(i).getStrattime());
                intent.putExtra("eventEndtime", eventList.get(i).getEndtime());
                intent.putExtra("eventDepart", eventList.get(i).getDepartment());
                intent.putExtra("eventCategoryId", eventCategoryId);
                intent.putExtra("eventYear", eventList.get(i).getYear());
                intent.putExtra("eventPlacename", eventList.get(i).getPlacename());
                intent.putExtra("eventLatitude", eventLatitude);
                intent.putExtra("eventLongitude", eventLongitude);
                intent.putExtra("eventAddress", eventList.get(i).getAddress());
                startActivity(intent);
            }
        });

        return view;
    }

    public void setEventList(String stuId) {

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<ArrayList<Event>> call = apiService.readeventcreate(stuId);
        call.enqueue(new Callback<ArrayList<Event>>() {
                         @Override
                         public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                             ArrayList<Event> res = response.body();
                             eventList.clear();

                             if (res.size() != 0) {

                                 for (Event r : res) {
                                     eventList.add(new Event(r.getEventId(),r.getEventname(), r.getStudentId()
                                             , r.getStartdate(), r.getEnddate(), r.getStrattime()
                                             , r.getEndtime() , r.getCategoryId() , r.getEventdetail()
                                             , r.getAmount() , r.getDepartment()
                                             , r.getYear() , r.getPlacename() , r.getLatitude()
                                             , r.getLongitude() , r.getAddress()));
                                 }
                                 EventListAdapter adapter = new EventListAdapter(getContext(),R.layout.item_event, eventList);
                                 listView.setAdapter(adapter);
                             } else {
                                 textbg_subcreate.setText("ไม่มีกิจกรรมที่คุณสร้าง");
                             }
                         }

                         @Override
                         public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                             Toast.makeText(getContext(), "Refresh Fail...", Toast.LENGTH_SHORT).show();
                         }
                     }
        );
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
