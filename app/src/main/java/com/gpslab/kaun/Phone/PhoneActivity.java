package com.gpslab.kaun.Phone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonArray;
import com.gpslab.kaun.DB.FirstTableData;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.Login.LoginSecActivity;
import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.OTP.OTPActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.Receiver.TelephonyInfo;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.Service.GetContactsService;
import com.gpslab.kaun.Splash.SplashActivity;
import com.gpslab.kaun.Webapi.createnew;
import com.gpslab.kaun.internetcheck.InternetActivity;
import com.gpslab.kaun.language.LanguageActivity;
import com.gpslab.kaun.model.Contact;
import com.gpslab.kaun.model.ContactFetcher;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

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

public class PhoneActivity extends AppCompatActivity {

    public CountryCodePicker ccpccp;
    public AppCompatButton tvcontinue;
    public EditText etmobile;
    public ArrayList<Contact> listContacts;
    String resTxt = null;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public LinearLayout llcontinuell;
    public FirstTableData user;
    public MyDbHandler myDbHandler;
    GoogleApiClient mGoogleApiClient;
    private static createnew addpopup;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PhoneActivity.this, LanguageActivity.class);
        startActivity(intent);
        finish();
    }
    public ImageView ivgreentick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected() )
        {

        }

        else
        {
            Intent intentnew = new Intent(PhoneActivity.this, InternetActivity.class);
            intentnew.putExtra("msg","No Internet");
            startActivity(intentnew);
        }

        llcontinuell = (LinearLayout) findViewById(R.id.llcontinue);

        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        edit.putBoolean("checkotp",true);
        edit.apply();
        ccpccp = (CountryCodePicker) findViewById(R.id.ccp);

        tvcontinue = (AppCompatButton) findViewById(R.id.tvcontinue);
        etmobile = (EditText) findViewById(R.id.etmobile);
        ivgreentick = (ImageView)findViewById(R.id.greencheckiv);
        ivgreentick.setVisibility(View.GONE);
        etmobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==10){
                    ivgreentick.setVisibility(View.VISIBLE);
                }else {
                    ivgreentick.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        myDbHandler = new MyDbHandler(getApplicationContext(), "userbd", null, 1);

        Temp.setMyDbHandler(myDbHandler);
        myDbHandler = Temp.getMyDbHandler();

        tvcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etmobile.getText().length()<10){
                    Toast.makeText(PhoneActivity.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                }
                else
                if (etmobile.getText().length() > 10) {
                    Toast.makeText(PhoneActivity.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                } else {


                    tvcontinue.setText("Please wait");
                    tvcontinue.setEnabled(false);
//                    Intent intentx = new Intent(PhoneActivity.this, GetContactsService.class);
//                    intentx.putExtra("key",etmobile.getText().toString());
//                    startService(intentx);




                    Log.d("CCpWallet", "CCP = " + ccpccp.getSelectedCountryCodeAsInt());
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                    if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected() )
                    {
                        Intent intent = new Intent(PhoneActivity.this, OTPActivity.class);
                        intent.putExtra("phone", etmobile.getText().toString());
                        intent.putExtra("code",ccpccp.getSelectedCountryCode());
                        startActivity(intent);
                        finish();
                    }

                    else
                    {
                        Intent intentnew = new Intent(PhoneActivity.this, InternetActivity.class);
                        intentnew.putExtra("msg","No Internet");
                        startActivity(intentnew);
                    }




                }
            }
        });
        llcontinuell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etmobile.getText().length()<10){
                    Toast.makeText(PhoneActivity.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                }
                else
                if (etmobile.getText().length() > 10) {
                    Toast.makeText(PhoneActivity.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                } else {




                    tvcontinue.setEnabled(false);
                    tvcontinue.setText("Please wait");

                    Log.d("CCpWallet", "CCP = " + ccpccp.getSelectedCountryCodeAsInt());

//                    Intent intent = new Intent(PhoneActivity.this, OTPActivity.class);
//                    intent.putExtra("phone", etmobile.getText().toString());
//                    intent.putExtra("code",ccpccp.getSelectedCountryCode());
//                    startActivity(intent);
//                    finish();




                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                    if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected() )
                    {
                        Intent intent = new Intent(PhoneActivity.this, OTPActivity.class);
                        intent.putExtra("phone", etmobile.getText().toString());
                        intent.putExtra("code",ccpccp.getSelectedCountryCode());
                        startActivity(intent);
                        finish();
                    }

                    else
                    {
                        Intent intentnew = new Intent(PhoneActivity.this, InternetActivity.class);
                        intentnew.putExtra("msg","No Internet");
                        startActivity(intentnew);
                    }
                }
            }
        });

        Log.d("CCpWallet", "CCP = " + ccpccp.getSelectedCountryCodeAsInt());
        Log.d("CCpWallet", "CountryName = " + ccpccp.getSelectedCountryName());



    }

    ArrayList<String> _mst=new ArrayList<>();
    private int RESOLVE_HINT = 2;

    public void firstinsertdata() {
        listContacts = new ContactFetcher(this).fetchAll();
        for (int i = 0; i < listContacts.size(); i++) {
            user = new FirstTableData();
            if(listContacts.get(i).name !=null){


                if (listContacts.get(i).name.length() > 0) {
                    user.setName(listContacts.get(i).name);
                } else {
                    user.setName("");
                }
            }


            if (listContacts.get(i).emails.size() > 0 && listContacts.get(i).emails.get(0) != null) {
                if (listContacts.get(i).id.equalsIgnoreCase(listContacts.get(i).emails.get(0).id)) {
                    user.setEmail(listContacts.get(i).emails.get(0).address);
                }
            } else {
                user.setEmail("");
            }


            if (listContacts.get(i).numbers.size() == 1) {
                if (listContacts.get(i).numbers.size() > 0 && listContacts.get(i).numbers.get(0) != null) {

                    if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() == 10) {
                        user.setMobileone("+91" + listContacts.get(i).numbers.get(0).number.replace(" ", ""));
                        user.setMobiletwo("");
                    } else if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() > 10) {
                        if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() == 11) {
                            String number = listContacts.get(i).numbers.get(0).number.replace(" ", "");
                            number = number.substring(1);
                            number = "+91" + number;
                            user.setMobileone(number);
                            user.setMobiletwo("");
                        } else {
                            user.setMobileone(listContacts.get(i).numbers.get(0).number.replace(" ", ""));
                            user.setMobiletwo("");
                        }

                    } else {
                        user.setMobileone(listContacts.get(i).numbers.get(0).number.replace(" ", ""));
                        user.setMobiletwo("");
                    }

                }
            } else if (listContacts.get(i).numbers.size() == 2) {
                if (listContacts.get(i).numbers.size() == 2 && listContacts.get(i).numbers.get(1) != null) {


                    if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() == 10) {
                        user.setMobileone("+91" + listContacts.get(i).numbers.get(0).number.replace(" ", ""));

                    } else if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() > 10) {
                        if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() == 11) {
                            String number = listContacts.get(i).numbers.get(0).number.replace(" ", "");
                            number = number.substring(1);
                            number = "+91" + number;
                            user.setMobileone(number);

                        } else {
                            user.setMobileone(listContacts.get(i).numbers.get(0).number.replace(" ", ""));

                        }

                    } else {
                        user.setMobileone(listContacts.get(i).numbers.get(0).number.replace(" ", ""));

                    }


                    if (listContacts.get(i).numbers.get(1).number.replace(" ", "").length() == 10) {
                        user.setMobiletwo("+91" + listContacts.get(i).numbers.get(1).number.replace(" ", ""));

                    } else if (listContacts.get(i).numbers.get(1).number.replace(" ", "").length() > 10) {
                        if (listContacts.get(i).numbers.get(1).number.replace(" ", "").length() == 11) {
                            String number = listContacts.get(i).numbers.get(1).number.replace(" ", "");
                            number = number.substring(1);
                            number = "+91" + number;
                            user.setMobiletwo(number);

                        } else {
                            user.setMobiletwo(listContacts.get(i).numbers.get(1).number.replace(" ", ""));

                        }

                    } else {
                        user.setMobiletwo(listContacts.get(i).numbers.get(1).number.replace(" ", ""));

                    }

                }
            } else {
                user.setMobileone("");
                user.setMobiletwo("");
            }
            user.setPhoto("");
            int j = myDbHandler.insertUser(user);
            Log.d("walletwallet", "inseart check = " + j);
        }

