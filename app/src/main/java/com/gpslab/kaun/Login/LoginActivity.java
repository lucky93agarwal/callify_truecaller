package com.gpslab.kaun.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gpslab.kaun.R;

public class LoginActivity extends AppCompatActivity {

    public String mobile;

    public RelativeLayout mfacebook, mgoogle;
    public ImageView ivfacebook,ivgoogle;
    public TextView tvfacebook,tvgoogle;



    public LinearLayout mllmenualy;
    public RelativeLayout mrlmenualy;
    public ImageView mivmeually;
    public TextView mtvmenually;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mllmenualy = (LinearLayout)findViewById(R.id.llmenual);
        mrlmenualy = (RelativeLayout)findViewById(R.id.rlmenual);
        mivmeually = (ImageView)findViewById(R.id.ivmenual);
        mtvmenually = (TextView)findViewById(R.id.tvmenual);


        mllmenualy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginSecActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });
        mrlmenualy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginSecActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });
        mivmeually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginSecActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });
        mtvmenually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginSecActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });
        mfacebook = (RelativeLayout)findViewById(R.id.relatfacebook);
        mgoogle = (RelativeLayout)findViewById(R.id.relatgoogle);


        ivfacebook = (ImageView)findViewById(R.id.imagefacebook);
        ivgoogle = (ImageView)findViewById(R.id.imagegoogle);



        tvfacebook = (TextView)findViewById(R.id.textviewfacebook);
        tvgoogle = (TextView)findViewById(R.id.googletv);





        mobile = getIntent().getStringExtra("mobile");
    }
}