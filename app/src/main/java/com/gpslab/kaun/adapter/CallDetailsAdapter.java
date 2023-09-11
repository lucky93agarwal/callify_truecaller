package com.gpslab.kaun.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gpslab.kaun.R;
import com.gpslab.kaun.model.GetContectData;
import com.gpslab.kaun.popup.simselectpopup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CallDetailsAdapter extends RecyclerView.Adapter<CallDetailsAdapter.ViewHolder> {
    public static Context mcontext;

    public boolean run = false;
    public static List<GetContectData> productList;
    public static boolean runtimer = true;
    public Context context;
    ViewHolder view;

    public GetContectData getselfdata;
    String Contest = "";


    ViewHolder viewHolder;

    public CallDetailsAdapter(Context context, List<GetContectData> productlist) {
        super();
        this.context = context;
        this.productList = productlist;
        mcontext = context;
    }

    public void filterList(ArrayList<GetContectData> filteredList) {
        productList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.calldetailslayoutnew, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        view = viewHolder;



       viewHolder.tvdate.setText("TUESDAY 15 DEC 2020");

        Date dateObj = new Date(Long.parseLong(productList.get(i).getDate()));
        Date c = Calendar.getInstance().getTime();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);


        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   ");


        SimpleDateFormat formatter_new = new SimpleDateFormat("hh:mm aa");


        //               Log.d("sjdljsldjlssdfjdl","size 7 Current Date = "+menuItem.getDate()+"  postion == == "+position+" current time == "+System.currentTimeMillis());

        if(formatter.format(c).equalsIgnoreCase(formatter.format(dateObj))){

            long diff = System.currentTimeMillis()-Long.parseLong(productList.get(i).getDate());
            long hourss = TimeUnit.MILLISECONDS.toHours(diff);
//                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
//                    long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);


            long minutes = (diff / 1000)  % 60;
            int seconds = (int)((diff / 1000) % 60);
            Log.d("sjdljsldjlssdfjdl","size 7 Current Date = "+hourss+"hr "+minutes+"min "+seconds+" Sec");


            if(hourss >0){
                if(hourss < 5){
                    viewHolder.tvdate.setText(hourss +"hr ago");
                }else {
                    viewHolder.tvdate.setText("Today");
                }

            }else {

                viewHolder.tvdate.setText(minutes+"min ago");
            }

        }else if(formatter.format(cal.getTime()).equalsIgnoreCase(formatter.format(dateObj))){
            viewHolder.tvdate.setText("Yesterday");
        }else {
            viewHolder.tvdate.setText(String.valueOf(formatter.format(dateObj)));
        }


        if(String.valueOf(productList.get(i).count).equalsIgnoreCase("1")){

            viewHolder.tvcount.setText(" Outgoing call "+String.valueOf(formatter_new.format(dateObj)));
        }else {
            viewHolder.tvcount.setText(String.valueOf(productList.get(i).count)+" Call ");
        }
    }

    @Override
    public int getItemCount() {
//        if(productList.size()>3){
//            return 3;
//        }else {
            return productList.size();
//        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvdate,tvcount;
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
        public ImageView ivclosetv,ivcaller;

        LinearLayout mlinearLayout,mmlinear;
        //        private FirebaseFirestore firebaseFirestore;
//        DatabaseReference rootRef;
        public AlertDialog alertDialog;


        private static simselectpopup defaultpopupnew;


        public ViewHolder(View itemView) {
            super(itemView);

            defaultpopupnew = new simselectpopup(mcontext);








            ivcaller = (ImageView) itemView.findViewById(R.id.calliv);


            tvdate = (TextView) itemView.findViewById(R.id.detailstv);
            tvcount = (TextView) itemView.findViewById(R.id.datesstv);



            ivcaller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    defaultpopupnew.defaultaddpopup(productList.get(getAdapterPosition()).getNumber());


                }
            });










        }




    }


}


