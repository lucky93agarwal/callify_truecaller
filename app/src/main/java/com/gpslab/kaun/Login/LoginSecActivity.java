package com.gpslab.kaun.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Telephony;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.gpslab.kaun.CallReceiver;
import com.gpslab.kaun.DB.FirstTableData;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.MainActivity;
import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.MyBrodcastRecieverService;
import com.gpslab.kaun.OTP.OTPActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;

import com.gpslab.kaun.Webapi.ChatWebAPI;
import com.gpslab.kaun.digitaloceanspaces.RandomString;
import com.gpslab.kaun.language.LanguageActivity;
import com.gpslab.kaun.model.Contact;
import com.gpslab.kaun.model.ContactFetcher;
import com.gpslab.kaun.popup.checkpermission;
import com.gpslab.kaun.popup.checkpermissionpopup;
import com.gpslab.kaun.privacyActivity;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit_model.AddUserRequestJson;
import com.gpslab.kaun.retrofit_model.AddUserResponseJson;
import com.gpslab.kaun.view.DownloadUploadStat;
import com.gpslab.kaun.view.MessageType;
import com.gpslab.kaun.view.RealmHelper;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginSecActivity extends AppCompatActivity {
    public String encoded = "null";
    public LinearLayout llcontinue;
    public TextView tvcontinue;
    String resTxt = null;
    Integer SELECT_FILE = 0;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public TextInputEditText etfistname, etsecondname, etemail;
    String reserver = "";
    byte[] CDRIVES;
    int REQUEST_CAMERA = 1;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public ImageView ivcameraiv;
    public CircleImageView circleImageView;
    public String Mobile = "9945698565";
    public static final String ALLOW_KEY = "ALLOWED";
    private static final int CAMERA_REQUEST = 1888;

    private Uri providerFile;

    public AVLoadingIndicatorView mprogress;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    public static final String CAMERA_PREF = "camera_pref";

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences
                (CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }


    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(LoginSecActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                    }
                });
        alertDialog.show();
    }


    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(LoginSecActivity.this);

                    }
                });
        alertDialog.show();
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
//        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, "com.gpslab.kaun");
//        startActivity(intent);
    }

    public JSONObject jsonObject, jsonObject1;
    public JSONArray jsonArray1;

    public ArrayList<String> listlist = new ArrayList<String>();
    public List<String> listlistfinal = new ArrayList<String>();
    public List<String> seclist = new ArrayList<>();
    public ArrayList<Contact> listContacts;

    private Intent forService;
    String[] appPermissions = {Manifest.permission.INTERNET,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.MODIFY_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SYSTEM_ALERT_WINDOW};
    private static final int PERMISSION_REQUEST_CODE = 1240;

    public MyDbHandler myDbHandler;


    public SharedPreferences.Editor editor;

    private static checkpermission profilepopupnew;

    public ArrayList<String> mobilearray = new ArrayList<>();
    public ArrayList<String> namearray = new ArrayList<>();


    public ImageView first_green_btn, sec_green_btn;

    public String CountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sec);
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        myDbHandler = new MyDbHandler(getApplicationContext(), "userbd", null, 1);
        CountryCode = getIntent().getStringExtra("code");
        first_green_btn = (ImageView) findViewById(R.id.greencheckivs);
        first_green_btn.setVisibility(View.GONE);
        sec_green_btn = (ImageView) findViewById(R.id.greencheckiv);
        sec_green_btn.setVisibility(View.GONE);
        Temp.setMyDbHandler(myDbHandler);
        myDbHandler = Temp.getMyDbHandler();
        profilepopupnew = new checkpermission(LoginSecActivity.this);

//        if (checkAndRequestPermissions()) {
//            // All permission are granted already. Proceed ahead
//        }

