package com.gpslab.kaun.view;

import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.util.Log;

import com.gpslab.kaun.digitaloceanspaces.RandomString;

import java.util.Date;
import java.util.HashMap;

public class StatusCreator {
    public static Status createImageStatus(String imagePath,String userid) {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts + "_" + randomString.nextString();


        String statusId = image_name;
        String thumbImg = BitmapUtils.decodeImage(imagePath, false);
        Status status = new Status(statusId, userid, new Date().getTime(), thumbImg, null, imagePath, StatusType.IMAGE);
        RealmHelper.getInstance().saveObjectToRealm(status);
        return status;
    }
    public static Status createImageStatusOther(String imagePath,String userid) {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts + "_" + randomString.nextString();


        String statusId = image_name;
//        String thumbImg = BitmapUtils.decodeImage(imagePath, true);
        Status status = new Status(statusId, userid, new Date().getTime(), imagePath, null, imagePath, StatusType.IMAGE);
        RealmHelper.getInstance().saveObjectToRealm(status);
        return status;
    }



    public static Status createVideoStatus(String videoPath, String userid) {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts + "_" + randomString.nextString();



        String statusId = image_name;
        String thumbImg = BitmapUtils.generateVideoThumbAsBase64(videoPath);
        long mediaLengthInMillis = Util.getMediaLengthInMillis(MyApp.context(), videoPath);
        Status status = new Status(statusId, userid, new Date().getTime(), thumbImg, null, videoPath, StatusType.VIDEO, mediaLengthInMillis);
        RealmHelper.getInstance().saveObjectToRealm(status);
        return status;
    }



    public static Status createVideoStatusOther(String videoPath, String userid, Long mediaLengthInMillis) {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts + "_" + randomString.nextString();



        String statusId = image_name;


//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        if (Build.VERSION.SDK_INT >= 14)
//            retriever.setDataSource(videoPath, new HashMap<String, String>());
//        else
//            retriever.setDataSource(videoPath);
//
//
//        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        long timeInMillisec = Long.parseLong(time);
//
//        long mediaLengthInMillis = Util.getMediaLengthInMillis(MyApp.context(), videoPath);
        Log.d("onBackPressedCheck","media lenthIn   ==   "+mediaLengthInMillis);
//        Log.d("onBackPressedCheck","retriever lenthIn   ==   "+timeInMillisec);
        Status status = new Status(statusId, userid, new Date().getTime(), videoPath, null, videoPath, StatusType.VIDEO, mediaLengthInMillis);
        RealmHelper.getInstance().saveObjectToRealm(status);
        return status;
    }
}
