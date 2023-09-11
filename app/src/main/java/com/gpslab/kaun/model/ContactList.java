package com.gpslab.kaun.model;

import java.util.ArrayList;

public interface ContactList {

    void additem(String id, String name, String number, String numbertwo, String email);


    ArrayList<GetContectData> getItems();
}
