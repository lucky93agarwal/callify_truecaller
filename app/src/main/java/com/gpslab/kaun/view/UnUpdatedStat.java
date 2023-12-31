package com.gpslab.kaun.view;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UnUpdatedStat extends RealmObject {
    @PrimaryKey
    private String messageId;
    private String myUid;
    //state to update (received,read)
    private int statToBeUpdated;

    public UnUpdatedStat() {
    }

    public UnUpdatedStat(String messageId, String myUid, int statToBeUpdated) {
        this.messageId = messageId;
        this.myUid = myUid;
        this.statToBeUpdated = statToBeUpdated;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getStatToBeUpdated() {
        return statToBeUpdated;
    }

    public void setStatToBeUpdated(int statToBeUpdated) {
        this.statToBeUpdated = statToBeUpdated;
    }

    public String getMyUid() {
        return myUid;
    }

    public void setMyUid(String myUid) {
        this.myUid = myUid;
    }
}


