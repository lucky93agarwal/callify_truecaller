package com.gpslab.kaun.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gpslab.kaun.R;

public class ProfileDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_dialog);
        ImageView profileImage = findViewById(R.id.profile_image);
        Glide.with(this)
                .load(getIntent().getStringExtra("url"))
                .apply(new RequestOptions().placeholder(R.drawable.profile))
                .into(profileImage);
    }
}