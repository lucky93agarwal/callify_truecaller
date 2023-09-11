package com.gpslab.kaun.Notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gpslab.kaun.Details.ContactDetailsActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.block.AsyncExecutorUtil;
import com.gpslab.kaun.block.BlockedNumber;
import com.gpslab.kaun.block.BlockedNumberDao;
import com.gpslab.kaun.block.BlockedNumberDatabase;
import com.gpslab.kaun.block.BlockedNumberType;
import com.gpslab.kaun.popup.simselectpopup;
import com.gpslab.kaun.view.ChatActivity;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Optional;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpamUserDetialActivity extends AppCompatActivity {
    public TextView tvuser, tvmobile, tvfirstlater, tvemail;
    public String UserName;
    public CircleImageView cimage;
    public LinearLayout mlinear;
    public String img;
    public LinearLayout mpopuplinear;
    private static simselectpopup defaultpopupnew;
    public Button refereshbutton;
    public UnifiedNativeAd nativeAd;


    public LinearLayout llcon;
    public TextView tvcon;
    public String nMobile;
    public ImageView ivcon;




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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static final String LAYOUT_ID_KEY = "layout";
    public static final String DIALOG_TYPE = "dialogType";
    public static final String TITLE = "title";
    private Window window;
    private int brightness = 200;




    private void ScreenViewControl() {
        //       Get the current system brightness
        window = SpamUserDetialActivity.this.getWindow();
        WindowManager.LayoutParams layoutpars = window.getAttributes();
        layoutpars.screenBrightness = brightness / (float) 255;
        window.setAttributes(layoutpars);
    }

    public String Name, Image;
    public LinearLayout historyll, historyllll, historyllllll, llchat, llmess, llblock;
    public ImageView historyiv, blockiv, ivchat, ivmess, ivblock;
    public TextView historytv, blocktv, tvlastcall, tvchat, tvmess, tvblock;

    public String TypeCheck = "";







    public void bottompopup() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SpamUserDetialActivity.this, R.style.BottomSheetDialogTheem);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.blocklayout, (LinearLayout) findViewById(R.id.bottomSheetContainerBlock));
        ImageView ivsales = bottomSheetView.findViewById(R.id.salesiv);
        TextView tvsales = bottomSheetView.findViewById(R.id.salestv);
        LinearLayout llsales = bottomSheetView.findViewById(R.id.salesll);


        RadioGroup radiotyperadio = bottomSheetView.findViewById(R.id.typeradio);


        TextView tvnametvtv = bottomSheetView.findViewById(R.id.tvnametv);
        tvnametvtv.setText("Block " + Name);


        LinearLayout llsalesback = bottomSheetView.findViewById(R.id.salesivback);


        ImageView ivscam = bottomSheetView.findViewById(R.id.scamiv);
        TextView tvscam = bottomSheetView.findViewById(R.id.scamtv);

        LinearLayout llscam = bottomSheetView.findViewById(R.id.scamll);
        LinearLayout llscanback = bottomSheetView.findViewById(R.id.Scamivback);
        LinearLayout lldonationllback = bottomSheetView.findViewById(R.id.donationllback);

        LinearLayout llotherllback = bottomSheetView.findViewById(R.id.otherllback);


        llsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llsalesback.setBackgroundResource(R.drawable.block_icon_background);
                llscanback.setBackgroundResource(R.drawable.block_white_icon_background);
                lldonationllback.setBackgroundResource(R.drawable.block_white_icon_background);
                llotherllback.setBackgroundResource(R.drawable.block_white_icon_background);

                TypeCheck = "Sales";
            }
        });
        ivsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llsalesback.setBackgroundResource(R.drawable.block_icon_background);
                llscanback.setBackgroundResource(R.drawable.block_white_icon_background);
                lldonationllback.setBackgroundResource(R.drawable.block_white_icon_background);
                llotherllback.setBackgroundResource(R.drawable.block_white_icon_background);

                TypeCheck = "Sales";
            }
        });
        tvsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llsalesback.setBackgroundResource(R.drawable.block_icon_background);
                llscanback.setBackgroundResource(R.drawable.block_white_icon_background);
                lldonationllback.setBackgroundResource(R.drawable.block_white_icon_background);
                llotherllback.setBackgroundResource(R.drawable.block_white_icon_background);

                TypeCheck = "Sales";
            }
        });


        llscam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llscanback.setBackgroundResource(R.drawable.block_icon_background);

                llsalesback.setBackgroundResource(R.drawable.block_white_icon_background);
                lldonationllback.setBackgroundResource(R.drawable.block_white_icon_background);
                llotherllback.setBackgroundResource(R.drawable.block_white_icon_background);


                TypeCheck = "Scam";
            }
        });
        ivscam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llscanback.setBackgroundResource(R.drawable.block_icon_background);
                llsalesback.setBackgroundResource(R.drawable.block_white_icon_background);
                lldonationllback.setBackgroundResource(R.drawable.block_white_icon_background);
                llotherllback.setBackgroundResource(R.drawable.block_white_icon_background);


                TypeCheck = "Scam";
            }
        });
        tvscam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llscanback.setBackgroundResource(R.drawable.block_icon_background);
                llsalesback.setBackgroundResource(R.drawable.block_white_icon_background);
                lldonationllback.setBackgroundResource(R.drawable.block_white_icon_background);
                llotherllback.setBackgroundResource(R.drawable.block_white_icon_background);


                TypeCheck = "Scam";
            }
        });


        ImageView ivdonation = bottomSheetView.findViewById(R.id.donationiv);
        TextView tvdonation = bottomSheetView.findViewById(R.id.donationtv);
        LinearLayout lldonation = bottomSheetView.findViewById(R.id.donationll);

        lldonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lldonationllback.setBackgroundResource(R.drawable.block_icon_background);
                llscanback.setBackgroundResource(R.drawable.block_white_icon_background);
                llsalesback.setBackgroundResource(R.drawable.block_white_icon_background);
                llotherllback.setBackgroundResource(R.drawable.block_white_icon_background);


                TypeCheck = "Donation";
            }
        });
        ivdonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lldonationllback.setBackgroundResource(R.drawable.block_icon_background);
                llscanback.setBackgroundResource(R.drawable.block_white_icon_background);
                llsalesback.setBackgroundResource(R.drawable.block_white_icon_background);
                llotherllback.setBackgroundResource(R.drawable.block_white_icon_background);


                TypeCheck = "Donation";
            }
        });
        tvdonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lldonationllback.setBackgroundResource(R.drawable.block_icon_background);
                llscanback.setBackgroundResource(R.drawable.block_white_icon_background);
                llsalesback.setBackgroundResource(R.drawable.block_white_icon_background);
                llotherllback.setBackgroundResource(R.drawable.block_white_icon_background);


                TypeCheck = "Donation";
            }
        });
        ImageView ivother = bottomSheetView.findViewById(R.id.ivotheriv);
        TextView tvother = bottomSheetView.findViewById(R.id.tvothertv);


        LinearLayout llother = bottomSheetView.findViewById(R.id.llotherll);

        llother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llotherllback.setBackgroundResource(R.drawable.block_icon_background);

                lldonationllback.setBackgroundResource(R.drawable.block_white_icon_background);
                llscanback.setBackgroundResource(R.drawable.block_white_icon_background);
                llsalesback.setBackgroundResource(R.drawable.block_white_icon_background);

                TypeCheck = "Other";
            }
        });

        ivother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llotherllback.setBackgroundResource(R.drawable.block_icon_background);

                lldonationllback.setBackgroundResource(R.drawable.block_white_icon_background);
                llscanback.setBackgroundResource(R.drawable.block_white_icon_background);
                llsalesback.setBackgroundResource(R.drawable.block_white_icon_background);

                TypeCheck = "Other";
            }
        });
        tvother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llotherllback.setBackgroundResource(R.drawable.block_icon_background);

                lldonationllback.setBackgroundResource(R.drawable.block_white_icon_background);
                llscanback.setBackgroundResource(R.drawable.block_white_icon_background);
                llsalesback.setBackgroundResource(R.drawable.block_white_icon_background);


                TypeCheck = "Other";
            }
        });

        TextView tvcancel = bottomSheetView.findViewById(R.id.clancelbtn);
        tvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        TextView tvblock = bottomSheetView.findViewById(R.id.blockbtn);
        tvblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radiotyperadio.getCheckedRadioButtonId();
                RadioButton radioSexButton = (RadioButton) bottomSheetView.findViewById(selectedId);
                Log.d("BlockUserProfile", "Radio   =   " + radioSexButton.getText() + "   Type   =    " + TypeCheck);

                SpamUserDetialActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        tvblock.setText("UNBLOCK");
                    }
                });
                bottomSheetDialog.dismiss();


