package com.gpslab.kaun.view;

import io.realm.RealmObject;

public class JobId extends RealmObject {
    private String id;
    private int jobId;
    private boolean isVoiceMessage;

    public JobId() {
    }

    public JobId(String id) {
        this.id = id;
        jobId = RealmHelper.getInstance().generateJobId();
    }

    public JobId(String id, boolean isVoiceMessage) {
        this.id = id;
        jobId = RealmHelper.getInstance().generateJobId();
        this.isVoiceMessage = isVoiceMessage;
    }


    public int getJobId() {
        return jobId;
    }

    public String getId() {
        return id;
    }

    public boolean isVoiceMessage() {
        return isVoiceMessage;
    }
}


