package com.gpslab.kaun.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.R;
import com.gpslab.kaun.adapter.ImpMessageRecyclerViewAdapter;
import com.gpslab.kaun.adapter.ImpUnMessageRecyclerViewAdapter;
import com.gpslab.kaun.adapter.MessageRecyclerViewAdapter;
import com.gpslab.kaun.adapter.MisscallRecyclerViewAdapter;
import com.gpslab.kaun.adapter.RecyclerViewAdapter;
import com.gpslab.kaun.adapter.UnMessageRecyclerViewAdapter;
import com.gpslab.kaun.model.GetImpMessage;
import com.gpslab.kaun.model.GetImpUnMessage;
import com.gpslab.kaun.model.GetMessage;
import com.gpslab.kaun.model.GetMessageTable;
import com.gpslab.kaun.model.GetSMSTable;
import com.gpslab.kaun.model.GetUnMessage;
import com.gpslab.kaun.model.GetUnReadMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportantFragment extends Fragment {
    public List<GetMessage> productList = new ArrayList<>();
    public GetMessage getbcdata;
    public ImpMessageRecyclerViewAdapter adapter;
    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager, mMisscallmLayoutManager;
    public List<GetUnReadMessage> productListsec = new ArrayList<>();
    public GetUnReadMessage getbcdatasec;
    public ImpUnMessageRecyclerViewAdapter adapterss;
    public RecyclerView mRecyclerViewsec;
    public RecyclerView.LayoutManager mLayoutManagersec;
    public TextView tvcount;
    public TextView btnsosoound, btnhide;
    public LinearLayout mlinearlayout;
    int count = 0;
    boolean check = false;
    private List<Object> recyclerViewItems = new ArrayList<>();
    private List<Object> miscallrecyclerViewItems = new ArrayList<>();
    RecyclerViewAdapter adapterxxxx;
    MisscallRecyclerViewAdapter adapterssxxxx;
    boolean checkxx = false;
    public static final int ITEMS_PER_AD = 6;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/4177191030";
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public MyDbHandler myDbHandler;
    public Context mcontext;

    public ShimmerFrameLayout progressbar;
//    private ArrayList<String> KeyWordList = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_important, container, false);

        progressbar = (ShimmerFrameLayout)view.findViewById(R.id.progressbarone);
        progressbar.setVisibility(View.VISIBLE);
        mcontext = getActivity();
//
//
//        KeyWordList.add("verification code");
//        KeyWordList.add("OLA");
//        KeyWordList.add("Jio");
//        KeyWordList.add("SBI");
//        KeyWordList.add("airtel");
//        KeyWordList.add("Amazon");
//
//
//        KeyWordList.add("paytm");
//        KeyWordList.add("Paytm");
//        KeyWordList.add("Samsung");
//        KeyWordList.add("samsung");
//        KeyWordList.add("OTP");
//        KeyWordList.add("otp");
//
//
//
//
//        KeyWordList.add("AD-OLACAB");
//        KeyWordList.add("JX-JioPay");
//        KeyWordList.add("JE-JIOINF");
//        KeyWordList.add("QP-CENTBK");
//        KeyWordList.add("AD-CENTBK");
//
//
//        KeyWordList.add("AX-CENTBK");
//        KeyWordList.add("JK-CENTBK");
//        KeyWordList.add("JM-CENTBK");
//        KeyWordList.add("BP-iPaytm");
//        KeyWordList.add("MD-iPaytm");
//        KeyWordList.add("VM-iPaytm");
//
//
//
//
//
//
//        KeyWordList.add("AE-AIRGNF");
//        KeyWordList.add("AE-AIRTRF");
//        KeyWordList.add("AE-AIRTEL");
//        KeyWordList.add("TM-SAMSNG");





        initView(view);


        btnhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlinearlayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerViewsec.setVisibility(View.GONE);
            }
        });

        btnsosoound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    check = false;
                    btnsosoound.setText("SHOW");
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerViewsec.setVisibility(View.GONE);
                } else {
                    check = true;
                    btnsosoound.setText("Cancel");
                    mRecyclerView.setVisibility(View.GONE);
                    mRecyclerViewsec.setVisibility(View.VISIBLE);
                }
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mMisscallmLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);



        myDbHandler = new MyDbHandler(getActivity(), "userbd", null, 1);
        Temp.setMyDbHandler(myDbHandler);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerViewsec.setLayoutManager(mMisscallmLayoutManager);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                getAllData();
            }
        }, 5000);

        return view;
    }

    private void getAllData() {
        try {
            initComponents();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        addBannerAds();
        loadBannerAds();
        adapter = new ImpMessageRecyclerViewAdapter(getActivity(), recyclerViewItems);
//        adapterss = new ImpUnMessageRecyclerViewAdapter(getActivity(), miscallrecyclerViewItems);
        mRecyclerView.setAdapter(adapter);
//        mRecyclerViewsec.setAdapter(adapterss);



        progressbar.setVisibility(View.GONE);
    }

    private void initView(View view) {
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tvcount = (TextView) view.findViewById(R.id.counttv);
        btnhide = (TextView) view.findViewById(R.id.hidetv);
        mlinearlayout = (LinearLayout) view.findViewById(R.id.linearhide);
        mRecyclerViewsec = (RecyclerView) view.findViewById(R.id.recyclerViews);

        btnsosoound = (TextView) view.findViewById(R.id.showtv);
    }

    int countnew = 0;
    boolean countbool = true;

    private void initComponents() throws UnsupportedEncodingException {

        Log.d("WalletCheckItsComponents","aray  =    ");
        ArrayList<GetMessageTable> arrayList = myDbHandler.viewMessageNew();
        ArrayList<GetSMSTable> keylist = myDbHandler.viewSMSNew();
        if(arrayList.size()>0){
            for (int idx = 0; idx < arrayList.size(); idx++) {
                byte[] dataMobileone = Base64.decode(arrayList.get(idx).msg_body, Base64.DEFAULT);
                String Bodydd = new String(dataMobileone, "UTF-8");


                byte[] dataMobiletwo = Base64.decode(arrayList.get(idx).msg_address, Base64.DEFAULT);
                String Addressdd = new String(dataMobiletwo, "UTF-8");



                String keyword = "verification code";
                for (int j =0;j<keylist.size();j++){
                    if ( Bodydd.toLowerCase().indexOf(keylist.get(j).getSms_key()) != -1 ) {
                        Log.d("WalletCheckItsComponents","KeyWord   =  Body    =    ");
                        String id = arrayList.get(idx).msg_id;
                        String Read = arrayList.get(idx).msg_read;
                        String Address =  Addressdd;


                        String Dates =  arrayList.get(idx).msg_date;
                        Log.i("WalletCheckItsComponents", "totalSMS = = " + Address);
                        Log.d("fragmeentone", "Bodydd = = " + Bodydd);
                        GetImpMessage menuItem = new GetImpMessage(Address, id, Bodydd, Dates, Read,keylist.get(j).getSms_url());
                        recyclerViewItems.add(menuItem);

                    } else if (Addressdd.toLowerCase().indexOf(keylist.get(j).getSms_key()) != -1 ) {
                        Log.d("WalletCheckItsComponents","KeyWord   =  Address    =    ");
                        String id = arrayList.get(idx).msg_id;
                        String Read = arrayList.get(idx).msg_read;
                        String Address = Addressdd;


                        String Dates =  arrayList.get(idx).msg_date;
                        Log.i("WalletCheckItsComponents", "totalSMS = = " + Address);
                        Log.d("fragmeentone", "Bodydd = = " + Bodydd);
                        GetImpMessage menuItem = new GetImpMessage(Address, id, Bodydd, Dates, Read,keylist.get(j).getSms_url());
                        recyclerViewItems.add(menuItem);

                    }else {

                        System.out.println("not found");

                    }
                }


//                }else {
//                    Log.d("fragmeentone", "Not found totalSMS = = " );
//                }




            }
        }



    }


    private void addBannerAds() {
        for (int i = 3; i <= recyclerViewItems.size(); i += ITEMS_PER_AD) {
            final AdView adView = new AdView(mcontext);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(AD_UNIT_ID);
            recyclerViewItems.add(i, adView);
        }
    }

    private void loadBannerAds() {
        loadBannerAd(3);
    }

    private void loadBannerAd(final int index) {
        if (index >= recyclerViewItems.size()) {
            return;
        }
        Object item = recyclerViewItems.get(index);
        if (!(item instanceof AdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a banner ad" + " ad.");
        }
        final AdView adView = (AdView) item;
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                loadBannerAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("MainActivity", "The previous banner ad failed to load. Attempting to"
                        + " load the next banner ad in the items list.");
                loadBannerAd(index + ITEMS_PER_AD);
            }
        });
        adView.loadAd(new AdRequest.Builder().build());
    }
}