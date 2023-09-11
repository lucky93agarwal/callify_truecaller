package com.gpslab.kaun.Webapi;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonArray;
import com.gpslab.kaun.CallReceiver;
import com.gpslab.kaun.DB.GetChatContactList;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.MyCustomDialog;
import com.gpslab.kaun.Notification.UserProfile;
import com.gpslab.kaun.R;
import com.gpslab.kaun.StartingPopupActivity;
import com.gpslab.kaun.chat.MyFCMService;
import com.gpslab.kaun.digitaloceanspaces.RandomString;
import com.gpslab.kaun.fragment.StatusNewFragment;
import com.gpslab.kaun.model.Constants;
import com.gpslab.kaun.testing.TestingActivity;
import com.gpslab.kaun.view.Chat;
import com.gpslab.kaun.view.ChatActivity;
import com.gpslab.kaun.view.DBConstants;
import com.gpslab.kaun.view.DownloadUploadStat;
import com.gpslab.kaun.view.Message;
import com.gpslab.kaun.view.MessageType;
import com.gpslab.kaun.view.QuotedMessage;
import com.gpslab.kaun.view.RealmHelper;
import com.gpslab.kaun.view.Status;
import com.gpslab.kaun.view.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatWebAPI {
    public Socket mSocket;

    private final Context _context;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public String MobileNo;
    public MediaPlayer catscan;
    private static final String EVENT_PING = "ping1";
    private static final String EVENT_PONG = "pong1";


    public ChatWebAPI(Context context) {
        this._context = context;
    }
    public void stopsendTypeing(String number) {
        Log.d("CheckChatAPI", "number 1 = " + number);


//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
//        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", number);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("RequestAndResponse", "Send End Typing     jsonObject 1 = " + jsonObject);
        mSocket.emit("end_typing", jsonObject);


    }


    public void islive(String number) {
        Log.d("CheckChatAPI", "number 1 = " + number);


//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
//        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", number);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("RequestAndResponse", "Send    live = " + jsonObject);
        mSocket.emit("live", jsonObject);


    }

    public void data() {
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();



        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);

        mSocket.on(EVENT_PING, onPing);


//        mSocket.on("user joined", onUserJoined);
        mSocket.connect();


        Log.i("WalletlsjWalletHome", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);

        Log.i("WalletlsjWalletHome", "connected 1 = " + sharedPreferences.getString("mobile", ""));
//        mSocket.emit("new user",sharedPreferences.getString("mobile",""));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", sharedPreferences.getString("name", ""));
            jsonObject.put("user_number", sharedPreferences.getString("mobile", ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("WalletlsjWalletHome", "jsonObject 1 = " + jsonObject.toString());
        Log.i("WalletlsjWalletHome", "onConnect 1 = ");
        mSocket.emit("chat_init", jsonObject);


        JSONObject jsonObjecttwo = new JSONObject();
        try {

            jsonObjecttwo.put("mobileno", sharedPreferences.getString("mobile", ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("WalletlsjWalletHome", "jsonObject 1 = " + jsonObjecttwo.toString());
        Log.i("WalletlsjWalletHome", "onConnect 1 = ");
        mSocket.emit("Home", jsonObjecttwo);


        mSocket.on("wellcome", onwellcome);


        /// जब server को number  दोगे तो server उस number की details send करेगा।
        mSocket.on("calldata", oncalldata);


        mSocket.on("aftercall", oncall);

        /// call आने पर nofication show कराना ह
        mSocket.on("beforecall", beforecall);
        // single message
        mSocket.on("updateChat", onundelivered_new);


        /// किसी भी message को recive कर सकते है।
        mSocket.on("message", updateChat);


        mSocket.on("chat_init_success", onundelivered_new_new);


        // multiple message
        mSocket.on("undelivered", onundelivered);


        mSocket.on("reconnect", reconnect);


        // अगर msg recive होजायेगा तो उसे तो server बतायेगा कि आप ने जिसको message send किया था मैने उसे message पहुचा दिया है।
        mSocket.on("delivery_report_to_sender", delivery_report_to_sender);


        mSocket.on("start_typing", onTyping);


        mSocket.on("live",onLive);
        mSocket.on("end_typing", EndonTyping);


        //  जब message server के पास पहुचेगा। तो server reply करेगा। कि मेरे पास message पहुच गया है।
        mSocket.on("server_received", server_received);


        // आगर message को read कर लिया गया है तो उससे reply आयेगा।
        mSocket.on("msg_read_report_sender", msg_read_report_sender);


        mSocket.on("delete_every_one", delete_every_one);
//
        mSocket.on("stopTyping", onStopTyping);
    }
    private Emitter.Listener EndonTyping = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {


            Log.d("RequestAndResponse", "server response  end typing = " + args[0].toString());


            try {
                JSONObject data = (JSONObject) args[0];


                Log.d("onStopTypingonStopTyping", "onStopTyping data = " + data);
                sender = data.getString("sender");

                receiver = data.getString("receiver");
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }

            SharedPreferences sharedPre = _context.getSharedPreferences("ChatData", 0);
            String checknumber = sharedPre.getString("chat_id", "");
            if (checknumber.equalsIgnoreCase(sender)) {
                sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
                edit = sharedPreferences.edit();
                edit.putInt("istyping", 0);
                edit.apply();
            } else {
                sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
                edit = sharedPreferences.edit();
                edit.putInt("istyping", 1);
                edit.apply();
            }


//            mSocket.emit("reconnected", sharedPreferences.getString("mobile", ""));


        }
    };


    private Emitter.Listener beforecall = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {


            Log.i("Call_Ane_ke_time_notifcation", "oncall   calldata = " + args[0].toString());
            try {
                JSONObject data_new = (JSONObject) args[0];


//                JSONObject data = (JSONObject) array;

//                Log.i("Call_Ane_ke_time_notifcation", "onStopTyping data = " + String.valueOf(data_new));
                String number = data_new.getString("mobile");

                String Name = data_new.getString("fname")+" "+data_new.getString("lname");
                String Image = data_new.getString("profile_image");
                String SpamCount = data_new.getString("spanCount");

                int Space = Integer.valueOf(SpamCount);
                int Space_Result =0;
                if(Space >3){
                    Space_Result = 1;
                }else {
                    Space_Result = 0;
                }
                edit.putString("before_call_number",number);
                edit.putString("before_call_name",Name);
                edit.putString("before_call_Image",Image);
                edit.putString("before_call_Space_Result",String.valueOf(Space_Result));
                edit.putString("before_call_SpamCount",SpamCount);
                edit.apply();

                notification_before_call("Identified by Callify","Call from "+Name,Image);


//                Intent intent = new Intent(_context, MyCustomDialog.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("contact_name",Name);
//                intent.putExtra("phone_no",number);
//                intent.putExtra("img",Image);
//                intent.putExtra("is_spam",String.valueOf(Space_Result));
//                intent.putExtra("spam_count",SpamCount);
//                intent.putExtra("type","Ringing");
//                intent.putExtra("tag","tag");
//
//                _context.startActivity(intent);


            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }

// "status":[{"Id":4,"Number":"919808001481","Carrier":"Aircel","Name":"Lal Bahadur Chaudhary","Gender":"UNKNOWN","Image":"https:\/\/s3-eu-west-1.amazonaws.com\/images1.truecaller.com\/myview\/1\/8a383511719757355236c942c1276797\/1","Address":"Uttar Pradesh West in","JobTitle":"","CompanyName":"","Email":"lalbahadursingh@mail.com","Website":"","Facebook":"","Twitter":"","Tags":"","Badges":"user","Score":"0.9","SpamCount":"0"}]}


        }
    };






    private Emitter.Listener oncall = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {


            Log.i("Call_Ane_ke_time", "oncall   calldata = " + args[0].toString());
            try {
                JSONObject data_new = (JSONObject) args[0];
                JSONArray jsonArray = data_new.getJSONArray("user");

//                JSONObject data = (JSONObject) array;

                Log.d("Call_Ane_ke_time", "onStopTyping data = " + String.valueOf(jsonArray));
                String number = jsonArray.getJSONObject(0).getString("Number");

                String Name = jsonArray.getJSONObject(0).getString("Name");
                String Image = jsonArray.getJSONObject(0).getString("Image");
                String SpamCount = jsonArray.getJSONObject(0).getString("SpamCount");

                int Space = Integer.valueOf(SpamCount);
                int Space_Result =0;
                if(Space >3){
                    Space_Result = 1;
                }else {
                    Space_Result = 0;
                }


                Intent intent = new Intent(_context, MyCustomDialog.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("contact_name",Name);
                intent.putExtra("phone_no",number);
                intent.putExtra("img",Image);
                intent.putExtra("is_spam",String.valueOf(Space_Result));
                intent.putExtra("spam_count",SpamCount);
                intent.putExtra("type","Ringing");
                intent.putExtra("tag","tag");

                _context.startActivity(intent);


            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }

// "status":[{"Id":4,"Number":"919808001481","Carrier":"Aircel","Name":"Lal Bahadur Chaudhary","Gender":"UNKNOWN","Image":"https:\/\/s3-eu-west-1.amazonaws.com\/images1.truecaller.com\/myview\/1\/8a383511719757355236c942c1276797\/1","Address":"Uttar Pradesh West in","JobTitle":"","CompanyName":"","Email":"lalbahadursingh@mail.com","Website":"","Facebook":"","Twitter":"","Tags":"","Badges":"user","Score":"0.9","SpamCount":"0"}]}


        }
    };
    private Emitter.Listener oncalldata = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {


            Log.i("incomingincoming", "calldata = " + args[0].toString());
            try {
                JSONObject data = (JSONObject) args[0];
                JSONArray jsonArray = data.getJSONArray("status");

                Log.i("incomingincoming", "onStopTyping data = " + data);
                String status = jsonArray.getJSONObject(0).getString("status");
                if(status.equalsIgnoreCase("1")){
                    String number = jsonArray.getJSONObject(0).getString("Number");
                    String Carrier = jsonArray.getJSONObject(0).getString("Carrier");
                    String Name = jsonArray.getJSONObject(0).getString("Name");
                    String Image = jsonArray.getJSONObject(0).getString("Image");
                    String SpamCount = jsonArray.getJSONObject(0).getString("SpamCount");

                    int Space = Integer.valueOf(SpamCount);
                    int Space_Result = 0;
                    if (Space > 3) {
                        Space_Result = 1;
                    } else {
                        Space_Result = 0;
                    }
                    edit.putString("before_call_number",number);
                    edit.putString("before_call_name",Name);
                    edit.putString("before_call_Image",Image);
                    edit.putString("before_call_Space_Result",String.valueOf(Space_Result));
                    edit.putString("before_call_SpamCount",SpamCount);
                    edit.apply();
                    SharedPreferences sh = _context.getSharedPreferences("incoming",0);
                    int check = sh.getInt("icomming",0);
                    Log.i("incomingincoming", "check data = " + check);
                    if(check == 1){
                        Intent intent = new Intent(_context, StartingPopupActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("contact_name", Name);
                        intent.putExtra("phone_no", number);
                        intent.putExtra("img", Image);
                        intent.putExtra("is_spam", String.valueOf(Space_Result));
                        intent.putExtra("spam_count", SpamCount);
                        intent.putExtra("type", "Ringing");
                        intent.putExtra("tag", "tag");

                        _context.startActivity(intent);
                    }
                }





            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }

// "status":[{"Id":4,"Number":"919808001481","Carrier":"Aircel","Name":"Lal Bahadur Chaudhary","Gender":"UNKNOWN","Image":"https:\/\/s3-eu-west-1.amazonaws.com\/images1.truecaller.com\/myview\/1\/8a383511719757355236c942c1276797\/1","Address":"Uttar Pradesh West in","JobTitle":"","CompanyName":"","Email":"lalbahadursingh@mail.com","Website":"","Facebook":"","Twitter":"","Tags":"","Badges":"user","Score":"0.9","SpamCount":"0"}]}


        }
    };
    private Emitter.Listener onwellcome = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {


            Log.i("WalletlsjWalletHome", "Welcome = " + args[0].toString());


        }
    };
    private Emitter.Listener reconnect = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {


            Log.d("reconnectredMessage", "reconnect = ");

            mSocket.emit("reconnected", sharedPreferences.getString("mobile", ""));


        }
    };
    public String message_id__server_received;
    private Emitter.Listener onTyping = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {


            Log.d("onStopTypingonStopTyping", "onStopTyping = " + args[0].toString());


            try {
                JSONObject data = (JSONObject) args[0];


                Log.d("onStopTypingonStopTyping", "onStopTyping data = " + data);
                sender = data.getString("sender");

                receiver = data.getString("receiver");
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }

            SharedPreferences sharedPre = _context.getSharedPreferences("ChatData", 0);
            String checknumber = sharedPre.getString("chat_id", "");
            if (checknumber.equalsIgnoreCase(sender)) {
                sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
                edit = sharedPreferences.edit();
                edit.putInt("istyping", 1);
                edit.apply();
            } else {
                sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
                edit = sharedPreferences.edit();
                edit.putInt("istyping", 0);
                edit.apply();
            }


//            mSocket.emit("reconnected", sharedPreferences.getString("mobile", ""));


        }
    };

    public String live;
    private Emitter.Listener onLive = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {


            Log.d("RequestAndResponse", "response live = " + args[0].toString());


//            try {
//                JSONObject data = (JSONObject) args[0];
//
//
//                Log.d("onStopTypingonStopTyping", "onStopTyping data = " + data);
//                live = data.getString("live");
//
//            } catch (JSONException e) {
//                Log.e("TAG", e.getMessage());
//                return;
//            }
//
//            if (live.equalsIgnoreCase("1")) {
//                sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
//                edit = sharedPreferences.edit();
//                edit.putInt("live", 1);
//                edit.apply();
//            } else {
//                sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
//                edit = sharedPreferences.edit();
//                edit.putInt("live", 0);
//                edit.apply();
//            }


//            mSocket.emit("reconnected", sharedPreferences.getString("mobile", ""));


        }
    };
    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {


            Log.d("onStopTypingonStopTyping", "onStopTyping = " + args[0].toString());


            try {
                JSONObject data = (JSONObject) args[0];


                Log.d("onStopTypingonStopTyping", "onStopTyping data = " + data);
                sender = data.getString("sender");

                receiver = data.getString("receiver");
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }


            SharedPreferences sharedPre = _context.getSharedPreferences("ChatData", 0);
            String checknumber = sharedPre.getString("chat_id", "");
            if (checknumber.equalsIgnoreCase(sender)) {
                sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
                edit = sharedPreferences.edit();
                edit.putInt("istyping", 0);
                edit.apply();
            }

//            mSocket.emit("reconnected", sharedPreferences.getString("mobile", ""));


        }
    };


    private Emitter.Listener delivery_report_to_sender = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {


            Log.d("msg_read_report_sender", "delivery_report_to_sender = " + args[0].toString());


            try {
                JSONObject data = (JSONObject) args[0];


                Log.d("onundeliveredMessage", "delivery_report_to_sender data = " + data);
                sender = data.getString("sender");

                receiver = data.getString("receiver");

                chat_id = data.getString("message_id");
                Log.d("msg_read_report_sender", "delivery_report_to_sender  chat_id = " + chat_id);
                Log.d("msg_read_report_sender", "delivery_report_to_sender  receiver = " + receiver);
                Log.d("msg_read_report_sender", "delivery_report_to_sender  sender = " + sender);
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }

            RealmHelper.getInstance().updateDownloadSendStat(chat_id, DownloadUploadStat.SUCCESS);


//            mSocket.emit("reconnected", sharedPreferences.getString("mobile", ""));


        }
    };
    private Emitter.Listener server_received = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {

            Log.d("PandingMessage", "server_received   =  " + args[0].toString());
            Log.d("server_received", "delivery_report_to_sender = " + args[0].toString());


            try {
                JSONObject data = (JSONObject) args[0];


                Log.d("onundeliveredMessage", "server_received data = " + data);


                message_id__server_received = data.getString("message_id");

            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }

            RealmHelper.getInstance().updateDownloadSend(message_id__server_received, DownloadUploadStat.SUCCESS);


//            mSocket.emit("reconnected", sharedPreferences.getString("mobile", ""));


        }
    };


    public String delete_every_one_variable_message_id;

    public String delete_every_one_variable_receiver;
    public String msg_read_report_sender_message_id;
    private Emitter.Listener delete_every_one = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {

            Log.d("PandingMessage", "delete_every_one   =   " + args[0].toString());

            Log.d("delete_every_one", "delivery_report_to_sender = " + args[0].toString());


            try {
                JSONObject data = (JSONObject) args[0];


                Log.d("onundeliveredMessage", "delete_every_one data = " + data);


                delete_every_one_variable_message_id = data.getString("message_id");
                delete_every_one_variable_receiver = data.getString("receiver");
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }
            RealmHelper.getInstance().deleteMessageFromRealm(delete_every_one_variable_receiver, delete_every_one_variable_message_id, false);


//            mSocket.emit("reconnected", sharedPreferences.getString("mobile", ""));


        }
    };

    private Emitter.Listener msg_read_report_sender = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {

            Log.d("PandingMessage", "msg_read_report_sender   =   " + args[0].toString());

            Log.d("msg_read_report_sender", "delivery_report_to_sender = " + args[0].toString());


            try {
                JSONObject data = (JSONObject) args[0];


                Log.d("onundeliveredMessage", "msg_read_report_sender data = " + data);


                msg_read_report_sender_message_id = data.getString("message_id");

            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }

            RealmHelper.getInstance().updateDownloadSendREAD(msg_read_report_sender_message_id, DownloadUploadStat.SUCCESS);


//            mSocket.emit("reconnected", sharedPreferences.getString("mobile", ""));


        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.d("CheckChatAPINew", "onConnect 1 = ");

            Log.d("WalletlsjWallet", "emit = " + sharedPreferences.getString("mobile", ""));


            mSocket.emit("user_connected", sharedPreferences.getString("mobile", ""));


        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.d("CheckChatAPINew", "diconnected = ");
            Log.i("TAG", "diconnected");


            {
                try {
                    mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            mSocket.connect();


            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", sharedPreferences.getString("name", ""));
                jsonObject.put("user_number", sharedPreferences.getString("mobile", ""));

            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.d("WalletlsjWallet", "onConnect 1 = ");
            mSocket.emit("chat_init", jsonObject);

        }
    };

    private Emitter.Listener onPing = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


            Log.d("WalletlsjWallet", "Error EVENT_PONG");

            mSocket.emit(EVENT_PONG);

        }
    };
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.d("WalletLucky", "Error connecting = " + args[0]);
            Log.e("6", "Error connecting");

        }
    };

    private static MyFCMService defaultpopupnew;
    public String chat_message;
    public String sender;
    public String chat_type;
    public String receiver;
    public boolean isGroup = false;
    public String attribute;


    public String contact;

    public String contact_name;

    public String contact_number;


    public String image;
    public String audio;
    public String video;
    public String thumb;
    public String audio_name;
    public String video_name;
    public String chat_id;
    public JSONObject jsonObject2;

    public String image_name;


    public String latitude;
    public String longitude;
    public String address;


    public String doc_name;
    public String document_name;
    public String document_size;

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {
            catscan = MediaPlayer.create(_context, R.raw.notification);
            catscan.start();

            Log.d("CheckReciveMessage", "onNewMessage = " + String.valueOf(args[0]));


            JSONObject data = (JSONObject) args[0];

            try {
                sender = data.getString("sender");
                chat_message = data.getString("chat_message");
                chat_type = data.getString("type");
                receiver = data.getString("receiver");
                attribute = data.getString("attribute");
                chat_id = data.getString("message_id");
                reply_id = data.getString("reply_id");

            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }
            Log.d("CheckReciveMessage", "chat_type = " + chat_type + "   chat_message = " + chat_message + "   sender = " + sender + "   receiver = " + receiver);


            JSONArray jsonArray = new JSONArray();
            try {
                jsonObject2 = new JSONObject();
                jsonObject2.put("msg_id", chat_id);
                jsonArray.put(jsonObject2);
            } catch (JSONException e) {

            }
            Log.d("msgDelivered", "  jsonArray =   " + jsonArray);
            mSocket.emit("msgDelivered", jsonArray);


            if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_IMAGE))) {

                Log.d("CheckReciveMessage", "  chat_type =   " + chat_type);

                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                image_name = ts + "_" + randomString.nextString() + "downloaded_image.jpg";


                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(chat_message);
            } else {


                Log.d("CheckReciveMessage", "  chat_type  1 =   " + chat_type);
                defaultpopupnew = new MyFCMService(_context);
                defaultpopupnew.onMessageReceived(sender, chat_message, chat_type, attribute, chat_id, reply_id);
            }


        }
    };

    private Emitter.Listener onundelivered_new_new = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {
//            try {
            JSONObject data = (JSONObject) args[0];
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
            Log.d("CheckChatAPINew", "loggedin data = " + String.valueOf(data));
            mSocket.emit("loggedin", data);
        }
    };
    public String reply_id;

    public MyDbHandler myDbHandler;

    private void notification_before_call(String message,String Title, String Image) {
        try {
            Log.d("Call_Ane_ke_time_notifcation", "onNewMessage = ");
            Log.d("Call_Ane_ke_time_notifcation", "onNewMessage = ");

//            myDbHandler = new MyDbHandler(_context, "userbd", null, 1);
//            Temp.setMyDbHandler(myDbHandler);
//            ArrayList<GetChatContactList> arrayList2 = myDbHandler.viewChatContact();
//            for(int i=0;i<arrayList2.size();i++){
//                if(sender.equalsIgnoreCase(arrayList2.get(i).getContacts_id())){
                    sendPushNotificationAPI26(Title, Image, message);
//                }
//            }


        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("WalletNotification", "ExecutionException = =  " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("WalletNotification", "InterruptedException = =  " + e.getMessage());
        }
    }




    private void notification(String message) {
        try {
            Log.d("WalletNotification", "onNewMessage = ");
            Log.d("WalletNotification", "onNewMessage = ");

            myDbHandler = new MyDbHandler(_context, "userbd", null, 1);
            Temp.setMyDbHandler(myDbHandler);
            ArrayList<GetChatContactList> arrayList2 = myDbHandler.viewChatContact();
            for(int i=0;i<arrayList2.size();i++){
                if(sender.equalsIgnoreCase(arrayList2.get(i).getContacts_id())){
                    sendPushNotificationAPI26(arrayList2.get(i).getContacts_name(), arrayList2.get(i).getContacts_image(), message);
                }
            }


        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("WalletNotification", "ExecutionException = =  " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("WalletNotification", "InterruptedException = =  " + e.getMessage());
        }
    }

    private List<String> imageList = new ArrayList<>();
    private List<String> imagenameList = new ArrayList<>();
    private int check_position = 0;
    private int ArraySizeinImage = 0;

    public Boolean checkloadimage = true;





    private void ImageRecursion(){
        Log.i("CheckImageCheckDownload","ImageRecursion = ");
        if(checkloadimage){
            Log.i("CheckImageCheckDownload","start Download = ");
            DownloadTaskTwo downloadTask = new DownloadTaskTwo();
            downloadTask.execute(imageList.get(check_position));
        }
    }
    private Emitter.Listener updateChat = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            try {
//                JSONObject data = (JSONObject) args[0];
//
//
//
//
//                Log.d("CheckChatAPINew","message Recive = "+String.valueOf(data));


//            catscan = MediaPlayer.create(_context, R.raw.notification);
//            catscan.start();

            Log.i("alletReplyImage", "onNewMesage   =    " + String.valueOf(args[0]));

            Log.d("onundeliverdfsdfe", "onNewMessage = " + String.valueOf(args[0]));


            try {
                JSONObject data = (JSONObject) args[0];

                Log.i("CheckChatAPINew", "message Recive = " + String.valueOf(data));
                Log.i("onundeliveredMessage", "data = " + data);
                sender = data.getString("sender");
                chat_message = data.getString("msg");
                chat_type = data.getString("type");
                receiver = data.getString("receiver");
                attribute = data.getString("attribute");

                image = data.getString("image");
                audio = data.getString("audio");
                video = data.getString("video");
                thumb = data.getString("thumb");


                latitude = data.getString("latitude");
                longitude = data.getString("longitude");
                address = data.getString("address");


                document_name = data.getString("document_name");
                document_size = data.getString("document_size");

                contact = data.getString("contact");
                chat_id = data.getString("message_id");
                reply_id = data.getString("reply_id");
                Log.i("onundeliveredMessage", "sender = " + sender);
                Log.i("onundeliveredMessage", "chat_message = " + chat_message);
                Log.i("onundeliveredMessage", "chat_type = " + chat_type);
                Log.i("onundeliveredMessage", "receiver = " + receiver);
                Log.i("onundeliveredMessage", "attribute = " + attribute);
                Log.i("onundeliveredMessage", "image = " + image);
                Log.i("onundeliveredMessage", "audio = " + audio);
                Log.i("onundeliveredMessage", "video = " + video);
                Log.i("onundeliveredMessage", "thumb = " + thumb);
                Log.i("onundeliveredMessage", "latitude = " + latitude);
                Log.i("onundeliveredMessage", "longitude = " + longitude);
                Log.i("onundeliveredMessage", "address = " + address);
                Log.i("onundeliveredMessage", "document_name = " + document_name);
                Log.i("onundeliveredMessage", "document_size = " + document_size);
                Log.i("onundeliveredMessage", "contact = " + contact);
                Log.i("onundeliveredMessage", "chat_id = " + chat_id);
                Log.i("onundeliveredMessage", "reply_id = " + reply_id);

            } catch (JSONException e) {
                Log.e("WalletNotification", "error ==    " + e.getMessage());
                return;
            }

            Log.d("onundeliveredMessage", "chat_type = " + chat_type + "   chat_message = " + chat_message + "   sender = " + sender + "   receiver = " + receiver);


            try {
                jsonObject2 = new JSONObject();
                jsonObject2.put("message_id", chat_id);
                jsonObject2.put("sender", sender);
                jsonObject2.put("receiver", receiver);
            } catch (JSONException e) {

            }
            Log.i("onundeliveredMessage", "msgDelivered = " + String.valueOf(jsonObject2));
            mSocket.emit("msgDelivered", jsonObject2);


            if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_DELETED_MESSAGE))) {
                Log.i("onundeliveredMessage", "delete = ");
                RealmHelper.getInstance().deleteMessageFromRealm(sender, chat_id, false);


            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_FILE))) {
                notification("You got a document");
                String extension = document_name.substring(document_name.lastIndexOf("."));
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                doc_name = ts + "_" + randomString.nextString() + "downloaded_audio" + extension;
//                Log.d("onundeliveredMessage", "data = " + doc_name);
//                Log.d("onundeliveredMessage", "document_name = " + document_name);
                DownloadTaskDoc downloadaudio = new DownloadTaskDoc();
                downloadaudio.execute(document_name);
//                defaultpopupnew = new MyFCMService(_context);
//
//                defaultpopupnew.onMessageReceivedLocation(sender, document_name,longitude,address, chat_type);
                edit.putString("scroll", "1");
                edit.apply();

            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_LOCATION))) {

                notification("Shared location with you");
                defaultpopupnew = new MyFCMService(_context);

                defaultpopupnew.onMessageReceivedLocation(sender, latitude, longitude, address, chat_type, chat_id, reply_id);

                edit.putString("scroll", "1");
                edit.apply();
            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_CONTACT))) {
                String[] separated = contact.split(",");

                Log.d("onundeliveredMessage", "number = " + separated[0].toString());
                Log.d("onundeliveredMessage", "name = " + separated[1].toString());

                contact_number = separated[0].toString();
                contact_name = separated[1].toString();
                notification("Shared a contact with you ");
                defaultpopupnew = new MyFCMService(_context);
                defaultpopupnew.onMessageReceivedContact(sender, contact_name, chat_type, contact_number, chat_id, reply_id);

                edit.putString("scroll", "1");
                edit.apply();
            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_VIDEO))) {
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                video_name = ts + "_" + randomString.nextString() + "downloaded_audio.mp4";
                notification("Shared a video with you ");
                Log.i("onundeliveredMessage", "video_name = " + video_name);
                Log.d("onundeliveredMessage", "video = " + video);
                Log.d("onundeliveredMessage", "thumb = " + thumb);

                DownloadTaskVideo downloadaudio = new DownloadTaskVideo();
                downloadaudio.execute(video);
                edit.putString("scroll", "1");
                edit.apply();
            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_AUDIO))) {

                notification("Shared an audio with you ");
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                audio_name = ts + "_" + randomString.nextString() + "downloaded_audio.mp3";


                Log.d("onundeliveredMessage", "audio_name = " + audio_name);
                Log.d("onundeliveredMessage", "audio = " + audio);

                DownloadTaskAudio downloadaudio = new DownloadTaskAudio();
                downloadaudio.execute(audio);
                edit.putString("scroll", "1");
                edit.apply();
            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_IMAGE))) {

                notification("Shared an image with you ");
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                image_name = ts + "_" + randomString.nextString() + "downloaded_image.jpeg";

                Log.i("ChatsendImageKnow", "image_name = " + image_name);
                Log.i("ChatsendImageKnow", "image = " + image);

                imageList.add(image);
                imagenameList.add(image_name);
                ArraySizeinImage = imageList.size()-1;
                Log.i("CheckImageCheckDownload","Check 1");
                Log.i("CheckImageCheckDownload","Image List = "+imageList.toString());
                Log.i("CheckImageCheckDownload","Image name = "+imagenameList.toString());
                Log.i("CheckImageCheckDownload","Image array size = "+String.valueOf(ArraySizeinImage));
                ImageRecursion();






                edit.putString("scroll", "1");
                edit.apply();
            } else {
                notification(chat_message);
                Log.d("CheckChatAPINew", "image = " + chat_id);
                defaultpopupnew = new MyFCMService(_context);
                defaultpopupnew.onMessageReceived(sender, chat_message, chat_type, attribute, chat_id, reply_id);

//                TextSet runtext = new TextSet();
//                runtext.execute();
                edit.putString("scroll", "1");
                edit.apply();

            }


            Readmessage(sender);
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
        }
    };
    private Emitter.Listener onundelivered_new = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {
            catscan = MediaPlayer.create(_context, R.raw.notification);
            catscan.start();

            Log.d("alletReplyImage", "onNewMesage   =    " + String.valueOf(args[0]));

            Log.d("onundeliverdfsdfe", "onNewMessage = " + String.valueOf(args[0]));


            try {
                JSONObject data = (JSONObject) args[0];


                Log.d("onundeliveredMessage", "data = " + data);
                sender = data.getString("sender");
                chat_message = data.getString("chat_message");
                chat_type = data.getString("type");
                receiver = data.getString("receiver");
                attribute = data.getString("attribute");

                image = data.getString("image");
                audio = data.getString("audio");
                video = data.getString("video");
                thumb = data.getString("thumb");


                latitude = data.getString("latitude");
                longitude = data.getString("longitude");
                address = data.getString("address");


                document_name = data.getString("document_name");
                document_size = data.getString("document_size");

                contact = data.getString("contact");
                chat_id = data.getString("message_id");
                reply_id = data.getString("reply_id");
            } catch (JSONException e) {
                Log.e("WalletNotification", "error ==    " + e.getMessage());
                return;
            }
            try {
                Log.d("WalletNotification", "onNewMessage = ");
                Log.d("WalletNotification", "onNewMessage = ");
                sendPushNotificationAPI26(sender, chat_type, chat_message);

            } catch (ExecutionException e) {
                e.printStackTrace();
                Log.d("WalletNotification", "ExecutionException = =  " + e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("WalletNotification", "InterruptedException = =  " + e.getMessage());
            }
            Log.d("onundeliveredMessage", "chat_type = " + chat_type + "   chat_message = " + chat_message + "   sender = " + sender + "   receiver = " + receiver);


            try {
                jsonObject2 = new JSONObject();
                jsonObject2.put("message_id", chat_id);
                jsonObject2.put("sender", sender);
                jsonObject2.put("receiver", receiver);
            } catch (JSONException e) {

            }

            mSocket.emit("msgDelivered", jsonObject2);

            if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_FILE))) {

                String extension = document_name.substring(document_name.lastIndexOf("."));
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                doc_name = ts + "_" + randomString.nextString() + "downloaded_audio" + extension;
//                Log.d("onundeliveredMessage", "data = " + doc_name);
//                Log.d("onundeliveredMessage", "document_name = " + document_name);
                DownloadTaskDoc downloadaudio = new DownloadTaskDoc();
                downloadaudio.execute(document_name);
//                defaultpopupnew = new MyFCMService(_context);
//
//                defaultpopupnew.onMessageReceivedLocation(sender, document_name,longitude,address, chat_type);
                edit.putString("scroll", "1");
                edit.apply();

            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_LOCATION))) {


                defaultpopupnew = new MyFCMService(_context);

                defaultpopupnew.onMessageReceivedLocation(sender, latitude, longitude, address, chat_type, chat_id, reply_id);

                edit.putString("scroll", "1");
                edit.apply();
            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_CONTACT))) {
                String[] separated = contact.split(",");

                Log.d("onundeliveredMessage", "number = " + separated[0].toString());
                Log.d("onundeliveredMessage", "name = " + separated[1].toString());

                contact_number = separated[0].toString();
                contact_name = separated[1].toString();
                defaultpopupnew = new MyFCMService(_context);
                defaultpopupnew.onMessageReceivedContact(sender, contact_name, chat_type, contact_number, chat_id, reply_id);

                edit.putString("scroll", "1");
                edit.apply();
            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_VIDEO))) {
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                video_name = ts + "_" + randomString.nextString() + "downloaded_audio.mp4";

                Log.d("onundeliveredMessage", "video_name = " + video_name);
                Log.d("onundeliveredMessage", "video = " + video);
                Log.d("onundeliveredMessage", "thumb = " + thumb);

                DownloadTaskVideo downloadaudio = new DownloadTaskVideo();
                downloadaudio.execute(video);
                edit.putString("scroll", "1");
                edit.apply();
            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_AUDIO))) {


                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                audio_name = ts + "_" + randomString.nextString() + "downloaded_audio.mp3";


                Log.d("onundeliveredMessage", "audio_name = " + audio_name);
                Log.d("onundeliveredMessage", "audio = " + audio);

                DownloadTaskAudio downloadaudio = new DownloadTaskAudio();
                downloadaudio.execute(audio);
                edit.putString("scroll", "1");
                edit.apply();
            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_IMAGE))) {


                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                image_name = ts + "_" + randomString.nextString() + "downloaded_image.jpg";

                Log.d("onundeliveredMessage", "image_name = " + image_name);
                Log.d("onundeliveredMessage", "image = " + image);

                DownloadTaskTwo downloadTask = new DownloadTaskTwo();
                downloadTask.execute(image);
                edit.putString("scroll", "1");
                edit.apply();
            } else {
                Log.d("onundeliveredMessageL", "image = " + chat_id);
                defaultpopupnew = new MyFCMService(_context);
                defaultpopupnew.onMessageReceived(sender, chat_message, chat_type, attribute, chat_id, reply_id);

//                TextSet runtext = new TextSet();
//                runtext.execute();
                edit.putString("scroll", "1");
                edit.apply();

            }


        }
    };

    private Emitter.Listener onundelivered = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(final Object... args) {
//            catscan = MediaPlayer.create(_context, R.raw.notification);
//            catscan.start();

            Log.d("onundeliveredMessage", "onNewMessage = " + String.valueOf(args[0]));


            try {
                JSONObject data = (JSONObject) args[0];


                Log.d("onundeliveredMessage", "data = " + data);
                sender = data.getString("sender");
                chat_message = data.getString("chat_message");
                chat_type = data.getString("type");
                receiver = data.getString("receiver");
                attribute = data.getString("attribute");

                image = data.getString("image");
                audio = data.getString("audio");
                video = data.getString("video");
                thumb = data.getString("thumb");


                latitude = data.getString("latitude");
                longitude = data.getString("longitude");
                address = data.getString("address");


                document_name = data.getString("document_name");
                document_size = data.getString("document_size");

                contact = data.getString("contact");
                chat_id = data.getString("message_id");
                reply_id = data.getString("reply_id");
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }
            Log.d("onundeliveredMessage", "chat_type = " + chat_type + "   chat_message = " + chat_message + "   sender = " + sender + "   receiver = " + receiver);


            try {
                jsonObject2 = new JSONObject();
                jsonObject2.put("message_id", chat_id);
                jsonObject2.put("sender", sender);
                jsonObject2.put("receiver", receiver);
            } catch (JSONException e) {

            }

            mSocket.emit("msgDelivered", jsonObject2);

            if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_FILE))) {

                String extension = document_name.substring(document_name.lastIndexOf("."));
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                doc_name = ts + "_" + randomString.nextString() + "downloaded_audio" + extension;
//                Log.d("onundeliveredMessage", "data = " + doc_name);
//                Log.d("onundeliveredMessage", "document_name = " + document_name);
                DownloadTaskDoc downloadaudio = new DownloadTaskDoc();
                downloadaudio.execute(document_name);
//                defaultpopupnew = new MyFCMService(_context);
//
//                defaultpopupnew.onMessageReceivedLocation(sender, document_name,longitude,address, chat_type);
                edit.putString("scroll", "1");
                edit.apply();

            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_LOCATION))) {


                defaultpopupnew = new MyFCMService(_context);

                defaultpopupnew.onMessageReceivedLocation(sender, latitude, longitude, address, chat_type, chat_id, reply_id);

                edit.putString("scroll", "1");
                edit.apply();
            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_CONTACT))) {
                String[] separated = contact.split(",");

                Log.d("onundeliveredMessage", "number = " + separated[0].toString());
                Log.d("onundeliveredMessage", "name = " + separated[1].toString());

                contact_number = separated[0].toString();
                contact_name = separated[1].toString();
                defaultpopupnew = new MyFCMService(_context);
                defaultpopupnew.onMessageReceivedContact(sender, contact_name, chat_type, contact_number, chat_id, reply_id);
                edit.putString("scroll", "1");
                edit.apply();
            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_VIDEO))) {
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                video_name = ts + "_" + randomString.nextString() + "downloaded_audio.mp4";

                Log.d("onundeliveredMessage", "video_name = " + video_name);
                Log.d("onundeliveredMessage", "video = " + video);
                Log.d("onundeliveredMessage", "thumb = " + thumb);

                DownloadTaskVideo downloadaudio = new DownloadTaskVideo();
                downloadaudio.execute(video);
                edit.putString("scroll", "1");
                edit.apply();
            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_AUDIO))) {


                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                audio_name = ts + "_" + randomString.nextString() + "downloaded_audio.mp3";


                Log.d("onundeliveredMessage", "audio_name = " + audio_name);
                Log.d("onundeliveredMessage", "audio = " + audio);

                DownloadTaskAudio downloadaudio = new DownloadTaskAudio();
                downloadaudio.execute(audio);
                edit.putString("scroll", "1");
                edit.apply();
            } else if (chat_type.equalsIgnoreCase(String.valueOf(MessageType.RECEIVED_IMAGE))) {


                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                image_name = ts + "_" + randomString.nextString() + "downloaded_image.jpg";

                Log.d("onundeliveredMessage", "image_name = " + image_name);
                Log.d("onundeliveredMessage", "image = " + image);

                DownloadTaskTwo downloadTask = new DownloadTaskTwo();
                downloadTask.execute(image);
                edit.putString("scroll", "1");
                edit.apply();
            } else {
                Log.d("onundeliveredMessageL", "image = " + chat_id);
                defaultpopupnew = new MyFCMService(_context);
                defaultpopupnew.onMessageReceived(sender, chat_message, chat_type, attribute, chat_id, reply_id);

//                TextSet runtext = new TextSet();
//                runtext.execute();

                Log.d("onundeliveredMes_context", "_context = " + _context);
//                ((MainHomeActivity)_context).datagetnewapi(chat_id);

                edit.putString("scroll", "1");
                edit.apply();
            }


        }
    };


    class TextSet extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            ((ChatActivity) _context).scrolltolaastlucky();
            return null;
        }
    }


    class DownloadTask extends AsyncTask<String, Integer, String> {

//        ProgressDialog progressDialog;

        /**
         * Set up a ProgressDialog
         */
        @Override
        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(TestingActivity.this);
//            progressDialog.setTitle("Download in progress...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setMax(100);
//            progressDialog.setProgress(0);
//            progressDialog.show();

        }

        /**
         * Background task
         */
        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            int file_length;

            Log.d("InfoInfoInfo: path", path);
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();

                /**
                 * Create a folder
                 */
                File new_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
                if (!new_folder.exists()) {
                    if (new_folder.mkdir()) {
                        Log.d("InfoInfoInfo", "Folder succesfully created");
                    } else {
                        Log.d("InfoInfoInfo", "Failed to create folder");
                    }
                } else {
                    Log.d("InfoInfoInfo", "Folder already exists" + new_folder);
                }

                /**
                 * Create an output file to store the image for download
                 */


                File output_file = new File(new_folder, image_name);
                OutputStream outputStream = new FileOutputStream(output_file);
                Log.d("InfoInfoInfo", "output_file: " + output_file);
                Log.d("InfoInfoInfo", "outputStream: " + outputStream);
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

                Log.d("InfoInfoInfo", "inputStream: " + inputStream);
                byte[] data = new byte[1024];
                int total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;

                    outputStream.write(data, 0, count);
                    int progress = 100 * total / file_length;
                    publishProgress(progress);

                    Log.d("InfoInfoInfo", "Progress: " + Integer.toString(progress));
                }
                inputStream.close();
                outputStream.close();

                Log.d("InfoInfoInfo", "file_length: " + Integer.toString(file_length));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download complete.";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
