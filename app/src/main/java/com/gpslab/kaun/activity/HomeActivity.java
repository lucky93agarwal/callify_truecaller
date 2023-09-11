package com.gpslab.kaun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.gpslab.kaun.CallReceiver;
import com.gpslab.kaun.R;

public class HomeActivity extends AppCompatActivity {
    private Intent forService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        forService = new Intent(HomeActivity.this, CallReceiver.class);
    }
}