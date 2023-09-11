package com.gpslab.kaun.fullscreen;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class IgnoreBottomInsetFrameLayout extends FrameLayout {

    public IgnoreBottomInsetFrameLayout(Context context) {
        super(context);
    }

    public IgnoreBottomInsetFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IgnoreBottomInsetFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected boolean fitSystemWindows(Rect insets) {
        insets.bottom = 0;
        return super.fitSystemWindows(insets);
    }
}

