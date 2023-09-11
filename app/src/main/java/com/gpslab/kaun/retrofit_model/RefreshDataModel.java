package com.gpslab.kaun.retrofit_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefreshDataModel {

    @Expose
    @SerializedName("name")
    public String name;



    @Expose
    @SerializedName("email")
    public String email;


    @Expose
    @SerializedName("mobile")
    public String mobile;



    @Expose
    @SerializedName("image")
    public String image;


    @Expose
    @SerializedName("Status")
    public Integer Status;
}
