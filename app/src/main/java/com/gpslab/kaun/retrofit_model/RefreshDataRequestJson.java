package com.gpslab.kaun.retrofit_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RefreshDataRequestJson {
    @Expose
    @SerializedName("mobiles")
    public List<String> mobiles;



    @Expose
    @SerializedName("token")
    public String token;





    @Expose
    @SerializedName("id")
    public String id;
}
