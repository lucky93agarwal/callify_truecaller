package com.gpslab.kaun.download_notification;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
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
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.ResponseBody;

public class BackgroundNotificationAudioSendService extends IntentService {

    public BackgroundNotificationAudioSendService() {
        super("Service");
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
    public Socket mSocket;
    public AmazonS3Client client;
    public BasicAWSCredentials credentials;
    public TransferObserver observer;


    //

    public String replay_id;

    public String U_ids;
    public String image_name;

    public String filePath;
    public String audioDuration;
    public String extension;

    public JSONObject jsonObject;


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("uploadExampleImageFile", "message id  2 =   onHandleIntent  ");
        credentials = new BasicAWSCredentials(accesskey, secretkey);

        client = new AmazonS3Client(credentials, Region.getRegion("us-east-1"));
        client.setEndpoint(spaceregion);
        transferUtility = new TransferUtility(client, getApplicationContext());
        CannedAccessControlList filePermission = CannedAccessControlList.PublicRead;



        U_ids = intent.getStringExtra("id");
        filePath = intent.getStringExtra("filePath");

        image_name = intent.getStringExtra("image_name");

        audioDuration = intent.getStringExtra("audioDuration");
        extension = intent.getStringExtra("extension");
        replay_id = intent.getStringExtra("replay_id");

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
                .setContentText("Uploading Song")
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        initRetrofit();

    }

    public File exampleFile;



    private void initRetrofit() {
        File file = new File(filePath);

        RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.LOADING);
        observer = transferUtility.upload(
                spacename + "/callify/upload/audio", //empty bucket name, included in endpoint
                image_name + extension,
                file, //a File object that you want to upload
                filePermission
        );
        observer.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
//                transferUtility.cancel(id);
                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        SharedPreferences sharedPreferences = getSharedPreferences("ChatData",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        int check_progress = sharedPreferences.getInt("pause_and_resume",1);
                        if(check_progress == 1){
                            transferUtility.pause(id);
                            Log.d("uploadExampleImageFile", "Pause =     "+1 );

                            editor.putBoolean("check_pro",false);
                            editor.putInt("pause_and_resume",0);
                            editor.apply();
                        }else if(check_progress == 2){
                            transferUtility.resume(id);
                            Log.d("uploadExampleImageFile", "Resume =     "+2 );
                            editor.putBoolean("check_pro",true);
                            editor.putInt("pause_and_resume",0);
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

                    Log.d("Checkrcoarding", "COMPLETED id  2 =     " );
                    RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.SUCCESS);
                    ChatWebAPI addpopup = new ChatWebAPI(getApplicationContext());
                    sendmessage(U_ids, image_name + extension + "," + audioDuration, MessageType.SENT_AUDIO, image_name,replay_id);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {



                SharedPreferences sharedPreferences = getSharedPreferences("ChatData",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();




                double minus = bytesTotal - bytesCurrent;

                double divided = minus / bytesTotal;

                double multi = divided *100;

                double sub = 100- multi;


                Integer y = (int) sub;
                Log.d("uploadExampmageFile", "sub id  2 =     " + y);

                editor.putBoolean("check_pro",true);
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

    private void sendmessage(String number, String Messgedata, int type, String ids,String replay_id) {
        Log.d("Checkrcoarding", "number 1 = " + number);
        Log.d("Checkrcoarding", "Messgedata 1 = " + Messgedata);

//
        {
            try {
                mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();
        Log.d("Checkrcoarding", "connected 1 = " + "hello");
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
        Log.d("Checkrcoarding", "jsonObject  = " + jsonObject);
        mSocket.emit("message", jsonObject);

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
//        LocalBroadcastManager.getInstance(BackgroundNotificationAudioSendService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
        notificationBuilder.setContentText("Song Upload Complete");
        notificationManager.notify(0, notificationBuilder.build());

    }
    private void onDownloadConcel(boolean downloadComplete) {


        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
        notificationBuilder.setContentText("Song Upload Cancel");
        notificationManager.notify(0, notificationBuilder.build());

    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);


    }

}


