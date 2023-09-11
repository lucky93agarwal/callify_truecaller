package com.gpslab.kaun.model;

import java.util.ArrayList;

public class GetSeeStatusVideo {
    private String Statusid;
    private ArrayList<GetUserStatusListVideo> userlist;


    public String getStatusid() {
        return Statusid;
    }

    public void setStatusid(String statusid) {
        Statusid = statusid;
    }

    public ArrayList<GetUserStatusListVideo> getUserlist() {
        return userlist;
    }

    public void setUserlist(ArrayList<GetUserStatusListVideo> userlist) {
        this.userlist = userlist;
    }
}
