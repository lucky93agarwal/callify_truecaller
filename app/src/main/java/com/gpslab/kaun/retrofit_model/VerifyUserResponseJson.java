package com.gpslab.kaun.retrofit_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VerifyUserResponseJson {
    @Expose
    @SerializedName("response")
    public ArrayList<VerifyUserModel> response;

}
