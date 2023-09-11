package com.gpslab.kaun;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.gpslab.kaun.Details.ContactDetailsActivity;
import com.gpslab.kaun.Notification.UserProfile;
import com.gpslab.kaun.popup.simselectpopup;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyCustomDialog extends Activity
{
    float x1, x2;
    static final int MIN_DISTANCE = 150;
    TextView Cname,Cnumber,tvnumber,tvfirsttext;
    String ContactName,Contactnumber,id;
    long cid;
    ImageView Cphoto;
    public static Activity mActivity;
    RelativeLayout card;
    Animation rslide,lslide;
    private Window wind;

    public Button refereshbutton;
    public UnifiedNativeAd nativeAd;
    public TextView tvcalltv, tvspamcount;
    public LinearLayout mpopuplinear,textlineartext, callllll,llspamlayout;
    public ImageView ivtag, ivclose, ivcalliv;
    public AppCompatButton btnviewbtn;
    public String imag;
    private static simselectpopup defaultpopupnew;
    public LinearLayout linearbg;

    public String is_spam ="0";


    private LinearLayout llblack, llmessagell, llsavell;
    private ImageView ivblack, ivmessageiv, ivsaveiv;
    private TextView tvblack,tvmessagetv, tvsavetv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.setFinishOnTouchOutside(true);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog);
            this.mActivity = this;

            llsavell = (LinearLayout)findViewById(R.id.savell);
            ivsaveiv = (ImageView)findViewById(R.id.saveiv);
            tvsavetv = (TextView)findViewById(R.id.savetv);

            llblack = (LinearLayout)findViewById(R.id.blackbtn);
            ivblack = (ImageView)findViewById(R.id.blackiv);
            tvblack = (TextView)findViewById(R.id.blacktv);


            llmessagell = (LinearLayout)findViewById(R.id.messagell);
            ivmessageiv = (ImageView)findViewById(R.id.messageiv);
            tvmessagetv = (TextView)findViewById(R.id.messagetv);



            tvspamcount = (TextView)findViewById(R.id.spam_count_tv);

            llspamlayout = (LinearLayout)findViewById(R.id.spamlayout);
            defaultpopupnew = new simselectpopup(this);
            tvcalltv = (TextView)findViewById(R.id.calltv);
            callllll = (LinearLayout)findViewById(R.id.callll);
            ivcalliv = (ImageView)findViewById(R.id.calliv);
            is_spam = getIntent().getStringExtra("is_spam");
            linearbg = (LinearLayout)findViewById(R.id.newbackgrond);



            tvcalltv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    defaultpopupnew.defaultaddpopup(ContactName);
                }
            });
            callllll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    defaultpopupnew.defaultaddpopup(ContactName);
                }
            });
            ivcalliv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    defaultpopupnew.defaultaddpopup(ContactName);
                }
            });


            ivclose = (ImageView)findViewById(R.id.dialog_ok);
            btnviewbtn = (AppCompatButton)findViewById(R.id.viewbtn);
            tvnumber = (TextView)findViewById(R.id.numbercalltv);

            if(is_spam.equalsIgnoreCase("1")){
                linearbg.setBackgroundResource(R.drawable.radbackgroundrow);
                btnviewbtn.setBackgroundResource(R.drawable.redbuttonbgnew);
                llspamlayout.setVisibility(View.VISIBLE);
                tvspamcount.setText(getIntent().getStringExtra("spam_count"));
            }
            ivtag = (ImageView)findViewById(R.id.imagetag);

            textlineartext = (LinearLayout)findViewById(R.id.lineartext);

            tvfirsttext = (TextView)findViewById(R.id.firsttexttv);
            mpopuplinear = (LinearLayout)findViewById(R.id.newlinearpopup);

            initializeContent();

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            ivclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btnviewbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inten = new Intent(MyCustomDialog.this, UserProfile.class);
                    inten.putExtra("name",Contactnumber);
                    inten.putExtra("number",ContactName);
                    inten.putExtra("img",imag);
                    inten.putExtra("duration","120");
                    startActivity(inten);
                    finish();

                }
            });

            refereshbutton = findViewById(R.id.btn_refersh);
            refereshbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            refershAds();


            llsavell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                    contactIntent
                            .putExtra(ContactsContract.Intents.Insert.NAME, ContactName)
                            .putExtra(ContactsContract.Intents.Insert.PHONE, Contactnumber);

                    startActivityForResult(contactIntent, 1);
                }
            });

            ivsaveiv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                    contactIntent
                            .putExtra(ContactsContract.Intents.Insert.NAME, ContactName)
                            .putExtra(ContactsContract.Intents.Insert.PHONE, Contactnumber);

                    startActivityForResult(contactIntent, 1);
                }
            });


            tvsavetv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                    contactIntent
                            .putExtra(ContactsContract.Intents.Insert.NAME, ContactName)
                            .putExtra(ContactsContract.Intents.Insert.PHONE, Contactnumber);

                    startActivityForResult(contactIntent, 1);
                }
            });

            tvmessagetv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent inten = new Intent(MyCustomDialog.this, ContactDetailsActivity.class);
                    inten.putExtra("name",ContactName);
                    inten.putExtra("number",Contactnumber);
                    inten.putExtra("img",imag);
                    startActivity(inten);
                    finish();
                }
            });
            llmessagell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inten = new Intent(MyCustomDialog.this, ContactDetailsActivity.class);
                    inten.putExtra("name",ContactName);
                    inten.putExtra("number",Contactnumber);
                    inten.putExtra("img",imag);
                    startActivity(inten);
                    finish();
                }
            });

            ivmessageiv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inten = new Intent(MyCustomDialog.this, ContactDetailsActivity.class);
                    inten.putExtra("name",ContactName);
                    inten.putExtra("number",Contactnumber);
                    inten.putExtra("img",imag);
                    startActivity(inten);
                    finish();
                }
            });

            llblack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inten = new Intent(MyCustomDialog.this, UserProfile.class);
                    inten.putExtra("name",ContactName);
                    inten.putExtra("number",Contactnumber);
                    inten.putExtra("img",imag);
                    inten.putExtra("duration","120");
                    startActivity(inten);
                    finish();
                }
            });



            tvblack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inten = new Intent(MyCustomDialog.this, UserProfile.class);
                    inten.putExtra("name",ContactName);
                    inten.putExtra("number",Contactnumber);
                    inten.putExtra("img",imag);
                    inten.putExtra("duration","120");
                    startActivity(inten);
                    finish();
                }
            });
            ivblack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inten = new Intent(MyCustomDialog.this, UserProfile.class);
                    inten.putExtra("name",ContactName);
                    inten.putExtra("number",Contactnumber);
                    inten.putExtra("img",imag);
                    inten.putExtra("duration","120");
                    startActivity(inten);
                    finish();
                }
            });





            lslide = AnimationUtils.loadAnimation(this, R.anim.lslide);
            rslide = AnimationUtils.loadAnimation(this, R.anim.rslide);
            ContactName = getIntent().getExtras().getString("contact_name");
            Contactnumber = getIntent().getExtras().getString("phone_no");
            imag = getIntent().getExtras().getString("img");
            String tag = getIntent().getExtras().getString("tag");
            if (tag.equalsIgnoreCase("NA")){
                ivtag.setVisibility(View.GONE);
            }else {
                ivtag.setVisibility(View.VISIBLE);
            }

            if (imag.equalsIgnoreCase("NA")){
                textlineartext.setVisibility(View.VISIBLE);
                Cphoto.setVisibility(View.GONE);
            }else {
                textlineartext.setVisibility(View.GONE);
                Cphoto.setVisibility(View.VISIBLE);
                Glide.with(MyCustomDialog.this)
                        .load(imag)
                        .centerCrop()
                        .placeholder(R.drawable.manicon)
                        .into(Cphoto);

            }


            String type = getIntent().getExtras().getString("type");
            tvnumber.setText(Contactnumber);
            if(type.equalsIgnoreCase("Ringing")){
                Cname.setText("Incomming call");
                tvfirsttext.setText(String.valueOf(ContactName.charAt(0)));
            }else if(type.equalsIgnoreCase("Missed")){
                tvfirsttext.setText(String.valueOf(Contactnumber.charAt(0)));
                Cname.setText("Missed call less then 1 minutes ago");
            }
            id = getIntent().getExtras().getString("id");
            try{
                if(id!=null) {
                    cid = Long.parseLong(id);
                    retrieveContactPhoto(cid);
                }

            }catch (NumberFormatException e){
                System.out.println("nfe");
            }
            if(ContactName==null){
//                Cname.setText("Incomming call");
                Cnumber.setText(Contactnumber);
            }
            else{
//                Cname.setText(""+ContactName +" is calling you");
                Cnumber.setText(ContactName);
            }





        }
        catch (Exception e)
        {
            Log.d("Exception", e.toString());
            e.printStackTrace();
        }
        card = (RelativeLayout)findViewById(R.id.card);

    }


    private void refershAds() {
        refereshbutton.setEnabled(false);
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_advanced_ad_unit_id));
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if(nativeAd!=null){
                    nativeAd = unifiedNativeAd;
                }
                CardView cardView= findViewById(R.id.ad_container);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout,null);
                populateNativeAd(unifiedNativeAd, adView);
                cardView.removeAllViews();
                cardView.addView(adView);
                refereshbutton.setEnabled(false);
                mpopuplinear.setVisibility(View.VISIBLE);
            }
        });
        AdLoader adLoader = builder.withAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i){
                refereshbutton.setEnabled(true);
                Toast.makeText(MyCustomDialog.this, "Failed to load ad", Toast.LENGTH_SHORT).show();
                super.onAdFailedToLoad(i);
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onDestroy() {
        if (nativeAd!=null){
            nativeAd.destroy();
        }
        super.onDestroy();

    }

    private void populateNativeAd(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView){

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setBodyView(adView.findViewById(R.id.ad_body_text));
        adView.setStarRatingView(adView.findViewById(R.id.star_rating));
        adView.setMediaView((MediaView) adView.findViewById(R.id.media_view));
        adView.setCallToActionView(adView.findViewById(R.id.add_call_to_action));
        adView.setIconView(adView.findViewById(R.id.adv_icon));


        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        ((TextView)adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if(nativeAd.getBody()==null){
            adView.getBodyView().setVisibility(View.INVISIBLE);
        }else {
            ((TextView)adView.getHeadlineView()).setText(nativeAd.getBody());
            adView.getBodyView().setVisibility(View.VISIBLE);
        }
        if(nativeAd.getAdvertiser()==null){

            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        }else {
            ((TextView)adView.getHeadlineView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }




        if(nativeAd.getStarRating()==null){

            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        }else {
            ((RatingBar)adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }





        if(nativeAd.getIcon()==null){

            adView.getIconView().setVisibility(View.GONE);
        }else {
            ((ImageView)adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }




        if(nativeAd.getCallToAction()==null){

            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        }else {
            ((Button)adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        }



        adView.setNativeAd(nativeAd);
    }

    private void initializeContent()
    {
        Cname  = (TextView) findViewById(R.id.Cname);
        Cnumber  = (TextView) findViewById(R.id.Cnumber);
        Cphoto = (ImageView) findViewById(R.id.contactPhoto);
    }
    private void retrieveContactPhoto(long contactID) {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactID));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                Cphoto.setImageBitmap(photo);
            }

            if(inputStream != null)
                inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//
//        wind = this.getWindow();
//        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//        wind.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        wind.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);

    }


}
