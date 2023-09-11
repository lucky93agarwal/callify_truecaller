package com.gpslab.kaun.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.CallLog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.gpslab.kaun.Details.ContactDetailsActivity;
import com.gpslab.kaun.MainActivity;
import com.gpslab.kaun.Notification.UserProfile;
import com.gpslab.kaun.R;
import com.gpslab.kaun.Utils;
import com.gpslab.kaun.model.MenuItem;
import com.gpslab.kaun.popup.simselectpopup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MisscallRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    public MisscallRecyclerViewAdapter(Context context, List<Object> recyclerViewItems) {
        this.context = context;
        this.recyclerViewItems = recyclerViewItems;
    }

    /**
     * The {@link MenuItemViewHolder} class.
     * Provides a reference to each view in the menu item view.
     */

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private TextView menuItemName;
        private TextView menuNumber;
        private RelativeLayout mRelativeLayout;
        private TextView menuItemPrice;
        private TextView menuItemDate;
        private TextView firstTime,tvtext, tvtiming,tvsim, tvapicheck;
        private ImageView menuItemImage,ivicon,ivsimiv,ivcall;
        private CircleImageView circleImageView;
        private LinearLayout mLinearLayout,mLinearOne,mLinearTwo;
        public simselectpopup defaultpopupnew;
        public SharedPreferences sharedPreferences;

        public Boolean CheckMissCall = false;
        MenuItemViewHolder(View view) {
            super(view);
            defaultpopupnew = new simselectpopup(context);
            sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);

            CheckMissCall = sharedPreferences.getBoolean("miss",false);
            tvapicheck = (TextView)view.findViewById(R.id.text);
            ivcall = (ImageView)view.findViewById(R.id.phoneiv);
            mRelativeLayout = (RelativeLayout)view.findViewById(R.id.recyclerViewone);
