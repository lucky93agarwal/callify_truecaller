package com.gpslab.kaun.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.gpslab.kaun.CallLogInfo;
import com.gpslab.kaun.CallLogUtils;
import com.gpslab.kaun.DB.FirstTableData;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.Home.GetCallLogTable;
import com.gpslab.kaun.Login.LoginSecActivity;
import com.gpslab.kaun.MainActivity;
import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.MyBrodcastRecieverService;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.Service.CallLogService;
import com.gpslab.kaun.Service.SocketServiceProvider;
import com.gpslab.kaun.adapter.CallLogAdapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


import com.gpslab.kaun.adapter.MisscallRecyclerViewAdapter;
import com.gpslab.kaun.adapter.RecyclerViewAdapter;
import com.gpslab.kaun.model.GetContectData;
import com.gpslab.kaun.model.MenuItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gpslab.kaun.privacyActivity;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit_model.AddUserResponseJson;
import com.gpslab.kaun.retrofit_model.RefreshDataRequestJson;
import com.gpslab.kaun.retrofit_model.RefreshDataResponseJson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentOne extends Fragment {

    /// it for RecyclerView
    public List<CallLogInfo> productList = new ArrayList<>();
    public CallLogInfo getbcdata;

//    public CallLogAdapter adapter;

    String resTxt = null;

    public RecyclerView mRecyclerView, mMisscallRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager, mMisscallmLayoutManager;
    public String LIST_STATE_KEY = "list_state";
    Cursor cursor;
    public TextView tvcount;
    public TextView btnsosoound, btnhide;
    String name, phonenumber;

    public FragmentOne() {

    }

    public String NewDate;
    RecyclerViewAdapter adapter;
    MisscallRecyclerViewAdapter adapterss;
    boolean check = false;
    public static final int ITEMS_PER_AD = 6;

    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";

    private List<Object> recyclerViewItems = new ArrayList<>();


    private List<Object> miscallrecyclerViewItems = new ArrayList<>();


    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;


    public List<String> getallLog = new ArrayList<>();
    public List<String> getnumber = new ArrayList<>();


// Miss Called
    public List<String> getMissCallednumber = new ArrayList<>();

    public List<String> getMiss_namexx = new ArrayList<>();

    public List<String> getMiss_calltype = new ArrayList<>();

    public List<String> getMiss_datexx = new ArrayList<>();

    public List<Long> getMiss_duration = new ArrayList<>();
    public List<String> getMiss_simtype = new ArrayList<>();

    /// Miss Called



    public List<String> getimage = new ArrayList<>();


    public List<String> getnamexx = new ArrayList<>();
    public List<String> getcallread = new ArrayList<>();
    public List<String> getids = new ArrayList<>();


    public List<String> getDateNew = new ArrayList<>();

    public List<String> getcalltype = new ArrayList<>();

    public List<String> getdatexx = new ArrayList<>();

    public List<Long> getduration = new ArrayList<>();
    public List<String> getsimtype = new ArrayList<>();


    public List<String> getMobilexx = new ArrayList<>();
    public List<String> getNamexx = new ArrayList<>();
    public List<String> getImage = new ArrayList<>();


    public TextView tvone, tvtwo;
    public LinearLayout mlinearLayout;

    public LinearLayout mlinearlayout;
    boolean misscalled = false;
    public MyDbHandler myDbHandler;
    private ContentResolver cResolver;
    private Window window;
    private int brightness = 200;

    public Context mcontext;
    public ShimmerFrameLayout mProgressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view;
        view = inflater.inflate(R.layout.fragment_one, container, false);
        window = getActivity().getWindow();
        mcontext = getActivity();
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        initView(view);


//        ScreenViewControl();


        tvtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlinearLayout.setVisibility(View.GONE);
            }
        });
        tvone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlinearLayout.setVisibility(View.GONE);
            }
        });


        Log.d("NewDataLuckyYUYU", "List = ");
        Toast.makeText(getActivity(), "List", Toast.LENGTH_SHORT).show();


        if (misscalled) {
            mlinearlayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mMisscallRecyclerView.setVisibility(View.GONE);
        }
        btnhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putBoolean("miss", true);
                edit.apply();
                Log.d("WalletUPdateQuery","Update");
