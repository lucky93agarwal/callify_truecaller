package com.gpslab.kaun.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.gpslab.kaun.R;

public class TestingActivity extends AppCompatActivity {
    //    String TAG = "PhoneActivityTAG";
//    Activity activity = TestingActivity.this;
//    String wantPermission = Manifest.permission.READ_PHONE_STATE;
//    private static final int PERMISSION_REQUEST_CODE = 1;
//    ArrayList<String> _mst=new ArrayList<>();
//    public Button refereshbutton;
//    public UnifiedNativeAd nativeAd;
//
//    private ContactViewModel contactViewModel;
//    String[] appPermissions = {Manifest.permission.INTERNET,
//            Manifest.permission.READ_CALL_LOG,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.PROCESS_OUTGOING_CALLS,
//            Manifest.permission.MODIFY_PHONE_STATE,
//            Manifest.permission.READ_CONTACTS,
//            Manifest.permission.CALL_PHONE,
//            Manifest.permission.RECEIVE_BOOT_COMPLETED,
//            Manifest.permission.WAKE_LOCK,
//            Manifest.permission.FOREGROUND_SERVICE,
//            Manifest.permission.SEND_SMS,
//            Manifest.permission.READ_SMS,
//            Manifest.permission.SYSTEM_ALERT_WINDOW};
//    private static final int PERMISSION_REQUEST_CODE = 1240;
//
//
//
//    String insertData = "";
//    String insertSecData = "";
//    public String numbers;
//
//    Button btnSend;
//    EditText editTextNum, editTextMsg;
//    private static final int PERMISSION_REQUEST = 101;
//    public MyDbHandler myDbHandler;
//
//
//    public String image_name;
//
//
//
//    public void downloadImage(View view) {
//
//        Log.d("InfoInfoInfo", "Button pressed");
//
//        DownloadTask downloadTask = new DownloadTask();
//        downloadTask.execute(image_url);
//    }
//
//
//    ImageView imageView;
//
//    String image_url = "https://image.shutterstock.com/image-photo/white-transparent-leaf-on-mirror-260nw-1029171697.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);



        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");


        AdLoader.Builder builder = new AdLoader.Builder(
                this, "ca-app-pub-3940256099942544/2247696110");

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                TemplateView template = findViewById(R.id.my_template);
                template.setNativeAd(unifiedNativeAd);
            }
        });

        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());
//        myDbHandler = new MyDbHandler(getApplicationContext(), "userbd", null, 1);
//        myDbHandler = Temp.getMyDbHandler();
//        if (checkAndRequestPermissions()) {
//            // All permission are granted already. Proceed ahead
//        }

//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
//        RandomString randomString = new RandomString();
//        image_name = ts+"_"+randomString.nextString()+"downloaded_image.jpg";
//
//
//        refereshbutton = (Button)findViewById(R.id.btn_download_msg);
//        imageView = (ImageView) findViewById(R.id.image_view);
//        initRecyclerView();

    }
