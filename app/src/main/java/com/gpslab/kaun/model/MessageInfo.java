package com.gpslab.kaun.model;

import androidx.databinding.BaseObservable;

public class MessageInfo  extends BaseObservable {

    private String id;
    private String read;
    private String address;

    private String body;
    private String date;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
