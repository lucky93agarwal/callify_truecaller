package com.gpslab.kaun.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncomingSms extends BroadcastReceiver {
    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");


        for(int i = 0;i < pdus.length; i++)
        {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            //String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.

            String messageBody = smsMessage.getMessageBody();
            Log.d("WalletLuckyWallet","MSG ==    "+messageBody);

            //Pass on the text to our listener.
            if(mListener != null)
            {
                // mListener.messageReceived(messageBody);
                mListener.messageReceived(parseCode(messageBody));
            }
        }

    }

    private String parseCode(String message)
    {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find())
        {
            code = m.group(0);
        }
        return code;
    }

    public static void bindListener(SmsListener listener)
    {
        mListener = listener;
    }
}