//                if(nMobile.substring(0,3).equalsIgnoreCase("+91")){
//                    Log.d("LOG_TAG","Check  =   Number +91 hai");
//                }else {
//                    Log.d("LOG_TAG","Check  =   Number +91 nhi hai");
//                }

                //// block any number
                try {
                    BlockedNumberType type = BlockedNumberType.EXACT_MATCH;
                    BlockedNumber blockedNumber = new BlockedNumber(type, "+91", nMobile,UserName);
                    AsyncExecutorUtil.getInstance().getExecutor().execute(() -> blockedNumberDao.insert(blockedNumber));
                    Log.i("LOG_TAG", String.format("Added valid number: %s", blockedNumber));
                } catch (IllegalArgumentException e) {
                    Log.i("LOG_TAG", String.format("Tried to add invalid number: %s", nMobile));

                }
                //// block any number
            }
        });


        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private TextView tvspamcount,tvcallinginaday,tvreport_count_tv,tvreport_persentage_tv,tvusually_call_tv;
    private BlockedNumberDao blockedNumberDao;
    public LinearLayout mCheckMobileLl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spam_user_detial);

        tvspamcount = (TextView) findViewById(R.id.spam_count_tv);
        tvspamcount.setText(getIntent().getStringExtra("spam_count")+ " spame reports");




        tvcallinginaday = (TextView)findViewById(R.id.callinginaday);

        tvreport_persentage_tv = (TextView)findViewById(R.id.report_persentage_tv);
        tvreport_count_tv = (TextView)findViewById(R.id.report_count_tv);
        tvusually_call_tv = (TextView)findViewById(R.id.usually_call_tv);

        mCheckMobileLl = (LinearLayout)findViewById(R.id.check_phone_number);
        blockedNumberDao = BlockedNumberDatabase.getInstance(SpamUserDetialActivity.this).blockedNumberDao();
        llblock = (LinearLayout) findViewById(R.id.llblockll);
        ivblock = (ImageView) findViewById(R.id.ivblockiv);
        tvblock = (TextView) findViewById(R.id.tvblocktv);

        tvblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nMobile.substring(0, 3).equalsIgnoreCase("+91")) {
                    Log.d("LOG_TAG", "Check  =   Number +91 hai");
                    CheckNumber = nMobile;
                } else {
                    CheckNumber = "+91" + nMobile;
                    Log.d("LOG_TAG", "Check  =   Number +91 nhi hai");
                }


                BlockedNumberDao blockedNumberDao = BlockedNumberDatabase.getInstance(SpamUserDetialActivity.this).blockedNumberDao();
                AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
                    Optional<BlockedNumber> match = blockedNumberDao.getAll().stream().filter(blockedNumber -> blockedNumber.getRegex().matcher(CheckNumber).find()).findAny();
                    if (!match.isPresent()) {
                        Log.d("LOG_TAG", "No blocked number matched");
//                        tvblock.setText("BLOCK");

                        SpamUserDetialActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                bottompopup();
                            }
                        });

                        return;
                    } else {
                        AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
                            List<BlockedNumber> blockedNumberList = blockedNumberDao.getAll();
                            Log.d("WalleListSize", "Size ==  " + blockedNumberList.size());
                            for (int i = 0; i < blockedNumberList.size(); i++) {
                                final BlockedNumber number = blockedNumberList.get(i);
                                String Snumber = number.toFormattedString();

                                String CheckNum = CheckNumber.substring(CheckNumber.length() - 10);
                                Snumber = Snumber.replace(" ", "");
                                Log.d("WalleListSize", "Number ==  " + Snumber);
                                Log.d("WalleListSize", "Orignal Number ==  " + CheckNum);
                                if (Snumber.equalsIgnoreCase(CheckNum)) {
                                    Log.d("WalleListSize", "Match Number ==  ");
                                    unblockdialog(number);
                                }

                            }

                        });


                        Log.d("LOG_TAG", "Yes blocked number matched");
                        tvblock.setText("UNBLOCK");
                    }
                    Log.i("LOG_TAG", String.format("Blocked number matched: %s", match.get().toFormattedString()));

                    TelecomManager telecomManager = (TelecomManager) SpamUserDetialActivity.this.getSystemService(Context.TELECOM_SERVICE);

                    telecomManager.endCall();


                    // TODO Some UI
                });


            }
        });
        ivblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bottompopup();
                if (nMobile.substring(0, 3).equalsIgnoreCase("+91")) {
                    Log.d("LOG_TAG", "Check  =   Number +91 hai");
                    CheckNumber = nMobile;
                } else {
                    CheckNumber = "+91" + nMobile;
                    Log.d("LOG_TAG", "Check  =   Number +91 nhi hai");
                }

                BlockedNumberDao blockedNumberDao = BlockedNumberDatabase.getInstance(SpamUserDetialActivity.this).blockedNumberDao();
                AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
                    Optional<BlockedNumber> match = blockedNumberDao.getAll().stream().filter(blockedNumber -> blockedNumber.getRegex().matcher(CheckNumber).find()).findAny();
                    if (!match.isPresent()) {
                        Log.d("LOG_TAG", "No blocked number matched");
//                        tvblock.setText("BLOCK");

                        SpamUserDetialActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                bottompopup();
                            }
                        });

                        return;
                    } else {
                        AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
                            List<BlockedNumber> blockedNumberList = blockedNumberDao.getAll();
                            Log.d("WalleListSize", "Size ==  " + blockedNumberList.size());
                            for (int i = 0; i < blockedNumberList.size(); i++) {
                                final BlockedNumber number = blockedNumberList.get(i);
                                String Snumber = number.toFormattedString();

                                String CheckNum = CheckNumber.substring(CheckNumber.length() - 10);
                                Snumber = Snumber.replace(" ", "");
                                Log.d("WalleListSize", "Number ==  " + Snumber);
                                Log.d("WalleListSize", "Orignal Number ==  " + CheckNum);
                                if (Snumber.equalsIgnoreCase(CheckNum)) {
                                    Log.d("WalleListSize", "Match Number ==  ");
                                    unblockdialog(number);
                                }

                            }

                        });
                        Log.d("LOG_TAG", "Yes blocked number matched");
