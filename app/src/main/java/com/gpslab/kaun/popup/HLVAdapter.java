package com.gpslab.kaun.popup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.telecom.Conference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gpslab.kaun.R;
import com.gpslab.kaun.language.LanguageActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class HLVAdapter extends RecyclerView.Adapter<HLVAdapter.ViewHolder> {
    public static Context mcontext;
    //    public static AsyncCallWS task;
    public boolean run = false;
    public static List<Getlanguage> productList;
    public static boolean runtimer = true;
    public Context context;
    ViewHolder view;
    public String Count, TotalWalletnew, Loginid, Counter;
    long timertime = 0;
    public Getlanguage getselfdata;
    String Contest = "";

    double entryfees = 0;
    JSONObject jasonObject;
    JSONArray jsonArray = null;
    ViewHolder viewHolder;
    public String totalwallet;
    public String GameName, Gameid, Gamelayout;
    public Dialog progressbar;
    public Dialog Popup;



    public HLVAdapter(Context context, List<Getlanguage> productList) {
        super();

        this.context = context;
        this.productList = productList;
        mcontext = context;
        progressbar = new Dialog(context);
   
        Popup = new Dialog(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_contest_row, viewGroup, false);
        viewHolder = new ViewHolder(v);


        return viewHolder;
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        getselfdata = productList.get(i);
        view = viewHolder;

        viewHolder.tvNickname.setText(productList.get(i).getLanguage());
        


    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNickname;
     
        private ItemClickListener clickListener;
        public SharedPreferences sharedPreferences;
        public SharedPreferences.Editor edit;

        public ViewHolder(View itemView) {
            super(itemView);


            sharedPreferences = mcontext.getSharedPreferences("data",Context.MODE_PRIVATE);
            edit = sharedPreferences.edit();


            tvNickname = (TextView) itemView.findViewById(R.id.nametv);
      


            tvNickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mcontext, "Language = "+productList.get(getAdapterPosition()).getLanguage(), Toast.LENGTH_SHORT).show();

                    if(productList.get(getAdapterPosition()).getId().equalsIgnoreCase("0")){
                        setLocale("en");

                    }else if(productList.get(getAdapterPosition()).getId().equalsIgnoreCase("1")){
                        setLocale("hi");
                    }else if(productList.get(getAdapterPosition()).getId().equalsIgnoreCase("2")){
                        setLocale("fr");
                    }else if(productList.get(getAdapterPosition()).getId().equalsIgnoreCase("3")){
                        setLocale("ur");
                    }

                }

                private void setLocale(String lang) {
                    Locale locale = new Locale(lang);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    mcontext.getResources().updateConfiguration(config, mcontext.getResources().getDisplayMetrics());
                    edit.putString("My_Lang",lang);
                    edit.apply();


                    Intent intent = new Intent(mcontext, LanguageActivity.class);
                    mcontext.startActivity(intent);
                }
            });

        }


    }







}


