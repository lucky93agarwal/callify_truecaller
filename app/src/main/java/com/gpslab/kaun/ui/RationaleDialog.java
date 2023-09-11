package com.gpslab.kaun.ui;


import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gpslab.kaun.R;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;

class RationaleDialog {

    static AlertDialog.Builder createFor(@NonNull Context context, @NonNull String message, @DrawableRes int... drawables) {
        View view = LayoutInflater.from(context).inflate(R.layout.permissions_rationale_dialog, null);
        ViewGroup header = view.findViewById(R.id.header_container);
        TextView text = view.findViewById(R.id.message);

        for (int i = 0; i < drawables.length; i++) {
            AppCompatImageView imageView = new AppCompatImageView(context);
            imageView.setImageDrawable(context.getResources().getDrawable(drawables[i]));
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            header.addView(imageView);

            if (i != drawables.length - 1) {
                TextView plus = new TextView(context);
                plus.setText("+");
                plus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                plus.setTextColor(Color.WHITE);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(AppHelper.dpToPx(20), 0, AppHelper.dpToPx(20), 0);

                plus.setLayoutParams(layoutParams);
                header.addView(plus);
            }
        }

        text.setText(message);

        return new AlertDialog.Builder(context, R.style.RationaleDialog).setView(view);
    }

}
