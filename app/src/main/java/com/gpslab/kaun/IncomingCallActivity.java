package com.gpslab.kaun;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gpslab.kaun.DB.FirstTableData;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class IncomingCallActivity extends Activity {
    PopupWindow popUp;
    boolean click = true;
    private final static String simSlotName[] = {
            "extra_asus_dial_use_dualsim",
            "com.android.phone.extra.slot",
            "slot",
            "simslot",
            "sim_slot",
            "subscription",
            "Subscription",
            "phone",
            "com.android.phone.DialingMode",
            "simSlot",
            "slot_id",
            "simId",
            "simnum",
            "phone_type",
            "slotId",
            "slotIdx"
    };
//    TextToSpeech t1;
    public String number;
    public String type;
    public MyDbHandler myDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popUp = new PopupWindow(this);
        if(Build.VERSION.SDK_INT>=21){
            popUp.setElevation(5.0f);
        }
        myDbHandler = new MyDbHandler(getApplicationContext(),"userbd",null,1);

        Temp.setMyDbHandler(myDbHandler);
        myDbHandler = Temp.getMyDbHandler();



//        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status == TextToSpeech.SUCCESS) {
//                    int result = t1.setLanguage(new Locale("en", "IN"));
//                    if (result == TextToSpeech.LANG_MISSING_DATA ||
//                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                        Toast.makeText(getApplicationContext(), "This language is not supported!",
//                                Toast.LENGTH_SHORT);
//                    } else {
//
//                        t1.setPitch(1.0f);
//                        t1.setSpeechRate(1.0f);
//
//
//                    }
//                }
//            }
//        });



        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        number = getIntent().getStringExtra("phone_no");
//        number = myDbHandler.getSingleUserInfo(number);
//        user = myDbHandler.getSingleUserInfo(number);
        Log.d("WalletlsjdflsjWallet","User Name = =  "+number);

        type = getIntent().getStringExtra("type");



//        String toSpeak = number;
//
//        int apiVer = android.os.Build.VERSION.SDK_INT;
//        if (apiVer >= 11){
//            Log.d("WalletLuckyYUYU","Value = 1");
//            speakApi13(toSpeak);
//        } else {
//            // compatibility mode
//            Log.d("WalletLuckyYUYU","Value = 2");
//            HashMap<String, String> params = new HashMap<String, String>();
//            t1.speak(toSpeak, TextToSpeech.QUEUE_ADD, params);
//        }

        LinearLayout.LayoutParams lpxx = new LinearLayout.LayoutParams(0, 40,40);
        lpxx.height=40;
        lpxx.weight = 40;




        Typeface face = getResources().getFont(R.font.montserratmedium);
        Typeface facebold = getResources().getFont(R.font.montserratbold);
//        tv.setTypeface(face);
        LinearLayout layout_top_one_center = new LinearLayout(this);
        layout_top_one_center.setLayoutParams(lpxx);
        layout_top_one_center.setOrientation(LinearLayout.VERTICAL);
        layout_top_one_center.setBackgroundResource(R.drawable.circledrawablepopup);

        TextView tvname = new TextView(this);
        tvname.setText("M");
        tvname.setTypeface(facebold);
        tvname.setTextColor(Color.parseColor("#ffffff"));
        tvname.setTypeface(null, Typeface.BOLD);
        tvname.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

        layout_top_one_center.addView(tvname);


        LinearLayout layout_bottom_two = new LinearLayout(this);




        /// top three linear layout

        LinearLayout layout_top_first = new LinearLayout(this);
        LinearLayout layout_top_two = new LinearLayout(this);
        LinearLayout layout_top_two_two = new LinearLayout(this);

        layout_top_two_two.setOrientation(LinearLayout.HORIZONTAL);
        /// top three linear layout


        TextView tvinformation = new TextView(this);
        tvinformation.setText(type);
        tvinformation.setTypeface(face);
        tvinformation.setTextColor(Color.parseColor("#E4E4E9"));
        tvinformation.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);

        ImageView ivimg = new ImageView(this);
        ivimg.setBackgroundResource(R.drawable.simiconpopup);
        int width = 30;
        int height = 30;

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
        parms.setMargins(0,2,5,0);
        ivimg.setLayoutParams(parms);
        layout_top_two_two.addView(ivimg);
        layout_top_two_two.addView(tvinformation);

        /// top three linear layout
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,20);
        lp.weight = 20;
        lp.gravity = Gravity.CENTER;
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,80);
        lps.weight = 80;
        lps.gravity = Gravity.CENTER_VERTICAL;
