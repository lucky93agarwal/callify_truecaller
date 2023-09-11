package com.gpslab.kaun.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.HashSet;
import java.util.Set;

public class PermissionsUtil {


    private static final String LOG_TAG = PermissionsUtil.class.getSimpleName();

    public static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE};

    private final Context context;

    public PermissionsUtil(final Context context) {
        this.context = context;
    }

    public boolean checkPermissions() {
        Log.d(LOG_TAG, "Checking permissions");

        final Set<String> missingPermissions = new HashSet<>();
        for (final String permission : PermissionsUtil.REQUIRED_PERMISSIONS) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        return missingPermissions.isEmpty();
    }

    //permissions we need
    public static final String[] permissions = new String[]{
            Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
            , Manifest.permission.RECORD_AUDIO};


    public static final String[] videoCallPermissions = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};


    //check if user granted all permissions
    public static boolean permissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public static boolean hasVideoCallPermissions(Context context) {
        if (context != null && permissions != null) {
            for (String permission : videoCallPermissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean hasVoiceCallPermissions(Context context) {
        if (context != null && permissions != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                return false;

            }
        }
        return true;
    }

    //check if the permissions granted or not (without request permissions from user)
    public static boolean hasPermissions(Context context) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean hasLocationPermissions(Context context) {
        if (context != null && permissions != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}


