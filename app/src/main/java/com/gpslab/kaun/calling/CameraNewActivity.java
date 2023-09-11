package com.gpslab.kaun.calling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.gpslab.kaun.R;
import com.gpslab.kaun.digitaloceanspaces.RandomString;
import com.gpslab.kaun.fragment.ChatFragment;
import com.gpslab.kaun.fragment.ImageReplayinActivity;
import com.gpslab.kaun.model.Constants;
import com.gpslab.kaun.view.DeleteStatusJob;
import com.gpslab.kaun.view.ExifUtil;
import com.gpslab.kaun.view.IntentUtils;
import com.gpslab.kaun.view.RealmHelper;
import com.gpslab.kaun.view.ResultCodes;
import com.gpslab.kaun.view.Status;
import com.gpslab.kaun.view.StatusCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;
import io.socket.client.IO;
import io.socket.client.Socket;

public class CameraNewActivity extends AppCompatActivity {
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    RecyclerView recyclerView;
    MyAdapter myAdapter;


    public String openPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_new);


        openPage = getIntent().getStringExtra("openPage");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(this);
        recyclerView.setAdapter(myAdapter);


        if(openPage.equalsIgnoreCase("chatActivity")){
            Options options = Options.init()
                    .setRequestCode(100)                                           //Request code for activity results
                    .setCount(1)                                                   //Number of images to restict selection count
                    .setFrontfacing(false)                             //Pre selected Image Urls
                    .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                    .setMode(Options.Mode.All)                                     //Option to select only pictures or videos or both
                    .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                    .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                    .setPath("/pix/images");
            Pix.start(CameraNewActivity.this, options);
        }else {
            Options options = Options.init()
                    .setRequestCode(100)                                           //Request code for activity results
                    .setCount(3)                                                   //Number of images to restict selection count
                    .setFrontfacing(false)                             //Pre selected Image Urls
                    .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                    .setMode(Options.Mode.All)                                     //Option to select only pictures or videos or both
                    .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                    .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                    .setPath("/pix/images");
            Pix.start(CameraNewActivity.this, options);
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("valonActivityResult", "requestCode ->  " + requestCode+"  resultCode "+resultCode);
        switch (requestCode) {
            case (100): {
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    myAdapter.AddImage(returnValue);
                    String path = returnValue.get(0);
                    path = path.substring(path.length() - 3);
                    Log.d("valonActivityResult", "path ->  " + path);
                    Log.d("valonActivityResult", "path ->  " + returnValue);

                    if(openPage.equalsIgnoreCase("camerafragment")){
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("returnValue", returnValue);
                        Intent intent = new Intent(CameraNewActivity.this, ImageReplayinActivity.class);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                        finish();
                    }else if(openPage.equalsIgnoreCase("statusfragment")){
                        file_path_path = returnValue;
                        setResult(ResultCodes.PICK_IMAGE_FROM_CAMERA, data);
                        finish();
                    }else if(openPage.equalsIgnoreCase("chatActivity")){

                        if(path.equalsIgnoreCase("mp4")){
                            file_path_path = returnValue;
                            setResult(ResultCodes.VIDEO_RECORD_SUCCESS, data);
                            finish();
                        }else {
                            file_path_path = returnValue;
                            setResult(ResultCodes.PICK_IMAGE_FROM_CAMERA, data);
                            finish();
                        }

                    }

//                    onBackPressed();
                    /*for (String s : returnValue) {
                        Log.e("val", " ->  " + s);
                    }*/
                }else {
                    onBackPressed();
                }
            }
            break;
        }
    }
    public int sizesize = 0;
    public static String size(long size) {
        Log.d("WalletScrollCheck", "Scroll Check 52");
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    byte[] CDRIVES;
    public String encoded = "null";
    public RelativeLayout mRelativeLayout;
    public ByteArrayOutputStream byteArrayOutputStream;
    public ArrayList<String> file_path_path = new ArrayList<>();
    private void getData() {


        Log.d("valonActivityResult", " ImageReplayinActivity  path ->  " + file_path_path);




        if (lengthsizeArry.size() > 0) {
            lengthsizeArry.clear();
        }
        if (ImageCDRIVES.size() > 0) {
            ImageCDRIVES.clear();
        }
        if (ImageName.size() > 0) {
            ImageName.clear();
        }
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();




        for (String imagePath : file_path_path) {
            Status status = new Status();
            status = StatusCreator.createImageStatus(imagePath,sharedPreferences.getString("mobile", "0000"));
            status.setContent("");
            RealmHelper.getInstance().saveStatus(sharedPreferences.getString("mobile", "0000"), status);
            DeleteStatusJob.schedule(status.getUserId(), status.getStatusId());
            Log.d("checuploadVideoStatusmentmmPathsid", "uploadImageStatus mmPathsid  ==   " + imagePath);



            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            RandomString randomString = new RandomString();
            String image_name = ts + "_" + randomString.nextString();

//            Log.d("WalletCheckUpdate", "ChatActivity message id =   " + image_name);

//            ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());


            String pathpath = imagePath;
            File imgFile = new  File(pathpath);
            Bitmap bm = BitmapFactory.decodeFile(pathpath);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Bitmap orientedBitmap = ExifUtil.rotateBitmap(imagePath, myBitmap);

            byteArrayOutputStream = new ByteArrayOutputStream();
            orientedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            CDRIVES = byteArrayOutputStream.toByteArray();
            encoded = Base64.encodeToString(CDRIVES, Base64.DEFAULT);
            long lengthbmp = CDRIVES.length;
            String lengthsize = size(lengthbmp);
//            Log.d("WalletCheckUpdate", "Image Path  =   1 " + imagePath);
//            updateChat(message);



            lengthsizeArry.add(lengthsize);
            ImageCDRIVES.add(CDRIVES);
            ImageName.add(image_name);


//            ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
//            addpopup.sendmessage(getIntent().getStringExtra("id"),"data:image/jpeg;base64," +encodedtwo,MessageType.SENT_IMAGE);
        }

        statuses = RealmHelper.getInstance().getUserStatuses(sharedPreferences.getString("mobile","00000000")).getFilteredStatuses();


        sizesize = file_path_path.size() - 1;



        send_data_from_server(ImageCDRIVES.get(0), ImageName.get(0), lengthsizeArry.get(0), sizesize, 0 );




    }
    String accesskey = "4SYUKBCFA4KASIHESCTP";
    String secretkey = "cWCsWzfqX4h1Y6ULx+8OE0hrPjf5CCiDPFOSIdkxOSA";
    String spacename = "gpslabindia";

    String spaceregion = "https://nyc3.digitaloceanspaces.com";

    public AmazonS3Client client;
    public BasicAWSCredentials credentials;
    public TransferObserver observer;
    public TransferUtility transferUtility;
    RealmResults<Status> statuses;
    public List<String> ImageName = new ArrayList();
    public List<byte[]> ImageCDRIVES = new ArrayList();
    public List<String> lengthsizeArry = new ArrayList<>();
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    public void send_data_from_server(byte[] ImageCDRIVES, String ImageName, String lengthsizeArry, int sizesize, int position ){
        credentials = new BasicAWSCredentials(accesskey, secretkey);

        client = new AmazonS3Client(credentials, Region.getRegion("us-east-1"));
        client.setEndpoint(spaceregion);
        transferUtility = new TransferUtility(client, CameraNewActivity.this);
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


        notificationBuilder = new NotificationCompat.Builder(CameraNewActivity.this, "id")
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Upload")
                .setContentText("Uploading Image")
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());




        observer = transferUtility.upload(
                spacename + "/callify/upload/story", //empty bucket name, included in endpoint
                ImageName + ".jpeg",
                convertKanuImageResourceToFile(ImageCDRIVES), //a File object that you want to upload
                filePermission
        );

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state.COMPLETED.equals(observer.getState())) {
                    onDownloadComplete(true);

                    Log.d("CheckPublic", "COMPLETED id  2 =     " );







                    sendmessageImage( ImageName + ".jpeg", sizesize, position, ImageName);
//                    addpopup.sendmessage(U_ids, image_name + ".jpeg," + size, MessageType.SENT_IMAGE, image_name);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {


                double minus = bytesTotal - bytesCurrent;
//                Log.d("WalletCheckUpdate", "minus  2 =     " + minus);
                double divided = minus / bytesTotal;
//                Log.d("WalletCheckUpdate", "divided  2 =     " + divided);
                double multi = divided *100;
//                Log.d("WalletCheckUpdate", "divide id  2 =     " + multi);
                double sub = 100- multi;


                Integer y = (int) sub;
//                Log.d("WalletCheckUpdate", "sub id  2 =     " + y);


                updateNotification(y);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d("WalletCheckUpdate", "error id  2 =     " + ex.getMessage());
//                Toast.makeText(activity, "Space upload error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateNotification(int currentProgress) {

//        Log.d("WalletCheckUpdate", "updateNotification id  2 =   counter =>  " + currentProgress);
        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentText("Upload: " + currentProgress + "%");
        notificationManager.notify(0, notificationBuilder.build());
    }
    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
        notificationBuilder.setContentText("Image Upload Complete");
        notificationManager.notify(0, notificationBuilder.build());

    }
    private void sendProgressUpdate(boolean downloadComplete) {

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
    public Socket mSocket;
    public JSONObject jsonObject;
    public List<String> videoPath = new ArrayList<>();
    public List<String> ImagePathPath = new ArrayList<>();

    public JSONArray jsonArray = new JSONArray();

    public JSONArray videojsonArray = new JSONArray();
    private void sendmessageImage(String image_path,int sizesize, int position, String image_name) {

        videojsonArray.put("NA");
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        edit.putString("story_check","1");
        edit.putString("story_name",image_path);
        edit.apply();
        jsonArray.put(image_path);

//        jsonArray.put("1615018127");
        Log.d("CheckPublic", "image_name position  =   " + image_path);
        Log.d("WalletCheckUpdate", "image_name sizesize  =   " + sizesize);

        if (position < sizesize) {
            Log.d("WalletCheckUpdate", "image_name position  =   " + position);
            position = position + 1;
            Log.d("WalletCheckUpdate", "image_name position  =   " + position);




            send_data_from_server(ImageCDRIVES.get(position), ImageName.get(position), lengthsizeArry.get(position), sizesize, position);
//            ((ChatActivity) _context).replydataimage(position);
//            edit.putInt("Image_position",position);
//            edit.putInt("Image_check",1);
//            edit.apply();
        }else {
            {
                try {
                    mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                mSocket.connect();


                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("userName", sharedPreferences.getString("mobile", ""));
                    jsonObject.put("roomName", "GPSLAB");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("WalletlsjWallet", "onConnect 1 = ");
                mSocket.emit("subscribe", jsonObject);
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("image", jsonArray);
                    jsonObject.put("user_mobile", sharedPreferences.getString("mobile", ""));
                    jsonObject.put("video", videojsonArray);
                    jsonObject.put("is_public", sharedPreferences.getString("public_value","1"));
                    jsonObject.put("is_private", sharedPreferences.getString("private_value","0"));

//                    jsonObject.put("image", "Lucky Agarwal");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("CheckPublic","Socket   ===     "+jsonObject);
                Log.d("WalletCheckUpdate", "EMIT  =   0 " + jsonObject);
                mSocket.emit("send_story", jsonObject);
                Log.d("WalletCheckUpdate", "EMIT  =   1 " + jsonObject);


//                for(int i=0;i<jsonArray.length();i++){
//                    jsonArray.remove(i);
//                }


//                if(checkImageClick){
//                    checkImageClick = false;
//                    replydataimage();
//                }

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Options options = Options.init()
                            .setRequestCode(100)                                           //Request code for activity results
                            .setCount(3)                                                   //Number of images to restict selection count
                            .setFrontfacing(false)                             //Pre selected Image Urls
                            .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                            .setMode(Options.Mode.All)                                     //Option to select only pictures or videos or both
                            .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                            .setPath("/pix/images");
                    Pix.start(CameraNewActivity.this, options);
                } else {
                    Toast.makeText(CameraNewActivity.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("CheckBackButton","onBackPressed   =       1");

        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        edit.putInt("CheckKnow",1);
        edit.apply();
    }
}