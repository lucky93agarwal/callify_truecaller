package com.gpslab.kaun.adapter;

import com.gpslab.kaun.DB.SpamDB;
import com.gpslab.kaun.R;

import com.gpslab.kaun.activity.PreferencesActivity;
import com.gpslab.kaun.fragment.OtherFragment;
import com.gpslab.kaun.model.Conversation;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import de.ub0r.android.lib.apis.Contact;
import de.ub0r.android.lib.apis.ContactsWrapper;
import de.ub0r.android.logg0r.Log;

public class ConversationAdapter extends ResourceCursorAdapter {

    /**
     * Tag for logging.
     */
    static final String TAG = "coa";

    /**
     * Cursor's sort.
     */
    public static final String SORT = CallLog.Calls.DATE + " DESC";

    /**
     * Used text size, color.
     */
//    private final int textSize, textColor;

    /**
     * {@link BackgroundQueryHandler}.
     */
    private final BackgroundQueryHandler queryHandler;

    /**
     * Token for {@link BackgroundQueryHandler}: message list query.
     */
    private static final int MESSAGE_LIST_QUERY_TOKEN = 0;


    private final Activity activity;

    /**
     * List of blocked numbers.
     */
    private final String[] blacklist;

    /**
     * {@link ContactsWrapper}.
     */
    private static final ContactsWrapper WRAPPER = ContactsWrapper.getInstance();

    /**
     * Default {@link Drawable} for {@link Contact}s.
     */
    private Drawable defaultContactAvatar = null;

    /**
     * Convert NCR.
     */
//    private final boolean convertNCR;

    /**
     * Show emoticons as images
     */
//    private final boolean showEmoticons;

    /**
     * Use grid instead of list.
     */
    private final boolean useGridLayout;

    /**
     * View holder.
     */
    private static class ViewHolder {

        TextView tvBody;

        TextView tvPerson;

        TextView tvCount;

        TextView tvDate;

        CircleImageView ivPhoto;
        LinearLayout lnewlinear;

        View vRead;
    }

    /**
     * Handle queries in background.
     *
     * @author flx
     */
    private final class BackgroundQueryHandler extends AsyncQueryHandler {

        /**
         * A helper class to help make handling asynchronous {@link ContentResolver} queries
         * easier.
         *
         * @param contentResolver {@link ContentResolver}
         */
        public BackgroundQueryHandler(final ContentResolver contentResolver) {
            super(contentResolver);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onQueryComplete(final int token, final Object cookie, final Cursor cursor) {
            switch (token) {
                case MESSAGE_LIST_QUERY_TOKEN:
                    ConversationAdapter.this.changeCursor(cursor);
                    ConversationAdapter.this.activity
                            .setProgressBarIndeterminateVisibility(Boolean.FALSE);
                    return;
                default:
            }
        }
    }


    public ConversationAdapter(final Activity c) {
        super(c, R.layout.conversationlist_item, null, true);
        activity = c;

        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(activity);
        useGridLayout = p.getBoolean("use_gridlayout", false);
        Log.d("WalletLuseGridLayoutYUYU","userGridLayout = 1 = = = "+useGridLayout );
        if (useGridLayout) {
            super.setViewResource(R.layout.conversation_square);
        }
        final ContentResolver cr = c.getContentResolver();
        queryHandler = new BackgroundQueryHandler(cr);
        blacklist = SpamDB.getBlacklist(c);

        defaultContactAvatar = c.getResources().getDrawable(R.drawable.buildingicon);

//        convertNCR = PreferencesActivity.decodeDecimalNCR(c);
//        showEmoticons = PreferencesActivity.showEmoticons(c);
//        textSize = PreferencesActivity.getTextsize(c);
//        textColor = PreferencesActivity.getTextcolor(c);

        Cursor cursor = null;
        try {
            cursor = cr.query(Conversation.URI_SIMPLE, Conversation.PROJECTION_SIMPLE,
                    Conversation.COUNT + ">0", null, null);
        } catch (Exception e) {
            Log.e(TAG, "error getting conversations", e);
        }

        if (cursor != null) {
            cursor.registerContentObserver(new ContentObserver(new Handler()) {
                @Override
                public void onChange(final boolean selfChange) {
                    super.onChange(selfChange);
                    if (!selfChange) {
                        Log.d(TAG, "call startMsgListQuery();");
                        ConversationAdapter.this.startMsgListQuery();
                        Log.d(TAG, "invalidate cache");
                        Conversation.invalidate();
                    }
                }
            });
        }
        // startMsgListQuery();
    }

    /**
     * Start ConversationList query.
     */
    public final void startMsgListQuery() {
        // Cancel any pending queries
        queryHandler.cancelOperation(MESSAGE_LIST_QUERY_TOKEN);
        try {
            // Kick off the new query
            activity.setProgressBarIndeterminateVisibility(Boolean.TRUE);
            queryHandler.startQuery(MESSAGE_LIST_QUERY_TOKEN, null, Conversation.URI_SIMPLE,
                    Conversation.PROJECTION_SIMPLE, Conversation.COUNT + ">0", null, SORT);
        } catch (SQLiteException e) {
            Log.e(TAG, "error starting query", e);
        }
    }

