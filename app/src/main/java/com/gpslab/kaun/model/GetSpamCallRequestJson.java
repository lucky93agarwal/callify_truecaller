package com.gpslab.kaun.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetSpamCallRequestJson {
    @Expose
    @SerializedName("mobile")
    public List<String> mobile;




}
