package com.gpslab.kaun.Splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.DB.databaseclass;
import com.gpslab.kaun.Login.LoginSecActivity;
import com.gpslab.kaun.MainActivity;
import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.Service.MqttMessageService;
import com.gpslab.kaun.Service.MyFirebaseMessagingService;
import com.gpslab.kaun.Service.SocketServiceProvider;
import com.gpslab.kaun.Webapi.createnew;
import com.gpslab.kaun.feedback.FeedbackActivity;
import com.gpslab.kaun.internetcheck.InternetActivity;
import com.gpslab.kaun.language.LanguageActivity;
import com.gpslab.kaun.model.GetLogoResponseJson;
import com.gpslab.kaun.model.GetSMSTable;
import com.gpslab.kaun.privacyActivity;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit_model.AddUserResponseJson;

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

public class SplashActivity extends AppCompatActivity {
    public SharedPreferences.Editor editor;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public String id;
    private static createnew addpopup;
    String resTxt = null;
    final int splashTimeOut = 1000;


    String insertMes = "";
    String insertMesData = "";


    String insertSpamMes = "";
    String insertSpamMesData = "";



    String insertSpamCall  = "";
    String insertSpamCallData = "";


    public String[] keykey = {"verification code","OLA","Jio","SBI","airtel","Amazon","paytm","Paytm","Samsung","OTP","AD-OLACAB","JX-JioPay",
            "JE-JIOINF","QP-CENTBK","AD-CENTBK","AX-CENTBK","JK-CENTBK","JM-CENTBK","BP-iPaytm","MD-iPaytm","VM-iPaytm","AE-AIRGNF","AE-AIRTRF","AE-AIRTEL"
            ,"TM-SAMSNG"};
    public String[] namename = {"OTP","OLA","Jio","SBI","Airtel","Amazon","Paytm",
            "Paytm","Samsung","OTP","OLA","Jio",
            "Jio","Central bank of india","Central bank of india",
            "Central bank of india",
            "Central bank of india","Central bank of india",
            "Paytm","Paytm","Paytm","Airtel","Airtel","Airtel"
            ,"Samsung"};

    public String[] urlurl = {"https://www.pngrepo.com/png/162957/512/footprint.png",
            "https://assets.materialup.com/uploads/ad34971d-a8fe-4fcc-9a30-0008043f2553/preview.jpg",
            "https://seeklogo.com/images/J/jio-logo-7720D2E7BA-seeklogo.com.png",
            "https://www.freepnglogos.com/uploads/sbi-logo-png/sbi-logo-sbi-symbol-meaning-history-and-evolution-11.png",
            "https://www.searchpng.com/wp-content/uploads/2019/01/Airtel-PNG-Icon.png",
            "https://upload.wikimedia.org/wikipedia/commons/d/de/Amazon_icon.png",
            "https://www.searchpng.com/wp-content/uploads/2019/02/Paytm-Logo-With-White-Border-PNG-image.png",
            "https://www.searchpng.com/wp-content/uploads/2019/02/Paytm-Logo-With-White-Border-PNG-image.png",
            "https://cdn.samsung.com/etc/designs/smg/global/imgs/logo-square-letter.png",
            "https://www.pngrepo.com/png/162957/512/footprint.png",
            "https://assets.materialup.com/uploads/ad34971d-a8fe-4fcc-9a30-0008043f2553/preview.jpg",
            "https://seeklogo.com/images/J/jio-logo-7720D2E7BA-seeklogo.com.png",
            "https://seeklogo.com/images/J/jio-logo-7720D2E7BA-seeklogo.com.png",
            "https://bankingfrontiers.com/wp-content/uploads/2016/06/Central-Bank-of-India.jpg",
            "https://bankingfrontiers.com/wp-content/uploads/2016/06/Central-Bank-of-India.jpg",
            "https://bankingfrontiers.com/wp-content/uploads/2016/06/Central-Bank-of-India.jpg",
            "https://bankingfrontiers.com/wp-content/uploads/2016/06/Central-Bank-of-India.jpg",
            "https://bankingfrontiers.com/wp-content/uploads/2016/06/Central-Bank-of-India.jpg",
            "https://www.searchpng.com/wp-content/uploads/2019/02/Paytm-Logo-With-White-Border-PNG-image.png",
            "https://www.searchpng.com/wp-content/uploads/2019/02/Paytm-Logo-With-White-Border-PNG-image.png",
            "https://www.searchpng.com/wp-content/uploads/2019/02/Paytm-Logo-With-White-Border-PNG-image.png",
            "https://www.searchpng.com/wp-content/uploads/2019/01/Airtel-PNG-Icon.png",
            "https://www.searchpng.com/wp-content/uploads/2019/01/Airtel-PNG-Icon.png",
            "https://www.searchpng.com/wp-content/uploads/2019/01/Airtel-PNG-Icon.png"
            ,"https://cdn.samsung.com/etc/designs/smg/global/imgs/logo-square-letter.png"};