//                myDbHandler = Temp.getMyDbHandler();
//                myDbHandler.view_update();
                mlinearlayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mMisscallRecyclerView.setVisibility(View.GONE);
            }
        });

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Do something after 5s = 5000ms
//
//            }
//        }, 1000);
        getAllData();
//        final Handler handler = new Handler();
//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//        getAllData();
//                handler.postDelayed(this, 5000);
//            }
//        };
//        handler.post(run);


        btnsosoound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    check = false;
                    btnsosoound.setText("SHOW");
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mMisscallRecyclerView.setVisibility(View.GONE);
                } else {
                    check = true;
                    btnsosoound.setText("Cancel");
                    mRecyclerView.setVisibility(View.GONE);
                    mMisscallRecyclerView.setVisibility(View.VISIBLE);
                }


//                filter(String.valueOf(CallLog.Calls.MISSED_TYPE));
            }
        });

        new CountDownTimer(300000, 500) {

            public void onTick(long millisUntilFinished) {
//                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                String CallLog = sharedPreferences.getString("callclog","one");
                if(CallLog.equalsIgnoreCase("two")){
                    Log.d("YUYULuckyYUYTUNowkyY", "Run api = " );
                    edit.putString("callclog","one");
                    edit.apply();
                    if(getids.size() >0){
                        getids.clear();
                    }
                    if(getcallread.size() >0){
                        getcallread.clear();
                    }
                    if(recyclerViewItems.size() >0){
                        recyclerViewItems.clear();
                    }
                    if(getnumber.size() >0){
                        getnumber.clear();
                    }
                    if(getMissCallednumber.size() >0){
                        getMissCallednumber.clear();
                    }
                    if(getMiss_namexx.size() >0){
                        getMiss_namexx.clear();
                    }
                    if(getMiss_calltype.size() >0){
                        getMiss_calltype.clear();
                    }
                    if(getMiss_datexx.size() >0){
                        getMiss_datexx.clear();
                    }
                    if(getMiss_duration.size() >0){
                        getMiss_duration.clear();
                    }
                    if(getMiss_simtype.size() >0){
                        getMiss_simtype.clear();
                    }
                    if(getnamexx.size() >0){
                        getnamexx.clear();
                    }
                    if(getcalltype.size() >0){
                        getcalltype.clear();
                    }
                    if(getdatexx.size() >0){
                        getdatexx.clear();
                    }
                    if(getduration.size() >0){
                        getduration.clear();
                    }

                    if(getsimtype.size() >0){
                        getsimtype.clear();
                    }
                    if(getImage.size() >0){
                        getImage.clear();
                    }
                    if(getDateNew.size() >0){
                        getDateNew.clear();
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            getAllData();
                        }
                    }, 1000);
                }

            }

            public void onFinish() {
//                mTextField.setText("done!");
            }

        }.start();

        return view;
    }

    private void getAllData() {
        initComponents();



        addBannerAds();
        addBannerAdsMisscall();


        loadBannerAds();


        setAdapterinRecyclerView();
    }

    private void ScreenViewControl() {
        //       Get the current system brightness
        WindowManager.LayoutParams layoutpars = window.getAttributes();
        layoutpars.screenBrightness = brightness / (float) 255;
        window.setAttributes(layoutpars);
    }

    private void setAdapterinRecyclerView() {
        adapter = new RecyclerViewAdapter(getActivity(),
                recyclerViewItems);
        adapterss = new MisscallRecyclerViewAdapter(getActivity(),
                miscallrecyclerViewItems);


        mRecyclerView.setAdapter(adapter);
        mMisscallRecyclerView.setAdapter(adapterss);
    }

    private void initView(View view) {
        mProgressbar = (ShimmerFrameLayout) view.findViewById(R.id.progressbarone);
        mProgressbar.setVisibility(View.VISIBLE);
        tvone = (TextView) view.findViewById(R.id.newonetv);
        tvtwo = (TextView) view.findViewById(R.id.newtwotv);
        mlinearLayout = (LinearLayout) view.findViewById(R.id.newnewlinear);


        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        misscalled = sharedPreferences.getBoolean("miss", false);
        tvcount = (TextView) view.findViewById(R.id.counttv);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mMisscallRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViews);


        mlinearlayout = (LinearLayout) view.findViewById(R.id.linearhide);

        btnsosoound = (TextView) view.findViewById(R.id.showtv);
        btnhide = (TextView) view.findViewById(R.id.hidetv);


        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mMisscallmLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mMisscallRecyclerView.setLayoutManager(mMisscallmLayoutManager);
    }

    int sizesize = 0;