//            progressDialog.hide();

            Toast.makeText(_context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
            File output_file = new File(folder, image_name);
            String path = output_file.toString();

            Log.d("InfoInfoInfo", "path: " + path);


            defaultpopupnew = new MyFCMService(_context);
            defaultpopupnew.onMessageReceivedImage(sender, chat_message, chat_type, attribute, path, chat_id, reply_id);


//            imageView.setImageDrawable(Drawable.createFromPath(path));
//            Log.i("Info", "Path: " + path);
        }
    }


    class DownloadTaskTwo extends AsyncTask<String, Integer, String> {

//        ProgressDialog progressDialog;

        /**
         * Set up a ProgressDialog
         */
        @Override
        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(TestingActivity.this);
//            progressDialog.setTitle("Download in progress...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setMax(100);
//            progressDialog.setProgress(0);
//            progressDialog.show();

        }

        /**
         * Background task
         */
        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            int file_length;

            Log.i("CheckImageCheckDownload", "path = "+path);
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();

                /**
                 * Create a folder
                 */
                File new_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
                if (!new_folder.exists()) {
                    if (new_folder.mkdir()) {
                        Log.i("ChatsendImageKnow", "Folder succesfully created");
                    } else {
                        Log.i("ChatsendImageKnow", "Failed to create folder");
                    }
                } else {
                    Log.i("ChatsendImageKnow", "Folder already exists" + new_folder);
                }

                /**
                 * Create an output file to store the image for download
                 */

                Log.i("ChatsendImageKnow", "output_file: " + new_folder);
                File output_file = new File(new_folder, imagenameList.get(check_position));
                Log.i("ChatsendImageKnow", "output_file: " + output_file);
                OutputStream outputStream = new FileOutputStream(output_file);
                Log.i("ChatsendImageKnow", "output_file: " + output_file);
                Log.i("ChatsendImageKnow", "outputStream: " + outputStream);
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

                Log.d("InfoInfoInfo", "inputStream: " + inputStream);
                byte[] data = new byte[1024];
                int total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;

                    outputStream.write(data, 0, count);
                    int progress = 100 * total / file_length;
                    publishProgress(progress);

                    Log.i("ChatsendImageKnow", "Progress: " + Integer.toString(progress));
                }
                inputStream.close();
                outputStream.close();

                Log.i("ChatsendImageKnow", "file_length: " + Integer.toString(file_length));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download complete.";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
