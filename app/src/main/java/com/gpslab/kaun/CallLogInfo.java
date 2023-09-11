package com.gpslab.kaun;

import androidx.databinding.BaseObservable;

public class CallLogInfo extends BaseObservable {

    private String name;
    private String number;
    private String callType;
    private String image;

    private String simtype;
    private long date;
    private long duration;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSimtype() {
        return simtype;
    }

    public void setSimtype(String simtype) {
        this.simtype = simtype;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





}
