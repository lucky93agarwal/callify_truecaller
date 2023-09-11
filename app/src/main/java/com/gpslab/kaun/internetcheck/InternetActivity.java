package com.gpslab.kaun.internetcheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.gpslab.kaun.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class InternetActivity extends AppCompatActivity {

    public AppCompatButton btnsubmit;
    public TextView tverror;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected() )
        {
            Toast.makeText(InternetActivity.this, "Internet Available", Toast.LENGTH_SHORT).show();
        }

        else
        {
            Toast.makeText(InternetActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }


        initViews();


        setData();




        onClik();
    }


    private void setData(){

        tverror.setText("");


    }

    private void onClik(){

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    private void initViews(){
        btnsubmit = (AppCompatButton)findViewById(R.id.btnnotify);
        tverror = (TextView)findViewById(R.id.errortv);
    }
}