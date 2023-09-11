package com.gpslab.kaun.private_and_public;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gpslab.kaun.R;
import com.gpslab.kaun.digitaloceanspaces.RandomString;
import com.gpslab.kaun.retrofit.Log;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class private_and_public {
    public Dialog epicDialog;
    private final Context _context;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public RadioGroup radioGroup;
    public ByteArrayOutputStream byteArrayOutputStream;

    public RadioButton mPublicRadioButton, mPrivateRadioButton;
    public ImageView ivSendbtn;
    public PrivatePubileAdapter adapter;
    public List<GetPrivateAndPubile> productList = new ArrayList<>();
    public GetPrivateAndPubile getbcdata;
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView mRecyclerView;

    public String public_R = "0", private_R = "0";

    public private_and_public(Context context) {
        this._context = context;

        epicDialog = new Dialog(context, R.style.PauseDialog);


    }


    public void profileaddpopup(List<String> pathList) {
        epicDialog.setContentView(R.layout.private_and_public_layout);




        sharedPreferences = _context.getSharedPreferences("data",Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        radioGroup = (RadioGroup) epicDialog.findViewById(R.id.radioGroup);
        ivSendbtn = (ImageView)epicDialog.findViewById(R.id.btnbtnicon);


        mPublicRadioButton = (RadioButton)epicDialog.findViewById(R.id.rbtnpublic);
        mPrivateRadioButton = (RadioButton)epicDialog.findViewById(R.id.rbtnprivate);


        ivSendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putString("checktypepublic","15");
                edit.apply();
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbtnpublic) {
                    edit.putString("public_value","1");
                    edit.putString("private_value","0");
                    edit.apply();
                    public_R = "1";
                    private_R = "0";
                    Log.d("CheckPublic","Radio Buttion   ===   public  ");

                }else {
                    edit.putString("private_value","1");
                    edit.putString("public_value","0");
                    edit.apply();
                    public_R = "0";
                    private_R = "1";



                    Log.d("CheckPublic","Radio Buttion   ===   private  ");
                }


                epicDialog.dismiss();
            }
        });

        if(productList.size()>0){
            productList.clear();
        }

        for(int i=0; i<pathList.size();i++){
            getbcdata = new GetPrivateAndPubile();
            getbcdata.setId(String.valueOf(i));
            getbcdata.setImg(pathList.get(i));



            productList.add(getbcdata);
        }
        Log.d("Popupcheckknew","size   ==     "+pathList.size());


        Log.d("Popupcheckknew","size 1   ==     "+productList.size());

        mRecyclerView = (RecyclerView)epicDialog.findViewById(R.id.rbtnRecyclerView);
        adapter = new PrivatePubileAdapter(_context, productList);
        final GridLayoutManager layoutManager = new GridLayoutManager(_context, 3);
        mLayoutManager = new LinearLayoutManager(_context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // popup background transparent
        epicDialog.show();
    }

    public static String size(long size) {
        android.util.Log.d("WalletScrollCheck", "Scroll Check 52");
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
