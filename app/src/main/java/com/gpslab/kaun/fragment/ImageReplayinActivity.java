package com.gpslab.kaun.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.View;
import android.widget.RelativeLayout;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devlomi.circularstatusview.CircularStatusView;
import com.gpslab.kaun.Contact.ContactListActivity;
import com.gpslab.kaun.DB.GetChatContactList;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.R;
import com.gpslab.kaun.adapter.ChatsAdapter;
import com.gpslab.kaun.adapter.ForwardAdapter;
import com.gpslab.kaun.digitaloceanspaces.RandomString;

import com.gpslab.kaun.download_notification.BackgroundNotificationImageGalleryService;
import com.gpslab.kaun.fullscreen.MimeTypes;
import com.gpslab.kaun.model.Constants;
import com.gpslab.kaun.view.ChatActivity;
import com.gpslab.kaun.view.DeleteStatusJob;
import com.gpslab.kaun.view.DownloadUploadStat;
import com.gpslab.kaun.view.ExifUtil;
import com.gpslab.kaun.view.ForwardChat;
import com.gpslab.kaun.view.IntentUtils;
import com.gpslab.kaun.view.Message;
import com.gpslab.kaun.view.MessageCreator;
import com.gpslab.kaun.view.MessageType;
import com.gpslab.kaun.view.RealmHelper;
import com.gpslab.kaun.view.Status;
import com.gpslab.kaun.view.StatusCreator;
import com.gpslab.kaun.view.User;

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

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;
import io.socket.client.IO;
import io.socket.client.Socket;

public class ImageReplayinActivity extends AppCompatActivity implements ForwardAdapter.OnUserClick {
    public CircularStatusView ivCircularStatusView;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    RealmResults<Status> statuses;
    public CircleImageView ivprofile_image;
    public ByteArrayOutputStream byteArrayOutputStream;

    byte[] CDRIVES;
    public String encoded = "null";
    public RelativeLayout mRelativeLayout;
    public String path ="NA";
    public ArrayList<String> file_path_path = new ArrayList<>();



    String accesskey = "4SYUKBCFA4KASIHESCTP";
    String secretkey = "cWCsWzfqX4h1Y6ULx+8OE0hrPjf5CCiDPFOSIdkxOSA";
    String spacename = "gpslabindia";

    String spaceregion = "https://nyc3.digitaloceanspaces.com";

