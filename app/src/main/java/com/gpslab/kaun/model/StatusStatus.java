package com.gpslab.kaun.model;

import java.util.ArrayList;

public class StatusStatus {
    private String mobile;
    private String imageUrl;
    private String name;
    private String time;
    private String type;
    private Long duration;
    private ArrayList<String> story_image = new ArrayList<>();

    private ArrayList<String> story_video = new ArrayList<>();



    public StatusStatus(String imageUrl, String name, String time, ArrayList<String> story_image, ArrayList<String> story_video, String type,String mobile, Long duration) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.time = time;
        this.story_image = story_image;
        this.story_video = story_video;
        this.type = type;
        this.mobile = mobile;
        this.duration = duration;
    }

    public StatusStatus(String name, String time) {
        this.name = name;
        this.time = time;
    }


    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getStory_video() {
        return story_video;
    }

    public void setStory_video(ArrayList<String> story_video) {
        this.story_video = story_video;
    }

    public ArrayList<String> getStory_image() {
        return story_image;
    }

    public void setStory_image(ArrayList<String> story_image) {
        this.story_image = story_image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
