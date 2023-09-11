package com.gpslab.kaun.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gpslab.kaun.CallLogUtils;
import com.gpslab.kaun.CallReceiver;
import com.gpslab.kaun.DB.FirstTableData;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.Home.GetCallLogTable;
import com.gpslab.kaun.MyBrodcastRecieverService;
import com.gpslab.kaun.model.ContactFetcher;

import java.util.ArrayList;
import java.util.Timer;

public class CallLogService extends Service {
    public Context context;

    public String numbers;
    public GetCallLogTable user;
    public MyDbHandler myDbHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        context = this;
    }

    @Override
    public void onDestroy() {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        myDbHandler = Temp.getMyDbHandler();
        firstqueryall();
        return START_STICKY;
    }


    private void firstqueryall() {

        ArrayList<GetCallLogTable> arrayList = myDbHandler.viewCallLogsFirstRow();
        long countnew = myDbHandler.getCallLogCount();
        Log.d("walletwallet", "size = " + arrayList.size() + "   Size ====  " + countnew);


        //// check row on the table is null or not
        if (arrayList.size() != 0) {
            Log.d("walletwallet", "update = ");

            arrayList.get(0).getName();
            boolean count = false;
            count = myDbHandler.checkrow("+919651667458");
            Log.d("walletwallet", "count check = " + arrayList.get(0).getName());
            if (count) {
                Log.d("walletwallet", " 2 UPdate Query........");
                //  updatedb();
            } else {
                Log.d("walletwallet", "2 Insert Query........");
                //  firstinsertdata();
            }
//            Log.d("walletwallet", "count = " + count);

        } else {
            Log.d("walletwallet", "Insert Query........");
            firstinsertdata();
        }
    }
    String insertData = "";
    String insertSecData = "";
    public void firstinsertdata() {

        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        for (int i = 0; i < callLogUtils.readCallLogs().size(); i++) {
//            user = new GetCallLogTable();
            if (callLogUtils.readCallLogs().get(i).getNumber().length() > 10) {
                numbers = callLogUtils.readCallLogs().get(i).getNumber();
                numbers = numbers.substring(numbers.length() - 10).toString();
            } else {
                numbers = callLogUtils.readCallLogs().get(i).getNumber();
            }




            if (TextUtils.isEmpty(callLogUtils.readCallLogs().get(i).getName())) {

                insertData = insertData + "'" + "NA" + "'";

//                user.setName("NA");
            } else {
//                user.setName(callLogUtils.readCallLogs().get(i).getName());
                String Name = callLogUtils.readCallLogs().get(i).getName();

                insertData = insertData + "'" + Name + "'";

            }
            insertData = insertData + "," + "'" + String.valueOf(i) + "'";
//            user.setNop(numbers);
            insertData = insertData + "," + "'" + numbers + "'";



            if (callLogUtils.readCallLogs().get(i).getCallType().equalsIgnoreCase("null")) {
                user.setCall_type("NA");

                insertData = insertData + "," + "'" + "NA" + "'";
            } else {
//                user.setCall_type(callLogUtils.readCallLogs().get(i).getCallType());

                insertData = insertData + "," + "'" + callLogUtils.readCallLogs().get(i).getCallType() + "'";
            }


//            user.setSim_type("NA");
            insertData = insertData + "," + "'" + "NA" + "'";

//            user.setDate(String.valueOf(callLogUtils.readCallLogs().get(i).getDate()));
            insertData = insertData + "," + "'" + String.valueOf(callLogUtils.readCallLogs().get(i).getDate()) + "'";
//            user.setImage("NA");
            insertData = insertData + "," + "'" + "NA" + "'";
//            user.setDuration(String.valueOf(callLogUtils.readCallLogs().get(i).getDuration()));
            insertData = insertData + "," + "'" + String.valueOf(callLogUtils.readCallLogs().get(i).getDuration()) + "'";
            insertSecData = insertSecData + "(" + insertData + ")" + ",";
            insertData = "";

//            Log.d("walletwallet", "inseart check date = " + callLogUtils.readCallLogs().get(i).getCallType()+" number = " + callLogUtils.readCallLogs().get(i).getNumber());
        }
        insertSecData = insertSecData.substring(0, insertSecData.length() - 1);
        Log.d("dfMainHomeActivityhsdfsdfdfsd", "CallLogService inseart check = " + insertSecData);
       myDbHandler.InsertLogCallerDataIntoSQLiteDatabase(insertSecData);

    }
}
