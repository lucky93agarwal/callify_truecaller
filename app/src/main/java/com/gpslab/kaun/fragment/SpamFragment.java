package com.gpslab.kaun.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CallLog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.gpslab.kaun.CallLogUtils;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.Home.GetCallLogTable;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.adapter.RecyclerViewAdapter;
import com.gpslab.kaun.adapter.SpamAdapter;
import com.gpslab.kaun.model.GetMessageTable;
import com.gpslab.kaun.model.GetSpamCallRequestJson;
import com.gpslab.kaun.model.GetSpamcallResponseJson;
import com.gpslab.kaun.model.MenuItem;
import com.gpslab.kaun.model.SpamMenuItem;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit_model.RefreshDataRequestJson;
import com.gpslab.kaun.retrofit_model.RefreshDataResponseJson;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpamFragment extends Fragment {


    public SpamFragment() {
        // Required empty public constructor
    }
    public static final int ITEMS_PER_AD = 6;
    public TextView tvone, tvtwo;
    public LinearLayout mlinearLayout;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> KeyWordList = new ArrayList<String>();
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
    public SpamAdapter adapter;
    private List<Object> recyclerViewItems = new ArrayList<>();

    ArrayList<SpamMenuItem> productlit = new ArrayList<>();
    public MyDbHandler myDbHandler;



    private ProgressBar progressBar;
    public ImageView ivno;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_spam, container, false);

        tvone = (TextView) view.findViewById(R.id.newonetv);
        tvtwo = (TextView) view.findViewById(R.id.newtwotv);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        ivno = (ImageView)view.findViewById(R.id.nodataiv);
        progressBar.setVisibility(View.VISIBLE);
        mlinearLayout = (LinearLayout) view.findViewById(R.id.newnewlinear);


        KeyWordList.add("verification code");
        KeyWordList.add("OLA");
        KeyWordList.add("Jio");
        KeyWordList.add("SBI");
        KeyWordList.add("airtel");
        KeyWordList.add("Amazon");







        KeyWordList.add("paytm");
        KeyWordList.add("Paytm");
        KeyWordList.add("Samsung");
        KeyWordList.add("samsung");
        KeyWordList.add("OTP");
        KeyWordList.add("otp");








        KeyWordList.add("AD-OLACAB");
        KeyWordList.add("JX-JioPay");
        KeyWordList.add("JE-JIOINF");
        KeyWordList.add("QP-CENTBK");
        KeyWordList.add("AD-CENTBK");








        KeyWordList.add("AX-CENTBK");
        KeyWordList.add("JK-CENTBK");
        KeyWordList.add("JM-CENTBK");
        KeyWordList.add("BP-iPaytm");
        KeyWordList.add("MD-iPaytm");
        KeyWordList.add("VM-iPaytm");








        KeyWordList.add("AE-AIRGNF");
        KeyWordList.add("AE-AIRTRF");
        KeyWordList.add("AE-AIRTEL");
        KeyWordList.add("TM-SAMSNG");






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

        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);
