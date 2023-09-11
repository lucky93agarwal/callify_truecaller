package com.gpslab.kaun.Webapi;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.graphics.SweepGradient;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.MyBrodcastRecieverService;
import com.gpslab.kaun.Notification.UserProfile;
import com.gpslab.kaun.R;
import com.gpslab.kaun.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class createnew {
    public Socket mSocket;

    private final Context _context;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public String MobileNo;
    public createnew(Context context) {
        this._context = context;
    }
    public void data(){
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();


        {
            try {
                mSocket = IO.socket(Constants.CHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new_message", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.on("typing", onTyping);
        mSocket.on("stop typing", onStopTyping);
        mSocket.connect();
        Log.d("WalletLucky", "onConnect v7 = "+mSocket.connected());
    }


    public void sendmessage(String number){
        Log.d("Call_Krne_ke_time_api","Emit 1 = "+number);




        {
            try {
                mSocket = IO.socket(Constants.CHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("Call_Krne_ke_time_api","Emit = "+number);
        Log.d("Call_Krne_ke_time_api","connected 1 = "+"hello");
        mSocket.emit("send_message","calling");
        mSocket.emit("calling", number );

    }





    public void verify_user(String number){
        Log.d("WalletlsjWallet","Emit 1 = "+number);




        {
            try {
                mSocket = IO.socket(Constants.CHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("WalletlsjWallet","connected 1 = "+"hello");
        mSocket.emit("send_message","calling");
        mSocket.emit("calling", number );

    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.d("WalletlsjWallet", "onConnect 1 = ");

            Log.d("WalletlsjWallet", "emit = "+sharedPreferences.getString("mobile",""));
            mSocket.emit("user_connected", sharedPreferences.getString("mobile",""));

        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
            Log.d("WalletLucky", "diconnected = ");
            Log.i("TAG", "diconnected");
//                    isConnected = false;
//            Toast.makeText(context,
//                    R.string.disconnect, Toast.LENGTH_LONG).show();
//                }
//            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
            Log.d("WalletLucky", "Error connecting = " + args[0]);
            Log.e("6", "Error connecting");
//            Toast.makeText(context,
//                    args[0] + " 1 ", Toast.LENGTH_LONG).show();
//
//                }
//            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
            Log.d("WalletlsjWallet", "onNewMessage = " + String.valueOf(args[0]));

            JSONObject data = (JSONObject) args[0];
            String name;
            String email;
            String mobile;

            String images;
            String provider;
            try {
                Username = data.getString("name");
                email = data.getString("email");
                MobileNos = data.getString("mobile");

                images = data.getString("image");
                SharedPreferences sharedPreferences = _context.getSharedPreferences("newdata",Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("mobile",MobileNos);
                edit.putString("name",Username);
                edit.putString("email",email);
                edit.putString("image",Constants.CHAT_IMAGE_URL + images);
                edit.apply();
                Log.d("WalletLucky", "onNewMessage = Username"+Username );
                Log.d("WalletLucky", "onNewMessage = MobileNos "+MobileNos );
                nm = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
                createchannel();


                if(images.length() == 0){
                    Log.d("WalletLucky", "onNewMessage = image not " );
                    and5_notificaiton(Username, MobileNos);
                }else {
                    imageurl = Constants.CHAT_IMAGE_URL + images;
                    Log.d("WalletLucky", "onNewMessage = image url "+imageurl );
                    sendNotification login = new sendNotification(_context);
                    login.execute();
                }

//                createNotificationChannel(name);
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }

//                    removeTyping(username);
//                    addMessage(username, message);
//                }
//            });
        }
    };
//    private Emitter.Listener onNewMessagesend = new Emitter.Listener() {
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        @Override
//        public void call(final Object... args) {
//
//
//
//
//
//        }
//    };
    public String Username;
    public String MobileNos;
    public String imageurl;
    private class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String message;

        public sendNotification(Context context) {
            super();
            this.ctx = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
//            message = params[0] + params[1];
            try {

                java.net.URL url = new URL(imageurl);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            and5_notificaitonbitmap(Username,getCircleBitmap(result),MobileNos);

        }
    }
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output;
        Rect srcRect, dstRect;
        float r;
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        if (width > height){
            output = Bitmap.createBitmap(height, height, Bitmap.Config.ARGB_8888);
            int left = (width - height) / 2;
            int right = left + height;
            srcRect = new Rect(left, 0, right, height);
            dstRect = new Rect(0, 0, height, height);
            r = height / 2;
        }else{
            output = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
            int top = (height - width)/2;
            int bottom = top + width;
            srcRect = new Rect(0, top, width, bottom);
            dstRect = new Rect(0, 0, width, width);
            r = width / 2;
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, srcRect, dstRect, paint);

        bitmap.recycle();

        return output;
    }
    int NotID = 1;
    public static String id1 = "test_channel_01";
    public static String id2 = "test_channel_02";
    public static String id3 = "test_channel_03";
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void and5_notificaiton(String names,String mobile) {
        Intent notificationIntent = new Intent(_context.getApplicationContext(), UserProfile.class);
        notificationIntent.putExtra("mytype", "iconmsg" + NotID);
        notificationIntent.putExtra("mobile",mobile);
        notificationIntent.putExtra("name",names);
        PendingIntent contentIntent = PendingIntent.getActivity(_context, NotID, notificationIntent, 0);
        Notification noti = new NotificationCompat.Builder(_context.getApplicationContext(), id3)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.amazon)
                .setWhen(System.currentTimeMillis())  //When the event occurred, now, since noti are stored by time.
                .setContentTitle("Call from "+names)   //Title message top row.
                .setContentText("Identified by Kaun")  //message when looking at the notification, second row
                //the following 2 lines cause it to show up as popup message at the top in android 5 systems.
                .setPriority(Notification.PRIORITY_MAX)  //could also be PRIORITY_HIGH.  needed for LOLLIPOP, M and N.  But not Oreo
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLargeIcon(BitmapFactory.decodeResource(_context.getResources(), R.drawable.profile))//for the heads/pop up must have sound or vibrate
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)  //VISIBILITY_PRIVATE or VISIBILITY_SECRET
                .setContentIntent(contentIntent)  //what activity to open.
                .setAutoCancel(true)   //allow auto cancel when pressed.
                .setChannelId(id3)  //Oreo notifications
                .build();  //finally build and return a Notification.

        //Show the notification
        nm.notify(NotID, noti);
        NotID++;
    }

    public void and5_notificaitonbitmap(String names,Bitmap bitmap,String mobile) {
        Intent notificationIntent = new Intent(_context.getApplicationContext(), UserProfile.class);
        notificationIntent.putExtra("mytype", "iconmsg" + NotID);
        notificationIntent.putExtra("mobile",mobile);
        notificationIntent.putExtra("name",names);

        PendingIntent contentIntent = PendingIntent.getActivity(_context, NotID, notificationIntent, 0);
        Notification noti = new NotificationCompat.Builder(_context.getApplicationContext(), id3)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.amazon)
                .setWhen(System.currentTimeMillis())  //When the event occurred, now, since noti are stored by time.
                .setContentTitle("Call from "+names)   //Title message top row.
                .setContentText("Identified by Kaun")  //message when looking at the notification, second row
                //the following 2 lines cause it to show up as popup message at the top in android 5 systems.
                .setPriority(Notification.PRIORITY_MAX)  //could also be PRIORITY_HIGH.  needed for LOLLIPOP, M and N.  But not Oreo
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLargeIcon(bitmap)//for the heads/pop up must have sound or vibrate
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)  //VISIBILITY_PRIVATE or VISIBILITY_SECRET
                .setContentIntent(contentIntent)  //what activity to open.
                .setAutoCancel(true)   //allow auto cancel when pressed.
                .setChannelId(id3)  //Oreo notifications
                .build();  //finally build and return a Notification.

        //Show the notification
        nm.notify(NotID, noti);
        NotID++;
    }
    NotificationManager nm;
    private void createchannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id1,
                    _context.getString(R.string.channel_name),  //name of the channel
                    NotificationManager.IMPORTANCE_DEFAULT);   //importance level
            //important level: default is is high on the phone.  high is urgent on the phone.  low is medium, so none is low?
            // Configure the notification channel.
            mChannel.setDescription(_context.getString(R.string.channel_description));
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            nm.createNotificationChannel(mChannel);

            //a medium level channel
            mChannel = new NotificationChannel(id2,
                    _context.getString(R.string.channel_name2),  //name of the channel
                    NotificationManager.IMPORTANCE_LOW);   //importance level
            // Configure the notification channel.
            mChannel.setDescription(_context.getString(R.string.channel_description2));
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            nm.createNotificationChannel(mChannel);

            //a urgent level channel
            mChannel = new NotificationChannel(id3,
                    _context.getString(R.string.channel_name2),  //name of the channel
                    NotificationManager.IMPORTANCE_HIGH);   //importance level
            // Configure the notification channel.
            mChannel.setDescription(_context.getString(R.string.channel_description3));
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            nm.createNotificationChannel(mChannel);
        }
    }
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void createNotificationChannel(String names) {
//        String NOTIFICATION_CHANNEL_ID = "examplde.c";
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_MAX;
//            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                    .setSmallIcon(R.drawable.airtel)
//                    .setContentTitle("Call from "+names)
//                    .setContentText("Identified by Kaun")
//                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.airtel))
//                    .setWhen(System.currentTimeMillis())
//                    .setPriority(NotificationCompat.PRIORITY_MAX)   //could also be PRIORITY_HIGH.  needed for LOLLIPOP, M and N.  But not Oreo
//                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})  //for the heads/pop up must have sound or vibrate
//                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                    .setAutoCancel(true)   //allow auto cancel when pressed.
//                    .setChannelId(NOTIFICATION_CHANNEL_ID);
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(1, builder.build());
//            // or other notification behaviors after this
//            NotificationManager notificationManagers = getSystemService(NotificationManager.class);
//            notificationManagers.createNotificationChannel(channel);
//        }
//    }

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
            Log.d("WalletLucky", "onUserJoined = " + args[0]);
            JSONObject data = (JSONObject) args[0];
            String username;
            int numUsers;
            try {
                username = data.getString("username");
                numUsers = data.getInt("numUsers");
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }

//                    addLog(getResources().getString(R.string.message_user_joined, username));
//                    addParticipantsLog(numUsers);
//                }
//            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
            Log.d("WalletLucky", "onUserLeft = " + args[0]);
            JSONObject data = (JSONObject) args[0];
            String username;
            int numUsers;
            try {
                username = data.getString("username");
                numUsers = data.getInt("numUsers");
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }


        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
            Log.d("WalletLucky", "onTyping = " + args[0]);
            JSONObject data = (JSONObject) args[0];
            String username;
            try {
                username = data.getString("username");
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }
//                    addTyping(username);
//                }
//            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
            Log.d("WalletLucky", "onStopTyping = " + args[0]);
            JSONObject data = (JSONObject) args[0];
            String username;
            try {
                username = data.getString("username");
            } catch (JSONException e) {
                Log.d("TAG", e.getMessage());
                return;
            }
//                    removeTyping(username);
//                }
//            });
        }
    };
}
