package com.gpslab.kaun.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gpslab.kaun.Profile.ProfileDialogActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.calling.CallingActivity;
import com.gpslab.kaun.digitaloceanspaces.RandomString;
import com.gpslab.kaun.model.Chat;
import com.gpslab.kaun.view.CallType;
import com.gpslab.kaun.view.ChatActivity;
import com.gpslab.kaun.view.FireCallDirection;
import com.gpslab.kaun.view.IntentUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapterNew extends RecyclerView.Adapter<ChatsAdapterNew.ChatsViewHolder> {

    private Context context;
    private ArrayList<Chat> chats;

    public ChatsAdapterNew(Context context, ArrayList<Chat> chats) {
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
        return new ChatsViewHolder(LayoutInflater.from(context).inflate(R.layout.callingrecyclerview, viewGroup, false));
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
                .apply(new RequestOptions().placeholder(R.drawable.profile))
                .into(chatsViewHolder.profilePic);

        chatsViewHolder.tvName.setText(chat.getName());


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
        ImageView ivphone, ivvideo;

        ConstraintLayout chatLayoutContainer;

        ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.tv_username);
            ivphone = (ImageView)itemView.findViewById(R.id.btn_call);
            ivvideo = (ImageView)itemView.findViewById(R.id.btn_video_call);

            chatLayoutContainer = itemView.findViewById(R.id.row_status_container);


            ivvideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setMessage(R.string.video_call_confirmation)

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Long tsLong = System.currentTimeMillis() / 1000;
                                    String ts = tsLong.toString();
                                    RandomString randomString = new RandomString();
                                    String image_name = ts + "_" + randomString.nextString();
                                    // Continue with delete operation
                                    Intent intent = new Intent(context, CallingActivity.class);
                                    intent.putExtra(IntentUtils.CALL_TYPE, 2);
                                    intent.putExtra(IntentUtils.CALL_DIRECTION, FireCallDirection.OUTGOING);
                                    intent.putExtra(IntentUtils.UID,chats.get(getAdapterPosition()).getId());
                                    intent.putExtra(IntentUtils.CALL_ID,image_name);
                                    intent.putExtra(IntentUtils.CALL_ACTION_TYPE,IntentUtils.ACTION_START_NEW_CALL);
                                    intent.putExtra(IntentUtils.PHONE,chats.get(getAdapterPosition()).getId());
                                    context.startActivity(intent);


                                    Log.d("UpdateUIUpdate","Check  Video Calling Type ==       "+CallType.VIDEO);
                                }
                            })


                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            ivphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setMessage(R.string.voice_call_confirmation)

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Long tsLong = System.currentTimeMillis() / 1000;
                                    String ts = tsLong.toString();
                                    RandomString randomString = new RandomString();
                                    String image_name = ts + "_" + randomString.nextString();

                                    Intent intent = new Intent(context, CallingActivity.class);
                                    intent.putExtra(IntentUtils.CALL_TYPE, 1);
                                    intent.putExtra(IntentUtils.CALL_DIRECTION, FireCallDirection.OUTGOING);
                                    intent.putExtra(IntentUtils.UID,chats.get(getAdapterPosition()).getId());
                                    intent.putExtra(IntentUtils.CALL_ID,image_name);
                                    intent.putExtra(IntentUtils.CALL_ACTION_TYPE,IntentUtils.ACTION_START_NEW_CALL);
                                    intent.putExtra(IntentUtils.PHONE,chats.get(getAdapterPosition()).getId());
                                    context.startActivity(intent);
                                    // Continue with delete operation

                                    Log.d("UpdateUIUpdate","Check  audio Calling Type ==       "+CallType.VOICE);
                                }
                            })


                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

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


        }
    }

}


