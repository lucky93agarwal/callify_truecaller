package com.gpslab.kaun.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gpslab.kaun.BuildConfig;
import com.gpslab.kaun.DB.SpamDB;
import com.gpslab.kaun.R;
import com.gpslab.kaun.Receiver.SmsReceiver;
import com.gpslab.kaun.adapter.ConversationAdapter;
import com.gpslab.kaun.model.AsyncHelper;
import com.gpslab.kaun.model.ConsentManager;
import com.gpslab.kaun.model.Conversation;
import com.gpslab.kaun.model.SMSdroid;
import com.gpslab.kaun.model.Message;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

import de.ub0r.android.lib.Utils;
import de.ub0r.android.lib.apis.Contact;
import de.ub0r.android.lib.apis.ContactsWrapper;
import de.ub0r.android.logg0r.Log;

public class ConversationListActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    /**
     * Tag for output.
     */
    public static final String TAG = "main";

    /**
     * ORIG_URI to resolve.
     */
    public static final Uri URI = Uri.parse("content://mms-sms/conversations/");

    /**
     * Number of items.
     */
    private static final int WHICH_N = 6;

    /**
     * Index in dialog: answer.
     */
    private static final int WHICH_ANSWER = 0;

    /**
     * Index in dialog: answer.
     */
    private static final int WHICH_CALL = 1;

    /**
     * Index in dialog: view/add contact.
     */
    private static final int WHICH_VIEW_CONTACT = 2;

    /**
     * Index in dialog: view.
     */
    private static final int WHICH_VIEW = 3;

    /**
     * Index in dialog: delete.
     */
    private static final int WHICH_DELETE = 4;

    /**
     * Index in dialog: mark as spam.
     */
    private static final int WHICH_MARK_SPAM = 5;

    /**
     * Minimum date.
     */
    public static final long MIN_DATE = 10000000000L;

    /**
     * Miliseconds per seconds.
     */
    public static final long MILLIS = 1000L;

    /**
     * Show contact's photo.
     */
    public static boolean showContactPhoto = false;


    public static boolean showEmoticons = false;

    /**
     * Dialog items shown if an item was long clicked.
     */
    private String[] longItemClickDialog = null;


    private ConversationAdapter adapter = null;

    /**
     * {@link Calendar} holding today 00:00.
     */
    private static final Calendar CAL_DAYAGO = Calendar.getInstance();

    static {
        // Get time for now - 24 hours
        CAL_DAYAGO.add(Calendar.DAY_OF_MONTH, -1);
    }

    private static final int PERMISSIONS_REQUEST_READ_SMS = 1;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 2;




    @Override
    public void onStart() {
        super.onStart();
        AsyncHelper.setAdapter(adapter);
    }
    @Override
    public void onStop() {
        super.onStop();
        AsyncHelper.setAdapter(null);
    }
    private AbsListView getListView() {
        return (AbsListView) findViewById(android.R.id.list);
    }
    private void setListAdapter(final ListAdapter la) {
        AbsListView v = getListView();
        if (v instanceof GridView) {
            //noinspection RedundantCast
            ((GridView) v).setAdapter(la);
        } else if (v instanceof ListView) {
            //noinspection RedundantCast
            ((ListView) v).setAdapter(la);
        }

    }

    public static void showRows(final Context context, final Uri u) {
        Log.d(TAG, "-----GET HEADERS-----");
        Log.d(TAG, "-- ", u.toString(), " --");
        Cursor c = context.getContentResolver().query(u, null, null, null, null);
        if (c != null) {
            int l = c.getColumnCount();
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < l; i++) {
                buf.append(i).append(":");
                buf.append(c.getColumnName(i));
                buf.append(" | ");
            }
            Log.d(TAG, buf.toString());
        }

    }

    public static void showRows(@SuppressWarnings("UnusedParameters") final Context context) {
        // showRows(ContactsWrapper.getInstance().getUriFilter());
        // showRows(context, URI);
        // showRows(context, Uri.parse("content://sms/"));
        // showRows(context, Uri.parse("content://mms/"));
        // showRows(context, Uri.parse("content://mms/part/"));
        // showRows(context, ConversationProvider.CONTENT_URI);
        // showRows(context, Uri.parse("content://mms-sms/threads"));
        // showRows(Uri.parse(MessageList.URI));
    }
    @Override
    public void onNewIntent(final Intent intent) {
        if (intent != null) {
            Log.d(TAG, "got intent: ", intent.getAction());
            Log.d(TAG, "got uri: ", intent.getData());
            final Bundle b = intent.getExtras();
            if (b != null) {
                Log.d(TAG, "user_query: ", b.get("user_query"));
                Log.d(TAG, "got extra: ", b);
            }
            final String query = intent.getStringExtra("user_query");
            Log.d(TAG, "user query: ", query);
            // TODO: do something with search query
        }
    }
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        final Intent i = getIntent();
        Log.d("TAG", "got intent: ", i.getAction());
        Log.d("TAG", "got uri: ", i.getData());
        Log.d("TAG", "got extra: ", i.getExtras());

