package com.gpslab.kaun.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gpslab.kaun.R;

import java.util.List;

public class simselectpopup {
    public Dialog epicDialog;
    int simnumber =0;
    private final Context _context;
    public TextView tvphone, tvsimone, tvsimtwo;
    public LinearLayout lineone, linetwo;

    private final static String simSlotName[] = {
            "extra_asus_dial_use_dualsim",
            "com.android.phone.extra.slot",
            "slot",
            "simslot",
            "sim_slot",
            "subscription",
            "Subscription",
            "phone",
            "com.android.phone.DialingMode",
            "simSlot",
            "slot_id",
            "simId",
            "simnum",
            "phone_type",
            "slotId",
            "slotIdx"
    };


    public simselectpopup(Context context) {
        this._context = context;

        epicDialog = new Dialog(context, R.style.PauseDialog);


    }

    public void defaultaddpopup(String number) {

        epicDialog.setContentView(R.layout.layoutpopupnew);
        tvphone = (TextView)epicDialog.findViewById(R.id.numbertv);
        tvphone.setText("Call "+number+" from");
        tvsimone = (TextView)epicDialog.findViewById(R.id.simonetv);
        tvsimtwo = (TextView)epicDialog.findViewById(R.id.simtwotv);


        lineone = (LinearLayout)epicDialog.findViewById(R.id.llsimonetv);
        linetwo = (LinearLayout)epicDialog.findViewById(R.id.llsimtwotv);


        tvsimone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simnumber = 0;
                TelecomManager telecomManager = (TelecomManager) _context.getSystemService(Context.TELECOM_SERVICE);
                List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

                Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:" + number));
                intent.putExtra("com.android.phone.force.slot", true);
                intent.putExtra("Cdma_Supp", true);

                if (simnumber== 0) {   //0 for sim1
                    for (String s : simSlotName)
                        intent.putExtra(s, 0); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 0)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(0));

                } else {
                    for (String s : simSlotName)
                        intent.putExtra(s, 1); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 1)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(1));

                }
                _context.startActivity(intent);
                epicDialog.dismiss();
            }
        });
        tvsimtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simnumber = 1;

                TelecomManager telecomManager = (TelecomManager) _context.getSystemService(Context.TELECOM_SERVICE);
                List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

                Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:" + number));
                intent.putExtra("com.android.phone.force.slot", true);
                intent.putExtra("Cdma_Supp", true);
                if (simnumber== 0) {   //0 for sim1
                    for (String s : simSlotName)
                        intent.putExtra(s, 0); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 0)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(0));

                } else {
                    for (String s : simSlotName)
                        intent.putExtra(s, 1); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 1)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(1));

                }
                _context.startActivity(intent);
                epicDialog.dismiss();
            }
        });



        lineone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simnumber = 0;
                TelecomManager telecomManager = (TelecomManager) _context.getSystemService(Context.TELECOM_SERVICE);
                List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

                Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:" + number));
                intent.putExtra("com.android.phone.force.slot", true);
                intent.putExtra("Cdma_Supp", true);
                if (simnumber== 0) {   //0 for sim1
                    for (String s : simSlotName)
                        intent.putExtra(s, 0); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 0)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(0));

                } else {
                    for (String s : simSlotName)
                        intent.putExtra(s, 1); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 1)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(1));

                }
                _context.startActivity(intent);
                epicDialog.dismiss();
            }
        });
        linetwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simnumber = 1;
                TelecomManager telecomManager = (TelecomManager) _context.getSystemService(Context.TELECOM_SERVICE);
                List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

                Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:" + number));
                intent.putExtra("com.android.phone.force.slot", true);
                intent.putExtra("Cdma_Supp", true);
                if (simnumber== 0) {   //0 for sim1
                    for (String s : simSlotName)
                        intent.putExtra(s, 0); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 0)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(0));

                } else {
                    for (String s : simSlotName)
                        intent.putExtra(s, 1); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 1)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(1));

                }
                _context.startActivity(intent);
                epicDialog.dismiss();
            }
        });



        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // popup background transparent
        epicDialog.show();
    }
}
