package com.gpslab.kaun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.github.karczews.rxbroadcastreceiver.RxBroadcastReceivers;
import com.google.gson.Gson;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit_model.VerifyUserRequestJson;
import com.gpslab.kaun.retrofit_model.VerifyUserResponseJson;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneStateListener extends BroadcastReceiver {
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;
    public SharedPreferences sharedPreferencesnew;

    public String resTxt = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferencesnew = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
                Log.d("WalletWalletfgdflucky", "CALL_STATE_RINGING 2 ");

            } else {
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

//                Log.d("WalletWalletfgdflucky", "NUMBER  ==   ==  ==  " + number);


                int state = 0;
                if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    state = TelephonyManager.CALL_STATE_IDLE;
//                    Log.d("WalletWalletfgdflucky", "CALL_STATE_IDLE");
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
//                    Log.d("WalletWalletfgdflucky", "CALL_STATE_OFFHOOK");
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    state = TelephonyManager.CALL_STATE_RINGING;
                    Log.d("WalletWalletfgdflucky", "CALL_STATE_RINGING" + savedNumber);
                }
//                Log.d("WalletWalletfgdflucky", "state  ==   ==  ==  " + state);
                /// dile call
                if (state == 2) {
//                    Log.d("WalletWalletfgdflucky", "state  ==   ==  ==  " + state+"      NUMBER  ==   ==  ==  " + number);
                    if (number != null) {
//                        DildCallNumber = number;
                        Log.i("WalletWalletfgdflucky", "onCallStateChanged = =  " + number + "  State = = =  " + state+" context ===  == == "+context);
                //        retrofit_new(number,context);
//                        SendServer(number, context);
//                        SendDataToServer(DildCallNumber,context);
                    }
                }

                Toast.makeText(context, "CALL_STATE_RINGING Lucky", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public VerifyUserRequestJson request = new VerifyUserRequestJson();
    private void retrofit_new(final String numbers,final Context mContext) {
        String number = numbers.substring(1,numbers.length());
        request.mobile = number;

        request.token = "NA";

        Log.i("WalletWalletfgdflucky","request = "+new Gson().toJson(request));


        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.verify_user(request).enqueue(new Callback<VerifyUserResponseJson>() {
            @Override
            public void onResponse(Call<VerifyUserResponseJson> call, Response<VerifyUserResponseJson> response) {
                Log.d("CCpWalletCCpWallet", "nameValuePairs code  = " + response.code());
                Log.i("WalletWalletfgdflucky","response = "+new Gson().toJson(response.body()));
                if (response.isSuccessful()) {




                    String results = response.body().response.get(0).result;
                    if(results.equalsIgnoreCase("0")){




                        final Intent intent = new Intent(mContext, MyCustomDialog.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("contact_name",savedNumber);
                        intent.putExtra("phone_no",response.body().response.get(0).data.get(0).mobile);
                        intent.putExtra("img",response.body().response.get(0).data.get(0).profile_image);
                        intent.putExtra("is_spam",response.body().response.get(0).data.get(0).is_spam);
                        intent.putExtra("spam_count",response.body().response.get(0).data.get(0).spanCount);
                        intent.putExtra("type","Ringing");
                        intent.putExtra("tag","tag");
                        Log.d("WalletlsjWalletsavedNumber", "image = = " + response.body().response.get(0).data.get(0).profile_image);
                        mContext.startActivity(intent);
                    }else if(results.equalsIgnoreCase("1")){

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
                    Toast.makeText(mContext, "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<VerifyUserResponseJson> call, Throwable t) {
                t.printStackTrace();

                Log.e("System error:", t.getLocalizedMessage());

            }
        });
    }

    // send message
    private void SendServer(final String datasss, final Context context) {
        Log.d("WalletWalletfgdflucky", "receiver = = " + datasss);
        Log.d("WalletWalletfgdflucky", "sender = = " + sharedPreferencesnew.getString("mobile", ""));
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {



                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("receiver", datasss));
                nameValuePairs.add(new BasicNameValuePair("sender", sharedPreferencesnew.getString("mobile", "")));


                Log.d("WalletWalletfgdflucky", "fname = = " + nameValuePairs);


//                JSONObject jsonObjectlucky = new JSONObject();
                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "calling/");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    resTxt = EntityUtils.toString(entity);

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    Log.d("WalletWalletfgdflucky",  " ClientProtocolException message = = " + e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("WalletWalletfgdflucky",  " IOException message = = " + e.getMessage());
                }
                return resTxt;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                final String res_result;
                Log.d("WalletWalletfgdflucky", "result = = " + result);
//                if(result.isEmpty()){
//
//                }else {
                Log.d("WalletlsjWalletsavedNumber", "result 1 = = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    if (jsonArray.length() == 1) {
                        res_result = jsonArray.getJSONObject(0).getString("result");
                        Log.d("WalletlsjWalletsavedNumber", "res_result = = " + res_result);
                    } else {
                        res_result = jsonArray.getJSONObject(1).getString("result");
                    }
                    Log.d("WalletlsjWalletsavedNumber", "result = = " + jsonArray.getJSONObject(0).getString("result"));

//                        jsonArray = jsonObject1.getJSONArray("response");

                    if (res_result.equalsIgnoreCase("0")) {
                        Log.d("WalletlsjWalletsavedNumber", "name = = " + jsonArray.getJSONObject(0).getString("name"));
                        Log.d("WalletlsjWalletsavedNumber", "phone_no = = " + jsonArray.getJSONObject(0).getString("name"));
                        Log.d("WalletlsjWalletsavedNumber", "profile_image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
                        final Intent intent = new Intent(context, MyCustomDialog.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("contact_name", savedNumber);
                        intent.putExtra("phone_no", jsonArray.getJSONObject(0).getString("name"));
                        intent.putExtra("img", jsonArray.getJSONObject(0).getString("profile_image"));
                        intent.putExtra("is_spam", jsonArray.getJSONObject(0).getString("is_spam"));
                        intent.putExtra("spam_count", jsonArray.getJSONObject(0).getString("spamCount"));
                        intent.putExtra("type", "Ringing");
                        intent.putExtra("tag", "tag");
                        Log.d("WalletlsjWalletsavedNumber", "image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
                        context.startActivity(intent);
                    }
//
                } catch (JSONException e) {
                    e.getMessage();
                }
//                }


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(datasss);
    }

}
