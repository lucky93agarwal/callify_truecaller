package com.gpslab.kaun.retrofit_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddUserResponseJson {



    @Expose
    @SerializedName("data")
    public AddUserModel response;
}
