package com.gpslab.kaun.fragment;

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
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abedelazizshe.lightcompressorlibrary.CompressionListener;
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor;
import com.abedelazizshe.lightcompressorlibrary.VideoQuality;
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
import com.fxn.pix.Pix;
import com.google.gson.JsonArray;
import com.gpslab.kaun.ImageEditor;
import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.Service.BackgroundNotificationImageSToryService;
import com.gpslab.kaun.adapter.StatusAdapter;
import com.gpslab.kaun.adapter.StatusTwoAdapter;
import com.gpslab.kaun.calling.CameraNewActivity;
import com.gpslab.kaun.digitaloceanspaces.RandomString;
import com.gpslab.kaun.download_notification.BackgroundNotificationImageGalleryService;
import com.gpslab.kaun.download_notification.BackgroundNotificationVideoSendService;
import com.gpslab.kaun.model.Constants;
import com.gpslab.kaun.model.StatusStatus;

import com.gpslab.kaun.popup.checkpermissionpopup;
import com.gpslab.kaun.private_and_public.private_and_public;
import com.gpslab.kaun.status.ViewStatusActivity;
import com.gpslab.kaun.view.BitmapUtils;
import com.gpslab.kaun.view.CameraActivity;

import com.gpslab.kaun.view.DeleteStatusJob;
import com.gpslab.kaun.view.ExifUtil;
import com.gpslab.kaun.view.FileUtils;
import com.gpslab.kaun.view.IntentUtils;
import com.gpslab.kaun.view.Message;
import com.gpslab.kaun.view.MessageCreator;
import com.gpslab.kaun.view.MessageType;
import com.gpslab.kaun.view.MyApp;
import com.gpslab.kaun.view.RealmHelper;
import com.gpslab.kaun.view.ResultCodes;
import com.gpslab.kaun.view.Status;
import com.gpslab.kaun.view.StatusCreator;
import com.gpslab.kaun.view.Util;
import com.zhihu.matisse.Matisse;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;
import io.socket.client.IO;
import io.socket.client.Socket;

import static android.app.Activity.RESULT_OK;

public class StatusFragment  extends Fragment {
    private RecyclerView rvStatus,rvLatestUpdate;
    private StatusAdapter adapter;
    public static final int PICK_GALLERY_REQUEST = 4815;
    public static final int REQUEST_CODE_TEXT_STATUS = 9145;
    public ByteArrayOutputStream byteArrayOutputStream;
    private StatusTwoAdapter adaptertwo;
    public String encoded = "null";
    private static private_and_public profilepopupnew;

    public CircularStatusView ivCircularStatusView;


    public CircleImageView ivprofile_image;

    public int counter = 0;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public int Image_position = 0;
    public int Image_check = 0;

    byte[] CDRIVES;
    private ArrayList<StatusStatus> statusList;
    public static final String[] profileUrls = {"https://blog.rackspace.com/wp-content/uploads/2018/09/pumping-iron-arnold-schwarzenegger-1-1108x0-c-default-696x522.jpg",
            "https://www.rollingstone.com/wp-content/uploads/2018/06/rs-213329-R1247_FEA_Rogen_A.jpg?crop=900:600&width=440",
            "https://assets.entrepreneur.com/content/3x2/2000/20170118220227-GettyImages-471763092.jpeg",
            "https://static.ffx.io/images/$zoom_0.238%2C$multiply_1%2C$ratio_1.776846%2C$width_1059%2C$x_0%2C$y_55/t_crop_custom/w_800/q_86%2Cf_auto/cf3f16e35b79207935da03f7b4a7e7d6e484ff71"
    };
    public static final int CAMERA_REQUEST = 4659;
    public StatusFragment() {
        // Required empty public constructor
    }


    public CircleImageView circleplusbtnbtn;
    public RelativeLayout mRelativeLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_status, container, false);




        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
        onclick();
        populateStatus();
        setAdapter();




        profilepopupnew = new private_and_public(getActivity());

    }


    private void onclick(){
        circleplusbtnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), CameraActivity.class);
