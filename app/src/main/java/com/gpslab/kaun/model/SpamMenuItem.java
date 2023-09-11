package com.gpslab.kaun.model;

public class SpamMenuItem {
    private String type;
    private String read;
    private String address;
    private String body;
    private long date;
    private String number;
    private String name;
    private String image;
    public String callType;
    public String Spamcount;


    public SpamMenuItem(String type, String read, String address, String body, long date, String number, String name, String image,String callType,String spamcount) {
        this.type = type;
        this.read = read;
        this.address = address;
        this.body = body;
        this.date = date;
        this.number = number;
        this.name = name;
        this.image = image;
        this.callType = callType;
        this.Spamcount = spamcount;
    }


    public String getSpamcount() {
        return Spamcount;
    }

    public String getCallType() {
        return callType;
    }

    public String getType() {
        return type;
    }

    public String getRead() {
        return read;
    }

    public String getAddress() {
        return address;
    }

    public String getBody() {
        return body;
    }

    public long getDate() {
        return date;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