//
        try {
            initComponents();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return view;
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
    private void addBannerAds() {
        for (int i = 3; i <= recyclerViewItems.size(); i += ITEMS_PER_AD) {
            final AdView adView = new AdView(getActivity());
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(AD_UNIT_ID);
            recyclerViewItems.add(i, adView);
        }
    }
    int countnew = 0;
    public void initComponents() throws UnsupportedEncodingException {
        myDbHandler = Temp.getMyDbHandler();
        ArrayList<GetCallLogTable> arrayList = myDbHandler.viewCallLogstRow();



        if(arrayList.size()>25){
            for (int i=0;i<25;i++) {
                String name = arrayList.get(i).getName();
                String number = arrayList.get(i).getNop();

                if(number.length() == 10){
                    String str = number;
                    String num = str;
                    String nums = "91"+num;
                    number = nums;
//                jsonArray.put(nums);
                }else if(number.length()== 11){
                    String str = number;
                    String num = String.valueOf(str.substring(str.length()-10));
                    String nums = "91"+num;
                    number = nums;
//                jsonArray.put(nums);
                }else if(number.length()== 12){
                    number = number;
//                jsonArray.put(mobile_input);
                }else if(number.length() == 13){
                    String str = number;
                    String num = String.valueOf(str.substring(str.length()-10));
                    String nums = "91"+num;
                    number = nums;
                }else{
                    number = number;
//                jsonArray.put(contact.getPhoneNumber());
                }



                getnumber.add(number);
                getname.add(name);

                long date = Long.parseLong(arrayList.get(i).getDate());
                getdate.add(date);

                String callType = arrayList.get(i).getCall_type();
                getcallType.add(callType);

                Log.d("CheckCallLogDate","Call Log Date  "+arrayList.get(i).getDate());
                String type = "calllog";
                SpamMenuItem menuItem = new SpamMenuItem(type, "0", "0","0",
                        date, number, name,"NA",callType,"0");
                productlit.add(menuItem);
//            recyclerViewItems.add(menuItem);
            }
        }else {
            for (int i=0;i<arrayList.size();i++) {
                String name = arrayList.get(i).getName();
                String number = arrayList.get(i).getNop();

                if(number.length() == 10){
                    String str = number;
                    String num = str;
                    String nums = "91"+num;
                    number = nums;
//                jsonArray.put(nums);
                }else if(number.length()== 11){
                    String str = number;
                    String num = String.valueOf(str.substring(str.length()-10));
                    String nums = "91"+num;
                    number = nums;
//                jsonArray.put(nums);
                }else if(number.length()== 12){
                    number = number;
//                jsonArray.put(mobile_input);
                }else if(number.length() == 13){
                    String str = number;
                    String num = String.valueOf(str.substring(str.length()-10));
                    String nums = "91"+num;
                    number = nums;
                }else{
                    number = number;
//                jsonArray.put(contact.getPhoneNumber());
                }



                getnumber.add(number);
                getname.add(name);

                long date = Long.parseLong(arrayList.get(i).getDate());
                getdate.add(date);

                String callType = arrayList.get(i).getCall_type();
                getcallType.add(callType);

                Log.d("CheckCallLogDate","Call Log Date  "+arrayList.get(i).getDate());
                String type = "calllog";
                SpamMenuItem menuItem = new SpamMenuItem(type, "0", "0","0",
                        date, number, name,"NA",callType,"0");
                productlit.add(menuItem);
//            recyclerViewItems.add(menuItem);
            }
        }


//        ArrayList<GetMessageTable> arrayListMess = myDbHandler.viewMessageNew();
//        if(arrayListMess.size()>0){
//            for (int idx = 0; idx < arrayListMess.size(); idx++) {
//                Log.d("CheckCallLogDate","Message Date  "+arrayListMess.get(idx).getMsg_date());
//                long date = Long.parseLong(arrayListMess.get(idx).getMsg_date());
//
//                byte[] dataMobileone = Base64.decode(arrayListMess.get(idx).msg_body, Base64.DEFAULT);
//                String Bodydd = new String(dataMobileone, "UTF-8");
//
//
//
//                byte[] dataMobiletwo = Base64.decode(arrayListMess.get(idx).msg_address, Base64.DEFAULT);
//                String Addressdd = new String(dataMobiletwo, "UTF-8");
//                String typeM = "Message";
//
//                SpamMenuItem menuItem = new SpamMenuItem(typeM, "0", Addressdd,Bodydd,
//                        date, "0", "0","NA","NA");
//                productlit.add(menuItem);
////                recyclerViewItems.add(menuItem);
//            }
//        }

        Collections.sort(productlit, new Comparator<SpamMenuItem>() {
            public int compare(SpamMenuItem s1, SpamMenuItem s2) {
                Long point2 = s1.getDate();
                Long point1 = s2.getDate();
                return Long.compare(point1, point2);
            }
        });


//        for(int ij=0; ij<productlit.size(); ij++){
//            String type = productlit.get(ij).getType();
//            String read = productlit.get(ij).getRead();
//            String address = productlit.get(ij).getAddress();
//            String body = productlit.get(ij).getBody();
//            long date = productlit.get(ij).getDate();
//            String number = productlit.get(ij).getNumber();
//            String name = productlit.get(ij).getName();
//            String image = productlit.get(ij).getImage();
//            String callType = productlit.get(ij).getCallType();
//
//
//            SpamMenuItem menuItem = new SpamMenuItem(type, read, address,body, date, number, name,image,callType);
//
//
//            recyclerViewItems.add(menuItem);
//        }

        retrofit();
    }


    public GetSpamCallRequestJson requet = new GetSpamCallRequestJson();
    public List<String> getnumber = new ArrayList<>();
    public List<String> getname = new ArrayList<>();
    public List<String> getcallType = new ArrayList<>();
    public List<Long> getdate = new ArrayList<>();

    public SpamMenuItem menuItem;
    private void retrofit() {

        Log.d("CCpWalletCCpWallet", "nameValuePairs getnumber  = " + getnumber);
        requet.mobile = getnumber;

        Log.i("CCpWalletCCpWalletSpam", "nameValuePairs Signup  = " + new Gson().toJson(requet));


        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.spamcall(requet).enqueue(new Callback<List<GetSpamcallResponseJson>>() {
            @Override
            public void onResponse(Call<List<GetSpamcallResponseJson>> call, Response<List<GetSpamcallResponseJson>> response) {
                Log.i("CCpWalletCCpWalletSpam", "nameValuePairs code  = " + response.code());
                Log.i("CCpWalletCCpWalletSpam", "nameValuePairs body  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    for(int i=0;i<response.body().size();i++){
                        if(response.body().get(i).Status.equalsIgnoreCase("1")){
                            String type = "calllog";
                            String number = response.body().get(i).MobileNo;
                            String name = response.body().get(i).Details.get(0).Name;
                            String Spamcount = response.body().get(i).Details.get(0).SpamCount;
                            Log.i("CCpWalletCCpWalletSpam", "nameValuePairs 1 mobile  = " + response.body().get(i).MobileNo);
                            String callType = getcallType.get(i);
                            long date = getdate.get(i);
                            menuItem = new SpamMenuItem(type, "0", "0","0", date, number, name,"NA",callType,Spamcount);
                            recyclerViewItems.add(menuItem);

                        }

                    }
                    progressBar.setVisibility(View.GONE);
                    if(recyclerViewItems.size()==0){

                        ivno.setVisibility(View.VISIBLE);
                    }else {
                        ivno.setVisibility(View.GONE);
                    }
                    addBannerAds();
                    loadBannerAds();


//
                    adapter = new SpamAdapter(getActivity(),
                            recyclerViewItems);
                    mRecyclerView.setAdapter(adapter);


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
            public void onFailure(Call<List<GetSpamcallResponseJson>> call, Throwable t) {
                t.printStackTrace();
                Log.i("CCpWalletCCpWalletSpam", "error  = " + t.getMessage());
                Log.e("System error:", t.getLocalizedMessage());

            }
        });
    }
}