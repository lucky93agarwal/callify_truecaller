package com.gpslab.kaun.Service;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.gpslab.kaun.MyBrodcastRecieverService;
import com.gpslab.kaun.Notification.UserProfile;
import com.gpslab.kaun.R;
import com.gpslab.kaun.Webapi.createnew;
import com.gpslab.kaun.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketServiceProvider extends Service {
    private Application signalApplication;
    //    private Realm       realm;
    public Socket mSocket;
    private Handler mHandler = new Handler();
    public static SocketServiceProvider instance = null;

    public static boolean isInstanceCreated() {
        return instance == null ? false : true;
    }

    private final IBinder myBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public SocketServiceProvider getService() {
            return SocketServiceProvider.this;
        }
    }

    public void IsBendable() {
    }
    private Timer mTimer = null;
    long notify_interval = 60000;
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    @Override
    public void onCreate() {

        super.onCreate();
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);


    }



    private class TimerTaskToGetLocation extends TimerTask {

        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                    edit = sharedPreferences.edit();
                    {
                        try {
                            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    mSocket.on(Socket.EVENT_CONNECT, onConnect);
                    mSocket.connect();


                }
            });

        }
    }
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.d("WalletlsjWallet", "onConnect 1 = ");

            Log.d("WalletlsjWallet", "emit = "+sharedPreferences.getString("mobile",""));
            mSocket.emit("user_connected", sharedPreferences.getString("mobile",""));

        }
    };



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, SocketServiceProvider.class);
        startService(intent);

    }

}
