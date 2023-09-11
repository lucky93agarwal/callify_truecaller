package com.gpslab.kaun.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gpslab.kaun.R;
import com.gpslab.kaun.model.StatusStatus;
import com.gpslab.kaun.status.ViewStatusActivity;
import com.gpslab.kaun.view.DeleteStatusJob;
import com.gpslab.kaun.view.IntentUtils;
import com.gpslab.kaun.view.RealmHelper;
import com.gpslab.kaun.view.Status;
import com.gpslab.kaun.view.StatusCreator;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusTwoAdapter extends RecyclerView.Adapter<StatusTwoAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<StatusStatus> statusArrayList;

    public StatusTwoAdapter(Context context, ArrayList<StatusStatus> status) {
        this.context = context;
        this.statusArrayList = status;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StatusViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.storywhatsapplayout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {

        StatusStatus status = statusArrayList.get(i);

        Glide.with(context)
                .load(status.getImageUrl())
                .apply(new RequestOptions().placeholder(R.drawable.profile))
                .into(statusViewHolder.image);


        Glide.with(context)
                .load(status.getStory_image().get(i))
                .apply(new RequestOptions().placeholder(R.drawable.profile))
                .into(statusViewHolder.ivstory);

        statusViewHolder.tvName.setText(status.getName());
        statusViewHolder.tvTime.setText(status.getTime());

    }

    @Override
    public int getItemCount() {
        if (statusArrayList != null) {
            return statusArrayList.size();
        }
        return 0;
    }

    class StatusViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        ImageView ivstory;
        TextView tvName;
        TextView tvTime;
        CardView mCardView;

        public SharedPreferences sharedPreferences;
        public SharedPreferences.Editor edit;

        StatusViewHolder(@NonNull View itemView) {
            super(itemView);

            ivstory = itemView.findViewById(R.id.story_image);
            image = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);


            mCardView = (CardView) itemView.findViewById(R.id.mcardview);


            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                    edit = sharedPreferences.edit();
                    edit.putString("img_img",statusArrayList.get(getAdapterPosition()).getImageUrl());
                    edit.putString("other_user_name",statusArrayList.get(getAdapterPosition()).getName());
                    edit.apply();
                    if(statusArrayList.get(getAdapterPosition()).getType().equalsIgnoreCase("0")){
                        for (int i = 0; i < statusArrayList.get(getAdapterPosition()).getStory_image().size(); i++) {


                            Status status = new Status();
                            status = StatusCreator.createImageStatusOther(statusArrayList.get(getAdapterPosition()).getStory_image().get(i), statusArrayList.get(getAdapterPosition()).getMobile());
                            status.setContent("");
                            RealmHelper.getInstance().saveStatus(statusArrayList.get(getAdapterPosition()).getMobile(), status);
                            DeleteStatusJob.schedule(status.getUserId(), status.getStatusId());
                        }

                        Intent intent = new Intent(context, ViewStatusActivity.class);

                        intent.putExtra(IntentUtils.UID, statusArrayList.get(getAdapterPosition()).getMobile());
                        context.startActivity(intent);
                    }else {
                        for (int i = 0; i < statusArrayList.get(getAdapterPosition()).getStory_video().size(); i++) {



                            Status status = new Status();
                            status = StatusCreator.createVideoStatusOther(statusArrayList.get(getAdapterPosition()).getStory_video().get(i), statusArrayList.get(getAdapterPosition()).getMobile(),statusArrayList.get(getAdapterPosition()).getDuration());
                            status.setContent("");
                            RealmHelper.getInstance().saveStatus(statusArrayList.get(getAdapterPosition()).getMobile(), status);
                            DeleteStatusJob.schedule(status.getUserId(), status.getStatusId());
                        }

                        Intent intent = new Intent(context, ViewStatusActivity.class);

                        intent.putExtra(IntentUtils.UID, statusArrayList.get(getAdapterPosition()).getMobile());
                        context.startActivity(intent);
                    }

                }
            });

        }
    }
}

