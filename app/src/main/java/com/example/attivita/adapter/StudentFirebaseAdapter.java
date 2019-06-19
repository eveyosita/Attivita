package com.example.attivita.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.MessagesActivity;
import com.example.attivita.R;
import com.example.attivita.SharedPrefManager;
import com.example.attivita.model.Student;
import com.example.attivita.model.StudentFirebase;
import com.example.attivita.model.StudentPHP;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentFirebaseAdapter extends RecyclerView.Adapter<StudentFirebaseAdapter.UserFirebaseViewHolder>  {

    private Context mCtx;
    private List<StudentFirebase> userList;

    public StudentFirebaseAdapter(Context mCtx, List<StudentFirebase> users) {
        this.mCtx = mCtx;
        this.userList = users;
    }

    @NonNull
    @Override
    public UserFirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_student, viewGroup, false);
        return new UserFirebaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserFirebaseViewHolder userFirebaseViewHolder, int i) {


        final StudentFirebase studentFirebase = userList.get(i);

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<StudentPHP> call = apiService.readstudent(studentFirebase.getUsername());

        call.enqueue(new Callback<StudentPHP>() {
            @Override
            public void onResponse(Call<StudentPHP> call, Response<StudentPHP> response) {
                StudentPHP res = response.body();
                if (res.isStatus()) {
                    String username = res.getFirstname()+" "+res.getLastname();
                    userFirebaseViewHolder.textViewUsername.setText(username);

                    String picture = res.getProfile_pic();
                    if (picture == null) {
                        userFirebaseViewHolder.circleImageViewUser.setImageResource(R.drawable.girl);
                    } else {
                        String url = "http://pilot.cp.su.ac.th/usr/u07580553/attivita/picture/profile/"+picture;
                        System.out.println(url);
                        Picasso.get().load(url).into(userFirebaseViewHolder.circleImageViewUser);
                    }


                } else {
                    Toast.makeText(mCtx, res.getMassage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StudentPHP> call, Throwable t) {
                Toast.makeText(mCtx, "FAIL", Toast.LENGTH_LONG).show();
            }
        });


        userFirebaseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx, MessagesActivity.class);
                i.putExtra("userid",studentFirebase.getId());
                i.putExtra("studentid",studentFirebase.getUsername());
                mCtx.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() { return userList.size();
    }

    class UserFirebaseViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageViewUser;
        TextView textViewUsername;

        public UserFirebaseViewHolder(View itemView) {
            super(itemView);

            circleImageViewUser = itemView.findViewById(R.id.circleImageViewUser);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
        }
    }



}