//        if (!Settings.canDrawOverlays(this)) {
//            // ask for setting
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, PERMISSION_REQUEST_CODE);
//        }


        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_PHONE_STATE
                        },
                        PERMISSION_REQUEST_CODE);
            }
        }
        forService = new Intent(LoginSecActivity.this, CallReceiver.class);


        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();


        mprogress = (AVLoadingIndicatorView) findViewById(R.id.avi);
        ivcameraiv = (ImageView) findViewById(R.id.ivcamera);
        circleImageView = (CircleImageView) findViewById(R.id.profile_image);


        ivcameraiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getBoolean("camerapermission", false)) {
                    bottompopup();
                } else {
                    profilepopupnew.profileaddpopup();
                }



                Log.d("WalletCamera", "Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);

            }
        });


        etfistname = (TextInputEditText) findViewById(R.id.etfname);
        etsecondname = (TextInputEditText) findViewById(R.id.etsname);
        etemail = (TextInputEditText) findViewById(R.id.etemail);
        etfistname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && etsecondname.getText().length() > 0 && etemail.getText().length() > 0) {
                    llcontinue.setBackgroundColor(Color.parseColor("#3FBA13"));
                } else {
                    llcontinue.setBackgroundColor(Color.parseColor("#A6B2BF"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etsecondname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    first_green_btn.setVisibility(View.VISIBLE);
                } else {
                    first_green_btn.setVisibility(View.GONE);
                }

                if (s.length() > 0 && etfistname.getText().length() > 0 && etemail.getText().length() > 0) {
                    llcontinue.setBackgroundColor(Color.parseColor("#3FBA13"));
                } else {
                    llcontinue.setBackgroundColor(Color.parseColor("#A6B2BF"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    sec_green_btn.setVisibility(View.VISIBLE);
                } else {
                    sec_green_btn.setVisibility(View.GONE);
                }

                if (s.length() > 0 && etsecondname.getText().length() > 0 && etfistname.getText().length() > 0) {
                    llcontinue.setBackgroundColor(Color.parseColor("#3FBA13"));
                } else {
                    llcontinue.setBackgroundColor(Color.parseColor("#A6B2BF"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Mobile = getIntent().getStringExtra("mobile");
//        Mobile = "919991110000";
        llcontinue = (LinearLayout) findViewById(R.id.llcontinue);
        tvcontinue = (TextView) findViewById(R.id.tvcontinue);

        Log.d("WalletLuckyYUYU", "mobile = Mobile = = " + Mobile);
        llcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (encoded.length() == 0) {
                    Toast.makeText(LoginSecActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();
                } else if (etfistname.getText().toString().length() == 0) {
                    Toast.makeText(LoginSecActivity.this, "Please Enter your fistname", Toast.LENGTH_SHORT).show();
                } else if (etsecondname.getText().toString().length() == 0) {
                    Toast.makeText(LoginSecActivity.this, "Please Enter your last name", Toast.LENGTH_SHORT).show();
                } else if (etemail.getText().toString().length() == 0) {
                    Toast.makeText(LoginSecActivity.this, "Please Enter your email address", Toast.LENGTH_SHORT).show();
                } else if (isValidEmail(etemail.getText().toString().trim())) {
                    mprogress.setVisibility(View.VISIBLE);
                    retrofit();
//                    SendDataToServerss(etfistname.getText().toString(), etsecondname.getText().toString(), etemail.getText().toString(), Mobile);
                } else {
                    Toast.makeText(LoginSecActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (encoded.length() == 0) {
                    Toast.makeText(LoginSecActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();
                } else if (etfistname.getText().toString().length() == 0) {
                    Toast.makeText(LoginSecActivity.this, "Please Enter your fistname", Toast.LENGTH_SHORT).show();
                } else if (etsecondname.getText().toString().length() == 0) {
                    Toast.makeText(LoginSecActivity.this, "Please Enter your last name", Toast.LENGTH_SHORT).show();
                } else if (etemail.getText().toString().length() == 0) {
                    Toast.makeText(LoginSecActivity.this, "Please Enter your email address", Toast.LENGTH_SHORT).show();
                } else if (isValidEmail(etemail.getText().toString().trim())) {
                    mprogress.setVisibility(View.VISIBLE);
                    retrofit();
//                    SendDataToServerss(etfistname.getText().toString(), etsecondname.getText().toString(), etemail.getText().toString(), Mobile);
                } else {
                    Toast.makeText(LoginSecActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void bottompopup() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LoginSecActivity.this, R.style.BottomSheetDialogTheem);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheetlayout, (LinearLayout) findViewById(R.id.bottomSheetContainer));
        ImageView ivaddcam = (ImageView) bottomSheetView.findViewById(R.id.addcamiv);
        TextView tvaddcam = (TextView) bottomSheetView.findViewById(R.id.addcamtv);


        tvaddcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(LoginSecActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (getFromPref(LoginSecActivity.this, ALLOW_KEY)) {

                        showSettingsAlert();

                    } else if (ContextCompat.checkSelfPermission(LoginSecActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(LoginSecActivity.this,
                                Manifest.permission.CAMERA)) {
                            showAlert();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(LoginSecActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                } else {
                    bottomSheetDialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                }
            }
        });

        ivaddcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(LoginSecActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (getFromPref(LoginSecActivity.this, ALLOW_KEY)) {

                        showSettingsAlert();

                    } else if (ContextCompat.checkSelfPermission(LoginSecActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(LoginSecActivity.this,
                                Manifest.permission.CAMERA)) {
                            showAlert();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(LoginSecActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                } else {
                    bottomSheetDialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                    //openCamera();

                }
            }
        });


        ImageView ivaddgalliv = (ImageView) bottomSheetView.findViewById(R.id.addgalliv);
        TextView tvaddgalltv = (TextView) bottomSheetView.findViewById(R.id.addgalltv);
        ivaddgalliv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);
            }
        });
        tvaddgalltv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private boolean checkAndRequestPermissions() {
        // Check which Permissions are granted (Check करें कि कौन सी Permissions Granted है।)
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);

//                Log.d("CheckPermissionLucky","Yes");
            }
        }
        // Ask for non-granted permissions
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
//            Log.d("CheckPermissionLucky","No");
            return false;
        }
        ;
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                Log.d("WalletYuyckey", "jsonArray ==  4");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //                 Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    Log.d("WalletYuyckey", "jsonArray ==  3");
//                    GetContactsIntoArrayList();
                    Intent service = new Intent(LoginSecActivity.this, MyBrodcastRecieverService.class);
                    startService(service);
                    editor.putString("check", "true");
                    editor.apply();


                } else {

                    Log.d("WalletYuyckey", "jsonArray ==  2");
//                    GetContactsIntoArrayList();
                    Intent service = new Intent(LoginSecActivity.this, MyBrodcastRecieverService.class);
                    startService(service);

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public FirstTableData user;

    public void updatedb() {

    }

    public void firstinsertdata() {
        listContacts = new ContactFetcher(this).fetchAll();
        for (int i = 0; i < listContacts.size(); i++) {
            user = new FirstTableData();
            if (listContacts.get(i).name.length() > 0) {
                user.setName(listContacts.get(i).name);
            } else {
                user.setName("");
            }


            if (listContacts.get(i).emails.size() > 0 && listContacts.get(i).emails.get(0) != null) {
                if (listContacts.get(i).id.equalsIgnoreCase(listContacts.get(i).emails.get(0).id)) {
                    user.setEmail(listContacts.get(i).emails.get(0).address);
                }
            } else {
                user.setEmail("");
            }


            if (listContacts.get(i).numbers.size() == 1) {
                if (listContacts.get(i).numbers.size() > 0 && listContacts.get(i).numbers.get(0) != null) {

                    if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() == 10) {
                        user.setMobileone("+91" + listContacts.get(i).numbers.get(0).number.replace(" ", ""));
                        user.setMobiletwo("");
                    } else if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() > 10) {
                        if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() == 11) {
                            String number = listContacts.get(i).numbers.get(0).number.replace(" ", "");
                            number = number.substring(1);
                            number = "+91" + number;
                            user.setMobileone(number);
                            user.setMobiletwo("");
                        } else {
                            user.setMobileone(listContacts.get(i).numbers.get(0).number.replace(" ", ""));
                            user.setMobiletwo("");
                        }

                    } else {
                        user.setMobileone(listContacts.get(i).numbers.get(0).number.replace(" ", ""));
                        user.setMobiletwo("");
                    }

                }
            } else if (listContacts.get(i).numbers.size() == 2) {
                if (listContacts.get(i).numbers.size() == 2 && listContacts.get(i).numbers.get(1) != null) {


                    if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() == 10) {
                        user.setMobileone("+91" + listContacts.get(i).numbers.get(0).number.replace(" ", ""));

                    } else if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() > 10) {
                        if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() == 11) {
                            String number = listContacts.get(i).numbers.get(0).number.replace(" ", "");
                            number = number.substring(1);
                            number = "+91" + number;
                            user.setMobileone(number);

                        } else {
                            user.setMobileone(listContacts.get(i).numbers.get(0).number.replace(" ", ""));

                        }

                    } else {
                        user.setMobileone(listContacts.get(i).numbers.get(0).number.replace(" ", ""));

                    }


                    if (listContacts.get(i).numbers.get(1).number.replace(" ", "").length() == 10) {
                        user.setMobiletwo("+91" + listContacts.get(i).numbers.get(1).number.replace(" ", ""));

                    } else if (listContacts.get(i).numbers.get(1).number.replace(" ", "").length() > 10) {
                        if (listContacts.get(i).numbers.get(1).number.replace(" ", "").length() == 11) {
                            String number = listContacts.get(i).numbers.get(1).number.replace(" ", "");
                            number = number.substring(1);
                            number = "+91" + number;
                            user.setMobiletwo(number);

                        } else {
                            user.setMobiletwo(listContacts.get(i).numbers.get(1).number.replace(" ", ""));

                        }

                    } else {
                        user.setMobiletwo(listContacts.get(i).numbers.get(1).number.replace(" ", ""));

                    }

                }
            } else {
                user.setMobileone("");
                user.setMobiletwo("");
            }
            user.setPhoto("");
            int j = myDbHandler.insertUser(user);
            Log.d("walletwallet", "inseart check = " + j);
        }

