package com.gpslab.kaun.model;

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
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.MobileAds;
import com.gpslab.kaun.BuildConfig;
import com.gpslab.kaun.R;
import com.gpslab.kaun.activity.PreferencesActivity;

import de.ub0r.android.logg0r.Log;

public final class SMSdroid extends Application {

    /**
     * Tag for logging.
     */
    private static final String TAG = "app";

    public static final String NOTIFICATION_CHANNEL_ID_MESSAGES = "messages";
    public static final String NOTIFICATION_CHANNEL_ID_FAILD_SENDING_MESSAGE = "failed_sending_message";

    /**
     * Projection for checking {@link Cursor}.
     */
    private static final String[] PROJECTION = new String[]{"_id"};

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "init SMSdroid v" + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE
                + ")");
//        updateSenderStatus();
        setupNotificationChannels();
//        MobileAds.initialize(this, getString(R.string.admob_app_id));
    }

//    private void updateSenderStatus() {
//        // check for default app only when READ_SMS was granted
//        // this may need a second launch on Android 6.0 though
//        if (hasPermission(this, Manifest.permission.READ_SMS)) {
//            final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
//            int state = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
//            if (p.getBoolean(PreferencesActivity.PREFS_ACTIVATE_SENDER, true)) {
//                try {
//                    Cursor c = getContentResolver().query(SenderActivity.URI_SENT, PROJECTION,
//                            null, null, "_id LIMIT 1");
//                    if (c == null) {
//                        Log.i(TAG, "disable .Sender: cursor=null");
//                    } else if (SmsManager.getDefault() == null) {
//                        Log.i(TAG, "disable .Sender: SmsManager=null");
//                    } else {
//                        state = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
//                        Log.d(TAG, "enable .Sender");
//                    }
//                    if (c != null && !c.isClosed()) {
//                        c.close();
//                    }
//                } catch (IllegalArgumentException | SQLiteException e) {
//                    Log.e(TAG, "disable .Sender: ", e.getMessage(), e);
//                }
//            } else {
//                Log.i(TAG, "disable .Sender");
//            }
//            getPackageManager().setComponentEnabledSetting(
//                    new ComponentName(this, SenderActivity.class), state,
//                    PackageManager.DONT_KILL_APP);
//        } else {
//            Log.w(TAG, "ignore .Sender state, READ_SMS permission is missing to check default app");
//        }
//    }

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
    public static View.OnClickListener getOnClickStartActivity(final Context context, final Intent intent) {
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

    public static boolean isDefaultApp(final Context context) {
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

    public static boolean requestPermission(final Activity activity, final String permission,
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
}