//        LinearLayout.LayoutParams lpss = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,20);
//        lpss.weight = 20;


        layout_top_first.setLayoutParams(lp);
        layout_top_first.setGravity(Gravity.CENTER);


        layout_top_two.setLayoutParams(lps);
        layout_top_two.setGravity(Gravity.CENTER_VERTICAL);
        layout_top_two.setOrientation(LinearLayout.VERTICAL);
//        layout_top_three.setLayoutParams(lpss);


        TextView tvnumber = new TextView(this);
        tvnumber.setText(number);
        tvnumber.setTypeface(facebold);
        tvnumber.setTextColor(Color.parseColor("#E4E4E9"));
        tvnumber.setTypeface(null, Typeface.BOLD);
        tvnumber.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        layout_top_two.addView(layout_top_two_two);

        layout_top_two.addView(tvnumber);

        TextView tvlucky = new TextView(this);
        tvlucky.setText("M");
        tvlucky.setTypeface(facebold);
        tvlucky.setTextColor(Color.parseColor("#3B8AFD"));
        tvlucky.setTypeface(null, Typeface.BOLD);
        tvlucky.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);


        LinearLayout ll = new LinearLayout(this);

        LinearLayout.LayoutParams lpxxb= new LinearLayout.LayoutParams(0, 135,50);
        lpxxb.height=135;
        lpxxb.weight = 50;
        lpxxb.setMargins(40,0,22,0);
        lpxxb.gravity= Gravity.CENTER;

        ll.setLayoutParams(lpxxb);
        ll.setGravity(Gravity.CENTER);
        ll.setBackgroundResource(R.drawable.circledrawablepopup);
        ll.addView(tvlucky);

        layout_top_first.addView(ll);











