package com.gpslab.kaun.ProfileEdit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.gpslab.kaun.DatePicker.DataPickerFragment;
import com.gpslab.kaun.Login.LoginSecActivity;
import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.digitaloceanspaces.RandomString;
import com.gpslab.kaun.model.MenuItem;
import com.gpslab.kaun.model.UpdateUserRequestJson;
import com.gpslab.kaun.model.UpdateUserResponseJson;
import com.gpslab.kaun.privacyActivity;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit_model.AddUserResponseJson;
import com.gpslab.kaun.view.DownloadUploadStat;
import com.gpslab.kaun.view.RealmHelper;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public ImageView ivcameraiv;
    public CircleImageView circleImageView;

    public static final String ALLOW_KEY = "ALLOWED";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String CAMERA_PREF = "camera_pref";
    int REQUEST_CAMERA = 1;
    Integer SELECT_FILE = 0;

    public String encoded = "null";
    byte[] CDRIVES;


    static final int START_DATE = 1;
    public int cur = 0;

    public LinearLayout mlgender, mlinearbirthday;
    public TextView etgender, etbirthday;

    public TextInputEditText etfname, etlastname, etphone, etemail;
    public TextInputEditText etStreet, etcityet, etzipcodeet, etcountryet;
    public TextInputEditText etcompanyet, ettitleet, etwebsiteet, etaboutmeet, etaddtaget;


    public AppCompatButton btnsave;



    public ImageView ivimagebg;
    private ProgressBar progressBar;

    /// api
    String resTxt = null;


    /// SharedPreferences
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);


        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();


        initViews();
        onClick();


        setData();
    }


    private void onClick() {


        /// save button
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofit();
            }
        });


        mlinearbirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur = START_DATE;
                DialogFragment datePicker = new DataPickerFragment();

                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        mlgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottompopup_gender();
            }
        });
        etgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottompopup_gender();
            }
        });
        ivcameraiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottompopup();


                Log.d("WalletCamera", "Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);

            }
        });
    }

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
                        ActivityCompat.requestPermissions(ProfileEditActivity.this,
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
                        startInstalledAppDetailsActivity(ProfileEditActivity.this);

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

    public void bottompopup_gender() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProfileEditActivity.this, R.style.BottomSheetDialogTheem);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.genderlayout, (LinearLayout) findViewById(R.id.bottomSheetGender));


        bottomSheetView.findViewById(R.id.prefer_not_to_saytv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etgender.setText("Prefer not to say");
                etgender.setTextColor(Color.parseColor("#000000"));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetView.findViewById(R.id.tvmale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etgender.setText("Male");
                etgender.setTextColor(Color.parseColor("#000000"));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetView.findViewById(R.id.tvfemale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etgender.setText("Female");
                etgender.setTextColor(Color.parseColor("#000000"));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void bottompopup() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProfileEditActivity.this, R.style.BottomSheetDialogTheem);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheetlayout, (LinearLayout) findViewById(R.id.bottomSheetContainer));
        ImageView ivaddcam = (ImageView) bottomSheetView.findViewById(R.id.addcamiv);
        TextView tvaddcam = (TextView) bottomSheetView.findViewById(R.id.addcamtv);


        tvaddcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(ProfileEditActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (getFromPref(ProfileEditActivity.this, ALLOW_KEY)) {

                        showSettingsAlert();

                    } else if (ContextCompat.checkSelfPermission(ProfileEditActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileEditActivity.this,
                                Manifest.permission.CAMERA)) {
                            showAlert();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(ProfileEditActivity.this,
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
                if (ContextCompat.checkSelfPermission(ProfileEditActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (getFromPref(ProfileEditActivity.this, ALLOW_KEY)) {

                        showSettingsAlert();

                    } else if (ContextCompat.checkSelfPermission(ProfileEditActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileEditActivity.this,
                                Manifest.permission.CAMERA)) {
                            showAlert();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(ProfileEditActivity.this,
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

    private void setData() {
        /// first name
        etfname.setText(sharedPreferences.getString("fname", ""));
        etfname.setTextColor(Color.parseColor("#000000"));

        /// first name
        etlastname.setText(sharedPreferences.getString("lname", ""));
        etlastname.setTextColor(Color.parseColor("#000000"));

        /// email
        etemail.setText(sharedPreferences.getString("email", ""));
        etemail.setTextColor(Color.parseColor("#000000"));


        /// phone
        etphone.setText(sharedPreferences.getString("mobile", ""));
        etphone.setTextColor(Color.parseColor("#000000"));



        etcityet.setText(sharedPreferences.getString("city", ""));
        etcityet.setTextColor(Color.parseColor("#000000"));



        etgender.setText(sharedPreferences.getString("gender", ""));
        etgender.setTextColor(Color.parseColor("#000000"));



        etgender.setText(sharedPreferences.getString("gender", ""));
        etgender.setTextColor(Color.parseColor("#000000"));



        etzipcodeet.setText(sharedPreferences.getString("zipcode",""));
        etzipcodeet.setTextColor(Color.parseColor("#000000"));




        etcityet.setText(sharedPreferences.getString("city",""));
        etcityet.setTextColor(Color.parseColor("#000000"));






        ettitleet.setText(sharedPreferences.getString("title",""));
        ettitleet.setTextColor(Color.parseColor("#000000"));






        etcompanyet.setText(sharedPreferences.getString("company_name",""));
        etcompanyet.setTextColor(Color.parseColor("#000000"));


        etbirthday.setText (sharedPreferences.getString("dob",""));
        etbirthday.setTextColor(Color.parseColor("#000000"));





        etwebsiteet.setText(sharedPreferences.getString("website",""));
        etwebsiteet.setTextColor(Color.parseColor("#000000"));




        /// CircleImageView
        Glide.with(ProfileEditActivity.this)
                .load(sharedPreferences.getString("img", "xyz"))
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(circleImageView);
    }

    private void initViews() {



        ivcameraiv = (ImageView) findViewById(R.id.ivcamera);
        circleImageView = (CircleImageView) findViewById(R.id.profile_image);


        etfname = (TextInputEditText) findViewById(R.id.etfnameet);
        etlastname = (TextInputEditText) findViewById(R.id.etlnameet);
        etphone = (TextInputEditText) findViewById(R.id.etphoneet);
        etemail = (TextInputEditText) findViewById(R.id.etemail);
        etbirthday = (TextView) findViewById(R.id.etbirthday);
        etgender = (TextView) findViewById(R.id.etgender);
        mlinearbirthday = (LinearLayout) findViewById(R.id.linearbirthday);
        mlgender = (LinearLayout) findViewById(R.id.lineargender);


        btnsave = (AppCompatButton) findViewById(R.id.savebtn);


        etStreet = (TextInputEditText) findViewById(R.id.etstate);
        etcityet = (TextInputEditText) findViewById(R.id.etcity);
        etzipcodeet = (TextInputEditText) findViewById(R.id.etzipcode);
        etcountryet = (TextInputEditText) findViewById(R.id.etcountry);


        etcompanyet = (TextInputEditText) findViewById(R.id.etcompany);
        ettitleet = (TextInputEditText) findViewById(R.id.ettitle);
        etwebsiteet = (TextInputEditText) findViewById(R.id.etwebsite);
        etaboutmeet = (TextInputEditText) findViewById(R.id.etaboutme);
        etaddtaget = (TextInputEditText) findViewById(R.id.etaddtag);


        ivimagebg = (ImageView)findViewById(R.id.iamgebtn);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        ivimagebg.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

    }

    public File finalFile;
    public String image_name;

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
                Log.i("checkrequestretufot", "Camera 6  bmp    == " + String.valueOf(bmp));


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
//
//
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                image_name = ts + "_" + randomString.nextString();
                finalFile = imageFile;//new File(getRealPathFromURI(providerFile));
                sendImage(CDRIVES,image_name);

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
            }
//            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
//            {
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                imageView.setImageBitmap(photo);
//
////                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////                photo.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
////                CDRIVES = byteArrayOutputStream.toByteArray();
////                encoded = Base64.encodeToString(CDRIVES, Base64.DEFAULT);
////                Toast.makeText(this, encoded, Toast.LENGTH_LONG).show();
//            }

        }
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


    public void sendImage(byte[] encodedtwo, String name) {
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

    private void initRetrofit(String image_name, byte[] encodedtwo) {

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

                    Log.i("CheckImageDetails", "COMPLETED id  2 =     ");
//                    RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.SUCCESS);


                    edit.putString("img", "https://nyc3.digitaloceanspaces.com/gpslabindia/callify/userdata/"+ image_name + ".jpeg");
                    edit.apply();

                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {


                double minus = bytesTotal - bytesCurrent;
                Log.i("checkrequestretufot", "minus  2 =     " + minus);
                double divided = minus / bytesTotal;
                Log.i("checkrequestretufot", "divided  2 =     " + divided);
                double multi = divided * 100;
                Log.i("checkrequestretufot", "divide id  2 =     " + multi);
                double sub = 100 - multi;


                Integer y = (int) sub;
                Log.i("checkrequestretufot", "sub id  2 =     " + y);


                updateNotification(y);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.i("checkrequestretufot", "error id  2 =     " + ex.getMessage());
//                Toast.makeText(activity, "Space upload error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void updateNotification(int currentProgress) {

        Log.i("checkrequestretufot", "updateNotification id  2 =   counter =>  " + currentProgress);
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

        notificationManager.cancelAll();

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MMMM-dd", Locale.getDefault());
        String currentDateString = dateParser.format(c.getTime());

        etbirthday.setText(currentDateString);
        etbirthday.setTextColor(Color.parseColor("#000000"));
    }


    public UpdateUserRequestJson request = new UpdateUserRequestJson();


    public String about, city, email, zip, country, company_name, dob, fname, lname, gender, mobile, website, address,street,title;

    private void retrofit() {
        about = etaboutmeet.getText().toString();
        city = etcityet.getText().toString();
        email = etemail.getText().toString();
        zip = etzipcodeet.getText().toString();
        country = etcountryet.getText().toString();
        title = ettitleet.getText().toString();
        company_name = etcompanyet.getText().toString();
        dob = etbirthday.getText().toString();
        street = etStreet.getText().toString();
        fname = etfname.getText().toString();
        lname = etlastname.getText().toString();
        gender = etgender.getText().toString();
        mobile = etphone.getText().toString();
        website = etwebsiteet.getText().toString();

        address = etStreet.getText().toString() + " " + etzipcodeet.getText().toString() + " " + etcityet.getText().toString() + " " + etcountryet.getText().toString();
        if (zip.length() == 0) {
            zip = "NA";
        }
        if (website.length() == 0) {
            website = "NA";
        }
        if (mobile.length() == 0) {
            mobile = "NA";
        }
        if (gender.length() == 0) {
            gender = "NA";
        }
        if (lname.length() == 0) {
            lname = "NA";
        }
        if (fname.length() == 0) {
            fname = "NA";
        }
        if (dob.length() == 0) {
            dob = "NA";
        }
        if (address.length() == 0) {
            address = "NA";
        }
        if (about.length() == 0) {
            about = "NA";
        }
        if (city.length() == 0) {
            city = "NA";
        }
        if (etemail.length() == 0) {
            email = "NA";
        }
        if (company_name.length() == 0) {
            company_name = "NA";
        }
        if (country.length() == 0) {
            country = "NA";
        }
        request.id = sharedPreferences.getString("id", "0");
        request.about = about;
        request.address = address;
        request.badges = "NA";
        request.city = city;
        request.company_email = "NA";
        request.company_name = company_name;
        request.country = country;
        request.created_at = "NA";
        request.dob = dob;
        request.email = email;
        request.facebook = "NA";
        request.fname = fname;
        request.gender = gender;
        request.is_active = "0";
        request.is_spam = "0";
        request.job_title = "NA";
        request.lname = lname;
        request.location = "NA";
        request.mobile = mobile;
        request.profile_image = sharedPreferences.getString("img", "");
        request.provider = "NA";
        request.score = "0";
        request.spanCount = "0";
        request.status = "0";
        request.tags = "0";
        request.token = sharedPreferences.getString("token", "");

        request.twitter = "NA";
        request.website = website;
        request.zip = zip;
        request.zone = "NA";
        ivimagebg.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Log.i("checkrequestretufot", "request = " + new Gson().toJson(request));


        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.updateuser(request).enqueue(new Callback<UpdateUserResponseJson>() {
            @Override
            public void onResponse(Call<UpdateUserResponseJson> call, Response<UpdateUserResponseJson> response) {
                Log.i("CCpWalletCCpWallet", "nameValuePairs code  = " + response.code());
                if (response.isSuccessful()) {


                    ivimagebg.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    Boolean result = response.body().data;
                    if (result) {


                        edit.putString("fname", fname);
                        edit.putString("lname", lname);
                        edit.putString("email", etemail.getText().toString().trim());
                        edit.putString("mobile", mobile);
                        edit.putString("city", city);
                        edit.putString("address", address);


                        edit.putString("gender", gender);
                        edit.putString("street", street);

                        edit.putString("zipcode", zip);
                        edit.putString("city", city);
                        edit.putString("title", title);





                        edit.putString("company_name", company_name);
                        edit.putString("dob", dob);
                        edit.putString("website", website);
                        edit.apply();
                        Toast.makeText(ProfileEditActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.putExtra("editTextValue", "value_here");
                        setResult(RESULT_OK, intent);
                        finish();

//                        onBackPressed();
                    } else {
                        Toast.makeText(ProfileEditActivity.this, "Not upload", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProfileEditActivity.this, "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<UpdateUserResponseJson> call, Throwable t) {
                t.printStackTrace();

                Log.e("System error:", t.getLocalizedMessage());

            }
        });

    }


    private void SendDataToServer(final String datasss) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("image", "data:image/jpeg;base64," + encoded.replace("\n", "")));
                nameValuePairs.add(new BasicNameValuePair("fname", etfname.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("lname", etlastname.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("email", etemail.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("mobile", etphone.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("dob", etbirthday.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("gender", etgender.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("street", etStreet.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("zip", etzipcodeet.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("city", etcityet.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("country", etcountryet.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("company", etcompanyet.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("title", ettitleet.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("website", etwebsiteet.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("about", etaboutmeet.getText().toString()));

                Log.d("fragmentonefragmentone", "fname = = " + nameValuePairs);

                try {


                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "refresh_data/");

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
                Log.d("fragmentonefragmentone", "result = = " + result);
                if (result.isEmpty()) {

                } else {


                }


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(datasss);
    }
}