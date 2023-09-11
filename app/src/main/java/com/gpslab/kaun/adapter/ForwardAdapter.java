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
import com.gpslab.kaun.fullscreen.MimeTypes;
import com.gpslab.kaun.model.Chat;
import com.gpslab.kaun.view.ChatActivity;
import com.gpslab.kaun.view.ForwardChat;
import com.gpslab.kaun.view.IntentUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForwardAdapter extends RecyclerView.Adapter<ForwardAdapter.ChatsViewHolder> {
    OnUserClick onUserClick;
    private Context context;
    private ArrayList<ForwardChat> chats;

    public ForwardAdapter(Context context, ArrayList<ForwardChat> chats, OnUserClick onUserClick) {
        this.context = context;
        this.chats = chats;
        this.onUserClick = onUserClick;
    }

    public void filterList(ArrayList<ForwardChat> filteredList) {
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

        ForwardChat chat = chats.get(i);

        if (chat.getName() == null) {
            //It is just a footer
            chatsViewHolder.chatLayoutContainer.setVisibility(View.INVISIBLE);
        }

        Glide.with(context)
                .load(chat.getImage())
                .apply(new RequestOptions().placeholder(R.drawable.profile))
                .into(chatsViewHolder.profilePic);

        chatsViewHolder.tvName.setText(chat.getName());
        chatsViewHolder.tvMsg.setText(chat.getLastMessage());
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
                    onUserClick.onChange(chats.get(getAdapterPosition()), false);




                }
            });
        }
    }
    public interface OnUserClick {
        void onChange(ForwardChat user, boolean added);
    }
}