//            menuItemImage = (ImageView) view.findViewById(R.id.menu_item_image);
            menuItemName = (TextView) view.findViewById(R.id.textViewName);
            menuItemPrice = (TextView) view.findViewById(R.id.textViewCallDuration);
            menuItemDate = (TextView) view.findViewById(R.id.textViewCallDate);
            tvsim = (TextView)view.findViewById(R.id.simtv);
            menuNumber = (TextView) view.findViewById(R.id.textViewCallNumber);
            firstTime = (TextView) view.findViewById(R.id.firsttexttv);
            circleImageView = (CircleImageView)view.findViewById(R.id.manmanimage);
            tvtiming = (TextView) view.findViewById(R.id.textViewCallDuration);
            tvtext = (TextView)view.findViewById(R.id.textViewCallDatess);
            ivicon = (ImageView) view.findViewById(R.id.imageView);
            ivsimiv = (ImageView) view.findViewById(R.id.simicon);
            mLinearLayout = (LinearLayout) view.findViewById(R.id.llone);
            mLinearOne = (LinearLayout) view.findViewById(R.id.llseven);
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
                        R.layout.list_row, viewGroup, false);
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
                MenuItem menuItem = (MenuItem) recyclerViewItems.get(position);

                if(menuItem.getApi().equalsIgnoreCase("1")){
                    menuItemHolder.tvapicheck.setVisibility(View.VISIBLE);
                }else {
                    menuItemHolder.tvapicheck.setVisibility(View.GONE);
                }
                menuItemHolder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent inten = new Intent(context, ContactDetailsActivity.class);
                        inten.putExtra("name",menuItem.getName());
                        inten.putExtra("number",menuItem.getNumber());
                        inten.putExtra("img",menuItem.getImage());
                        context.startActivity(inten);
                    }
                });
                menuItemHolder.menuItemName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent inten = new Intent(context, ContactDetailsActivity.class);
                        inten.putExtra("name",menuItem.getName());
                        inten.putExtra("number",menuItem.getNumber());
                        inten.putExtra("img",menuItem.getImage());
                        context.startActivity(inten);
                    }
                });
                menuItemHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent inten = new Intent(context, UserProfile.class);
                        inten.putExtra("name",menuItem.getName());
                        inten.putExtra("number",menuItem.getNumber());
                        inten.putExtra("img",menuItem.getImage());
                        context.startActivity(inten);
                    }
                });
                menuItemHolder.mLinearOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent inten = new Intent(context, UserProfile.class);
                        inten.putExtra("name",menuItem.getName());
                        inten.putExtra("number",menuItem.getNumber());
                        inten.putExtra("img",menuItem.getImage());
                        context.startActivity(inten);
                    }
                });

                menuItemHolder.ivcall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuItemHolder.defaultpopupnew.defaultaddpopup(menuItem.getNumber());
                    }
                });
                if(menuItem.getImage().equalsIgnoreCase("NA")){
                    menuItemHolder.mLinearLayout.setVisibility(View.VISIBLE);
                    menuItemHolder.mLinearOne.setVisibility(View.GONE);
                    menuItemHolder.firstTime.setVisibility(View.VISIBLE);
                }else {

                    menuItemHolder.mLinearOne.setVisibility(View.VISIBLE);
                    menuItemHolder.firstTime.setVisibility(View.GONE);

                    Glide.with(context)
                            .load(menuItem.getImage())
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(menuItemHolder.circleImageView);


                }
                if ((position & 1) == 1) {
                    menuItemHolder.mLinearLayout.setBackgroundResource(R.drawable.circledrawable);
                    menuItemHolder.firstTime.setTextColor(Color.parseColor("#55A655"));
                } else
                if ((position & 1) == 0 ) {
                    menuItemHolder.firstTime.setTextColor(Color.parseColor("#4F88AA"));
                    menuItemHolder.mLinearLayout.setBackgroundResource(R.drawable.circledrawnew);
                } else {
                    menuItemHolder.firstTime.setTextColor(Color.parseColor("#ffffff"));
                    menuItemHolder.mLinearLayout.setBackgroundResource(R.drawable.circledrawnewtwo);
                }

                menuItemHolder.menuItemName.setTextColor(Color.parseColor("#5C5C65"));

                if(menuItem.getCallType().equalsIgnoreCase(String.valueOf(CallLog.Calls.REJECTED_TYPE))){
                    menuItemHolder.menuItemName.setText(menuItem.getName());
                    menuItemHolder.menuItemName.setTypeface(null, Typeface.NORMAL);

                    if(TextUtils.isEmpty(menuItem.getName())) {
                        menuItemHolder.menuItemName.setText(menuItem.getNumber());
//                    menuItemHolder.menuItemName.setTypeface(custom);
                        menuItemHolder.firstTime.setText(menuItem.getNumber().substring(0,1));

                    }else{
                        if(menuItem.getName().equalsIgnoreCase("NA")){
                            menuItemHolder.menuItemName.setText(menuItem.getNumber());
                            menuItemHolder.firstTime.setText(menuItem.getNumber().substring(0,1));
                        }else {
                            menuItemHolder.menuItemName.setText(menuItem.getName());
                            menuItemHolder.firstTime.setText(menuItem.getName().substring(0,1));
                        }

//                    menuItemHolder.menuItemName.setTypeface(custom);


                    }
                    menuItemHolder.ivsimiv.setImageResource(R.drawable.simtwonew);

                    menuItemHolder.ivicon.setImageResource(R.drawable.ic_outgoing);
                    DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.tabUnselectedIconColor));
                    menuItemHolder.tvtext.setText("Outgoing call · ");
                    menuItemHolder.tvsim.setText("");
                    menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#908F9D"));
                    menuItemHolder.menuItemDate.setTypeface(null, Typeface.NORMAL);
                    menuItemHolder.tvtext.setTextColor(Color.parseColor("#908F9D"));
                    menuItemHolder.tvtext.setTypeface(null, Typeface.NORMAL);

                }else
                if(menuItem.getCallType().equalsIgnoreCase(String.valueOf(CallLog.Calls.OUTGOING_TYPE))){
                    menuItemHolder.menuItemName.setText(menuItem.getName());
                    menuItemHolder.menuItemName.setTypeface(null, Typeface.NORMAL);

                    if(TextUtils.isEmpty(menuItem.getName())) {
                        menuItemHolder.menuItemName.setText(menuItem.getNumber());
//                    menuItemHolder.menuItemName.setTypeface(custom);
                        menuItemHolder.firstTime.setText(menuItem.getNumber().substring(0,1));

                    }else{
                        if(menuItem.getName().equalsIgnoreCase("NA")){
                            menuItemHolder.menuItemName.setText(menuItem.getNumber());
                            menuItemHolder.firstTime.setText(menuItem.getNumber().substring(0,1));
                        }else {
                            menuItemHolder.menuItemName.setText(menuItem.getName());
                            menuItemHolder.firstTime.setText(menuItem.getName().substring(0,1));
                        }

//                    menuItemHolder.menuItemName.setTypeface(custom);


                    }
                    menuItemHolder.ivsimiv.setImageResource(R.drawable.simtwonew);

                    menuItemHolder.ivicon.setImageResource(R.drawable.ic_outgoing);
                    DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.tabUnselectedIconColor));
                    menuItemHolder.tvtext.setText("Outgoing call · ");
                    menuItemHolder.tvsim.setText("");
                    menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#908F9D"));
                    menuItemHolder.menuItemDate.setTypeface(null, Typeface.NORMAL);
                    menuItemHolder.tvtext.setTextColor(Color.parseColor("#908F9D"));
                    menuItemHolder.tvtext.setTypeface(null, Typeface.NORMAL);

                }else if(menuItem.getCallType().equalsIgnoreCase(String.valueOf(CallLog.Calls.INCOMING_TYPE))){
                    menuItemHolder.menuItemName.setText(menuItem.getName());

                    menuItemHolder.menuItemName.setTypeface(null, Typeface.NORMAL);

                    if(TextUtils.isEmpty(menuItem.getName())) {
                        menuItemHolder.menuItemName.setText(menuItem.getNumber());
//                    menuItemHolder.menuItemName.setTypeface(custom);
                        menuItemHolder.firstTime.setText(menuItem.getNumber().substring(0,1));

                    }else{
                        if(menuItem.getName().equalsIgnoreCase("NA")){
                            menuItemHolder.menuItemName.setText(menuItem.getNumber());
                            menuItemHolder.firstTime.setText(menuItem.getNumber().substring(0,1));
                        }else {
                            menuItemHolder.menuItemName.setText(menuItem.getName());
                            menuItemHolder.firstTime.setText(menuItem.getName().substring(0,1));
                        }
//                        menuItemHolder.menuItemName.setText(menuItem.getName());
////                    menuItemHolder.menuItemName.setTypeface(custom);
//                        menuItemHolder.firstTime.setText(menuItem.getName().substring(0,1));

                    }
                    menuItemHolder.ivsimiv.setImageResource(R.drawable.simonenew);

                    menuItemHolder.ivicon.setImageResource(R.drawable.ic_missed);
                    DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.tabUnselectedIconColor));
                    menuItemHolder.tvtiming.setText(Utils.formatSeconds(menuItem.getDuration()));
                    menuItemHolder.tvtext.setText("Incoming call · ");
                    menuItemHolder.tvtext.setTextColor(Color.parseColor("#908F9D"));
                    menuItemHolder.tvtext.setTypeface(null, Typeface.NORMAL);
                    menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#908F9D"));
                    menuItemHolder.menuItemDate.setTypeface(null, Typeface.NORMAL);
                    menuItemHolder.tvsim.setText(" ");

                }else if(menuItem.getCallType().equalsIgnoreCase(String.valueOf(CallLog.Calls.MISSED_TYPE))){
                    menuItemHolder.menuItemName.setText(menuItem.getName());

                    if(menuItemHolder.CheckMissCall){
                        menuItemHolder.ivsimiv.setImageResource(R.drawable.simonenew);
                        menuItemHolder.ivicon.setImageResource(R.drawable.ic_missed);
                        DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.tabUnselectedIconColor));


                        menuItemHolder.tvtext.setText("Missed call · ");
                        menuItemHolder.tvtext.setTextColor(Color.parseColor("#908F9D"));
                        menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#908F9D"));

                    }else {
                        menuItemHolder.ivsimiv.setImageResource(R.drawable.simmisone);
                        menuItemHolder.ivicon.setImageResource(R.drawable.misscallednew);
                        DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.red));

                        menuItemHolder.tvtext.setText("Missed call · ");
                        menuItemHolder.tvtext.setTextColor(Color.parseColor("#ec0b0b"));
                        menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#ec0b0b"));
                        menuItemHolder.tvtext.setTypeface(null, Typeface.BOLD);
                        menuItemHolder.menuItemDate.setTypeface(null, Typeface.BOLD);
                    }


                    menuItemHolder.menuItemName.setTypeface(null, Typeface.BOLD);

                    if(TextUtils.isEmpty(menuItem.getName())) {

                        menuItemHolder.menuItemName.setText(menuItem.getNumber());

                        menuItemHolder.firstTime.setText(menuItem.getNumber().substring(0,1));

                    }else{
                        if(menuItem.getName().equalsIgnoreCase("NA")){

                            menuItemHolder.menuItemName.setText(menuItem.getNumber());
                            menuItemHolder.firstTime.setText(menuItem.getNumber().substring(0,1));
                        }else {
                            menuItemHolder.menuItemName.setText(menuItem.getName());
                            menuItemHolder.firstTime.setText(menuItem.getName().substring(0,1));
                        }
//                        menuItemHolder.menuItemName.setText(menuItem.getName());
////                    menuItemHolder.menuItemName.setTypeface(custom);
//                        menuItemHolder.firstTime.setText(menuItem.getName().substring(0,1));

                    }





                }

                menuItemHolder.menuItemDate.setText(menuItem.getDate());

