package com.gpslab.kaun.Service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.ResponseBody;

public class BackgroundNotificationImageSToryService extends IntentService {

    public BackgroundNotificationImageSToryService() {
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

    public AmazonS3Client client;
    public BasicAWSCredentials credentials;
    public TransferObserver observer;


        public SharedPreferences sharedPreferences;
        public SharedPreferences.Editor edit;



    public String image_name;
    public String size;

    public byte[] encodedtwo;


    public int sizesize;
    public int position;


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("WalletCheckUpdate", "message id  2 =   onHandleIntent  ");
        credentials = new BasicAWSCredentials(accesskey, secretkey);

        client = new AmazonS3Client(credentials, Region.getRegion("us-east-1"));
        client.setEndpoint(spaceregion);
        transferUtility = new TransferUtility(client, getApplicationContext());
        CannedAccessControlList filePermission = CannedAccessControlList.PublicRead;

        position = intent.getIntExtra("position",0);
        Log.d("WalletCheckUpdate", "position id  2 =     " +position);

        sizesize = intent.getIntExtra("sizesize",0);
        Log.d("WalletCheckUpdate", "sizesize id  2 =     "+ sizesize);



        encodedtwo = intent.getByteArrayExtra("encodedtwo");
        Log.d("WalletCheckUpdate", "encodedtwo id  2 =     "+encodedtwo );


        image_name = intent.getStringExtra("image_name");
        Log.d("WalletCheckUpdate", "image_name id  2 =     "+image_name );



        size = intent.getStringExtra("lengthsize");
        Log.d("WalletCheckUpdate", "size id  2 =     " +size);







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

        initRetrofit();

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

    private void initRetrofit() {

        RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.LOADING);
        observer = transferUtility.upload(
                spacename + "/callify/upload/story", //empty bucket name, included in endpoint
                image_name + ".jpeg",
                convertKanuImageResourceToFile(encodedtwo), //a File object that you want to upload
                filePermission
        );
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state.COMPLETED.equals(observer.getState())) {
                    onDownloadComplete(true);

                    Log.d("WalletCheckUpdate", "COMPLETED id  2 =     " );







                    sendmessageImage( image_name + ".jpeg," + size, MessageType.SENT_IMAGE, sizesize, position, image_name);
//                    addpopup.sendmessage(U_ids, image_name + ".jpeg," + size, MessageType.SENT_IMAGE, image_name);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {


                double minus = bytesTotal - bytesCurrent;
                Log.d("WalletCheckUpdate", "minus  2 =     " + minus);
                double divided = minus / bytesTotal;
                Log.d("WalletCheckUpdate", "divided  2 =     " + divided);
                double multi = divided *100;
                Log.d("WalletCheckUpdate", "divide id  2 =     " + multi);
                double sub = 100- multi;


                Integer y = (int) sub;
                Log.d("WalletCheckUpdate", "sub id  2 =     " + y);


                updateNotification(y);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d("WalletCheckUpdate", "error id  2 =     " + ex.getMessage());
//                Toast.makeText(activity, "Space upload error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    public Socket mSocket;
    public JSONObject jsonObject;
    public List<String> imagePath = new ArrayList<>();
    public List<String> videoPath = new ArrayList<>();
    private void sendmessageImage(String image_path, int sentImage, int sizesize, int position, String image_name) {
        imagePath.add(image_path);
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        if (position < sizesize) {
            Log.d("WalletCheckUpdate", "image_name position  =   0 " + position);
            position = position + 1;
            Log.d("WalletCheckUpdate", "image_name position  =   1 " + position);
//            ((ChatActivity) _context).replydataimage(position);
            edit.putInt("Image_position",position);
            edit.putInt("Image_check",1);
            edit.apply();
        }else {
            {
                try {
                    mSocket = IO.socket(Constants.NewCHAT_SERVER_URL);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }

                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("image", imagePath);
                    jsonObject.put("user_mobile", sharedPreferences.getString("mobile", ""));
                    jsonObject.put("video", videoPath);
                    jsonObject.put("is_public", "1");
                    jsonObject.put("is_private", "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mSocket.emit("send_story", jsonObject);
            }
        }
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

        Log.d("WalletCheckUpdate", "updateNotification id  2 =   counter =>  " + currentProgress);
        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentText("Upload: " + currentProgress + "%");
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void sendProgressUpdate(boolean downloadComplete) {

//        Intent intent = new Intent(MainActivity.PROGRESS_UPDATE);
//        intent.putExtra("downloadComplete", downloadComplete);
//        LocalBroadcastManager.getInstance(BackgroundNotificationImageSToryService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
        notificationBuilder.setContentText("Image Upload Complete");
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);


    }

}



