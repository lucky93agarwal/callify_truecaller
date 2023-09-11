package com.gpslab.kaun.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import com.zhihu.matisse.Matisse;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;
import android.widget.ImageView;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.cjt2325.cameralibrary.util.DeviceUtil;
import com.gpslab.kaun.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.zhanghai.android.systemuihelper.SystemUiHelper;

public class CameraActivity extends AppCompatActivity {
    //max image selectable(when choosing from gallery)
    public static final int MAX_IMAGE_SELECTABLE = 5;
    private static final String EXTENSION_MP4 = ".mp4";
    //max video selectable(when choosing from gallery)
    public static final int MAX_VIDEO_SELECTABLE = 1;
    public static final int REQUEST_CODE_PICK_FROM_GALLERY = 2323;
    private JCameraView jCameraView;
    private Chronometer chronometer;
    private static final String APP_FOLDER_NAME = "LuckyStorage";
    SystemUiHelper uiHelper;
    //if the user opens the camera for adding new status we will save the image in the received images folder
    private boolean isStatus = false;
    public ImageView ivimageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ivimageView = (ImageView)findViewById(R.id.imageView);


        ivimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImages();
            }
        });

        chronometer = findViewById(R.id.chronometer);
        uiHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, SystemUiHelper.FLAG_IMMERSIVE_STICKY);


        //if the user opens the camera for adding new status we will save the image in the received images folder
        isStatus = getIntent().hasExtra(IntentUtils.IS_STATUS);


        jCameraView = findViewById(R.id.jcameraview);
        //if it's status we will save the video in received video folder,otherwise we will save it in sent video folder

        File file = new File(sentVideoDir() + "/" + generateNewName(5) + EXTENSION_MP4);

        jCameraView.setSaveVideoPath(file.getPath());
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        jCameraView.setTip(getString(R.string.camera_tip));
        ivimageView.setVisibility(View.GONE);
        //show pickImage from gallery button if needed
        if (getIntent().hasExtra(IntentUtils.CAMERA_VIEW_SHOW_PICK_IMAGE_BUTTON))
            ivimageView.setVisibility(View.VISIBLE);
//            jCameraView.showPickImageButton();

            //set media quality
            jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                Log.i("CJT", "camera error");
                Intent intent = new Intent();
                setResult(ResultCodes.CAMERA_ERROR_STATE, intent);
                finish();
            }

            @Override
            public void AudioPermissionError() {
                Toast.makeText(CameraActivity.this, R.string.audio_permission_error, Toast.LENGTH_SHORT).show();
            }
        });

        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {

                //if the user opens the camera for adding new status we will save the image in the received images folder
                File outputFile = DirManager.generateFile(isStatus ? MessageType.RECEIVED_IMAGE : MessageType.SENT_IMAGE);

                BitmapUtils.convertBitmapToJpeg(bitmap, outputFile);

                String path = outputFile.getPath();
                Intent intent = new Intent();
                intent.putExtra(IntentUtils.EXTRA_PATH_RESULT, path);
                setResult(ResultCodes.IMAGE_CAPTURE_SUCCESS, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {

                Intent intent = new Intent();
                intent.putExtra(IntentUtils.EXTRA_PATH_RESULT, url);
                setResult(ResultCodes.VIDEO_RECORD_SUCCESS, intent);
                finish();
            }
//
//            @Override
//            public void quit() {
//
//            }
        });

//        jCameraView.setRecordStartListener(new RecordStartListener() {
//            @Override
//            public void onStart() {
//                chronometer.setBase(SystemClock.currentThreadTimeMillis());
//                chronometer.start();
//            }
//
//            @Override
//            public void onStop() {
//                chronometer.stop();
//            }
//        });

        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });

//        jCameraView.setPickImageListener(new ClickListener() {
//            @Override
//            public void onClick() {
//                pickImages();
//            }
//        });

        Log.i("CJT", DeviceUtil.getDeviceModel());
    }



    public static String generateNewName(int type) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddSSSS", Locale.US); //the Locale us is to use english numbers
        return getFileTypeString(type) + "-" + sdf.format(date);
    }
    private static String getFileTypeString(int type) {
        switch (type) {
            case MessageType.SENT_IMAGE:
            case MessageType.RECEIVED_IMAGE:
                return "IMG";


            case MessageType.SENT_VIDEO:
            case MessageType.RECEIVED_VIDEO:
                return "VID";


            case MessageType.SENT_AUDIO:
            case MessageType.RECEIVED_AUDIO:
                return "AUD";


            case MessageType.SENT_VOICE_MESSAGE:
            case MessageType.RECEIVED_VOICE_MESSAGE:
                //push to talk (voice message)
                return "PTT";


            default:
                return "FILE";


        }

    }
    public static String sentVideoDir() {
        File file = new File(mainAppFolder() + "/" + APP_FOLDER_NAME + " " + "Video/Sent");
        if (!file.exists()) {
            file.mkdirs();
        }
        createNoMediaFile(file);

        return file.getPath();
    }
    //Main App Folder: /sdcard/FireApp/
    public static String mainAppFolder() {
        File file;
        if (Build.VERSION.SDK_INT >= 30) {
            file = new File(MyApp.context().getExternalFilesDir(null) + "/" + APP_FOLDER_NAME + "/");
        } else {
            file = new File(Environment.getExternalStorageDirectory() + "/" + APP_FOLDER_NAME + "/");
        }
        //if the directory is not exists create it
        if (!file.exists())
            file.mkdir();


        return file.getAbsolutePath();
    }
    //this will prevent the gallery or audio player app in the device from showing "sent" files (images,videos,audio,voice messages) by our app
    //basically just hide them
    public static void createNoMediaFile(File folderPath) {
        File file = new File(folderPath + "/" + ".nomedia");
        try {
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pickImages() {
        Matisse.from(CameraActivity.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.BMP, MimeType.PNG))
                .countable(true)
                .maxSelectablePerMediaType(MAX_IMAGE_SELECTABLE, MAX_VIDEO_SELECTABLE)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_PICK_FROM_GALLERY);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //hiding system bars
        uiHelper.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            setResult(ResultCodes.PICK_IMAGE_FROM_CAMERA, data);
            finish();
        }
    }
}