package com.gpslab.kaun.view;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.Date;

import io.realm.Realm;

public class RealmBackupRestore {
    private static File EXPORT_REALM_PATH = DirManager.getDatabasesFolder();
    private static String EXPORT_REALM_FILE_NAME = "messages.fbup";
    private static String IMPORT_REALM_FILE_NAME = "temp.realm";

    private final static String TAG = RealmBackupRestore.class.getName();

    private Activity activity;
    private Realm realm;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;


    public RealmBackupRestore(Activity activity) {
        this.realm = Realm.getDefaultInstance();
        this.activity = activity;
    }
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private void checkStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void backup() throws io.realm.internal.IOException {
        // First check if we have storage permissions
        if (activity != null)
            checkStoragePermissions(activity);

        File exportRealmFile;


        // create a backup file
        exportRealmFile = new File(EXPORT_REALM_PATH, EXPORT_REALM_FILE_NAME);

        // if backup file already exists, delete it
        exportRealmFile.delete();

        // copy current realm to backup file
        realm.writeCopyTo(exportRealmFile);


        realm.close();
        SharedPreferencesManager.setLastBackup(new Date().getTime());

    }
}

