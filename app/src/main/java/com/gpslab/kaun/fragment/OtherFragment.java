package com.gpslab.kaun.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.gpslab.kaun.CallLogInfo;
import com.gpslab.kaun.DB.SpamDB;
import com.gpslab.kaun.R;
import com.gpslab.kaun.Receiver.SmsReceiver;
import com.gpslab.kaun.activity.ConversationListActivity;
import com.gpslab.kaun.activity.MessageListActivity;
import com.gpslab.kaun.activity.PreferencesActivity;
import com.gpslab.kaun.adapter.CallLogAdapter;
import com.gpslab.kaun.adapter.ConversationAdapter;
import com.gpslab.kaun.adapter.MessageAdapter;
import com.gpslab.kaun.adapter.MessageRecyclerViewAdapter;
import com.gpslab.kaun.adapter.MisscallRecyclerViewAdapter;
import com.gpslab.kaun.adapter.RecyclerViewAdapter;
import com.gpslab.kaun.adapter.UnMessageRecyclerViewAdapter;
import com.gpslab.kaun.adapter.UnReadMessageAdapter;
import com.gpslab.kaun.model.AsyncHelper;
import com.gpslab.kaun.model.ConsentManager;
import com.gpslab.kaun.model.Conversation;
import com.gpslab.kaun.model.GetMessage;
import com.gpslab.kaun.model.GetUnMessage;
import com.gpslab.kaun.model.GetUnReadMessage;
import com.gpslab.kaun.model.Message;
import com.gpslab.kaun.model.SMSdroid;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.ub0r.android.lib.Utils;
import de.ub0r.android.lib.apis.Contact;
import de.ub0r.android.lib.apis.ContactsWrapper;
import de.ub0r.android.logg0r.Log;


public class OtherFragment extends Fragment{



    public OtherFragment() {
        // Required empty public constructor
    }




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
     *
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



    private AbsListView getListView(final View view) {
        return (AbsListView) view.findViewById(android.R.id.list);
    }


    private void setListAdapter(final ListAdapter la,final View view) {
        AbsListView v = getListView(view);
        if (v instanceof GridView) {
            //noinspection RedundantCast
            ((GridView) v).setAdapter(la);
        } else if (v instanceof ListView) {
            //noinspection RedundantCast
            ((ListView) v).setAdapter(la);
        }
    }

    public static void showRows(final Context context, final Uri u) {
        de.ub0r.android.logg0r.Log.d(TAG, "-----GET HEADERS-----");
        de.ub0r.android.logg0r.Log.d(TAG, "-- ", u.toString(), " --");
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

    }
//    @Override
//    public void onNewIntent(final Intent intent) {
//        if (intent != null) {
//            Log.d(TAG, "got intent: ", intent.getAction());
//            Log.d(TAG, "got uri: ", intent.getData());
//            final Bundle b = intent.getExtras();
//            if (b != null) {
//                Log.d(TAG, "user_query: ", b.get("user_query"));
//                Log.d(TAG, "got extra: ", b);
//            }
//            final String query = intent.getStringExtra("user_query");
//            Log.d(TAG, "user query: ", query);
//
//        }
//    }
    public View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.conversationlist, container, false);


        final Intent i = getActivity().getIntent();
        Log.d("TAG", "got intent: ", i.getAction());
        Log.d("TAG", "got uri: ", i.getData());
        Log.d("TAG", "got extra: ", i.getExtras());