//        setTheme(PreferencesActivity.getTheme(this));
        Utils.setLocale(this);
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("use_gridlayout", false)) {
            setContentView(R.layout.conversationgrid);
        } else {
            setContentView(R.layout.conversationlist);
        }

        // debug info
        showRows(this);

        final AbsListView list = getListView();
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);
        longItemClickDialog = new String[WHICH_N];
        longItemClickDialog[WHICH_ANSWER] = getString(R.string.reply);
        longItemClickDialog[WHICH_CALL] = getString(R.string.call);
        longItemClickDialog[WHICH_VIEW_CONTACT] = getString(R.string.view_contact_);
        longItemClickDialog[WHICH_VIEW] = getString(R.string.view_thread_);
        longItemClickDialog[WHICH_DELETE] = getString(R.string.delete_thread_);
        longItemClickDialog[WHICH_MARK_SPAM] = getString(R.string.filter_spam_);

//        if (SMSdroid.isDefaultApp(this)) {
            initAdapter();
            getAdsConstent();
//        } else {
//            AlertDialog.Builder b = new AlertDialog.Builder(this);
//            b.setTitle(R.string.not_default_app);
//            b.setMessage(R.string.not_default_app_message);
//            b.setCancelable(false);
//            b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(final DialogInterface dialogInterface, final int i) {
//                    ConversationListActivity.this.finish();
//                }
//            });
//            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                @TargetApi(Build.VERSION_CODES.KITKAT)
//                @Override
//                public void onClick(final DialogInterface dialogInterface, final int i) {
//                    Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
//                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, "com.gpslab.kaun");
//                    startActivity(intent);




