package com.gpslab.kaun.mssagedetail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gpslab.kaun.databinding.ActivityMessageDetailsBinding;


public class MessageDetailsActivity extends AppCompatActivity {

    public String Body, Address, Time, image;
    private Window window;
    private int brightness = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMessageDetailsBinding activityMessageDetailsBinding = ActivityMessageDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityMessageDetailsBinding.getRoot());


        Body = getIntent().getStringExtra("body");
        Address = getIntent().getStringExtra("address");
        Time = getIntent().getStringExtra("date");
        image = getIntent().getStringExtra("image");




        MessageDataModel messageDataModel = new MessageDataModel(Time, "", Address, Body,image ,this);
        activityMessageDetailsBinding.setUser(messageDataModel);

//        ScreenViewControl();

    }

    private void ScreenViewControl() {
        //       Get the current system brightness
        window = MessageDetailsActivity.this.getWindow();
        WindowManager.LayoutParams layoutpars = window.getAttributes();
        layoutpars.screenBrightness = brightness / (float) 255;
        window.setAttributes(layoutpars);
    }
}