//            progressDialog.hide();

            Toast.makeText(_context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
            File output_file = new File(folder, imagenameList.get(check_position));
            String path = output_file.toString();

            Log.i("CheckImageCheckDownload", "image path: " + path);
            Log.i("CheckImageCheckDownload", "image = " + imageList.get(check_position));

            defaultpopupnew = new MyFCMService(_context);
            defaultpopupnew.onMessageReceivedImage(sender, imageList.get(check_position), chat_type, attribute, path, chat_id, reply_id);

            edit.putString("scroll", "1");
            edit.apply();


            if(ArraySizeinImage == check_position){
                Log.i("CheckImageCheckDownload", "if = 1 " );
                check_position = 0;
                imageList.clear();
                imagenameList.clear();
                ArraySizeinImage = 0;
                checkloadimage = true;
            }else {
                check_position = check_position+1;

                Log.i("CheckImageCheckDownload", "if = 2 " );

                DownloadTaskTwo downloadTask = new DownloadTaskTwo();
                downloadTask.execute(imageList.get(check_position));

            }

//            imageView.setImageDrawable(Drawable.createFromPath(path));
//            Log.i("Info", "Path: " + path);
        }
    }


    class DownloadTaskAudio extends AsyncTask<String, Integer, String> {

//        ProgressDialog progressDialog;

        /**
         * Set up a ProgressDialog
         */
        @Override
        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(TestingActivity.this);
//            progressDialog.setTitle("Download in progress...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setMax(100);
//            progressDialog.setProgress(0);
//            progressDialog.show();

        }

        /**
         * Background task
         */
        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            int file_length;

            Log.d("InfoInfoInfo: path", path);
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();

                /**
                 * Create a folder
                 */
                File new_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
                if (!new_folder.exists()) {
                    if (new_folder.mkdir()) {
                        Log.d("InfoInfoInfo", "Folder succesfully created");
                    } else {
                        Log.d("InfoInfoInfo", "Failed to create folder");
                    }
                } else {
                    Log.d("InfoInfoInfo", "Folder already exists" + new_folder);
                }

                /**
                 * Create an output file to store the image for download
                 */


                File output_file = new File(new_folder, audio_name);
                OutputStream outputStream = new FileOutputStream(output_file);
                Log.d("InfoInfoInfo", "output_file: " + output_file);
                Log.d("InfoInfoInfo", "outputStream: " + outputStream);
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

                Log.d("InfoInfoInfo", "inputStream: " + inputStream);
                byte[] data = new byte[1024];
                int total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;

                    outputStream.write(data, 0, count);
                    int progress = 100 * total / file_length;
                    publishProgress(progress);

                    Log.d("InfoInfoInfo", "Progress: " + Integer.toString(progress));
                }
                inputStream.close();
                outputStream.close();

                Log.d("InfoInfoInfo", "file_length: " + Integer.toString(file_length));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download complete.";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
