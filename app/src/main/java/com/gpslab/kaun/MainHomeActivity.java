package com.gpslab.kaun;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.gpslab.kaun.Contact.ContactListActivity;
import com.gpslab.kaun.Contact.ContactListF;
import com.gpslab.kaun.Contact.ContactViewModel;
import com.gpslab.kaun.DB.FirstTableData;
import com.gpslab.kaun.DB.GetChatContactList;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.Home.GetCallLogTable;
import com.gpslab.kaun.Home.HomeFragment;
import com.gpslab.kaun.OTP.OTPActivity;
import com.gpslab.kaun.ProfileEdit.ProfileEditActivity;
import com.gpslab.kaun.Search.SearchActivity;
import com.gpslab.kaun.Service.CallLogService;
import com.gpslab.kaun.Service.GetMessageServices;
import com.gpslab.kaun.Service.MyService;
import com.gpslab.kaun.Splash.SplashActivity;
import com.gpslab.kaun.Webapi.ChatWebAPI;
import com.gpslab.kaun.Webapi.createnew;
import com.gpslab.kaun.block.ManageBlockingActivity;
import com.gpslab.kaun.faq.FAQActivity;
import com.gpslab.kaun.feedback.FeedbackActivity;
import com.gpslab.kaun.fragment.ChatFragment;
import com.gpslab.kaun.fragment.ContactListFragment;
import com.gpslab.kaun.fragment.FragmentTwo;
import com.gpslab.kaun.fragment.PermiumFragment;
import com.gpslab.kaun.fragment.SettingFragment;
import com.gpslab.kaun.internetcheck.InternetActivity;
import com.gpslab.kaun.language.LanguageActivity;
import com.gpslab.kaun.model.MessageInfo;
import com.gpslab.kaun.model.MessageUtils;
import com.gpslab.kaun.popup.checkpermissionpopup;
import com.gpslab.kaun.popup.profilepopup;
import com.gpslab.kaun.status.StatusFragmentCallbacks;
import com.gpslab.kaun.testing.TestingActivity;

import com.gpslab.kaun.upgrade_to_premium.UpgradePremiumActivity;
import com.gpslab.kaun.upgrade_to_premium.UpgradePremiumSecActivity;
import com.gpslab.kaun.view.Message;
import com.gpslab.kaun.view.MessageStat;
import com.gpslab.kaun.view.MessageType;
import com.gpslab.kaun.view.PhoneNumber;
import com.gpslab.kaun.view.RealmContact;
import com.gpslab.kaun.view.RealmHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainHomeActivityMainHomeActivity";
    public static final int ITEMS_PER_AD = 8;
    private Toolbar toolbar;
    Context context;
    private static ChatWebAPI addpopup;
    private ViewPager mViewPager;
    String[] appPermissions = {Manifest.permission.INTERNET,
            Manifest.permission.READ_CALL_LOG,
//            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.MODIFY_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SYSTEM_ALERT_WINDOW};
    private static final int PERMISSION_REQUEST_CODE = 1240;

    public void datagetnewapi(String chatid) {
        Toast.makeText(this, "Data Get in Socket = " + chatid, Toast.LENGTH_SHORT).show();
    }

    String NOTIFICATION_CHANNEL_ID = "examplde.c";
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                    .setSmallIcon(R.drawable.airtel)
//                    .setContentTitle("Call from Lucky Agarwal")
//                    .setContentText("Identified by Kaun")
//                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.airtel))
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(1, builder.build());
//            // or other notification behaviors after this
//            NotificationManager notificationManagers = getSystemService(NotificationManager.class);
//            notificationManagers.createNotificationChannel(channel);
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d("WalletlsjWallet","onResume");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d("WalletlsjWallet","onPause");
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = this;
        ExitActivity.exitApplication(context);
        Log.d("WalletlsjWallet", "onDestroy");
    }

    private static checkpermissionpopup profilepopupnew;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public LinearLayout llonell;
    public TextView tvonetv;
    public MyDbHandler myDbHandler;

    private List<CallLogInfo> filtered_icontacts = new ArrayList<>();
    private List<MessageInfo> filtered_imessage = new ArrayList<>();
    private ContactViewModel contactViewModel;

    String insertData = "";
    String insertSecData = "";

    String insertMes = "";
    String insertMesData = "";
    public String numbers;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String FName = sharedPreferences.getString("fname", "xyz");
                FName = FName.substring(0, 1).toUpperCase() + FName.substring(1);
                String LName = sharedPreferences.getString("lname", "xyz");
                LName = LName.substring(0, 1).toUpperCase() + LName.substring(1);
                tvName.setText(String.valueOf(FName + " " + LName));
                tvMobile.setText(String.valueOf(sharedPreferences.getString("mobile", "0000")));

                Glide.with(MainHomeActivity.this)
                        .load(sharedPreferences.getString("img", "xyz"))
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(ivuser);
            }
        }
    }

    private TextView tvName,tvMobile;
    private View headerView;
    private CircleImageView ivuser;
    private NavigationView navigationView;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.drawablelayout);
