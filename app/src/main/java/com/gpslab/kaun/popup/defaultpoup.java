package com.gpslab.kaun.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;

import com.gpslab.kaun.R;

public class defaultpoup {
    public Dialog epicDialog;
    private final Context _context;

    public Button btnonebtn, btntwobtn;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;

    public defaultpoup(Context context) {
        this._context = context;

        epicDialog = new Dialog(context, R.style.PauseDialog);


    }


    public void defaultaddpopup() {
        epicDialog.setContentView(R.layout.defaultpopuplayout);
//        epicDialog.setCancelable(false);
        sharedPreferences = _context.getSharedPreferences("data",Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();

        btnonebtn = (Button)epicDialog.findViewById(R.id.changebtn);
        btntwobtn = (Button)epicDialog.findViewById(R.id.usesystembtn);



        btnonebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putBoolean("default",true);
                edit.apply();
                epicDialog.dismiss();
            }
        });

        btntwobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putBoolean("default",true);
                edit.apply();
                epicDialog.dismiss();
            }
        });


        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // popup background transparent
        epicDialog.show();
    }
}