//            progressDialog.hide();

            Toast.makeText(_context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
            File output_file = new File(folder, audio_name);
            String path = output_file.toString();

            Log.d("onundeliveredMessage", "audio path: " + path);
            Log.d("onundeliveredMessage", "audio = " + audio);
            Log.d("alletReplyImage", "reply_id   =    " + reply_id);
            defaultpopupnew = new MyFCMService(_context);
            defaultpopupnew.onMessageReceivedImage(sender, audio, chat_type, attribute, path, chat_id, reply_id);
            edit.putString("scroll", "1");
            edit.apply();

//            imageView.setImageDrawable(Drawable.createFromPath(path));
//            Log.i("Info", "Path: " + path);
        }
    }


    class DownloadTaskVideo extends AsyncTask<String, Integer, String> {

//        ProgressDialog progressDialog;

        /**
         * Set up a ProgressDialog
         */
        @Override
        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(TestingActivity.this);
//            progressDialog.setTitle("Download in progress...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setMax(100);
//            progressDialog.setProgress(0);
//            progressDialog.show();

        }

        /**
         * Background task
         */
        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            int file_length;

            Log.d("onundeliveredMessage: path", path);
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();

                /**
                 * Create a folder
                 */
                File new_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
                if (!new_folder.exists()) {
                    if (new_folder.mkdir()) {
                        Log.d("InfoInfoInfo", "Folder succesfully created");
                    } else {
                        Log.d("InfoInfoInfo", "Failed to create folder");
                    }
                } else {
                    Log.d("InfoInfoInfo", "Folder already exists" + new_folder);
                }

                /**
                 * Create an output file to store the image for download
                 */


                File output_file = new File(new_folder, video_name);
                OutputStream outputStream = new FileOutputStream(output_file);
                Log.d("InfoInfoInfo", "output_file: " + output_file);
                Log.d("InfoInfoInfo", "outputStream: " + outputStream);
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

                Log.d("InfoInfoInfo", "inputStream: " + inputStream);
                byte[] data = new byte[1024];
                int total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;

                    outputStream.write(data, 0, count);
                    int progress = 100 * total / file_length;
                    publishProgress(progress);

                    Log.d("InfoInfoInfo", "Progress: " + Integer.toString(progress));
                }
                inputStream.close();
                outputStream.close();

                Log.d("InfoInfoInfo", "file_length: " + Integer.toString(file_length));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download complete.";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