//        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (checkAndRequestPermissions()) {
            // All permission are granted already. Proceed ahead
        }
//        addpopup = new ChatWebAPI(MainHomeActivity.this);
//        addpopup.data();


        MobileAds.initialize(MainHomeActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        profilepopupnew = new checkpermissionpopup(MainHomeActivity.this);


        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        String check_story = sharedPreferences.getString("story_check","10");



        edit.putString("story_url","https://gpslabindia.nyc3.digitaloceanspaces.com/callify/upload/story/");

        if(check_story.equalsIgnoreCase("10")){
            edit.putString("story_check","0");
        }else {

        }

        edit.apply();


        myDbHandler = new MyDbHandler(getApplicationContext(), "userbd", null, 1);
        Temp.setMyDbHandler(myDbHandler);


//        ArrayList<GetCallLogTable> arrayList = myDbHandler.viewCallLogsNew();
//        ArrayList<GetCallLogTable> arrayListsnew = myDbHandler.viewCallLogsFirstRow();
//        Log.d("dfMainHomeActivityhsdfsdfdfsd", "GetContacts inseart check = " + arrayList.size());
//        Log.d("dfMainHomeActivityhsdfsdfdfsd", "GetContacts inseart New = " + arrayListsnew.size());

        myDbHandler.deletecalllogAll();
        myDbHandler.deletemessageAll();

        ArrayList<GetCallLogTable> arrayList2 = myDbHandler.viewCallLogsNew();
        ArrayList<GetCallLogTable> arrayListsnew2 = myDbHandler.viewCallLogsFirstRow();
        Log.d("dfMainHomeActivityhsdfsdfdfsd", "GetContacts inseart check 2 = " + arrayList2.size());
        Log.d("dfMainHomeActivityhsdfsdfdfsd", "GetContacts inseart New 2 = " + arrayListsnew2.size());


        initRecyclerView();


//
//        Intent servicexc = new Intent(MainHomeActivity.this, CallLogService.class);
//        startService(servicexc);


//
//        Intent servicemessage = new Intent(MainHomeActivity.this, GetMessageServices.class);
//        startService(servicemessage);


//        ArrayList<GetCallLogTable> arrayList3 = myDbHandler.viewCallLogsNew();
//        ArrayList<GetCallLogTable> arrayListsnew3 = myDbHandler.viewCallLogsFirstRow();
//        Log.d("dfMainHomeActivityhsdfsdfdfsd", "GetContacts inseart check 3 = " + arrayList3.size());
//        Log.d("dfMainHomeActivityhsdfsdfdfsd", "GetContacts inseart New 3 = " + arrayListsnew3.size());

        llonell = (LinearLayout) findViewById(R.id.llone);
        tvonetv = (TextView) findViewById(R.id.tvone);
        llonell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainHomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        tvonetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainHomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(MainHomeActivity.this, MyService.class));
        } else {
            startService(new Intent(MainHomeActivity.this, MyService.class));
        }
//        createchannel();
//        createNotificationChannel();

        /// Local Notification
