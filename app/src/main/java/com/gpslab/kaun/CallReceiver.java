package com.gpslab.kaun;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.gpslab.kaun.Webapi.createnew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class CallReceiver extends PhoneCallReceiver
{

    Context context;



    @Override
    protected void onIncomingCallStarted(final Context ctx, String pnumber, Date start)
    {
        Log.i("YUYULuckyYUYTUNowkyY","Call Receiver pnumber 786786 = "+pnumber+" start = "+start);
        Toast.makeText(ctx,"Jeet Incoming Call from "+ pnumber,Toast.LENGTH_SHORT).show();

        context =   ctx;




    }


    @Override
    protected void onDeaildCallEnded(Context ctx, String pnumber){
        Log.i("YUYULuckyYUYTUNowkyY","datanew =onDeaildCallEnded ");
//
        sharedPreferences = ctx.getSharedPreferences("data", Context.MODE_PRIVATE);
        String YourNo = sharedPreferences.getString("mobile","");
        createnew addpopup = new createnew(context);

        String datanew = YourNo+","+pnumber.substring(pnumber.length() - 10)+","+"calling";
        Log.d("YUYULuckyYUYTUNow","datanew = "+datanew);
        addpopup.sendmessage(String.valueOf(datanew));
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String pnumber, Date start, Date end)
    {
        Log.i("YUYULuckyYUYTUNowkyY","Call Receiver 23 = "+pnumber);
        Toast.makeText(ctx,"Call dropped"+ pnumber,Toast.LENGTH_SHORT).show();
    }
}
