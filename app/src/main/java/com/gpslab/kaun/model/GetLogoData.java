package com.gpslab.kaun.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLogoData {
    @Expose
    @SerializedName("id")
    public String id;



    @Expose
    @SerializedName("logo")
    public String logo;


    @Expose
    @SerializedName("tags")
    public String tags;
}
