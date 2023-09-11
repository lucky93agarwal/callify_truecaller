package com.gpslab.kaun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.gpslab.kaun.R;

import java.util.regex.PatternSyntaxException;

import de.ub0r.android.logg0r.Log;

public class PreferencesActivity extends AppCompatActivity {
    public static final String TAG = "prefs";

    private static final String PREFS_BEHAVIOR = "prefs_behavior";

    /**
     * Preference's name: vibrate on receive.
     */
    public static final String PREFS_VIBRATE = "receive_vibrate";

    /**
     * Preference's name: sound on receive.
     */
    public static final String PREFS_SOUND = "receive_sound";

    /**
     * Preference's name: led color.
     */
    private static final String PREFS_LED_COLOR = "receive_led_color";

    /**
     * Preference's name: led flash.
     */
    private static final String PREFS_LED_FLASH = "receive_led_flash";

    /**
     * Preference's name: vibrator pattern.
     */
    private static final String PREFS_VIBRATOR_PATTERN = "receive_vibrate_mode";

    /**
     * Preference's name: enable notifications.
     */
    public static final String PREFS_NOTIFICATION_ENABLE = "notification_enable";

    /**
     * Preference's name: hide sender/text in notifications.
     */
    public static final String PREFS_NOTIFICATION_PRIVACY = "receive_privacy";

    /**
     * Preference's name: icon for notifications.
     */
    private static final String PREFS_NOTIFICATION_ICON = "notification_icon";

    /**
     * Prefernece's name: show contact's photo.
     */
    public static final String PREFS_CONTACT_PHOTO = "show_contact_photo";

    /**
     * Preference's name: show emoticons in messagelist.
     */
    public static final String PREFS_EMOTICONS = "show_emoticons";

    /**
     * Preference's name: bubbles for incoming messages.
     */
    private static final String PREFS_BUBBLES_IN = "bubbles_in";

    /**
     * Preference's name: bubbles for outgoing messages.
     */
    private static final String PREFS_BUBBLES_OUT = "bubbles_out";

    /**
     * Preference's name: show full date and time.
     */
    public static final String PREFS_FULL_DATE = "show_full_date";

    /**
     * Preference's name: hide send button.
     */
    public static final String PREFS_HIDE_SEND = "hide_send";

    /**
     * Preference's name: hide restore.
     */
    public static final String PREFS_HIDE_RESTORE = "hide_restore";

    /**
     * Preference's name: hide paste button.
     */
    public static final String PREFS_HIDE_PASTE = "hide_paste";

    /**
     * Preference's name: hide widget's label.
     */
    public static final String PREFS_HIDE_WIDGET_LABEL = "hide_widget_label";

    /**
     * Preference's name: hide delete all threads.
     */
    public static final String PREFS_HIDE_DELETE_ALL_THREADS = "hide_delete_all_threads";

    /**
     * Preference's name: hide message count.
     */
    public static final String PREFS_HIDE_MESSAGE_COUNT = "hide_message_count";

    /**
     * Preference's name: theme.
     */
    private static final String PREFS_THEME = "theme";

    /**
     * Theme: black.
     */
    private static final String THEME_BLACK = "black";

    /**
     * Preference's name: text size.
     */
    private static final String PREFS_TEXTSIZE = "textsizen";

    /**
     * Preference's name: text color.
     */
    private static final String PREFS_TEXTCOLOR = "textcolor";

    /**
     * Preference's name: ignore text color for list ov threads.
     */
    private static final String PREFS_TEXTCOLOR_IGNORE_CONV = "text_color_ignore_conv";

    /**
     * Preference's name: enable autosend.
     */
    public static final String PREFS_ENABLE_AUTOSEND = "enable_autosend";

    /**
     * Preference's name: mobile_only.
     */
    public static final String PREFS_MOBILE_ONLY = "mobile_only";

