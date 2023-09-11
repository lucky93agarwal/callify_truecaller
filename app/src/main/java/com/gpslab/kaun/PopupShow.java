package com.gpslab.kaun;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PopupShow {
    public Dialog epicDialog;
    private final Context _context;
    public Button btnclancel;
    public TextView tvmobile;

    public PopupShow(Context context) {
        this._context = context;
        epicDialog = new Dialog(context, R.style.PauseDialog); //


    }


    public void popupShow(String number) {
        epicDialog.setContentView(R.layout.dialog);


//        tvmobile = (TextView) epicDialog.findViewById(R.id.tv_client);
        tvmobile.setText(number);
//        btnclancel = (Button) epicDialog.findViewById(R.id.dialog_ok);

        btnclancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
            }
        });
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // popup background transparent
        epicDialog.show();

    }
}
