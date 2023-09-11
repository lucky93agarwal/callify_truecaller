package com.gpslab.kaun.download_notification;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.ResponseBody;

public class BackgroundNotificationDocSendService extends IntentService {

    public BackgroundNotificationDocSendService() {
        super("Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
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

    public JSONObject jsonObject;
    //
    public Socket mSocket;

    public String U_ids;
//    public String doc_name;

    public ArrayList<String> doc_path_path = new ArrayList<>();
    public ArrayList<String> doc_name = new ArrayList<>();
    public ArrayList<String> doc_file_size = new ArrayList<>();
    public ArrayList<String> doc_extension = new ArrayList<>();


//    public String doc_path_path, doc_file_size, doc_extension;

    public String reply_id;
    public int sizesize_doc = 0;
    public int position = 0;
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("uploadExampleImageFile", "message id  2 =   onHandleIntent  ");
        credentials = new BasicAWSCredentials(accesskey, secretkey);

        client = new AmazonS3Client(credentials, Region.getRegion("us-east-1"));
        client.setEndpoint(spaceregion);
        transferUtility = new TransferUtility(client, getApplicationContext());
        CannedAccessControlList filePermission = CannedAccessControlList.PublicRead;
        Bundle args = intent.getBundleExtra("bundle");
        if(doc_path_path.size()>0){
            doc_path_path.clear();
        }
        if(doc_name.size()>0){
            doc_name.clear();
        }
        if(doc_file_size.size()>0){
            doc_file_size.clear();
        }
        if(doc_extension.size()>0){
            doc_extension.clear();
        }
        doc_path_path = args.getStringArrayList("doc_path_path");
        doc_name = args.getStringArrayList("doc_name");
        doc_file_size = args.getStringArrayList("doc_file_size");
        doc_extension = args.getStringArrayList("doc_extension");
//        doc_path_path = intent.getStringExtra("doc_path_path");
        U_ids = intent.getStringExtra("id");
        reply_id = intent.getStringExtra("replay_id");
//        doc_name = intent.getStringExtra("doc_name");
//        doc_file_size = intent.getStringExtra("doc_file_size");
//        doc_extension = intent.getStringExtra("doc_extension");

        position = intent.getIntExtra("position",0);
        sizesize_doc = intent.getIntExtra("sizesize_doc",0);


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
                .setContentText("Uploading Document")
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        initRetrofit(doc_path_path.get(0),doc_name.get(0),doc_extension.get(0),doc_file_size.get(0),position);

    }

    public File exampleFile;



    private void initRetrofit(String path,String docname,String extenction,String doc_size, int postion_new) {
        File file = new File(path);

        RealmHelper.getInstance().updateDownloadUploadStat(docname, DownloadUploadStat.LOADING);
        observer = transferUtility.upload(
                spacename + "/callify/upload/docs", //empty bucket name, included in endpoint
                docname + extenction,
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

                        int check_progress = sharedPreferences.getInt("pause_and_resume_doc",1);
                        if(check_progress == 1){
                            transferUtility.pause(id);
                            Log.d("PlayButtonclickFile", "Pause =     "+1 );

                            editor.putBoolean("check_pro_doc",false);
                            editor.putInt("pause_and_resume_doc",0);
                            editor.apply();
                        }else if(check_progress == 2){
                            transferUtility.resume(id);
                            Log.d("PlayButtonclickFile", "Resume =     "+2 );
                            editor.putBoolean("check_pro_doc",true);
                            editor.putInt("pause_and_resume_doc",0);
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

                    Log.d("uploadExampleImageFile", "COMPLETED id  2 =     " );
                    RealmHelper.getInstance().updateDownloadUploadStat(docname, DownloadUploadStat.SUCCESS);


                    sendmessageDoc(U_ids, docname + extenction + "," + doc_size, MessageType.SENT_FILE, docname, sizesize_doc, postion_new,reply_id);
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
                Log.d("PlayButtonclickFile", "progress_in_per  1    =     " + y);
                editor.putBoolean("check_pro_doc",true);
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

    private void sendmessageDoc(String number, String Messgedata, int type, String ids, int arraysize, int position,String reply_id) {

        Log.d("CheckdocSize", "number 1 = " + number);
        Log.d("CheckdocSize", "Messgedata 1 = " + Messgedata);

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
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        try {
            jsonObject = new JSONObject();
            jsonObject.put("receiver", number);
            jsonObject.put("sender", sharedPreferences.getString("mobile", ""));
            jsonObject.put("msg", Messgedata);
            jsonObject.put("type", String.valueOf(type));
            jsonObject.put("message_id", ids);
            jsonObject.put("reply_id", reply_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("CheckdocSize", "jsonObject 1 = " + jsonObject);
        mSocket.emit("message", jsonObject);
        Log.d("CheckdocSize", "image_name total size  =   " + arraysize);
        if (position < arraysize) {
            Log.d("CheckdocSize", "image_name position  =   0 " + position);
            position = position + 1;
            Log.d("CheckdocSize", "image_name position  =   1 " + position);
//            ((ChatActivity) _context).replydatadoc(position);
            replydatavideo(position);
//            edit.putInt("Docs_position",position);
//            edit.putInt("Docs_check",1);
//            edit.apply();
        }
    }

    private void replydatavideo(int postion_new) {
        initRetrofit(doc_path_path.get(postion_new),doc_name.get(postion_new),doc_extension.get(postion_new),doc_file_size.get(postion_new),postion_new);
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
//        LocalBroadcastManager.getInstance(BackgroundNotificationDocSendService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
        notificationBuilder.setContentText("Document Upload Complete");
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);


    }

}


