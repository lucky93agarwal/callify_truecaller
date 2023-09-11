package com.gpslab.kaun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gpslab.kaun.Contact.ContactViewModel;
import com.gpslab.kaun.Contact.ITelephony;
import com.gpslab.kaun.Contact.NewContact;
import com.gpslab.kaun.DB.FirstTableData;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import com.gpslab.kaun.OTP.OTPActivity;
import com.gpslab.kaun.Webapi.ChatWebAPI;
import com.gpslab.kaun.Webapi.createnew;
import com.gpslab.kaun.block.AsyncExecutorUtil;
import com.gpslab.kaun.block.BlockedNumber;
import com.gpslab.kaun.block.BlockedNumberDao;
import com.gpslab.kaun.block.BlockedNumberDatabase;
import com.gpslab.kaun.check.CheckActivity;
import com.gpslab.kaun.model.Constants;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit_model.VerifyUserRequestJson;
import com.gpslab.kaun.retrofit_model.VerifyUserResponseJson;

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
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class PhoneCallReceiver extends BroadcastReceiver {

    public String resTxt = null;
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;
    private static String blocksavedNumber;
    public MyDbHandler myDbHandler;
    private List<NewContact> contacts  = new ArrayList<>();
    public SharedPreferences sharedPreferences;

    public SharedPreferences sharedPreferencesnew;
    public SharedPreferences.Editor edit;
    private ContactViewModel contactViewModel;

    //    private Socket mSocket;
//
//    {
//        try {
//            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "CALL_STATE_RINGING", Toast.LENGTH_SHORT).show();
        sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        sharedPreferencesnew = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        try {
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {

                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            } else {

                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                int state = 0;

                if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    state = TelephonyManager.CALL_STATE_IDLE;
                    Log.i("Call_Ane_ke_time_notifcation","CALL_STATE_IDLE");
                    if(number !=null){
                        String apinumber = number;
                        if(number.length() == 13) {
                            apinumber = number.substring(1, number.length());

                        }

                        Log.i("Call_Ane_ke_time_notifcation","CALL_STATE_IDLE number = "+apinumber);
                        Log.i("Call_Ane_ke_time_notifcation","image = "+sharedPreferencesnew.getString("before_call_Image","img"));
                        Log.i("Call_Ane_ke_time_notifcation","Name = "+sharedPreferencesnew.getString("before_call_name","img"));
                        Log.i("Call_Ane_ke_time_notifcation","Space_Result = "+sharedPreferencesnew.getString("before_call_Space_Result","img"));
                        Log.i("Call_Ane_ke_time_notifcation","SpamCount = "+sharedPreferencesnew.getString("before_call_SpamCount","img"));

                        if(apinumber.equalsIgnoreCase(sharedPreferencesnew.getString("before_call_number",""))){
                            Intent intents = new Intent(context, MyCustomDialog.class);
                            intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intents.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intents.putExtra("contact_name", sharedPreferencesnew.getString("before_call_name","Name"));
                            intents.putExtra("phone_no", number);
                            intents.putExtra("img", sharedPreferencesnew.getString("before_call_Image","img"));
                            intents.putExtra("is_spam", sharedPreferencesnew.getString("before_call_Space_Result","Name"));
                            intents.putExtra("spam_count", sharedPreferencesnew.getString("before_call_SpamCount","Name"));
                            intents.putExtra("type", "Ringing");
                            intents.putExtra("tag", "tag");

                            context.startActivity(intents);
                        }


//                        apinumber = number.substring(1,number.length());
//                        ChatWebAPI addpopuptwo = new ChatWebAPI(context);
//                        addpopuptwo.data();
//                        addpopuptwo.senddieldcall_me(apinumber);
//                        Log.i("YUYULuckyLUcky","PhoneCallReceiver 736 q =  EXTRA_STATE_IDLE "+apinumber);
                    }

                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK;

                    Log.i("Call_Ane_ke_time_notifcation","EXTRA_STATE_OFFHOOK");

//                    Intent intents = new Intent(context, MyCustomDialog.class);
//                    intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intents.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    intents.putExtra("contact_name", sharedPreferencesnew.getString("before_call_name","Name"));
//                    intents.putExtra("phone_no", number);
//                    intents.putExtra("img", sharedPreferencesnew.getString("before_call_Image","img"));
//                    intents.putExtra("is_spam", sharedPreferencesnew.getString("before_call_Space_Result","Name"));
//                    intents.putExtra("spam_count", sharedPreferencesnew.getString("before_call_SpamCount","Name"));
//                    intents.putExtra("type", "Ringing");
//                    intents.putExtra("tag", "tag");
//
//                    context.startActivity(intent);
                    String apinumber = number;
                    if(number.length()==13){
                        apinumber = number.substring(1,number.length());
                    }else if(number.length() == 10){
                        apinumber = "91"+apinumber;
                    }

                    ChatWebAPI addpopuptwo = new ChatWebAPI(context);
                    addpopuptwo.data();
                    addpopuptwo.senddieldcall_me(apinumber);
//                    Log.i("Call_krne_ke_time","PhoneCallReceiver 736 q =  EXTRA_STATE_IDLE "+apinumber);
                    Log.i("YUYULuckyLUcky","PhoneCallReceiver two 736 q =  CALL_STATE_OFFHOOK ");
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    state = TelephonyManager.CALL_STATE_RINGING;
                    Log.i("LOG_TAG_Block", "mobile up no =   "+String.valueOf(number));
                    Log.i("LOG_TAG_Block","mobile up 2 no = "+number.substring(1,number.length()));
                    savedNumber = number;
                    if(TextUtils.isEmpty(number)){

                    }else {

                        blocksavedNumber = String.valueOf(number.substring(1,number.length()));
                        Log.i("LOG_TAG_Block", "mobile 2 no =   "+blocksavedNumber);
                        BlockedNumberDao blockedNumberDao = BlockedNumberDatabase.getInstance(context).blockedNumberDao();


                        AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {


                            List<BlockedNumber> data =blockedNumberDao.getAll();

                            for(int i=0;i<data.size();i++){
                                String noblock = data.get(i).getPhoneNumber();
                                Log.i("LOG_TAG_Block", "match no = "+noblock);
                                if(blocksavedNumber.equalsIgnoreCase(noblock)){
                                    Log.i("LOG_TAG_Block", "true = ");
                                    TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

                                    telecomManager.endCall();
                                }
                            }

                        });
                    }








                    if(number.length()>=10){


                        contactViewModel = new ContactViewModel(context.getApplicationContext());
                        contacts = contactViewModel.getContacts();
                        String getNumber = savedNumber.substring(savedNumber.length() -10);


                        for(int i=0;i<contacts.size();i++){
                            number = contacts.get(i).getPhoneNumber();



                            if(number.length()>9){
                                String Number_new = number.substring(number.length() - 10);

                                if(getNumber.equalsIgnoreCase(Number_new)){
                                    check = true;
                                }
                            }

                        }

                        /** जब आप के पास call आती है तो आप आपने local storage में ये check करेगे कि या ये numbmer save है या नही
                         * यदि number save नही है तो आप popup को show करायेगे।
                         * */
                        if(!check){
//                        retrofit(savedNumber,context);
                            SharedPreferences sh = context.getSharedPreferences("incoming",0);
                            SharedPreferences.Editor edi = sh.edit();
                            edi.putInt("icomming",1);
                            edi.apply();


                            Log.i("incomingincoming","savedNumber = "+savedNumber);


                        }else {
                            Log.i("incomingincoming","savedNumber = no");
                            SharedPreferences sh = context.getSharedPreferences("incoming",0);
                            SharedPreferences.Editor edi = sh.edit();
                            edi.putInt("icomming",1);
                            edi.apply();
                        }
                        Log.i("incomingincoming","savedNumber = "+savedNumber);
                        ChatWebAPI addpopuptwo = new ChatWebAPI(context);
                        addpopuptwo.data();
                        addpopuptwo.sendincomming_me(savedNumber.substring(1,savedNumber.length()));


                    }
                    Log.i("YUYULuckyLUcky","PhoneCallReceiver 736 q =  EXTRA_STATE_RINGING ");


                }
//                mSocket.on(Socket.EVENT_CONNECT, onConnect);
//                mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
//                mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
////        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//                mSocket.on("new_message", onNewMessage);
//                mSocket.on("user joined", onUserJoined);
//                mSocket.on("user left", onUserLeft);
//                mSocket.on("typing", onTyping);
//                mSocket.on("stop typing", onStopTyping);
//                mSocket.connect();
                onCallStateChanged(context, state, number);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private TextToSpeech mTts;

    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        Log.i("YUYULuckyLUcky","PhoneCallReceiver 736 q =  onIncomingCallStarted "+number);
    }

    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.i("YUYULuckyLUcky","PhoneCallReceiver 736 q =  onIncomingCallEnded "+number);
    }


    protected void onDeaildCallEnded(Context ctx, String number) {
        Log.d("YUYULuckyYUYTUNow","PhoneCallReceiver 736 q =  onDeaildCallEnded "+number);

        Log.i("YUYULuckyLUcky","PhoneCallReceiver 736 q =  onDeaildCallEnded "+number);

    }

    public boolean check = false;
    public String DildCallNumber;

    public void onCallStateChanged(Context context, int state, String number) {
//        Log.d("WalletlsjWaRishiNumber", "onCallStateChanged = =  " + DildCallNumber + "  State = = =  " + state);
        if (state == 2) {
            if (number != null) {
                DildCallNumber = number;
//                Log.d("WalletlsjWaRishiNumber","onCallStateChanged = =  "+DildCallNumber+"  State = = =  "+state);
//                SendServer(DildCallNumber,context);

                retrofit(DildCallNumber, context);
//                SendDataToServer(DildCallNumber, context);
            }
        }


        if (lastState == state) {

            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:

                // call ringling
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                Log.i("YUYULuckyYUYTUNowkyY","PhoneCallReceiver 736 q =  CALL_STATE_RINGING ");
                Log.i("YUYULuckyYUYTUNowkyY","PhoneCallReceiver 736 q =  CALL_STATE_RINGING = "+savedNumber);
                Log.i("YUYULuckyYUYTUNowkyY","PhoneCallReceiver 736 q =  CALL_STATE_RINGING = "+savedNumber.substring(1,savedNumber.length()));
                Log.i("LOG_TAG_Block", "mobile 1 no =   "+String.valueOf(savedNumber));






                Log.d("YUYULuckyYUYTUNow","PhoneCallReceiver 736 q =  CALL_STATE_RINGING ");
                onIncomingCallStarted(context, number, callStartTime);

                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                // call diald
                isIncoming = true;
                Log.i("YUYULuckyYUYTUNowkyY","PhoneCallReceiver two 736 q =  CALL_STATE_OFFHOOK ");

                if (isIncoming)
                {
                    Log.i("YUYULuckyYUYTUNowkyY","PhoneCallReceiver 736 q =  onDeaildCallEnded "+number);


                    sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                    String YourNo = sharedPreferences.getString("mobile","");
                    createnew addpopup = new createnew(context);

                    String datanew = YourNo+","+number.substring(number.length() - 10)+","+"calling";
                    Log.d("Call_Krne_ke_time_api","datanew = "+datanew);
                    addpopup.sendmessage(String.valueOf(datanew));
//                    onDeaildCallEnded(context,savedNumber);
                    if(number.length()>=10){

                        myDbHandler = new MyDbHandler(context,"userbd",null,1);

                        Temp.setMyDbHandler(myDbHandler);
                        myDbHandler = Temp.getMyDbHandler();



                        Log.i("YUYULuckyYUYTUNowkyY","User Name 4 = =  "+number);

                        ArrayList<FirstTableData> arrayList = myDbHandler.viewUser();
                        for (FirstTableData u:arrayList){
//
                            if(u.getMobileone().equalsIgnoreCase(number)){
//
                                number = u.getName();
                            }
                        }





                        Log.i("YUYULuckyYUYTUNowkyY","PhoneCallReceiver 736 q =  Mobile No =  "+number+" State = "+state+" lastState = "+lastState);
                    }
                }


            case TelephonyManager.CALL_STATE_IDLE:

                // call cut
                Log.i("YUYULuckyYUYTUNowkyY","PhoneCallReceiver 736 q =  CALL_STATE_IDLE ");
//                Log.d("LuckyYUYTUNosdfw","PhoneCallReceiver 736 q =  CALL_STATE_IDLE =  "+isIncoming+ "  Contest  =  "+context);
                Log.i("YUYULuckyYUYTUNowkyY","User Name dil = =  "+DildCallNumber);
//                if(DildCallNumber.length() == 11){
//                    ChatWebAPI addpopuptwo = new ChatWebAPI(context);
//                    addpopuptwo.data();
//                    addpopuptwo.senddieldcall_me("91"+DildCallNumber.substring(1,DildCallNumber.length()));
//                }else if(DildCallNumber.length() == 12){
//                    ChatWebAPI addpopuptwo = new ChatWebAPI(context);
//                    addpopuptwo.data();
//                    addpopuptwo.senddieldcall_me(DildCallNumber);
//                }else if(DildCallNumber.length() == 10){
//                    ChatWebAPI addpopuptwo = new ChatWebAPI(context);
//                    addpopuptwo.data();
//                    addpopuptwo.senddieldcall_me("91"+DildCallNumber);
//                }else  if(DildCallNumber.length() == 13){
//                    ChatWebAPI addpopuptwo = new ChatWebAPI(context);
//                    addpopuptwo.data();
//                    addpopuptwo.senddieldcall_me(DildCallNumber.substring(1,DildCallNumber.length()));
//                }else{
//                    ChatWebAPI addpopuptwo = new ChatWebAPI(context);
//                    addpopuptwo.data();
//                    addpopuptwo.senddieldcall_me(DildCallNumber);
//                }


                if(isIncoming)
                {
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                    if(number.length()>=10){
                        myDbHandler = new MyDbHandler(context,"userbd",null,1);

                        Temp.setMyDbHandler(myDbHandler);
                        myDbHandler = Temp.getMyDbHandler();

                        Log.d("YUYULuckyYUYTUNowkyY","User Name 7 = =  "+number);
//                        Log.d("WalletlsjWallet","User Name 7 = =  "+number);
                        ArrayList<FirstTableData> arrayList = myDbHandler.viewUser();
                        for (FirstTableData u:arrayList){
//
                            if(u.getMobileone().equalsIgnoreCase(number)){
//
                                number = u.getName();
                            }
                        }

                        Log.d("WalletlsjWaRishiNumber", "fname = = CALL_STATE_IDLE = " );
//                        SendDataToServer_new(savedNumber.substring(savedNumber.length() - 10).toString(),context);
//                        retrofit_new(savedNumber,context);
                        myDbHandler.deletecalllogAll();
                        initRecyclerView(context);

                        Log.d("CheckPermissionLucky","PhoneCallReceiver 736 q =  Mobile No =  "+number+" State = "+state+" lastState = "+lastState);
                    }

                }else {
                    Log.i("YUYULuckyYUYTUNowkyY","User Name dil else  = =  "+DildCallNumber);
//                    SendDataToServer(DildCallNumber,context);
                }
        }
        lastState = state;
    }


    private void initRecyclerView(final Context context){
        contactViewModel = new ContactViewModel(context);

        setContacts(contactViewModel.getCallLog(),context);
    }

    private List<CallLogInfo> filtered_icontacts = new ArrayList<>();
    String insertSecData = "";
    public void setContacts(List<CallLogInfo> contacts,final Context context) {

        this.filtered_icontacts = contacts;
        for (int i = 0; i < contacts.size(); i++) {
            onBind(filtered_icontacts.get(i), i);
        }
        insertSecData = insertSecData.substring(0, insertSecData.length() - 1);
        Log.d("YUYULuckyYUYTUNowkyY", "CallLogService inseart check = " + insertSecData);
        myDbHandler.InsertLogCallerDataIntoSQLiteDatabase(insertSecData);
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        edit.putString("callclog","two");
        edit.apply();


//        initMessageView();

    }
    String insertData = "";
    public String numbers;
    private void onBind(CallLogInfo callLogInfo, int i) {
        Log.d("dfsdfsdfsdfdfsdd", "GetContacts fname = = " + callLogInfo.getName());


        if (TextUtils.isEmpty(callLogInfo.getName())) {

            insertData = insertData + "'" + "NA" + "'";

        } else {
            String Name = callLogInfo.getName();

            insertData = insertData + "'" + Name + "'";

        }

        insertData = insertData + "," + "'" + String.valueOf(i) + "'";

        if (callLogInfo.getNumber().length() > 10) {
            numbers = callLogInfo.getNumber();
            numbers = numbers.substring(numbers.length() - 10).toString();
        } else {
            numbers = callLogInfo.getNumber();
        }

        insertData = insertData + "," + "'" + numbers + "'";


        if (callLogInfo.getCallType().equalsIgnoreCase("null")) {

            insertData = insertData + "," + "'" + "NA" + "'";
        } else {
            insertData = insertData + "," + "'" + callLogInfo.getCallType() + "'";
        }

        insertData = insertData + "," + "'" + "NA" + "'";

        insertData = insertData + "," + "'" + String.valueOf(callLogInfo.getDate()) + "'";

        insertData = insertData + "," + "'" + "0" + "'";

        insertData = insertData + "," + "'" + String.valueOf(callLogInfo.getDuration()) + "'";
        insertSecData = insertSecData + "(" + insertData + ")" + ",";
        insertData = "";
    }
    public VerifyUserRequestJson request = new VerifyUserRequestJson();
    private void retrofit(final String numbers,final Context mContext) {
        Log.i("WalletWalletfgdflucky","2 number = "+numbers);
        String number = numbers.substring(1,numbers.length());
        request.mobile = number;

        request.token = "NA";

        Log.i("WalletWalletfgdflucky","2 request = "+new Gson().toJson(request));

        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.verify_user(request).enqueue(new Callback<VerifyUserResponseJson>() {
            @Override
            public void onResponse(Call<VerifyUserResponseJson> call, Response<VerifyUserResponseJson> response) {
                Log.d("CCpWalletCCpWallet", "nameValuePairs code  = " + response.code());
                Log.i("WalletWalletfgdflucky","2 response = "+new Gson().toJson(response.body()));
                if (response.isSuccessful()) {




                    String results = response.body().response.get(0).result;
                    if(results.equalsIgnoreCase("0")){


                        final Intent intent = new Intent(mContext, StartingPopupActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("contact_name",savedNumber);
                        intent.putExtra("phone_no",response.body().response.get(0).data.get(0).mobile);
                        intent.putExtra("img",response.body().response.get(0).data.get(0).profile_image);
                        intent.putExtra("is_spam",response.body().response.get(0).data.get(0).is_spam);
                        intent.putExtra("spam_count",response.body().response.get(0).data.get(0).spanCount);
                        intent.putExtra("type","Ringing");
                        intent.putExtra("tag","tag");

                        mContext.startActivity(intent);
                    }else if(results.equalsIgnoreCase("1")){

//                        final Intent intent = new Intent(mContext, StartingPopupActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("contact_name",savedNumber);
//                        intent.putExtra("phone_no",response.body().response.get(0).data.get(0).mobile);
//                        intent.putExtra("img",response.body().response.get(0).data.get(0).profile_image);
//                        intent.putExtra("is_spam",response.body().response.get(0).data.get(0).is_spam);
//                        intent.putExtra("spam_count",response.body().response.get(0).data.get(0).spanCount);
//                        intent.putExtra("type","Ringing");
//                        intent.putExtra("tag","tag");
//
//                        mContext.startActivity(intent);
                    }



                } else {
                    switch (response.code()) {
                        case 401:
//                            Toast.makeText(Splash.this, "email and pass not check", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
//                            Toast.makeText(Splash.this, "ForbiddenException", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
//                            Toast.makeText(Splash.this, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
//                            Toast.makeText(Splash.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
//                            Toast.makeText(Splash.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    Toast.makeText(mContext, "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<VerifyUserResponseJson> call, Throwable t) {
                t.printStackTrace();

                Log.e("System error:", t.getLocalizedMessage());

            }
        });
    }


    private void retrofit_new(final String numbers,final Context mContext) {
        String number = numbers.substring(1,numbers.length());
        request.mobile = number;

        request.token = "NA";
        Log.i("WalletWalletfgdflucky","3 request = "+new Gson().toJson(request));


        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.verify_user(request).enqueue(new Callback<VerifyUserResponseJson>() {
            @Override
            public void onResponse(Call<VerifyUserResponseJson> call, Response<VerifyUserResponseJson> response) {
                Log.d("CCpWalletCCpWallet", "nameValuePairs code  = " + response.code());
                Log.i("WalletWalletfgdflucky","3 response = "+new Gson().toJson(response.body()));
                if (response.isSuccessful()) {




                    String results = response.body().response.get(0).result;
                    if(results.equalsIgnoreCase("0")){
//
//
//                        final Intent intent = new Intent(mContext, StartingPopupActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("contact_name",savedNumber);
//                        intent.putExtra("phone_no",response.body().response.get(0).data.get(0).mobile);
//                        intent.putExtra("img",response.body().response.get(0).data.get(0).profile_image);
//                        intent.putExtra("is_spam",response.body().response.get(0).data.get(0).is_spam);
//                        intent.putExtra("spam_count",response.body().response.get(0).data.get(0).spanCount);
//                        intent.putExtra("type","Ringing");
//                        intent.putExtra("tag","tag");
//
//                        mContext.startActivity(intent);



                        final Intent intent = new Intent(mContext, MyCustomDialog.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("contact_name",savedNumber);
                        intent.putExtra("phone_no",response.body().response.get(0).data.get(0).mobile);
                        intent.putExtra("img",response.body().response.get(0).data.get(0).profile_image);
                        intent.putExtra("is_spam",response.body().response.get(0).data.get(0).is_spam);
                        intent.putExtra("spam_count",response.body().response.get(0).data.get(0).spanCount);
                        intent.putExtra("type","Ringing");
                        intent.putExtra("tag","tag");
                        Log.d("WalletlsjWalletsavedNumber", "image = = " + response.body().response.get(0).data.get(0).profile_image);
                        mContext.startActivity(intent);
                    }else if(results.equalsIgnoreCase("1")){
//
//                        final Intent intent = new Intent(mContext, StartingPopupActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("contact_name",savedNumber);
//                        intent.putExtra("phone_no",response.body().response.get(0).data.get(0).mobile);
//                        intent.putExtra("img",response.body().response.get(0).data.get(0).profile_image);
//                        intent.putExtra("is_spam",response.body().response.get(0).data.get(0).is_spam);
//                        intent.putExtra("spam_count",response.body().response.get(0).data.get(0).spanCount);
//                        intent.putExtra("type","Ringing");
//                        intent.putExtra("tag","tag");
//
//                        mContext.startActivity(intent);





//                        final Intent intent = new Intent(mContext, MyCustomDialog.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("contact_name",savedNumber);
//                        intent.putExtra("phone_no",response.body().response.get(0).data.get(0).mobile);
//                        intent.putExtra("img",response.body().response.get(0).data.get(0).profile_image);
//                        intent.putExtra("is_spam",response.body().response.get(0).data.get(0).is_spam);
//                        intent.putExtra("spam_count",response.body().response.get(0).data.get(0).spanCount);
//                        intent.putExtra("type","Ringing");
//                        intent.putExtra("tag","tag");
//                        Log.d("WalletlsjWalletsavedNumber", "image = = " + response.body().response.get(0).data.get(0).profile_image);
//                        mContext.startActivity(intent);
                    }



                } else {
                    switch (response.code()) {
                        case 401:
//                            Toast.makeText(Splash.this, "email and pass not check", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
//                            Toast.makeText(Splash.this, "ForbiddenException", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
//                            Toast.makeText(Splash.this, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
//                            Toast.makeText(Splash.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
//                            Toast.makeText(Splash.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    Toast.makeText(mContext, "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<VerifyUserResponseJson> call, Throwable t) {
                t.printStackTrace();

                Log.e("System error:", t.getLocalizedMessage());

            }
        });
    }




//    private void SendDataToServer(final String datasss,final Context context) {
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//                String QuickFirstName = datasss;
//                JSONObject jsonObjectlucky = new JSONObject();
//                try {
//                    jsonObjectlucky.put("mobile", QuickFirstName);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
////                String data = String.valueOf(Html.fromHtml(listlistfinal.toString() , Html.FROM_HTML_MODE_LEGACY));
////                QuickFirstName = Character.toString ((char) QuickFirstName);
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("mobile", QuickFirstName));
//
//
//
//                Log.d("CALL_STATE_RINGING", "fname = = " + nameValuePairs);
//
//
////                JSONObject jsonObjectlucky = new JSONObject();
//                try {
//                    StringEntity se;
//                    se = new StringEntity(jsonObjectlucky.toString());
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "verify_caller/");
//
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//                    resTxt = EntityUtils.toString(entity);
//
//                } catch (ClientProtocolException e) {
//
//                } catch (IOException e) {
//
//                }
//                return resTxt;
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//               final String res_result;
//                Log.d("CALL_STATE_RINGING", "result = = " + result);
////                if(result.isEmpty()){
////
////                }else {
//                    Log.d("WalletlsjWalletsavedNumber", "result 1 = = " + result);
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONArray jsonArray = jsonObject.getJSONArray("response");
//                        if (jsonArray.length() == 1) {
//                            res_result = jsonArray.getJSONObject(0).getString("result");
//                            Log.d("WalletlsjWalletsavedNumber", "res_result = = " + res_result);
//                        } else {
//                            res_result = jsonArray.getJSONObject(1).getString("result");
//                        }
//                        Log.d("WalletlsjWalletsavedNumber", "result = = " + jsonArray.getJSONObject(0).getString("result"));
//
////                        jsonArray = jsonObject1.getJSONArray("response");
//
//                        if (res_result.equalsIgnoreCase("0")) {
//                            Log.d("WalletlsjWalletsavedNumber", "name = = " + jsonArray.getJSONObject(0).getString("name"));
//                            Log.d("WalletlsjWalletsavedNumber", "phone_no = = " + jsonArray.getJSONObject(0).getString("name"));
//                            Log.d("WalletlsjWalletsavedNumber", "profile_image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
//                            final Intent intent = new Intent(context, StartingPopupActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            intent.putExtra("contact_name",savedNumber);
//                            intent.putExtra("phone_no",jsonArray.getJSONObject(0).getString("name"));
//                            intent.putExtra("img",jsonArray.getJSONObject(0).getString("profile_image"));
//                            intent.putExtra("is_spam",jsonArray.getJSONObject(0).getString("is_spam"));
//                            intent.putExtra("spam_count",jsonArray.getJSONObject(0).getString("spamCount"));
//                            intent.putExtra("type","Ringing");
//                            intent.putExtra("tag","tag");
//                            Log.d("WalletlsjWalletsavedNumber", "image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
//                            context.startActivity(intent);
//                        }
////
//                    } catch (JSONException e) {
//                        e.getMessage();
//                    }
////                }
//
//
//            }
//        }
//        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
//        sendPostReqAsyncTask.execute(datasss);
//    }


//    private void SendDataToServer_new(final String datasss,final Context context) {
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//                String QuickFirstName = datasss;
//                JSONObject jsonObjectlucky = new JSONObject();
//                try {
//                    jsonObjectlucky.put("mobile", QuickFirstName);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
////                String data = String.valueOf(Html.fromHtml(listlistfinal.toString() , Html.FROM_HTML_MODE_LEGACY));
////                QuickFirstName = Character.toString ((char) QuickFirstName);
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("mobile", QuickFirstName));
//
//
//
//                Log.d("CALL_STATE_RINGING", "fname = = " + nameValuePairs);
//
//
////                JSONObject jsonObjectlucky = new JSONObject();
//                try {
//                    StringEntity se;
//                    se = new StringEntity(jsonObjectlucky.toString());
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "verify_caller/");
//
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//                    resTxt = EntityUtils.toString(entity);
//
//                } catch (ClientProtocolException e) {
//
//                } catch (IOException e) {
//
//                }
//                return resTxt;
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//                final String res_result;
//                Log.d("CALL_STATE_RINGING", "result = = " + result);
////                if(result.isEmpty()){
////
////                }else {
//                Log.d("WalletlsjWalletsavedNumber", "result 1 = = " + result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONArray jsonArray = jsonObject.getJSONArray("response");
//                    if (jsonArray.length() == 1) {
//                        res_result = jsonArray.getJSONObject(0).getString("result");
//                        Log.d("WalletlsjWalletsavedNumber", "res_result = = " + res_result);
//                    } else {
//                        res_result = jsonArray.getJSONObject(1).getString("result");
//                    }
//                    Log.d("WalletlsjWalletsavedNumber", "result = = " + jsonArray.getJSONObject(0).getString("result"));
//
////                        jsonArray = jsonObject1.getJSONArray("response");
//
//                    if (res_result.equalsIgnoreCase("0")) {
//                        Log.d("WalletlsjWalletsavedNumber", "name = = " + jsonArray.getJSONObject(0).getString("name"));
//                        Log.d("WalletlsjWalletsavedNumber", "phone_no = = " + jsonArray.getJSONObject(0).getString("name"));
//                        Log.d("WalletlsjWalletsavedNumber", "profile_image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
//                        final Intent intent = new Intent(context, MyCustomDialog.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("contact_name",savedNumber);
//                        intent.putExtra("phone_no",jsonArray.getJSONObject(0).getString("name"));
//                        intent.putExtra("img",jsonArray.getJSONObject(0).getString("profile_image"));
//                        intent.putExtra("is_spam",jsonArray.getJSONObject(0).getString("is_spam"));
//                        intent.putExtra("spam_count",jsonArray.getJSONObject(0).getString("spamCount"));
//                        intent.putExtra("type","Ringing");
//                        intent.putExtra("tag","tag");
//                        Log.d("WalletlsjWalletsavedNumber", "image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
//                        context.startActivity(intent);
//                    }
////
//                } catch (JSONException e) {
//                    e.getMessage();
//                }
////                }
//
//
//            }
//        }
//        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
//        sendPostReqAsyncTask.execute(datasss);
//    }


//    private void SendServer(final String datasss,final Context context) {
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//                String QuickFirstName = datasss;
//                JSONObject jsonObjectlucky = new JSONObject();
//                try {
//                    jsonObjectlucky.put("mobile", QuickFirstName);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
////                String data = String.valueOf(Html.fromHtml(listlistfinal.toString() , Html.FROM_HTML_MODE_LEGACY));
////                QuickFirstName = Character.toString ((char) QuickFirstName);
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("receiver", QuickFirstName));
//                nameValuePairs.add(new BasicNameValuePair("sender", sharedPreferencesnew.getString("mobile","")));
//
//
//
//                Log.d("WalletlsjWaRishiNumber", "fname = = " + nameValuePairs);
//
//
////                JSONObject jsonObjectlucky = new JSONObject();
//                try {
//                    StringEntity se;
//                    se = new StringEntity(jsonObjectlucky.toString());
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "calling/");
//
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//                    resTxt = EntityUtils.toString(entity);
//
//                } catch (ClientProtocolException e) {
//
//                } catch (IOException e) {
//
//                }
//                return resTxt;
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//                final String res_result;
//                Log.d("WalletlsjWalletsavedNumber", "result = = " + result);
////                if(result.isEmpty()){
////
////                }else {
//                Log.d("WalletlsjWalletsavedNumber", "result 1 = = " + result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONArray jsonArray = jsonObject.getJSONArray("response");
//                    if (jsonArray.length() == 1) {
//                        res_result = jsonArray.getJSONObject(0).getString("result");
//                        Log.d("WalletlsjWalletsavedNumber", "res_result = = " + res_result);
//                    } else {
//                        res_result = jsonArray.getJSONObject(1).getString("result");
//                    }
//                    Log.d("WalletlsjWalletsavedNumber", "result = = " + jsonArray.getJSONObject(0).getString("result"));
//
////                        jsonArray = jsonObject1.getJSONArray("response");
//
//                    if (res_result.equalsIgnoreCase("0")) {
//                        Log.d("WalletlsjWalletsavedNumber", "name = = " + jsonArray.getJSONObject(0).getString("name"));
//                        Log.d("WalletlsjWalletsavedNumber", "phone_no = = " + jsonArray.getJSONObject(0).getString("name"));
//                        Log.d("WalletlsjWalletsavedNumber", "profile_image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
//                        final Intent intent = new Intent(context, MyCustomDialog.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("contact_name",savedNumber);
//                        intent.putExtra("phone_no",jsonArray.getJSONObject(0).getString("name"));
//                        intent.putExtra("img",jsonArray.getJSONObject(0).getString("profile_image"));
//                        intent.putExtra("is_spam",jsonArray.getJSONObject(0).getString("is_spam"));
//                        intent.putExtra("spam_count",jsonArray.getJSONObject(0).getString("spamCount"));
//                        intent.putExtra("type","Ringing");
//                        intent.putExtra("tag","tag");
//                        Log.d("WalletlsjWalletsavedNumber", "image = = " + jsonArray.getJSONObject(0).getString("profile_image"));
//                        context.startActivity(intent);
//                    }
////
//                } catch (JSONException e) {
//                    e.getMessage();
//                }
////                }
//
//
//            }
//        }
//        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
//        sendPostReqAsyncTask.execute(datasss);
//    }
}
