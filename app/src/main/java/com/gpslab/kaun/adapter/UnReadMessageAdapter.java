package com.gpslab.kaun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gpslab.kaun.R;
import com.gpslab.kaun.model.GetUnReadMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UnReadMessageAdapter extends RecyclerView.Adapter<UnReadMessageAdapter.ViewHolder> {
    public static Context mcontext;

    public boolean run = false;
    public static List<GetUnReadMessage> productList;
    public static boolean runtimer = true;
    public Context context;
    ViewHolder view;

    public GetUnReadMessage getselfdata;
    String Contest = "";


    ViewHolder viewHolder;

    public UnReadMessageAdapter(Context context, List<GetUnReadMessage> productList) {
        super();
        this.context = context;
        this.productList = productList;
        mcontext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.messagelist, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        getselfdata = productList.get(i);
        view = viewHolder;





        viewHolder.tvmessage.setText(productList.get(i).getBody());


        Date dateObj = new Date(Long.parseLong(productList.get(i).getDates()));
        Date c = Calendar.getInstance().getTime();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   ");
        if(formatter.format(c).equalsIgnoreCase(formatter.format(dateObj))){

            long diff = System.currentTimeMillis()-Long.parseLong(productList.get(i).getDates());
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


        if(productList.get(i).getRead().equalsIgnoreCase("1")){
            viewHolder.tvmessage.setTypeface(Typeface.DEFAULT_BOLD);
            viewHolder.tvdate.setTypeface(Typeface.DEFAULT_BOLD);

        }
        if(productList.get(i).getBody().indexOf("OLA")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ola));
            viewHolder.tvaddress.setText("OLA");
        }else
        if(productList.get(i).getBody().indexOf("ola")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ola));
            viewHolder.tvaddress.setText("OLA");
        }else
        if(productList.get(i).getAddress().equalsIgnoreCase("AD-OLACAB")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ola));
            viewHolder.tvaddress.setText("OLA");
        }else
        if(productList.get(i).getAddress().equalsIgnoreCase("JX-JioPay")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.jio));
            viewHolder.tvaddress.setText("Jio");
        }else
        if(productList.get(i).getAddress().equalsIgnoreCase("JE-JIOINF")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.jio));
            viewHolder.tvaddress.setText("Jio");
        }else
        if(productList.get(i).getBody().indexOf("Jio")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.jio));
            viewHolder.tvaddress.setText("Jio");
        }else
        if(productList.get(i).getBody().indexOf("SBI")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.sbinew));
            viewHolder.tvaddress.setText("SBI");
        }else


        if(productList.get(i).getBody().indexOf("verification code")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.otp));
            viewHolder.tvaddress.setText("OTP");
        }else
        if(productList.get(i).getBody().indexOf("airtel")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.airtel));
            viewHolder.tvaddress.setText("Airtel");
        }else  if(productList.get(i).getAddress().equalsIgnoreCase("QP-CENTBK")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.cbi));
            viewHolder.tvaddress.setText("Central Bank Of India");
        }else  if(productList.get(i).getAddress().equalsIgnoreCase("AD-CENTBK")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.cbi));
            viewHolder.tvaddress.setText("Central Bank Of India");
        }else if(productList.get(i).getAddress().equalsIgnoreCase("AX-CENTBK")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.cbi));
            viewHolder.tvaddress.setText("Central Bank Of India");
        }else if(productList.get(i).getAddress().equalsIgnoreCase("JK-CENTBK")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.cbi));
            viewHolder.tvaddress.setText("Central Bank Of India");
        }else if(productList.get(i).getAddress().equalsIgnoreCase("JM-CENTBK")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.cbi));
            viewHolder.tvaddress.setText("Central Bank Of India");
        }else if(productList.get(i).getBody().indexOf("Amazon")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.amazon));
            viewHolder.tvaddress.setText("Amazon");
        }else if(productList.get(i).getAddress().equalsIgnoreCase("BP-iPaytm")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.paytm));
            viewHolder.tvaddress.setText("Paytm");
        }else  if(productList.get(i).getAddress().equalsIgnoreCase("MD-iPaytm")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.paytm));
            viewHolder.tvaddress.setText("Paytm");
        }else if(productList.get(i).getAddress().equalsIgnoreCase("VM-iPaytm")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.paytm));
            viewHolder.tvaddress.setText("Paytm");
        }else if(productList.get(i).getAddress().equalsIgnoreCase("AE-AIRGNF")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.airtel));
            viewHolder.tvaddress.setText("Airtel");
        }else  if(productList.get(i).getAddress().equalsIgnoreCase("AE-AIRTRF")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.airtel));
            viewHolder.tvaddress.setText("Airtel");
        }else if(productList.get(i).getAddress().equalsIgnoreCase("AE-AIRTEL")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.airtel));
            viewHolder.tvaddress.setText("Airtel");
        }else if(productList.get(i).getBody().indexOf("paytm")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.paytm));
            viewHolder.tvaddress.setText("Paytm");
        }else  if(productList.get(i).getBody().indexOf("Paytm")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.paytm));
            viewHolder.tvaddress.setText("Paytm");
        }else if(productList.get(i).getAddress().equalsIgnoreCase("TM-SAMSNG")){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.samsung));
            viewHolder.tvaddress.setText("Samsung");
        }else if(productList.get(i).getBody().indexOf("Samsung")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.samsung));
            viewHolder.tvaddress.setText("Samsung");
        }else if(productList.get(i).getBody().indexOf("samsung")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.samsung));
            viewHolder.tvaddress.setText("Samsung");
        }else  if(productList.get(i).getBody().indexOf("OTP")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.otp));
            viewHolder.tvaddress.setText("OTP");
        }else if(productList.get(i).getBody().indexOf("otp")>0){
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.otp));
            viewHolder.tvaddress.setText("OTP");
        }else{
            viewHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.download));
            viewHolder.tvaddress.setText(productList.get(i).getAddress());
        }



//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   ");
//
//
//
//        viewHolder.tvdate.setText(productList.get(i).getDates());



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvmessage, tvaddress, tvdate;
        public ImageView ivimg;







        public ViewHolder(View itemView) {
            super(itemView);
            ivimg = (ImageView) itemView.findViewById(R.id.imageiv);
            tvaddress = (TextView) itemView.findViewById(R.id.addresstv);
            tvmessage = (TextView)itemView.findViewById(R.id.messagetv);

            tvdate = (TextView) itemView.findViewById(R.id.datetv);











        }




    }

}