//                        tvblock.setText("UNBLOCK");
                    }
                    Log.i("LOG_TAG", String.format("Blocked number matched: %s", match.get().toFormattedString()));

                    TelecomManager telecomManager = (TelecomManager) SpamUserDetialActivity.this.getSystemService(Context.TELECOM_SERVICE);

                    telecomManager.endCall();


                    // TODO Some UI
                });
            }
        });
        llblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bottompopup();
                if (nMobile.substring(0, 3).equalsIgnoreCase("+91")) {
                    Log.d("LOG_TAG", "Check  =   Number +91 hai");
                    CheckNumber = nMobile;
                } else {
                    CheckNumber = "+91" + nMobile;
                    Log.d("LOG_TAG", "Check  =   Number +91 nhi hai");
                }

                BlockedNumberDao blockedNumberDao = BlockedNumberDatabase.getInstance(SpamUserDetialActivity.this).blockedNumberDao();
                AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
                    Optional<BlockedNumber> match = blockedNumberDao.getAll().stream().filter(blockedNumber -> blockedNumber.getRegex().matcher(CheckNumber).find()).findAny();
                    if (!match.isPresent()) {
                        Log.d("LOG_TAG", "No blocked number matched");
//                        tvblock.setText("BLOCK");
                        SpamUserDetialActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                bottompopup();
                            }
                        });


                        return;
                    } else {
                        AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
                            List<BlockedNumber> blockedNumberList = blockedNumberDao.getAll();
                            Log.d("WalleListSize", "Size ==  " + blockedNumberList.size());
                            for (int i = 0; i < blockedNumberList.size(); i++) {
                                final BlockedNumber number = blockedNumberList.get(i);
                                String Snumber = number.toFormattedString();

                                String CheckNum = CheckNumber.substring(CheckNumber.length() - 10);
                                Snumber = Snumber.replace(" ", "");
                                Log.d("WalleListSize", "Number ==  " + Snumber);
                                Log.d("WalleListSize", "Orignal Number ==  " + CheckNum);
                                if (Snumber.equalsIgnoreCase(CheckNum)) {
                                    Log.d("WalleListSize", "Match Number ==  ");
                                    unblockdialog(number);
                                }

                            }

                        });
                        Log.d("LOG_TAG", "Yes blocked number matched");
                        tvblock.setText("UNBLOCK");
                    }
                    Log.i("LOG_TAG", String.format("Blocked number matched: %s", match.get().toFormattedString()));

                    TelecomManager telecomManager = (TelecomManager) SpamUserDetialActivity.this.getSystemService(Context.TELECOM_SERVICE);

                    telecomManager.endCall();


                    // TODO Some UI
                });
            }
        });


        llchat = (LinearLayout) findViewById(R.id.chatll);
        ivchat = (ImageView) findViewById(R.id.chativ);
        tvchat = (TextView) findViewById(R.id.chattv);

        llmess = (LinearLayout) findViewById(R.id.massll);
        ivmess = (ImageView) findViewById(R.id.massiv);
        tvmess = (TextView) findViewById(R.id.masstv);


        llmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(SpamUserDetialActivity.this, ContactDetailsActivity.class);
                inten.putExtra("name", Name);
                inten.putExtra("number", nMobile);
                inten.putExtra("img", Image);
                startActivity(inten);
            }
        });
        ivmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(SpamUserDetialActivity.this, ContactDetailsActivity.class);
                inten.putExtra("name", Name);
                inten.putExtra("number", nMobile);
                inten.putExtra("img", Image);
                startActivity(inten);
            }
        });
        tvmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(SpamUserDetialActivity.this, ContactDetailsActivity.class);
                inten.putExtra("name", Name);
                inten.putExtra("number", nMobile);
                inten.putExtra("img", Image);
                startActivity(inten);
            }
        });
        Name = getIntent().getStringExtra("name");
        Image = getIntent().getStringExtra("img");

        llchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("ChatData",0);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("chat_id",nMobile);
                edit.apply();
                Intent intent = new Intent(SpamUserDetialActivity.this, ChatActivity.class);
                intent.putExtra("image",Image);
                intent.putExtra("name",Name);
                intent.putExtra("id",nMobile);
                startActivity(intent);
            }
        });
        tvchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("ChatData",0);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("chat_id",nMobile);
                edit.apply();
                Intent intent = new Intent(SpamUserDetialActivity.this, ChatActivity.class);
                intent.putExtra("image",Image);
                intent.putExtra("name",Name);
                intent.putExtra("id",nMobile);
                startActivity(intent);
            }
        });

        ivchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("ChatData",0);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("chat_id",nMobile);
                edit.apply();
                Intent intent = new Intent(SpamUserDetialActivity.this, ChatActivity.class);
                intent.putExtra("image",Image);
                intent.putExtra("name",Name);
                intent.putExtra("id",nMobile);
                startActivity(intent);
            }
        });


        historyiv = (ImageView) findViewById(R.id.ivhistory);
        historytv = (TextView) findViewById(R.id.tvhistory);


        tvlastcall = (TextView) findViewById(R.id.lastcalltv);
        String sec = getIntent().getStringExtra("duration");
        if (Integer.valueOf(sec) > 60) {
            int ssec = Integer.valueOf(sec) / 60;
            tvlastcall.setText("Duration of last call " + String.valueOf(ssec).substring(0, 1) + "m " + String.valueOf(ssec).substring(0, 1) + "s");
        } else {
            tvlastcall.setText("Duration of last call " + getIntent().getStringExtra("duration") + "s");
        }


        historyll = (LinearLayout) findViewById(R.id.llhistory);
        historyllll = (LinearLayout) findViewById(R.id.llllhistory);
        historyllllll = (LinearLayout) findViewById(R.id.llllllhistory);

        historyiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        historytv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        historyll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        historyllll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        historyllllll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        defaultpopupnew = new simselectpopup(SpamUserDetialActivity.this);
        nMobile = getIntent().getStringExtra("number");

        if(nMobile.length()>1){
            mCheckMobileLl.setVisibility(View.VISIBLE);
        }else {
            mCheckMobileLl.setVisibility(View.GONE);
        }

        llcon = (LinearLayout) findViewById(R.id.callingll);
        tvcon = (TextView) findViewById(R.id.callingtv);
        ivcon = (ImageView) findViewById(R.id.callingiv);
        ivcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultpopupnew.defaultaddpopup(nMobile);
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + nMobile));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("com.android.phone.force.slot", true);
//                intent.putExtra("Cdma_Supp", true);
//                //Add all slots here, according to device.. (different device require different key so put all together)
//                for (String s : simSlotName)
//                    intent.putExtra(s, 0); //0 or 1 according to sim.......
//
//                //works only for API >= 21
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                    intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", " here You have to get phone account handle list by using telecom manger for both sims:- using this method getCallCapablePhoneAccounts()");
//
//                startActivity(intent);
            }
        });


        findViewById(R.id.backiv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultpopupnew.defaultaddpopup(nMobile);
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + nMobile));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("com.android.phone.force.slot", true);
//                intent.putExtra("Cdma_Supp", true);
//                //Add all slots here, according to device.. (different device require different key so put all together)
//                for (String s : simSlotName)
//                    intent.putExtra(s, 0); //0 or 1 according to sim.......
//
//                //works only for API >= 21
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                    intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", " here You have to get phone account handle list by using telecom manger for both sims:- using this method getCallCapablePhoneAccounts()");
//
//                startActivity(intent);
            }
        });


        llcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultpopupnew.defaultaddpopup(nMobile);
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + nMobile));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("com.android.phone.force.slot", true);
//                intent.putExtra("Cdma_Supp", true);
//                //Add all slots here, according to device.. (different device require different key so put all together)
//                for (String s : simSlotName)
//                    intent.putExtra(s, 0); //0 or 1 according to sim.......
//
//                //works only for API >= 21
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                    intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", " here You have to get phone account handle list by using telecom manger for both sims:- using this method getCallCapablePhoneAccounts()");
//
//                startActivity(intent);
            }
        });


        mpopuplinear = (LinearLayout) findViewById(R.id.newlinearpopup);
        refereshbutton = findViewById(R.id.btn_refersh);
        refereshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        refershAds();


        tvfirstlater = (TextView) findViewById(R.id.firsttexttv);
        mlinear = (LinearLayout) findViewById(R.id.llone);
        cimage = (CircleImageView) findViewById(R.id.image);
        SharedPreferences sharedPreferences = getSharedPreferences("newdata", Context.MODE_PRIVATE);

        tvfirstlater.setText(sharedPreferences.getString("name", ""));


