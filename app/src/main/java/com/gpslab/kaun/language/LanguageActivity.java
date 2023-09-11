package com.gpslab.kaun.language;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gpslab.kaun.Login.LoginSecActivity;
import com.gpslab.kaun.MyBrodcastRecieverService;
import com.gpslab.kaun.Phone.PhoneActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.popup.defaultpoup;
import com.gpslab.kaun.popup.language;
import com.gpslab.kaun.popup.profilepopup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    private static language bannedapps;
    private static defaultpoup defaultpopupnew;

    String[] appPermissions = {Manifest.permission.INTERNET,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.MODIFY_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SYSTEM_ALERT_WINDOW};
    private static final int PERMISSION_REQUEST_CODE = 1240;

    private static profilepopup profilepopupnew;
    public LinearLayout llbtn;
    public TextView tvbtn, tvmoretv, tvhinditv;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;

    String language;

    public boolean newdata = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        loadLocal();
        setContentView(R.layout.activity_language);




        tvhinditv = (TextView)findViewById(R.id.hinditv);
        tvmoretv = (TextView)findViewById(R.id.moretv);
        bannedapps = new language(LanguageActivity.this);

        defaultpopupnew = new defaultpoup(LanguageActivity.this);


        profilepopupnew = new profilepopup(LanguageActivity.this);
        llbtn = (LinearLayout)findViewById(R.id.llbutttonll);
        tvbtn = (TextView)findViewById(R.id.tvbutttontv);

        tvhinditv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("hi");
                newdata = true;

            }
        });

        tvmoretv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannedapps.addpopup();
            }
        });

        llbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(sharedPreferences.getBoolean("default",false)){

                    if(sharedPreferences.getBoolean("defaultnew",false)){
//                        if (checkAndRequestPermissions()) {
//                            // All permission are granted already. Proceed ahead
//                        }
                        Intent intent = new Intent(LanguageActivity.this, PhoneActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        profilepopupnew.profileaddpopup();
                    }
//                }else {
//                    defaultpopupnew.defaultaddpopup();
//                }

            }
        });
     //   new TaskGetData().execute("http://node.hwashsanitizer.com:3000/getposts");
//        if (checkAndRequestPermissions()) {
//            // All permission are granted already. Proceed ahead
//        }
        tvbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(sharedPreferences.getBoolean("default",false)){

                    if(sharedPreferences.getBoolean("defaultnew",false)){
//                        if (checkAndRequestPermissions()) {
//                            // All permission are granted already. Proceed ahead
//                        }
                        Intent intent = new Intent(LanguageActivity.this, PhoneActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        profilepopupnew.profileaddpopup();
                    }
//                }else {
//                    defaultpopupnew.defaultaddpopup();
//                }


            }
        });
    }


    private boolean checkAndRequestPermissions() {

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);


            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);

            return false;
        }
        ;
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                Log.d("WalletYuyckey", "jsonArray ==  4");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

 //                   Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    Log.d("WalletYuyckey", "jsonArray ==  3");
//                    GetContactsIntoArrayList();
//                    Intent service = new Intent(LoginSecActivity.this, MyBrodcastRecieverService.class);
//                    startService(service);
//                    editor.putString("check", "true");
//                    editor.apply();

                    Intent intent = new Intent(LanguageActivity.this, PhoneActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Log.d("WalletYuyckey", "jsonArray ==  2");
//                    GetContactsIntoArrayList();
//                    Intent service = new Intent(LoginSecActivity.this, MyBrodcastRecieverService.class);
//                    startService(service);

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }





    public class TaskGetData extends AsyncTask<String , String , String> {

        @Override
        protected void onPreExecute() {
//            textView.append("Get data ...\n\n");
        }

        @Override
        protected String doInBackground(String... params) {
            return getDataHttpUriConnection(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {

  //          Toast.makeText(LanguageActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getDataHttpUriConnection(String uri){
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String result = inputStreamToString(con.getInputStream());
            return result;


        } catch (IOException e) {
            Log.d("sldjflsjdflsjfl","error == == == "+e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
    public static String inputStreamToString(InputStream stream)  {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line = "";

        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public void loadLocal(){
        language = sharedPreferences.getString("My_Lang","");
        setLocale(language);
    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        edit.putString("My_Lang",lang);
        edit.apply();


        if(newdata){
            Intent intent = new Intent(LanguageActivity.this, LanguageActivity.class);
            startActivity(intent);
            finish();
        }

    }
}