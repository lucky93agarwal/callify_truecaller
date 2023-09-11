package com.gpslab.kaun.Service;

import android.annotation.SuppressLint;
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
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gpslab.kaun.CallReceiver;
import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.MyCustomDialog;
import com.gpslab.kaun.Notification.UserProfile;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.SettingsPreferences;
import com.gpslab.kaun.view.ChatActivity;
import com.gpslab.kaun.view.DBConstants;
import com.gpslab.kaun.view.Message;
import com.gpslab.kaun.view.MessageType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    public Bitmap bitmap1;
    public String resTxt = null;
    public FutureTarget<Bitmap> bitmap;

    public String MobileNumber;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        //Getting registration token

        //Displaying token on logcat


        // Saving reg id to shared preferences
        StoreToken(s);
        Log.d("Instance ID ", FirebaseInstanceId.getInstance().getId());

    }

    private void StoreToken(String token) {

        //save the token in sharedPreferences
        SettingsPreferences.setDeviceToken(token, getApplicationContext());

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("WalletNotification", "ExecutionException = =  " );
        try {


            sendPushNotificationAPI26(remoteMessage);

        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("WalletNotification", "ExecutionException = =  " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("WalletNotification", "InterruptedException = =  " + e.getMessage());
        }


    }

    private void handleNewMessage(RemoteMessage remoteMessage) {
        Map<String, String> map = remoteMessage.getData();


        String messageid = map.get(DBConstants.MESSAGE_ID);
        String phone = map.get(DBConstants.PHONE);
        String content = map.get(DBConstants.CONTENT);
        String timestamp = map.get(DBConstants.TIMESTAMP);
        String type = map.get(DBConstants.TYPE);
        String fromId = map.get(DBConstants.FROM_ID);
        String toId = map.get(DBConstants.TOID);
        String metadata = map.get(DBConstants.METADATA);


        int convertedType = MessageType.convertSentToReceived(Integer.valueOf(type));


        Message message = new Message();
        message.setContent(content);
        message.setTimestamp(timestamp);
        message.setFromId(fromId);
        message.setType(Integer.valueOf(type));
        message.setMessageId(messageid);
        message.setMetadata(metadata);
        message.setToId(toId);
        message.setFromPhone(phone);


        if (Integer.valueOf(type) == MessageType.RECEIVED_LOCATION) {
        } else if (Integer.valueOf(type) == MessageType.RECEIVED_IMAGE) {
        } else if (Integer.valueOf(type) == MessageType.RECEIVED_FILE) {
        } else if (Integer.valueOf(type) == MessageType.RECEIVED_CONTACT) {
        } else if (Integer.valueOf(type) == MessageType.RECEIVED_AUDIO) {
        } else if (Integer.valueOf(type) == MessageType.RECEIVED_AUDIO) {
        }


    }


    public void sendPushNotificationAPI26(RemoteMessage remoteMessage) throws ExecutionException, InterruptedException {
        Log.d("WalletgetServiceNew", "Title  =  " + remoteMessage.getData());
        Map<String, String> data = remoteMessage.getData();

        String title = data.get("title");
        String message = data.get("body");
        String image = data.get("image");
        String chat = data.get("type");
        String sender = data.get("sender");
        String sender_image = data.get("sender_image");
        MobileNumber = message;
        FutureTarget<Bitmap> bitmap;
        bitmap = Glide.with(this).asBitmap().load(image).submit();
        bitmap1 = bitmap.get();
//        SendDataToServer(message, getApplicationContext());
        Intent intent = new Intent(this, CallReceiver.class);
        sendBroadcast(intent);






        Log.d("WalletgetServiceNew", "bitmap1  =  " + bitmap1);

        if(chat.equalsIgnoreCase("chat")){
            SharedPreferences sharedPreferences = getSharedPreferences("ChatData",0);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("chat_id",sender);
            edit.apply();
            Intent notificationIntent = new Intent(getApplicationContext(), ChatActivity.class);
            notificationIntent.putExtra("id", sender);
            notificationIntent.putExtra("name", title);
            notificationIntent.putExtra("image", sender_image);
            Log.d("WalletgetServiceNew", "title  =  " + title);
            Log.d("WalletgetServiceNew", "message  =  " + message);
            Log.d("WalletgetServiceNew", "image  =  " + sender_image);
            Log.d("WalletgetServiceNew", "data  =  " + data);
            PendingIntent contentIntent = PendingIntent.getActivity(this, NotID, notificationIntent, 0);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "Lcky";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notification);
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();
                @SuppressLint("WrongConstant")
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Lucky Agarwa", NotificationManager.IMPORTANCE_MAX);
                notificationChannel.setDescription("This is my name");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setSound(sound, audioAttributes);
                notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);

            }


            NotificationCompat.Builder notificationbuild = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            notificationbuild.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis())
                    .setTicker("Herry")
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.logonew)
                    .setLargeIcon(getCircleBitmap(bitmap1))
                    .setContentIntent(contentIntent)
                    .setContentText(message)
                    .setContentInfo("info");

            notificationManager.notify(1, notificationbuild.build());
        }else {

            Intent notificationIntent = new Intent(getApplicationContext(), UserProfile.class);
            notificationIntent.putExtra("number", message);
            notificationIntent.putExtra("name", title);
            notificationIntent.putExtra("img", image);
            notificationIntent.putExtra("duration", "100");
            Log.d("WalletgetServiceNew", "title  =  " + title);
            Log.d("WalletgetServiceNew", "message  =  " + message);
            Log.d("WalletgetServiceNew", "image  =  " + sender_image);
            Log.d("WalletgetServiceNew", "data  =  " + data);
            PendingIntent contentIntent = PendingIntent.getActivity(this, NotID, notificationIntent, 0);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "Lcky";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notification);
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();
                @SuppressLint("WrongConstant")
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Lucky Agarwa", NotificationManager.IMPORTANCE_MAX);
                notificationChannel.setDescription("This is my name");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setSound(sound, audioAttributes);
                notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }


            NotificationCompat.Builder notificationbuild = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            notificationbuild.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis())
                    .setTicker("Herry")
                    .setContentTitle("Call from " + title)
                    .setSmallIcon(R.drawable.logonew)
                    .setLargeIcon(getCircleBitmap(bitmap1))
                    .setContentIntent(contentIntent)
                    .setContentText("Identified by Callify")
                    .setContentInfo("info");

            notificationManager.notify(1, notificationbuild.build());
        }

    }

    private void SendDataToServer(final String datasss, final Context context) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String QuickFirstName = datasss;
                JSONObject jsonObjectlucky = new JSONObject();
                try {
                    jsonObjectlucky.put("mobile", QuickFirstName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                String data = String.valueOf(Html.fromHtml(listlistfinal.toString() , Html.FROM_HTML_MODE_LEGACY));
//                QuickFirstName = Character.toString ((char) QuickFirstName);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("mobile", QuickFirstName));


                Log.d("WalletlsjWumber", "fname = = " + nameValuePairs);


//                JSONObject jsonObjectlucky = new JSONObject();
                try {
                    StringEntity se;
                    se = new StringEntity(jsonObjectlucky.toString());

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "verify_caller/");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    resTxt = EntityUtils.toString(entity);

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return resTxt;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                final String res_result;
                Log.d("WalletlsjWumber", "result = = " + result);
//                if(result.isEmpty()){
//
//                }else {
                Log.d("WalletlsjWalletsavedNumber", "result 1 = = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    if (jsonArray.length() == 1) {
                        res_result = jsonArray.getJSONObject(0).getString("result");
                        Log.d("WalletlsjWalletsavedNumber", "res_result = = " + res_result);
                    } else {
                        res_result = jsonArray.getJSONObject(1).getString("result");
                    }
                    Log.d("WalletlsjWalletsavedNumber", "result = = " + jsonArray.getJSONObject(0).getString("result"));

//                        jsonArray = jsonObject1.getJSONArray("response");

                    if (res_result.equalsIgnoreCase("0")) {
                        Log.d("WalletlsjWalletsavedNumber", "name = = " + jsonArray.getJSONObject(0).getString("name"));
                        Log.d("WalletlsjWalletsavedNumber", "phone_no = = " + jsonArray.getJSONObject(0).getString("name"));
                        Log.d("WalletlsjWalletsavedNumber", "profile_image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
                        final Intent intent = new Intent(context, MyCustomDialog.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("contact_name", MobileNumber);
                        intent.putExtra("phone_no", jsonArray.getJSONObject(0).getString("name"));
                        intent.putExtra("img", jsonArray.getJSONObject(0).getString("profile_image"));
                        intent.putExtra("is_spam", jsonArray.getJSONObject(0).getString("is_spam"));
                        intent.putExtra("spam_count", jsonArray.getJSONObject(0).getString("spamCount"));
                        intent.putExtra("type", "Ringing");
                        intent.putExtra("tag", "tag");
                        Log.d("WalletlsjWalletsavedNumber", "image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
                        context.startActivity(intent);
                    }
//
                } catch (JSONException e) {
                    e.getMessage();
                }
//                }


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(datasss);
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
    NotificationManager nm;

    int NotID = 1;
    public static String id1 = "test_channel_01";
    public static String id2 = "test_channel_02";
    public static String id3 = "test_channel_03";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void and5_notificaiton(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String names = data.get("title");
        String mobile = data.get("body");
        String image = data.get("image");
        Intent notificationIntent = new Intent(getApplicationContext(), UserProfile.class);
        notificationIntent.putExtra("mytype", "iconmsg" + NotID);
        notificationIntent.putExtra("mobile", mobile);
        notificationIntent.putExtra("name", names);
        notificationIntent.putExtra("img", image);
        PendingIntent contentIntent = PendingIntent.getActivity(this, NotID, notificationIntent, 0);

        Notification noti = new NotificationCompat.Builder(this.getApplicationContext(), id3)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.logonew)
                .setWhen(System.currentTimeMillis())  //When the event occurred, now, since noti are stored by time.
                .setContentTitle("Call from " + names)   //Title message top row.
                .setContentText("Identified by Callify")  //message when looking at the notification, second row
                //the following 2 lines cause it to show up as popup message at the top in android 5 systems.
                .setPriority(Notification.PRIORITY_MAX)  //could also be PRIORITY_HIGH.  needed for LOLLIPOP, M and N.  But not Oreo
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLargeIcon(bitmap1)//for the heads/pop up must have sound or vibrate
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)  //VISIBILITY_PRIVATE or VISIBILITY_SECRET
                .setContentIntent(contentIntent)  //what activity to open.
                .setAutoCancel(true)   //allow auto cancel when pressed.
                .setChannelId(id3)  //Oreo notifications
                .build();  //finally build and return a Notification.

        //Show the notification
        nm.notify(NotID, noti);
        NotID++;
    }

    private void createchannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id1,
                    this.getString(R.string.channel_name),  //name of the channel
                    NotificationManager.IMPORTANCE_DEFAULT);   //importance level
            //important level: default is is high on the phone.  high is urgent on the phone.  low is medium, so none is low?
            // Configure the notification channel.
            mChannel.setDescription(this.getString(R.string.channel_description));
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            nm.createNotificationChannel(mChannel);

            //a medium level channel
            mChannel = new NotificationChannel(id2,
                    this.getString(R.string.channel_name2),  //name of the channel
                    NotificationManager.IMPORTANCE_LOW);   //importance level
            // Configure the notification channel.
            mChannel.setDescription(this.getString(R.string.channel_description2));
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            nm.createNotificationChannel(mChannel);

            //a urgent level channel
            mChannel = new NotificationChannel(id3,
                    this.getString(R.string.channel_name2),  //name of the channel
                    NotificationManager.IMPORTANCE_HIGH);   //importance level
            // Configure the notification channel.
            mChannel.setDescription(this.getString(R.string.channel_description3));
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            nm.createNotificationChannel(mChannel);
        }
    }
//    public void getFirebaseMessage(String title, String message){
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myFirebasechannel")
//                .setSmallIcon(R.drawable.logonew)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//        managerCompat.notify(101,builder.build());
//    }


    private void sendPushNotification(JSONObject json) {
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            //creating MyNotificationManager object
//            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
//
//            //creating an intent for the notification
//            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
//
//            //if there is no image
//            if (imageUrl.equals("null")) {
//                //displaying small notification
//                mNotificationManager.showSmallNotification(title, message, intent);
//            } else {
//                //if there is an image
//                //displaying a big notification
//                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
//            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
}