//        if(sharedPreferences.getString("image","").equalsIgnoreCase("NA")){
//            cimage.setVisibility(View.GONE);
//            mlinear.setVisibility(View.VISIBLE);
//
//        }else {
//            cimage.setVisibility(View.VISIBLE);
//            mlinear.setVisibility(View.GONE);
//            Glide.with(UserProfile.this)
//                    .load(sharedPreferences.getString("image",""))
//                    .centerCrop()
//                    .placeholder(R.drawable.profile)
//                    .into(cimage);
//        }


        img = getIntent().getStringExtra("img");
        Log.d("WalletgetServiceNew", "image userprofile  =  " + img);

        if (img != null) {
            if (img.equalsIgnoreCase("NA")) {
                cimage.setVisibility(View.GONE);
                mlinear.setVisibility(View.VISIBLE);
            } else {
                cimage.setVisibility(View.VISIBLE);
                mlinear.setVisibility(View.GONE);
                Glide.with(SpamUserDetialActivity.this)
                        .load(img)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(cimage);
            }
        } else {
            cimage.setVisibility(View.GONE);
            mlinear.setVisibility(View.VISIBLE);
        }
        tvemail = (TextView) findViewById(R.id.emailtv);


        tvuser = (TextView) findViewById(R.id.usernametv);

        tvemail.setText(sharedPreferences.getString("email", ""));
        UserName = getIntent().getStringExtra("name");
        Log.d("WalletgetServiceNew", "UserName userprofile  =  " + UserName);
        tvuser.setText(UserName);
