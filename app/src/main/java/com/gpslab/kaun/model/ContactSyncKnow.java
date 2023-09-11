package com.gpslab.kaun.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ContactSyncKnow {
    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("mobile")
    public ArrayList<String> mobile;

    @Expose
    @SerializedName("img")
    public String img;

    @Expose
    @SerializedName("email")
    public String email;
}
