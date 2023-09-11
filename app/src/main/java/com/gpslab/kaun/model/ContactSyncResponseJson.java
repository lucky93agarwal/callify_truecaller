package com.gpslab.kaun.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gpslab.kaun.model.ContactSyncResponseData;

public class ContactSyncResponseJson {
    @Expose
    @SerializedName("result")
    public String Status;
}