//        user.setMobileone();
//        user.setMobiletwo();
//        user.setPhoto();
//        user.setEmail();


    }

    private void firstqueryall() {

        ArrayList<FirstTableData> arrayList = myDbHandler.viewUser();
        long countnew = myDbHandler.getProfilesCount();
        Log.d("walletwallet", "size = " + arrayList.size() + "   Size ====  " + countnew);


        //// check row on the table is null or not
        if (arrayList.size() != 0) {



        } else {
            Log.d("walletwallet", "Insert Query........");
            firstinsertdata();
        }
    }

    private void GetContactsIntoArrayList() {
        Log.d("WalletYuyckey", "jsonArray ==  1");
        listContacts = new ContactFetcher(this).fetchAll();

        user = new FirstTableData();
        firstqueryall();



    }

//    private void SendDataToServerss(final String mobile) {
//        Intent intent = new Intent(PhoneActivity.this, GetContactsService.class);
//        intent.putExtra("key",mobile);
//        startService(intent);
////        GetContactsIntoArrayList();
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//
//                String QuickMobile = mobile;
//
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//
//                nameValuePairs.add(new BasicNameValuePair("mobile", QuickMobile));
//
//                Log.d("dfsdfsdfsdfdfsd", "QuickMobile = = " + QuickMobile);
//                try {
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "verify_user/");
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
//                Log.d("dfsdfsdfsdfdfsd", "result = = " + result);
//                if (result.isEmpty()) {
//
//                } else {
//                    String res_result;
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        Log.d("dfsdfsdfsdfdfsd", "jsonObject2 = = " + jsonObject.getString("response"));
//                        JSONArray response = jsonObject.getJSONArray("response");
//                        if (response.length() == 1) {
//                            res_result = response.getJSONObject(0).getString("result");
//                            Log.d("dfsdfsdfsdfdfsd", "res_result = = " + res_result);
//                        } else {
//                            res_result = response.getJSONObject(1).getString("result");
//                        }
//
//                        Log.d("dfsdfsdfsdfdfsd", "res_result = = " + res_result);
////                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
////                    Log.d("dfsdfsdfsdfdfsd", "jsonObject1 = = " + jsonObject1);
////                    JsonArray jsonElements = new JsonArray();
//
//
//                        if (res_result.equalsIgnoreCase("0")) {
//                            Log.d("dfsdfsdfsdfdfsd", "response = = " + String.valueOf(response.getJSONObject(0).getInt("id")));
//
//                            edit.putString("fname", response.getJSONObject(0).getString("fname"));
//                            edit.putString("lname", response.getJSONObject(0).getString("lname"));
//                            edit.putString("email", response.getJSONObject(0).getString("email"));
//                            edit.putString("mobile", response.getJSONObject(0).getString("mobile"));
//                            edit.putString("id", String.valueOf(response.getJSONObject(0).getInt("id")));
//                            edit.putString("img", response.getJSONObject(0).getString("profile_image"));
//
//
////                            edit.putString("is_active", response.getJSONObject(0).getString("is_active"));
//                            edit.putString("provider", response.getJSONObject(0).getString("provider"));
//                            edit.putString("location", response.getJSONObject(0).getString("location"));
//                            edit.putString("zone", response.getJSONObject(0).getString("zone"));
//                            edit.putString("is_spam", response.getJSONObject(0).getString("is_spam"));
////                            edit.putString("created_at", response.getJSONObject(0).getString("created_at"));
//
//                            edit.apply();
//                            addpopup = new createnew(PhoneActivity.this);
//                            addpopup.data();
//                            Intent intent = new Intent(PhoneActivity.this, MainHomeActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } else if (res_result.equalsIgnoreCase("1")) {
//                            addpopup = new createnew(PhoneActivity.this);
//                            addpopup.data();
//
//                            Intent intent = new Intent(PhoneActivity.this, OTPActivity.class);
//                            intent.putExtra("phone", etmobile.getText().toString());
//                            intent.putExtra("code",ccpccp.getSelectedCountryCode());
//                            startActivity(intent);
//                            finish();
//                        } else if (res_result.equalsIgnoreCase("2")) {
//                            Log.d("WalletLuckyYUYU", "response = = " + res_result);
//                            edit.putString("fname", response.getJSONObject(0).getString("fname"));
//                            edit.putString("lname", response.getJSONObject(0).getString("lname"));
//                            edit.putString("email", response.getJSONObject(0).getString("email"));
//                            edit.putString("mobile", response.getJSONObject(0).getString("mobile"));
//                            edit.putString("id", String.valueOf(response.getJSONObject(0).getInt("id")));
//                            edit.putString("img", response.getJSONObject(0).getString("profile_image"));
//
//
//                            edit.putString("is_active", response.getJSONObject(0).getString("is_active"));
//                            edit.putString("provider", response.getJSONObject(0).getString("provider"));
//                            edit.putString("location", response.getJSONObject(0).getString("location"));
//                            edit.putString("zone", response.getJSONObject(0).getString("zone"));
//                            edit.putString("is_spam", response.getJSONObject(0).getString("is_spam"));
//                            edit.putString("created_at", response.getJSONObject(0).getString("created_at"));
//                            edit.apply();
//                            addpopup = new createnew(PhoneActivity.this);
//                            addpopup.data();
//                            Intent intent = new Intent(PhoneActivity.this, MainHomeActivity.class);
//                            startActivity(intent);
//                            finish();
//                            Toast.makeText(PhoneActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
//                        } else if (res_result.equalsIgnoreCase("3")) {
//                            Toast.makeText(PhoneActivity.this, "Mobile no is null", Toast.LENGTH_SHORT).show();
//                        }
//
////                    Intent intent = new Intent(PhoneActivity.this, MainHomeActivity.class);
////                    startActivity(intent);
////                    finish();
//                    } catch (JSONException e) {
//                        e.getMessage();
//                    }
//                }
//
//
//            }
//        }
//        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
//        sendPostReqAsyncTask.execute(mobile);
//    }


}