//                Date dateObj = new Date(menuItem.getDate());
//                Date c = Calendar.getInstance().getTime();
//
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.DATE, -1);
//
//
//                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   ");
//
//
//                //               Log.d("sjdljsldjlssdfjdl","size 7 Current Date = "+menuItem.getDate()+"  postion == == "+position+" current time == "+System.currentTimeMillis());
//                if(formatter.format(c).equalsIgnoreCase(formatter.format(dateObj))){
//
//                    long diff = System.currentTimeMillis()-menuItem.getDate();
//                    long hourss = TimeUnit.MILLISECONDS.toHours(diff);
////                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
////                    long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
//
//
//                    long minutes = (diff / 1000)  % 60;
//                    int seconds = (int)((diff / 1000) % 60);
//                    Log.d("sjdljsldjlssdfjdl","size 7 Current Date = "+hourss+"hr "+minutes+"min "+seconds+" Sec");
//
//
//                    if(hourss >0){
//                        if(hourss < 5){
//                            menuItemHolder.menuItemDate.setText(hourss +"hr ago");
//                        }else {
//                            menuItemHolder.menuItemDate.setText("Today");
//                        }
//
//                    }else {
//
//                        menuItemHolder.menuItemDate.setText(minutes+"min ago");
//                    }
//
//                }else if(formatter.format(cal.getTime()).equalsIgnoreCase(formatter.format(dateObj))){
//                    menuItemHolder.menuItemDate.setText("Yesterday");
//                }else {
//                    menuItemHolder.menuItemDate.setText(String.valueOf(formatter.format(dateObj)));
//                }


                menuItemHolder.menuItemPrice.setText(Utils.formatSeconds(menuItem.getDuration()));

                menuItemHolder.menuNumber.setText("");
                // Get the menu item image resource ID.
