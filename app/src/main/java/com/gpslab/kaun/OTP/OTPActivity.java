package com.gpslab.kaun.OTP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.Login.LoginActivity;
import com.gpslab.kaun.Login.LoginSecActivity;
import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.Phone.PhoneActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.Receiver.IncomingSms;
import com.gpslab.kaun.Receiver.SmsListener;
import com.gpslab.kaun.ResURls;

import com.gpslab.kaun.Service.GetContactsService;
import com.gpslab.kaun.Webapi.createnew;
import com.gpslab.kaun.activity.PreferencesActivity;
import com.gpslab.kaun.check.CheckActivity;
import com.gpslab.kaun.internetcheck.InternetActivity;
import com.gpslab.kaun.privacyActivity;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit_model.AddUserResponseJson;
import com.gpslab.kaun.retrofit_model.VerifyUserRequestJson;
import com.gpslab.kaun.retrofit_model.VerifyUserResponseJson;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {
    public LinearLayout contiLinear;
    public SharedPreferences sharedPreferences;
    private CountDownTimer countDownTimer;
    private FirebaseAuth mAuth;
    public SharedPreferences.Editor editor;
    public TextView secnd, resend;
    public String FullName, Mobile, Email, Password, City, Area, Society, Tower, Flat, Flor;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public EditText etotp, etone, ettwo, etthree, etfour, etfive, etsix;
    String reserver = "";
    public Button contiText;
    private String mVerificationId, Message;
    String resTxt = null;
    private static createnew addpopup;
    public SharedPreferences.Editor edit;
    public TextView tvnote;

    public SharedPreferences sharedPreferencesxx;
    private String verificationId;
    private static final String KEY_VERIFICATION_ID = "key_verification_id";
    //// progress bar
    public AVLoadingIndicatorView progressbar;
    public MyDbHandler myDbHandler;


    public String CountryCode = "";
    public String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        tvnote = (TextView) findViewById(R.id.note);


        if (verificationId == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {

        } else {
            Intent intentnew = new Intent(OTPActivity.this, InternetActivity.class);
            intentnew.putExtra("msg", "No Internet");
            startActivity(intentnew);
        }


        etone = (EditText) findViewById(R.id.oneet);
        ettwo = (EditText) findViewById(R.id.twoet);
        etthree = (EditText) findViewById(R.id.threeet);
        etfour = (EditText) findViewById(R.id.fouret);
        etfive = (EditText) findViewById(R.id.fiveet);
        etsix = (EditText) findViewById(R.id.sixet);
        myDbHandler = new MyDbHandler(getApplicationContext(), "userbd", null, 1);
        Temp.setMyDbHandler(myDbHandler);


        etone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etone.clearFocus();
                ettwo.requestFocus();
                ettwo.setCursorVisible(true);
            }
        });
        ettwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ettwo.clearFocus();
                etthree.requestFocus();
                etthree.setCursorVisible(true);
            }
        });
        etthree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etthree.clearFocus();
                etfour.requestFocus();
                etfour.setCursorVisible(true);
            }
        });
        etfour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etfour.clearFocus();
                etfive.requestFocus();
                etfive.setCursorVisible(true);
            }
        });

        etfive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etfive.clearFocus();
                etsix.requestFocus();
                etsix.setCursorVisible(true);
            }
        });


        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.buttonanimation);
        sharedPreferencesxx = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferencesxx.edit();
        Token = sharedPreferencesxx.getString("token", "");
        sharedPreferences = getSharedPreferences("DataBase", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        contiText = (Button) findViewById(R.id.continuetext);
        secnd = (TextView) findViewById(R.id.second);
        resend = (TextView) findViewById(R.id.resend);
        contiLinear = (LinearLayout) findViewById(R.id.continuelinear);
        findViewById(R.id.ivleftarrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OTPActivity.this, PhoneActivity.class);
                startActivity(intent);
                finish();
            }
        });


        progressbar = (AVLoadingIndicatorView) findViewById(R.id.avi);


        etotp = (EditText) findViewById(R.id.fullname);


        Mobile = getIntent().getStringExtra("phone");
        CountryCode = getIntent().getStringExtra("code");

        Log.d("CheckData", "Mobile = = " + Mobile);
        Log.d("CCpWallet", "CCP = " + CountryCode);

        //// firebase