    public AmazonS3Client client;
    public BasicAWSCredentials credentials;
    public TransferObserver observer;
    public TransferUtility transferUtility;
    private ForwardAdapter adapter;

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private RecyclerView rvStatus;
    private ArrayList<ForwardChat> chats;
    public MyDbHandler myDbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_replayin);

        initialize();


        onclick();




        onRecyclerView();



    }

    private void onRecyclerView() {
        ArrayList<GetChatContactList> arrayList2 = myDbHandler.viewChatContact();

        if(arrayList2.size()>0){


            for(int i=0;i<arrayList2.size();i++){
                chats.add(new ForwardChat(arrayList2.get(i).getContacts_image(), arrayList2.get(i).getContacts_name(), arrayList2.get(i).getContacts_status(), "",arrayList2.get(i).getContacts_id(),file_path_path));

//                user.setUserName(arrayList2.get(i).getContacts_name());
//                user.setUid(arrayList2.get(i).getContacts_id());
//                user.setPhone(arrayList2.get(i).getContacts_id());
//                user.setStatus(arrayList2.get(i).getContacts_status());
//                user.setPhoto(arrayList2.get(i).getContacts_image());
//                user.setThumbImg(arrayList2.get(i).getContacts_image());
//
//
//                RealmHelper.getInstance().saveEmptyChat(user);
            }
            adapter = new ForwardAdapter(ImageReplayinActivity.this, chats,this);
            rvStatus.setLayoutManager(new LinearLayoutManager(ImageReplayinActivity.this));
            rvStatus.setAdapter(adapter);
        }else {
//            SendDataToServer("");
        }
    }

    public List<String> ImageName = new ArrayList();
    public List<byte[]> ImageCDRIVES = new ArrayList();
    public List<String> lengthsizeArry = new ArrayList<>();
    private void getData() {
        Bundle args = getIntent().getBundleExtra("bundle");
        file_path_path = args.getStringArrayList("returnValue");

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
            ivprofile_image.setImageBitmap(orientedBitmap);
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

        ivCircularStatusView.setPortionsCount(statuses.size());
        sizesize = file_path_path.size() - 1;



        send_data_from_server(ImageCDRIVES.get(0), ImageName.get(0), lengthsizeArry.get(0), sizesize, 0 );



        onBackPressed();
    }


    public void send_data_from_server(byte[] ImageCDRIVES, String ImageName, String lengthsizeArry, int sizesize, int position ){
        credentials = new BasicAWSCredentials(accesskey, secretkey);

        client = new AmazonS3Client(credentials, Region.getRegion("us-east-1"));
        client.setEndpoint(spaceregion);
        transferUtility = new TransferUtility(client, ImageReplayinActivity.this);
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


        notificationBuilder = new NotificationCompat.Builder(ImageReplayinActivity.this, "id")
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
    public int sizesize = 0;
    public static String size(long size) {
        Log.d("WalletScrollCheck", "Scroll Check 52");
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    private void onclick() {
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        ivprofile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void initialize() {
        chats = new ArrayList<>();
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();




        mRelativeLayout = (RelativeLayout)findViewById(R.id.chat_row_container);

        ivCircularStatusView = (CircularStatusView)findViewById(R.id.circular_status_view);

        ivprofile_image = (CircleImageView)findViewById(R.id.profile_image);



        rvStatus = (RecyclerView) findViewById(R.id.rvStatus);


        if(sharedPreferences.getString("story_check","").equalsIgnoreCase("0")){
            ivCircularStatusView.setPortionsCount(0);

        }else if(sharedPreferences.getString("story_check","").equalsIgnoreCase("1")){
            /// image

            Glide.with(ImageReplayinActivity.this)
                    .load(sharedPreferences.getString("story_url","")+sharedPreferences.getString("story_name",""))
                    .apply(new RequestOptions().placeholder(R.drawable.profile))
                    .into(ivprofile_image);
            statuses = RealmHelper.getInstance().getUserStatuses(sharedPreferences.getString("mobile","00000000")).getFilteredStatuses();

            ivCircularStatusView.setPortionsCount(statuses.size());

        }else if(sharedPreferences.getString("story_check","").equalsIgnoreCase("2")){
            // video

            Glide.with(ImageReplayinActivity.this)
                    .load(sharedPreferences.getString("story_url","")+sharedPreferences.getString("story_name",""))
                    .apply(new RequestOptions().placeholder(R.drawable.profile))
                    .into(ivprofile_image);
            statuses = RealmHelper.getInstance().getUserStatuses(sharedPreferences.getString("mobile","00000000")).getFilteredStatuses();

            ivCircularStatusView.setPortionsCount(statuses.size());


        }

        myDbHandler = new MyDbHandler(getApplicationContext(), "userbd", null, 1);
        Temp.setMyDbHandler(myDbHandler);




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


    public String replay_id = "NA";
    public List<String> filePath = new ArrayList<>();
    public void sendImage() {
        Bundle args = getIntent().getBundleExtra("bundle");
        file_path_path = args.getStringArrayList("returnValue");
        Log.d("WalletScrollCheck", "Scroll Check 51");
        Log.d("WalletScrollCheck", "pathList size   ===   "+file_path_path.size());
        if (filePath.size() > 0) {
            filePath.clear();
        }
        if (lengthsizeArry.size() > 0) {
            lengthsizeArry.clear();
        }
        if (ImageCDRIVES.size() > 0) {
            ImageCDRIVES.clear();
        }
        if (ImageName.size() > 0) {
            ImageName.clear();
        }


        for (String imagePath : file_path_path) {
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            RandomString randomString = new RandomString();
            String image_name = ts + "_" + randomString.nextString();
            Log.d("WalletScrollCheck", "ChatActivity imagePath =   " + imagePath);
            Log.d("WalletScrollCheck", "ChatActivity message id =   " + image_name);
            Message message = new MessageCreator.Builder(user_nwe, MessageType.SENT_IMAGE, image_name).quotedMessage(null).path(imagePath).fromCamera(false).build();
            RealmHelper.getInstance().updateDownloadSend(image_name, DownloadUploadStat.SUCCESS);
//            ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());
//            if (getQuotedMessage() != null) {
//                Message quotedMessage = getQuotedMessage();
//                hideReplyLayout();
//                replay_id = quotedMessage.getMessageId();
//
//                Log.d("SendImageLucky", "quotedMessage  " + quotedMessage.getMessageId());
//
//
////            String id = quotedMessage.getMessageId();
////            Log.d("sendMessagesendMessage", "Replay id =   "+id);
//                Log.d("SendImageLucky", "message id =   " + image_name);
////                addpopup.sendmessage_reply(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name, replay_id);
//            } else {
//                replay_id = "NA";
////                addpopup.sendmessage(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name);
//            }

            String pathpath = imagePath;
            Bitmap bm = BitmapFactory.decodeFile(pathpath);


            byteArrayOutputStream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            CDRIVES = byteArrayOutputStream.toByteArray();
            encoded = Base64.encodeToString(CDRIVES, Base64.DEFAULT);
            long lengthbmp = CDRIVES.length;
            String lengthsize = size(lengthbmp);
            Log.d("SendImageLucky", "Image Path  =   1 " + imagePath);



            filePath.add(imagePath);
            lengthsizeArry.add(lengthsize);
            ImageCDRIVES.add(CDRIVES);
            ImageName.add(image_name);


//            ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
//            addpopup.sendmessage(getIntent().getStringExtra("id"),"data:image/jpeg;base64," +encodedtwo,MessageType.SENT_IMAGE);
        }


        sizesize = file_path_path.size() - 1;
        Log.d("CheckImageSize", "image_name  =   " + sizesize);
//        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//        spacesFileRepository.uploadExampleFile(filePath.get(0), ImageCDRIVES.get(0), ChatActivity.this, getIntent().getStringExtra("id"), ImageName.get(0), lengthsizeArry.get(0), sizesize, 0, ImageName.get(0));

        Log.d("SendImageLuckyreplay_id", "message id =   " + replay_id);

//        Intent intent = new Intent(ImageReplayinActivity.this, BackgroundNotificationImageGalleryService.class);
//
//        intent.putExtra("encodedtwo", ImageCDRIVES.get(0));
//        intent.putExtra("id", getIntent().getStringExtra("id"));
//        intent.putExtra("image_name", ImageName.get(0));
//        intent.putExtra("lengthsize", lengthsizeArry.get(0));
//        intent.putExtra("sizesize", sizesize);
//        intent.putExtra("position", 0);
//        intent.putExtra("replay_id",replay_id);
//        startService(intent);
        send_data_from_server(ImageCDRIVES.get(0), ImageName.get(0), lengthsizeArry.get(0), sizesize, 0 );
        replydataimage();

    }




    public void replydataimage() {




            SharedPreferences sharedPreferences = getSharedPreferences("ChatData",0);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("chat_id",userid);
            edit.apply();
            Intent intent1 = new Intent(new Intent(ImageReplayinActivity.this, ChatActivity.class));
            intent1.putExtra("image",userimage);
            intent1.putExtra("name",username);
            intent1.putExtra("id",userid);
            startActivity(intent1);

            finish();


//        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//        spacesFileRepository.uploadExampleFile(filePath.get(i), ImageCDRIVES.get(i), ChatActivity.this, getIntent().getStringExtra("id"), ImageName.get(i), lengthsizeArry.get(i), sizesize, i, ImageName.get(i));
    }



    String userid,username, userimage;
    public User user_nwe =  new User();

    public Boolean checkImageClick = false;
    @Override
    public void onChange(ForwardChat user, boolean added) {

        SharedPreferences sharedPreferences = getSharedPreferences("ChatData",0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("chat_id",user.getId());
        edit.apply();

        user_nwe.setUid(user.getId());
        user_nwe.setPhoto(user.getImage());
        user_nwe.setPhone(user.getId());

        userid = user.getId();
        userimage = user.getImage();
        username = user.getName();
        Log.d("WalletScrollCheck", "Scroll userid  ==  "+userid);
        Log.d("WalletScrollCheck", "Scroll userimage  ===  "+userimage);
        Log.d("WalletScrollCheck", "Scroll username  ====    "+username);
        checkImageClick = true;
        sendImage();


    }
}