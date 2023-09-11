package com.gpslab.kaun.Contact;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.gpslab.kaun.R;
import com.gpslab.kaun.view.PermissionsUtil;

public class TestingContactActivity extends AppCompatActivity {
    private static final int READ_CONTACT_PERMISSION_REQUEST_CODE = 76;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_contact);
        launchFragment();
    }



    private void launchFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.root_container, new ContactListF())
                .commit();
    }





}