//            progressDialog.hide();

            Toast.makeText(_context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
            File output_file = new File(folder, video_name);
            String path = output_file.toString();

            Log.i("onundeliveredMessage", "path: " + path);

            Log.i("onundeliveredMessage", "thumb = " + thumb);
            defaultpopupnew = new MyFCMService(_context);
            defaultpopupnew.onMessageReceivedVideo(sender, thumb, chat_type, attribute, path, chat_id, reply_id);

            edit.putString("scroll", "1");
            edit.apply();
//            imageView.setImageDrawable(Drawable.createFromPath(path));
//            Log.i("Info", "Path: " + path);
        }
    }


    class DownloadTaskDoc extends AsyncTask<String, Integer, String> {

//        ProgressDialog progressDialog;

        /**
         * Set up a ProgressDialog
         */
        @Override
        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(TestingActivity.this);
//            progressDialog.setTitle("Download in progress...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setMax(100);
//            progressDialog.setProgress(0);
//            progressDialog.show();

        }

        /**
         * Background task
         */
        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            int file_length;

//            Log.d("onundeliveredMessage: path", path);
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();

                /**
                 * Create a folder
                 */
                File new_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
                if (!new_folder.exists()) {
                    if (new_folder.mkdir()) {
//                        Log.d("onundeliveredMessage", "Folder succesfully created");
                    } else {
//                        Log.d("onundeliveredMessage", "Failed to create folder");
                    }
                } else {
//                    Log.d("onundeliveredMessage", "Folder already exists" + new_folder);
                }

                /**
                 * Create an output file to store the image for download
                 */


                File output_file = new File(new_folder, doc_name);
                OutputStream outputStream = new FileOutputStream(output_file);
