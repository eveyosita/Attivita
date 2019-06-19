package com.example.attivita.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attivita.R;
import com.example.attivita.adapter.StudentFirebaseAdapter;
import com.example.attivita.model.Student;
import com.example.attivita.model.StudentFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentFragment extends Fragment {

    private RecyclerView recyclerView;
    private StudentFirebaseAdapter adapter;
    private List<StudentFirebase> studentFirebaseList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_student, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        studentFirebaseList = new ArrayList<>();
        readUsers();
        return v;
    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");

        System.out.println("RRR "+reference);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentFirebaseList.clear();
                System.out.println("RRR2.1");
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    StudentFirebase studentFirebase = snapshot.getValue(StudentFirebase.class);
                    System.out.println("RRR3");
                    assert studentFirebase != null;
                    assert firebaseUser != null;
                    if(!studentFirebase.getId().equals(firebaseUser.getUid())){
                        studentFirebaseList.add(studentFirebase);
                    }
                }

                adapter = new StudentFirebaseAdapter(getContext(),studentFirebaseList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("RRR2.2");
            }
        });

    }

}
