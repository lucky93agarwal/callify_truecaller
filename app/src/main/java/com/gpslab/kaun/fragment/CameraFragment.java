package com.gpslab.kaun.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.gpslab.kaun.R;
import com.gpslab.kaun.calling.CameraNewActivity;
import com.gpslab.kaun.retrofit.Log;

import java.util.ArrayList;
import java.util.List;

public class CameraFragment  extends Fragment {
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;


    public CameraFragment() {
        // Required empty public constructor
    }

    int checkPage = 0;

    public Context _context = getActivity();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_camera, container, false);

//        Pix.start(getActivity(), Options.init().setRequestCode(100));
//        Log.d("CheckCameraPosition");
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        _context = getActivity();
        new CountDownTimer(30000, 500) {

            public void onTick(long millisUntilFinished) {
                checkPage = sharedPreferences.getInt("CheckFragment",1);

                Log.d("CheckFragmentECheck","CheckFragment   ====  "+String.valueOf(checkPage));
                if(checkPage == 0){
                    edit.putInt("CheckFragment",1);
                    edit.apply();

                  Intent intent = new Intent(_context,CameraNewActivity.class);
                  intent.putExtra("openPage","camerafragment");
                  _context.startActivity(intent);
                }

            }

            public void onFinish() {

            }

        }.start();


        return view;
    }


}
