package com.gpslab.kaun.model;

public class MenuItem {
    private final String name;
    private final String number;
    private final String callType;
    private final String date;
    private final long duration;
    private final String read;
    private final String simType;
    private final String image;
    private final String api;
    private final String ids;
    private final String datenew;

    public MenuItem(String name, String number, String callType, String date,
                    long duration,String read, String simType,String image, String api, String ids,String datenew) {
        this.name = name;
        this.number = number;
        this.callType = callType;
        this.date = date;
        this.duration = duration;
        this.read = read;
        this.simType = simType;
        this.image = image;
        this.api = api;
        this.ids = ids;
        this.datenew = datenew;
    }

    public String getDatenew() {
        return datenew;
    }

    public String getIds() {
        return ids;
    }

    public String getRead() {
        return read;
    }

    public String getApi() {
        return api;
    }

    public String getImage() {
        return image;
    }

    public String getSimType(){
        return simType;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getCallType() {
        return callType;
    }

    public long getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }
}
