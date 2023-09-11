package com.gpslab.kaun.view;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.gpslab.kaun.BuildConfig;
import com.gpslab.kaun.fullscreen.MimeTypes;

import java.io.File;
import java.util.ArrayList;

import io.realm.RealmList;

public class IntentUtils {
    public static final String EXTRA_TEXT_STATUS = "extra-text-status";
    public static final String EXTRA_PATH = "path";
    public static final String EXTRA_PATH_RESULT = "path_result";
    public static final String EXTRA_MESSAGE_ID = "messageId";
    public static final String EXTRA_REAL_PATH = "real-path";
    public static final String INTENT_ACTION_HANDLE_REPLY = "intent-action-handle-reply";
    public static final String ISVIDEO = "is_video";
    public static final String CALL_TYPE = "call-type";
    public static final String INTENT_ACTION_MARK_AS_READ = "intent-action-mark-as-read";
    public static final String IS_GROUP = "isGroup";
    public static final String CALL_DIRECTION = "call-direction";
    public static final String CALL_ACTION_TYPE = "call-action-type";
    public static int ACTION_START_NEW_CALL = 0;
    public static int NOTIFICATION_ACTION_NONE = -1;
    public static int NOTIFICATION_ACTION_ANSWER = 1;
    public static int NOTIFICATION_ACTION_DECLINE = 2;
    public static int NOTIFICATION_ACTION_HANGUP = 3;
    public static int NOTIFICATION_ACTION_CLICK = 4;
    public static int NOTIFICATION_ACTION_START_INCOMING = 5;
    public static final String PHONE = "phone";
    public static final String ACTION_FINISH_CALLING_ACTIVITY = "finish_calling_activity";
    public static final String EXTRA_MIME_TYPE = "mime_type";
    public static final String EXTRA_REAL_PATH_LIST = "real-path-list";
    public static final String EXTRA_SHARED_TEXT = "shared_text";
    public static final String EXTRA_DATA_RESULT = "data";
    public static final String EXTRA_CONTACT_LIST = "contactList";
    public static final String CAMERA_VIEW_SHOW_PICK_IMAGE_BUTTON = "camera-view-show-pick-image";
    public static final String IS_STATUS = "isStatus";
    public static final String UID = "uid";
    public static final String EXTRA_STARTING_POSITION = "extra_starting_item_position";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_FORWARDED = "forwarded";
    public static final String EXTRA_CURRENT_ALBUM_POSITION = "extra_current_item_position";
    public static final String EXTRA_CURRENT_MESSAGE_ID = "current_message_id";
    public static final String EXTRA_STATUS_ID = "extra-status-id";
    public static final String CALL_ID = "call-id";
    public static final String EXTRA_CHAT_ID = "extra-chat-id";
    public static final String EXTRA_FIRST_VISIBLE_ITEM_POSITION = "extra_first_visible_item_position";
    public static final String EXTRA_LAST_VISIBLE_ITEM_POSITION = "extra_last_visible_item_position";
    //add contact to the device using realm contact

    //used to open a maps app with the given location
    public static Intent getOpenMapIntent(RealmLocation location) {
        double latitude = location.getLat();
        double longitude = location.getLng();
        String label = location.getName();
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=17";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        return intent;
    }

    public static Intent getShareImageIntent(String filePath) {

        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);

        Uri uri = Uri.fromFile(new File(filePath));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        if (Build.VERSION.SDK_INT >= 24) {
            //this solves android.os.FileUriExposedException
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        shareIntent.setType(MimeTypes.getMimeType(filePath));

        return Intent.createChooser(shareIntent, "Share Using");

    }
    public static Intent getAddContactIntent(RealmContact contact) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        RealmList<PhoneNumber> numbersList = contact.getRealmList();
        ArrayList<ContentValues> data = new ArrayList<ContentValues>();

        //add phone numbers to ContentValue
        for (int i = 0; i < numbersList.size(); i++) {
            ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, numbersList.get(i).getNumber());
            row.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            data.add(row);
        }

        //add contact name
        intent.putExtra(ContactsContract.Intents.Insert.NAME, contact.getName());
        //set the contact numbers
        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data);

        return intent;
    }
    //used to open the file by system
    public static Intent getOpenFileIntent(Context context, String path) {

        String fileExtension = Util.getFileExtensionFromPath(path);
        File toInstall = new File(path);


        //if it's apk make the system open apk installer
        if (fileExtension.equalsIgnoreCase("apk")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", toInstall);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                return intent;
            } else {
                Uri apkUri = Uri.fromFile(toInstall);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                return intent;
            }


            //else make the system open an app that can handle given type
        } else {
            String mimeType = getMimeType(toInstall);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uriForFile = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", toInstall);
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                newIntent.setDataAndType(uriForFile, mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                return newIntent;
            } else {
                Uri uriForFile = Uri.fromFile(toInstall);
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                newIntent.setDataAndType(uriForFile, mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                return newIntent;
            }
        }
    }

    private static String getMimeType(File file) {
        String type = "application/*";
        try {


            String extension = MimeTypeMap.getFileExtensionFromUrl(file.getPath());
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
        } catch (Exception e) {
        }

        return type;
    }
}