//
//        layout_top_three.setBackgroundColor(Color.parseColor("#1180FF"));

        LinearLayout layout = new LinearLayout(this);
        LinearLayout layout_wit = new LinearLayout(this);
        LinearLayout mainLayout = new LinearLayout(this);
        TextView tv = new TextView(this);

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        WindowManager.LayoutParams params1 = new WindowManager.LayoutParams();
        params1.height = 400;
        params1.width = 1000;


        layout.setOrientation(LinearLayout.HORIZONTAL);

        layout.setBackgroundResource(R.drawable.popupcolor);

        tv.setText("Hi this is a sample text for popup window");
        params.height = 230;
        params.width = 1000;
        layout.addView(layout_top_first);
        layout.addView(layout_top_two);


        // popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);

        LinearLayout linearLayout_two_bottom_one = new LinearLayout(this);
        LinearLayout linearLayout_two_bottom_two = new LinearLayout(this);
        LinearLayout linearLayout_two_bottom_three = new LinearLayout(this);
        LinearLayout linearLayout_two_bottom_four = new LinearLayout(this);

        LinearLayout.LayoutParams lp_one = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
        lp_one.width = 1;
        lp_one.gravity = Gravity.CENTER;
        linearLayout_two_bottom_one.setLayoutParams(lp_one);
        linearLayout_two_bottom_one.setBackgroundColor(Color.parseColor("#0f0e0e"));
        linearLayout_two_bottom_one.setOrientation(LinearLayout.VERTICAL);
        linearLayout_two_bottom_one.setGravity(Gravity.CENTER);

        ImageView oneiv = new ImageView(this);
        oneiv.setBackgroundResource(R.drawable.phoneicon);
        oneiv.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
        oneiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +number));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("com.android.phone.force.slot", true);
                intent.putExtra("Cdma_Supp", true);
                //Add all slots here, according to device.. (different device require different key so put all together)
                for (String s : simSlotName)
                    intent.putExtra(s, 0); //0 or 1 according to sim.......

                //works only for API >= 21
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", " here You have to get phone account handle list by using telecom manger for both sims:- using this method getCallCapablePhoneAccounts()");

                startActivity(intent);
                popUp.dismiss();
            }
        });
        TextView tvphone = new TextView(this);
        tvphone.setText("CALL");
        tvphone.setTypeface(face);

        tvphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +number));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("com.android.phone.force.slot", true);
                intent.putExtra("Cdma_Supp", true);
                //Add all slots here, according to device.. (different device require different key so put all together)
                for (String s : simSlotName)
                    intent.putExtra(s, 0); //0 or 1 according to sim.......

                //works only for API >= 21
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", " here You have to get phone account handle list by using telecom manger for both sims:- using this method getCallCapablePhoneAccounts()");

                startActivity(intent);
                popUp.dismiss();
            }
        });

        tvphone.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        tvphone.setGravity(Gravity.CENTER);
        tvphone.setTextColor(Color.parseColor("#908F9D"));
        linearLayout_two_bottom_one.addView(oneiv);
        linearLayout_two_bottom_one.addView(tvphone);



        linearLayout_two_bottom_two.setLayoutParams(lp_one);
        linearLayout_two_bottom_two.setBackgroundColor(Color.parseColor("#0f0e0e"));
        linearLayout_two_bottom_two.setOrientation(LinearLayout.VERTICAL);
        linearLayout_two_bottom_two.setGravity(Gravity.CENTER);

        ImageView twoiv = new ImageView(this);
        twoiv.setBackgroundResource(R.drawable.chat);
        twoiv.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
        TextView tvmsg = new TextView(this);
        tvmsg.setText("MESSAGE");
        tvmsg.setTypeface(face);
        tvmsg.setTypeface(face);
        tvmsg.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        tvmsg.setGravity(Gravity.CENTER);
        tvmsg.setTextColor(Color.parseColor("#908F9D"));
        linearLayout_two_bottom_two.addView(twoiv);
        linearLayout_two_bottom_two.addView(tvmsg);



        linearLayout_two_bottom_three.setLayoutParams(lp_one);
        linearLayout_two_bottom_three.setBackgroundColor(Color.parseColor("#0f0e0e"));
        linearLayout_two_bottom_three.setOrientation(LinearLayout.VERTICAL);
        linearLayout_two_bottom_three.setGravity(Gravity.CENTER);

        ImageView threeiv = new ImageView(this);
        threeiv.setBackgroundResource(R.drawable.edit);
        threeiv.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
        TextView tvedit = new TextView(this);
        tvedit.setText("EDIT");
        tvedit.setTypeface(face);
        tvedit.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        tvedit.setGravity(Gravity.CENTER);
        tvedit.setTextColor(Color.parseColor("#908F9D"));
        linearLayout_two_bottom_three.addView(threeiv);
        linearLayout_two_bottom_three.addView(tvedit);



        linearLayout_two_bottom_four.setLayoutParams(lp_one);
        linearLayout_two_bottom_four.setBackgroundColor(Color.parseColor("#0f0e0e"));
        linearLayout_two_bottom_four.setOrientation(LinearLayout.VERTICAL);
        linearLayout_two_bottom_four.setGravity(Gravity.CENTER);


        ImageView fouriv = new ImageView(this);
        fouriv.setBackgroundResource(R.drawable.rupee);
        fouriv.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
        TextView tvpay = new TextView(this);
        tvpay.setText("PAY");
        tvpay.setTypeface(face);
        tvpay.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        tvpay.setGravity(Gravity.CENTER);
        tvpay.setTextColor(Color.parseColor("#908F9D"));
        linearLayout_two_bottom_four.addView(fouriv);
        linearLayout_two_bottom_four.addView(tvpay);




        LinearLayout linearLayout_one_bottom_one = new LinearLayout(this);
        LinearLayout linearLayout_one_bottom_two = new LinearLayout(this);

        LinearLayout.LayoutParams lp_one_bottom_one = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,65);
        lp_one_bottom_one.width = 65;
        lp_one_bottom_one.gravity = Gravity.CENTER;
        lp_one_bottom_one.setMargins(30,0,0,0);

        LinearLayout.LayoutParams lp_one_bottom_two = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,35);
        lp_one_bottom_two.width = 35;
        lp_one_bottom_two.gravity = Gravity.CENTER;

        linearLayout_one_bottom_one.setBackgroundColor(Color.parseColor("#0f0e0e"));
        linearLayout_one_bottom_two.setBackgroundColor(Color.parseColor("#0f0e0e"));
        linearLayout_one_bottom_one.setLayoutParams(lp_one_bottom_one);
        linearLayout_one_bottom_one.setGravity(Gravity.CENTER);
        linearLayout_one_bottom_one.setOrientation(LinearLayout.VERTICAL);

        linearLayout_one_bottom_two.setLayoutParams(lp_one_bottom_two);
        linearLayout_one_bottom_two.setGravity(Gravity.CENTER);
        linearLayout_one_bottom_two.setOrientation(LinearLayout.VERTICAL);

        TextView tvnumber_two = new TextView(this);
        tvnumber_two.setText(number+" - Airtel");
        tvnumber_two.setTypeface(face);
        tvnumber_two.setTextColor(Color.parseColor("#cacaca"));
        tvnumber_two.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

        TextView tvnumber_area = new TextView(this);
        tvnumber_area.setText("Uttar Pradesh East, India");
        tvnumber_area.setTypeface(face);
        tvnumber_area.setTextColor(Color.parseColor("#908F9D"));
        tvnumber_area.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);

        Button btnview = new Button(this);
        btnview.setText("View");
        btnview.setTypeface(face);
        btnview.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        btnview.setTextColor(Color.parseColor("#FFFFFF"));
        btnview.setBackgroundResource(R.drawable.popupcolortwo);
        btnview.setElevation(3);
        btnview.setGravity(Gravity.CENTER);
        btnview.setLayoutParams(new LinearLayout.LayoutParams(180, 90));

        linearLayout_one_bottom_two.addView(btnview);
        linearLayout_one_bottom_one.addView(tvnumber_two);
        linearLayout_one_bottom_one.addView(tvnumber_area);



        LinearLayout linearLayout_one_bottom = new LinearLayout(this);
        LinearLayout linearLayout_two_bottom = new LinearLayout(this);

        LinearLayout.LayoutParams lp_one_bottom = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,1);
        lp_one_bottom.height = 1;
        lp_one_bottom.gravity = Gravity.CENTER;
        linearLayout_one_bottom.setLayoutParams(lp_one_bottom);
        linearLayout_one_bottom.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout_one_bottom.setBackgroundColor(Color.parseColor("#0f0e0e"));

        linearLayout_one_bottom.addView(linearLayout_one_bottom_one);
        linearLayout_one_bottom.addView(linearLayout_one_bottom_two);

        LinearLayout.LayoutParams lps_two_bottom = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,1);
        lps_two_bottom.height = 1;
        lps_two_bottom.gravity = Gravity.CENTER_VERTICAL;
        linearLayout_two_bottom.setLayoutParams(lps_two_bottom);
        linearLayout_two_bottom.setBackgroundColor(Color.parseColor("#0f0e0e"));
        linearLayout_two_bottom.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout_two_bottom.addView(linearLayout_two_bottom_one);
        linearLayout_two_bottom.addView(linearLayout_two_bottom_two);
        linearLayout_two_bottom.addView(linearLayout_two_bottom_three);
        linearLayout_two_bottom.addView(linearLayout_two_bottom_four);

        TextView tvss = new TextView(this);
        tvss.setText("T1");

        TextView tv2s = new TextView(this);
        tv2s.setText("T2");
        layout_wit.addView(linearLayout_one_bottom);
        layout_wit.addView(linearLayout_two_bottom);
        layout_wit.setOrientation(LinearLayout.VERTICAL);
        layout_wit.setBackgroundColor(Color.parseColor("#0f0e0e"));










        mainLayout.addView(layout, params);
        mainLayout.addView(layout_wit, params1);

        mainLayout.setBackgroundResource(R.drawable.popuplayoutbackground);

        setContentView(mainLayout);
    }
//    @TargetApi(13)
//    protected void speakApi13(String text) {
//
//        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//        int amStreamMusicMaxVol = am.getStreamMaxVolume(am.STREAM_MUSIC);
//        am.setStreamVolume(am.STREAM_MUSIC, amStreamMusicMaxVol, 0);
//
//
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, "15");
//        t1.speak(text, TextToSpeech.QUEUE_FLUSH, params);
//    }
}