//                String imageName = menuItem.getName();
//                int imageResID = context.getResources().getIdentifier(imageName, "drawable",
//                        context.getPackageName());
//
//                // Add the menu item details to the menu item view.
//                menuItemHolder.menuItemImage.setImageResource(imageResID);

//                menuItemHolder.menuItemName.setTypeface(null, Typeface.BOLD);
//
//                if ((position & 1) == 0 ) {
//                    menuItemHolder.mLinearLayout.setBackgroundResource(R.drawable.circledrawnew);
//                } else {
//                    menuItemHolder.mLinearLayout.setBackgroundResource(R.drawable.circledrawnewtwo);
//                }
//                menuItemHolder.menuItemName.setTextColor(Color.parseColor("#000000"));
//
//                if(menuItem.getCallType().equalsIgnoreCase(String.valueOf(CallLog.Calls.OUTGOING_TYPE))){
//                    DrawableCompat.setTint(menuItemHolder.ivsimiv.getDrawable(), ContextCompat.getColor(context, R.color.tabUnselectedIconColor));
//                    menuItemHolder.ivicon.setImageResource(R.drawable.ic_outgoing);
//                    DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.tabUnselectedIconColor));
//                    menuItemHolder.tvtext.setText("Outgoing call · ");
//                    menuItemHolder.tvsim.setText("  2");
//                }else if(menuItem.getCallType().equalsIgnoreCase(String.valueOf(CallLog.Calls.INCOMING_TYPE))){
//                    DrawableCompat.setTint(menuItemHolder.ivsimiv.getDrawable(), ContextCompat.getColor(context, R.color.tabUnselectedIconColor));
//                    menuItemHolder.ivicon.setImageResource(R.drawable.ic_missed);
//                    DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.tabUnselectedIconColor));
//                    menuItemHolder.tvtiming.setText(Utils.formatSeconds(menuItem.getDuration()));
//                    menuItemHolder.tvtext.setText("Incoming call · ");
//                    menuItemHolder.tvsim.setText("  1");
//                }else if(menuItem.getCallType().equalsIgnoreCase(String.valueOf(CallLog.Calls.MISSED_TYPE))){
//
//
//                    menuItemHolder.menuItemName.setText(menuItem.getName());
//
//                    menuItemHolder.ivicon.setImageResource(R.drawable.misscallednew);
//                    DrawableCompat.setTint(menuItemHolder.ivsimiv.getDrawable(), ContextCompat.getColor(context, R.color.red));
//                    menuItemHolder.tvtext.setText("Missed call · ");
//                    menuItemHolder.tvtext.setTextColor(Color.parseColor("#FF0000"));
//                    menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#FF0000"));
//                    menuItemHolder.tvtext.setTypeface(null, Typeface.BOLD);
//                    menuItemHolder.menuItemDate.setTypeface(null, Typeface.BOLD);
//                    menuItemHolder.tvsim.setText("  1"AAAAAAAAAAAAAAAAAAZZZZZZZZZZZZZZZZZZZZZZZ);
//                }
//
//
//
//
//
//
//
//
//                Date dateObj = new Date(menuItem.getDate());
//                Date c = Calendar.getInstance().getTime();
//
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.DATE, -1);
//
//
//                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   ");
//
//
//                //               Log.d("sjdljsldjlssdfjdl","size 7 Current Date = "+menuItem.getDate()+"  postion == == "+position+" current time == "+System.currentTimeMillis());
//                if(formatter.format(c).equalsIgnoreCase(formatter.format(dateObj))){
//
//                    long diff = System.currentTimeMillis()-menuItem.getDate();
//                    long hourss = TimeUnit.MILLISECONDS.toHours(diff);
////                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
////                    long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
//
//
//                    long minutes = (diff / 1000)  % 60;
//                    int seconds = (int)((diff / 1000) % 60);
//                    Log.d("sjdljsldjlssdfjdl","size 7 Current Date = "+hourss+"hr "+minutes+"min "+seconds+" Sec");
//
//
//                    if(hourss >0){
//                        if(hourss < 5){
//                            menuItemHolder.menuItemDate.setText(hourss +"hr ago");
//                        }else {
//                            menuItemHolder.menuItemDate.setText("Today");
//                        }
//
//                    }else {
//
//                        menuItemHolder.menuItemDate.setText(minutes+"min ago");
//                    }
//
//                }else if(formatter.format(cal.getTime()).equalsIgnoreCase(formatter.format(dateObj))){
//                    menuItemHolder.menuItemDate.setText("Yesterday");
//                }else {
//                    menuItemHolder.menuItemDate.setText(String.valueOf(formatter.format(dateObj)));
//                }
//                menuItemHolder.menuItemName.setText(menuItem.getName());
//                menuItemHolder.menuItemPrice.setText(Utils.formatSeconds(menuItem.getDuration()));
//
//                menuItemHolder.menuNumber.setText("");
//
//                Typeface custom = Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-SemiBold.ttf");
//                if(TextUtils.isEmpty(menuItem.getName())) {
//                    menuItemHolder.menuItemName.setText(menuItem.getNumber());
//                    menuItemHolder.menuItemName.setTypeface(custom);
//                    menuItemHolder.firstTime.setText(menuItem.getNumber().substring(0,1));
//
//                }else{
//                    menuItemHolder.menuItemName.setText(menuItem.getName());
//                    menuItemHolder.menuItemName.setTypeface(custom);
//                    menuItemHolder.firstTime.setText(menuItem.getName().substring(0,1));
//
//                }
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


