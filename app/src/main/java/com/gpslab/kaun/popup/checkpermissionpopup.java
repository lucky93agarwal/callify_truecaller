package com.gpslab.kaun.popup;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.R;

import java.util.ArrayList;
import java.util.List;

public class checkpermissionpopup {
    public Dialog epicDialog;
    private final Context _context;

    public Button btnonebtn;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    String[] appPermissions = {Manifest.permission.INTERNET,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.MODIFY_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SYSTEM_ALERT_WINDOW};
    private static final int PERMISSION_REQUEST_CODE = 1240;

    public checkpermissionpopup(Context context) {
        this._context = context;

        epicDialog = new Dialog(context, R.style.PauseDialog);


    }
    public void profileaddpopup() {
        epicDialog.setContentView(R.layout.checkpermissionlayout);
//        epicDialog.setCancelable(false);

        sharedPreferences = _context.getSharedPreferences("data",Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();

        btnonebtn = epicDialog.findViewById(R.id.usesystembtn);

        btnonebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (checkAndRequestPermissions()) {
//                    // All permission are granted already. Proceed ahead
//                }else {
                if (!Settings.canDrawOverlays(_context)) {
                    ((MainHomeActivity)_context).checkpermissdfsdfssion();
                }
                    edit.putBoolean("defaultnewnew",true);
                    edit.apply();
                    epicDialog.dismiss();
//                }

            }
        });


        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // popup background transparent
        epicDialog.show();
    }
    private boolean checkAndRequestPermissions() {

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(_context, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);

            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) _context,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);

            return false;
        }

        return true;
    }

}
