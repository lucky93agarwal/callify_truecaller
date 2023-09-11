package com.gpslab.kaun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gpslab.kaun.R;
import com.gpslab.kaun.block.AsyncExecutorUtil;
import com.gpslab.kaun.block.BlockedNumber;
import com.gpslab.kaun.block.BlockedNumberDao;
import com.gpslab.kaun.block.BlockedNumberDatabase;
import com.gpslab.kaun.block.GetBlockData;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.ChatsViewHolder> {

    private Context context;
    private ArrayList<GetBlockData> chats;

    public BlockAdapter(Context context, ArrayList<GetBlockData> chats) {
        this.context = context;
        this.chats = chats;
    }

    public void filterList(ArrayList<GetBlockData> filteredList) {
        chats = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BlockAdapter.ChatsViewHolder(LayoutInflater.from(context).inflate(R.layout.blockrecyclerlayout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlockAdapter.ChatsViewHolder chatsViewHolder, int i) {


        chatsViewHolder.tvName.setText(chats.get(i).getName());
        chatsViewHolder.tvMsg.setText(chats.get(i).getNumber());


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
        ImageView tvTime;
        RelativeLayout chatLayoutContainer;
        public String datanewlucky;
        private BlockedNumberDao blockedNumberDao;

        ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.tvName);
            tvMsg = itemView.findViewById(R.id.tvLastMsg);
            tvTime = (ImageView) itemView.findViewById(R.id.tvTime);
            chatLayoutContainer = itemView.findViewById(R.id.chat_row_container);


            tvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datanewlucky =chats.get(getAdapterPosition()).getNumber();


                    chats.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), chats.size());


                    blockedNumberDao = BlockedNumberDatabase.getInstance(context).blockedNumberDao();
                    AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
                        List<BlockedNumber> blockedNumberList = blockedNumberDao.getAll();
                        for (int i = 0; i < blockedNumberList.size(); i++) {
                            final BlockedNumber number = blockedNumberList.get(i);
                            if (datanewlucky.equalsIgnoreCase(number.getPhoneNumber())) {
                                blockedNumberDao.delete(number);


                            }
                        }

                    });



                }
            });
        }
    }
}




