package com.example.attivita.fragment;

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
import com.example.attivita.model.Chatlist;
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

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;

    private StudentFirebaseAdapter studentFirebaseAdapter;
    private List<StudentFirebase> studentFirebaseList;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private List<Chatlist> studentList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        studentList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    studentList.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    private void chatList() {

        studentFirebaseList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Student");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentFirebaseList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StudentFirebase studentFirebase = snapshot.getValue(StudentFirebase.class);
                    for(Chatlist chatlist : studentList){
                        if(studentFirebase.getId().equals(chatlist.getId())){
                            studentFirebaseList.add(studentFirebase);
                        }
                    }
                }

                studentFirebaseAdapter = new StudentFirebaseAdapter(getContext(),studentFirebaseList);
                recyclerView.setAdapter(studentFirebaseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
