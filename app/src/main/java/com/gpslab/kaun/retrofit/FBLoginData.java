package com.gpslab.kaun.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FBLoginData {
    @Expose
    @SerializedName("name")
    public String name;

    @SerializedName("mobile")
    @Expose
    public String mobile;

    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("status")
    @Expose
    public String status;
}
