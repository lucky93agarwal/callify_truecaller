package com.gpslab.kaun;

import android.annotation.TargetApi;
import android.app.Notification;
import com.gpslab.kaun.Webapi.createnew;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.gpslab.kaun.model.Constants;

import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyBrodcastRecieverService extends Service {

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;
    private Intent forService;
    private static BroadcastReceiver br_ScreenOffReceiver;

    private Socket mSocket;

    private static final String URL = "http://socket.hwashsanitizer.com:8082";


    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 60000;
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;


    private static createnew addpopup;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public Context context;
//
//    {
//        try {
//            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    @Override
    public void onCreate() {
        context = this;
        Log.d("CheckPermissionLucky", "Context = ");



        sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Intent intent = new Intent(context, CallReceiver.class);
                sendBroadcast(intent);
                Log.d("WalletWalletServices","Services Running");
            }
        }, 5000);
//        addpopup = new createnew(context);
//        addpopup.data();

//        mSocket.on(Socket.EVENT_CONNECT, onConnect);
//        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
//        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
////        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.on("new_message", onNewMessage);
//        mSocket.on("user joined", onUserJoined);
//        mSocket.on("user left", onUserLeft);
//        mSocket.on("typing", onTyping);
//        mSocket.on("stop typing", onStopTyping);
//        mSocket.connect();
//        Log.d("WalletLucky", "onConnect v7 = "+mSocket.connected());
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
        intent = new Intent(str_receiver);
        Log.d("WalletlsjWallet","datanew = service on ");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
//            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }
        registerScreenOffReceiver();
    }







    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    private class TimerTaskToGetLocation extends TimerTask {

        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("CheckPermissionLucky", "Context = 2");
                        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {

                            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
                            Log.d("CheckPermissionLucky", "savedNumber =  " + savedNumber);
                        } else {

                            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                            int state = 0;
                            Log.d("CheckPermissionLucky", "number =  " + number);
                            Log.d("CheckPermissionLucky", "stateStr =  " + stateStr);
                            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                                state = TelephonyManager.CALL_STATE_IDLE;
                            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                                state = TelephonyManager.CALL_STATE_OFFHOOK;
                            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                                state = TelephonyManager.CALL_STATE_RINGING;
                            }

                            onCallStateChanged(context, state, number);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                }
            });

        }
    }


    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
    }

    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    public void onCallStateChanged(Context context, int state, String number) {
        if (number.length() >= 10) {

//
//            final Intent intent = new Intent(context, IncomingCallActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            intent.putExtra("phone_no", number);
//            context.startActivity(intent);


            Log.d("CheckPermissionLucky", "PhoneCallReceiver 736 q =  Mobile No =  " + number + " State = " + state + " lastState = " + lastState);
        }

        if (lastState == state) {

            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
//                Log.d("LuckyYUYTUNow","PhoneCallReceiver 736 q =  CALL_STATE_RINGING ");
                onIncomingCallStarted(context, number, callStartTime);

                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
//                Log.d("LuckyYUYTUNow","PhoneCallReceiver 736 q =  CALL_STATE_OFFHOOK ");
                if (isIncoming) {
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());

                }

            case TelephonyManager.CALL_STATE_IDLE:
//                Log.d("LuckyYUYTUNosdfw","PhoneCallReceiver 736 q =  CALL_STATE_IDLE =  "+isIncoming+ "  Contest  =  "+context);
                if (isIncoming) {
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());


                }
        }
        lastState = state;
    }

    @Override
    public void onDestroy() {
        Log.d("WalletlsjWallet","datanew = onDestroy on ");
        Log.d("WalletWalletServices","Services Stop Running");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, CallReceiver.class);
        this.sendBroadcast(broadcastIntent);

    }

    private void registerScreenOffReceiver() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
        intent = new Intent(str_receiver);
        return START_STICKY;
    }


}
