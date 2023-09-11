package com.gpslab.kaun.upgrade_to_premium;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.gpslab.kaun.R;
import com.gpslab.kaun.model.Chat;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewUpgradAdapter extends RecyclerView.Adapter<NewUpgradAdapter.ChatsViewHolder> {

    private Context context;
    private ArrayList<Chat> chats;
    private OnItemClickListener listener;



    public NewUpgradAdapter(Context context, ArrayList<Chat> chats, OnItemClickListener listener) {
        this.context = context;
        this.chats = chats;
        this.listener = listener;
    }



    public interface OnItemClickListener {
        void onItemClick(Chat item);
    }
    public void filterList(ArrayList<Chat> filteredList) {
        chats = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChatsViewHolder(LayoutInflater.from(context).inflate(R.layout.upgrad_chat_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder chatsViewHolder, int i) {

        Chat chat = chats.get(i);

        if (chat.getName() == null) {
            //It is just a footer
            chatsViewHolder.chatLayoutContainer.setVisibility(View.INVISIBLE);
        }

        Glide.with(context)
                .load("chat.getImage()")
                .apply(new RequestOptions().placeholder(R.drawable.backgoundluckynew))
                .into(chatsViewHolder.profilePic);

        chatsViewHolder.tvName.setText("xxx  xxx");
        chatsViewHolder.tvMsg.setText("xxxxxxxxxxxx");


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



            tvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(chats.get(getAdapterPosition()));
//                    Intent inten = new Intent(context, UpgradePremiumDetailsActivity.class);
//                    inten.putExtra("name",chats.get(getAdapterPosition()).getName());
//                    inten.putExtra("number",chats.get(getAdapterPosition()).getId());
//                    inten.putExtra("img",chats.get(getAdapterPosition()).getImage());
//                    inten.putExtra("duration",String.valueOf("100"));
//                    context.startActivity(inten);



                }
            });
        }
    }
}



