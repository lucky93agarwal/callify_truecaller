package com.gpslab.kaun.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gpslab.kaun.Contact.ContactViewModel;
import com.gpslab.kaun.Contact.NewContact;
import com.gpslab.kaun.DB.FirstTableData;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.MyCustomDialog;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.StartingPopupActivity;
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

public class MyService extends Service {
    public MyService() {
    }
    public SharedPreferences sharedPreferencesnew;
    public String resTxt = null;
    public MyDbHandler myDbHandler;
    public boolean check = false;
    private ContactViewModel contactViewModel;
    private List<NewContact> contacts  = new ArrayList<>();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        onTaskRemoved(intent);
        /*Toast.makeText(getApplicationContext(),"This is a Service running in Background",
                Toast.LENGTH_SHORT).show();*/
        //startActivity(new Intent(MyService.this,ApprovalDashboardAcitivity.class));
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferencesnew = getSharedPreferences("data", Context.MODE_PRIVATE);

        myDbHandler = new MyDbHandler(MyService.this,"userbd",null,1);

        Temp.setMyDbHandler(myDbHandler);
        myDbHandler = Temp.getMyDbHandler();

        IntentFilter CallFilter = new IntentFilter();
        CallFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        CallFilter.addAction("android.intent.action.PHONE_STATE");
        this.registerReceiver(mCallBroadcastReceiver, CallFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mCallBroadcastReceiver);
    }

    private BroadcastReceiver mCallBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String PhoneNumber = "UNKNOWN";
            Log.d("RECEIVER :  ","IS UP AGAIN....");



            try
            {
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                if(state == null)
                {
                    PhoneNumber = "UNKNOWN";
                }
                else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING))
                {
                    PhoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                    contactViewModel = new ContactViewModel(context.getApplicationContext());
                    contacts = contactViewModel.getContacts();

                    String getNumber = "00000000000";

                    for(int i=0;i<contacts.size();i++){
                       String number = contacts.get(i).getPhoneNumber();



                        if(number.length()>9){
                            String Number_new = number.substring(number.length() - 10);

                            if(getNumber.equalsIgnoreCase(Number_new)){
                                check = true;
                            }
                        }

                    }

                    if(!check){

                    //    retrofit(PhoneNumber,context);
                        Log.d("WalletlsjWalletsavedNumber","savedNumber = "+getNumber);


                    }else {

                    }


                    Log.d("RECEIVER : ","Incoming number : "+PhoneNumber);
                }
                if(intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL"))
                {
                    PhoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
           //        retrofit_new(PhoneNumber,context);

                    Log.d("RECEIVER : ","Outgoing number : "+PhoneNumber);
                }

                Toast.makeText(context, "sdfsdfsfdfsfds  ==  ==  ==    "+PhoneNumber, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e("RECEIVER : ", "Exception is : ", e);
            }
        }
    };

    public VerifyUserRequestJson request = new VerifyUserRequestJson();

    private void retrofit(final String numbers,final Context mContext) {
        String number = numbers.substring(1,numbers.length());
        request.mobile = number;

        request.token = "NA";

        Log.i("WalletWalletfgdflucky","4 request = "+new Gson().toJson(request));

        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.verify_user(request).enqueue(new Callback<VerifyUserResponseJson>() {
            @Override
            public void onResponse(Call<VerifyUserResponseJson> call, Response<VerifyUserResponseJson> response) {
                Log.d("CCpWalletCCpWallet", "nameValuePairs code  = " + response.code());
                Log.i("WalletWalletfgdflucky","4 response = "+new Gson().toJson(response.body()));
                if (response.isSuccessful()) {




                    String results = response.body().response.get(0).result;
                    if(results.equalsIgnoreCase("0")){


                        final Intent intent = new Intent(mContext, StartingPopupActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("contact_name",numbers);
                        intent.putExtra("phone_no",response.body().response.get(0).data.get(0).mobile);
                        intent.putExtra("img",response.body().response.get(0).data.get(0).profile_image);
                        intent.putExtra("is_spam",response.body().response.get(0).data.get(0).is_spam);
                        intent.putExtra("spam_count",response.body().response.get(0).data.get(0).spanCount);
                        intent.putExtra("type","Ringing");
                        intent.putExtra("tag","tag");

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


    private void retrofit_new(final String numbers,final Context mContext) {
        String number = numbers.substring(1,numbers.length());
        request.mobile = number;

        request.token = "NA";
        Log.i("WalletWalletfgdflucky","5 request = "+new Gson().toJson(request));


        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.verify_user(request).enqueue(new Callback<VerifyUserResponseJson>() {
            @Override
            public void onResponse(Call<VerifyUserResponseJson> call, Response<VerifyUserResponseJson> response) {
                Log.d("CCpWalletCCpWallet", "nameValuePairs code  = " + response.code());
                Log.i("WalletWalletfgdflucky","5 response = "+new Gson().toJson(response.body()));
                if (response.isSuccessful()) {




                    String results = response.body().response.get(0).result;
                    if(results.equalsIgnoreCase("0")){

                        final Intent intent = new Intent(mContext, MyCustomDialog.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("contact_name",numbers);
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
//    private void SendServer(final String datasss, final Context context) {
//        Log.d("WalletWadfsfgdflucky", "receiver = = " + datasss);
//        Log.d("WalletWadfsfgdflucky", "sender = = " + sharedPreferencesnew.getString("mobile", ""));
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("receiver", datasss));
//                nameValuePairs.add(new BasicNameValuePair("sender", sharedPreferencesnew.getString("mobile", "")));
//
//
//                Log.d("WalletWalletfgdflucky", "fname = = " + nameValuePairs);
//
//
////                JSONObject jsonObjectlucky = new JSONObject();
//                try {
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "calling/");
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
//                    e.printStackTrace();
//                    Log.d("WalletWadfsfgdflucky",  " ClientProtocolException message = = " + e.getMessage());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.d("WalletWadfsfgdflucky",  " IOException message = = " + e.getMessage());
//                }
//                return resTxt;
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//                final String res_result;
//                Log.d("WalletWadfsfgdflucky", "result 787   = = " + result);
////                if(result.isEmpty()){
////
////                }else {
//                Log.d("WalletlsjWalletsavedNumber", "result 1 = = " + result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONArray jsonArray = jsonObject.getJSONArray("response");
//                    if (jsonArray.length() == 1) {
//                        res_result = jsonArray.getJSONObject(0).getString("result");
//                        Log.d("WalletlsjWalletsavedNumber", "res_result = = " + res_result);
//                    } else {
//                        res_result = jsonArray.getJSONObject(1).getString("result");
//                    }
//                    Log.d("WalletlsjWalletsavedNumber", "result = = " + jsonArray.getJSONObject(0).getString("result"));
//
////                        jsonArray = jsonObject1.getJSONArray("response");
//
//                    if (res_result.equalsIgnoreCase("0")) {
//                        Log.d("WalletlsjWalletsavedNumber", "name = = " + jsonArray.getJSONObject(0).getString("name"));
//                        Log.d("WalletlsjWalletsavedNumber", "phone_no = = " + jsonArray.getJSONObject(0).getString("name"));
//                        Log.d("WalletlsjWalletsavedNumber", "profile_image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
//                        final Intent intent = new Intent(context, StartingPopupActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("contact_name", datasss);
//                        intent.putExtra("phone_no", jsonArray.getJSONObject(0).getString("name"));
//                        intent.putExtra("img", jsonArray.getJSONObject(0).getString("profile_image"));
//                        intent.putExtra("is_spam", jsonArray.getJSONObject(0).getString("is_spam"));
//                        intent.putExtra("spam_count", jsonArray.getJSONObject(0).getString("spamCount"));
//                        intent.putExtra("type", "Ringing");
//                        intent.putExtra("tag", "tag");
//                        Log.d("WalletlsjWalletsavedNumber", "image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
//                        context.startActivity(intent);
//                    }
////
//                } catch (JSONException e) {
//                    e.getMessage();
//                }
////                }
//
//
//            }
//        }
//        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
//        sendPostReqAsyncTask.execute(datasss);
//    }
//
//
//
//
//    private void SendServer_new(final String datasss, final Context context) {
//        Log.d("WalletWadfsfgdflucky", "receiver = = " + datasss);
//        Log.d("WalletWadfsfgdflucky", "sender = = " + sharedPreferencesnew.getString("mobile", ""));
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("receiver", datasss));
//                nameValuePairs.add(new BasicNameValuePair("sender", sharedPreferencesnew.getString("mobile", "")));
//
//
//                Log.d("WalletWalletfgdflucky", "fname = = " + nameValuePairs);
//
//
////                JSONObject jsonObjectlucky = new JSONObject();
//                try {
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "calling/");
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
//                    e.printStackTrace();
//                    Log.d("WalletWadfsfgdflucky",  " ClientProtocolException message = = " + e.getMessage());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.d("WalletWadfsfgdflucky",  " IOException message = = " + e.getMessage());
//                }
//                return resTxt;
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//                final String res_result;
//                Log.d("WalletWadfsfgdflucky", "result 787   = = " + result);
////                if(result.isEmpty()){
////
////                }else {
//                Log.d("WalletlsjWalletsavedNumber", "result 1 = = " + result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONArray jsonArray = jsonObject.getJSONArray("response");
//                    if (jsonArray.length() == 1) {
//                        res_result = jsonArray.getJSONObject(0).getString("result");
//                        Log.d("WalletlsjWalletsavedNumber", "res_result = = " + res_result);
//                    } else {
//                        res_result = jsonArray.getJSONObject(1).getString("result");
//                    }
//                    Log.d("WalletlsjWalletsavedNumber", "result = = " + jsonArray.getJSONObject(0).getString("result"));
//
////                        jsonArray = jsonObject1.getJSONArray("response");
//
//                    if (res_result.equalsIgnoreCase("0")) {
//                        Log.d("WalletlsjWalletsavedNumber", "name = = " + jsonArray.getJSONObject(0).getString("name"));
//                        Log.d("WalletlsjWalletsavedNumber", "phone_no = = " + jsonArray.getJSONObject(0).getString("name"));
//                        Log.d("WalletlsjWalletsavedNumber", "profile_image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
//                        final Intent intent = new Intent(context, MyCustomDialog.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("contact_name", datasss);
//                        intent.putExtra("phone_no", jsonArray.getJSONObject(0).getString("name"));
//                        intent.putExtra("img", jsonArray.getJSONObject(0).getString("profile_image"));
//                        intent.putExtra("is_spam", jsonArray.getJSONObject(0).getString("is_spam"));
//                        intent.putExtra("spam_count", jsonArray.getJSONObject(0).getString("spamCount"));
//                        intent.putExtra("type", "Ringing");
//                        intent.putExtra("tag", "tag");
//                        Log.d("WalletlsjWalletsavedNumber", "image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
//                        context.startActivity(intent);
//                    }
////
//                } catch (JSONException e) {
//                    e.getMessage();
//                }
////                }
//
//
//            }
//        }
//        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
//        sendPostReqAsyncTask.execute(datasss);
//    }
}

