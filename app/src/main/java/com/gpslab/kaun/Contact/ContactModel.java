package com.gpslab.kaun.Contact;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class ContactModel {
    private String name;
    private String mobile;
    private String profile_image;

    private Context context;


    public ContactModel(String name, String mobile, String profile_image, Context context) {
        this.name = name;
        this.mobile = mobile;
        this.profile_image = profile_image;
        this.context = context;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    // image load
    @BindingAdapter("android:loadImage")
    public static void loadImage(ImageView imageView, String profile_image){
        Glide.with(imageView)
                .load(profile_image)
                .into(imageView);
    }

    @Override
    public String toString() {
        return "ContactModel{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", context=" + context +
                '}';
    }
}