//    private void onScrolledToBottom() {
//
////        getnumber.clear();
//
//
//        sizesize = recyclerViewItems.size();
//        CallLogUtils callLogUtils = CallLogUtils.getInstance(getActivity());
//        if (recyclerViewItems.size() < callLogUtils.readCallLogs().size()) {
//            int x, y;
//            if ((callLogUtils.readCallLogs().size() - recyclerViewItems.size()) >= 50) {
//                x = recyclerViewItems.size();
//                y = x + 50;
//            } else {
//                x = recyclerViewItems.size();
//                y = x + callLogUtils.readCallLogs().size() - recyclerViewItems.size();
//            }
//            for (int i = x; i < y; i++) {
//                String name = callLogUtils.readCallLogs().get(i).getName();
//                String number = callLogUtils.readCallLogs().get(i).getNumber();
//                String calltype = callLogUtils.readCallLogs().get(i).getCallType();
//                String SimType = callLogUtils.readCallLogs().get(i).getSimtype();
//                Log.d("WalletYUYULucky", "size = " + number);
//                Log.d("WalletYUYULucky", "size 786 = " + getnumber.size());
////                getnumber.add(number.substring(number.length() - 10).toString());
//
//                long date = callLogUtils.readCallLogs().get(i).getDate();
//                long duration = callLogUtils.readCallLogs().get(i).getDuration();
//
////                Log.d("WalletYUYULucky","calltype = "+SimType);
//
//                MenuItem menuItem = new MenuItem(name, number, calltype,
//                        date, duration, SimType, "NA", "0");
//                recyclerViewItems.add(menuItem);
//            }
////            SendDataToServer(getnumber.toString());
////            Log.d("WalletYUYULucky", "size = " + recyclerViewItems.size());
//            addBannerAdss();
//            loadBannerAdss();
//            adapter.notifyDataSetChanged();
//        }
//
//    }


    private void addBannerAdss() {

        for (int i = sizesize + 3; i <= recyclerViewItems.size(); i += ITEMS_PER_AD) {
            final AdView adView = new AdView(getActivity());

            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(AD_UNIT_ID);

            recyclerViewItems.add(i, adView);


        }


    }

    private void loadBannerAdss() {
        loadBannerAd(sizesize + 3);
        adapter.notifyDataSetChanged();
    }

    private void addBannerAds() {
        // Loop through the items array and place a new banner ad in every ith position in
        // the items List.


        for (int i = 3; i <= recyclerViewItems.size(); i += ITEMS_PER_AD) {
            final AdView adView = new AdView(mcontext);

            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(AD_UNIT_ID);

            recyclerViewItems.add(i, adView);


        }


    }


    private void addBannerAdsMisscall() {


        for (int i = 3; i <= miscallrecyclerViewItems.size(); i += ITEMS_PER_AD) {
            final AdView adView = new AdView(mcontext);

            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(AD_UNIT_ID);


            miscallrecyclerViewItems.add(i, adView);
        }


    }


    private void loadBannerAds() {

        loadBannerAd(3);
        loadBannerAdMissicall(3);
    }

    private void loadBannerAdMissicall(final int index) {

        if (index >= miscallrecyclerViewItems.size()) {
            return;
        }

        Object item = miscallrecyclerViewItems.get(index);
        if (!(item instanceof AdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a banner ad" + " ad.");
        }

        final AdView adView = (AdView) item;

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous banner ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadBannerAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous banner ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous banner ad failed to load. Attempting to"
                        + " load the next banner ad in the items list.");
                loadBannerAd(index + ITEMS_PER_AD);
            }
        });

        // Load the banner ad.
        adView.loadAd(new AdRequest.Builder().build());
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

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous banner ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadBannerAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous banner ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous banner ad failed to load. Attempting to"
                        + " load the next banner ad in the items list.");
                loadBannerAd(index + ITEMS_PER_AD);
            }
        });

        // Load the banner ad.
        adView.loadAd(new AdRequest.Builder().build());
    }

    int count = 0;





    int countnew = 0;

    public void initComponents() {




        newsenddata();


    }

    public String images_new;
    private void newsenddata() {
        myDbHandler = Temp.getMyDbHandler();
        ArrayList<GetCallLogTable> arrayList = myDbHandler.viewCallLogstRow();

        Log.d("CCpWalletCCpWalletCalllog", "name =  " + arrayList.get(3).getName());
        Log.d("CCpWalletCCpWalletCalllog", "number =  " + arrayList.get(3).getNop());
        Log.d("CCpWalletCCpWalletCalllog", "image =  " + arrayList.get(3).getImage());
        Log.d("CCpWalletCCpWalletCalllog", "calltype =  " + arrayList.get(3).getCall_type());
        Log.d("k;a'skdfks;kfs", "Contacts =  " + arrayList.size());
        for (int i=0;i<arrayList.size();i++) {
//            Log.d("k;a'skdfks;kfs","Contacts =  "+arrayList.get(i).toString());


            String ids = arrayList.get(i).getId();
            String name = arrayList.get(i).getName();
            String readcall = arrayList.get(i).getCaller_read();
            images_new = arrayList.get(i).getImage();
//            Log.i("GetContactswalletimages", "GetContacts images = " + images);

            Log.d("GetContactswallet", "GetContacts inseart New = " + arrayList.get(i).getName());
            String number = arrayList.get(i).getNop();





           String mobile_input = number.replace(" ", "");
//            if(mobile_input.length() >10){
//
//            }else
            if(mobile_input.length() == 10){
                String str = mobile_input;
                String num = str;
                String nums = "91"+num;
                number = nums;
//                jsonArray.put(nums);
            }else if(mobile_input.length()== 11){
                String str = mobile_input;
                String num = String.valueOf(str.substring(str.length()-10));
                String nums = "91"+num;
                number = nums;
//                jsonArray.put(nums);
            }else if(mobile_input.length()== 12){
                number = mobile_input;
//                jsonArray.put(mobile_input);
            }else if(mobile_input.length() == 13){
                String str = mobile_input;
                String num = String.valueOf(str.substring(str.length()-10));
                String nums = "91"+num;
                number = nums;
            }else{
                number = number;
//                jsonArray.put(contact.getPhoneNumber());
            }

            if(i<10){
                getnumber.add(number);
            }

//            try{
//                byte[] datamobileone = number.getBytes("UTF-8");
//                String base64mobileone = Base64.encodeToString(datamobileone, Base64.DEFAULT);
//                ArrayList<FirstTableData> arrayListtwo = myDbHandler.viewContact(base64mobileone);
//                Log.i("GetContactswalletimages", "GetContacts inseart New = " + arrayListtwo.size());
//            }catch (IOException e){
//                e.printStackTrace();
//            }




            String calltype = arrayList.get(i).getCall_type();
            String SimType = arrayList.get(i).getSim_type();
            long date = Long.parseLong(arrayList.get(i).getDate());
            String datenew = arrayList.get(i).getDate();
            getDateNew.add(arrayList.get(i).getDate());

            /// CALCULATE date
            Date dateObj = new Date(date);
            Date c = Calendar.getInstance().getTime();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   ");

            long difffs = System.currentTimeMillis()-date;
            long secfs = TimeUnit.MILLISECONDS.toSeconds(difffs);
            if(formatter.format(c).equalsIgnoreCase(formatter.format(dateObj))){

                long diff = System.currentTimeMillis()-date;


                long hourss = TimeUnit.MILLISECONDS.toHours(diff);
                long min = TimeUnit.MILLISECONDS.toMinutes(diff);
                long sec = TimeUnit.MILLISECONDS.toSeconds(diff);



                Log.d("sjdljsldjlssdfjdl","size 7 Current Date = "+hourss+"hr "+diff+" diff= "+date+" date  "+System.currentTimeMillis()+" CurrentTime "+ min +" min");


                if(hourss >0){
                    if(hourss < 5){
                        NewDate = hourss +"hr ago";
                    }else {
                        NewDate = "Today";
                    }

                }else {

                    if(min >0){
                        NewDate = min+"min ago";
                    }else {
                        NewDate = sec+"sec ago";
                    }

                }

            }else if(formatter.format(cal.getTime()).equalsIgnoreCase(formatter.format(dateObj))){
                NewDate = "Yesterday";
            }else {
                NewDate = String.valueOf(formatter.format(dateObj));
            }



            long duration = Long.parseLong(arrayList.get(i).getDuration());
//            if (calltype.equalsIgnoreCase(String.valueOf(CallLog.Calls.MISSED_TYPE))) {
//                count = count + 1;
//                getMissCallednumber.add(number);
//
//                Log.d("fragmentoMissCalled", "f calltype = = " + calltype);
//                getMiss_namexx.add(name);
//                getMiss_calltype.add(calltype);
//                getMiss_simtype.add(SimType);
//                getMiss_datexx.add(NewDate);
//                getMiss_duration.add(secfs);
//
//                MenuItem missionmenu = new MenuItem(name, number, calltype, NewDate, secfs, readcall, SimType, "NA", "0",ids,datenew);
//
//                miscallrecyclerViewItems.add(missionmenu);
//            }
            getids.add(ids);
            getcallread.add(readcall);
            getnamexx.add(name);
            getcalltype.add(calltype);
            getsimtype.add(SimType);
            getdatexx.add(NewDate);
            getduration.add(secfs);



            getimage.add(images_new);
//            }
            MenuItem menuItem = new MenuItem(name, number, calltype,
                    NewDate, secfs, readcall, SimType, images_new, "0",ids,datenew);

            recyclerViewItems.add(menuItem);


        }


        if (arrayList.size() > 0) {


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    retrofit();
//                    SendDataToServer(getnumber.toString());
                }
            }, 5000);



