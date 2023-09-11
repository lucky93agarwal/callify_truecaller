package com.gpslab.kaun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gpslab.kaun.adapter.ContectAdapter;
import com.gpslab.kaun.model.Contact;
import com.gpslab.kaun.model.ContactFetcher;
import com.gpslab.kaun.model.GetContectData;
import com.gpslab.kaun.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ITEMS_PER_AD = 6;
    private Intent forService;
    String[] appPermissions = {Manifest.permission.INTERNET,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.MODIFY_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.SYSTEM_ALERT_WINDOW};
    private static final int PERMISSION_REQUEST_CODE = 1240;


    ///////////// Contect List ///////////////////////
    @Override
    protected void onResume()
    {
        super.onResume();
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, "com.gpslab.kaun");
        startActivity(intent);
    }

    ArrayList<String> StoreContacts;
    ArrayAdapter<String> arrayAdapter;
    Cursor cursor;
    String name, phonenumber, phoneid;
    public static final int RequestPermissionCode = 1;
    Button button;
    HashMap user;


    public List<GetContectData> productList = new ArrayList<>();
    public GetContectData getbcdata;
    public ContectAdapter adapter;
    public RecyclerView mRecyclerView;
    public FloatingActionButton floatingActionBtn;
    public RecyclerView.LayoutManager mLayoutManager;


    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;


    public Button btngetdata;


    public 	ArrayList<Contact> listContacts;


    //////////// Contect List ///////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        btngetdata = (Button)findViewById(R.id.getdata);

        btngetdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainHomeActivity.class);
                startActivity(intent);
            }
        });


        if (checkAndRequestPermissions()) {
            // All permission are granted already. Proceed ahead
        }

        if (!Settings.canDrawOverlays(this)) {
            // ask for setting
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, PERMISSION_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {} else {
                ActivityCompat.requestPermissions(this,
                        new String[] {
                                Manifest.permission.READ_PHONE_STATE
                        },
                        PERMISSION_REQUEST_CODE);
            }
        }
        forService = new Intent(MainActivity.this,CallReceiver.class);

        mRecyclerView = (RecyclerView) findViewById(R.id.datarecyclerview);
        user = new HashMap();

        StoreContacts = new ArrayList<String>();


    }

    public void GetContactsIntoArrayList() {
        listContacts = new ContactFetcher(this).fetchAll();


        for(int i=0;i<listContacts.size();i++){



            if(listContacts.get(i).id.equalsIgnoreCase("212")){
                Log.d("WalletLuckyYUYUWalletLuckyYUYU","name == "+listContacts.get(i).id+" number 1 == "+listContacts.get(i).numbers.get(0).number+" number 2 == "+listContacts.get(i).numbers.get(1).number);
            }
        }

//        adapter = new ContectAdapter(MainActivity.this, listContacts);
//        mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//
//        cursor.close();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {

        }
    }
    private boolean checkAndRequestPermissions() {
        // Check which Permissions are granted (Check करें कि कौन सी Permissions Granted है।)
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions){
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);

//                Log.d("CheckPermissionLucky","Yes");
            }
        }
        // Ask for non-granted permissions
        if (!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
//            Log.d("CheckPermissionLucky","No");
            return false;
        };
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        switch (requestCode) {
            case PERMISSION_REQUEST_CODE : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.d("CheckPermissionLucky","Yes2");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    GetContactsIntoArrayList();
                    Intent service = new Intent(MainActivity.this, MyBrodcastRecieverService.class);
                    startService(service);
                    editor.putString("check","true");
                    editor.apply();

                    Intent intents = new Intent(MainActivity.this, MainHomeActivity.class);
                    startActivity(intents);

                } else {
                 if(sharedPreferences.getString("check","false").equalsIgnoreCase("true")){
                     GetContactsIntoArrayList();
                     Intent service = new Intent(MainActivity.this, MyBrodcastRecieverService.class);
                     startService(service);
                 }
//                    Log.d("CheckPermissionLucky","NO2");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
//                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }





}