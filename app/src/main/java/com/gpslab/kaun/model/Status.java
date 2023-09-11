package com.gpslab.kaun.model;

public class Status {
    private String imageUrl;
    private String name;
    private String time;
    private String story_image;

    public Status(String imageUrl, String name, String time, String story_image) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.time = time;
        this.story_image = story_image;
    }

    public Status(String name, String time) {
        this.name = name;
        this.time = time;
    }


    public String getStory_image() {
        return story_image;
    }

    public void setStory_image(String story_image) {
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