//            final Handler handler = new Handler();
//            Runnable run = new Runnable() {
//                @Override
//                public void run() {
//                    SendDataToServer(getnumber.toString());
//                    handler.postDelayed(this, 5000);
//                }
//            };
//            handler.post(run);
        }


        if (count == 0) {
            mlinearlayout.setVisibility(View.GONE);
        }
        mProgressbar.setVisibility(View.GONE);
        Log.d("MissCallDtacheck","count in miss call   =    "+count + " unread");
        tvcount.setText(String.valueOf(count + " unread"));
    }


//    public String image;
    public String namexx;

    public String Miss_image;
    public String Miss_namexx;
    public String Miss_apicheck = "0";


    public int Miss_j = 0;

    public int j = 0;
    public int z = 0;
    public String apicheck = "0";



    public RefreshDataRequestJson requet = new RefreshDataRequestJson();


    private void retrofit() {

        Log.d("CCpWalletCCpWallet", "nameValuePairs getnumber  = " + getnumber);
        Log.d("CCpWalletCCpWallet", "nameValuePairs token  = " + sharedPreferences.getString("token", ""));
        Log.d("CCpWalletCCpWallet", "nameValuePairs id  = " + sharedPreferences.getString("id",""));
        requet.mobiles = getnumber;
        requet.token =sharedPreferences.getString("token", "");
        requet.id = sharedPreferences.getString("id","");

        Log.i("tokentokentokentoken", "nameValuePairs Signup  = " + new Gson().toJson(requet));


        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.refresh_data(requet).enqueue(new Callback<RefreshDataResponseJson>() {
            @Override
            public void onResponse(Call<RefreshDataResponseJson> call, Response<RefreshDataResponseJson> response) {
//                Log.i("CCpWalletCCpWallet", "nameValuePairs code  = " + response.code());
                Log.i("CCpWalletCCpWalletCalllog", "nameValuePairs body  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    for(int i=0;i<response.body().response.size();i++){

                        Object item = recyclerViewItems.get(i);
                        if (!(item instanceof AdView)) {
                            Log.d("fragmentonefragmentone", "calltype 3 namexx = " + j);

                            namexx = String.valueOf(getnamexx.get(i));
                            Log.i("CCpWalletCCpWalletCalllog", "name = "+namexx);
                            if (namexx.equalsIgnoreCase("NA")) {
                                Log.i("CCpWalletCCpWalletCalllog", "785 = ");
                                if (response.body().response.get(i).name.equalsIgnoreCase("NA")){
                                    namexx = "NA";
                                    apicheck = "0";
                                }else {
                                    namexx = response.body().response.get(i).name;
                                    apicheck = "1";
                                }


                            }

                            if (response.body().response.get(i).image.equalsIgnoreCase("NA")) {

                                images_new = getimage.get(i);
                                apicheck = "0";

                            } else {

                                if(getimage.get(i).equalsIgnoreCase("NA")){
                                    apicheck = "1";
                                    images_new = response.body().response.get(i).image;
                                }else {
                                    images_new = getimage.get(i);
                                    apicheck = "0";
                                }



                            }


                            if (i >= j) {
                                MenuItem menuItem = new MenuItem(namexx, getnumber.get(i), getcalltype.get(i),
                                        getdatexx.get(i), getduration.get(i), getcallread.get(i), getsimtype.get(i), images_new, apicheck,getids.get(i),getDateNew.get(i));
                                recyclerViewItems.set(j, menuItem);
                                adapter.notifyItemChanged(j);
                            }

                            j = j + 1;
//                                throw new ClassCastException("Expected item at index " + i + " to be a banner ad" + " ad.");
                        } else {
                            j = j + 1;
                        }


                        Log.d("fragmentonefragmentone", "calltype 3 namexx = " + j);
                    }


                } else {
                    switch (response.code()) {
                        case 401:
//                            Toast.makeText(Splash.this, "email and pass not check", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
//                            Toast.makeText(Splash.this, "ForbiddenException", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
//                            Toast.makeText(Splash.this, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
//                            Toast.makeText(Splash.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
//                            Toast.makeText(Splash.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    Toast.makeText(getActivity(), "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<RefreshDataResponseJson> call, Throwable t) {
                t.printStackTrace();
                Log.i("CCpWalletCCpWalletCalllog", "error  = " + t.getMessage());
                Log.e("System error:", t.getLocalizedMessage());

            }
        });
    }

//    private void SendDataToServer(final String datasss) {
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//                String QuickFirstName = datasss;
//                JSONObject jsonObjectlucky = new JSONObject();
//                try {
//
//                    jsonObjectlucky.put("data", getnumber);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                String data = String.valueOf(Html.fromHtml(getnumber.toString(), Html.FROM_HTML_MODE_LEGACY));
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//                nameValuePairs.add(new BasicNameValuePair("mobiles", getnumber.toString()));
//                nameValuePairs.add(new BasicNameValuePair("token", sharedPreferences.getString("token","")));
//                nameValuePairs.add(new BasicNameValuePair("id", sharedPreferences.getString("mobile","")));
//                Log.d("fragmentonefragmentone", "fname = = " + nameValuePairs);
//
//                try {
//                    StringEntity se;
//                    se = new StringEntity(jsonObjectlucky.toString());
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "refresh_data/");
//
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//                    resTxt = EntityUtils.toString(entity);
//
//                } catch (ClientProtocolException e) {
//
//                } catch (IOException e) {
//
//                }
//                return resTxt;
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
////                mprogress.setVisibility(View.GONE);
//                Log.d("fragmentonefragmentone", "result = = " + result);
//                if (TextUtils.isEmpty(result)) {
//
//                } else {
//                    Log.d("fragmentonefragmentone", "calltype 2 = " + getnamexx.size());
//                    adapter.notifyDataSetChanged();
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONArray response = jsonObject.getJSONArray("response");
//                        Log.d("fragmentonefragmentone", "calltype 2 response = " + response.length());
//                        for (int i = 0; i < response.length(); i++) {
//
//
//                            Object item = recyclerViewItems.get(i);
//                            if (!(item instanceof AdView)) {
//                                Log.d("fragmentonefragmentone", "calltype 3 namexx = " + j);
//
//                                namexx = String.valueOf(getnamexx.get(i));
//                                if (namexx.equalsIgnoreCase("null")) {
//                                    namexx = response.getJSONObject(i).getString("name");
//                                    apicheck = "1";
//                                }
//
//                                if (response.getJSONObject(i).getString("image").equalsIgnoreCase("NA")) {
//
//                                    image = "NA";
//                                    apicheck = "0";
//
//                                } else {
//
//                                    apicheck = "1";
//                                    image = response.getJSONObject(i).getString("image");
//
//                                }
//
//
//                                if (i >= j) {
//                                    MenuItem menuItem = new MenuItem(namexx, getnumber.get(i), getcalltype.get(i),
//                                            getdatexx.get(i), getduration.get(i), getcallread.get(i), getsimtype.get(i), image, apicheck,getids.get(i),getDateNew.get(i));
//                                    recyclerViewItems.set(j, menuItem);
//                                    adapter.notifyItemChanged(j);
//                                }
//
//                                j = j + 1;
////                                throw new ClassCastException("Expected item at index " + i + " to be a banner ad" + " ad.");
//                            } else {
//                                j = j + 1;
//                            }
//
//
//                            Log.d("fragmentonefragmentone", "calltype 3 namexx = " + j);
////                            Log.d("fragmentonefragmentone", "calltype 33 namexx = " + namexx);
//////                            namexx ="1";
////                            Log.d("fragmentonefragmentone", "calltype 3333 namexx = " + namexx.length());
//                        }
//
////                        if(getMissCallednumber.size()>0){
////                            MissCallDataToServer(getMissCallednumber.toString());
////                        }
//
//
//                    } catch (JSONException e) {
//                        e.getMessage();
//                    }
//                }
//
//
//            }
//        }
//        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
//        sendPostReqAsyncTask.execute(datasss);
//    }




    ///// misscalled allow
