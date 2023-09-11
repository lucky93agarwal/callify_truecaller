package com.gpslab.kaun.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VersionResponseJson {
    @Expose
    @SerializedName("response")
    public ArrayList<FBLoginData> data;
}