//        Log.d("WalletLucky", "onNewMessage = Name 1  "+UserName.charAt(0));
        tvfirstlater.setText(String.valueOf(UserName.charAt(0)).toUpperCase());
        tvmobile = (TextView) findViewById(R.id.mobiletv);
        tvmobile.setText(getIntent().getStringExtra("number"));
        Log.d("WalletgetServiceNew", "number userprofile  =  " + getIntent().getStringExtra("number"));
        Log.d("WalletLucky", "onNewMessage = MobileNos 1  " + sharedPreferences.getString("mobile", ""));


        CheckBlockNumber();


    }















    private void unblockdialog(BlockedNumber number) {
        removeNumber(number);

//        this.runOnUiThread(new Runnable() {
//            public void run() {
//                AlertDialog alertDialog = new AlertDialog.Builder(UserProfile.this).create();
//                alertDialog.setTitle("Alert");
//                alertDialog.setMessage("Are you sure you want to unblock this number?");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CANCEL",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "UNBLOCK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.show();
//            }
//        });


    }

    public void removeNumber(final BlockedNumber number) {

        blockedNumberDao.delete(number);



        SpamUserDetialActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                tvblock.setText("BLOCK");
            }
        });

    }

    public String CheckNumber;

    private void CheckBlockNumber() {
        if(nMobile !=null) {

            if(nMobile.length() >3) {

                if (nMobile.substring(0, 3).equalsIgnoreCase("+91")) {
                    Log.d("LOG_TAG", "Check  =   Number +91 hai");
                    CheckNumber = nMobile;
                } else {
                    CheckNumber = "+91" + nMobile;
                    Log.d("LOG_TAG", "Check  =   Number +91 nhi hai");
                }
                BlockedNumberDao blockedNumberDao = BlockedNumberDatabase.getInstance(SpamUserDetialActivity.this).blockedNumberDao();
                AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
                    Optional<BlockedNumber> match = blockedNumberDao.getAll().stream().filter(blockedNumber -> blockedNumber.getRegex().matcher(CheckNumber).find()).findAny();
                    if (!match.isPresent()) {
                        Log.d("LOG_TAG", "No blocked number matched");
                        tvblock.setText("BLOCK");
                        return;
                    } else {
                        Log.d("LOG_TAG", "Yes blocked number matched");
                        tvblock.setText("UNBLOCK");
                    }
                    Log.i("LOG_TAG", String.format("Blocked number matched: %s", match.get().toFormattedString()));

                    TelecomManager telecomManager = (TelecomManager) SpamUserDetialActivity.this.getSystemService(Context.TELECOM_SERVICE);

                    telecomManager.endCall();


                    // TODO Some UI
                });
            }
        }
    }


    private void refershAds() {
        refereshbutton.setEnabled(false);
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_advanced_ad_unit_id));
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if (nativeAd != null) {
                    nativeAd = unifiedNativeAd;
                }
                CardView cardView = findViewById(R.id.ad_container);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout, null);
                populateNativeAd(unifiedNativeAd, adView);
                cardView.removeAllViews();
                cardView.addView(adView);
                refereshbutton.setEnabled(false);
                mpopuplinear.setVisibility(View.VISIBLE);
            }
        });
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                refereshbutton.setEnabled(true);
                Toast.makeText(SpamUserDetialActivity.this, "Failed to load ad", Toast.LENGTH_SHORT).show();
                super.onAdFailedToLoad(i);
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();

    }

    private void populateNativeAd(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setBodyView(adView.findViewById(R.id.ad_body_text));
        adView.setStarRatingView(adView.findViewById(R.id.star_rating));
        adView.setMediaView((MediaView) adView.findViewById(R.id.media_view));
        adView.setCallToActionView(adView.findViewById(R.id.add_call_to_action));
        adView.setIconView(adView.findViewById(R.id.adv_icon));


        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getBody());
            adView.getBodyView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getAdvertiser() == null) {

            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getStarRating() == null) {

            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getIcon() == null) {

            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getCallToAction() == null) {

            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        }


        adView.setNativeAd(nativeAd);
    }
}