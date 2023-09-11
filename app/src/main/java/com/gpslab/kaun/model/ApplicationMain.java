package com.gpslab.kaun.model;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

public class ApplicationMain extends Application implements ContactList {

    private static Context context;
    public ArrayList<GetContectData> productList= new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        productList = new ArrayList<>();
    }

    public static Context getMyContext(){
        return context;
    }

    @Override
    public void additem(String id, String name, String number, String numbertwo, String email) {

    }

    @Override
    public ArrayList<GetContectData> getItems() {
        return productList;
    }
}
