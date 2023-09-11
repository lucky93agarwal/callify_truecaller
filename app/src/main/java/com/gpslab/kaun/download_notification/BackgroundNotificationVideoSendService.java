package com.gpslab.kaun.download_notification;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.gpslab.kaun.Webapi.ChatWebAPI;
import com.gpslab.kaun.model.Constants;
import com.gpslab.kaun.view.BitmapUtils;

import com.gpslab.kaun.view.DownloadUploadStat;
import com.gpslab.kaun.view.MessageType;
import com.gpslab.kaun.view.RealmHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.ResponseBody;

public class BackgroundNotificationVideoSendService extends IntentService {

    public BackgroundNotificationVideoSendService() {
        super("Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;


    ///
    String accesskey = "4SYUKBCFA4KASIHESCTP";
    String secretkey = "cWCsWzfqX4h1Y6ULx+8OE0hrPjf5CCiDPFOSIdkxOSA";
    String spacename = "gpslabindia";

    String spaceregion = "https://nyc3.digitaloceanspaces.com";

    CannedAccessControlList filePermission = CannedAccessControlList.PublicRead;

    public TransferUtility transferUtility;
    public int counter = 0;

    public AmazonS3Client client;
    public BasicAWSCredentials credentials;
    public TransferObserver observer;
    public JSONObject jsonObject;
    //
    public Socket mSocket;

    //


    public String U_ids;

    public int sizesize_video;


    public String replay_id;

    public ArrayList<String> file_path_path = new ArrayList<>();
    public ArrayList<String> video_name_array = new ArrayList<>();
    public ArrayList<String> video_size_array = new ArrayList<>();
    public ArrayList<String> video_extension_array = new ArrayList<>();

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("VideoCheckknow", "message id  2 =   onHandleIntent  ");
        credentials = new BasicAWSCredentials(accesskey, secretkey);

        client = new AmazonS3Client(credentials, Region.getRegion("us-east-1"));
        client.setEndpoint(spaceregion);
        transferUtility = new TransferUtility(client, getApplicationContext());
        CannedAccessControlList filePermission = CannedAccessControlList.PublicRead;



        U_ids = intent.getStringExtra("id");
        replay_id = intent.getStringExtra("replay_id");

        sizesize_video =  intent.getIntExtra("sizesize_video",0);
        Bundle args = intent.getBundleExtra("bundle");
        if(file_path_path.size()>0){
            file_path_path.clear();
        }
        if(video_name_array.size()>0){
            video_name_array.clear();
        }
        if(video_size_array.size()>0){
            video_size_array.clear();
        }
        if(video_extension_array.size()>0){
            video_extension_array.clear();
        }
        file_path_path = args.getStringArrayList("filePath_array");
        video_name_array = args.getStringArrayList("video_name_array");
        video_size_array = args.getStringArrayList("video_size_array");
        video_extension_array = args.getStringArrayList("video_extension_array");


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
                .setContentText("Uploading Video")
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        intiThumupload(video_name_array.get(0),file_path_path.get(0));
        initRetrofit(video_name_array.get(0),video_extension_array.get(0),file_path_path.get(0),video_size_array.get(0),sizesize_video,0);

    }
    private File storeImage(Bitmap image, String video_name) {
        Log.d("WalletScrollCheck","Scroll Check 48");
        File pictureFile = getOutputMediaFile(video_name);
        if (pictureFile == null) {
            Log.i("VideoCheckknow",
                    "Error creating media file, check storage permissions: ");// e.getMessage());

        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }


        return pictureFile;
    }
    private File getOutputMediaFile(String video_name) {
        Log.d("WalletScrollCheck","Scroll Check 49");
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = video_name + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
    public Bitmap StringToBitMap(String encodedString) {
        Log.d("WalletScrollCheck","Scroll Check 47");
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    private void intiThumupload(String thum_name,String path) {
        Bitmap videoThumbBitmap = BitmapUtils.getThumbnailFromVideo(path);
        //generate blurred thumb to send it to other user
        String thumb = BitmapUtils.decodeImage(videoThumbBitmap);
        File Imagefile = storeImage(StringToBitMap(thumb), thum_name);

        observer = transferUtility.upload(
                spacename +  "/callify/upload/videos/thumb", //empty bucket name, included in endpoint
                thum_name + ".jpeg",
                Imagefile, //a File object that you want to upload
                filePermission
        );

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

                if (state.COMPLETED.equals(observer.getState())) {



                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {


            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d("uploadExampleImageFile", "error id  2 =     " + ex.getMessage());
//                Toast.makeText(activity, "Space upload error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public File exampleFile;



    private void initRetrofit(String video_name,String video_extension,String path,String video_size, int sizesize_video,int position_new) {

        File file = new File(path);
        RealmHelper.getInstance().updateDownloadUploadStat(video_name, DownloadUploadStat.LOADING);
        observer = transferUtility.upload(
                spacename + "/callify/upload/videos/video_data", //empty bucket name, included in endpoint
                video_name + video_extension,
                file, //a File object that you want to upload
                filePermission
        );

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        SharedPreferences sharedPreferences = getSharedPreferences("ChatData",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        int check_progress = sharedPreferences.getInt("pause_and_resume_video",1);
                        if(check_progress == 1){
                            transferUtility.pause(id);
                            Log.d("PlayButtonclickVideo", "Pause =     "+1 );

                            editor.putBoolean("check_pro_video",false);
                            editor.putInt("pause_and_resume_video",0);
                            editor.apply();
                        }else if(check_progress == 2){
                            transferUtility.resume(id);
                            Log.d("PlayButtonclickVideo", "Resume =     "+2 );
                            editor.putBoolean("check_pro_video",true);
                            editor.putInt("pause_and_resume_video",0);
                            editor.apply();
                        }
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {

                    }

                }.start();

                if(state.CANCELED.equals(observer.getState())){
                    Log.d("uploadExampleImageFile", "CANCELED id  2 =     " );
                }
                if (state.COMPLETED.equals(observer.getState())) {
                    onDownloadComplete(true);

                    Log.d("PlayButtonclickVideo", "COMPLETED id  2 =     "+video_name );
                    RealmHelper.getInstance().updateDownloadUploadStat(video_name, DownloadUploadStat.SUCCESS);
                    sendmessageVideo(U_ids, video_name + video_extension + "," + video_size, MessageType.SENT_VIDEO, video_name, sizesize_video, position_new,replay_id);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                SharedPreferences sharedPreferences = getSharedPreferences("ChatData",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                double minus = bytesTotal - bytesCurrent;
                Log.d("uploadExampmageFile", "minus  2 =     " + minus);
                double divided = minus / bytesTotal;
                Log.d("uploadExampmageFile", "divided  2 =     " + divided);
                double multi = divided *100;
                Log.d("uploadExampmageFile", "divide id  2 =     " + multi);
                double sub = 100- multi;


                Integer y = (int) sub;
                Log.d("PlayButtonclickVideo", "progress_in_per  1    =     " + y);
                editor.putBoolean("check_pro_video",true);
                editor.putInt("progress_in_per",y);
                editor.apply();


                updateNotification(y);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d("uploadExampleImageFile", "error id  2 =     " + ex.getMessage());
//                Toast.makeText(activity, "Space upload error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void sendmessageVideo(String number, String Messgedata, int type, String ids, int arraysize, int position,String replay_id) {
        Log.d("CheckChatAPI", "number 1 = " + number);
        Log.d("CheckChatAPI", "Messgedata 1 = " + Messgedata);

//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("CheckChatAPI", "connected 1 = " + "hello");
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", number);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
            jsonObject.put("msg", Messgedata);
            jsonObject.put("type", String.valueOf(type));
            jsonObject.put("message_id", ids);
            jsonObject.put("reply_id",replay_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("CheckChatAPI", "jsonObject 1 = " + jsonObject);
        mSocket.emit("message", jsonObject);
        Log.d("WalletCheckLucky", "image_name total size  =   " + arraysize);
        if (position < arraysize) {
            Log.d("WalletCheckLucky", "image_name position  =   0 " + position);
            position = position + 1;
            Log.d("WalletCheckLucky", "image_name position  =   1 " + position);
            replydatavideo(position);
        }
    }

    private void replydatavideo(int position) {
        intiThumupload(video_name_array.get(position),file_path_path.get(position));
        initRetrofit(video_name_array.get(position),video_extension_array.get(position),file_path_path.get(position),video_size_array.get(position),sizesize_video,position);
    }

    private void downloadImage(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream inputStream = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "journaldev-image-downloaded.jpg");
        OutputStream outputStream = new FileOutputStream(outputFile);
        long total = 0;
        boolean downloadComplete = false;
        //int totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));

        while ((count = inputStream.read(data)) != -1) {

            total += count;
            int progress = (int) ((double) (total * 100) / (double) fileSize);


            updateNotification(progress);
            outputStream.write(data, 0, count);
            downloadComplete = true;
        }
        onDownloadComplete(downloadComplete);
        outputStream.flush();
        outputStream.close();
        inputStream.close();
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
//        LocalBroadcastManager.getInstance(BackgroundNotificationVideoSendService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
        notificationBuilder.setContentText("Video Upload Complete");
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);


    }

}


