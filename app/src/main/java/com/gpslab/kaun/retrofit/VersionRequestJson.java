package com.gpslab.kaun.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VersionRequestJson {
    @Expose
    @SerializedName("mobile")
    public ArrayList<String> mobile;
}