    /*
     *
     * /** {@inheritDoc}
     */
    int i = 0;
    @Override
    public final void bindView(final View view, final Context context, final Cursor cursor) {
        final Conversation c = Conversation.getConversation(context, cursor, false);
        final Contact contact = c.getContact();

     
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.tvPerson = (TextView) view.findViewById(R.id.addr);
            holder.tvCount = (TextView) view.findViewById(R.id.count);
            holder.tvBody = (TextView) view.findViewById(R.id.body);
            holder.tvDate = (TextView) view.findViewById(R.id.date);
            holder.ivPhoto = (CircleImageView) view.findViewById(R.id.photo);
            holder.vRead = view.findViewById(R.id.read);
            holder.lnewlinear = view.findViewById(R.id.newlinear);
            view.setTag(holder);
        }

        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
        if (useGridLayout || p.getBoolean(PreferencesActivity.PREFS_HIDE_MESSAGE_COUNT, false)) {
            holder.tvCount.setVisibility(View.GONE);
        } else {
            final int count = c.getCount();
            if (count < 0) {
                holder.tvCount.setText("");
            } else {
                holder.tvCount.setText("" + c.getCount() + "");
            }
        }
//        if (textSize > 0) {
            holder.tvBody.setTextSize(13);
//        }
//
//        final int col = textColor;
//        if (col != 0) {
//            holder.tvPerson.setTextColor(CalendarCo);
//            holder.tvBody.setTextColor(col);
//            holder.tvCount.setTextColor(col);
//            holder.tvDate.setTextColor(col);
//        }

        i = i+1;
        if ((i & 1) == 0 ) {
            holder.lnewlinear.setBackgroundResource(R.drawable.circledrawnew);
        } else {
            holder.lnewlinear.setBackgroundResource(R.drawable.circledrawnewtwo);
        }



        Log.d("WalletLuseGridLayoutYUYU","userGridLayout = 1 = = = "+OtherFragment.showContactPhoto );
        if (useGridLayout || OtherFragment.showContactPhoto) {
            Log.d("WalletLuckyuYUYU","userGridLayout = 7 = = =" );
//            holder.ivPhoto.setImageDrawable(contact.getAvatar(activity, defaultContactAvatar));

//
//            holder.ivPhoto.setVisibility(View.VISIBLE);
//            if (!useGridLayout) {
//                Log.d("WalletLuckyuYUYU","userGridLayout = 8 = = ="+contact.getLookUpUri(context.getContentResolver()) );
//                holder.ivPhoto.setOnClickListener(WRAPPER.getQuickContact(context, holder.ivPhoto, contact.getLookUpUri(context.getContentResolver()), 2, null));
//            }
        } else {
            Log.d("WalletLuckyuYUYU","userGridLayout = 9 = = =" );
            holder.ivPhoto.setVisibility(View.GONE);
        }

        if (isBlocked(contact.getNumber())) {
            holder.tvPerson.setText("[" + contact.getDisplayName() + "]");
        } else {
            holder.tvPerson.setText(contact.getDisplayName());
        }

        // read status
        if (c.getRead() == 0) {
            holder.vRead.setVisibility(View.INVISIBLE);
            holder.tvPerson.setTextColor(Color.parseColor("#000000"));
            holder.tvPerson.setTypeface(null, Typeface.BOLD);

            holder.tvBody.setTextColor(Color.parseColor("#908F9D"));
            holder.tvBody.setTypeface(null, Typeface.NORMAL);
        } else {
            holder.vRead.setVisibility(View.INVISIBLE);
            holder.tvPerson.setTextColor(Color.parseColor("#000000"));
            holder.tvPerson.setTypeface(null, Typeface.NORMAL);

            holder.tvBody.setTextColor(Color.parseColor("#908F9D"));
            holder.tvBody.setTypeface(null, Typeface.NORMAL);
        }

        // body
        CharSequence text = c.getBody();
        if (text == null) {
            text = context.getString(R.string.mms_conversation);
        }
//        if (convertNCR) {
//            text = Converter.convertDecNCR2Char(text);
//        }
//        if (showEmoticons) {
//            text = SmileyParser.getInstance(context).addSmileySpans(text);
//        }
        holder.tvBody.setText(text);

        // date
        long time = c.getDate();


        Date dateObj = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
//        holder.tvDate.setText(OtherFragment.getDate(context, time));
        holder.tvDate.setText(String.valueOf(formatter.format(dateObj)));

        // presence
        ImageView ivPresence = (ImageView) view.findViewById(R.id.presence);
        if (contact.getPresenceState() > 0) {
            ivPresence.setImageResource(Contact.getPresenceRes(contact.getPresenceState()));
            ivPresence.setVisibility(View.VISIBLE);
        } else {
            ivPresence.setVisibility(View.GONE);
        }
    }

    /**
     * Check if address is blacklisted.
     *
     * @param addr address
     * @return true if address is blocked
     */
    private boolean isBlocked(final String addr) {
        if (addr == null) {
            return false;
        }
        for (String aBlacklist : blacklist) {
            if (addr.equals(aBlacklist)) {
                return true;
            }
        }
        return false;
    }
}