//        mAuth = FirebaseAuth.getInstance();
//        sendVerificationCode(Mobile);
//        progressbar.setVisibility(View.VISIBLE);
        // firebase

        MyMessage();


        countDownTimer();

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(myAnim);
                sendVerificationCode(Mobile);
            }
        });

        contiLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(myAnim);
                if (etone.getText().length() == 0) {
                    Toast.makeText(OTPActivity.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();
                } else if (ettwo.getText().length() == 0) {
                    Toast.makeText(OTPActivity.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();
                } else if (etthree.getText().length() == 0) {
                    Toast.makeText(OTPActivity.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();
                } else if (etfour.getText().length() == 0) {
                    Toast.makeText(OTPActivity.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();
                } else if (etfive.getText().length() == 0) {
                    Toast.makeText(OTPActivity.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();
                } else if (etsix.getText().length() == 0) {
                    Toast.makeText(OTPActivity.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();
                } else {

//                    String code = etotp.getText().toString().trim();
                    String code = etone.getText().toString().trim() + ettwo.getText().toString().trim() + etthree.getText().toString().trim() + etfour.getText().toString().trim() + etfive.getText().toString().trim() + etsix.getText().toString().trim();
//                    verifyVerificationCode(code);
                    Log.d("CCpWallet", "code write = " + code);
                    Log.d("CCpWallet", "code genrate = " + CodeNo);
                    if (CodeNo.equalsIgnoreCase(code)) {


                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                            retrofit();
                        } else {
                            Intent intentnew = new Intent(OTPActivity.this, InternetActivity.class);
                            intentnew.putExtra("msg", "No Internet");
                            startActivity(intentnew);
                        }

//                        SendDataToServerss(Mobile);
                    } else {
                        Toast.makeText(OTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    }
//                    Intent i=new Intent(OtpActivity.this, HomeActivity.class);
//                    startActivity(i);
//
//                    //Remove activity
//                    finish();
                }

            }
        });
        contiText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(myAnim);
                if (etone.getText().length() == 0) {
                    Toast.makeText(OTPActivity.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();
                } else if (ettwo.getText().length() == 0) {
                    Toast.makeText(OTPActivity.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();
                } else if (etthree.getText().length() == 0) {
                    Toast.makeText(OTPActivity.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();
                } else if (etfour.getText().length() == 0) {
                    Toast.makeText(OTPActivity.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();
                } else if (etfive.getText().length() == 0) {
                    Toast.makeText(OTPActivity.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();
                } else if (etsix.getText().length() == 0) {
                    Toast.makeText(OTPActivity.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();
                } else {
                    String code = etone.getText().toString().trim() + ettwo.getText().toString().trim() + etthree.getText().toString().trim() + etfour.getText().toString().trim() + etfive.getText().toString().trim() + etsix.getText().toString().trim();
                    Log.d("OTPPage", "Message:  verifyVerificationCode");
                    Log.d("OTPPage", "code write = " + code);
                    Log.d("OTPPage", "code genrate = " + CodeNo);
                    if (CodeNo.equalsIgnoreCase(code)) {


                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                            retrofit();
                        } else {
                            Intent intentnew = new Intent(OTPActivity.this, InternetActivity.class);
                            intentnew.putExtra("msg", "No Internet");
                            startActivity(intentnew);
                        }


//                        SendDataToServerss(Mobile);
                    } else {
                        Toast.makeText(OTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    }

//                    Intent i=new Intent(OtpActivity.this, HomeActivity.class);
//                    startActivity(i);
//
//                    //Remove activity
//                    finish();
                }
            }
        });
        IncomingSms.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String code) {
                Log.d("Text", code);
                Toast.makeText(OTPActivity.this, "Message: " + code, Toast.LENGTH_LONG).show();
//                tvotp.setText(code);
//                OTPs = tvotp.getText().toString().trim();
                Log.d("OTPPage", "OTP R = = " + code);
                if (sharedPreferencesxx.getBoolean("checkotp", true)) {
                    edit.putBoolean("checkotp", false);
                    edit.apply();
                    etone.setText(String.valueOf(code.charAt(0)));
                    ettwo.setText(String.valueOf(code.charAt(1)));
                    etthree.setText(String.valueOf(code.charAt(2)));
                    etfour.setText(String.valueOf(code.charAt(3)));
                    etfive.setText(String.valueOf(code.charAt(4)));
                    etsix.setText(String.valueOf(code.charAt(5)));


                    Log.d("CCpWallet", "code write = " + code);
                    Log.d("CCpWallet", "code genrate = " + CodeNo);


                    if (CodeNo.equalsIgnoreCase(code)) {
//                        SendDataToServerss(Mobile);


                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                            retrofit();
                        } else {
                            Intent intentnew = new Intent(OTPActivity.this, InternetActivity.class);
                            intentnew.putExtra("msg", "No Internet");
                            startActivity(intentnew);
                        }


                    }

                }


//                OtpMatched otpMatched=new OtpMatched();
//                otpMatched.execute();
            }
        });

    }

    public IncomingSms reciver;

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

        reciver = new IncomingSms();
        registerReceiver(reciver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver(reciver);
    }

    public String CodeNo = "0";
    public int number;
    public int simnumber = 0;

    private void MyMessage() {
        Random rnd = new Random();

        number = rnd.nextInt(999999);
//        if((number)>5){
//
//
//            number = rnd.nextInt(999999);
//        }

        CodeNo = String.valueOf(number);
        if (CodeNo.length() == 1) {
            CodeNo = CodeNo + "00000";
        } else if (CodeNo.length() == 2) {
            CodeNo = CodeNo + "0000";
        } else if (CodeNo.length() == 3) {
            CodeNo = CodeNo + "000";
        } else if (CodeNo.length() == 4) {
            CodeNo = CodeNo + "00";
        } else if (CodeNo.length() == 5) {
            CodeNo = CodeNo + "0";
        }

        Log.i("checkknowtwo", "OTP = = " + number);
        SmsManager smsManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {

            SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
            int subId = subscriptionInfoList.get(simnumber).getSubscriptionId();// change index to 1 if you want to get Subscrption Id for slot 1.
            smsManager = SmsManager.getSmsManagerForSubscriptionId(subId);

        } else {
            smsManager = SmsManager.getDefault();
        }

        de.ub0r.android.logg0r.Log.d("WalletLuckynew", "Sim Mobile == == == " + simnumber);


        String myNumber = Mobile;
        String myMsg = CodeNo + " is your verification code.";
        de.ub0r.android.logg0r.Log.d("WalletLuckynew", "Mobile == == == " + myNumber);
        String datanew = myNumber.substring(0, 3);
        if (datanew.equalsIgnoreCase("+91")) {
            String datanews = myNumber.substring(3);
            de.ub0r.android.logg0r.Log.d("WalletLuckynew", "Mobile == == == " + datanews);
            if (datanews.equals("") || myMsg.equals("")) {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                if (TextUtils.isDigitsOnly(datanews)) {
//                SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(datanews, null, myMsg, null, null);
                    Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();

                    PreferenceManager.getDefaultSharedPreferences(this).edit().putString(PreferencesActivity.PREFS_BACKUPLASTTEXT, myMsg).commit();

                } else {
                    Toast.makeText(this, "Please enter the correct number", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (myNumber.equals("") || myMsg.equals("")) {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                if (TextUtils.isDigitsOnly(myNumber)) {
//                SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(myNumber, null, myMsg, null, null);
                    Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();

                    PreferenceManager.getDefaultSharedPreferences(this).edit().putString(PreferencesActivity.PREFS_BACKUPLASTTEXT, myMsg).commit();

                } else {
                    Toast.makeText(this, "Please enter the correct number", Toast.LENGTH_SHORT).show();
                }
            }
        }
        Log.d("WalletLuckynew", "Mobile new == == == " + datanew);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_VERIFICATION_ID, verificationId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        verificationId = savedInstanceState.getString(KEY_VERIFICATION_ID);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    //the callback to detect the verificati on status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
//                etotp.setText(code);
                etone.setText(String.valueOf(code.charAt(0)));
                ettwo.setText(String.valueOf(code.charAt(1)));
                etthree.setText(String.valueOf(code.charAt(2)));
                etfour.setText(String.valueOf(code.charAt(3)));
                etfive.setText(String.valueOf(code.charAt(4)));
                etsix.setText(String.valueOf(code.charAt(5)));

                verifyVerificationCode(code);
                //               Toast.makeText(OTPActivity.this, "Verification Successful", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressbar.setVisibility(View.GONE);
            Log.d("CheckData", "Error = = " + e.getMessage());
            //          Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;

        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


//                            Intent intent = new Intent(OTPActivity.this, LoginSecActivity.class);
//                            intent.putExtra("mobile", Mobile);
//                            startActivity(intent);


                        } else {

//                            progressbar.setVisibility(View.GONE);
                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";
                            Log.d("CheckData", "Error in :  " + task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

//                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
//                            snackbar.setAction("Dismiss", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                            snackbar.show();
                        }
                    }
                });
    }


    public VerifyUserRequestJson request = new VerifyUserRequestJson();

    private void retrofit() {

        request.mobile = CountryCode + Mobile;

        request.token = Token;

        Log.i("OTPPageOTPPage", "nameValuePairs Signup  = " + new Gson().toJson(request));


        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.verify_user(request).enqueue(new Callback<VerifyUserResponseJson>() {
            @Override
            public void onResponse(Call<VerifyUserResponseJson> call, Response<VerifyUserResponseJson> response) {
                Log.i("OTPPageOTPPage", "nameValuePairs code  = " + response.code());
                if (response.isSuccessful()) {


                    String results = response.body().response.get(0).result;
                    if (results.equalsIgnoreCase("0")) {

                        edit.putString("fname", response.body().response.get(0).data.get(0).fname);
                        edit.putString("lname", response.body().response.get(0).data.get(0).lname);
                        edit.putString("email", response.body().response.get(0).data.get(0).email);
                        edit.putString("mobile", CountryCode + Mobile);
                        edit.putString("id", response.body().response.get(0).data.get(0).id);
                        edit.putString("img", response.body().response.get(0).data.get(0).profile_image);


//                            edit.putString("is_active", response.getJSONObject(0).getString("is_active"));
                        edit.putString("provider", response.body().response.get(0).data.get(0).provider);
                        edit.putString("location", response.body().response.get(0).data.get(0).location);
                        edit.putString("zone", response.body().response.get(0).data.get(0).zone);
                        edit.putString("is_spam", response.body().response.get(0).data.get(0).is_spam);
//                            edit.putString("created_at", response.getJSONObject(0).getString("created_at"));

                        edit.apply();
                        unregisterReceiver(reciver);
                        addpopup = new createnew(OTPActivity.this);
                        addpopup.data();
                        Intent intent = new Intent(OTPActivity.this, CheckActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (results.equalsIgnoreCase("1")) {

                        Intent intent = new Intent(OTPActivity.this, LoginSecActivity.class);
                        intent.putExtra("mobile", CountryCode + Mobile);

                        startActivity(intent);
                        finish();
                        Toast.makeText(OTPActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    switch (response.code()) {
                        case 401:
//                            Toast.makeText(Splash.this, "email and pass not check", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
//                            Toast.makeText(Splash.this, "ForbiddenException", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
//                            Toast.makeText(Splash.this, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
//                            Toast.makeText(Splash.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
//                            Toast.makeText(Splash.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    Toast.makeText(OTPActivity.this, "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<VerifyUserResponseJson> call, Throwable t) {
                t.printStackTrace();

                Intent intentnew = new Intent(OTPActivity.this, InternetActivity.class);
                intentnew.putExtra("msg", t.getLocalizedMessage());
                startActivity(intentnew);

                Log.e("System error:", t.getLocalizedMessage());

            }
        });
    }

    private void SendDataToServerss(final String mobile) {
//        Intent intent = new Intent(OTPActivity.this, GetContactsService.class);
//        intent.putExtra("key",sharedPreferencesxx.getString("mobile",""));
//        startService(intent);
//        GetContactsIntoArrayList();
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                String QuickMobile = mobile;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


                nameValuePairs.add(new BasicNameValuePair("mobile", CountryCode + QuickMobile));
                nameValuePairs.add(new BasicNameValuePair("token", Token));

                Log.d("OTPPagecheck", "QuickMobile = = " + nameValuePairs);
                Log.d("CCpWallet", "nameValuePairs = " + nameValuePairs);
                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL_Second + "verify_user");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    resTxt = EntityUtils.toString(entity);

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return resTxt;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
//                mprogress.setVisibility(View.GONE);
                Log.d("OTPPagecheck", "result otp = = " + result);
                Log.d("CCpWallet", "result otp  = " + result);
                if (result.isEmpty()) {

                } else {
                    String res_result;
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Log.d("OTPPage", "jsonObject2 = = " + jsonObject.getString("response"));
                        JSONArray response = jsonObject.getJSONArray("response");

                        if (response.length() == 1) {
                            res_result = response.getJSONObject(0).getString("result");
                            Log.d("OTPPage", "res_result = = " + res_result);
                        } else {
                            res_result = response.getJSONObject(1).getString("result");
                        }

                        Log.d("OTPPage", "res_result = = " + res_result);
//                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
//                    Log.d("dfsdfsdfsdfdfsd", "jsonObject1 = = " + jsonObject1);
//                    JsonArray jsonElements = new JsonArray();


                        if (res_result.equalsIgnoreCase("0")) {
                            Log.d("OTPPage", "response = = " + String.valueOf(response.getJSONObject(0).getInt("id")));
                            JSONArray responsedata = response.getJSONObject(0).getJSONArray("data");

                            edit.putString("fname", responsedata.getJSONObject(0).getString("fname"));
                            edit.putString("lname", responsedata.getJSONObject(0).getString("lname"));
                            edit.putString("email", responsedata.getJSONObject(0).getString("email"));
                            edit.putString("mobile", CountryCode + Mobile);
                            edit.putString("id", String.valueOf(response.getJSONObject(0).getInt("id")));
                            edit.putString("img", responsedata.getJSONObject(0).getString("profile_image"));


//                            edit.putString("is_active", response.getJSONObject(0).getString("is_active"));
                            edit.putString("provider", responsedata.getJSONObject(0).getString("provider"));
                            edit.putString("location", responsedata.getJSONObject(0).getString("location"));
                            edit.putString("zone", responsedata.getJSONObject(0).getString("zone"));
                            edit.putString("is_spam", responsedata.getJSONObject(0).getString("is_spam"));
//                            edit.putString("created_at", response.getJSONObject(0).getString("created_at"));

                            edit.apply();
                            unregisterReceiver(reciver);
                            addpopup = new createnew(OTPActivity.this);
                            addpopup.data();
                            Intent intent = new Intent(OTPActivity.this, CheckActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (res_result.equalsIgnoreCase("1")) {

                            addpopup = new createnew(OTPActivity.this);
                            addpopup.data();
                            Intent intent = new Intent(OTPActivity.this, LoginSecActivity.class);
                            intent.putExtra("mobile", CountryCode + Mobile);

                            startActivity(intent);
                            finish();
                        } else if (res_result.equalsIgnoreCase("2")) {
                            JSONArray responsedata = response.getJSONObject(0).getJSONArray("data");
                            Log.d("WalletLuckyYUYU", "response = = " + res_result);
                            edit.putString("fname", responsedata.getJSONObject(0).getString("fname"));
                            edit.putString("lname", responsedata.getJSONObject(0).getString("lname"));
                            edit.putString("email", responsedata.getJSONObject(0).getString("email"));
                            edit.putString("mobile", CountryCode + Mobile);
                            edit.putString("id", String.valueOf(response.getJSONObject(0).getInt("id")));
                            edit.putString("img", responsedata.getJSONObject(0).getString("profile_image"));


                            edit.putString("is_active", responsedata.getJSONObject(0).getString("is_active"));
                            edit.putString("provider", responsedata.getJSONObject(0).getString("provider"));
                            edit.putString("location", responsedata.getJSONObject(0).getString("location"));
                            edit.putString("zone", responsedata.getJSONObject(0).getString("zone"));
                            edit.putString("is_spam", responsedata.getJSONObject(0).getString("is_spam"));
                            edit.putString("created_at", responsedata.getJSONObject(0).getString("created_at"));
                            edit.apply();
                            unregisterReceiver(reciver);
                            addpopup = new createnew(OTPActivity.this);
                            addpopup.data();
                            Intent intent = new Intent(OTPActivity.this, CheckActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(OTPActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        } else if (res_result.equalsIgnoreCase("3")) {
                            Toast.makeText(OTPActivity.this, "Mobile no is null", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(mobile);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(OTPActivity.this, PhoneActivity.class);
        startActivity(i);

        //Remove activity
        finish();
    }

    private void countDownTimer() {
        countDownTimer = new CountDownTimer(1000 * 60 * 2, 1000) {
            @Override
            public void onTick(long l) {
                String text = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(l) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(l) % 60);
                secnd.setText(text);
            }

            @Override
            public void onFinish() {
                secnd.setText("00:00");
            }
        };
        countDownTimer.start();
    }
}