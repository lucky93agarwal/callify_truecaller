package com.gpslab.kaun.upgrade_to_premium;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.gpslab.kaun.R;

public class UpgradePremiumSecActivity extends AppCompatActivity {

    public LinearLayout llcheckbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_premium_sec);
        initViews();

        onclick();


    }


    public void onclick(){
        llcheckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpgradePremiumSecActivity.this)
                        .setIcon(R.drawable.logonew)
                        .setTitle("Upgrade Premium")
                        .setMessage("Do you want to proceed Yes or No?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Conferm();
                                    }
                                })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }


    public void Conferm(){
        new AlertDialog.Builder(UpgradePremiumSecActivity.this)
                .setIcon(R.drawable.logonew)
                .setTitle("Upgrade Premium")
                .setMessage("This feature is coming soon, we will notify you.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void initViews(){
        llcheckbtn = (LinearLayout)findViewById(R.id.checkbtn);
    }
}