//        and5_notificaiton();

        if (sharedPreferences.getBoolean("defaultnewnew", false)) {

        } else {
            profilepopupnew.profileaddpopup();
        }

        // Local Notification
        check_read_message();

//        if (!Settings.canDrawOverlays(this)) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, 0);
//        }

        Intent service = new Intent(MainHomeActivity.this, MyBrodcastRecieverService.class);
        startService(service);
        navigationView = findViewById(R.id.nav_view_main);
        headerView = navigationView.getHeaderView(0);
        tvName = (TextView) headerView.findViewById(R.id.tvname);
        tvMobile = (TextView) headerView.findViewById(R.id.mobiletv);
        ivuser = (CircleImageView) headerView.findViewById(R.id.profileimage);
        imageView = (ImageView) headerView.findViewById(R.id.editiv);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainHomeActivity.this, ProfileEditActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        String FName = sharedPreferences.getString("fname", "xyz");
        FName = FName.substring(0, 1).toUpperCase() + FName.substring(1);
        String LName = sharedPreferences.getString("lname", "xyz");
        LName = LName.substring(0, 1).toUpperCase() + LName.substring(1);
        tvName.setText(String.valueOf(FName + " " + LName));
        tvMobile.setText(String.valueOf(sharedPreferences.getString("mobile", "0000")));

        Glide.with(MainHomeActivity.this)
                .load(sharedPreferences.getString("img", "xyz"))
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(ivuser);
        navigationView.setCheckedItem(R.id.menu_home);
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_item);


        navigationView.setNavigationItemSelectedListener(this);


        toolbar = findViewById(R.id.toolbar);

//        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(Color.BLACK);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.ic_arrow:
                        selectedFragment = new HomeFragment();

                        break;

                    case R.id.ic_android:
//                        selectedFragment = new ContactListFragment();
                        selectedFragment = new ContactListF();
                        break;

                    case R.id.ic_books:
                        selectedFragment = new PermiumFragment();
                        break;

                    case R.id.ic_center_focus:
                        selectedFragment = new ChatFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();


                return true;
            }
        });
