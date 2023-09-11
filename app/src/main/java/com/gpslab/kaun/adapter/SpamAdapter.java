package com.gpslab.kaun.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.provider.CallLog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.Details.ContactDetailsActivity;
import com.gpslab.kaun.Home.GetCallLogTable;
import com.gpslab.kaun.MainActivity;
import com.gpslab.kaun.Notification.SpamUserDetialActivity;
import com.gpslab.kaun.Notification.UserProfile;
import com.gpslab.kaun.R;
import com.gpslab.kaun.Utils;
import com.gpslab.kaun.model.SpamMenuItem;
import com.gpslab.kaun.mssagedetail.MessageDetailsActivity;
import com.gpslab.kaun.popup.simselectpopup;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    public SpamAdapter(Context context, List<Object> recyclerViewItems) {
        this.context = context;
        this.recyclerViewItems = recyclerViewItems;
    }

    /**
     * The {@link MenuItemViewHolder} class.
     * Provides a reference to each view in the menu item view.
     */
    int Ri,G,B;
    private int getRandomColor() {
        Random rnd = new Random();

        Ri=rnd.nextInt(156);
        G=rnd.nextInt(156);
        B=rnd.nextInt(156);
        return Color.argb(30, Ri, G, B);
    }
    private int getRandomColorText() {


        return Color.argb(150, Ri, G, B);
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private TextView menuItemName;
        private TextView menuNumber;
        private RelativeLayout mRelativeLayout;
        private TextView menuItemPrice;
        private TextView menuItemDate;
        private TextView firstTime,tvtext, tvtiming,tvsim, tvapicheck;
        private ImageView menuItemImage,ivicon,ivsimiv,ivcall;
        private ImageView circleImageView;
        private LinearLayout mLinearLayout,mLinearOne,mLinearTwo;
        public simselectpopup defaultpopupnew;
        public SharedPreferences sharedPreferences;
        public ProgressBar progressBar;

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
            progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
            menuItemPrice = (TextView) view.findViewById(R.id.textViewCallDuration);
            menuItemDate = (TextView) view.findViewById(R.id.textViewCallDate);
            tvsim = (TextView)view.findViewById(R.id.simtv);
            menuNumber = (TextView) view.findViewById(R.id.textViewCallNumber);
            firstTime = (TextView) view.findViewById(R.id.firsttexttv);
            circleImageView = (ImageView)view.findViewById(R.id.manmanimage);
            tvtiming = (TextView) view.findViewById(R.id.textViewCallDuration);
            tvtext = (TextView)view.findViewById(R.id.textViewCallDatess);
            ivicon = (ImageView) view.findViewById(R.id.imageView);
            ivsimiv = (ImageView) view.findViewById(R.id.simicon);
            mLinearLayout = (LinearLayout) view.findViewById(R.id.llone);
            mLinearOne = (LinearLayout) view.findViewById(R.id.llseven);



            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    progressBar.setVisibility(View.GONE);
                }
            }, 1500);





        }
    }
//    public void filterList(List<Object>  filteredList) {
//        recyclerViewItems = filteredList;
//        notifyDataSetChanged();
//    }


    public class AdViewHolder extends RecyclerView.ViewHolder {
        private TemplateView template;
        AdViewHolder(View view) {
            super(view);



            template = view.findViewById(R.id.my_template);
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
                        R.layout.list_row_two, viewGroup, false);
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

    public int colornew = 0;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                MenuItemViewHolder menuItemHolder = (MenuItemViewHolder) holder;
                SpamMenuItem menuItem = (SpamMenuItem) recyclerViewItems.get(position);


//                if(menuItem.getApi().equalsIgnoreCase("1")){
//                    menuItemHolder.tvapicheck.setVisibility(View.VISIBLE);
//                }else {
//                    menuItemHolder.tvapicheck.setVisibility(View.GONE);
//                }
                Log.d("GetContactswallet", "GetContacts inseart New Adapter = " + menuItem.getName());
//                Log.d("GetContactswallet", "New Adapter Call Type = " + menuItem.getCallType());
                // Get the menu item image resource ID.
//                String imageName = menuItem.getName();
//                int imageResID = context.getResources().getIdentifier(imageName, "drawable",
//                        context.getPackageName());
//
//                // Add the menu item details to the menu item view.
//                menuItemHolder.menuItemImage.setImageResource(imageResID);
//                Resources res = context.getResources();
//                final int newColor = res.getColor(R.color.red);
//                menuItemHolder.circleImageView.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                DrawableCompat.setTint(menuItemHolder.circleImageView.getDrawable(), ContextCompat.getColor(context, R.color.red));
                menuItemHolder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        MyDbHandler myDbHandler = new MyDbHandler(context,"userbd",null,1);
//                        myDbHandler = Temp.getMyDbHandler();
//                        GetCallLogTable user = new GetCallLogTable();
//                        user.setId(menuItem.getIds());
//                        user.setCaller_read("1");
//                        user.setIndex("0");
//                        user.setImage(menuItem.getImage());
//                        user.setDuration(String.valueOf(menuItem.getDuration()));
//                        user.setDate(menuItem.getDatenew());
//                        user.setSim_type(menuItem.getSimType());
//                        user.setCall_type(menuItem.getCallType());
//                        user.setNop(menuItem.getNumber());
//                        user.setName(menuItem.getName());
//                        myDbHandler.updateUser(user);
                        if(menuItem.getType().equalsIgnoreCase("calllog")) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString("callclog", "two");
                            edit.apply();

                            Intent inten = new Intent(context, ContactDetailsActivity.class);
                            inten.putExtra("name", menuItem.getName());
                            inten.putExtra("number", menuItem.getNumber());
                            inten.putExtra("img", menuItem.getImage());
                            context.startActivity(inten);
                        }else {
                            Intent intent = new Intent(context, MessageDetailsActivity.class);
                            intent.putExtra("address",menuItem.getAddress());
                            intent.putExtra("body",menuItem.getBody());
                            intent.putExtra("date",menuItem.getDate());
                            intent.putExtra("image",menuItem.getImage());
                            context.startActivity(intent);
                        }
                    }
                });
                menuItemHolder.menuItemName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        MyDbHandler myDbHandler = new MyDbHandler(context,"userbd",null,1);
