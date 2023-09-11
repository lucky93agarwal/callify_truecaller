package com.gpslab.kaun.view;
import com.gpslab.kaun.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSeekBar;

public class DevlomiSeekbar extends AppCompatSeekBar {
    public DevlomiSeekbar(Context context) {
        super(context);
    }

    public DevlomiSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DevlomiSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DevlomiSeekbar, 0, 0);
        if (array != null) {

            //get given progress color from xml
            int progressColor = array.getColor(R.styleable.DevlomiSeekbar_progressColor, -1);
            if (progressColor != -1) {
                //change progress color
                Drawable progressDrawable = getProgressDrawable().mutate();
                progressDrawable.setColorFilter(progressColor, android.graphics.PorterDuff.Mode.SRC_IN);
                setProgressDrawable(progressDrawable);
            }
            //get given thumb color from xml
            int thumbColor = array.getColor(R.styleable.DevlomiSeekbar_thumbColor, -1);
            if (thumbColor != -1) {
                //change progress color
                Drawable thumbDrawable = getThumb().mutate();
                thumbDrawable.setColorFilter(thumbColor, android.graphics.PorterDuff.Mode.SRC_IN);
                setThumb(thumbDrawable);
            }

            array.recycle();
        }


    }

}


