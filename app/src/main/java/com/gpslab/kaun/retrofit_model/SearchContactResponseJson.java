package com.gpslab.kaun.retrofit_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchContactResponseJson {
    @Expose
    @SerializedName("response")
    public ArrayList<ArrayList<SearchContactModel>> response;
    @Expose
    @SerializedName("status")
    public String status;

}
