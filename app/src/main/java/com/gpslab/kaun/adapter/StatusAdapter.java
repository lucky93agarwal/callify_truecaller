package com.gpslab.kaun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gpslab.kaun.R;
import com.gpslab.kaun.model.StatusStatus;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder>{
    private Context context;
    private ArrayList<StatusStatus> statusArrayList;

    public StatusAdapter(Context context, ArrayList<StatusStatus> status) {
        this.context = context;
        this.statusArrayList = status;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StatusViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.status_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {

        StatusStatus status = statusArrayList.get(i);

        Glide.with(context)
                .load(status.getImageUrl())
                .apply(new RequestOptions().placeholder(R.drawable.profile))
                .into(statusViewHolder.image);

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
        TextView tvName;
        TextView tvTime;

        StatusViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);

        }
    }
}
