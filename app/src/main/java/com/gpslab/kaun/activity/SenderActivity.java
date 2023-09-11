package com.gpslab.kaun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.ClipboardManager;

import com.gpslab.kaun.R;

public class SenderActivity extends AppCompatActivity {
    /**
     * Tag for output.
     */
    private static final String TAG = "send";

    /**
     * {@link Uri} for saving messages.
     */
    private static final Uri URI_SMS = Uri.parse("content://sms");

    /**
     * {@link Uri} for saving sent messages.
     */
    public static final Uri URI_SENT = Uri.parse("content://sms/sent");

    /**
     * Projection for getting the id.
     */
    private static final String[] PROJECTION_ID = new String[]{BaseColumns._ID};

    /**
     * SMS DB: address.
     */
    private static final String ADDRESS = "address";

    /**
     * SMS DB: read.
     */
    private static final String READ = "read";

    /**
     * SMS DB: type.
     */
    public static final String TYPE = "type";

    /**
     * SMS DB: body.
     */
    private static final String BODY = "body";

    /**
     * SMS DB: date.
     */
    private static final String DATE = "date";

    /**
     * Message set action.
     */
    public static final String MESSAGE_SENT_ACTION = "com.android.mms.transaction.MESSAGE_SENT";

    /**
     * Hold recipient and text.
     */
    private String to, text;

    /**
     * {@link ClipboardManager}.
     */
    @SuppressWarnings("deprecation")
    private ClipboardManager cbmgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);
    }


}