//                }
//            });
//            b.show();
//        }
    }

    private void getAdsConstent() {
        new ConsentManager(this).updateConsent();
    }

    private void initAdapter() {
        if (!SMSdroid.requestPermission(this, Manifest.permission.READ_SMS,
                PERMISSIONS_REQUEST_READ_SMS, R.string.permissions_read_sms,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })) {
            return;
        }

        if (!SMSdroid.requestPermission(this, Manifest.permission.READ_CONTACTS,
                PERMISSIONS_REQUEST_READ_CONTACTS, R.string.permissions_read_contacts,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })) {
            return;
        }

        adapter = new ConversationAdapter(this);
        setListAdapter(adapter);
        adapter.startMsgListQuery();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter == null && SMSdroid.isDefaultApp(this)) {
            // after setting SMSdroid to default SMS app
            initAdapter();
        }

        CAL_DAYAGO.setTimeInMillis(System.currentTimeMillis());
        CAL_DAYAGO.add(Calendar.DAY_OF_MONTH, -1);

        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        showContactPhoto = p.getBoolean(PreferencesActivity.PREFS_CONTACT_PHOTO, true);
        showEmoticons = p.getBoolean(PreferencesActivity.PREFS_EMOTICONS, false);
        if (adapter != null) {
            adapter.startMsgListQuery();
        }
    }





    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.conversationlist, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        boolean hideDeleteAll = p
                .getBoolean(PreferencesActivity.PREFS_HIDE_DELETE_ALL_THREADS, false);
        menu.findItem(R.id.item_delete_all_threads).setVisible(!hideDeleteAll);
        return true;
    }



    @Override
    public void onRequestPermissionsResult(
            final int requestCode,
            @NonNull final String permissions[],
            @NonNull final int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // just try again.
                    initAdapter();
                } else {
                    // this app is useless without permission for reading sms
                    Log.e(TAG, "permission for reading sms denied, exit");
                    finish();
                }
                return;
            }
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // just try again.
                    initAdapter();
                } else {
                    // this app is useless without permission for reading sms
                    Log.e(TAG, "permission for reading contacts denied, exit");
                    finish();
                }
                return;
            }
        }
    }
    public static void markRead(final Context context, final Uri uri, final int read) {
        Log.d(TAG, "markRead(", uri, ",", read, ")");
        if (uri == null) {
            return;
        }
        String[] sel = Message.SELECTION_UNREAD;
        if (read == 0) {
            sel = Message.SELECTION_READ;
        }
        final ContentResolver cr = context.getContentResolver();
        final ContentValues cv = new ContentValues();
        cv.put(Message.PROJECTION[Message.INDEX_READ], read);
        try {
            cr.update(uri, cv, Message.SELECTION_READ_UNREAD, sel);
        } catch (IllegalArgumentException | SQLiteException e) {
            Log.e(TAG, "failed update", e);
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //SmsReceiver.updateNewMessageNotification(context, null);
    }
    public static void deleteMessages(final Context context, final Uri uri, final int title,
                                      final int message, final Activity activity) {
        Log.i(TAG, "deleteMessages(..,", uri, " ,..)");
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(android.R.string.no, null);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                try {
                    final int ret = context.getContentResolver().delete(uri, null, null);
                    Log.d(TAG, "deleted: ", ret);
                    if (activity != null && !activity.isFinishing()) {
                        activity.finish();
                    }
                    if (ret > 0) {
                        Conversation.flushCache();
                        Message.flushCache();
        //                SmsReceiver.updateNewMessageNotification(context, null);
                    }
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Argument Error", e);
                    Toast.makeText(context, R.string.error_unknown, Toast.LENGTH_LONG).show();
                } catch (SQLiteException e) {
                    Log.e(TAG, "SQL Error", e);
                    Toast.makeText(context, R.string.error_unknown, Toast.LENGTH_LONG).show();
                }

            }
        });
        builder.show();
    }
    public static void addToOrRemoveFromSpamlist(final Context context, final String addr) {
        SpamDB.toggleBlacklist(context, addr);
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_compose:
                final Intent i = getComposeIntent(this, null, false);
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "error launching intent: ", i.getAction(), ", ", i.getData());
                    Toast.makeText(this,
                            "error launching messaging app!\nPlease contact the developer.",
                            Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.item_settings: // start settings activity
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
            case R.id.item_delete_all_threads:
                deleteMessages(this, Uri.parse("content://sms/"), R.string.delete_threads_,
                        R.string.delete_threads_question, null);
                return true;
            case R.id.item_mark_all_read:
                markRead(this, Uri.parse("content://sms/"), 1);
                markRead(this, Uri.parse("content://mms/"), 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    static Intent getComposeIntent(final Context context, final String address,
                                   final boolean showChooser) {
        Intent i = null;

        if (!showChooser && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Search for WebSMS
            PackageManager pm = context.getPackageManager();
            i = pm == null ? null : pm.getLaunchIntentForPackage("de.ub0r.android.websms");
        }

        if (i == null) {
            Log.d(TAG, "WebSMS is not installed!");
            i = new Intent(Intent.ACTION_SENDTO);
        }

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (address == null) {
            i.setData(Uri.parse("sms:"));
        } else {
            i.setData(Uri.parse("smsto:" + PreferencesActivity.fixNumber(context, address)));
        }

        return i;
    }
    public void onItemClick(final AdapterView<?> parent, final View view, final int position,
                            final long id) {
        final Conversation c = Conversation.getConversation(this,
                (Cursor) parent.getItemAtPosition(position), false);
        final Uri target = c.getUri();
        final Intent i = new Intent(this, MessageListActivity.class);
        i.setData(target);
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "error launching intent: ", i.getAction(), ", ", i.getData());
            Toast.makeText(this,
                    "error launching messaging app!\n" + "Please contact the developer.",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean onItemLongClick(final AdapterView<?> parent, final View view,
                                   final int position, final long id) {
        final Conversation c = Conversation.getConversation(this,
                (Cursor) parent.getItemAtPosition(position), true);
        final Uri target = c.getUri();
        if (ContentUris.parseId(target) < 0) {
            // do not show anything for broken threadIds
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = longItemClickDialog;
        final Contact contact = c.getContact();
        final String a = contact.getNumber();
        Log.d(TAG, "p: ", a);
        final String n = contact.getName();
        if (TextUtils.isEmpty(n)) {
            builder.setTitle(a);
            items = items.clone();
            items[WHICH_VIEW_CONTACT] = getString(R.string.add_contact_);
        } else {
            builder.setTitle(n);
        }
        if (SpamDB.isBlacklisted(getApplicationContext(), a)) {
            items = items.clone();
            items[WHICH_MARK_SPAM] = getString(R.string.dont_filter_spam_);
        }
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                Intent i;
                try {
                    switch (which) {
                        case WHICH_ANSWER:
                            ConversationListActivity.this.startActivity(getComposeIntent(
                                    ConversationListActivity.this, a, false));
                            break;
                        case WHICH_CALL:
                            i = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + a));
                            ConversationListActivity.this.startActivity(i);
                            break;
                        case WHICH_VIEW_CONTACT:
                            if (n == null) {
                                i = ContactsWrapper.getInstance().getInsertPickIntent(a);
                                Conversation.flushCache();
                            } else {
                                final Uri uri = c.getContact().getUri();
                                i = new Intent(Intent.ACTION_VIEW, uri);
                            }
                            ConversationListActivity.this.startActivity(i);
                            break;
                        case WHICH_VIEW:
                            i = new Intent(ConversationListActivity.this,
                                    MessageListActivity.class);
                            i.setData(target);
                            ConversationListActivity.this.startActivity(i);
                            break;
                        case WHICH_DELETE:
                            ConversationListActivity
                                    .deleteMessages(ConversationListActivity.this, target,
                                            R.string.delete_thread_,
                                            R.string.delete_thread_question,
                                            null);
                            break;
                        case WHICH_MARK_SPAM:
                            ConversationListActivity.addToOrRemoveFromSpamlist(
                                    ConversationListActivity.this, c.getContact().getNumber());
                            break;
                        default:
                            break;
                    }
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "unable to launch activity:", e);
                    Toast.makeText(ConversationListActivity.this, R.string.error_unknown,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.create().show();
        return true;
    }
    public static String getDate(final Context context, final long time) {
        long t = time;
        if (t < MIN_DATE) {
            t *= MILLIS;
        }
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                PreferencesActivity.PREFS_FULL_DATE, false)) {
            return DateFormat.getTimeFormat(context).format(t) + " "
                    + DateFormat.getDateFormat(context).format(t);
        } else if (t < CAL_DAYAGO.getTimeInMillis()) {
            return DateFormat.getDateFormat(context).format(t);
        } else {
            return DateFormat.getTimeFormat(context).format(t);
        }
    }

}