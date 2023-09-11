package com.gpslab.kaun.retrofit_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppContactsModel {

    @Expose
    @SerializedName("fname")
    public String fname;


    @Expose
    @SerializedName("lname")
    public String lname;


    @Expose
    @SerializedName("email")
    public String email;



    @Expose
    @SerializedName("mobile")
    public String mobile;




    @Expose
    @SerializedName("profile_image")
    public String profile_image;




    @Expose
    @SerializedName("status")
    public String status;
}