//    private void MissCallDataToServer(final String datasss) {
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//                String QuickFirstName = datasss;
//                JSONObject jsonObjectlucky = new JSONObject();
//                try {
//
//                    jsonObjectlucky.put("data", getMissCallednumber);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                String data = String.valueOf(Html.fromHtml(getnumber.toString(), Html.FROM_HTML_MODE_LEGACY));
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//                nameValuePairs.add(new BasicNameValuePair("mobiles", getMissCallednumber.toString()));
//
//                Log.d("fragmentoMissCalled", "fname = = " + nameValuePairs);
//
//                try {
//                    StringEntity se;
//                    se = new StringEntity(jsonObjectlucky.toString());
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "refresh_data/");
//
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//                    resTxt = EntityUtils.toString(entity);
//
//                } catch (ClientProtocolException e) {
//
//                } catch (IOException e) {
//
//                }
//                return resTxt;
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
////                mprogress.setVisibility(View.GONE);
//                Log.d("fragmentoMissCalled", "result = = " + result);
//                if (TextUtils.isEmpty(result)) {
//
//                } else {
//                    Log.d("fragmentoMissCalled", "calltype 2 = " + getMiss_namexx.size());
//                    adapterss.notifyDataSetChanged();
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONArray miss_response = jsonObject.getJSONArray("response");
//                        Log.d("fragmentoMissCalled", "Miss Called = " + miss_response.length());
//                        for (int ii = 0; ii < miss_response.length(); ii++) {
//
//
//                            Object Miss_item = miscallrecyclerViewItems.get(ii);
//                            if (!(Miss_item instanceof AdView)) {
//                                Log.d("fragmentonefragmentone", "calltype 3 namexx = " + Miss_j);
//
//                                Miss_namexx = String.valueOf(getMiss_namexx.get(ii));
//                                if (Miss_namexx.equalsIgnoreCase("null")) {
//                                    Miss_namexx = miss_response.getJSONObject(ii).getString("name");
//                                    Miss_apicheck = "1";
//                                }
//
//                                if (miss_response.getJSONObject(ii).getString("image").equalsIgnoreCase("NA")) {
//
//                                    Miss_image = "NA";
//                                    Miss_apicheck = "0";
//
//                                } else {
//
//                                    Miss_apicheck = "1";
//                                    Miss_image = miss_response.getJSONObject(ii).getString("image");
//
//                                }
//
//
//                                if (ii >= Miss_j) {
//                                    MenuItem menuItem = new MenuItem(Miss_namexx, getMissCallednumber.get(ii), getMiss_calltype.get(ii),
//                                            getMiss_datexx.get(ii), getMiss_duration.get(ii), getMiss_simtype.get(ii), Miss_image, Miss_apicheck);
//                                    miscallrecyclerViewItems.set(Miss_j, menuItem);
//                                    adapterss.notifyItemChanged(Miss_j);
//                                }
//
//                                Miss_j = Miss_j + 1;
////                                throw new ClassCastException("Expected item at index " + i + " to be a banner ad" + " ad.");
//                            } else {
//                                Miss_j = Miss_j + 1;
//                            }
//
//
//                            Log.d("fragmentonefragmentone", "calltype 3 namexx = " + Miss_j);
////                            Log.d("fragmentonefragmentone", "calltype 33 namexx = " + namexx);
//////                            namexx ="1";
////                            Log.d("fragmentonefragmentone", "calltype 3333 namexx = " + namexx.length());
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.getMessage();
//                    }
//                }
//
//
//            }
//        }
//        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
//        sendPostReqAsyncTask.execute(datasss);
//    }
}