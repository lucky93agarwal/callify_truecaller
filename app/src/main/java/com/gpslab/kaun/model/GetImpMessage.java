package com.gpslab.kaun.model;

public class GetImpMessage {
    public String address;
    public String _id;
    public String body;

    public String dates;

    public String read;
    public String image;


    public GetImpMessage(String address, String _id, String body, String dates, String read, String image) {
        this.address = address;
        this._id = _id;
        this.body = body;
        this.dates = dates;
        this.read = read;
        this.image = image;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
