package com.gpslab.kaun.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gpslab.kaun.model.GetSpamcallData;

import java.util.ArrayList;

public class GetSpamcallResponseJson {
    @Expose
    @SerializedName("MobileNo")
    public String MobileNo;

    @Expose
    @SerializedName("Details")
    public ArrayList<GetSpamcallData> Details;

    @Expose
    @SerializedName("Massage")
    public String Massage;
    @Expose
    @SerializedName("Status")
    public String Status;

}