//                        myDbHandler = Temp.getMyDbHandler();
//                        GetCallLogTable user = new GetCallLogTable();
//                        user.setId(menuItem.getIds());
//                        user.setCaller_read("1");
//                        user.setIndex("0");
//                        user.setImage(menuItem.getImage());
//                        user.setDuration(String.valueOf(menuItem.getDuration()));
//                        user.setDate(menuItem.getDatenew());
//                        user.setSim_type(menuItem.getSimType());
//                        user.setCall_type(menuItem.getCallType());
//                        user.setNop(menuItem.getNumber());
//                        user.setName(menuItem.getName());
//                        myDbHandler.updateUser(user);
                        if(menuItem.getType().equalsIgnoreCase("calllog")) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString("callclog", "two");
                            edit.apply();

                            Intent inten = new Intent(context, ContactDetailsActivity.class);
                            inten.putExtra("name", menuItem.getName());
                            inten.putExtra("number", menuItem.getNumber());
                            inten.putExtra("img", menuItem.getImage());
                            context.startActivity(inten);
                        }else {
                            Intent intent = new Intent(context, MessageDetailsActivity.class);
                            intent.putExtra("address",menuItem.getAddress());
                            intent.putExtra("body",menuItem.getBody());
                            intent.putExtra("date",menuItem.getDate());
                            intent.putExtra("image",menuItem.getImage());
                            context.startActivity(intent);
                        }
                    }
                });
                menuItemHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(menuItem.getType().equalsIgnoreCase("calllog")){
                            Intent inten = new Intent(context, SpamUserDetialActivity.class);
                            inten.putExtra("name",menuItem.getName());
                            inten.putExtra("number",menuItem.getNumber());
                            inten.putExtra("img",menuItem.getImage());
                            inten.putExtra("duration","100");
                            inten.putExtra("spam_count",menuItem.getSpamcount());
                            context.startActivity(inten);
                        }else {
                            Intent intent = new Intent(context, MessageDetailsActivity.class);
                            intent.putExtra("address",menuItem.getAddress());
                            intent.putExtra("body",menuItem.getBody());
                            intent.putExtra("date",menuItem.getDate());
                            intent.putExtra("image",menuItem.getImage());
                            context.startActivity(intent);
                        }

                    }
                });
                menuItemHolder.mLinearOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(menuItem.getType().equalsIgnoreCase("calllog")){
                            Intent inten = new Intent(context, SpamUserDetialActivity.class);
                            inten.putExtra("name",menuItem.getName());
                            inten.putExtra("number",menuItem.getNumber());
                            inten.putExtra("img",menuItem.getImage());
                            inten.putExtra("duration","100");
                            inten.putExtra("spam_count",menuItem.getSpamcount());
                            context.startActivity(inten);
                        }else {
                            Intent intent = new Intent(context, MessageDetailsActivity.class);
                            intent.putExtra("address",menuItem.getAddress());
                            intent.putExtra("body",menuItem.getBody());
                            intent.putExtra("date",menuItem.getDate());
                            intent.putExtra("image",menuItem.getImage());
                            context.startActivity(intent);
                        }
                    }
                });

                menuItemHolder.ivcall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuItemHolder.defaultpopupnew.defaultaddpopup(menuItem.getNumber());
                    }
                });
                if(menuItem.getImage().equalsIgnoreCase("NA")){
                    menuItemHolder.mLinearLayout.setVisibility(View.GONE);
                    menuItemHolder.mLinearOne.setVisibility(View.VISIBLE);
                    menuItemHolder.firstTime.setVisibility(View.VISIBLE);
                }else {

                    menuItemHolder.mLinearOne.setVisibility(View.VISIBLE);
                    menuItemHolder.firstTime.setVisibility(View.GONE);

//                    Glide.with(context)
//                            .load(menuItem.getImage())
//                            .centerCrop()
//                            .placeholder(R.drawable.profile)
//                            .into(menuItemHolder.circleImageView);


                }
                GradientDrawable gradientDrawable = (GradientDrawable) menuItemHolder.firstTime.getBackground();
                gradientDrawable.setColor(getRandomColor());


                menuItemHolder.firstTime.setTextColor(getRandomColorText());