//                Log.d("onundeliveredMessage", "output_file: " + output_file);
//                Log.d("onundeliveredMessage", "outputStream: " + outputStream);
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

//                Log.d("onundeliveredMessage", "inputStream: " + inputStream);
                byte[] data = new byte[1024];
                int total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;

                    outputStream.write(data, 0, count);
                    int progress = 100 * total / file_length;
                    publishProgress(progress);

//                    Log.d("onundeliveredMessage", "Progress: " + Integer.toString(progress));
                }
                inputStream.close();
                outputStream.close();

//                Log.d("onundeliveredMessage", "file_length: " + Integer.toString(file_length));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download complete.";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
//            progressDialog.hide();

            Toast.makeText(_context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
            File output_file = new File(folder, doc_name);
            String path = output_file.toString();

            Log.i("onundeliveredMessage", "path: " + path);
            Log.i("onundeliveredMessage", "sender: " + sender);
            defaultpopupnew = new MyFCMService(_context);
            defaultpopupnew.onMessageReceivedDoc(sender, chat_type, path, reply_id);
            edit.putString("scroll", "1");
            edit.apply();

//            imageView.setImageDrawable(Drawable.createFromPath(path));
//            Log.i("Info", "Path: " + path);
        }
    }


    public JSONObject jsonObject;

    public void sende() {


        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);

        Log.d("CheckChatAPI", "connected 1 = " + sharedPreferences.getString("mobile", ""));
