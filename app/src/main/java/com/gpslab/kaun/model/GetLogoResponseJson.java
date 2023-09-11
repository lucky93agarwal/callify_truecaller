package com.gpslab.kaun.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetLogoResponseJson {

    @Expose
    @SerializedName("data")
    public ArrayList<GetLogoData> data;
}