//
//                if ((position & 1) == 1) {
//                    menuItemHolder.mLinearLayout.setBackgroundResource(R.drawable.circledrawable);
//                    menuItemHolder.firstTime.setTextColor(Color.parseColor("#55A655"));
//                } else
//                if ((position & 1) == 0 ) {
//                    menuItemHolder.firstTime.setTextColor(Color.parseColor("#4F88AA"));
//                    menuItemHolder.mLinearLayout.setBackgroundResource(R.drawable.circledrawnew);
//                } else {
//                    menuItemHolder.firstTime.setTextColor(Color.parseColor("#ffffff"));
//                    menuItemHolder.mLinearLayout.setBackgroundResource(R.drawable.circledrawnewtwo);
//                }

                if(menuItem.getRead().equalsIgnoreCase("0")){
                    menuItemHolder.menuItemName.setTextColor(Color.parseColor("#ff0000"));
                }else {
                    menuItemHolder.menuItemName.setTextColor(Color.parseColor("#000000"));
                }


                if(menuItem.getType().equalsIgnoreCase("calllog")) {
                    if(menuItem.getCallType().equalsIgnoreCase(String.valueOf(CallLog.Calls.REJECTED_TYPE))){
                        menuItemHolder.menuItemName.setText(menuItem.getName());
                        if(menuItem.getRead().equalsIgnoreCase("0")){
                            menuItemHolder.menuItemName.setTypeface(null, Typeface.BOLD);
                        }else {
                            menuItemHolder.menuItemName.setTypeface(null, Typeface.NORMAL);
                        }


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
                        DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.red));
                        menuItemHolder.tvtext.setText("Outgoing call · ");
                        menuItemHolder.tvsim.setText("");
                        menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#908F9D"));
                        menuItemHolder.menuItemDate.setTypeface(null, Typeface.NORMAL);
                        menuItemHolder.tvtext.setTextColor(Color.parseColor("#ff0000"));
                        menuItemHolder.tvtext.setTypeface(null, Typeface.NORMAL);

                    }else
                    if(menuItem.getCallType().equalsIgnoreCase(String.valueOf(CallLog.Calls.OUTGOING_TYPE))){
                        menuItemHolder.menuItemName.setText(menuItem.getName());

                        if(menuItem.getRead().equalsIgnoreCase("0")){
                            menuItemHolder.menuItemName.setTypeface(null, Typeface.BOLD);
                        }else {
                            menuItemHolder.menuItemName.setTypeface(null, Typeface.NORMAL);
                        }


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
                        DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.red));
                        menuItemHolder.tvtext.setText("Outgoing call · ");
                        menuItemHolder.tvsim.setText("");
                        menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#908F9D"));
                        menuItemHolder.menuItemDate.setTypeface(null, Typeface.NORMAL);
                        menuItemHolder.tvtext.setTextColor(Color.parseColor("#ff0000"));
                        menuItemHolder.tvtext.setTypeface(null, Typeface.NORMAL);

                    }else

                    if(menuItem.getCallType().equalsIgnoreCase(String.valueOf(CallLog.Calls.INCOMING_TYPE))){
                        menuItemHolder.menuItemName.setText(menuItem.getName());
                        if(menuItem.getRead().equalsIgnoreCase("0")){
                            menuItemHolder.menuItemName.setTypeface(null, Typeface.BOLD);
                        }else {
                            menuItemHolder.menuItemName.setTypeface(null, Typeface.NORMAL);
                        }
//                    menuItemHolder.menuItemName.setTypeface(null, Typeface.BOLD);

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
                        DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.red));

                        menuItemHolder.tvtext.setText("Incoming call · ");
                        menuItemHolder.tvtext.setTextColor(Color.parseColor("#ff0000"));
                        menuItemHolder.tvtext.setTypeface(null, Typeface.NORMAL);
                        menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#ff0000"));
                        menuItemHolder.menuItemDate.setTypeface(null, Typeface.NORMAL);
                        menuItemHolder.tvsim.setText(" ");

                    }else
                    if(menuItem.getCallType().equalsIgnoreCase(String.valueOf(CallLog.Calls.MISSED_TYPE))){
                        menuItemHolder.menuItemName.setText(menuItem.getName());

                        if(menuItemHolder.CheckMissCall){
                            menuItemHolder.ivsimiv.setImageResource(R.drawable.simonenew);
                            menuItemHolder.ivicon.setImageResource(R.drawable.ic_missed);
                            DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.red));


                            menuItemHolder.tvtext.setText("Missed call · ");
                            menuItemHolder.tvtext.setTextColor(Color.parseColor("#ff0000"));
                            menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#908F9D"));
                        }else {
                            menuItemHolder.ivsimiv.setImageResource(R.drawable.simmisone);
                            menuItemHolder.ivicon.setImageResource(R.drawable.misscallednew);
                            DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.red));


                            menuItemHolder.tvtext.setText("Missed call · ");

                            if(menuItem.getRead().equalsIgnoreCase("0")){
                                menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#ec0b0b"));
                                menuItemHolder.menuItemDate.setTypeface(null, Typeface.BOLD);
                                menuItemHolder.tvtext.setTextColor(Color.parseColor("#ec0b0b"));
                                menuItemHolder.tvtext.setTypeface(null, Typeface.BOLD);
                            }else {
                                menuItemHolder.tvtext.setTextColor(Color.parseColor("#000000"));
                                menuItemHolder.tvtext.setTypeface(null, Typeface.NORMAL);

                                menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#000000"));
                                menuItemHolder.menuItemDate.setTypeface(null, Typeface.NORMAL);
                            }

                        }