//        ChatWebAPI addpopup = new ChatWebAPI(MainHomeActivity.this);
//        addpopup.data();





        new CountDownTimer(300000000, 25000) {

            public void onTick(long millisUntilFinished) {

                ChatWebAPI addpopup = new ChatWebAPI(MainHomeActivity.this);
                addpopup.data();
                addpopup.reconnect();
            }

            public void onFinish() {

            }

        }.start();
    }


    public String contact_number;
    RealmResults<Message> messageList;

    public boolean checkNUmber = true;

    int number_new = 0;
    public String replay_id="NA";

    private void Recursion(ArrayList<GetChatContactList> pathList, int number) throws IOException {

        Log.d("PandingMessage", "Recursion    number_new  starting   =   " + number);

        if (pathList.size() > number) {
            if (checkNUmber) {
                contact_number = pathList.get(number).getContacts_id();
                messageList = RealmHelper.getInstance().getMessagesInChat(contact_number);

                Log.d("PandingMessage", "messageList Size  =   " + messageList.size() + "     contact_number   =   " + contact_number + "    number_new   =   " + number);

                if (messageList.size() > 0) {
                    for (int i = 0; i < messageList.size(); i++) {
                        int stat = messageList.get(i).getMessageStat();

                        if (stat == MessageStat.PENDING) {
                            Log.d("PandingMessage", "MessageStat   =  PENDING ");

                            if (MessageType.SENT_TEXT == messageList.get(i).getType()) {
                                if (checkNUmber) {
                                    Log.d("PandingMessage", "SENT_TEXT Content  =  " + messageList.get(i).getContent());
                                    ChatWebAPI addpopup = new ChatWebAPI(MainHomeActivity.this);
                                    addpopup.sendmessage(contact_number, messageList.get(i).getContent(), MessageType.SENT_TEXT, messageList.get(i).getMessageId(),"NA");
                                }
                            } else if (MessageType.SENT_AUDIO == messageList.get(i).getType()) {

                                if (checkNUmber) {
                                    String filepath = messageList.get(i).getLocalPath();

                                    String[] separated = filepath.split("\\.");

                                    String extenstion = separated[1];


                                    ChatWebAPI addpopup = new ChatWebAPI(MainHomeActivity.this);
                                    addpopup.sendmessage(messageList.get(i).getToId(), messageList.get(i).getMessageId() + "." + extenstion + "," + messageList.get(i).getMediaDuration(), MessageType.SENT_AUDIO, messageList.get(i).getMessageId(),"NA");
                                }
                            } else if (MessageType.SENT_CONTACT == messageList.get(i).getType()) {
                                if (checkNUmber) {

                                    RealmContact realmContact = messageList.get(i).getContact();
                                    String title = realmContact.getName();
                                    RealmList<PhoneNumber> numbers = realmContact.getRealmList();
                                    String number_your = numbers.get(0).getNumber();

                                    ChatWebAPI addpopup = new ChatWebAPI(MainHomeActivity.this);
                                    addpopup.sendcontact(messageList.get(i).getToId(), number_your, title, MessageType.SENT_CONTACT, messageList.get(i).getMessageId());
                                }
                            } else if (MessageType.SENT_FILE == messageList.get(i).getType()) {
                                if (checkNUmber) {
                                    String filepath = messageList.get(i).getLocalPath();

                                    String[] separated = filepath.split("\\.");

                                    String extenstion = separated[1];
                                    ChatWebAPI addpopup = new ChatWebAPI(MainHomeActivity.this);

                                    addpopup.sendmessageDoc(messageList.get(i).getToId(),
                                            messageList.get(i).getMessageId() + "." + extenstion + "," + messageList.get(i).getFileSize(),
                                            MessageType.SENT_FILE, messageList.get(i).getMessageId(),
                                            0,
                                            1);
                                }
                            } else if (MessageType.SENT_IMAGE == messageList.get(i).getType()) {
                                if (checkNUmber) {
                                    String filepath = messageList.get(i).getLocalPath();

                                    String[] separated = filepath.split("\\.");

                                    String extenstion = separated[1];


                                    ChatWebAPI addpopup = new ChatWebAPI(MainHomeActivity.this);
                                    addpopup.sendmessageImage(messageList.get(i).getToId(), messageList.get(i).getMessageId() + "." + extenstion + "," + messageList.get(i).getMetadata(), MessageType.SENT_IMAGE, 0, 1, messageList.get(i).getMessageId(),replay_id);


                                }
                            } else if (MessageType.SENT_LOCATION == messageList.get(i).getType()) {
                                if (checkNUmber) {
                                    Double latitude = messageList.get(i).getLocation().getLat();
                                    Double longitude = messageList.get(i).getLocation().getLng();

                                    Geocoder geocoder;
                                    List<Address> addresses;
                                    geocoder = new Geocoder(this, Locale.getDefault());

                                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                    String address = addresses.get(0).getAddressLine(0);


                                    ChatWebAPI addpopup = new ChatWebAPI(MainHomeActivity.this);
                                    addpopup.sendlocation(messageList.get(i).getToId(), String.valueOf(latitude) + ";" + String.valueOf(longitude) + ";" + address, MessageType.SENT_LOCATION, messageList.get(i).getMessageId());
                                }
                            } else if (MessageType.SENT_VIDEO == messageList.get(i).getType()) {
                                if (checkNUmber) {
                                    String filepath = messageList.get(i).getLocalPath();

                                    String[] separated = filepath.split("\\.");

                                    String extenstion = separated[1];


                                    ChatWebAPI addpopup = new ChatWebAPI(MainHomeActivity.this);
                                    addpopup.sendmessageVideo(messageList.get(i).getToId(), messageList.get(i).getMessageId() + "." + extenstion + "," + messageList.get(i).getMetadata(), MessageType.SENT_VIDEO, messageList.get(i).getMessageId(), 0, 1);
                                }
                            }
                        }


                        if (i == messageList.size() - 1) {
                            try {
//                            Log.d("PandingMessage","Recursion    number_new  starting 1   =   ");
                                Recursion(pathList, number + 1);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    try {

                        Recursion(pathList, number + 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        } else {
            checkNUmber = false;
            Log.d("PandingMessage", "Recursion      Brack   =   ");
        }
    }

    private void check_read_message() {
        myDbHandler = new MyDbHandler(getApplicationContext(), "userbd", null, 1);
        Temp.setMyDbHandler(myDbHandler);
        ArrayList<GetChatContactList> arrayList2 = myDbHandler.viewChatContact();
//        Log.d("PandingMessage","Db Size  =   "+arrayList2.size());

        if (arrayList2.size() > 0) {
            try {
                Recursion(arrayList2, number_new);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void initMessageView() {

        contactViewModel = new ContactViewModel(getApplicationContext());

        setMessage(contactViewModel.getMessage());

    }

    public void setMessage(List<MessageInfo> message) {
        Log.d("GetAllMessage", "CallLogService size = " + message.size());
        this.filtered_imessage = message;
        for (int i = 0; i < message.size(); i++) {
            onBindMessage(filtered_imessage.get(i), i);
        }
        insertMesData = insertMesData.substring(0, insertMesData.length() - 1);
        Log.i("WalletCheckItsComponents", "CallLogService inseart check = " + insertMesData);
        myDbHandler.InsertMessageSQLiteDatabase(insertMesData);
    }

    private void onBindMessage(MessageInfo messageInfo, int i) {

        String id = messageInfo.getId();
        insertMes = insertMes + "'" + id + "'";


        String Read = messageInfo.getRead();
        insertMes = insertMes + "," + "'" + Read + "'";


        String Address = messageInfo.getAddress();
        insertMes = insertMes + "," + "'" + Address + "'";


        String Bodydd = messageInfo.getBody();
        insertMes = insertMes + "," + "'" + Bodydd + "'";


        String Dates = messageInfo.getDate();
        insertMes = insertMes + "," + "'" + Dates + "'";

        insertMes = insertMes + "," + "'" + "NA" + "'";
        insertMes = insertMes + "," + "'" + "NA" + "'";
        insertMesData = insertMesData + "(" + insertMes + ")" + ",";
        insertMes = "";
    }


    private void initRecyclerView() {
        contactViewModel = new ContactViewModel(getApplicationContext());

        setContacts(contactViewModel.getCallLog());
    }

    public void setContacts(List<CallLogInfo> contacts) {

        this.filtered_icontacts = contacts;
        for (int i = 0; i < contacts.size(); i++) {

            onBind(filtered_icontacts.get(i), i);
        }
        insertSecData = insertSecData.substring(0, insertSecData.length() - 1);
        Log.d("dfMainHomeActivityhsdfsdfdfsd", "CallLogService inseart check = " + insertSecData);
        myDbHandler.InsertLogCallerDataIntoSQLiteDatabase(insertSecData);


        initMessageView();

    }

    private void onBind(CallLogInfo callLogInfo, int i) {
        Log.d("dfsdfsdfsdfdfsdd", "GetContacts fname = = " + callLogInfo.getName());


        if (TextUtils.isEmpty(callLogInfo.getName())) {

            insertData = insertData + "'" + "NA" + "'";

        } else {
            String Name = callLogInfo.getName();
            Name = Name.replace("'", "");

            insertData = insertData + "'" + Name + "'";

        }

        insertData = insertData + "," + "'" + String.valueOf(i) + "'";

//        Log.i("CheckNumberlog","number    =      "+callLogInfo.getNumber().charAt(0));






        if(callLogInfo.getNumber().length() != 0){
            if(String.valueOf(callLogInfo.getNumber().charAt(0)).equalsIgnoreCase("*")){

            }else {
                if (callLogInfo.getNumber().length() > 10) {
                    numbers = callLogInfo.getNumber();
                    numbers = numbers.replace("&#34", "");
                    numbers = numbers.replace("&#42;", "");
                    numbers = numbers.substring(numbers.length() - 10).toString();
                } else {
                    numbers = callLogInfo.getNumber();
                    numbers = numbers.replace("&#34", "");
                    numbers = numbers.replace("&#42;", "");

                }
            }
        }


        insertData = insertData + "," + "'" + numbers + "'";


        if (callLogInfo.getCallType().equalsIgnoreCase("null")) {

            insertData = insertData + "," + "'" + "NA" + "'";
        } else {
            insertData = insertData + "," + "'" + callLogInfo.getCallType() + "'";
        }

        insertData = insertData + "," + "'" + "NA" + "'";

        insertData = insertData + "," + "'" + String.valueOf(callLogInfo.getDate()) + "'";

        if(callLogInfo.getImage() !=null){
            insertData = insertData + "," + "'" + callLogInfo.getImage() + "'";

        }else {
            insertData = insertData + "," + "'" + "NA" + "'";
        }

        insertData = insertData + "," + "'" + "0" + "'";
        insertData = insertData + "," + "'" + String.valueOf(callLogInfo.getDuration()) + "'";
        insertSecData = insertSecData + "(" + insertData + ")" + ",";
        insertData = "";
    }


    public void checkpermissdfsdfssion() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        }
    }

    private boolean checkAndRequestPermissions() {
        // Check which Permissions are granted (Check करें कि कौन सी Permissions Granted है।)


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected() )
        {

        }

        else
        {
            Intent intent = new Intent(MainHomeActivity.this,InternetActivity.class);
            intent.putExtra("msg","No Internet");
            startActivity(intent);
        }




        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);

//                Log.d("CheckPermissionLucky","Yes");
            }
        }
        // Ask for non-granted permissions
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
//            Log.d("CheckPermissionLucky","No");
            return false;
        }
        ;
        return true;
    }
    private RewardedAd mRewardedAd;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            Intent intent1 = new Intent(MainHomeActivity.this, UpgradePremiumSecActivity.class);
            startActivity(intent1);
            return true;

        } else if (id == R.id.menu_hubdetails) {



//            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                @Override
//                public void onAdShowedFullScreenContent() {
//                    // Called when ad is shown.
//                    Log.d(TAG, "Ad was shown.");
//                    mRewardedAd = null;
//                }
//
//                @Override
//                public void onAdFailedToShowFullScreenContent(AdError adError) {
//                    // Called when ad fails to show.
//                    Log.d(TAG, "Ad failed to show.");
//                }
//
//                @Override
//                public void onAdDismissedFullScreenContent() {
//                    // Called when ad is dismissed.
//                    // Don't forget to set the ad reference to null so you
//                    // don't show the ad a second time.
//                    Log.d(TAG, "Ad was dismissed.");
//                }
//            });
            Intent intent1 = new Intent(MainHomeActivity.this, UpgradePremiumActivity.class);
            startActivity(intent1);
            return true;

        }else if (id == R.id.menu_run_send_feedback) {
            Intent intent1 = new Intent(MainHomeActivity.this, FeedbackActivity.class);
            startActivity(intent1);
            return true;

        }



        else if (id == R.id.menu_run_sheet) {
            Intent intent1 = new Intent(MainHomeActivity.this, ManageBlockingActivity.class);
            startActivity(intent1);

            return true;


        } else if (id == R.id.menu_faq) {
            Intent intent = new Intent(MainHomeActivity.this, FAQActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_contact_us) {
//            Intent intent6 = new Intent(HomeActivity.this, ContactUsActivity.class);
//            startActivity(intent6);
//            finish();
            return true;
        } else if (id == R.id.menu_logout) {
            edit.clear();
            edit.apply();
            Toast.makeText(MainHomeActivity.this, "getData", Toast.LENGTH_SHORT).show();
            Intent intent6 = new Intent(MainHomeActivity.this, SplashActivity.class);
            startActivity(intent6);
            finish();
            return true;
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_navigation_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_default_sms_app:
                Toast.makeText(this, "change_default_sms_app", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.mark_all_as_read:
                Toast.makeText(this, "mark_all_as_read", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ic_books:
                Toast.makeText(this, "ic_books", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ic_center_focus:
                Toast.makeText(this, "ic_center_focus", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}