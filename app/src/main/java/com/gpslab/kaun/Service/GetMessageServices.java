package com.gpslab.kaun.Service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gpslab.kaun.CallLogUtils;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.Home.GetCallLogTable;
import com.gpslab.kaun.model.GetImpMessage;
import com.gpslab.kaun.model.GetMessageTable;

import java.util.ArrayList;

public class GetMessageServices extends Service {
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

        myDbHandler = Temp.getMyDbHandler();
        Log.d("walletwallet", "update = ");
        firstinsertdata();
        return START_STICKY;
    }




    private void firstqueryall() {

        ArrayList<GetMessageTable> arrayList = myDbHandler.viewMessageNew();
        long countnew = myDbHandler.getMessageCount();
        Log.d("walletwallet", "size = " + arrayList.size() + "   Size ====  " + countnew);


        //// check row on the table is null or not
        if (arrayList.size() != 0) {
            Log.d("walletwallet", "update = ");

//            arrayList.get(0).getName();
//            boolean count = false;
//            count = myDbHandler.checkrow("+919651667458");
//            Log.d("walletwallet", "count check = " + arrayList.get(0).getName());
//            if (count) {
//                Log.d("walletwallet", " 2 UPdate Query........");
//                //  updatedb();
//            } else {
//                Log.d("walletwallet", "2 Insert Query........");
//                //  firstinsertdata();
//            }
//            Log.d("walletwallet", "count = " + count);

        } else {
            Log.d("walletwallet", "Insert Query........");
            firstinsertdata();
        }
    }
    String insertData = "";
    String insertSecData = "";
    public void firstinsertdata() {
        Uri inboxURI = Uri.parse("content://sms/inbox");
        ContentResolver cr = (ContentResolver) getContentResolver();
        Cursor cursor = cr.query(inboxURI, null, null, null, null);
        int totalSMS = cursor.getCount();
        if (cursor.moveToFirst()) {
//            String msgData = "";
//            edit.putString("total_msg", String.valueOf(totalSMS));
//            edit.apply();
            Log.d("fragmeentone", "totalSMS = = " + totalSMS);
            for (int idx = 0; idx < totalSMS; idx++) {


                try {


                    String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    insertData = insertData + "'" + id + "'";
                    String Read = cursor.getString(cursor.getColumnIndexOrThrow("read"));
                    insertData = insertData + "," + "'" + Read + "'";
                    String Address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    if (Address.replace(" ", "").length() == 10) {


                        byte[] dataaddresseone = Address.getBytes("UTF-8");
                        String baseaddresssone = Base64.encodeToString(dataaddresseone, Base64.DEFAULT);


                        insertData = insertData + "," + "'" + baseaddresssone + "'";
                    }else if(Address.replace(" ", "").length() < 10){


                        byte[] dataaddresseone = Address.getBytes("UTF-8");
                        String baseaddresssone = Base64.encodeToString(dataaddresseone, Base64.DEFAULT);

                        insertData = insertData + "," + "'" + baseaddresssone + "'";
                    }else {

                        Address=Address.substring(Address.length() - 10);

                        byte[] dataaddresseone = Address.getBytes("UTF-8");
                        String baseaddresssone = Base64.encodeToString(dataaddresseone, Base64.DEFAULT);


                        insertData = insertData + "," + "'" + baseaddresssone + "'";
                    }

                    String Bodydd = cursor.getString(cursor.getColumnIndexOrThrow("body")).replace("\n", "");
                    Log.d("ContactDetailsActivityNew", "List 2 Address = " + Address);
                    byte[] datamobileone = Bodydd.getBytes("UTF-8");
                    String base64mobileone = Base64.encodeToString(datamobileone, Base64.DEFAULT);
                    insertData = insertData + "," + "'" + base64mobileone + "'";
                    String Dates = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                    insertData = insertData + "," + "'" + Dates + "'";
                    insertData = insertData + "," + "'" + "NA" + "'";
                    insertData = insertData + "," + "'" + "NA" + "'";
                    insertSecData = insertSecData + "(" + insertData + ")" + ",";
                    insertData = "";
                    cursor.moveToNext();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        } else {

        }


        cursor.close();

        insertSecData = insertSecData.substring(0, insertSecData.length() - 1);
        Log.d("dfMainHomeActivityhsdfsdfdfsd", "MessageService inseart check = " + insertSecData);
        myDbHandler.InsertMessageSQLiteDatabase(insertSecData);

    }
}