//        mSocket.emit("new user",sharedPreferences.getString("mobile",""));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", sharedPreferences.getString("mobile", ""));
            jsonObject.put("roomName", "GPSLAB");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d("WalletlsjWallet", "onConnect 1 = ");
        mSocket.emit("subscribe", jsonObject);


    }

    public void Readmessage(String senderid) {
        Log.d("Readmessage", "Messgedata 1 = " + senderid);

//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", sharedPreferences.getString("mobile", ""));
            jsonObject.put("sender", senderid);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Readmessage", "Emit   jsonObject 1 = " + jsonObject);
        mSocket.emit("msgRead", jsonObject);


    }

    public void sendmessage(String number, String Messgedata, int type, String ids, String replay_id) {
        Log.d("CheckChatAPI", "number 1 = " + number);
        Log.d("CheckChatAPI", "Messgedata 1 = " + Messgedata);

//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", number);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
            jsonObject.put("msg", Messgedata);
            jsonObject.put("type", String.valueOf(type));
            jsonObject.put("message_id", ids);
            jsonObject.put("reply_id", replay_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("PandingMessage", "jsonObject Content  =  " + jsonObject);
        Log.d("CheckChatAPINew", "send Message  = " + jsonObject);
        mSocket.emit("message", jsonObject);


    }


    public void sendmessage_reply(String number, String Messgedata, int type, String ids, String reply_id) {
        Log.d("CheckChatAPI", "number 1 = " + number);
        Log.d("CheckChatAPI", "Messgedata 1 = " + Messgedata);

//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", number);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
            jsonObject.put("msg", Messgedata);
            jsonObject.put("type", String.valueOf(type));
            jsonObject.put("message_id", ids);
            jsonObject.put("reply_id", reply_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("PandingMessage", "jsonObject Content  =  " + jsonObject);
        Log.d("msg_read_report_sender", "jsonObject  = " + jsonObject);
        mSocket.emit("message", jsonObject);


    }

    public void sendmessagemsgRead(String number, String Messageid) {
        Log.d("sendmessagemsgRead", "number 1 = " + number);
        Log.d("sendmessagemsgRead", "Messgedata 1 = " + Messageid);

//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", number);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));

            jsonObject.put("message_id", Messageid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("msgReadmsgReadmsgRead", "sendmessagemsgRead jsonObject  = " + jsonObject);
        mSocket.emit("msgRead", jsonObject);


    }

    /// send Contacts
    public void sendcontact(String senderid, String mobileNo, String Contact_Name, int type, String ids) {
        Log.d("MainActivityLL", "number 1 = " + senderid);
        Log.d("MainActivityLL", "Messgedata 1 = " + mobileNo);
        Log.d("MainActivityLL", "Contact_Name 1 = " + Contact_Name);
//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", senderid);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
            jsonObject.put("number", mobileNo);
            jsonObject.put("contact_name", Contact_Name);
            jsonObject.put("type", String.valueOf(type));
            jsonObject.put("message_id", ids);
            jsonObject.put("reply_id", "NA");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("MainActivityLL", "jsonObject  = " + jsonObject);
        mSocket.emit("message", jsonObject);


    }

    public void sendcontactReplay(String senderid, String mobileNo, String Contact_Name, int type, String ids, String reply_id) {
        Log.d("MainActivityLL", "number 1 = " + senderid);
        Log.d("MainActivityLL", "Messgedata 1 = " + mobileNo);
        Log.d("MainActivityLL", "Contact_Name 1 = " + Contact_Name);
//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", senderid);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
            jsonObject.put("number", mobileNo);
            jsonObject.put("contact_name", Contact_Name);
            jsonObject.put("type", String.valueOf(type));
            jsonObject.put("message_id", ids);
            jsonObject.put("reply_id", reply_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("MainActivityLL", "jsonObject  = " + jsonObject);
        mSocket.emit("message", jsonObject);


    }
    // send location

    public void sendlocation(String senderid, String data, int type, String ids) {
        Log.d("MainActivityLL", "number 1 = " + senderid);
        Log.d("MainActivityLL", "Messgedata 1 = " + data);
        Log.d("MainActivityLL", "type 1 = " + type);
        Log.d("MainActivityLL", "ids 1 = " + ids);
//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", senderid);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
            jsonObject.put("msg", data);
            jsonObject.put("type", String.valueOf(type));
            jsonObject.put("message_id", ids);
            jsonObject.put("reply_id", "NA");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("MainActivityLL", "jsonObject  = " + jsonObject);
        mSocket.emit("message", jsonObject);


    }


    public void sendlocationReplay(String senderid, String data, int type, String ids, String reply_id) {
        Log.d("MainActivityLL", "number 1 = " + senderid);
        Log.d("MainActivityLL", "Messgedata 1 = " + data);
        Log.d("MainActivityLL", "type 1 = " + type);
        Log.d("MainActivityLL", "ids 1 = " + ids);
//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", senderid);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
            jsonObject.put("msg", data);
            jsonObject.put("type", String.valueOf(type));
            jsonObject.put("message_id", ids);
            jsonObject.put("reply_id", reply_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("MainActivityLL", "jsonObject  = " + jsonObject);
        mSocket.emit("message", jsonObject);


    }

    public JSONObject jsonObjecthome;


    public void senddelete_home(final String number) {
//        Log.i("YUYULuckyYUYTUNowkyY", "number 1 = " + incomming);


//
//        {
//            try {
//                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
//            } catch (URISyntaxException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        mSocket.connect();


        Log.d("delete_medelete_me", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObjecthome = new JSONObject();
//            jsonObject.put("Mobileno_to", incomming);
            jsonObjecthome.put("mobileno", sharedPreferences.getString("mobile", ""));

            jsonObject = new JSONObject();
            jsonObject.put("Mobileno_to", number);
            jsonObject.put("Mobileno_from", sharedPreferences.getString("mobile", ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("YUYULuckyYUYTUNowkyY", "home jsonObject  = " + jsonObjecthome);

        mSocket.emit("Home", jsonObjecthome);
//
//        Log.i("YUYULuckyYUYTUNowkyY", "incomming jsonObject  = " + jsonObject);
//        mSocket.emit("incoming", jsonObject);


    }

    public void sendincomming_me(String incomming) {
        Log.i("YUYULuckyYUYTUNowkyY", "number 1 = " + incomming);


//
//        {
//            try {
//                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
//            } catch (URISyntaxException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        mSocket.connect();


        Log.d("delete_medelete_me", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("mobileno", incomming);
//            jsonObject.put("Mobileno_from", sharedPreferences.getString("mobile", ""));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("incomingincoming", "incomming jsonObject  = " + jsonObject);
        mSocket.emit("incoming", jsonObject);


    }


    public void senddieldcall_me(String incomming) {
        Log.i("Call_Ane_ke_time_notifcation", "number 1 = " + incomming);


//
//        {
//            try {
//                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
//            } catch (URISyntaxException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        mSocket.connect();


        Log.d("delete_medelete_me", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            // mobile_to_ :- जिसको call करी जा रही है।
            // mobile_from:- जो call करेगा उसका
            jsonObject.put("mobileno_to", incomming);
            jsonObject.put("mobileno_from", sharedPreferences.getString("mobile", ""));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Call_Ane_ke_time_notifcation", "outgoing jsonObject  = " + jsonObject);
        mSocket.emit("outgoing", jsonObject);


    }





    public void reconnect() {
        Log.i("reconnect_reconnect", "number 1 = " );


//
//        {
//            try {
//                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
//            } catch (URISyntaxException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        mSocket.connect();


        Log.i("reconnect_reconnect", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("mobile", sharedPreferences.getString("mobile", ""));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("reconnect_reconnect", "outgoing jsonObject  = " + jsonObject);
        mSocket.emit("reconnect", jsonObject);


    }

    public void senddelete_me(String senderid, String ids) {
        Log.d("delete_medelete_me", "number 1 = " + senderid);

        Log.d("delete_medelete_me", "ids 1 = " + ids);
//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("delete_medelete_me", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", senderid);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));

            jsonObject.put("message_id", ids);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("delete_medelete_me", "jsonObject  = " + jsonObject);
        mSocket.emit("delete_me", jsonObject);


    }

    public void senddelete_foreveryone(String senderid, String ids) {
        Log.d("delete_medelete_me", "number 1 = " + senderid);

        Log.d("delete_medelete_me", "ids 1 = " + ids);
//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("delete_medelete_me", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();


            jsonObject.put("receiver", senderid);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
            jsonObject.put("msg", "NA");
            jsonObject.put("type", "30");
            jsonObject.put("message_id", ids);
            jsonObject.put("reply_id", "NA");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("delete_medelete_me", "jsonObject  = " + jsonObject);
        mSocket.emit("message", jsonObject);


    }

    public void sendTypeing(String number) {
        Log.d("onStopTypingonStopTyping", "number 1 = " + number);


//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
//        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", number);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("onStopTypingonStopTyping", "sendTypeing     jsonObject 1 = " + jsonObject);
        mSocket.emit("start_typing", jsonObject);


    }


    public void sendStopTypeing(String number) {
        Log.d("CheckChatAPI", "number 1 = " + number);


//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", number);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("CheckChatAPI", "sendStopTypeing   jsonObject 1 = " + jsonObject);
        mSocket.emit("stopTyping", jsonObject);


    }

    public void sendmessageImage(String number, String Messgedata, int type, Integer arraysize, Integer position, String ids, String reply_id) {
        Log.d("CheckChatAPI", "number 1 = " + number);
        Log.d("CheckChatAPI", "Messgedata 1 = " + Messgedata);

//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", number);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
            jsonObject.put("msg", Messgedata);
            jsonObject.put("type", String.valueOf(type));
            jsonObject.put("message_id", ids);
            jsonObject.put("reply_id", reply_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("CheckChatAPI", "jsonObject 1 = " + jsonObject);
        mSocket.emit("message", jsonObject);
        Log.d("CheckImageSize", "image_name total size  =   " + arraysize);
        if (position < arraysize) {
            Log.d("CheckImageSize", "image_name position  =   0 " + position);
            position = position + 1;
            Log.d("CheckImageSize", "image_name position  =   1 " + position);
//            ((ChatActivity) _context).replydataimage(position);
            edit.putInt("Image_position", position);
            edit.putInt("Image_check", 1);
            edit.apply();
        }


    }


    public void sendmessageVideo(String number, String Messgedata, int type, String ids, int arraysize, int position) {
        Log.d("CheckChatAPI", "number 1 = " + number);
        Log.d("CheckChatAPI", "Messgedata 1 = " + Messgedata);

//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", number);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
            jsonObject.put("msg", Messgedata);
            jsonObject.put("type", String.valueOf(type));
            jsonObject.put("message_id", ids);
            jsonObject.put("reply_id", "NA");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("sendVideocheck", "jsonObject 1 = " + jsonObject);
        mSocket.emit("message", jsonObject);
        Log.d("WalletCheckLucky", "image_name total size  =   " + arraysize);
        if (position < arraysize) {
            Log.d("WalletCheckLucky", "image_name position  =   0 " + position);
            position = position + 1;
            Log.d("WalletCheckLucky", "image_name position  =   1 " + position);
//            ((ChatActivity) _context).replydatavideo(position);
        }


    }

    public void sendmessageDoc(String number, String Messgedata, int type, String ids, int arraysize, int position) {
        Log.d("CheckdocSize", "number 1 = " + number);
        Log.d("CheckdocSize", "Messgedata 1 = " + Messgedata);

//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        sharedPreferences = _context.getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", number);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
            jsonObject.put("msg", Messgedata);
            jsonObject.put("type", String.valueOf(type));
            jsonObject.put("message_id", ids);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("CheckdocSize", "jsonObject 1 = " + jsonObject);
        mSocket.emit("message", jsonObject);
        Log.d("CheckdocSize", "image_name total size  =   " + arraysize);
        if (position < arraysize) {
            Log.d("CheckdocSize", "image_name position  =   0 " + position);
            position = position + 1;
            Log.d("CheckdocSize", "image_name position  =   1 " + position);
//            ((ChatActivity) _context).replydatadoc(position);

            edit.putInt("Docs_position", position);
            edit.putInt("Docs_check", 1);
            edit.apply();
        }


    }


    public String MobileNumber;
    public Bitmap bitmap1;
    NotificationManager nm;

    int NotID = 1;
    public static String id1 = "test_channel_01";
    public static String id2 = "test_channel_02";
    public static String id3 = "test_channel_03";

    public String image_Notification;
    public void sendPushNotificationAPI26(String Sender, String Chat_type, String Message_type) throws ExecutionException, InterruptedException {

        Log.d("WalletNotification", "Sender = " + Sender);
        Log.d("WalletNotification", "Chat_type = " + Chat_type);
        Log.d("WalletNotification", "Message_type = " + Message_type);
//        User user = new User();
        String title = Sender;
        String message = Message_type;


        MobileNumber = message;
//        if(Chat_type.length()>5){
//            image_Notification = Chat_type;
//        }else {
            image_Notification = "https://thecallify.com/callify_logo.png";
//        }

        FutureTarget<Bitmap> bitmap;
        bitmap = Glide.with(_context).asBitmap().load(image_Notification).submit();

        SharedPreferences sharedPreferences = _context.getSharedPreferences("ChatData", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("chat_id", sender);
        edit.apply();

        Intent notificationIntent = new Intent(_context, ChatActivity.class);
        notificationIntent.putExtra("id", sender);
//        notificationIntent.putExtra("number", message);
        notificationIntent.putExtra("name", title);
        notificationIntent.putExtra("image", image);
//        notificationIntent.putExtra("mytype", "iconmsg" + NotID);
//        notificationIntent.putExtra("number", message);
//        notificationIntent.putExtra("name", title);
//        notificationIntent.putExtra("img", image);
        Log.d("WalletgetServiceNew", "title  =  " + title);
        Log.d("WalletgetServiceNew", "message  =  " + message);
        Log.d("WalletgetServiceNew", "image  =  " + image);
        PendingIntent contentIntent = PendingIntent.getActivity(_context, NotID, notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "Lcky";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Lucky Agarwa", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("This is my name");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);


//            nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//            createchannel();
//            and5_notificaiton(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        Log.d("WalletgetServiceNew", "bitmap1  =  " + bitmap1);

        NotificationCompat.Builder notificationbuild = new NotificationCompat.Builder(_context, NOTIFICATION_CHANNEL_ID);
        notificationbuild.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis())
                .setTicker("Herry")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.logonew)
                .setLargeIcon(bitmap.get())
                .setContentIntent(contentIntent)
                .setContentText(message)
                .setSound(Uri.parse("android.resource://" + _context.getPackageName() + "/" + R.raw.notification))
                .setContentInfo("info");

        notificationManager.notify(1, notificationbuild.build());
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output;
        Rect srcRect, dstRect;
        float r;
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        if (width > height) {
            output = Bitmap.createBitmap(height, height, Bitmap.Config.ARGB_8888);
            int left = (width - height) / 2;
            int right = left + height;
            srcRect = new Rect(left, 0, right, height);
            dstRect = new Rect(0, 0, height, height);
            r = height / 2;
        } else {
            output = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
            int top = (height - width) / 2;
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void and5_notificaiton(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String names = data.get("title");
        String mobile = data.get("body");
        String image = data.get("image");
        Intent notificationIntent = new Intent(_context, UserProfile.class);
        notificationIntent.putExtra("mytype", "iconmsg" + NotID);
        notificationIntent.putExtra("mobile", mobile);
        notificationIntent.putExtra("name", names);
        notificationIntent.putExtra("img", image);
        PendingIntent contentIntent = PendingIntent.getActivity(_context, NotID, notificationIntent, 0);

        Notification noti = new NotificationCompat.Builder(_context, id3)
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
}
