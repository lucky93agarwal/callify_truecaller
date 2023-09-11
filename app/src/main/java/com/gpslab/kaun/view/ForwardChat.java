package com.gpslab.kaun.view;

import java.util.ArrayList;

public class ForwardChat {

    private String image;
    private String name;
    private String lastMessage;
    private String lastMessageTime;

    private String id;


    private ArrayList<String> realPathList;




    public ForwardChat() {
    }


    public ForwardChat(String name, String lastMessage, String lastMessageTime) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public ForwardChat(String image, String name, String lastMessage, String lastMessageTime, String id, ArrayList<String> realPathList) {
        this.image = image;
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.id = id;
        this.realPathList = realPathList;
    }


    public ArrayList<String> getRealPathList() {
        return realPathList;
    }

    public void setRealPathList(ArrayList<String> realPathList) {
        this.realPathList = realPathList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
