package com.gpslab.kaun.Contact;

import android.widget.ImageView;
import androidx.databinding.BaseObservable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class NewContact extends BaseObservable {

    private String name;
    private String phoneNumber;
    private String photoUri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(view);
    }
}