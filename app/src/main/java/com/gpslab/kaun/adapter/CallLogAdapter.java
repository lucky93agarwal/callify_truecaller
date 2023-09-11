package com.gpslab.kaun.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gpslab.kaun.R;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gpslab.kaun.model.CallLogtwoinfoSearch;
import com.gpslab.kaun.model.CallLogtwoinfor;
import com.gpslab.kaun.Utils;
import com.gpslab.kaun.model.GetContectData;
import com.gpslab.kaun.popup.simselectpopup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {
    public static Context mcontext;

    public boolean run = false;
    public static List<CallLogtwoinfoSearch> productList;
    public static boolean runtimer = true;
    public Context context;
    ViewHolder view;

    public CallLogtwoinfoSearch getselfdata;
    String Contest = "";


    ViewHolder viewHolder;

    public CallLogAdapter(Context context, List<CallLogtwoinfoSearch> productList) {
        super();
        this.context = context;
        this.productList = productList;
        mcontext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        getselfdata = productList.get(i);
        view = viewHolder;


        viewHolder.mimagesdfView.setVisibility(View.GONE);

        Typeface custom = Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-SemiBold.ttf");

        if(TextUtils.isEmpty(productList.get(i).getName())) {
            viewHolder.tvname.setText(productList.get(i).getNumber());
            viewHolder.tvname.setTypeface(custom);
            viewHolder.tvfirstlater.setText(productList.get(i).getNumber().substring(0,1));

        }else{
            viewHolder.tvname.setText(productList.get(i).getName());
            viewHolder.tvname.setTypeface(custom);
            viewHolder.tvfirstlater.setText(productList.get(i).getName().substring(0,1));

        }
        viewHolder.mProgressbar.setVisibility(View.GONE);
        viewHolder.tvtext.setText(productList.get(i).getDate());
        viewHolder.mcircular.setVisibility(View.GONE);


        viewHolder.tvnumber.setText(productList.get(i).getNumber());


    }
    public void filterList(ArrayList<CallLogtwoinfoSearch> filteredList) {
        productList = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(productList.size()>3){
            return 3;
        }else {
            return productList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvname, tvnumber, tvdate, tvtiming, tvfirstlater,tvtext;



        public CircleImageView mcircular;
        public ProgressBar mProgressbar;
        public ImageView ivicon,ivuser,ivphone;


        public TextView tvdatesss;


        public RelativeLayout mimagesdfView;


        private final static String simSlotName[] = {
                "extra_asus_dial_use_dualsim",
                "com.android.phone.extra.slot",
                "slot",
                "simslot",
                "sim_slot",
                "subscription",
                "Subscription",
                "phone",
                "com.android.phone.DialingMode",
                "simSlot",
                "slot_id",
                "simId",
                "simnum",
                "phone_type",
                "slotId",
                "slotIdx"
        };

        private static simselectpopup defaultpopupnew;
        public ViewHolder(View itemView) {
            super(itemView);
            mimagesdfView = (RelativeLayout)itemView.findViewById(R.id.imagesdfView);

            mcircular = (CircleImageView)itemView.findViewById(R.id.manmanimage);
            mProgressbar = (ProgressBar)itemView.findViewById(R.id.progressBar);

            tvfirstlater = (TextView) itemView.findViewById(R.id.firsttexttv);
            tvtext = (TextView)itemView.findViewById(R.id.textViewCallDatess);
            defaultpopupnew = new simselectpopup(mcontext);

            ivphone = (ImageView)itemView.findViewById(R.id.phoneiv);



            tvname = (TextView) itemView.findViewById(R.id.textViewName);
            tvnumber = (TextView) itemView.findViewById(R.id.textViewCallNumber);
            tvdate = (TextView) itemView.findViewById(R.id.textViewCallDate);
            tvtiming = (TextView) itemView.findViewById(R.id.textViewCallDuration);


            ivicon = (ImageView) itemView.findViewById(R.id.imageView);
            ivuser = (ImageView) itemView.findViewById(R.id.imageViewProfile);




            ivphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    defaultpopupnew.defaultaddpopup(productList.get(getAdapterPosition()).getNumber());

                }
            });





        }




    }

}

