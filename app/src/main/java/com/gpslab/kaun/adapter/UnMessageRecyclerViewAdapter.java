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

import com.google.android.gms.ads.AdView;
import com.gpslab.kaun.MainActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.model.GetMessage;
import com.gpslab.kaun.model.GetUnMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UnMessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    // The banner ad view type.
    private static final int BANNER_AD_VIEW_TYPE = 1;

    // An Activity's Context.
    private final Context context;

    // The list of banner ads and menu items.
    private List<Object> recyclerViewItems;

    /**
     * For this example app, the recyclerViewItems list contains only
     * {@link } and {@link AdView} types.
     */

    public UnMessageRecyclerViewAdapter(Context context, List<Object> recyclerViewItems) {
        this.context = context;
        this.recyclerViewItems = recyclerViewItems;
    }

    /**
     * The {@link MenuItemViewHolder} class.
     * Provides a reference to each view in the menu item view.
     */

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {



        public TextView tvmessage, tvaddress, tvdate;
        public ImageView ivimg;

        MenuItemViewHolder(View view) {
            super(view);


            ivimg = (ImageView) view.findViewById(R.id.imageiv);
            tvaddress = (TextView) view.findViewById(R.id.addresstv);
            tvmessage = (TextView)view.findViewById(R.id.messagetv);

            tvdate = (TextView) view.findViewById(R.id.datetv);


        }
    }
