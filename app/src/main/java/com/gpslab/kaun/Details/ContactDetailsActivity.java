package com.gpslab.kaun.Details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.internal.$Gson$Preconditions;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.Home.GetCallLogTable;
import com.gpslab.kaun.Notification.UserProfile;
import com.gpslab.kaun.R;
import com.gpslab.kaun.activity.PreferencesActivity;
import com.gpslab.kaun.adapter.CallDetailsAdapter;
import com.gpslab.kaun.adapter.ContectAdapter;
import com.gpslab.kaun.adapter.RecyclerViewAdapter;
import com.gpslab.kaun.model.GetContectData;
import com.gpslab.kaun.model.GetMessageTable;
import com.gpslab.kaun.mssagedetail.MessageDetailsActivity;
import com.gpslab.kaun.popup.simselectpopup;
import com.gpslab.kaun.view.ChatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDetailsActivity extends AppCompatActivity {

    public TextView tvname;
    public String Name, Mobile, Img;
    public simselectpopup defaultpopupnew;



    public EditText etetmsgtv;
    public ImageView ivback;


    public List<GetContectData> productList = new ArrayList<>();
    public GetContectData getbcdata;
    public CallDetailsAdapter adapter;
    public RecyclerView mRecyclerView;

    public RecyclerView.LayoutManager mLayoutManager;

    public MyDbHandler myDbHandler;


    public CircleImageView ivcircle;
    public ImageView ivcaller, ivmsendiviv;
    public TextView tvcalltv, tvhidetvtv;
    public LinearLayout mlinear;

    public RelativeLayout mrelative;

    public boolean checknow = true;
    public TextView tvtvnewname;
    private Window window;
    private int brightness = 200;



    public LinearLayout llchat;
    public TextView tvchat;
    public ImageView ivchat;

    private void ScreenViewControl() {
        //       Get the current system brightness
        window = ContactDetailsActivity.this.getWindow();
        WindowManager.LayoutParams layoutpars = window.getAttributes();
        layoutpars.screenBrightness = brightness / (float) 255;
        window.setAttributes(layoutpars);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
//        ScreenViewControl();
        tvtvnewname = (TextView)findViewById(R.id.tvnewname);

        etetmsgtv = (EditText)findViewById(R.id.etmsgtv);
        ivmsendiviv = (ImageView)findViewById(R.id.sendiviv);

        llchat = (LinearLayout)findViewById(R.id.chativssll);
        tvchat = (TextView)findViewById(R.id.chatsstvs);
        ivchat = (ImageView)findViewById(R.id.chativss);


        ivcircle = (CircleImageView) findViewById(R.id.imageprofile);
        mlinear = (LinearLayout) findViewById(R.id.callivssll);
        mrelative = (RelativeLayout) findViewById(R.id.newrelativelayout);
        ivcaller = (ImageView) findViewById(R.id.callivss);
        ivback = (ImageView) findViewById(R.id.ivleftarrow);
        tvcalltv = (TextView)findViewById(R.id.datesstvs);
        tvhidetvtv = (TextView)findViewById(R.id.hidetvtv);

        Name = getIntent().getStringExtra("name");
        Mobile = getIntent().getStringExtra("number");
        Img = getIntent().getStringExtra("img");



        ivmsendiviv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                SmsManager smsManager = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {

                    SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(ContactDetailsActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
                    int subId = subscriptionInfoList.get(0).getSubscriptionId();// change index to 1 if you want to get Subscrption Id for slot 1.
                    smsManager = SmsManager.getSmsManagerForSubscriptionId(subId);

                }else{
                    smsManager = SmsManager.getDefault();
                }
                String myNumber = getIntent().getStringExtra("number");
                String myMsg = etetmsgtv.getText().toString();
                Log.i("checkdataknow"," message = "+myMsg);
                Log.i("checkdataknow"," Number = "+myNumber);








                String datanew = myNumber.substring(0,3);
                if(datanew.equalsIgnoreCase("+91")){
                    String datanews = myNumber.substring(3);
                    de.ub0r.android.logg0r.Log.d("WalletLuckynew","Mobile == == == "+datanews);
                    if (datanews.equals("") || myMsg.equals("")){
                        Toast.makeText(ContactDetailsActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        if (TextUtils.isDigitsOnly(datanews)){
//                SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(datanews, null, myMsg, null, null);
                            Toast.makeText(ContactDetailsActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();

                            PreferenceManager.getDefaultSharedPreferences(ContactDetailsActivity.this).edit().putString(PreferencesActivity.PREFS_BACKUPLASTTEXT, myMsg).commit();

                        } else {
                            Toast.makeText(ContactDetailsActivity.this, "Please enter the correct number", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    if (myNumber.equals("") || myMsg.equals("")){
                        Toast.makeText(ContactDetailsActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        if (TextUtils.isDigitsOnly(myNumber)){
//                SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(myNumber, null, myMsg, null, null);
                            Toast.makeText(ContactDetailsActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();

                            PreferenceManager.getDefaultSharedPreferences(ContactDetailsActivity.this).edit().putString(PreferencesActivity.PREFS_BACKUPLASTTEXT, myMsg).commit();

                        } else {
                            Toast.makeText(ContactDetailsActivity.this, "Please enter the correct number", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        llchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetailsActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        tvchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetailsActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        ivchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetailsActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        if (Img.equalsIgnoreCase("NA")) {

        } else {
            Glide.with(ContactDetailsActivity.this)
                    .load(Img)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(ivcircle);
        }
        ivcircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(ContactDetailsActivity.this, UserProfile.class);
                inten.putExtra("name",Name);
                inten.putExtra("number",Mobile);
                inten.putExtra("img",Img);
                inten.putExtra("duration","120");
                startActivity(inten);
            }
        });
        Log.d("WalletLuckyRishi", "Name =  =  " + Name);
        Log.d("WalletLuckyRishi", "Mobile =  =  " + Mobile);
        defaultpopupnew = new simselectpopup(ContactDetailsActivity.this);
        mrelative.setVisibility(View.GONE);
        tvhidetvtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checknow){
                    checknow = false;
                    mRecyclerView.setVisibility(View.GONE);
                    mrelative.setVisibility(View.VISIBLE);
                    tvtvnewname.setText("conversation with "+Name);
                }else {
                    checknow = true;
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mrelative.setVisibility(View.GONE);

                }

            }
        });
        ivcaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultpopupnew.defaultaddpopup(Mobile);
            }
        });
        mlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultpopupnew.defaultaddpopup(Mobile);
            }
        });

        tvcalltv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultpopupnew.defaultaddpopup(Mobile);
            }
        });

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        tvname = (TextView) findViewById(R.id.nametv);
        tvname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.calliv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultpopupnew.defaultaddpopup(Mobile);
            }
        });

        if (Name.equalsIgnoreCase("NA")) {
            tvname.setText(Mobile);
        } else {
            if (Name.length() != 0) {
                tvname.setText(Name);
            } else {
                tvname.setText(Mobile);
            }

        }


        myDbHandler = Temp.getMyDbHandler();
        if (productList.size() > 0) {
            productList.clear();
        }
        ArrayList<GetCallLogTable> arrayList = myDbHandler.viewCallalltRow(Mobile);


        Log.d("ContactDetailsActivityNew", "List 1 = " + arrayList.size());


        for (int i = 0; i < arrayList.size(); i++) {
            getbcdata = new GetContectData();

            Date dateObj = new Date(Long.parseLong(arrayList.get(i).getDate()));

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   ");
//            if(String.valueOf(formatter.format(dateObj)).equalsIgnoreCase(u)){
//
//            }
            if (i == 0) {
                datae = String.valueOf(formatter.format(dateObj));
                k = 0;
                u = 0;
                getbcdata.setCount(1);
                getbcdata.setName(arrayList.get(i).getName());
                getbcdata.setNumber(arrayList.get(i).getNop());
                getbcdata.setDate(arrayList.get(i).getDate());
                getbcdata.setTime(arrayList.get(i).getDuration());
                getbcdata.setCalltype(arrayList.get(i).getCall_type());
                productList.add(getbcdata);

            } else {
                if (datae.equalsIgnoreCase(String.valueOf(formatter.format(dateObj)))) {
                    k = k + 1;

                    getbcdata.setCount(k);
                    getbcdata.setName(arrayList.get(i).getName());
                    getbcdata.setNumber(arrayList.get(i).getNop());
                    getbcdata.setDate(arrayList.get(i).getDate());
                    getbcdata.setTime(arrayList.get(i).getDuration());
                    getbcdata.setCalltype(arrayList.get(i).getCall_type());
                    productList.set(u, getbcdata);
//                    adapter.notifyItemChanged(u);
                } else {
                    datae = String.valueOf(formatter.format(dateObj));
                    k = 0;
                    u = u + 1;
                    getbcdata.setCount(1);
                    getbcdata.setName(arrayList.get(i).getName());
                    getbcdata.setNumber(arrayList.get(i).getNop());
                    getbcdata.setDate(arrayList.get(i).getDate());
                    getbcdata.setTime(arrayList.get(i).getDuration());
                    getbcdata.setCalltype(arrayList.get(i).getCall_type());
                    productList.add(getbcdata);
                }
            }


        }
        adapter = new CallDetailsAdapter(ContactDetailsActivity.this, productList);
        mLayoutManager = new LinearLayoutManager(ContactDetailsActivity.this, LinearLayoutManager.VERTICAL, true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
        if(Mobile.length()>10){
            Mobile=Mobile.substring(Mobile.length() - 10);
            ArrayList<GetMessageTable> arrayMessageList = myDbHandler.viewmessageRow(Mobile);
            Log.d("ContactDetailsActivityNew", "List 2 = " + arrayMessageList.size());
            Log.d("ContactDetailsActivityNew", "List Mobile 2 = " + Mobile);
        }else {
            ArrayList<GetMessageTable> arrayMessageList = myDbHandler.viewmessageRow(Mobile);
            Log.d("ContactDetailsActivityNew", "List 2 = " + arrayMessageList.size());
            Log.d("ContactDetailsActivityNew", "List Mobile 2 = " + Mobile);
        }

    }


    public String datae;
    public int k = 0;
    public int u = 0;
}