//    class DownloadTask extends AsyncTask<String, Integer, String> {
//
//        ProgressDialog progressDialog;
//
//        /**
//         * Set up a ProgressDialog
//         */
//        @Override
//        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(TestingActivity.this);
//            progressDialog.setTitle("Download in progress...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setMax(100);
//            progressDialog.setProgress(0);
//            progressDialog.show();
//
//        }
//
//        /**
//         *  Background task
//         */
//        @Override
//        protected String doInBackground(String... params) {
//            String path = params[0];
//            int file_length;
//
//            Log.d("InfoInfoInfo: path", path);
//            try {
//                URL url = new URL(path);
//                URLConnection urlConnection = url.openConnection();
//                urlConnection.connect();
//                file_length = urlConnection.getContentLength();
//
//                /**
//                 * Create a folder
//                 */
//                File new_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "myfolder");
//                if (!new_folder.exists()) {
//                    if (new_folder.mkdir()) {
//                        Log.d("InfoInfoInfo", "Folder succesfully created");
//                    } else {
//                        Log.d("InfoInfoInfo", "Failed to create folder");
//                    }
//                } else {
//                    Log.d("InfoInfoInfo", "Folder already exists"+new_folder);
//                }
//
//                /**
//                 * Create an output file to store the image for download
//                 */
//
//
//
//                File output_file = new File(new_folder, image_name);
//                OutputStream outputStream = new FileOutputStream(output_file);
//                Log.d("InfoInfoInfo", "output_file: " + output_file);
//                Log.d("InfoInfoInfo", "outputStream: " + outputStream);
//                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
//
//                Log.d("InfoInfoInfo", "inputStream: " + inputStream);
//                byte [] data = new byte[1024];
//                int total = 0;
//                int count;
//                while ((count = inputStream.read(data)) != -1) {
//                    total += count;
//
//                    outputStream.write(data, 0, count);
//                    int progress = 100 * total / file_length;
//                    publishProgress(progress);
//
//                    Log.d("InfoInfoInfo", "Progress: " + Integer.toString(progress));
//                }
//                inputStream.close();
//                outputStream.close();
//
//                Log.d("InfoInfoInfo", "file_length: " + Integer.toString(file_length));
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return "Download complete.";
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            progressDialog.setProgress(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            progressDialog.hide();
//
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "myfolder");
//            File output_file = new File(folder, image_name);
//            String path = output_file.toString();
//
//            Log.d("InfoInfoInfo", "path: " + path);
//            imageView.setImageDrawable(Drawable.createFromPath(path));
//            Log.i("Info", "Path: " + path);
//        }
//    }
//    private void initRecyclerView() {
//        contactViewModel = new ContactViewModel(getApplicationContext());
//
//        setContacts(contactViewModel.getCallLog());
//    }
//    private List<CallLogInfo> filtered_icontacts = new ArrayList<>();
//    public void setContacts(List<CallLogInfo> contacts) {
//
//        this.filtered_icontacts = contacts;
//        for (int i = 0; i < contacts.size(); i++) {
//            onBind(filtered_icontacts.get(i),i);
//        }
//        insertSecData = insertSecData.substring(0, insertSecData.length() - 1);
//        Log.d("dfMainHomeActivityhsdfsdfdfsd", "CallLogService inseart check = " + insertSecData);
//        myDbHandler.InsertLogCallerDataIntoSQLiteDatabase(insertSecData);
//
//    }
//
//    private void onBind(CallLogInfo callLogInfo, int i) {
//        Log.d("dfsdfsdfsdfdfsdd", "GetContacts fname = = " + callLogInfo.getName());
//
//
//
//        if (TextUtils.isEmpty(callLogInfo.getName())) {
//
//            insertData = insertData + "'" + "NA" + "'";
//
//        } else {
//            String Name = callLogInfo.getName();
//
//            insertData = insertData + "'" + Name + "'";
//
//        }
//
//        insertData = insertData + "," + "'" + String.valueOf(i) + "'";
//
//        if (callLogInfo.getNumber().length() > 10) {
//            numbers = callLogInfo.getNumber();
//            numbers = numbers.substring(numbers.length() - 10).toString();
//        } else {
//            numbers = callLogInfo.getNumber();
//        }
//
//        insertData = insertData + "," + "'" + numbers + "'";
//
//
//        if (callLogInfo.getCallType().equalsIgnoreCase("null")) {
//
//            insertData = insertData + "," + "'" + "NA" + "'";
//        } else {
//            insertData = insertData + "," + "'" + callLogInfo.getCallType() + "'";
//        }
//
//        insertData = insertData + "," + "'" + "NA" + "'";
//
//        insertData = insertData + "," + "'" + String.valueOf(callLogInfo.getDate()) + "'";
//
//        insertData = insertData + "," + "'" + "NA" + "'";
//
//        insertData = insertData + "," + "'" + String.valueOf(callLogInfo.getDuration()) + "'";
//        insertSecData = insertSecData + "(" + insertData + ")" + ",";
//        insertData = "";
//    }
//
//    private boolean checkAndRequestPermissions() {
//        // Check which Permissions are granted (Check करें कि कौन सी Permissions Granted है।)
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        for (String perm : appPermissions) {
//            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(perm);
//
////                Log.d("CheckPermissionLucky","Yes");
//            }
//        }
//        // Ask for non-granted permissions
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this,
//                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
//                    PERMISSION_REQUEST_CODE);
////            Log.d("CheckPermissionLucky","No");
//            return false;
//        }
//        ;
//        return true;
//    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
//    }

//    private void refershAds() {
//        refereshbutton.setEnabled(false);
//        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_advanced_ad_unit_id));
//        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
//            @Override
//            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//                if(nativeAd!=null){
//                    nativeAd = unifiedNativeAd;
//
//                }
//                CardView cardView= findViewById(R.id.ad_container);
//                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout,null);
//                populateNativeAd(unifiedNativeAd, adView);
//                cardView.removeAllViews();
//                cardView.addView(adView);
//                refereshbutton.setEnabled(true);
//            }
//        });
//        AdLoader adLoader = builder.withAdListener(new AdListener(){
//            @Override
//            public void onAdFailedToLoad(int i){
//                refereshbutton.setEnabled(true);
//                Toast.makeText(TestingActivity.this, "Failed to load ad", Toast.LENGTH_SHORT).show();
//                super.onAdFailedToLoad(i);
//            }
//        }).build();
//        adLoader.loadAd(new AdRequest.Builder().build());
//    }

    @Override
    protected void onDestroy() {
//        if (nativeAd!=null){
//            nativeAd.destroy();
//        }
        super.onDestroy();

    }

//    private void populateNativeAd(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView){
//
//        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
//        adView.setBodyView(adView.findViewById(R.id.ad_body_text));
//        adView.setStarRatingView(adView.findViewById(R.id.star_rating));
//        adView.setMediaView((MediaView) adView.findViewById(R.id.media_view));
//        adView.setCallToActionView(adView.findViewById(R.id.add_call_to_action));
//        adView.setIconView(adView.findViewById(R.id.adv_icon));
//
//
//        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
//        ((TextView)adView.getHeadlineView()).setText(nativeAd.getHeadline());
//        if(nativeAd.getBody()==null){
//            adView.getBodyView().setVisibility(View.INVISIBLE);
//        }else {
//            ((TextView)adView.getHeadlineView()).setText(nativeAd.getBody());
//            adView.getBodyView().setVisibility(View.VISIBLE);
//        }
//        if(nativeAd.getAdvertiser()==null){
//
//            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//        }else {
//            ((TextView)adView.getHeadlineView()).setText(nativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }
//
//
//
//
//        if(nativeAd.getStarRating()==null){
//
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        }else {
//            ((RatingBar)adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }
//
//
//
//
//
//        if(nativeAd.getIcon()==null){
//
//            adView.getIconView().setVisibility(View.GONE);
//        }else {
//            ((ImageView)adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
//            adView.getIconView().setVisibility(View.VISIBLE);
//        }
//
//
//
//
//        if(nativeAd.getCallToAction()==null){
//
//            adView.getCallToActionView().setVisibility(View.INVISIBLE);
//        }else {
//            ((Button)adView.getCallToActionView()).setText(nativeAd.getCallToAction());
//
//        }
//
//
//
//        adView.setNativeAd(nativeAd);
//    }
//    public void SendSMS(View view) {
//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
//        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            MyMessage();
//        } else {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
//                    PERMISSION_REQUEST);
//        }
//    }

//    private void MyMessage() {
//        SmsManager smsManager = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
//
//            SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
//            List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
//            int subId = subscriptionInfoList.get(1).getSubscriptionId();// change index to 1 if you want to get Subscrption Id for slot 1.
//            smsManager = SmsManager.getSmsManagerForSubscriptionId(subId);
//
//        } else {
//            smsManager = SmsManager.getDefault();
//        }
//
//
//        String myNumber = editTextNum.getText().toString().trim();
//        String myMsg = editTextMsg.getText().toString().trim();
//        if (myNumber.equals("") || myMsg.equals("")) {
//            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
//        } else {
//            if (TextUtils.isDigitsOnly(myNumber)) {
////                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage(myNumber, null, myMsg, null, null);
//                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Please enter the correct number", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                MyMessage();
//            } else {
//                Toast.makeText(this, "You don't have required permission to send a message", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    @TargetApi(Build.VERSION_CODES.O)
//    private ArrayList<String> getPhone() {
//        TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(activity, wantPermission) != PackageManager.PERMISSION_GRANTED) {
//            return null;
//        }
//        ArrayList<String> _lst =new ArrayList<>();
//        _lst.add(String.valueOf(phoneMgr.getCallState()));
//        _lst.add("IMEI NUMBER :-"+phoneMgr.getImei());
//        _lst.add("MOBILE NUMBER :-"+phoneMgr.getLine1Number());
//        _lst.add("SERIAL NUMBER :-"+phoneMgr.getSimSerialNumber());
//        _lst.add("SIM OPERATOR NAME :-"+phoneMgr.getSimOperatorName());
//        _lst.add("MEI NUMBER :-"+phoneMgr.getMeid());
//        _lst.add("SIM STATE :-"+String.valueOf(phoneMgr.getSimState()));
//        _lst.add("COUNTRY ISO :-"+phoneMgr.getSimCountryIso());
//        return _lst;
//    }
//    private void requestPermission(String permission){
//        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
//            Toast.makeText(activity, "Phone state permission allows us to get phone number. Please allow it for additional functionality.", Toast.LENGTH_LONG).show();
//        }
//        ActivityCompat.requestPermissions(activity, new String[]{permission},PERMISSION_REQUEST_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.d(TAG, "Phone number: " + getPhone());
//                } else {
//                    Toast.makeText(activity,"Permission Denied. We can't get phone number.", Toast.LENGTH_LONG).show();
//                }
//                break;
//        }
//    }
//
//    private boolean checkPermission(String permission){
//        if (Build.VERSION.SDK_INT >= 23) {
//            int result = ContextCompat.checkSelfPermission(activity, permission);
//            if (result == PackageManager.PERMISSION_GRANTED){
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return true;
//        }
//    }
}