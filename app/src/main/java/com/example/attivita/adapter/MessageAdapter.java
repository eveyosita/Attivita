package com.example.attivita.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attivita.R;
import com.example.attivita.model.Chat;
import com.example.attivita.model.Student;
import com.example.attivita.model.StudentPHP;
import com.example.attivita.retrofit.APIInterface;
import com.example.attivita.retrofit.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.UserFirebaseViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mCtx;
    private List<Chat> chatList;
    private String studentid;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mCtx, List<Chat> chatList, String studentid) {
        this.mCtx = mCtx;
        this.chatList = chatList;
        this.studentid = studentid;
    }

    @NonNull
    @Override
    public MessageAdapter.UserFirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if( i == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mCtx).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.UserFirebaseViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.UserFirebaseViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.UserFirebaseViewHolder userFirebaseViewHolder, int i) {
        Chat chat = chatList.get(i);
        userFirebaseViewHolder.textViewShowTextMessage.setText(chat.getMessage());

        final APIInterface apiService = RetrofitClient.getClient().create(APIInterface.class);
        Call<StudentPHP> call = apiService.getstudent(studentid);
        call.enqueue(new Callback<StudentPHP>() {
            @Override
            public void onResponse(Call<StudentPHP> call, Response<StudentPHP> response) {

                if (response.body().isStatus()) {
                    String picture = response.body().getProfile_pic();
                    if (picture == null) {
                        userFirebaseViewHolder.circleImageViewUser.setImageResource(R.drawable.girl);
                    } else {
                        String url = "http://pilot.cp.su.ac.th/usr/u07580553/attivita/picture/profile/"+picture;
                        System.out.println(url);
                        Picasso.get().load(url).into(userFirebaseViewHolder.circleImageViewUser);
                    }


                } else {
                    Toast.makeText(mCtx, response.body().getMassage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StudentPHP> call, Throwable t) {
                Toast.makeText(mCtx, "FAIL", Toast.LENGTH_LONG).show();
            }
        });

//        if(imageurl.equals("defult")){
//            userFirebaseViewHolder.circleImageViewUser.setImageResource(R.drawable.user);
//        }else{
//            Picasso.get().load(imageurl).into(userFirebaseViewHolder.circleImageViewUser);
//        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class UserFirebaseViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageViewUser;
        TextView textViewShowTextMessage;

        public UserFirebaseViewHolder(View itemView) {
            super(itemView);
            circleImageViewUser = itemView.findViewById(R.id.circleImageViewUser);
            textViewShowTextMessage = itemView.findViewById(R.id.textViewShowTextMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}