    public MyDbHandler myDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);




        myDbHandler = new MyDbHandler(getApplicationContext(),"userbd",null,1);

        Temp.setMyDbHandler(myDbHandler);

        ComponentName componentName = new ComponentName(this, FirebaseMessagingService.class);
        getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        Intent intent = new Intent(SplashActivity.this, MyFirebaseMessagingService.class);
        startService(intent);


        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful()){
                    String Token = task.getResult().getToken();
                    edit.putString("token",Token);
                    edit.apply();
                    Log.i("tokentokentokentoken","Token == ==  "+Token);
                }
            }
        });

        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();

        id = sharedPreferences.getString("id","0");




        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        String CountryID= tm.getSimCountryIso().toUpperCase();
//
//        new CountDownTimer(1000,1000){
//            @Override
//            public void onTick(long millisUntilFinished) {
//                //
//                Log.d("TabTag:"," onTick ");
//            }
//
//            @Override
//            public void onFinish() {
//                Log.d("TabTag:"," onFinish ");
//            }
//        }.start();



//        SendDataToServer("");
        Log.i("CCpWalletCCpWalletlucky", "Retrofit Running  = ");
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected() )
        {
            retrofit();
        }

        else
        {
            Intent intentnew = new Intent(SplashActivity.this, InternetActivity.class);
            intentnew.putExtra("msg","No Internet");
            startActivity(intentnew);
        }



