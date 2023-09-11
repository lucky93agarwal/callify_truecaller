package com.gpslab.kaun.mssagedetail;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.gpslab.kaun.view.Message;

public class MessageDataModel {
    private String Date;
    private String Time;
    private String Address;
    private String Body;
    private String imageUrl;
    private Context context;


    public MessageDataModel(String date, String time, String address, String body, String imageUrl, Context context) {
        Date = date;
        Time = time;
        Address = address;
        Body = body;
        this.imageUrl = imageUrl;
        this.context = context;
    }


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /// button click
    public void handleclick(){
        MessageDetailsActivity activity  = (MessageDetailsActivity)context;
        activity.onBackPressed();
    }

    /// load image
    @BindingAdapter("android:whatevr")
    public static void loadImage(ImageView imageView, String imageurl){
        Glide.with(imageView)
                .load(imageurl)
                .into(imageView);
    }


    @Override
    public String toString() {
        return "MessageDataModel{" +
                "Date='" + Date + '\'' +
                ", Time='" + Time + '\'' +
                ", Address='" + Address + '\'' +
                ", Body='" + Body + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", context=" + context +
                '}';
    }
}
