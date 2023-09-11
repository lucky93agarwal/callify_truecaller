package com.gpslab.kaun.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gpslab.kaun.retrofit_model.SearchContactResponseJson;

import java.util.ArrayList;

public class SearchContactMainResponseJson {
    @Expose
    @SerializedName("response")
    public SearchContactResponseJson response;
}
