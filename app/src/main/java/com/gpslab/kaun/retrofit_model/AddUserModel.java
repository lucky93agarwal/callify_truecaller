package com.gpslab.kaun.retrofit_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddUserModel {
    @Expose
    @SerializedName("result")
    public String result;



    @Expose
    @SerializedName("insertId")
    public String insertId;
}
