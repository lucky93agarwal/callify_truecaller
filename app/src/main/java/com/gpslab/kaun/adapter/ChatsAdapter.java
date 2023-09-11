package com.gpslab.kaun.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gpslab.kaun.Profile.ProfileDialogActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.messages.MessagesActivity;
import com.gpslab.kaun.model.Chat;
import com.gpslab.kaun.model.GetContectData;
import com.gpslab.kaun.view.ChatActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {

    private Context context;
    private ArrayList<Chat> chats;

    public ChatsAdapter(Context context, ArrayList<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    public void filterList(ArrayList<Chat> filteredList) {
        chats = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChatsViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder chatsViewHolder, int i) {

        Chat chat = chats.get(i);

        if (chat.getName() == null) {
            //It is just a footer
            chatsViewHolder.chatLayoutContainer.setVisibility(View.INVISIBLE);
        }

        Glide.with(context)
                .load(chat.getImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.profile))
                .into(chatsViewHolder.profilePic);

        chatsViewHolder.tvName.setText(chat.getName());
        if(chat.getLastMessage().equalsIgnoreCase("NA")){
            chatsViewHolder.tvMsg.setText("Hey there! I am using Callify.");
        }else if(chat.getLastMessage().equalsIgnoreCase("0")){
            chatsViewHolder.tvMsg.setText("Hey there! I am using Callify.");
        }else {
            chatsViewHolder.tvMsg.setText(chat.getLastMessage());
        }

        chatsViewHolder.tvTime.setText(chat.getLastMessageTime());


    }

    @Override
    public int getItemCount() {
        if (chats != null) {
            return chats.size();
        }
        return 0;
    }

    class ChatsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profilePic;
        TextView tvName;
        TextView tvMsg;
        TextView tvTime;
        RelativeLayout chatLayoutContainer;

        ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.tvName);
            tvMsg = itemView.findViewById(R.id.tvLastMsg);
            tvTime = itemView.findViewById(R.id.tvTime);
            chatLayoutContainer = itemView.findViewById(R.id.chat_row_container);

            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(new Intent(context, ProfileDialogActivity.class));
                    i.putExtra("url", chats.get(getAdapterPosition()).getImage());
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation((Activity)context, view, "transition");
                    context.startActivity(i, options.toBundle());
                }
            });


            chatLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("ChatData",0);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("chat_id",chats.get(getAdapterPosition()).getId());
                    edit.apply();
                    Intent i = new Intent(new Intent(context, ChatActivity.class));
                    i.putExtra("image",chats.get(getAdapterPosition()).getImage());
                    i.putExtra("name",chats.get(getAdapterPosition()).getName());
                    i.putExtra("id",chats.get(getAdapterPosition()).getId());
                    context.startActivity(i);



                }
            });
        }
    }
}

