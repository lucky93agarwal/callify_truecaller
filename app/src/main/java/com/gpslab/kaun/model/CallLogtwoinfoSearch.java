package com.gpslab.kaun.model;

public class CallLogtwoinfoSearch {
    private String name;
    private String number;
    private String callType;

    private String simtype;
    private String date;
    private long duration;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