//
//        GetData();


        Log.d("WalletLuckyXXX","local = = = "+countryCodeValue+" locale == == == "+CountryID);
    }

    private void GetData() {
        ArrayList<GetSMSTable> keylist = myDbHandler.viewSMSNew();
        if(keylist.size() >0){
            myDbHandler.deleteSMSAll();
        }

        Log.d("WalletLuckyKeyKey","Size ==   "+keykey.length);
        for(int i=0;i<keykey.length;i++){
            String key = keykey[i];
            String url = urlurl[i];
            String name = namename[i];
            insertMes = insertMes + "'" + key + "'";
            insertMes = insertMes + "," + "'" + url + "'";
            insertMes = insertMes + "," + "'" + name + "'";
            insertMesData = insertMesData + "(" + insertMes + ")" + ",";
            insertMes = "";
        }
        insertMesData = insertMesData.substring(0, insertMesData.length() - 1);
        myDbHandler.InsertSMSDatabase(insertMesData);





        Thread splashThread = new Thread(){
            int wait = 0;
            @Override
            public void run() {
                try {
                    super.run();
                    while(wait < splashTimeOut){
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                }finally{

                    if(id == "0"){
                        Intent intent  = new Intent(SplashActivity.this, LanguageActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent  = new Intent(SplashActivity.this, MainHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    finish();
                }
            }
        };
        splashThread.start();
    }




    private void retrofit(){
//        Log.i("CCpWalletCCpWalletlucky", "nameValuePairs body  = " + new Gson().toJson(response.body()));
        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.getlogo().enqueue(new Callback<GetLogoResponseJson>() {
            @Override
            public void onResponse(Call<GetLogoResponseJson> call, Response<GetLogoResponseJson> response) {
                Log.i("CCpWalletCCpWalletlucky", "nameValuePairs code  = " + response.code());
                Log.i("CCpWalletCCpWalletlucky", "nameValuePairs body  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    if(response.body().data.size()>0){
                        ArrayList<GetSMSTable> keylist = myDbHandler.viewSMSNew();
                        if(keylist.size() >0){
                            myDbHandler.deleteSMSAll();
                        }
                        for(int i=0;i<response.body().data.size();i++){
                            Log.i("CCpWalletCCpWalletlucky", "nameValuePairs key  = " + response.body().data.get(i).tags);
                            String tag = response.body().data.get(i).tags;
                            String[] res = tag.split("[,]", 0);
                            for(String myStr: res) {
                                Log.i("CCpWalletCCpWalletlucky", "nameValuePairs for key  = " + myStr);

                                String url = "https://gpslabindia.nyc3.digitaloceanspaces.com/callify/companylogo/"+response.body().data.get(i).logo;
                                insertMes = insertMes + "'" + myStr + "'";
                                insertMes = insertMes + "," + "'" + url + "'";
                                insertMes = insertMes + "," + "'"+ "NA" +"'";
                                insertMesData = insertMesData + "(" + insertMes + ")" + ",";
                                insertMes = "";
                                System.out.println(myStr);

                            }

                        }
                        insertMesData = insertMesData.substring(0, insertMesData.length() - 1);
                        myDbHandler.InsertSMSDatabase(insertMesData);
                        Log.i("CCpWalletCCpWalletlucky", "nameValuePairs for list  = " + insertMesData);
                    }









                    Thread splashThread = new Thread(){
                        int wait = 0;
                        @Override
                        public void run() {
                            try {
                                super.run();
                                while(wait < splashTimeOut){
                                    sleep(100);
                                    wait += 100;
                                }
                            } catch (Exception e) {
                            }finally{

                                if(id == "0"){
                                    Intent intent  = new Intent(SplashActivity.this, LanguageActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Intent intent  = new Intent(SplashActivity.this, MainHomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                finish();
                            }
                        }
                    };
                    splashThread.start();
//                    String result = response.body().response.result;
//                    if(result.equalsIgnoreCase("1")){

//                        edit.putString("fname", etfistname.getText().toString().trim());
//                        edit.putString("lname", etsecondname.getText().toString().trim());
//                        edit.putString("email", etemail.getText().toString().trim());
//                        edit.putString("mobile", Mobile);
//                        edit.putString("id", response.body().response.insertId);
//                        edit.putString("img", "https://nyc3.digitaloceanspaces.com/gpslabindia/callify/userdata/"+ image_name+".jpg");
//                        edit.apply();
//
//                        Intent intent = new Intent(LoginSecActivity.this, privacyActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }


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
                    Toast.makeText(SplashActivity.this, "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<GetLogoResponseJson> call, Throwable t) {
                t.printStackTrace();
                Intent intentnew = new Intent(SplashActivity.this, InternetActivity.class);
                intentnew.putExtra("msg",t.getLocalizedMessage());
                startActivity(intentnew);
                Log.e("System error:", t.getLocalizedMessage());

            }
        });
    }

    private void SendDataToServer(final String datasss) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String QuickFirstName = datasss;




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("feedback", "Feedback"));

                nameValuePairs.add(new BasicNameValuePair("id", sharedPreferences.getString("mobile","")));
                Log.d("fragmentonefragmentone", "fname = = " + nameValuePairs);

                try {


                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "feedback_data/");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    resTxt = EntityUtils.toString(entity);

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return resTxt;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Log.d("fragmentonefragmentone", "result = = " + result);
                if (TextUtils.isEmpty(result)) {

                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray response = jsonObject.getJSONArray("message");
                        Log.d("fragmentonefragmentone", "calltype 2 response = " + response.length());
                        Toast.makeText(SplashActivity.this, "Your feedback is important to us.", Toast.LENGTH_SHORT).show();



                        for(int i=0;i<response.length();i++){
                            String key = response.getJSONObject(i).getString("key");
                            String url = response.getJSONObject(i).getString("url");
                            String name = response.getJSONObject(i).getString("name");
                            insertMes = insertMes + "'" + key + "'";
                            insertMes = insertMes + "," + "'" + url + "'";
                            insertMes = insertMes + "," + "'" + name + "'";
                            insertMesData = insertMesData + "(" + insertMes + ")" + ",";
                            insertMes = "";
                        }
                        insertMesData = insertMesData.substring(0, insertMesData.length() - 1);
                        myDbHandler.InsertSMSDatabase(insertMesData);

                        String  spam_status = jsonObject.getString("spam_status");
                        if(spam_status.equalsIgnoreCase("1")){
                            JSONArray spam_msg = jsonObject.getJSONArray("spam_msg");

                            if(spam_msg.length() >0){
                                for(int j=0;j<spam_msg.length();j++){
                                    String spam_msg_number = response.getJSONObject(j).getString("number");
                                    String spam_msg_name = response.getJSONObject(j).getString("name");
                                    String spam_msg_count = response.getJSONObject(j).getString("count");

                                    insertSpamMes = insertSpamMes + "'" + spam_msg_number + "'";
                                    insertSpamMes = insertSpamMes + "," + "'" + spam_msg_name + "'";
                                    insertSpamMes = insertSpamMes + "," + "'" + spam_msg_count + "'";
                                    insertSpamMesData = insertSpamMesData + "(" + insertSpamMes + ")" + ",";
                                    insertSpamMes = "";

                                }
                                insertSpamMesData = insertSpamMesData.substring(0, insertSpamMesData.length() - 1);
                                myDbHandler.InsertSpamMessage(insertSpamMesData);
                            }




                            JSONArray spam_call = jsonObject.getJSONArray("spam_call");

                            if(spam_call.length() >0){
                                for(int z=0;z<spam_call.length();z++){
                                    String spam_call_number = response.getJSONObject(z).getString("number");
                                    String spam_call_name = response.getJSONObject(z).getString("name");
                                    String spam_call_count = response.getJSONObject(z).getString("count");

                                    String spam_call_usally_calls = response.getJSONObject(z).getString("usally_calls");
                                    String spam_call_call_activity = response.getJSONObject(z).getString("call_activity");






                                    insertSpamCall = insertSpamCall + "'" + spam_call_number + "'";
                                    insertSpamCall = insertSpamCall + "," + "'" + spam_call_name + "'";
                                    insertSpamCall = insertSpamCall + "," + "'" + spam_call_count + "'";
                                    insertSpamCall = insertSpamCall + "," + "'" + spam_call_usally_calls + "'";
                                    insertSpamCall = insertSpamCall + "," + "'" + spam_call_call_activity + "'";
                                    insertSpamCallData = insertSpamCallData + "(" + insertSpamCall + ")" + ",";
                                    insertSpamCall = "";
                                }
                                insertSpamCallData = insertSpamCallData.substring(0, insertSpamCallData.length() - 1);
                                myDbHandler.InsertSpamCall(insertSpamCallData);
                            }

                        }







                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(datasss);
    }
}