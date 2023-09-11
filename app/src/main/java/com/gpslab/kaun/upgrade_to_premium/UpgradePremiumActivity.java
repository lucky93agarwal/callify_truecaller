package com.gpslab.kaun.upgrade_to_premium;

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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.gson.Gson;
import com.gpslab.kaun.Contact.ContactListActivity;
import com.gpslab.kaun.DB.GetChatContactList;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.adapter.ChatsAdapter;
import com.gpslab.kaun.model.Chat;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit_model.AppContactsContactResponseJson;
import com.gpslab.kaun.retrofit_model.AppContactsRequestJsn;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpgradePremiumActivity extends AppCompatActivity {
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    private ArrayList<Chat> chats;
    private RecyclerView rvChats;
    private NewUpgradAdapter adapter;
    public String resTxt = "";
    public ImageView ivbackiv, imgclick;
    private RewardedAd mRewardedAd;
    private final static String TAG = "CheckAdsNew";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_premium);
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        NewData();
                        Log.d(TAG, "onAdFailedToLoad");
                    }

                });


        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        initialize();
        onClick();
        getData();
        retrofit();
//        SendDataToServer("");
    }

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
                Intent inten = new Intent(UpgradePremiumActivity.this, UpgradePremiumDetailsActivity.class);
                inten.putExtra("name", itemsx.getName());
                inten.putExtra("number", itemsx.getId());
                inten.putExtra("img", itemsx.getImage());
                inten.putExtra("duration", String.valueOf("100"));
                startActivity(inten);
            }
        });
    }

    public Chat itemsx = new Chat();

    private void getData() {
        chats.add(new Chat("", "Lucky Agarwal", "1", "", "919450345636"));
        adapter = new NewUpgradAdapter(UpgradePremiumActivity.this, chats, new NewUpgradAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chat item) {
                if (mRewardedAd != null) {
                    itemsx = item;
                    Activity activityContext = UpgradePremiumActivity.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Log.d(TAG, "The user earned the reward.");
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();


                        }
                    });
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }

            }
        });
        rvChats.setLayoutManager(new LinearLayoutManager(UpgradePremiumActivity.this));
        rvChats.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void onClick() {
        ivbackiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();


            }
        });
    }

    private void initialize() {

        chats = new ArrayList<>();
        rvChats = (RecyclerView) findViewById(R.id.rvChats);
        ivbackiv = (ImageView) findViewById(R.id.backiv);

    }

    public AppContactsRequestJsn request = new AppContactsRequestJsn();

    public void retrofit() {
        request.mobile = sharedPreferences.getString("mobile", "");


        Log.i("checkcontactlisr","request = "+new Gson().toJson(request));
        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.get_app_contacts(request).enqueue(new Callback<AppContactsContactResponseJson>() {
            @Override
            public void onResponse(Call<AppContactsContactResponseJson> call, Response<AppContactsContactResponseJson> response) {
                Log.d("CCpWalletCCpWallet", "nameValuePairs code  = " + response.code());
                Log.i("checkcontactlisr","request = "+new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    String result = response.body().status;
                    if (result.equalsIgnoreCase("1")) {
                        if (chats.size() > 0) {
                            chats.clear();
                        }
                        Log.i("checkcontactlisr","size = "+response.body().response.get(0).size());

                        for (int i = 0; i < response.body().response.get(0).size(); i++) {

                            chats.add(new Chat(response.body().response.get(0).get(i).profile_image, response.body().response.get(0).get(i).fname+" "+response.body().response.get(0).get(i).lname, response.body().response.get(0).get(i).status, "", response.body().response.get(0).get(i).mobile));
                        }


                        adapter = new NewUpgradAdapter(UpgradePremiumActivity.this, chats, new NewUpgradAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Chat item) {
                                if (mRewardedAd != null) {
                                    Activity activityContext = UpgradePremiumActivity.this;
                                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                        @Override
                                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                            // Handle the reward.
                                            Log.d(TAG, "The user earned the reward.");
                                            int rewardAmount = rewardItem.getAmount();
                                            String rewardType = rewardItem.getType();
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                                }
                                Intent inten = new Intent(UpgradePremiumActivity.this, UpgradePremiumDetailsActivity.class);
                                inten.putExtra("name", item.getName());
                                inten.putExtra("number", item.getId());
                                inten.putExtra("img", item.getImage());
                                inten.putExtra("duration", String.valueOf("100"));
                                startActivity(inten);
                            }
                        });
                        rvChats.setLayoutManager(new LinearLayoutManager(UpgradePremiumActivity.this));
                        rvChats.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

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
                    Toast.makeText(UpgradePremiumActivity.this, "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<AppContactsContactResponseJson> call, Throwable t) {
                t.printStackTrace();

                Log.e("System error:", t.getLocalizedMessage());

            }
        });
    }

//    private void SendDataToServer(final String datasss) {
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//                Log.d("Contactlist5lucky", "fname = = ");
//
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//                nameValuePairs.add(new BasicNameValuePair("mobile", sharedPreferences.getString("mobile", "")));
//                Log.d("Contactlist5luckyssdf", "fname = = " + nameValuePairs);
//
//                try {
//
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPosts = new HttpPost(ResURls.baseURLChat + "get_app_contacts");
//                    Log.d("Contactlist5luckyssdf", "URL = = " + ResURls.baseURLChat + "get_app_contacts");
//                    httpPosts.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPosts);
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
//                Log.d("Contactlist5luckyssdf", "result = = " + result);
//
//                if (result.isEmpty()) {
//
//                } else {
//
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String status = jsonObject.getString("status");
//                        Log.d("Contactlist5lucky", "statuse = " + status);
//                        if (status.equalsIgnoreCase("0")) {
//                            JSONArray response = jsonObject.getJSONArray("response");
//
//                            Log.d("Contactlist5lucky", "calltype 2 response = " + response.length());
//
//                            if (chats.size() > 0) {
//                                chats.clear();
//                            }
//
//
//                            for (int i = 0; i < response.length(); i++) {
////
//
//
//                                Log.d("Contactlist5lucky", "fname = = " + response.getJSONObject(i).getString("name"));
//                                chats.add(new Chat(response.getJSONObject(i).getString("image"), response.getJSONObject(i).getString("name"), response.getJSONObject(i).getString("status"), "", response.getJSONObject(i).getString("mobile")));
//                            }
//
//                            Log.d("Contactlist5lucky", "size = = " + chats.size());
//                        }
//
//                        adapter = new NewUpgradAdapter(UpgradePremiumActivity.this, chats, new NewUpgradAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(Chat item) {
//                                if (mRewardedAd != null) {
//                                    Activity activityContext = UpgradePremiumActivity.this;
//                                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
//                                        @Override
//                                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
//                                            // Handle the reward.
//                                            Log.d(TAG, "The user earned the reward.");
//                                            int rewardAmount = rewardItem.getAmount();
//                                            String rewardType = rewardItem.getType();
//                                        }
//                                    });
//                                } else {
//                                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
//                                }
//                                Intent inten = new Intent(UpgradePremiumActivity.this, UpgradePremiumDetailsActivity.class);
//                                inten.putExtra("name", item.getName());
//                                inten.putExtra("number", item.getId());
//                                inten.putExtra("img", item.getImage());
//                                inten.putExtra("duration", String.valueOf("100"));
//                                startActivity(inten);
//                            }
//                        });
//                        rvChats.setLayoutManager(new LinearLayoutManager(UpgradePremiumActivity.this));
//                        rvChats.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
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