//                intent.putExtra(IntentUtils.CAMERA_VIEW_SHOW_PICK_IMAGE_BUTTON, true);
//                intent.putExtra(IntentUtils.IS_STATUS, true);
//                startActivityForResult(intent, CAMERA_REQUEST);



                Intent intent = new Intent(getActivity(), CameraNewActivity.class);
                intent.putExtra("openPage","statusfragment");
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

                if(RealmHelper.getInstance().getUserStatuses(sharedPreferences.getString("mobile","00000000")) != null){
                    Intent intent = new Intent(getActivity(), ViewStatusActivity.class);

                    intent.putExtra(IntentUtils.UID, sharedPreferences.getString("mobile",""));
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), CameraNewActivity.class);
                    intent.putExtra("openPage","statusfragment");
                    startActivityForResult(intent, CAMERA_REQUEST);
                }

//                if(Check_which_page_open){
//                    Intent intent = new Intent(getActivity(), ViewStatusActivity.class);
//
//                    intent.putExtra(IntentUtils.UID, sharedPreferences.getString("mobile",""));
//                    startActivity(intent);
//                }else {
////                    Intent intent = new Intent(getActivity(), CameraActivity.class);
////                    intent.putExtra(IntentUtils.CAMERA_VIEW_SHOW_PICK_IMAGE_BUTTON, true);
////                    intent.putExtra(IntentUtils.IS_STATUS, true);
////                    startActivityForResult(intent, CAMERA_REQUEST);
//
//                    Intent intent = new Intent(getActivity(), CameraNewActivity.class);
//                    intent.putExtra("openPage","statusfragment");
//                    startActivityForResult(intent, CAMERA_REQUEST);
//                }
            }
        });
    }
    public ArrayList<String> video_path = new ArrayList<>();
    private void Recursion(List<String> pathList, int number) {
        Log.d("WalletScrollCheck", "Scroll Check 40");
        Log.d("WalletCheckPathSize", "Number 1 ===   " + number);
        Log.d("WalletCheckPathSize", "Size 1 ===   " + pathList.size());


        Log.d("WalletCheckPathSize", "size 786 ===   " + video_path.size());
        if (pathList.size() > number) {


            File file = new File(pathList.get(number));
            long length = file.length();
            String lengthsize = size(length);
            Log.d("WalletCheckPathSize", "Size " + number + " ===   " + lengthsize);

            String[] separated = lengthsize.split("\\ ");
            String first = separated[0]; // this will contain "Fruit: they taste good"
            String Second = separated[1];
            double d = Double.valueOf(first);

            if (Second.equalsIgnoreCase("MB")) {
                if (d > 50.00) {


                    Long tsLong_new = System.currentTimeMillis() / 1000;
                    String ts_new = tsLong_new.toString();
                    RandomString randomString_new = new RandomString();
                    String video_name_new = ts_new + "_" + randomString_new.nextString();

                    File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
                    File output_file = new File(folder, video_name_new + ".mp4");
                    String paths = output_file.toString();
                    Log.d("WalletLuckySamsung", "paths = " + paths);


//                    VideoCompressor.start(pathList.get(number), paths, new CompressionListener() {
//                        @Override
//                        public void onStart() {
//                            // Compression start
//                            Log.d("WalletLuckySamsung", "onStart");
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            Log.d("WalletLuckySamsung", "onSuccess");
//                            // On Compression success
//
//                            File file_new = new File(paths);
//
//
//                            long length_new = file_new.length();
//
//
//                            String lengthsize_new = size(length_new);
//
//                            Log.d("WalletLuckySamsung", "onSuccess = " + lengthsize_new);
//
//
//                            video_path.add(paths);
//                            Recursion(pathList, number + 1);
//
//
//                        }
//
//                        @Override
//                        public void onFailure(String failureMessage) {
//                            // On Failure
//                            Log.d("WalletLuckySamsung", "onFailure");
//                        }
//
//                        @Override
//                        public void onProgress(float progressPercent) {
//                            // Update UI with progress value
//                            getActivity().runOnUiThread(new Runnable() {
//                                public void run() {
//
////                        progress.setText(progressPercent + "%");
////                        progressBar.setProgress((int) progressPercent);
//                                    Log.d("WalletLuckySamsung", "progressPercent = " + progressPercent);
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onCancelled() {
//                            // On Cancelled
//                        }
//                    }, VideoQuality.MEDIUM, false, false);


//                    Toast.makeText(this, "Grater then 50 Path ==   " + pathList.get(number), Toast.LENGTH_SHORT).show();
                    Log.d("WalletCheckPathSize", "path 1 ===   " + pathList.get(number));

                } else {
                    Log.d("WalletCheckPathSize", "path 2 ===   " + pathList.get(number));
//                    Toast.makeText(this, "Less then 50 ==   " + pathList.get(number), Toast.LENGTH_SHORT).show();
                    video_path.add(pathList.get(number));
                    Recursion(pathList, number + 1);

                }
            } else if (Second.equalsIgnoreCase("KB")) {
                video_path.add(pathList.get(number));
                Log.d("WalletCheckPathSize", "path 2 ===   " + pathList.get(number));
                Recursion(pathList, number + 1);
            }


        } else {
            sendTheVideo(video_path);
        }
    }
    public int sizesize_video = 0;
    public ArrayList<String> file_path_path = new ArrayList<>();
    public ArrayList<String> video_name_array = new ArrayList<>();
    public ArrayList<File> filePath_array = new ArrayList<>();

    public ArrayList<String> video_size_array = new ArrayList<>();

    public ArrayList<String> video_extension_array = new ArrayList<>();

    public ArrayList<File> video_thum_path_array = new ArrayList<>();


    public Bitmap StringToBitMap(String encodedString) {
        Log.d("WalletScrollCheck", "Scroll Check 47");
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    private File getOutputMediaFile(String video_name) {
        Log.d("WalletScrollCheck", "Scroll Check 49");
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getActivity().getApplicationContext().getPackageName()
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
    private File storeImage(Bitmap image, String video_name) {
        Log.d("WalletScrollCheck", "Scroll Check 48");
        File pictureFile = getOutputMediaFile(video_name);
        if (pictureFile == null) {
            Log.d("TAG",
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


    public ArrayList<String> durationinmili = new ArrayList<>();
    private void sendTheVideo(List<String> pathList) {
        profilepopupnew.profileaddpopup(pathList);
        Log.d("WalletScrollCheck", "Scroll Check 54");
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        edit.putString("story_count",String.valueOf(pathList.size()));
        edit.apply();
        ivCircularStatusView.setPortionsCount(pathList.size());
        sizesize_video = pathList.size() - 1;



        if(durationinmili.size()>0){
            durationinmili.clear();
        }

        if (file_path_path.size() > 0) {
            file_path_path.clear();
        }

        if (video_name_array.size() > 0) {
            video_extension_array.clear();
        }
        if (filePath_array.size() > 0) {
            filePath_array.clear();
        }

        if (video_size_array.size() > 0) {
            video_size_array.clear();
        }

        if (video_extension_array.size() > 0) {
            video_extension_array.clear();
        }
        if (video_thum_path_array.size() > 0) {
            video_thum_path_array.clear();
        }
        for (String path : pathList) {


            Status status = new Status();
            status = StatusCreator.createVideoStatus(path,sharedPreferences.getString("mobile", "0000"));
            status.setContent("");
            RealmHelper.getInstance().saveStatus(sharedPreferences.getString("mobile", "0000"), status);
            DeleteStatusJob.schedule(status.getUserId(), status.getStatusId());



            long mediaLengthInMillis = Util.getMediaLengthInMillis(MyApp.context(), path);
            durationinmili.add(String.valueOf(mediaLengthInMillis));
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            RandomString randomString = new RandomString();
            String video_name = ts + "_" + randomString.nextString();

            video_name_array.add(video_name);







            Bitmap videoThumbBitmap = BitmapUtils.getThumbnailFromVideo(path);
            //generate blurred thumb to send it to other user
            String thumb = BitmapUtils.decodeImage(videoThumbBitmap);
            //generate normal video thumb without blur to show it in recyclerView
            String videoThumb = BitmapUtils.generateVideoThumbAsBase64(videoThumbBitmap);
            File file = new File(path);
            file_path_path.add(path);
            filePath_array.add(file);

            long length = file.length();


            File Imagefile = storeImage(StringToBitMap(thumb), video_name);
            video_thum_path_array.add(Imagefile);
            String lengthsize = size(length);

            video_size_array.add(lengthsize);
            String extension = path.substring(path.lastIndexOf("."));
            video_extension_array.add(extension);


            Log.d("WalletLuckySamsung", "extension =   " + extension);
            Log.d("WalletLuckySamsung", "lengthsize =   " + lengthsize);
            Log.d("WalletLuckySamsung", "size =     " + lengthsize);
            Log.d("WalletLuckySamsung", "Imagefile =   " + Imagefile);



//            spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//            spacesFileRepository.uploadExamplevideoFile(file, ChatActivity.this, getIntent().getStringExtra("id"), video_name, lengthsize,extension);
        }

        statuses = RealmHelper.getInstance().getUserStatuses(sharedPreferences.getString("mobile","00000000")).getFilteredStatuses();

        ivCircularStatusView.setPortionsCount(statuses.size());

//        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//        spacesFileRepository.uploadExamplevideothumFile(video_thum_path_array.get(0), video_name_array.get(0), "jpg");
//        spacesFileRepository.uploadExamplevideoFile(filePath_array.get(0), ChatActivity.this, getIntent().getStringExtra("id"), video_name_array.get(0), video_size_array.get(0), video_extension_array.get(0), sizesize_video, 0);



        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                check_type = sharedPreferences.getString("checktypepublic","");
                if(check_type.equalsIgnoreCase("15")){
                    edit.putString("checktypepublic","20");
                    edit.apply();
                    Log.d("CheckPublic","SendData ");
                    send_data_from_server_video(file_path_path, video_name_array, video_size_array, video_extension_array, sizesize_video, 0,durationinmili);
                }

            }

            public void onFinish() {

            }

        }.start();






    }



    public JSONArray jsonArrayvideo = new JSONArray();


    private void send_data_from_server_video(ArrayList<String> file_path_path, ArrayList<String> video_name_array, ArrayList<String> video_size_array, ArrayList<String> video_extension_array, int sizesize_video, int i, ArrayList<String> mediaduration) {


        credentials = new BasicAWSCredentials(accesskey, secretkey);

        client = new AmazonS3Client(credentials, Region.getRegion("us-east-1"));
        client.setEndpoint(spaceregion);
        transferUtility = new TransferUtility(client, getActivity());
        CannedAccessControlList filePermission = CannedAccessControlList.PublicRead;





        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("id", "an", NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("no sound");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        notificationBuilder = new NotificationCompat.Builder(getActivity(), "id")
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Upload")
                .setContentText("Uploading Image")
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());



        File file = new File(video_path.get(i));
        observer = transferUtility.upload(
                spacename + "/callify/upload/story", //empty bucket name, included in endpoint
                video_name_array.get(i) + video_extension_array.get(i),
                file, //a File object that you want to upload
                filePermission
        );

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state.COMPLETED.equals(observer.getState())) {
                    onDownloadComplete(true);

                    Log.d("WalletCheckUpdate", "COMPLETED id  2 =     " );






                    sendmessageVideo(video_name_array.get(i),sizesize_video,i,video_name_array.get(i) + video_extension_array.get(i)+","+mediaduration.get(i));


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
    private void sendmessageVideo(String video_name,int sizesize, int position, String video_path) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("NA");

        jsonArrayvideo.put(video_path);
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        edit.putString("story_check","2");
        edit.putString("story_name",video_path);
        edit.apply();
        Log.d("WalletCheckUpdate", "image_name position  =   " + position);
        Log.d("WalletCheckUpdate", "image_name sizesize  =   " + sizesize);

        if (position < sizesize) {
            Log.d("WalletCheckUpdate", "image_name position  =   " + position);
            position = position + 1;
            Log.d("WalletCheckUpdate", "image_name position  =   " + position);


            send_data_from_server_video(file_path_path, video_name_array, video_size_array, video_extension_array, sizesize_video, position,durationinmili);


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
                    jsonObject.put("video", jsonArrayvideo);
                    jsonObject.put("is_public", sharedPreferences.getString("public_value","1"));
                    jsonObject.put("is_private", sharedPreferences.getString("private_value","0"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("CheckPublic", "EMIT  =   0 " + jsonObject);
                mSocket.emit("send_story", jsonObject);
                Log.d("WalletCheckUpdate", "EMIT  =   1 " + jsonObject);


//                for(int i=0;i<jsonArray.length();i++){
//                    jsonArray.remove(i);
//                }
//                for(int i=0;i<jsonArrayvideo.length();i++){
//                    jsonArrayvideo.remove(i);
//                }


            }
        }
    }
    public int count = 0;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("WalletCheckUpdate","requestCode  =   "+requestCode);
//        Log.d("WalletCheckUpdate","resultCode  =   "+resultCode);
//        Log.d("WalletCheckUpdate","data  =   "+data);
        if (requestCode == CAMERA_REQUEST || requestCode == ImageEditor.RC_IMAGE_EDITOR || requestCode == REQUEST_CODE_TEXT_STATUS) {
//            Log.d("WalletCheckUpdate","requestCode  =   "+requestCode);
            if (resultCode == ResultCodes.PICK_IMAGE_FROM_CAMERA) {
                List<String> mPaths = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                for (String mPath : mPaths) {
                    if (!FileUtils.isFileExists(mPath)) {
                        Toast.makeText(getActivity(), R.string.image_video_not_found, Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if (FileUtils.isPickedVideo(mPaths.get(0))) {
                    File new_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
                    if (!new_folder.exists()) {
                        if (new_folder.mkdir()) {
                            Log.d("WalletCheckPathSize", "Folder succesfully created");
                        } else {
                            Log.d("WalletCheckPathSize", "Failed to create folder");
                        }
                    } else {
                        Log.d("WalletCheckPathSize", "Folder already exists" + new_folder);
                    }//Getting a file within the dir.
//                FileOutputStream out = new FileOutputStream(fileWithinMyDir);

                    Recursion(mPaths, count);
                } else {
//                    Log.d("MainActivityLL", "Image Path ==  ==   ");
                    Log.d("WalletCheckUpdate","Image Path =   "+mPaths);

                    sendImage(mPaths);
                }
//                Log.d("WalletCheckUpdate","PICK_IMAGE_FROM_CAMERA  =   "+requestCode);
            }
        }else {
            Log.d("WalletCheckUpdate","else  =   ");
        }



    }


    public List<String> ImageName = new ArrayList();
    public List<byte[]> ImageCDRIVES = new ArrayList();
    public List<String> lengthsizeArry = new ArrayList<>();


    public int sizesize = 0;


    public void sendImage(List<String> pathList) {
//        Log.d("WalletCheckUpdate", "Scroll Check 51");



        profilepopupnew.profileaddpopup(pathList);
        if (lengthsizeArry.size() > 0) {
            lengthsizeArry.clear();
        }
        if (ImageCDRIVES.size() > 0) {
            ImageCDRIVES.clear();
        }
        if (ImageName.size() > 0) {
            ImageName.clear();
        }
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        edit.putString("story_count",String.valueOf(pathList.size()));
        edit.apply();
        ivCircularStatusView.setPortionsCount(pathList.size());

        for (String imagePath : pathList) {
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
        sizesize = pathList.size() - 1;

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                check_type = sharedPreferences.getString("checktypepublic","");
                if(check_type.equalsIgnoreCase("15")){
                    edit.putString("checktypepublic","20");
                    edit.apply();
                    Log.d("CheckPublic","SendData ");
                    send_data_from_server(ImageCDRIVES.get(0), ImageName.get(0), lengthsizeArry.get(0), sizesize, 0 );
                }

            }

            public void onFinish() {

            }

        }.start();



    }

    public String check_type = "0";

    String accesskey = "4SYUKBCFA4KASIHESCTP";
    String secretkey = "cWCsWzfqX4h1Y6ULx+8OE0hrPjf5CCiDPFOSIdkxOSA";
    String spacename = "gpslabindia";

    String spaceregion = "https://nyc3.digitaloceanspaces.com";

    public AmazonS3Client client;
    public BasicAWSCredentials credentials;
    public TransferObserver observer;
    public TransferUtility transferUtility;


    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;



    public void send_data_from_server(byte[] ImageCDRIVES, String ImageName, String lengthsizeArry, int sizesize, int position ){
        credentials = new BasicAWSCredentials(accesskey, secretkey);

        client = new AmazonS3Client(credentials, Region.getRegion("us-east-1"));
        client.setEndpoint(spaceregion);
        transferUtility = new TransferUtility(client, getActivity());
        CannedAccessControlList filePermission = CannedAccessControlList.PublicRead;





        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("id", "an", NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("no sound");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        notificationBuilder = new NotificationCompat.Builder(getActivity(), "id")
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

//        Intent intent = new Intent(MainActivity.PROGRESS_UPDATE);
//        intent.putExtra("downloadComplete", downloadComplete);
//        LocalBroadcastManager.getInstance(BackgroundNotificationImageSToryService.this).sendBroadcast(intent);
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
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
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



            }
        }
    }

    public void replydataimage(int i) {
        Log.d("WalletCheckUpdate", "Scroll Check 52");
        Log.d("WalletCheckUpdate", "replydataimage position  =   " + i);








//        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//        spacesFileRepository.uploadExampleFile(filePath.get(i), ImageCDRIVES.get(i), ChatActivity.this, getIntent().getStringExtra("id"), ImageName.get(i), lengthsizeArry.get(i), sizesize, i, ImageName.get(i));
    }
    public static String size(long size) {
        Log.d("WalletScrollCheck", "Scroll Check 52");
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }



    RealmResults<Status> statuses;
    public Boolean Check_which_page_open = false;
    private void initialize(View view) {

        circleplusbtnbtn = (CircleImageView)view.findViewById(R.id.plusbtnbtn);

        ivCircularStatusView = (CircularStatusView)view.findViewById(R.id.circular_status_view);

        ivprofile_image = (CircleImageView)view.findViewById(R.id.profile_image);
        mRelativeLayout = (RelativeLayout)view.findViewById(R.id.chat_row_container);
        statusList = new ArrayList<>();
        rvLatestUpdate = view.findViewById(R.id.rvlatestStatus);
        rvStatus = view.findViewById(R.id.rvStatus);

        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();


        Log.d("CheckStatus","story_check  =  "+sharedPreferences.getString("story_check",""));
        Log.d("CheckStatus","story_url  =  "+sharedPreferences.getString("story_url",""));
        Log.d("CheckStatus","story_name  =  "+sharedPreferences.getString("story_name",""));
        Log.d("CheckStatus","story_count  =  "+sharedPreferences.getString("story_count",""));


        if(sharedPreferences.getString("story_check","").equalsIgnoreCase("0")){
            ivCircularStatusView.setPortionsCount(0);
            Check_which_page_open = false;
        }else if(sharedPreferences.getString("story_check","").equalsIgnoreCase("1")){
            /// image
            Check_which_page_open = true;
            Glide.with(getActivity())
                    .load(sharedPreferences.getString("story_url","")+sharedPreferences.getString("story_name",""))
                    .apply(new RequestOptions().placeholder(R.drawable.profile))
                    .into(ivprofile_image);

            if(RealmHelper.getInstance().getUserStatuses(sharedPreferences.getString("mobile","00000000")) != null){
                statuses = RealmHelper.getInstance().getUserStatuses(sharedPreferences.getString("mobile","00000000")).getFilteredStatuses();
                ivCircularStatusView.setPortionsCount(statuses.size());
            }else {
                ivCircularStatusView.setPortionsCount(0);
            }





        }else if(sharedPreferences.getString("story_check","").equalsIgnoreCase("2")){
            // video
            Check_which_page_open = true;
            Glide.with(getActivity())
                    .load(sharedPreferences.getString("story_url","")+sharedPreferences.getString("story_name",""))
                    .apply(new RequestOptions().placeholder(R.drawable.profile))
                    .into(ivprofile_image);

            if(RealmHelper.getInstance().getUserStatuses(sharedPreferences.getString("mobile","00000000")) != null){
                statuses = RealmHelper.getInstance().getUserStatuses(sharedPreferences.getString("mobile","00000000")).getFilteredStatuses();
                ivCircularStatusView.setPortionsCount(statuses.size());
            }else {
                ivCircularStatusView.setPortionsCount(0);
            }


        }



    }
    private ArrayList<String> story_image = new ArrayList<>();

    private ArrayList<String> story_video = new ArrayList<>();

    private void populateStatus() {
        if(story_image.size() >0){
            story_image.clear();
        }
        story_image.add("https://wallpaperaccess.com/full/2667054.jpg");
        story_image.add("https://upload.wikimedia.org/wikipedia/commons/c/c3/Vertical_jump_measurement_test.png");
        story_image.add("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg");
        story_image.add("https://helpx.adobe.com/content/dam/help/en/stock/how-to/visual-reverse-image-search/jcr_content/main-pars/image/visual-reverse-image-search-v2_intro.jpg");
        story_video.add("https://gpslabindia.nyc3.digitaloceanspaces.com/callify/upload/story/1615268666_JiseIiUzhAFG3o3ix3iFw.mp4");

        statusList.add(new StatusStatus(profileUrls[3], "Conor McGregor", "5 minutes ago",story_image,story_video,"0","90010500009",Long.parseLong("2333")));
        Log.d("story_image","story_image   =  1  "+story_image.toString());



        statusList.add(new StatusStatus(profileUrls[1], "Joe Rogan", "30 minutes ago",story_image,story_video,"0","919554453444",Long.parseLong("2333")));
        Log.d("story_image","story_image   =  2  "+story_image.toString());



        statusList.add(new StatusStatus(profileUrls[2], "Elon Musk", "Today, 10:15 PM",story_image,story_video,"0","9195640458",Long.parseLong("2333")));
        Log.d("story_image","story_image   =  3  "+story_image.toString());



        statusList.add(new StatusStatus(profileUrls[0], "Arnold", "Today, 6:15 AM",story_image,story_video,"0","9187474754045",Long.parseLong("2333")));
        Log.d("story_image","story_image   =   4 "+story_image.toString());
    }
    private void setAdapter() {

        rvStatus.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StatusAdapter(getContext(), statusList);

        rvStatus.setAdapter(adapter);




        rvLatestUpdate.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        adaptertwo = new StatusTwoAdapter(getContext(), statusList);
        rvLatestUpdate.setAdapter(adaptertwo);

    }
}