//                    menuItemHolder.ivsimiv.setImageResource(R.drawable.simmisone);
                        if(menuItem.getRead().equalsIgnoreCase("0")){
                            menuItemHolder.menuItemName.setTypeface(null, Typeface.BOLD);
                        }else {
                            menuItemHolder.menuItemName.setTypeface(null, Typeface.NORMAL);
                        }
//                    menuItemHolder.menuItemName.setTypeface(null, Typeface.BOLD);

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
//                    menuItemHolder.ivicon.setImageResource(R.drawable.misscallednew);
//                    DrawableCompat.setTint(menuItemHolder.ivicon.getDrawable(), ContextCompat.getColor(context, R.color.red));


//                    menuItemHolder.tvtext.setText("Missed call · ");
//                    menuItemHolder.tvtext.setTextColor(Color.parseColor("#ec0b0b"));
//                    menuItemHolder.menuItemDate.setTextColor(Color.parseColor("#ec0b0b"));
//                    menuItemHolder.tvtext.setTypeface(null, Typeface.BOLD);
//                    menuItemHolder.menuItemDate.setTypeface(null, Typeface.BOLD);

                    }
                    menuItemHolder.ivcall.setVisibility(View.VISIBLE);

                    menuItemHolder.ivsimiv.setVisibility(View.VISIBLE);
                    menuItemHolder.ivicon.setVisibility(View.VISIBLE);

                }else {
                    menuItemHolder.menuItemName.setText(menuItem.getAddress());
                    menuItemHolder.tvtext.setText("Message for you · ");
                    menuItemHolder.ivsimiv.setVisibility(View.GONE);
                    menuItemHolder.ivicon.setVisibility(View.GONE);
                    menuItemHolder.tvtext.setTextColor(Color.parseColor("#ff0000"));
                    menuItemHolder.ivcall.setVisibility(View.GONE);
                }





//                menuItemHolder.menuItemDate.setText(menuItem.getDate());


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
//
//                if(formatter.format(c).equalsIgnoreCase(formatter.format(dateObj))){
//
//                    long diff = System.currentTimeMillis()-menuItem.getDate();
//
//
//                    long hourss = TimeUnit.MILLISECONDS.toHours(diff);
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


//                menuItemHolder.menuItemPrice.setText(Utils.formatSeconds(menuItem.getDuration()));

                menuItemHolder.menuNumber.setText("");

                //   Typeface custom = Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-SemiBold.ttf");

                break;
            case BANNER_AD_VIEW_TYPE:
                // fall through
            default:
           AdViewHolder bannerHolder = (AdViewHolder) holder;

                AdView adView = (AdView) recyclerViewItems.get(position);
//                ViewGroup adCardView = (ViewGroup) bannerHolder.itemView;
//
//                if (adCardView.getChildCount() > 0) {
//                    adCardView.removeAllViews();
//                }
//                if (adView.getParent() != null) {
//                    ((ViewGroup) adView.getParent()).removeView(adView);
//                }




                AdLoader.Builder builder = new AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110");

                builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

                        bannerHolder.template.setNativeAd(unifiedNativeAd);
                    }
                });

                AdLoader adLoader = builder.build();
                adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

}