//    public void filterList(List<Object>  filteredList) {
//        recyclerViewItems = filteredList;
//        notifyDataSetChanged();
//    }

    /**
     * The {@link AdViewHolder} class.
     */
    public class AdViewHolder extends RecyclerView.ViewHolder {

        AdViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

    /**
     * Determines the view type for the given position.
     */
    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return MENU_ITEM_VIEW_TYPE;
        }else if(position == 1){
            return MENU_ITEM_VIEW_TYPE;
        }else if (position == 2){
            return MENU_ITEM_VIEW_TYPE;
        }else if (position == 3){
            return BANNER_AD_VIEW_TYPE;
        }else  {
            return (position % MainActivity.ITEMS_PER_AD == 3) ? BANNER_AD_VIEW_TYPE : MENU_ITEM_VIEW_TYPE;
        }
    }

    /**
     * Creates a new view for a menu item view or a banner ad view
     * based on the viewType. This method is invoked by the layout manager.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                View menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.messagelist, viewGroup, false);
                return new MenuItemViewHolder(menuItemLayoutView);
            case BANNER_AD_VIEW_TYPE:
                // fall through
            default:
                View bannerLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.banner_ad_container,
                        viewGroup, false);
                return new AdViewHolder(bannerLayoutView);
        }
    }

    /**
     * Replaces the content in the views that make up the menu item view and the
     * banner ad view. This method is invoked by the layout manager.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                MenuItemViewHolder menuItemHolder = (MenuItemViewHolder) holder;
                GetUnMessage menuItem = (GetUnMessage) recyclerViewItems.get(position);



                menuItemHolder.tvmessage.setText(menuItem.getBody());


                Date dateObj = new Date(Long.parseLong(menuItem.getDates()));
                Date c = Calendar.getInstance().getTime();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   ");
                if(formatter.format(c).equalsIgnoreCase(formatter.format(dateObj))){

                    long diff = System.currentTimeMillis()-Long.parseLong(menuItem.getDates());
                    long hourss = TimeUnit.MILLISECONDS.toHours(diff);
//                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
//                    long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);


                    long minutes = (diff / 1000)  % 60;
                    int seconds = (int)((diff / 1000) % 60);
                    Log.d("sjdljsldjlssdfjdl","size 7 Current Date = "+hourss+"hr "+minutes+"min "+seconds+" Sec");


                    if(hourss >0){
                        if(hourss < 5){
                            menuItemHolder.tvdate.setText(hourss +"hr ago");
                        }else {
                            menuItemHolder.tvdate.setText("Today");
                        }

                    }else {

                        menuItemHolder.tvdate.setText(minutes+"min ago");
                    }

                }else if(formatter.format(cal.getTime()).equalsIgnoreCase(formatter.format(dateObj))){
                    menuItemHolder.tvdate.setText("Yesterday");
                }else {
                    menuItemHolder.tvdate.setText(String.valueOf(formatter.format(dateObj)));
                }


                if(menuItem.getRead().equalsIgnoreCase("0")){
                    menuItemHolder.tvmessage.setTypeface(Typeface.DEFAULT_BOLD);
                    menuItemHolder.tvdate.setTypeface(Typeface.DEFAULT_BOLD);

                }
                if(menuItem.getBody().indexOf("OLA")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ola));
                    menuItemHolder.tvaddress.setText("OLA");
                }else
                if(menuItem.getBody().indexOf("ola")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ola));
                    menuItemHolder.tvaddress.setText("OLA");
                }else
                if(menuItem.getAddress().equalsIgnoreCase("AD-OLACAB")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ola));
                    menuItemHolder.tvaddress.setText("OLA");
                }else
                if(menuItem.getAddress().equalsIgnoreCase("JX-JioPay")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.jio));
                    menuItemHolder.tvaddress.setText("Jio");
                }else
                if(menuItem.getAddress().equalsIgnoreCase("JE-JIOINF")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.jio));
                    menuItemHolder.tvaddress.setText("Jio");
                }else
                if(menuItem.getBody().indexOf("Jio")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.jio));
                    menuItemHolder.tvaddress.setText("Jio");
                }else
                if(menuItem.getBody().indexOf("SBI")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sbinew));
                    menuItemHolder.tvaddress.setText("SBI");
                }else


                if(menuItem.getBody().indexOf("verification code")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.otp));
                    menuItemHolder.tvaddress.setText("OTP");
                }else
                if(menuItem.getBody().indexOf("airtel")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.airtel));
                    menuItemHolder.tvaddress.setText("Airtel");
                }else  if(menuItem.getAddress().equalsIgnoreCase("QP-CENTBK")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cbi));
                    menuItemHolder.tvaddress.setText("Central Bank Of India");
                }else  if(menuItem.getAddress().equalsIgnoreCase("AD-CENTBK")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cbi));
                    menuItemHolder.tvaddress.setText("Central Bank Of India");
                }else if(menuItem.getAddress().equalsIgnoreCase("AX-CENTBK")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cbi));
                    menuItemHolder.tvaddress.setText("Central Bank Of India");
                }else if(menuItem.getAddress().equalsIgnoreCase("JK-CENTBK")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cbi));
                    menuItemHolder.tvaddress.setText("Central Bank Of India");
                }else if(menuItem.getAddress().equalsIgnoreCase("JM-CENTBK")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cbi));
                    menuItemHolder.tvaddress.setText("Central Bank Of India");
                }else if(menuItem.getBody().indexOf("Amazon")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.amazon));
                    menuItemHolder.tvaddress.setText("Amazon");
                }else if(menuItem.getAddress().equalsIgnoreCase("BP-iPaytm")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.paytm));
                    menuItemHolder.tvaddress.setText("Paytm");
                }else  if(menuItem.getAddress().equalsIgnoreCase("MD-iPaytm")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.paytm));
                    menuItemHolder.tvaddress.setText("Paytm");
                }else if(menuItem.getAddress().equalsIgnoreCase("VM-iPaytm")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.paytm));
                    menuItemHolder.tvaddress.setText("Paytm");
                }else if(menuItem.getAddress().equalsIgnoreCase("AE-AIRGNF")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.airtel));
                    menuItemHolder.tvaddress.setText("Airtel");
                }else  if(menuItem.getAddress().equalsIgnoreCase("AE-AIRTRF")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.airtel));
                    menuItemHolder.tvaddress.setText("Airtel");
                }else if(menuItem.getAddress().equalsIgnoreCase("AE-AIRTEL")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.airtel));
                    menuItemHolder.tvaddress.setText("Airtel");
                }else if(menuItem.getBody().indexOf("paytm")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.paytm));
                    menuItemHolder.tvaddress.setText("Paytm");
                }else  if(menuItem.getBody().indexOf("Paytm")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.paytm));
                    menuItemHolder.tvaddress.setText("Paytm");
                }else if(menuItem.getAddress().equalsIgnoreCase("TM-SAMSNG")){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.samsung));
                    menuItemHolder.tvaddress.setText("Samsung");
                }else if(menuItem.getBody().indexOf("Samsung")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.samsung));
                    menuItemHolder.tvaddress.setText("Samsung");
                }else if(menuItem.getBody().indexOf("samsung")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.samsung));
                    menuItemHolder.tvaddress.setText("Samsung");
                }else  if(menuItem.getBody().indexOf("OTP")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.otp));
                    menuItemHolder.tvaddress.setText("OTP");
                }else if(menuItem.getBody().indexOf("otp")>0){
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.otp));
                    menuItemHolder.tvaddress.setText("OTP");
                }else{
                    menuItemHolder.ivimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.download));
                    menuItemHolder.tvaddress.setText(menuItem.getAddress());
                }
                break;
            case BANNER_AD_VIEW_TYPE:
                // fall through
            default:
                AdViewHolder bannerHolder = (AdViewHolder) holder;

                AdView adView = (AdView) recyclerViewItems.get(position);
                ViewGroup adCardView = (ViewGroup) bannerHolder.itemView;

                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                // Add the banner ad to the ad view.
                adCardView.addView(adView);
        }
    }

}



