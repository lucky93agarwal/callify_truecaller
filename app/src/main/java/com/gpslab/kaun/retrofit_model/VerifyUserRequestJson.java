package com.gpslab.kaun.retrofit_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyUserRequestJson {
    @Expose
    @SerializedName("mobile")
    public String mobile;



    @Expose
    @SerializedName("token")
    public String token;
}
