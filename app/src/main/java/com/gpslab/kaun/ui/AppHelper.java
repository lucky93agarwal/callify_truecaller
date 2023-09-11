package com.gpslab.kaun.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import com.orhanobut.logger.Logger;
import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatDrawableManager;

public class AppHelper {
    private static ProgressDialog mDialog;
    private static Dialog dialog;
    public static float density = 1;
    public static ProgressDialog progressDialog;

    public static Point displaySize = new Point();
    public static int statusBarHeight = 0;

    public static boolean usingHardwareInput;
    public static DisplayMetrics displayMetrics = new DisplayMetrics();
    public static Drawable getVectorDrawable(Context mContext, @DrawableRes int id) {
        return AppCompatDrawableManager.get().getDrawable(mContext, id);
    }
    public static boolean isAndroid8() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static void LogCat(String Message) {
        if (AppConstants.DEBUGGING_MODE) {
            if (Message != null) {
                if (!AppConstants.DEBUGGING_MODE) {
                    return;
                }
                Logger.e(Message);

            }
        }
    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * method to convert pixel to dp
     *
     * @param px this is  parameter for pxToDp  method
     * @return return value
     */
    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
