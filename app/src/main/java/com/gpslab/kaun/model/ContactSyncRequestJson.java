package com.gpslab.kaun.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gpslab.kaun.model.ContactSyncKnow;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContactSyncRequestJson {
    @Expose
    @SerializedName("parent_no")
    public String parent_no;



    @Expose
    @SerializedName("data")
    public ArrayList<ContactSyncKnow> data;
}
