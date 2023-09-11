package com.gpslab.kaun.model;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import com.gpslab.kaun.R;
import android.widget.RemoteViews;

import com.gpslab.kaun.Receiver.SmsReceiver;
import com.gpslab.kaun.activity.PreferencesActivity;

import de.ub0r.android.logg0r.Log;

public final class WidgetProvider extends AppWidgetProvider {

    /**
     * Tag for output.
     */
    private static final String TAG = "wdp";

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager,
                         final int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");
        SmsReceiver.updateNewMessageNotification(context, null);
    }

    /**
     * Get {@link RemoteViews}.
     *
     * @param context {@link Context}
     * @param count   number of unread messages
     * @param pIntent {@link PendingIntent}
     * @return {@link RemoteViews}
     */
    public static RemoteViews getRemoteViews(final Context context, final int count,
                                      final PendingIntent pIntent) {
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.text1, String.valueOf(count));
        if (count == 0) {
            views.setViewVisibility(R.id.text1, View.GONE);
        } else {
            views.setViewVisibility(R.id.text1, View.VISIBLE);
        }
        if (p.getBoolean(PreferencesActivity.PREFS_HIDE_WIDGET_LABEL, false)) {
            views.setViewVisibility(R.id.label, View.GONE);
        } else {
            views.setViewVisibility(R.id.label, View.VISIBLE);
        }
        if (pIntent != null) {
            views.setOnClickPendingIntent(R.id.widget, pIntent);
            Log.d(TAG, "set pending intent: ", pIntent.toString());
        }
        return views;
    }
}