//        setTheme(PreferencesActivity.getTheme(this));
        Utils.setLocale(getActivity());


        // debug info
        showRows(getActivity());

        final AbsListView list = getListView(view);



        if (adapter == null && SMSdroid.isDefaultApp(getActivity())) {
            // after setting SMSdroid to default SMS app
            initAdapter(view);
        }

        CAL_DAYAGO.setTimeInMillis(System.currentTimeMillis());
        CAL_DAYAGO.add(Calendar.DAY_OF_MONTH, -1);

        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getActivity());
        showContactPhoto = p.getBoolean(PreferencesActivity.PREFS_CONTACT_PHOTO, true);
        showEmoticons = p.getBoolean(PreferencesActivity.PREFS_EMOTICONS, false);
        if (adapter != null) {
            adapter.startMsgListQuery();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Conversation c = Conversation.getConversation(getActivity(),
                        (Cursor) parent.getItemAtPosition(position), false);
                final Uri target = c.getUri();
                final Intent i = new Intent(getActivity(), MessageListActivity.class);
                i.setData(target);
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "error launching intent: ", i.getAction(), ", ", i.getData());
                    Toast.makeText(getActivity(), "error launching messaging app!\n" + "Please contact the developer.", Toast.LENGTH_LONG).show();
                }
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Conversation c = Conversation.getConversation(getActivity(),
                        (Cursor) parent.getItemAtPosition(position), true);
                final Uri target = c.getUri();
                if (ContentUris.parseId(target) < 0) {
                    // do not show anything for broken threadIds
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                if (SpamDB.isBlacklisted(getActivity().getApplicationContext(), a)) {
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
                                    getActivity().startActivity(getComposeIntent(
                                            getActivity(), a, false));
                                    break;
                                case WHICH_CALL:
                                    i = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + a));
                                    getActivity().startActivity(i);
                                    break;
                                case WHICH_VIEW_CONTACT:
                                    if (n == null) {
                                        i = ContactsWrapper.getInstance().getInsertPickIntent(a);
                                        Conversation.flushCache();
                                    } else {
                                        final Uri uri = c.getContact().getUri();
                                        i = new Intent(Intent.ACTION_VIEW, uri);
                                    }
                                    getActivity().startActivity(i);
                                    break;
                                case WHICH_VIEW:
                                    i = new Intent(getActivity(),
                                            MessageListActivity.class);
                                    i.setData(target);
                                    getActivity().startActivity(i);
                                    break;
                                case WHICH_DELETE:
                                    ConversationListActivity
                                            .deleteMessages(getActivity(), target,
                                                    R.string.delete_thread_,
                                                    R.string.delete_thread_question,
                                                    null);
                                    break;
                                case WHICH_MARK_SPAM:
                                    ConversationListActivity.addToOrRemoveFromSpamlist(
                                            getActivity(), c.getContact().getNumber());
                                    break;
                                default:
                                    break;
                            }
                        } catch (ActivityNotFoundException e) {
                            Log.e(TAG, "unable to launch activity:", e);
                            Toast.makeText(getActivity(), R.string.error_unknown,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
        longItemClickDialog = new String[WHICH_N];
        longItemClickDialog[WHICH_ANSWER] = getString(R.string.reply);
        longItemClickDialog[WHICH_CALL] = getString(R.string.call);
        longItemClickDialog[WHICH_VIEW_CONTACT] = getString(R.string.view_contact_);
        longItemClickDialog[WHICH_VIEW] = getString(R.string.view_thread_);
        longItemClickDialog[WHICH_DELETE] = getString(R.string.delete_thread_);
        longItemClickDialog[WHICH_MARK_SPAM] = getString(R.string.filter_spam_);

//        if (SMSdroid.isDefaultApp(this)) {
        initAdapter(view);
        getAdsConstent();

        AsyncHelper.setAdapter(adapter);

        return view;
    }



    private void getAdsConstent() {
        new ConsentManager(getActivity()).updateConsent();
    }

    private void initAdapter(final View view) {
        if (!SMSdroid.requestPermission(getActivity(), Manifest.permission.READ_SMS,
                PERMISSIONS_REQUEST_READ_SMS, R.string.permissions_read_sms,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                })) {
            return;
        }

        if (!SMSdroid.requestPermission(getActivity(), Manifest.permission.READ_CONTACTS,
                PERMISSIONS_REQUEST_READ_CONTACTS, R.string.permissions_read_contacts,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                })) {
            return;
        }

        adapter = new ConversationAdapter(getActivity());
        setListAdapter(adapter,view);
        adapter.startMsgListQuery();
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
                    initAdapter(view);
                } else {
                    // this app is useless without permission for reading sms
                    Log.e(TAG, "permission for reading sms denied, exit");
                    getActivity().finish();
                }
                return;
            }
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // just try again.
                    initAdapter(view);
                } else {
                    // this app is useless without permission for reading sms
                    Log.e(TAG, "permission for reading contacts denied, exit");
                    getActivity().finish();
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
//        SmsReceiver.updateNewMessageNotification(context, null);
    }
    static void deleteMessages(final Context context, final Uri uri, final int title,
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
//                        SmsReceiver.updateNewMessageNotification(context, null);
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
    private static void addToOrRemoveFromSpamlist(final Context context, final String addr) {
        SpamDB.toggleBlacklist(context, addr);
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_compose:
                final Intent i = getComposeIntent(getActivity(), null, false);
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "error launching intent: ", i.getAction(), ", ", i.getData());
                    Toast.makeText(getActivity(),
                            "error launching messaging app!\nPlease contact the developer.",
                            Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.item_settings: // start settings activity
                startActivity(new Intent(getActivity(), PreferencesActivity.class));
                return true;
            case R.id.item_delete_all_threads:
                deleteMessages(getActivity(), Uri.parse("content://sms/"), R.string.delete_threads_,
                        R.string.delete_threads_question, null);
                return true;
            case R.id.item_mark_all_read:
                markRead(getActivity(), Uri.parse("content://sms/"), 1);
                markRead(getActivity(), Uri.parse("content://mms/"), 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    static Intent getComposeIntent(final Context context, final String address, final boolean showChooser) {
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