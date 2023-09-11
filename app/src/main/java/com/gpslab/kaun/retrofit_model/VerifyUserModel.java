package com.gpslab.kaun.retrofit_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VerifyUserModel {
    @Expose
    @SerializedName("result")
    public String result;


    @Expose
    @SerializedName("data")
    public ArrayList<VerifyUserSecModel> data;
}