//        user.setMobileone();
//        user.setMobiletwo();
//        user.setPhoto();
//        user.setEmail();


    }

    private void firstqueryall() {

        ArrayList<FirstTableData> arrayList = myDbHandler.viewUser();
        long countnew = myDbHandler.getProfilesCount();
        Log.d("walletwallet", "size = " + arrayList.size() + "   Size ====  " + countnew);


        //// check row on the table is null or not
        if (arrayList.size() != 0) {
            Log.d("walletwallet", "update = ");


            boolean count = false;
            count = myDbHandler.checkrow("+919651667458");
            Log.d("walletwallet", "count check = " + count);
            if (count) {
                Log.d("walletwallet", " 2 UPdate Query........");
                //  updatedb();
            } else {
                Log.d("walletwallet", "2 Insert Query........");
                //  firstinsertdata();
            }
//            Log.d("walletwallet", "count = " + count);

        } else {
            Log.d("walletwallet", "Insert Query........");
            firstinsertdata();
        }
    }

    private void GetContactsIntoArrayList() {
        Log.d("WalletYuyckey", "jsonArray ==  1");
        listContacts = new ContactFetcher(this).fetchAll();

        jsonObject = new JSONObject();
        jsonObject1 = new JSONObject();
        jsonArray1 = new JSONArray();
        user = new FirstTableData();
        firstqueryall();

        int total = (int) Math.ceil(listContacts.size() / 100);
        Log.d("dfsdfsdfsdfdfsd", "size ==  " + total);
        try {
            for (int i = 0; i < listContacts.size(); i++) {
                JSONArray jsonArray = new JSONArray();
                if (listlist.size() > 0) {
                    listlist.clear();
                }
                jsonObject.put("name", listContacts.get(i).name);

                if (listContacts.get(i).emails.size() > 0 && listContacts.get(i).emails.get(0) != null) {

                    if (listContacts.get(i).id.equalsIgnoreCase(listContacts.get(i).emails.get(0).id)) {
                        jsonObject.put("email", listContacts.get(i).emails.get(0).address);

                    }

                } else {

                    jsonObject.put("email", "");
                }


                if (listContacts.get(i).numbers.size() == 1) {
                    if (listContacts.get(i).numbers.size() > 0 && listContacts.get(i).numbers.get(0) != null) {
                        jsonArray.put(listContacts.get(i).numbers.get(0).number);
                        mobilearray.add(listContacts.get(i).numbers.get(0).number);
                        namearray.add(listContacts.get(i).name);
                        Log.d("WalletYuyckeynumber", "Mobile ==  " + listContacts.get(i).numbers.get(0).number);

                    }
                } else if (listContacts.get(i).numbers.size() == 2) {
                    if (listContacts.get(i).numbers.size() == 2 && listContacts.get(i).numbers.get(1) != null) {
                        Log.d("WalletYuyckeynumber", "Mobile ==  " + listContacts.get(i).numbers.get(0).number);
                        mobilearray.add(listContacts.get(i).numbers.get(0).number);
                        mobilearray.add(listContacts.get(i).numbers.get(1).number);
                        namearray.add(listContacts.get(i).name);
                        namearray.add(listContacts.get(i).name);

                        jsonArray.put(listContacts.get(i).numbers.get(0).number);
                        jsonArray.put(listContacts.get(i).numbers.get(1).number);
                    }
                }


                if (listContacts.get(i).numbers.size() == 0) {
                    jsonObject.put("mobile", "");
                } else {
                    jsonObject.put("mobile", jsonArray);
                }


                listlistfinal.add(jsonObject.toString());


            }

//            for(int j =0;j<total;j++){
//                int z= j +1;
//                int no = 100 * z;
//                seclist = listlistfinal.subList(j,no);
////                Log.d("dfsdfsdfsdfdfsd","size 2 ==  "+seclist.size());
//                SendDataToServer(seclist.toString());
//            }
            Log.d("dfsdfsdfsdfdfsd", "fname = = " + listlistfinal.toString());
            SendDataToServer(listlistfinal.toString());

            Log.d("WalletYuyckey", "jsonArray ==  " + listlistfinal);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
//    public static Uri getImageUriNew(Activity inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
    public Uri getImageUri(Bitmap inImage) {
        Log.d("WalletNewYUYU", "Camera 7  bmp    == " + String.valueOf(inImage));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;


    ///
    String accesskey = "4SYUKBCFA4KASIHESCTP";
    String secretkey = "cWCsWzfqX4h1Y6ULx+8OE0hrPjf5CCiDPFOSIdkxOSA";
    String spacename = "gpslabindia";

    String spaceregion = "https://nyc3.digitaloceanspaces.com";
    String filename = "example_image";
    CannedAccessControlList filePermission = CannedAccessControlList.PublicRead;
    String filetype = "jpeg";
    public TransferUtility transferUtility;
    public int counter = 0;

    public AmazonS3Client client;
    public BasicAWSCredentials credentials;
    public TransferObserver observer;


    public void sendImage( byte[] encodedtwo,String name){
        credentials = new BasicAWSCredentials(accesskey, secretkey);

        client = new AmazonS3Client(credentials, Region.getRegion("us-east-1"));
        client.setEndpoint(spaceregion);
        transferUtility = new TransferUtility(client, getApplicationContext());
        CannedAccessControlList filePermission = CannedAccessControlList.PublicRead;



        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("id", "an", NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("no sound");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        notificationBuilder = new NotificationCompat.Builder(this, "id")
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Upload")
                .setContentText("Uploading Image")
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        initRetrofit(name, encodedtwo);

    }



    public File exampleFile;

    private File convertKanuImageResourceToFile(byte[] encoded) {

        Date d = new Date();
        try {
            exampleFile = new File(Environment.getExternalStorageDirectory(), d.toString());

            exampleFile.createNewFile();


            FileOutputStream fos = new FileOutputStream(exampleFile);
            fos.write(encoded);
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exampleFile;
    }
    private void initRetrofit(String image_name,byte[] encodedtwo) {

        RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.LOADING);
        observer = transferUtility.upload(
                spacename + "/callify/userdata", //empty bucket name, included in endpoint
                image_name + ".jpeg",
                finalFile, //a File object that you want to upload
                filePermission
        );
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state.COMPLETED.equals(observer.getState())) {
                    onDownloadComplete(true);

                    Log.i("CheckImageDetails", "COMPLETED id  2 =     " );
//                    RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.SUCCESS);

                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {


                double minus = bytesTotal - bytesCurrent;
                Log.d("uploadExampmageFile", "minus  2 =     " + minus);
                double divided = minus / bytesTotal;
                Log.d("uploadExampmageFile", "divided  2 =     " + divided);
                double multi = divided *100;
                Log.d("uploadExampmageFile", "divide id  2 =     " + multi);
                double sub = 100- multi;


                Integer y = (int) sub;
                Log.d("uploadExampmageFile", "sub id  2 =     " + y);


                updateNotification(y);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.i("CheckImageDetails", "error id  2 =     " + ex.getMessage());
//                Toast.makeText(activity, "Space upload error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void updateNotification(int currentProgress) {

        Log.d("uploadExampleImageFile", "updateNotification id  2 =   counter =>  " + currentProgress);
        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentText("Upload: " + currentProgress + "%");
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void sendProgressUpdate(boolean downloadComplete) {

//        Intent intent = new Intent(MainActivity.PROGRESS_UPDATE);
//        intent.putExtra("downloadComplete", downloadComplete);
//        LocalBroadcastManager.getInstance(BackgroundNotificationService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
        notificationBuilder.setContentText("Image Upload Complete");
        notificationManager.notify(0, notificationBuilder.build());

    }
    public File finalFile;
    public   String image_name;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("CheckImageDetails", "Camera == " + data);
        if (resultCode == Activity.RESULT_OK) {
            Log.i("CheckImageDetails", "Camera 2 == " + data);
            if (requestCode == REQUEST_CAMERA) {
                Log.i("CheckImageDetails", "Camera 3 == " + data);
                Bundle bundle = data.getExtras();

                Log.i("CheckImageDetails", "Camera 4 == " + bundle.toString());
//                if(bundle !=null){
                final Bitmap bmp = (Bitmap) bundle.get("data");

                Log.i("CheckImageDetails", "Camera 5 == " + bmp.toString());
                CircleImageView cir = (CircleImageView) findViewById(R.id.profile_image);
                cir.setImageBitmap(bmp);
                //    uploadtitle.setText("screenshot upload successfully");

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                CDRIVES = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(CDRIVES, Base64.DEFAULT);



//                }else {
//                    Toast.makeText(LoginSecActivity.this, "Please try agin", Toast.LENGTH_SHORT).show();
//                }
                Log.i("CheckImageDetails", "Camera 6  bmp    == " + String.valueOf(bmp));


                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                /*Uri tempUri = getImageUri(bmp);
                Log.d("WalletNewYUYU", "Camera 6  path    == " + tempUri.toString());*/
                File filesDir = getFilesDir();
                File imageFile = new File(filesDir, "temp" + ".jpg");

                OutputStream os;
                try {
                    os = new FileOutputStream(imageFile);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 50, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }


                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                image_name = ts + "_" + randomString.nextString();
                Log.i("CheckImageDetails", "Camera 6  image_name    == " + String.valueOf(image_name));
                finalFile = imageFile;//new File(getRealPathFromURI(providerFile));
                sendImage(CDRIVES,image_name);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
//


//                storepickter.add(encoded);
//
//                HashSet<String> hashSettwosss = new HashSet<String>();
//                hashSettwosss.addAll(storepickter);
//                storepickter.clear();
//                storepickter.addAll(hashSettwosss);
//                Toast.makeText(this,"2  "+ storepickter, Toast.LENGTH_LONG).show();
//                UploadDocs uploadDocs = new UploadDocs();
//                uploadDocs.execute();

            } else if (requestCode == SELECT_FILE) {

                Uri selectedImageUri = data.getData();

                circleImageView.setImageURI(selectedImageUri);

                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                CDRIVES = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(CDRIVES, Base64.DEFAULT);


                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                image_name = ts + "_" + randomString.nextString();

                finalFile = new File(getRealPathFromURI(selectedImageUri));
                sendImage(CDRIVES,image_name);

            } else {
                Toast.makeText(this, "Technical problem please try again", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(this, "Technical problem please try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private void openCamera() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File filePhoto = null;
        try {
            filePhoto = getPhotoFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        providerFile = FileProvider.getUriForFile(this, "com.gpslab.kaun.fileprovider", filePhoto);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePhotoIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(this, "Camera could not open", Toast.LENGTH_SHORT).show();
        }
    }
    private File getPhotoFile() throws IOException {
        File directoryStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/kaun/");
        if (!directoryStorage.exists()) {
            if (!directoryStorage.mkdirs()) {
                return null;
            }
        }
        Log.d("path: %s", directoryStorage.getPath());
        return File.createTempFile(String.valueOf(System.currentTimeMillis()), ".jpg", directoryStorage);
    }



    public AddUserRequestJson request = new AddUserRequestJson();
    private void retrofit() {


        request.fname = etfistname.getText().toString();
        request.lname = etsecondname.getText().toString();
        request.email = etemail.getText().toString();
        request.mobile = Mobile;
        request.profile_image ="https://nyc3.digitaloceanspaces.com/gpslabindia/callify/userdata/"+ image_name + ".jpeg";
        request.gender = "NA";
        request.dob = "NA";
        request.is_active = 0;
        request.provider = "NA";
        request.location = "NA";
        request.zone = "NA";
        request.zip = "NA";



        request.city = "NA";
        request.country = "NA";
        request.about = "NA";
        request.is_spam = 0;
        request.address = "NA";
        request.company_name = "NA";
        request.job_title = "NA";



        request.company_email = "NA";
        request.facebook = "NA";
        request.twitter = "NA";
        request.website = "NA";
        request.tags = 0;
        request.badges = "NA";
        request.score = 0;



        request.spanCount = 0;
        request.created_at = "NA";
        request.token = sharedPreferences.getString("token", "");
        request.status = 0;
        Log.i("CCpWalletCCpWallet", "nameValuePairs Signup  = " + new Gson().toJson(request));

        mprogress.setVisibility(View.VISIBLE);
        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.adduser(request).enqueue(new Callback<AddUserResponseJson>() {
            @Override
            public void onResponse(Call<AddUserResponseJson> call, Response<AddUserResponseJson> response) {
                Log.i("CCpWalletCCpWallet", "nameValuePairs code  = " + response.code());
                if (response.isSuccessful()) {

                    String result = response.body().response.result;
                    if(result.equalsIgnoreCase("1")){

                        edit.putString("fname", etfistname.getText().toString().trim());
                        edit.putString("lname", etsecondname.getText().toString().trim());
                        edit.putString("email", etemail.getText().toString().trim());
                        edit.putString("mobile", Mobile);
                        edit.putString("id", response.body().response.insertId);
                        edit.putString("img", "https://nyc3.digitaloceanspaces.com/gpslabindia/callify/userdata/"+ image_name + ".jpeg");
                        edit.apply();

                        Intent intent = new Intent(LoginSecActivity.this, privacyActivity.class);
                        startActivity(intent);
                        finish();
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
                    Toast.makeText(LoginSecActivity.this, "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<AddUserResponseJson> call, Throwable t) {
                t.printStackTrace();

                Log.e("System error:", t.getLocalizedMessage());

            }
        });
    }

    private void SendDataToServerss(final String firstname, final String secondname, final String email, final String mobile) {





        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String QuickFirstName = firstname;
                String QuickSecName = secondname;
                String QuickEmail = email;

                String QuickMobile = mobile;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("fname", QuickFirstName));
                nameValuePairs.add(new BasicNameValuePair("lname", QuickSecName));

                nameValuePairs.add(new BasicNameValuePair("email", QuickEmail));
                nameValuePairs.add(new BasicNameValuePair("mobile", QuickMobile));

                nameValuePairs.add(new BasicNameValuePair("profile_image", image_name + ".jpeg"));


                nameValuePairs.add(new BasicNameValuePair("gender", "NA"));
                nameValuePairs.add(new BasicNameValuePair("dob", "NA"));

                nameValuePairs.add(new BasicNameValuePair("is_active", "NA"));
                nameValuePairs.add(new BasicNameValuePair("provider", "NA"));
                nameValuePairs.add(new BasicNameValuePair("location", "NA"));
                nameValuePairs.add(new BasicNameValuePair("zone", "NA"));


                nameValuePairs.add(new BasicNameValuePair("zip", "NA"));
                nameValuePairs.add(new BasicNameValuePair("city", "NA"));

                nameValuePairs.add(new BasicNameValuePair("country", "NA"));
                nameValuePairs.add(new BasicNameValuePair("about", "NA"));
                nameValuePairs.add(new BasicNameValuePair("is_spam", "NA"));
                nameValuePairs.add(new BasicNameValuePair("address", "NA"));


                nameValuePairs.add(new BasicNameValuePair("company_name", "NA"));
                nameValuePairs.add(new BasicNameValuePair("job_title", "NA"));

                nameValuePairs.add(new BasicNameValuePair("company_email", "NA"));
                nameValuePairs.add(new BasicNameValuePair("facebook", "NA"));
                nameValuePairs.add(new BasicNameValuePair("twitter", "NA"));
                nameValuePairs.add(new BasicNameValuePair("website", "NA"));


                nameValuePairs.add(new BasicNameValuePair("tags", "NA"));
                nameValuePairs.add(new BasicNameValuePair("badges", "NA"));

                nameValuePairs.add(new BasicNameValuePair("score", "NA"));
                nameValuePairs.add(new BasicNameValuePair("spanCount", "NA"));
                nameValuePairs.add(new BasicNameValuePair("created_at", "NA"));
                nameValuePairs.add(new BasicNameValuePair("token", sharedPreferences.getString("token", "")));

                nameValuePairs.add(new BasicNameValuePair("status", "NA"));


                Log.d("CCpWalletCCpWallet", "nameValuePairs Signup  = " + nameValuePairs);
                Log.d("dfsdfsdfsdfdfsd", "fname = = " + QuickFirstName);
                Log.d("dfsdfsdfsdfdfsd", "lname = = " + QuickSecName);
                Log.d("dfsdfsdfsdfdfsd", "email = = " + QuickEmail);
                Log.d("dfsdfsdfsdfdfsd", "mobile = = " + QuickMobile);
                Log.d("dfsdfsdfsdfdfsd", "image = = " + "data:image/jpeg;base64," + encoded.replace("\n", ""));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL_Second+ "adduser");

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
                Log.d("CCpWalletCCpWallet", "result Signup  = " + result);
                mprogress.setVisibility(View.GONE);
                if(result !=null){
                    Log.d("dfsdfsdfsdfdfsd", "result = = " + result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
                        if (jsonObject1.getString("result").equalsIgnoreCase("0")) {
                            Log.d("WalletLuckyYUYU", "response = = " + jsonObject1.getString("id"));

                            edit.putString("fname", etfistname.getText().toString().trim());
                            edit.putString("lname", etsecondname.getText().toString().trim());
                            edit.putString("email", etemail.getText().toString().trim());
                            edit.putString("mobile", Mobile);
                            edit.putString("id", jsonObject1.getString("id"));
                            edit.putString("img", ResURls.imagebaseURL+image_name+".jpg");
                            edit.apply();

                            Intent intent = new Intent(LoginSecActivity.this, privacyActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (jsonObject1.getString("result").equalsIgnoreCase("1")) {
                            Toast.makeText(LoginSecActivity.this, "Please enter after some time.", Toast.LENGTH_SHORT).show();
                        } else if (jsonObject1.getString("result").equalsIgnoreCase("2")) {
                            Log.d("WalletLuckyYUYU", "response = = " + jsonObject1.toString());
                            edit.putString("fname", etfistname.getText().toString().trim());
                            edit.putString("lname", etsecondname.getText().toString().trim());
                            edit.putString("email", etemail.getText().toString().trim());
                            edit.putString("mobile", Mobile);
                            edit.putString("id", jsonObject1.getString("id"));
                            edit.putString("img", ResURls.imagebaseURL+image_name+".jpg");
                            edit.apply();
                            Intent intent = new Intent(LoginSecActivity.this, privacyActivity.class);
                            startActivity(intent);
                            finish();
                            //                       Toast.makeText(LoginSecActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        } else if (jsonObject1.getString("result").equalsIgnoreCase("3")) {
                            //                       Toast.makeText(LoginSecActivity.this, "Mobile no is null", Toast.LENGTH_SHORT).show();
                        }

//                    Intent intent = new Intent(LoginSecActivity.this, MainHomeActivity.class);
//                    startActivity(intent);
//                    finish();
                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }else {
                    Toast.makeText(LoginSecActivity.this, "Please try agin", Toast.LENGTH_SHORT).show();
                }


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(firstname, secondname, email, mobile);
    }


    private void SendDataToServer(final String datasss) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String QuickFirstName = datasss;
                JSONObject jsonObjectlucky = new JSONObject();
                try {
                    jsonObjectlucky.put("parent_no", Mobile);
                    jsonObjectlucky.put("data", listlistfinal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String data = String.valueOf(Html.fromHtml(listlistfinal.toString(), Html.FROM_HTML_MODE_LEGACY));
//                QuickFirstName = Character.toString ((char) QuickFirstName);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("parent_no", Mobile));
                nameValuePairs.add(new BasicNameValuePair("data", listlistfinal.toString()));

                Log.d("CCpWallet", "nameValuePairs contact_sync Signup  = " + nameValuePairs);
                Log.d("dfsdfsdfsdfdfsd", "fname = = " + nameValuePairs);


//                JSONObject jsonObjectlucky = new JSONObject();
                try {
                    StringEntity se;
                    se = new StringEntity(jsonObjectlucky.toString());

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "contact_sync/");

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
                mprogress.setVisibility(View.GONE);
                Log.d("CCpWallet", "nameValuePairs result Signup  = " + result);
                Log.d("dfsdfsdfsdfdfsd", "result = = " + result);
                if (result.isEmpty()) {

                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));

//
                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(datasss);
    }

}