    /**
     * Preference's name: edit_short_text.
     */
    public static final String PREFS_EDIT_SHORT_TEXT = "edit_short_text";

    /**
     * Preference's name: show text field.
     */
    public static final String PREFS_SHOWTEXTFIELD = "show_textfield";

    /**
     * Preference's name: show target app.
     */
    public static final String PREFS_SHOWTARGETAPP = "show_target_app";

    /**
     * Preference's name: backup of last sms.
     */
    public static final String PREFS_BACKUPLASTTEXT = "backup_last_sms";

    /**
     * Preference's name: decode decimal ncr.
     */
    public static final String PREFS_DECODE_DECIMAL_NCR = "decode_decimal_ncr";

    /**
     * Preference's name: activate sender.
     */
    public static final String PREFS_ACTIVATE_SENDER = "activate_sender";

    /**
     * Preference's name: forward sms sender.
     */
    public static final String PREFS_FORWARD_SMS_CLEAN = "forwarded_sms_clean";

    /**
     * Preference's name: prefix regular expression.
     */
    private static final String PREFS_REGEX = "regex";

    /**
     * Preference's name: prefix replace.
     */
    private static final String PREFS_REPLACE = "replace";

    private static final String PREFS_EU_USER_CONSENT_POLICY = "eu_user_consent_policy";

    /**
     * Number of regular expressions.
     */
    private static final int PREFS_REGEX_COUNT = 3;

    /**
     * Default color.
     */
    private static final int BLACK = 0xff000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
    }
    public static long[] getVibratorPattern(final Context context) {
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
        final String s = p.getString(PREFS_VIBRATOR_PATTERN, "0");
        final String[] ss = s.split("_");
        final int l = ss.length;
        final long[] ret = new long[l];
        for (int i = 0; i < l; i++) {
            ret[i] = Long.parseLong(ss[i]);
        }
        return ret;
    }
    public static int getLEDcolor(final Context context) {
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
        final String s = p.getString(PREFS_LED_COLOR, "65280");
        return Integer.parseInt(s);
    }

    public static int[] getLEDflash(final Context context) {
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
        final String s = p.getString(PREFS_LED_FLASH, "500_2000");
        final String[] ss = s.split("_");
        final int[] ret = new int[2];
        ret[0] = Integer.parseInt(ss[0]);
        ret[1] = Integer.parseInt(ss[1]);
        return ret;
    }


    private static final int[] NOTIFICAION_IMG = new int[]{R.drawable.stat_notify_sms,
            R.drawable.stat_notify_sms_gingerbread, R.drawable.stat_notify_email_generic,
            R.drawable.stat_notify_sms_black, R.drawable.stat_notify_sms_green,
            R.drawable.stat_notify_sms_yellow,};

    public static int getNotificationIcon(final Context context) {
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
        final int i = p.getInt(PREFS_NOTIFICATION_ICON, R.drawable.stat_notify_sms);
        if (i >= 0 && i < NOTIFICAION_IMG.length) {
            return NOTIFICAION_IMG[i];
        }
        return R.drawable.stat_notify_sms;
    }


    public static String fixNumber(final Context context, final String number) {
        String ret = number;
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
        for (int i = 1; i <= PREFS_REGEX_COUNT; i++) {
            final String regex = p.getString(PREFS_REGEX + i, null);
            if (!TextUtils.isEmpty(regex)) {
                try {
                    Log.d(TAG, "search for '", regex, "' in ", ret);
                    ret = ret.replaceAll(regex, p.getString(PREFS_REPLACE + i, ""));
                    Log.d(TAG, "new number: ", ret);
                } catch (PatternSyntaxException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
        return ret;
    }


    public static boolean decodeDecimalNCR(final Context context) {
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
        final boolean b = p.getBoolean(PREFS_DECODE_DECIMAL_NCR, true);
        Log.d(TAG, "decode decimal ncr: ", b);
        return b;
    }
}