package com.gpslab.kaun.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.evernote.android.job.JobManager;
import com.gpslab.kaun.BuildConfig;
import com.gpslab.kaun.R;
import com.gpslab.kaun.activity.PreferencesActivity;
import com.gpslab.kaun.activity.SenderActivity;
import com.gpslab.kaun.calling.AGEventHandler;
import com.gpslab.kaun.calling.MyEngineEventHandler;

import de.ub0r.android.logg0r.Log;
import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApp extends Application implements Application.ActivityLifecycleCallbacks  {
    /////////////////////

    /**
     * Tag for logging.
     */
    private static final String TAG = "app";

    static final String NOTIFICATION_CHANNEL_ID_MESSAGES = "messages";
    static final String NOTIFICATION_CHANNEL_ID_FAILD_SENDING_MESSAGE = "failed_sending_message";

    /**
     * Projection for checking {@link Cursor}.
     */
    private static final String[] PROJECTION = new String[]{"_id"};

    private MyEngineEventHandler mEventHandler;
    public void addEventHandler(AGEventHandler handler) {
        mEventHandler.addEventHandler(handler);
    }

    //////////////////
    private static MyApp mApp = null;
    private static String currentChatId = "";
    private static boolean chatActivityVisible;
    private static boolean phoneCallActivityVisible;
    private static boolean baseActivityVisible;
    private static boolean isCallActive = false;

    public static boolean isChatActivityVisible() {
        return chatActivityVisible;
    }

    public static String getCurrentChatId() {
        return currentChatId;
    }

    private boolean hasMovedToForeground = false;

    public boolean isHasMovedToForeground() {
        return hasMovedToForeground;
    }

    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;

    public static void chatActivityResumed(String chatId) {
        chatActivityVisible = true;
        currentChatId = chatId;
    }

    public static void chatActivityPaused() {
        chatActivityVisible = false;
        currentChatId = "";
    }

    public void removeEventHandler(AGEventHandler handler) {
        mEventHandler.removeEventHandler(handler);
    }
    public static boolean isPhoneCallActivityVisible() {
        return phoneCallActivityVisible;
    }

    public static void phoneCallActivityResumed() {
        phoneCallActivityVisible = true;
    }

    public static void phoneCallActivityPaused() {
        phoneCallActivityVisible = false;
    }


    public static boolean isBaseActivityVisible() {
        return baseActivityVisible;
    }

    public static void baseActivityResumed() {
        baseActivityVisible = true;
    }

    public static void baseActivityPaused() {
        baseActivityVisible = false;
    }


    public static void setCallActive(boolean mCallActive) {
        isCallActive = mCallActive;
    }

    public static boolean isIsCallActive() {
        return isCallActive;
    }
    private RtcEngine mRtcEngine;
    private EngineConfig mConfig;


    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public EngineConfig config() {
        return mConfig;
    }

    public static Context context() {
        return mApp.getApplicationContext();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        //add support for vector drawables on older APIs
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //init realm
        Realm.init(this);
        //init set realm configs
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(MyMigration.SCHEMA_VERSION)
                .migration(new MyMigration())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        //init shared prefs manager
        SharedPreferencesManager.init(this);
        //init evernote job
        JobManager.create(this).addJobCreator(new FireJobCreator());



        //initialize ads for faster loading in first time


        Log.i(TAG, "init SMSdroid v" + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE
                + ")");
        updateSenderStatus();
        setupNotificationChannels();
//        MobileAds.initialize(this, getString(R.string.admob_app_id));


        createRtcEngine();


        mApp = this;

    }


    private void updateSenderStatus() {
        // check for default app only when READ_SMS was granted
        // this may need a second launch on Android 6.0 though
        if (hasPermission(this, Manifest.permission.READ_SMS)) {
            final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
            int state = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
            if (p.getBoolean(PreferencesActivity.PREFS_ACTIVATE_SENDER, true)) {
                try {
                    Cursor c = getContentResolver().query(SenderActivity.URI_SENT, PROJECTION,
                            null, null, "_id LIMIT 1");
                    if (c == null) {
                        Log.i(TAG, "disable .Sender: cursor=null");
                    } else if (SmsManager.getDefault() == null) {
                        Log.i(TAG, "disable .Sender: SmsManager=null");
                    } else {
                        state = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
                        Log.d(TAG, "enable .Sender");
                    }
                    if (c != null && !c.isClosed()) {
                        c.close();
                    }
                } catch (IllegalArgumentException | SQLiteException e) {
                    Log.e(TAG, "disable .Sender: ", e.getMessage(), e);
                }
            } else {
                Log.i(TAG, "disable .Sender");
            }
            getPackageManager().setComponentEnabledSetting(
                    new ComponentName(this, SenderActivity.class), state,
                    PackageManager.DONT_KILL_APP);
        } else {
            Log.w(TAG, "ignore .Sender state, READ_SMS permission is missing to check default app");
        }
    }

    private void setupNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "Setting up notification channels");
            final NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_MESSAGES, getString(R.string.notification_channel_messages_name), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(getString(R.string.notification_channel_messages_description));
            channel.enableLights(true);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);

            channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_FAILD_SENDING_MESSAGE, getString(R.string.notification_channel_failed_sending_message_name), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(getString(R.string.notification_channel_failed_sending_message_description));
            channel.enableLights(true);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }
    }

    /**
     * Get an {@link View.OnClickListener} for stating an Activity for given {@link Intent}.
     *
     * @param context {@link Context}
     * @param intent  {@link Intent}
     * @return {@link View.OnClickListener}
     */
    static View.OnClickListener getOnClickStartActivity(final Context context, final Intent intent) {
        if (intent == null) {
            return null;
        }
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.w(TAG, "activity not found", e);
                    Toast.makeText(context, "no activity for data: " + intent.getType(),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    static boolean isDefaultApp(final Context context) {
        // there is no default sms app before android 4.4
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return true;
        }

        try {
            // check if this is the default sms app.
            // If the device doesn't support Telephony.Sms (i.e. tablet) getDefaultSmsPackage() will
            // be null.
            final String smsPackage = Telephony.Sms.getDefaultSmsPackage(context);
            return smsPackage == null || smsPackage.equals(BuildConfig.APPLICATION_ID);
        } catch (SecurityException e) {
            // some samsung devices/tablets want permission GET_TASKS o.O
            Log.e(TAG, "failed to query default SMS app", e);
            return true;
        }
    }

    static boolean hasPermission(final Context context, final String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    static boolean requestPermission(final Activity activity, final String permission,
                                     final int requestCode, final int message,
                                     final DialogInterface.OnClickListener onCancelListener) {
        Log.i(TAG, "requesting permission: " + permission);
        if (!hasPermission(activity, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.permissions_)
                        .setMessage(message)
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.cancel, onCancelListener)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface,
                                                        final int i) {
                                        ActivityCompat.requestPermissions(activity,
                                                new String[]{permission}, requestCode);
                                    }
                                })
                        .show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
            return false;
        } else {
            return true;
        }
    }





    private void createRtcEngine() {

        Context context = getApplicationContext();
        String appId = context.getString(R.string.agora_app_id);
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");




        }
        Log.d("CheckErrorCheck","Check Error  1    =    ");
        mEventHandler = new MyEngineEventHandler();
        try {
            // Creates an RtcEngine instance
            mRtcEngine = RtcEngine.create(context, appId, mEventHandler);
        } catch (Exception e) {
            Log.d("CheckErrorCheck","Check Error  2    =    "+ android.util.Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + android.util.Log.getStackTraceString(e));
        }

        /*
          Sets the channel profile of the Agora RtcEngine.
          The Agora RtcEngine differentiates channel profiles and applies different optimization
          algorithms accordingly. For example, it prioritizes smoothness and low latency for a
          video call, and prioritizes video quality for a video broadcast.
         */
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);


//        /*
//          Enables the onAudioVolumeIndication callback at a set time interval to report on which
//          users are speaking and the speakers' volume.
//          Once this method is enabled, the SDK returns the volume indication in the
//          onAudioVolumeIndication callback at the set time interval, regardless of whether any user
//          is speaking in the channel.
//         */
//        mRtcEngine.enableAudioVolumeIndication(200, 3, false);

        mConfig = new EngineConfig();
    }
}