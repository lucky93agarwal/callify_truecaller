package com.gpslab.kaun.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.gpslab.kaun.CallLogUtils;
import com.gpslab.kaun.DB.FirstTableData;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.Login.LoginSecActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.adapter.CallLogAdapter;
import com.gpslab.kaun.adapter.ContectAPIAdapter;
import com.gpslab.kaun.adapter.ContectAdapter;
import com.gpslab.kaun.model.CallLogtwoinfoSearch;
import com.gpslab.kaun.model.CallLogtwoinfor;
import com.gpslab.kaun.model.Chat;
import com.gpslab.kaun.model.GetApiSearchData;
import com.gpslab.kaun.model.GetContectData;
import com.gpslab.kaun.model.MenuItem;
import com.gpslab.kaun.privacyActivity;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit_model.AddUserResponseJson;
import com.gpslab.kaun.retrofit_model.SearchContactRequestJson;
import com.gpslab.kaun.model.SearchContactMainResponseJson;
import com.gpslab.kaun.retrofit_model.SearchContactResponseJson;
import com.gpslab.kaun.upgrade_to_premium.NewUpgradAdapter;
import com.gpslab.kaun.upgrade_to_premium.UpgradePremiumActivity;
import com.gpslab.kaun.upgrade_to_premium.UpgradePremiumDetailsActivity;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    public List<GetContectData> productList = new ArrayList<>();
    public GetContectData getbcdata;
    public ContectAdapter adapter;
    public RecyclerView mRecyclerView;

    public RecyclerView.LayoutManager mLayoutManager;

    String resTxt = null;


    public List<CallLogtwoinfoSearch> productList_log = new ArrayList<>();
    public CallLogtwoinfoSearch getbcdata_log;
    public CallLogAdapter adapter_log;
    public RecyclerView mRecyclerView_log;
    public RecyclerView.LayoutManager mLayoutManager_log;


    public RecyclerView mRecyclerView_api;
    public RecyclerView.LayoutManager mLayoutManager_api;
    public List<GetApiSearchData> productList_api = new ArrayList<>();
    public GetApiSearchData getbcdata_api;
    public ContectAPIAdapter adapter_api;
    private RewardedAd mRewardedAd;
    private final static String TAG = "CheckAdsNew";

    public FloatingActionButton floatingActionBtn;
    MyDbHandler myDbHandler = Temp.getMyDbHandler();
    public EditText etsearch;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;

    public LinearLayout linearone, lineartwo;
    public TextView tvtotalcontact, tvmessage, tvcontactsearchtv, tvcallifymore, tvconversationsmore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        tvcallifymore = (TextView) findViewById(R.id.callifymore);
        tvcontactsearchtv = (TextView) findViewById(R.id.contactsearchtv);
        tvconversationsmore = (TextView) findViewById(R.id.conversationsmore);


        tvconversationsmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, SearchCallLogMoreActivity.class);
                intent.putExtra("searchword", etsearch.getText().toString());
                startActivity(intent);
            }
        });
        tvcontactsearchtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, SearchMoreActivity.class);
                intent.putExtra("searchword", etsearch.getText().toString());
                startActivity(intent);

            }
        });
        tvcallifymore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, SearchCallifyMoreActivity.class);
                intent.putExtra("searchword", etsearch.getText().toString());
                startActivity(intent);
            }
        });


        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        tvtotalcontact = (TextView) findViewById(R.id.totalcontacttv);

        tvtotalcontact.setText(" " + String.valueOf(sharedPreferences.getString("total_contact", "0")) + " ");
        tvmessage = (TextView) findViewById(R.id.totalmessage);
        tvmessage.setText(" " + String.valueOf(sharedPreferences.getString("total_msg", "0")) + " ");

        linearone = (LinearLayout) findViewById(R.id.linearone);
        linearone.setVisibility(View.VISIBLE);
        lineartwo = (LinearLayout) findViewById(R.id.lineartwo);
        lineartwo.setVisibility(View.GONE);


        mRecyclerView_log = (RecyclerView) findViewById(R.id.datarecyclerviewsss);
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback(){
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "onAdFailedToLoad");
                    }
                });

        mRecyclerView_api = (RecyclerView) findViewById(R.id.datariews);

        mRecyclerView = (RecyclerView) findViewById(R.id.datarecyclerview);


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom();
            }
        });

        etsearch = (EditText) findViewById(R.id.searchet);
        etsearch.requestFocus();
        etsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d("CheckDataSearch", "Search id = " + actionId);
                    Log.d("CheckDataSearch", "Search IME_ACTION_SEARCH = " + EditorInfo.IME_ACTION_SEARCH);

                    hideKeybaord(v);
                    handled = true;
                }
                return handled;
            }
        });
        etsearch.setFocusable(true);
        UIUtil.showKeyboard(this, etsearch);
        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("fragmentonefSearch", "result = = ");
                if (s.length() > 1) {
                    filter(s.toString());
                    filter_log(s.toString());

                    linearone.setVisibility(View.VISIBLE);
                    lineartwo.setVisibility(View.GONE);
                } else {
                    linearone.setVisibility(View.GONE);
                    lineartwo.setVisibility(View.VISIBLE);
                }
                if (s.length() > 3) {

                    if(isNumeric(s.toString())){
                        if (s.length() == 10){
                            SearchMobile = "91"+s.toString();
                            SearchText ="";
                            retrofit();


                        }else  if (s.length() == 12){
                            SearchMobile = s.toString();
                            SearchText ="";
                            retrofit();
                        }
                        Log.i("checkthenum","response = 1 ");
                    }
                    else
                        {
                            SearchText = s.toString();
                            SearchMobile = "";
                            retrofit();
                            Log.i("checkthenum","response = 2 ");
                    }

                }

            }
        });
        try {
            getViewRecyclerViewShowData();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        getViewRecyclerViewShowDataCallLog();
    }
    public String SearchText,SearchMobile;
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private void onScrolledToBottom() {
        ArrayList<FirstTableData> arrayList = myDbHandler.viewUser();
        if (productList.size() < arrayList.size()) {
            int x, y;
            if ((arrayList.size() - productList.size()) >= 50) {
                x = productList.size();
                y = x + 50;
            } else {
                x = productList.size();
                y = x + arrayList.size() - productList.size();
            }
            for (int i = x; i < y; i++) {
//                songMainList.add(songAllList.get(i));

                getbcdata = new GetContectData();
                getbcdata.setId(arrayList.get(i).getId());
                getbcdata.setName(arrayList.get(i).getName());
                getbcdata.setNumber_two(arrayList.get(i).getMobiletwo());
                getbcdata.setEmail(arrayList.get(i).getEmail());
                getbcdata.setNumber(arrayList.get(i).getMobileone());
                productList.add(getbcdata);
            }
            adapter.notifyDataSetChanged();
        }

    }

    private void filter(String text) {
        ArrayList<GetContectData> filteredList = new ArrayList<>();

        for (GetContectData item : productList) {
            String Name = item.getName();
            String Number = item.getNumber();
            if (Name.toLowerCase().contains(text.toLowerCase()) || Number.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    private void filter_log(final String text) {
        ArrayList<CallLogtwoinfoSearch> filteredList = new ArrayList<>();
        for (CallLogtwoinfoSearch item : productList_log) {
            String Name = item.getName();
            String Number = item.getNumber();
            if (TextUtils.isEmpty(Name)) {
                Name = "NA";
            }
            if (Name.toLowerCase().contains(text.toLowerCase()) || Number.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter_log.filterList(filteredList);
    }

    private void getViewRecyclerViewShowData() throws UnsupportedEncodingException {
        // सबसे पहले Team class का जो getMyDbHandler method को call करेगे।
        productList.clear();
        MyDbHandler myDbHandler = Temp.getMyDbHandler();
        ArrayList<FirstTableData> arrayList = myDbHandler.viewUser();
        Log.d("lsdjflsjdfljsdf", "name = " + arrayList.size());
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                getbcdata = new GetContectData();
                getbcdata.setId(arrayList.get(i).getId());
                Log.d("lsdjflsjdfljsdf", "name 2 = " + arrayList.get(i).getName());
                byte[] data = Base64.decode(arrayList.get(i).getName(), Base64.DEFAULT);
                String Name = new String(data, "UTF-8");

                byte[] datanew = Base64.decode(arrayList.get(i).getMobileone(), Base64.DEFAULT);
                String getMobileone = new String(datanew, "UTF-8");
                getbcdata.setName(Name);
                getbcdata.setNumber_two(arrayList.get(i).getMobiletwo());
                getbcdata.setEmail(arrayList.get(i).getEmail());
                getbcdata.setNumber(getMobileone);
                productList.add(getbcdata);
            }


            mRecyclerView.setHasFixedSize(true);
            adapter = new ContectAdapter(this, productList);
            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


    }

    String NewDate = "";

    private void getViewRecyclerViewShowDataCallLog() {
        productList_log.clear();
        CallLogUtils callLogUtils = CallLogUtils.getInstance(this);
        if (callLogUtils.readCallLogs().size() > 0) {
            for (int i = 0; i < callLogUtils.readCallLogs().size(); i++) {
                getbcdata_log = new CallLogtwoinfoSearch();
                getbcdata_log.setName(callLogUtils.readCallLogs().get(i).getName());
                getbcdata_log.setNumber(callLogUtils.readCallLogs().get(i).getNumber());

//                getbcdata_log.setDuration(callLogUtils.readCallLogs().get(i).getDuration());



                long date = callLogUtils.readCallLogs().get(i).getDate();
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

                getbcdata_log.setDate(NewDate);

                productList_log.add(getbcdata_log);
            }
            mRecyclerView_log.setHasFixedSize(true);
            adapter_log = new CallLogAdapter(this, productList_log);
            mLayoutManager_log = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView_log.setLayoutManager(mLayoutManager_log);
            mRecyclerView_log.setAdapter(adapter_log);
            adapter_log.notifyDataSetChanged();
        }


    }


    public SearchContactRequestJson request = new SearchContactRequestJson();

    private void retrofit() {


        request.username = SearchText;
        request.mobileno = SearchMobile;

        Log.i("CCpWalletCCpWalletSearch", "nameValuePairs Signup  = " + new Gson().toJson(request));

        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.search_contact(request).enqueue(new Callback<SearchContactResponseJson>() {
            @Override
            public void onResponse(Call<SearchContactResponseJson> call, Response<SearchContactResponseJson> response) {
                Log.i("CCpWalletCCpWalletSearch", "nameValuePairs code  = " + response.code());
                if (response.isSuccessful()) {

                    Log.i("CCpWalletCCpWalletSearch", "nameValuePairs code  = " + new Gson().toJson(response.body().response));
                    if (productList_api.size() > 0) {
                        productList_api.clear();
                    }
                    if(response.body().status.equalsIgnoreCase("1")){

                        for (int i = 0; i < response.body().response.get(0).size(); i++) {
                            GetApiSearchData getApiSearchData = new GetApiSearchData();
                            getApiSearchData.setName(response.body().response.get(0).get(i).Name);
                            getApiSearchData.setNumber(response.body().response.get(0).get(i).Number);
                            getApiSearchData.setEmail(response.body().response.get(0).get(i).Email);
                            productList_api.add(getApiSearchData);
                        }
//
//
                        mRecyclerView_api.setHasFixedSize(true);
                        adapter_api = new ContectAPIAdapter(SearchActivity.this, productList_api, new ContectAPIAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(GetApiSearchData item) {

                                if (mRewardedAd != null) {
                                    Activity activityContext = SearchActivity.this;
                                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                        @Override
                                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                            // Handle the reward.
                                            Log.d(TAG, "The user earned the reward.");
                                            int rewardAmount = rewardItem.getAmount();
                                            String rewardType = rewardItem.getType();
                                            NewData();
                                            itemsx = item;
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                                }

                            }
                        });
                        mLayoutManager_api = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
                        mRecyclerView_api.setLayoutManager(mLayoutManager_api);
                        mRecyclerView_api.setAdapter(adapter_api);
                        adapter_api.notifyDataSetChanged();
                    }else {
                        Toast.makeText(SearchActivity.this, "Result not found", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SearchActivity.this, "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }



            @Override
            public void onFailure(Call<SearchContactResponseJson> call, Throwable t) {
                t.printStackTrace();

                Log.i("CCpWalletCCpWalletSearch","error = "+t.getMessage());
                Log.e("System error:", t.getLocalizedMessage());

            }
        });
    }

    public GetApiSearchData itemsx = new GetApiSearchData();
    private void NewData() {
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.");
                mRewardedAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.d(TAG, "Ad failed to show.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Don't forget to set the ad reference to null so you
                // don't show the ad a second time.
                Log.d(TAG, "Ad was dismissed.");
                Intent inten = new Intent(SearchActivity.this, UpgradePremiumDetailsActivity.class);
                inten.putExtra("name",itemsx.getName());
                inten.putExtra("number",itemsx.getNumber());
                inten.putExtra("img","");
                inten.putExtra("duration",String.valueOf("100"));
                startActivity(inten);
            }
        });
    }

//    private void SendDataToServer(final String datasss) {
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//                nameValuePairs.add(new BasicNameValuePair("string", datasss.toString()));
//
//                Log.d("fragmentonefSearch", "result = = " + nameValuePairs);
//
//
//                try {
//
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "search_contact/");
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
//                Log.d("fragmentonefSearch", "result = = " + result);
//                if (result.isEmpty()) {
//
//                } else {
//                    adapter.notifyDataSetChanged();
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONArray response = jsonObject.getJSONArray("response");
//                        for (int i = 0; i < response.length(); i++) {
//                            GetApiSearchData getApiSearchData = new GetApiSearchData();
//                            getApiSearchData.setName(response.getJSONObject(i).getString("name"));
//                            getApiSearchData.setNumber(response.getJSONObject(i).getString("mobile"));
//                            getApiSearchData.setEmail(response.getJSONObject(i).getString("email"));
//                            productList_api.add(getApiSearchData);
//                        }
//
//
//                        mRecyclerView_api.setHasFixedSize(true);
//                        adapter_api = new ContectAPIAdapter(SearchActivity.this, productList_api);
//                        mLayoutManager_api = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
//                        mRecyclerView_api.setLayoutManager(mLayoutManager_api);
//                        mRecyclerView_api.setAdapter(adapter_api);
//                        adapter_api.notifyDataSetChanged();
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